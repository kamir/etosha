 #!/bin/sh

#-------------------------------------------------------------------------------
#  Prep the target environment
#-------------------------------------------------------------------------------
WSHOST=topedo
USER=root

#-------------------------------------------------------------------------------
# Build the Etosha toolbox ...
#-------------------------------------------------------------------------------

#mvn -Phadoop_2 clean compile install assembly:single
mvn clean compile install package assembly:single

ssh $USER@WSHOST sudo mkdir /home/$USER/etosha
ssh $USER@WSHOST sudo mkdir /home/$USER/etosha
ssh $USER@WSHOST sudo mkdir /etc/etosha





