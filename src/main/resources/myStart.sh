#!/bin/bash
echo "before"
/app/bin/start.sh
exec  "$@"