#!/bin/bash

# Install a link to this script in /usr/sbin
#
# cd /usr/sbin
# sudo ln -s /home/training/workspace/etosha/etosha-parent/bin/ecl.sh ecl
# sudo chmod 777 ecl

MAIN=org.etosha.tools.admin.ContextLoggerTool

echo "> MAIN-Class:  $MAIN"
echo "> arg1:  $1"
echo "> arg2:  $2"
echo ">"

sudo hadoop jar /GITHUB/ETOSHA.WS/etosha/etosha-parent/etosha-core/target/etosha-core-0.9.0-SNAPSHOT-jar-with-dependencies.jar $1 $2

