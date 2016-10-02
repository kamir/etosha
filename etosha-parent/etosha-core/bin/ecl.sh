#!/bin/bash
 
MAIN=org.etosha.cmd.EtoshaContextLogger

echo "> MAIN-Class :  $MAIN"
echo "> JAVA_HOME  :  $JAVA_HOME"
echo "> ARGS       :  [$1, $2]"
hadoop jar /home/cloudera/workspace/etosha/etosha-parent/etosha-core/target/etosha-core-0.9.0-SNAPSHOT-jar-with-dependencies-filtered.jar $1 $2
