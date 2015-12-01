#!/bin/bash

export FUSEKI_HOME=../main/apache-jena-fuseki-2.3.0/

CMD=$1
WEBSERVER_PORT=3030
MODEL_FILE=../main/data/model.ttl

#
# Here we use a parameter to define a location of the modelfile.
#
MODEL_FILE=$2

######################################
#
#  Etosha Triple Collector Service
#

case $CMD in
  (start)
    clear
    echo "FUSEKI_HOME: $FUSEKI_HOME"
    echo "Starting the Fuseki-Server on port [$WEBSERVER_PORT] (default: 3030)"
    echo "        PORT=$WEBSERVER_PORT"
    echo "  MODEL_FILE=$MODEL_FILE"

    exec ../main/apache-jena-fuseki-2.3.0/fuseki-server --file=$MODEL_FILE --port=$WEBSERVER_PORT /EC

    ;;
  (list)
    ../main/apache-jena-fuseki-2.3.0/bin/s-query --service http://localhost:$WEBSERVER_PORT/EC/query 'SELECT * {?s ?p ?o}'
    ;;
  (*)
    echo "Don't understand [$CMD]"
    ;;
esac