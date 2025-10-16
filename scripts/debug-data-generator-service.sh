#!/usr/bin/env bash
set -euo pipefail

if [[ ! -f .env ]]; then
  echo "Missing .env. Run ./scripts/bootstrap-env.sh or copy .env.example." >&2
  exit 1
fi

set -a
source ./.env
set +a

# DB envs
export SPRING_R2DBC_URL="r2dbc:mysql://localhost:3306/${MYSQL_DATABASE}"
export SPRING_R2DBC_USERNAME="${MYSQL_USER}"
export SPRING_R2DBC_PASSWORD="${MYSQL_PASSWORD}"
export SPRING_SQL_INIT_MODE="${SPRING_SQL_INIT_MODE:-never}"

# Ollama / AI envs
export SPRING_AI_OLLAMA_BASE_URL="http://localhost:11434/"
export SPRING_AI_OLLAMA_CHAT_OPTIONS_MODEL="${SPRING_AI_OLLAMA_CHAT_OPTIONS_MODEL:-${OLLAMA_MODEL:-llama3}}"
export SPRING_AI_OLLAMA_CHAT_OPTIONS_TEMPERATURE="${SPRING_AI_OLLAMA_CHAT_OPTIONS_TEMPERATURE:-0.0}"

# Client base URLs for local services
export CLIENTS_COMPANY_BASE_URL="http://localhost:${COMPANY_SERVICE_PORT}"
export CLIENTS_USER_BASE_URL="http://localhost:${USER_SERVICE_PORT}"
export CLIENTS_ACTIVITY_BASE_URL="http://localhost:${ACTIVITY_SERVICE_PORT}"
export CLIENTS_ACTION_BASE_URL="http://localhost:${ACTION_SERVICE_PORT}"
export CLIENTS_USER_ACTIVITY_BASE_URL="http://localhost:${ACTIVITY_SERVICE_PORT}"

export SERVER_PORT="${DATA_GENERATOR_SERVICE_PORT:-8083}"

./gradlew :apps:data-generator-service:bootRun -x test