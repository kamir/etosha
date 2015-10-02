#!/bin/bash
CMD=start
WEBSERVER_PORT=8083

#JAVA_HOME=/usr/java/jdk1.7.0_67-cloudera

JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.7.0_80.jdk/Contents/Home

case $CMD in
  (start)
    echo "Starting the Jetty web server on port [$WEBSERVER_PORT]"
    echo ">>> deploy: DocWorld from /GITHUB/DocWorld/dist/DocWorld.war"

    exec $JAVA_HOME/bin/java -jar jetty-runner-8.1.9.v20130131.jar --port $WEBSERVER_PORT --path /DocWorld /GITHUB/DocWorld/dist/DocWorld.war 
    # --lib /GITHUB/opt/cloudera/parcels/jars
    
    ;;
  (*)
    echo "Don't understand [$CMD]"
    ;;
esac

# tested on trainng07 and training03
# each time the same error ...
