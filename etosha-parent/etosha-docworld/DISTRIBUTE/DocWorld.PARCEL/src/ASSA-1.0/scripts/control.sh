#!/bin/bash
CMD=$1


######################################
#
#  Example from Spark-Docu
#
# ./bin/spark-submit --name "My app" --master local[4] --conf spark.shuffle.spill=false
#    --conf "spark.executor.extraJavaOptions=-XX:+PrintGCDetails -XX:+PrintGCTimeStamps" myApp.jar
#
#

case $CMD in
  (start)
    clear
    echo "Starting the web server on port [$WEBSERVER_PORT]"
    echo "  --conf spark.ui.port=$WEBSERVER_PORT"

#    exec spark-submit --name "ASSA Service" --conf spark.ui.port=$WEBSERVER_PORT --class $CLASS MyJAR.jar 
    ;;
  (*)
    echo "Don't understand [$CMD]"
    ;;
esac