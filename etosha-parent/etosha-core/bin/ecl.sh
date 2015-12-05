#!/bin/bash

# Install the script in /usr/sbin
#
# cd /usr/sbin
# sudo ln -s /home/training/workspace/EtoshaCore/bin/ecl.sh ecl
#

ETOSHA_PATH=/GITHUB/ETOSHA.WS/etosha/etosha-parent/etosha-core/target/
ARCHIVE=Etosha-Core-0.7.0-SNAPSHOT-jar-with-dependencies.jar

MAIN=org.etosha.cmd.EtoshaContextLogger

echo "> MAIN:  $MAIN"
echo "> arg1:  $1"
echo "> arg2:  $2"
echo ">"

hadoop jar $ETOSHA_PATH/$ARCHIVE $MAIN $1 $2 
