#!/usr/bin/env bash
set -euo pipefail

SERVICE=""

usage() {
  echo "Usage: $0 <service>" >&2
  echo "Services: company-service | user-service | activity-service | action-service | rating-service | data-generator-service" >&2
}

if [[ $# -lt 1 ]]; then
  usage
  exit 1
fi

SERVICE="$1"

case "$SERVICE" in
  company-service|user-service|activity-service|action-service|rating-service|data-generator-service)
    ;;
  *)
    echo "Invalid service: $SERVICE" >&2
    usage
    exit 1
    ;;
esac

# Ensure .env exists; if not, generate in compose mode
if [[ ! -f .env ]]; then
  if [[ -f .env.example ]]; then
    echo "No .env found. Copying from .env.example."
    cp .env.example .env
  else
    echo "Missing .env and .env.example. Please create one." >&2
    exit 1
  fi
fi

# Export variables from .env
set -a
source ./.env
set +a

# Calculate server port env var per service
case "$SERVICE" in
  company-service)
    SERVER_PORT=${COMPANY_SERVICE_PORT:-8082}
    ;;
  user-service)
    SERVER_PORT=${USER_SERVICE_PORT:-8085}
    ;;
  activity-service)
    SERVER_PORT=${ACTIVITY_SERVICE_PORT:-8086}
    ;;
  action-service)
    SERVER_PORT=${ACTION_SERVICE_PORT:-8081}
    ;;
  rating-service)
    SERVER_PORT=${RATING_SERVICE_PORT:-8084}
    ;;
  data-generator-service)
    SERVER_PORT=${DATA_GENERATOR_SERVICE_PORT:-8083}
    ;;
esac

export SERVER_PORT

echo "Running $SERVICE on port $SERVER_PORT"
./gradlew ":apps:${SERVICE}:bootRun" -x test