#!/bin/bash
#
# This tool creates a simple DataSet profile by using the
# DataSetStatisticsJob which extends the the DataSetInspector.
#

G=D
package=org.etosha.tools.statistics
MAIN=DataSetStatisticsJob

echo Tool : $package.$MAIN
echo IN Base    : $1
echo IN Folder  : $2
echo OUT  : /user/training/testdata/dsstats/$2.$G.$3

hadoop jar /home/training/bin/ci.jar $package.$MAIN -libjars /home/training/bin/jsoup.jar $1$2 /user/training/testdata/dsstats/$2.$G.$3