#!/usr/bin/env bash
fifo="$HOME/.cache/cerco-firma.fifo"
[ -p "$fifo" ] || mkfifo "$fifo"
cd "$(dirname "$0")" || exit 1
while true; do
  cat "$fifo"
  script -qec "mvn -q cerco:approve" /dev/null <<< yes
done
