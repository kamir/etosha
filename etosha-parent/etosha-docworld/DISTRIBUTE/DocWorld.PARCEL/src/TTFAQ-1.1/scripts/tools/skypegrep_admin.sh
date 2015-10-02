#!/bin/bash
CMD=$1
TMP_FILE=temp.xml
 
#
# where is the data to import?
#
SGS=http://training03.sjc.cloudera.com/faq/results?action=stream&id=0

#
# Clean up first ...
#
rm $TMP_FILE


case $CMD in
  (import)
    echo "Import the data from SkypeGrep service on port [$SGS]"
    exec wget $SGS -O $TMP_FILE
	curl http://training07.sjc.cloudera.com:8983/solr/FAQMails02Collection_shard1_replica1/update --data-binary @$TMP_FILE -H 'Content-type:text/xml; charset=utf-8'
    ;;
  (*)
    echo "Don't understand [$CMD]"
    ;;
esac


