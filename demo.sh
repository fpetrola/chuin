#!/usr/bin/env bash
# Runs the tui demo on this terminal: keys arrive one by one, unechoed, Enter as \r.
# Ctrl+Q (or Ctrl+C) quits; the terminal is restored on exit.
set -euo pipefail
cd "$(dirname "$0")"
mvn -q -pl tui-demo -am package -DskipTests
saved=$(stty -g)
trap 'stty "$saved"; printf "\033[0m\033[2J\033[H"' EXIT
stty -icanon -echo -icrnl -ixon
java -cp tui-api/target/classes:tui-text/target/classes:tui-demo/target/classes com.fpetrola.tui.demo.Demo
