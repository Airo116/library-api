#!/bin/bash
# wait-for-it.sh

set -e

host="$1"
shift
cmd="$@"

until nc -z "$host" 5432; do
  >&2 echo "Waiting for database..."
  sleep 1
done

<&2 echo "Database is up - executing command"
exec $cmd

chmod +x wait-for-it.sh