#!/usr/bin/env bash
set -euo pipefail

MODE="compose" # compose or local
OUTPUT=".env"
OVERWRITE=false

usage() {
  echo "Usage: $0 [--mode compose|local] [--output .env] [--overwrite]" >&2
}

while [[ $# -gt 0 ]]; do
  case "$1" in
    --mode)
      MODE=${2:-compose}
      shift 2
      ;;
    --output)
      OUTPUT=${2:-.env}
      shift 2
      ;;
    --overwrite)
      OVERWRITE=true
      shift 1
      ;;
    -h|--help)
      usage
      exit 0
      ;;
    *)
      echo "Unknown argument: $1" >&2
      usage
      exit 1
      ;;
  esac
done

if [[ -f "$OUTPUT" && "$OVERWRITE" != true ]];
then
  echo "$OUTPUT already exists. Use --overwrite to replace it." >&2
  exit 0
fi

if [[ ! -f .env.example ]]; then
  echo "Missing .env.example at project root." >&2
  exit 1
fi

cp .env.example "$OUTPUT"

if [[ "$MODE" == "local" ]]; then
  # Replace service hosts with localhost for local runs
  sed -i '' 's#r2dbc:mysql://db:#r2dbc:mysql://localhost:#' "$OUTPUT"
  sed -i '' 's#http://ollama:#http://localhost:#' "$OUTPUT"
  sed -i '' 's#http://company-service:#http://localhost:#' "$OUTPUT"
  sed -i '' 's#http://user-service:#http://localhost:#' "$OUTPUT"
  sed -i '' 's#http://activity-service:#http://localhost:#' "$OUTPUT"
  sed -i '' 's#http://action-service:#http://localhost:#' "$OUTPUT"
fi

echo "Generated $OUTPUT for mode: $MODE"