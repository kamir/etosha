#!/bin/bash

CMD=start
WEBSERVER_PORT=9090

#
# Where is JAVA_HOME
#
JAVA_HOME=/home/mirko.kaempf/runtime/j8

# This script runs Jetty on a port defined by $WEBSERVER_PORT 
#
# The "DocWorld" Wiki Application is available on path /DocWorld.
#
case $CMD in
  (start)
    echo "Starting the Jetty web server on port [$WEBSERVER_PORT]"
    echo ">>> deploy: DocWorld"

    exec $JAVA_HOME/bin/java -jar jetty-runner-8.1.9.v20130131.jar --port $WEBSERVER_PORT --path /DocWorld DocWorld.war
    
    ;;
  (*)
    echo "Don't understand [$CMD]"
    ;;
esac