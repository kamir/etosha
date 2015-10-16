echo # kompiliere eine einfache Seite ...

echo %1
echo %2
echo %3

del %2\%1.dvi /F /Q
del %2\%1.ps /F /Q
del %2\%1.pdf /F /Q

C:
cd \
cd TEST\P0\default

dir

latex %2\%1.tex
dvips -q* %2%1 
ps2pdf %2%1.ps

echo # svn commit ...

rem "C:\Program Files\Subversion\bin\svn.exe" add %2%1.pdf
rem "C:\Program Files\Subversion\bin\svn.exe" commit %2 -m "autocommit after compile"

mkdir G:\DEV\BITOCEAN\bitoceanTB\build\web\DATA\%3

copy %2%1.pdf G:\DEV\BITOCEAN\bitoceanTB\build\web\DATA\%3\%1.pdf

 




