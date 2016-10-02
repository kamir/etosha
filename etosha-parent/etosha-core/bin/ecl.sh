#!/bin/bash

# Install the script in /usr/sbin
#
# cd /usr/sbin
# sudo ln -s /home/training/workspace/EtoshaCore/bin/ecl.sh ecl
#

ETOSHA_PATH=/home/cloudera/workspace/etosha/etosha-parent/etosha-core/target/
UBER_JAR=etosha-core-0.9.0-SNAPSHOT-jar-with-dependencies-filtered.jar

MAIN=org.etosha.cmd.EtoshaContextLogger

echo "> MAIN:  $MAIN"
echo "> arg1:  $1"
echo "> arg2:  $2"

hadoop jar $ETOSHA_PATH/$UBER_JAR $MAIN $1 $2 
