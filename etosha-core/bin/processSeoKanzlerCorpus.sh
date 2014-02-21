#!/bin/bash

package=org.etosha.tools.corpusprofiler

#
# this one loads a sequence file which has FN + HTML as (k,v) pair inside.
#
G=C
MAIN=WordContextInspectorTool2

echo Tool : $package.$MAIN
echo IN   : /user/training/testdata/corpus.compare/seokanzler/corpus.SEOKanzler_$1_$2.seq
echo OUT  : /user/training/testdata/domainindex/$1_$2.$G

hadoop jar /home/training/bin/ci.jar $package.$MAIN -libjars /home/training/bin/jsoup.jar /user/training/testdata/corpus.compare/corpus.SEOKanzler_$1_$2.seq /user/training/testdata/domainindex/$1_$2.$G.$3