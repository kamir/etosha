 #!/bin/sh

#-------------------------------------------------------------------------------
#  Prep the target environment
#-------------------------------------------------------------------------------
WSHOST=127.0.0.1
USER=cloudera

#-------------------------------------------------------------------------------
# Build the Etosha toolbox ...
#-------------------------------------------------------------------------------
cd ..
cd etosha-parent
mvn clean compile install package assembly:single


#-------------------------------------------------------------------------------
# deploy the Etosha CLI ...
#-------------------------------------------------------------------------------
ssh $USER@WSHOST sudo mkdir /home/$USER/etosha
ssh $USER@WSHOST sudo mkdir /home/$USER/etosha/bin
ssh $USER@WSHOST sudo mkdir /etc/etosha

# scp etosha-site.xml
# scp UBER_JAR
# scp script
 




