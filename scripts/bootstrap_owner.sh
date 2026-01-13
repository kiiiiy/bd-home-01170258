#!/usr/bin/env bash
set -euo pipefail

need(){ command -v "$1" >/dev/null 2>&1; }

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

echo "==> bd-home-template (monorepo) owner bootstrap"
echo "==> Repo root: $ROOT_DIR"
echo

# 0) tools
echo "==> Install base tools"
sudo apt update -y
sudo apt install -y git curl unzip

if ! need gh; then
  sudo apt install -y gh
fi

if ! need fly; then
  curl -L https://fly.io/install.sh | sh
  export FLYCTL_INSTALL="$HOME/.fly"
  export PATH="$FLYCTL_INSTALL/bin:$PATH"
fi

# 1) auth
echo "==> Auth check (1-time browser login if needed)"
gh auth status >/dev/null 2>&1 || { echo "!! Run: gh auth login"; exit 1; }
fly auth whoami >/dev/null 2>&1 || { echo "!! Run: fly auth login"; exit 1; }

OWNER="$(gh api user -q .login)"
echo "==> GitHub owner: $OWNER"

# 운영 레포 이름(충돌 방지: 기본은 bd-home-<owner>)
OPS_REPO="${OPS_REPO:-bd-home-${OWNER}}"
VISIBILITY="${VISIBILITY:-public}"   # Pages 쓰려면 public 권장

echo "==> Target 운영 repo: ${OWNER}/${OPS_REPO} (${VISIBILITY})"

# 2) create/repoint repo
if ! gh repo view "${OWNER}/${OPS_REPO}" >/dev/null 2>&1; then
  gh repo create "${OPS_REPO}" --"${VISIBILITY}"
fi

git remote remove origin 2>/dev/null || true
git remote add origin "https://github.com/${OWNER}/${OPS_REPO}.git"

# 템플릿을 운영 레포로 밀어넣기(초기화 의도)
git add -A || true
git commit -m "chore: init from bd-home-template" || true
git branch -M main
git push -u origin main --force-with-lease

# 3) backend/frontend 세팅
echo
echo "==> Setup backend & frontend inside this repo"
./scripts/bootstrap_backend.sh
./scripts/bootstrap_frontend.sh

# 4) push changes (generated backend/frontend + workflows)
echo
echo "==> Push generated contents"
git add -A
git commit -m "chore: generate backend/frontend & workflows" || true
git push

echo
echo "✅ Done."
echo "- Backend deploy: GitHub Actions -> Fly"
echo "- Frontend deploy: GitHub Actions -> GitHub Pages"
echo
echo "Check:"
echo "  GitHub repo: https://github.com/${OWNER}/${OPS_REPO}"
echo "  (After first backend deploy) Fly URL: https://bd-homepage-${OWNER}.fly.dev/api/health"
echo "  Pages URL: https://${OWNER}.github.io/${OPS_REPO}/"
