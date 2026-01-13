#!/usr/bin/env bash
set -euo pipefail

need(){ command -v "$1" >/dev/null 2>&1; }
ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

echo "=============================="
echo " Backend bootstrap (monorepo)"
echo "=============================="

# tools
sudo apt update -y
sudo apt install -y curl unzip

if ! need gh; then
  sudo apt install -y gh
fi
if ! need fly; then
  curl -L https://fly.io/install.sh | sh
  export FLYCTL_INSTALL="$HOME/.fly"
  export PATH="$FLYCTL_INSTALL/bin:$PATH"
fi

gh auth status >/dev/null 2>&1 || { echo "!! Run: gh auth login"; exit 1; }
fly auth whoami >/dev/null 2>&1 || { echo "!! Run: fly auth login"; exit 1; }

OWNER="$(gh api user -q .login)"
FLY_APP="${FLY_APP:-bd-homepage-${OWNER}}"
JAVA_VERSION="${JAVA_VERSION:-17}"
PKG="${PKG:-com.bd.homepage}"

echo "==> Owner: $OWNER"
echo "==> Fly app: $FLY_APP"
echo "==> Recreate backend/"

rm -rf backend
mkdir -p backend

# Spring Boot project (no bootVersion pin)
curl -fsSL "https://start.spring.io/starter.zip" \
  -G \
  -d type=gradle-project \
  -d language=java \
  -d javaVersion="$JAVA_VERSION" \
  -d groupId=com.bd \
  -d artifactId=homepage \
  -d name=homepage \
  -d packageName="$PKG" \
  -d dependencies=web,actuator \
  -o /tmp/sb.zip

unzip -q /tmp/sb.zip -d backend

# Minimal API
cat > backend/src/main/java/com/bd/homepage/HealthController.java <<'EOF'
package com.bd.homepage;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
  @GetMapping("/api/health")
  public String health() {
    return "ok";
  }
}
EOF

# CORS (Pages uses https://<owner>.github.io and repo path)
cat > backend/src/main/java/com/bd/homepage/CorsConfig.java <<EOF
package com.bd.homepage;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**")
      .allowedOrigins("https://${OWNER}.github.io")
      .allowedMethods("GET","POST","PUT","DELETE","OPTIONS");
  }
}
EOF

# Stable Dockerfile: jar is built in CI
cat > backend/Dockerfile <<'EOF'
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY build/libs/*.jar app.jar
EXPOSE 8080
ENV PORT=8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
EOF

# Fly config
cat > backend/fly.toml <<EOF
app = "${FLY_APP}"
primary_region = "nrt"

[build]
  dockerfile = "Dockerfile"

[http_service]
  internal_port = 8080
  force_https = true
  auto_stop_machines = "stop"
  auto_start_machines = true
  min_machines_running = 0
EOF

# Ensure Fly app exists
if ! fly apps list | awk '{print $1}' | grep -qx "${FLY_APP}"; then
  fly apps create "${FLY_APP}"
fi

# Create deploy token and store as repo secret
TOKEN="$(fly tokens create deploy --app "${FLY_APP}" | tail -n 1 | tr -d '\r')"
if [ -z "$TOKEN" ]; then
  echo "!! Failed to create Fly token"
  exit 1
fi
gh secret set FLY_API_TOKEN --body "$TOKEN"

echo "✅ backend/ created and Fly secret set."
