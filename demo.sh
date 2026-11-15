#!/usr/bin/env bash
# Runs one of the tui examples on this terminal: ./demo.sh [name]; with no name, the address book.
# Names: address-book, inventory, hello, form, lists, menus, scrolling.
# Keys arrive one by one, unechoed, Enter as \r; the mouse is on while it runs. Ctrl+Q (or Ctrl+C) quits; the
# terminal, mouse included, is restored on exit.
set -euo pipefail
cd "$(dirname "$0")"
mvn -q -pl tui-demo -am package -DskipTests
saved=$(stty -g)
trap 'stty "$saved"; printf "\033[?1006l\033[?1002l\033[?1000l\033[?25h\033[0m\033[2J\033[H"' EXIT
stty -icanon -echo -icrnl -ixon
java -cp tui-api/target/classes:tui-text/target/classes:tui-demo/target/classes com.fpetrola.tui.demo.Demo "$@" $(stty size)
