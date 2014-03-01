#!/bin/bash

# Install a link to this script in /usr/sbin
#
# cd /usr/sbin
# sudo ln -s /home/training/workspace/etosha/etosha-parent/bin/ecl.sh ecl
# sudo chmod 777 ecl

MAIN=org.etosha.tools.admin.ContextLoggerTool

#echo "> MAIN:  $MAIN"
#echo "> arg1:  $1"
#echo "> arg2:  $2"
#echo ">"

hadoop jar /home/training/workspace/etosha/etosha-parent/etosha-core/target/Etosha-Core-0.2.0-SNAPSHOT-jar-with-dependencies.jar $1 $2
