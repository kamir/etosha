echo # kompiliere eine einfache Seite ...

echo %1
echo %2
echo %3

del %2\%1.dvi /F /Q
del %2\%1.ps /F /Q
del %2\%1.pdf /F /Q

cd /TEST/P0/default

dir 

latex %2\%1.tex
dvips -q* %2%1 
ps2pdf %2%1.ps


 




