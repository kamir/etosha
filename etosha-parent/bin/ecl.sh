#!/bin/bash

# Install a link to this script in /usr/sbin
#
# cd /usr/sbin
# sudo ln -s /home/training/workspace/etosha/etosha-parent/bin/ecl.sh ecl
# sudo chmod 777 ecl

MAIN=org.etosha.cmd.EtoshaContextLogger

echo "> MAIN-Class:  $MAIN"
echo "> arg1:  $1"
echo "> arg2:  $2"

sudo hadoop jar /home/cloudera/workspace/etosha/etosha-parent/etosha-core/target/etosha-core-0.9.0-SNAPSHOT-jar-with-dependencies-filtered2.jar $1 $2

