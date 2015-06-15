gnuplot -e "id='A'" tasksovertime.pg
gnuplot -e "id='B'" tasksovertime.pg

firefox tasksovertime.A.svg &
firefox tasksovertime.B.svg &


