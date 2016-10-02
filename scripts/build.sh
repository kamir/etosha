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

cp ./../etc/etosha-site.xml /home/$USER/etc/etosha/etosha-site.xml
cp ./../etc/etosha-site.xml /etc/etosha/etosha-site.xml

cd /usr/sbin
sudo ln -s /home/cloudera/workspace/etosha/etosha-parent/bin/ecl.sh ecl
sudo chmod 777 ecl

#-------------------------------------------------------------------------------
# deploy the Etosha CLI ...
#-------------------------------------------------------------------------------
#ssh $USER@WSHOST sudo mkdir /home/$USER/etc
#ssh $USER@WSHOST sudo mkdir /home/$USER/etc/etosha 
#ssh $USER@WSHOST sudo mkdir /etc/etosha





