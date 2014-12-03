# Install the hadoop client via homebrew
#   - requieres XCode 5.x
#

MAIN=org.etosha.tools.admin.ContextLoggerTool

ETOSHA_PATH=/users/kamir/projects/ETOSHA.WS/etosha/etosha-parent/etosha-core/target/
ARCHIVE=Etosha-Core-0.3.0-SNAPSHOT-jar-with-dependencies.jar

echo "> MAIN:  $MAIN"
echo "> arg1:  $1"
echo "> arg2:  $2"

# zip -d $ETOSHA_PATH/$ARCHIVE META-INF/LICENSE

/users/webex/homebrew/bin/hadoop jar $ETOSHA_PATH/$ARCHIVE $MAIN $1 $2 
