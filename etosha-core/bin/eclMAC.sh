
# Install the hadoop client via homebrew
#   - requieres XCode 5.x
#

MAIN=org.etosha.tools.admin.ContextLoggerTool

echo "> MAIN:  $MAIN"
echo "> arg1:  $1"
echo "> arg2:  $2"
echo ">"

/users/webex/homebrew/bin/hadoop jar /users/webex/Desktop/EtoshaCore/bin/etosha-0.1.jar $MAIN $1 $2 
