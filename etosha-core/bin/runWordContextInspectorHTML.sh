#!/bin/bash

package=org.etosha.tools.corpusprofiler

#
# right now it extracts only links from the HTML file.
#
G=B
MAIN=WordContextInspector4HTMLTool

echo Tool: $package.$MAIN
echo IN:  /user/training/testdata/corpus.compare/$1
echo OUT: /user/training/testdata/domainindex/$1.$G.$2

hadoop jar /home/training/bin/ci.jar $package.$MAIN -libjars /home/training/bin/jsoup.jar /user/training/testdata/corpus.compare/$1 /user/training/testdata/domainindex/$1.$G.$2