NOW=$(date +"%m-%d-%Y-%h-%s")
FILE="tg2.$NOW"
echo "# work on file: $FILE"
mkdir temp

export NRLINES="100000"
export EXAMPLEJAR="/usr/lib/hadoop-mapreduce/hadoop-mapreduce-examples.jar"

ssh training@elephant hadoop jar $EXAMPLEJAR teragen $NRLINES $FILE

export OPTS1="-Dio.sort.mb=256 -Dio.sort.factor=20 -Dmapreduce.reduce.slowstart.completed.maps=0.90"
ssh training@elephant hadoop jar $EXAMPLEJAR terasort $FILE "$FILE.sorted.A" $OPTS1 

export OPTS2="-Dio.sort.mb=5 -Dio.sort.factor=2 -Dmapreduce.reduce.slowstart.completed.maps=0.01"
ssh training@elephant hadoop jar $EXAMPLEJAR terasort $FILE "$FILE.sorted.B" $OPTS2 

ssh training@elephant "mkdir /home/training/temp" 
ssh training@elephant "hadoop jar training_materials/hdt.jar --hdfsdir /user/training/$FILE.sorted.A 2> /home/training/temp/job.stat.A.dat"
ssh training@elephant "hadoop jar training_materials/hdt.jar --hdfsdir /user/training/$FILE.sorted.B 2> /home/training/temp/job.stat.B.dat"

scp training@elephant:/home/training/temp/*.dat ./temp/


