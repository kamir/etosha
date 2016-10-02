#!/bin/sh
 
#-------------------------------------------------------------------------------
# Prepare the Etosha project after cloning from Github
#-------------------------------------------------------------------------------

cd ..

git lfs pull
#
# install external libraries into the local mvn-repo
#-------------------------------------------------------------------------------
mvn install:install-file -Durl=file:///home/cloudera/workspace/etosha/extlibs -Dfile=/home/cloudera/workspace/etosha/extlibs/sshxcute-1.0.jar -DgroupId=net.neoremind.sshxcute -DartifactId=sshxcute -Dpackaging=jar -Dversion=1.0
mvn install:install-file -Durl=file:///home/cloudera/workspace/etosha/extlibs -Dfile=/home/cloudera/workspace/etosha/extlibs/json-lib.jar -DgroupId=net.sf.json-lib -DartifactId=json-lib -Dpackaging=jar -Dversion=2.4
mvn install:install-file -Durl=file:///home/cloudera/workspace/etosha/extlibs -Dfile=/home/cloudera/workspace/etosha/extlibs/gephi-spanning-tree-0.1.0.jar -DgroupId=org.cloudera -DartifactId=gephi-spanning-tree -Dpackaging=jar -Dversion=0.1.0
mvn install:install-file -Durl=file:///home/cloudera/workspace/etosha/extlibs -Dfile=/home/cloudera/workspace/etosha/extlibs/gephi-toolkit-0.1.0.jar -DgroupId=org.cloudera -DartifactId=gephi-toolkit -Dpackaging=jar -Dversion=0.1.0

#-------------------
#
# SETUP Java 8
#
#    Source: http://tecadmin.net/install-java-8-on-centos-rhel-and-fedora/
#
#-------------------

#wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" "http://download.oracle.com/otn-pub/java/jdk/8u60-b27/jdk-8u60-linux-x64.tar.gz"
#tar xzf jdk-8u60-linux-x64.tar.gz
#sudo mv jdk1.8.0_60 /opt/
#sudo ln -s /opt/jdk1.8.0_60 /opt/java8

#sudo alternatives --install /usr/bin/java java /opt/java8/bin/java 2
#sudo alternatives --config java

#sudo alternatives --install /usr/bin/jar jar /opt/java8/bin/jar 2
#sudo alternatives --install /usr/bin/javac javac /opt/java8/bin/javac 2

#sudo alternatives --set jar /opt/java8/bin/jar
#sudo alternatives --set javac /opt/java8/bin/javac 

#export JAVA_HOME=/opt/java8

#
#  If not yet available we install Gnuplot, wget, and Ruby (used for Fuseki clients and
#  HBase shell).
#
sudo yum install gnuplot
sudo yum install wget
sudo yum install ruby







