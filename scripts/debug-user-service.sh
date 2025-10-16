#!/usr/bin/env bash
set -euo pipefail

if [[ ! -f .env ]]; then
  echo "Missing .env. Run ./scripts/bootstrap-env.sh or copy .env.example." >&2
  exit 1
fi

set -a
source ./.env
set +a

export SPRING_R2DBC_URL="r2dbc:mysql://localhost:3306/${MYSQL_DATABASE}"
export SPRING_R2DBC_USERNAME="${MYSQL_USER}"
export SPRING_R2DBC_PASSWORD="${MYSQL_PASSWORD}"
export SPRING_SQL_INIT_MODE="${SPRING_SQL_INIT_MODE:-never}"
export SERVER_PORT="${USER_SERVICE_PORT:-8085}"

./gradlew :apps:user-service:bootRun -x test