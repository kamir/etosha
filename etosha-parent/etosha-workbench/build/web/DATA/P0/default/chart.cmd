#set term postscript eps enhanced
#set output "chart.eps"

set term png enhanced transparent
set output "chart.png"
set key left top
set size 0.5,0.5
set xrange [0:9]
set yrange [0:4]
set xlabel "Energy [MeV]"
set ylabel "Cross Section [b]"
# for gnuplot ver.4
set style line 1 lt 1
set style line 2 lt 1 pt 7
plot [-18:248] sin(x/2),atan(x/5),cos(atan(x))
