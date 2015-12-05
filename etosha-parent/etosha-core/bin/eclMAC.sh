# First we have to install the hadoop client via homebrew
#
#   > brew install hadoop
#   - requieres XCode 5.x
#-------------------------------------------------------------------------------

MAIN=org.etosha.tools.admin.EtoshaContextLogger

ETOSHA_PATH=/GITHUB/ETOSHA.WS/etosha/etosha-parent/etosha-core/target
ARCHIVE=Etosha-Core-0.7.0-SNAPSHOT-jar-with-dependencies.jar

echo "> MAIN:  $MAIN"
echo "> arg1:  $1"
echo "> arg2:  $2"

#
zip -d $ETOSHA_PATH/$ARCHIVE META-INF/license
#
hadoop jar $ETOSHA_PATH/$ARCHIVE $MAIN $1 $2 
