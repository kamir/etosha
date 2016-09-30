# First we have to install the hadoop client via homebrew
#
#   > brew install hadoop
#   - requieres XCode 5.x
#-------------------------------------------------------------------------------

MAIN=org.etosha.tools.admin.EtoshaContextLogger

ETOSHA_PATH=/GITHUB/ETOSHA.WS/etosha/etosha-parent/etosha-core/target

ARCHIVE=etosha-core-0.9.0-SNAPSHOT-jar-with-dependencies-filtered2.jar
ARCHIVE=etosha-core-0.9.0-SNAPSHOT-shaded.jar

echo "> MAIN:  $MAIN"
echo "> arg1:  $1"
echo "> arg2:  $2"


hadoop jar $ETOSHA_PATH/$ARCHIVE $MAIN $1 $2 
