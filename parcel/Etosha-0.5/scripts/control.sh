#!/bin/bash

################################
#
# Etosha Service Control Script
#-------------------------------
#    Version 0.5
#
#    mirko@cloudera.com
#
################################
CMD=$1
JAVA_HOME=/usr/java/jdk1.7.0_67-cloudera

################################################################################
#
# Environment: We need some additional services to setup Etosha
#
################################################################################
# This script runs Jetty on a port defined by $WEBSERVER_PORT 
#
# The "DocWorld" Wiki Application is available on path /DocWorld.
#
# The "Any23" conversion service is available on path /any23
#
# And "Fuseki 2.0" is running on an individual service.
# The port is: 3030 (default)
#
case $CMD in
  (start)
    echo "Starting the Jetty web server on port [$WEBSERVER_PORT]"
    echo ">>> deploy: DocWorld"
    echo ">>> deploy: Any23"
    exec $JAVA_HOME/bin/java -jar $CONF_DIR/scripts/jetty-runner-8.1.9.v20130131.jar --port $WEBSERVER_PORT --path /DocWorld $CONF_DIR/scripts/tools/docworld/DocWorld.war --path /any23 $CONF_DIR/scripts/tools/any23-1.1/apache-any23-service-1.1.war

#
#  RUN FUSEKI ....
#
    
    ;;
  (*)
    echo "Don't understand [$CMD]"
    ;;
esac