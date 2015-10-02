echo # kompiliere ein gnuplotskript einfache Seite ...

echo %1
echo %2
echo %3


C:
cd \
cd TEST\P0\default

"C:\Program Files\gnuplot\bin\wgnuplot.exe" chart.cmd

 

echo # svn commit ...

rem "C:\Program Files\Subversion\bin\svn.exe" add %2%1.pdf
rem "C:\Program Files\Subversion\bin\svn.exe" commit %2 -m "autocommit after compile pdf"

rem mkdir G:\DEV\BITOCEAN\ChartEditor\build\web\DATA\%3
 
copy %2%1.png G:\DEV\BITOCEAN\bitoceanTB\build\web\DATA\%3\%1.png

 




