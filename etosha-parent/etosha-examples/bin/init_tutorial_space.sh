#!/bin/bash
NOW=$(date +"%m-%d-%Y-%h-%s")
FILE="tg1.$NOW"

echo "I like to jump to:" 
read jump
echo "Fine, will jump to $jump"

echo "" 
echo "" 
echo "This are all repositories:"
echo "" 
ls /etc/yum.repos.d
echo ""
read goOn

sudo rm /etc/yum.repos.d/centos-base.repo

echo "" 
echo "Please check the CDH repository settings!"
echo "" 
cat /etc/yum.repos.d/Cloudera*
echo "" 
read goOn

echo "" 
echo "1.) Install GIT"
echo "       http://tecadmin.net/how-to-upgrade-git-version-1-7-10-on-centos-6/"
echo " "
echo "* Do it        : 1" 
echo "* Jump to next : 0" 
read goOn

if [ $goOn -eq 1 ]
then
  sudo rpm -i 'http://packages.sw.be/rpmforge-release/rpmforge-release-0.5.3-1.el6.rf.i686.rpm'
  sudo rpm --import http://apt.sw.be/RPM-GPG-KEY.dag.txt
  sudo sed -i 's/enabled=0/enabled=1/g' /etc/yum.repos.d/rpmforge.repo
  sudo yum clean all
  sudo yum install git
  git config --global user.name "contrib"
  git config --global user.email thats@me.com
else
  echo 'JUMP'
fi

echo "" 
echo "2.) Checkout the tutorial workspace."
echo "" 
echo " "
echo "* Do it        : 1" 
echo "* Jump to next : 0" 
read goOn

if [ $goOn -eq 1 ]
then
  cd /home/training/GIT
  git clone https://github.com/kamir/hadoop-admin-and-developer-scripts
else
  echo 'JUMP'
fi

echo "" 
echo "3.) Install Hadoop libraries."
echo "" 
echo " "
echo "* Do it        : 1" 
echo "* Jump to next : 0" 
read goOn

if [ $goOn -eq 1 ]
then
  sudo yum install hadoop-client
  sudo yum install hadoop-0.20-mapreduce 
  sudo yum install hive
  sudo yum install pig
  sudo yum install hbase
  sudo yum install sqoop
else
  echo 'JUMP'
fi

echo "" 
echo "4.) Generate some TeraGen data."
echo "" 
echo " "
echo "* Do it        : 1" 
echo "* Jump to next : 0" 
read goOn

if [ $goOn -eq 1 ]
then
  ssh training@elephant hadoop jar /usr/lib/hadoop-0.20-mapreduce/hadoop-examples-2.0.0-mr1-cdh4.2.0.jar teragen 5000000 $FILE
else
  echo 'JUMP'
fi


echo "" 
echo "5.) Do a TeraSort"
echo "" 
echo " "
echo "* Do it        : 1" 
echo "* Jump to next : 0" 
read goOn

if [ $goOn -eq 1 ]
then
  ssh training@elephant hadoop jar /usr/lib/hadoop-0.20-mapreduce/hadoop-examples-2.0.0-mr1-cdh4.2.0.jar terasort $FILE "$FILE.sorted"
else
  echo 'JUMP'
fi

echo "" 
echo "6.) Install more nice tools ..."
echo "        ant, gnuplot 4.2, R, subversion"
echo " "
echo "* Do it        : 1" 
echo "* Jump to next : 0" 
read goOn

if [ $goOn -eq 1 ]
then
  sudo yum install ant
  sudo yum install gnuplot
  sudo yum install svn
  sudo yum install R
else
  echo 'JUMP'
fi


echo "" 
echo "7.) Now we should install Eclipse with Remote System Explorer."
echo ""
echo "       http://git.eclipse.org/c/tcf/org.eclipse.tcf.git/plain/docs/TCF%20Getting%20Started.html"
echo " "
echo "* Do it        : 1" 
echo "* Jump to next : 0" 
read goOn

if [ $goOn -eq 1 ]
then
  echo '...'
#  wget http://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/kepler/R/eclipse-reporting-kepler-R-linux-gtk.tar.gz&mirror_id=346
else
  echo 'JUMP'
fi


echo "" 
echo "8.) Install Maven"
echo " "
echo "* Do it        : 1" 
echo "* Jump to next : 0" 
read goOn

if [ $goOn -eq 1 ]
then
  java -version
  # 8.1. Download the current maven version from the prescribed repository:
  wget http://www.eng.lsu.edu/mirrors/apache/maven/maven-3/3.1.0/binaries/apache-maven-3.1.0-bin.tar.gz

  # 8.2. Extract the archive to the maven home directory: /usr/local/
  sudo cp apache-maven-3.1.0-bin.tar.gz /usr/local
  cd /usr/local
  sudo tar -zxvf apache-maven-3.1.0-bin.tar.gz

  # 8.3. Create a sym link..
  sudo ln -s apache-maven-3.1.0 maven

  # 8.4. Open with gedit ~/.bashrc file with and add the following lines to the end of the file,
  echo "export M2_HOME=/usr/local/apache-maven-3.1.0" >> /home/training/.bashrc
  echo 'export PATH=${M2_HOME}/bin:${PATH}' >> /home/training/.bashrc

  # 8.5. Execute the environment changes with the command,    
  /home/training/.bashrc
  chmod 777 /usr/local/apache-maven-3.1.0/bin/mvn
  /usr/local/apache-maven-3.1.0/bin/mvn -version
else
  echo 'JUMP'
fi


echo "" 
echo "9.) Install Gnuplot 4.6"
echo " "
echo "* Do it        : 1" 
echo "* Jump to next : 0" 
read goOn

if [ $goOn -eq 1 ]
then
  sudo wget http://downloads.sourceforge.net/project/gnuplot/gnuplot/4.6.3/gnuplot-4.6.3.tar.gz?r=http%3A%2F%2Fsourceforge.net%2Fprojects%2Fgnuplot%2Ffiles%2Fgnuplot%2F4.6.3%2F&ts=1374785888&use_mirror=superb-dca3
  echo "GoOn ...  >"
  read goOn
  
  sudo tar -zxvf gnuplot-4.6.3.tar.gz
  cd /home/training/gnuplot-4.6.3
  sudo ./configure
  sudo make
  sudo make install
  cd /home/training
  echo 'Done ...'
else
  echo 'JUMP'
fi


echo "" 
echo "10.) Deploy the sample code to the cluster."
echo "" 
echo " "
echo "* Do it        : 1" 
echo "* Jump to next : 0" 
read goOn

if [ $goOn -eq 1 ]
then
  cd /home/training/hadoop-admin-and-developer-scripts/java-tutorial/ews/hadoop-dev-tutorial/dist
  sudo ant
  ssh training@elephant "mkdir /home/training/temp"
  scp hdt.jar training@elephant:/home/training/training_materials
  ssh training@elephant "hadoop jar training_materials/hdt.jar --hdfsdir /user/training/$FILE.sorted 2> /home/training/temp/job.stat.dat"
  scp training@elephant:/home/training/temp/*.dat ./temp/
  gnuplot tasksovertime.pg
  firefox ./tasksovertime.svg &
  cd /home/training
else
  echo 'JUMP'
fi

mkdir /home/training/GIT

echo "" 
echo "11.) Setup the cloudera-twitter example."
echo "" 
echo " "
echo "* Do it        : 1" 
echo "* Jump to next : 0" 
read goOn

if [ $goOn -eq 1 ]
then
  cd /home/training/GIT
  git clone https://github.com/cloudera/cdh-twitter-example
  cd cdh-twitter-example
  cd flume-sources 
  /usr/local/apache-maven-3.1.0/bin/mvn package
  cd ..
  cd hive-serdes
  /usr/local/apache-maven-3.1.0/bin/mvn package

  cd /home/training
else
  echo 'JUMP'
fi

echo "" 
echo "12.) Setup the Apache-HDT project."
echo "" 
echo " "
echo "* Do it        : 1" 
echo "* Jump to next : 0" 
read goOn

if [ $goOn -eq 1 ]
then
  cd /home/training/GIT
  git clone https://git-wip-us.apache.org/repos/asf/incubator-hdt.git
  cd /home/training
else
  echo 'JUMP'
fi

echo "" 
echo "13.) Setup the Hadoop.TS project."
echo "" 
echo " "
echo "* Do it        : 1" 
echo "* Jump to next : 0" 
read goOn

if [ $goOn -eq 1 ]
then
  cd /home/training/GIT
  git clone git://github.com/kamir/Hadoop.TS.git
  cd /home/training
else
  echo 'JUMP'
fi

echo "" 
echo "14.) Setup the TSCache project."
echo "" 
echo " "
echo "* Do it        : 1" 
echo "* Jump to next : 0" 
read goOn

if [ $goOn -eq 1 ]
then
  cd /home/training/GIT
  git clone git://github.com/kamir/TSCache.git
  cd /home/training
else
  echo 'JUMP'
fi

echo "" 
echo "15.) Setup the Genix project."
echo "" 
echo " "
echo "* Do it        : 1" 
echo "* Jump to next : 0" 
read goOn

if [ $goOn -eq 1 ]
then
  cd /home/training/GIT
  git clone git://github.com/kamir/genix2.git
  cd /home/training
else
  echo 'JUMP'
fi








