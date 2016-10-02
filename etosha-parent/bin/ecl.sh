#!/bin/bash

# Install a link to this script in /usr/sbin
#
# cd /usr/sbin
# sudo ln -s /home/training/workspace/etosha/etosha-parent/bin/ecl.sh ecl
# sudo chmod 777 ecl

MAIN=org.etosha.cmd.EtoshaContextLogger

echo "> MAIN-Class :  $MAIN"
echo "> JAVA_HOME  :  $JAVA_HOME"
echo "> ARGS       :  [$1, $2]"
hadoop jar /home/cloudera/workspace/etosha/etosha-parent/etosha-core/target/etosha-core-0.9.0-SNAPSHOT-jar-with-dependencies-filtered.jar $1 $2


