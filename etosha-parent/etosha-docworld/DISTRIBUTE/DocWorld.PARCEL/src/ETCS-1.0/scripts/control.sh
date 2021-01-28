#!/bin/bash

export FUSEKI_HOME=../main/apache-jena-fuseki-2.3.0/

#
# Some default-settings (maybe they work not in any case)
#

CMD=$1
WEBSERVER_PORT=5051
MODEL_FILE=/GITHUB/ETOSHA.WS/etosha-workbench/fuseki-buffer/default.ttl
PART_FOLDER=/GITHUB/ETOSHA.WS/etosha-workbench/fuseki-buffer/facets







#
# Here we use a CLI parameter to define a location of the modelfile and the port.
#

MODEL_FILE=$2
WEBSERVER_PORT=$3

######################################
#
#  Etosha Triple Collector Service
#

case $CMD in
  (start)
    clear
    echo "$(pwd)/control.sh"
    echo " "
    echo "      FUSEKI_HOME: $FUSEKI_HOME"
    echo "             PORT: $WEBSERVER_PORT"
    echo "       MODEL_FILE: $MODEL_FILE"
    echo " PARTITION_FOLDER: $PART_FOLDER"
    echo " "
    echo ">>> Starting the Fuseki-Server on port [$WEBSERVER_PORT] (default: 3030)"

    exec ../main/apache-jena-fuseki-2.3.0/fuseki-server --file=$MODEL_FILE --update --port=$WEBSERVER_PORT /ETCS &

    sleep 2

    FILES=$PART_FOLDER/*
    for f in $FILES
    do
      echo "> LOAD GRAPH-PARTITION $f ..."
      # take action on each file. $f store current file name

      ../main/apache-jena-fuseki-2.3.0/bin/s-post http://localhost:$WEBSERVER_PORT/ETCS/data default $f
    done





    ;;
  (list)
    ../main/apache-jena-fuseki-2.3.0/bin/s-query --service http://localhost:$WEBSERVER_PORT/EC/query 'SELECT * {?s ?p ?o}'
    ;;
  (*)
    echo "Don't understand [$CMD]"
    ;;
esac
