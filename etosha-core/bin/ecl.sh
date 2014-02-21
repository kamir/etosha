#!/bin/bash

# Install the script in /usr/sbin
#
# cd /usr/sbin
# sudo ln -s /home/training/workspace/EtoshaCore/bin/ecl.sh ecl
#

MAIN=org.etosha.tools.admin.ContextLoggerTool

echo "> MAIN:  $MAIN"
echo "> arg1:  $1"
echo "> arg2:  $2"
echo ">"

hadoop jar /home/training/bin/etosha-0.1.jar $MAIN $1 $2 
