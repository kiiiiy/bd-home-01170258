#!/usr/bin/env bash
set -euo pipefail

need(){ command -v "$1" >/dev/null 2>&1; }
ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

echo "=============================="
echo " Frontend bootstrap (monorepo)"
echo "=============================="

sudo apt update -y
sudo apt install -y

if ! need gh; then
  sudo apt install -y gh
fi

gh auth status >/dev/null 2>&1 || { echo "!! Run: gh auth login"; exit 1; }

OWNER="$(gh api user -q .login)"
OPS_REPO="${OPS_REPO:-bd-home-${OWNER}}"
FLY_APP="${FLY_APP:-bd-homepage-${OWNER}}"

echo "==> Owner: $OWNER"
echo "==> Repo:  $OPS_REPO"
echo "==> Fly:   $FLY_APP"
echo "==> Recreate frontend/"

rm -rf frontend
mkdir -p frontend

cat > frontend/config.js <<EOF
window.API_BASE = "https://${FLY_APP}.fly.dev";
EOF

cat > frontend/app.js <<'EOF'
fetch(`${window.API_BASE}/api/health`)
  .then(r => r.text())
  .then(t => document.getElementById("out").innerText = t)
  .catch(e => document.getElementById("out").innerText = String(e));
EOF

cat > frontend/index.html <<'EOF'
<!doctype html>
<html lang="ko">
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>BD Homepage</title>
</head>
<body>
  <h1>BD Homepage</h1>
  <p>Backend health check:</p>
  <pre id="out">loading...</pre>
  <script src="./config.js"></script>
  <script src="./app.js"></script>
</body>
</html>
EOF

echo "✅ frontend/ created."
