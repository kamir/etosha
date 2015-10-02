/*
 * Stellt ein TemplateFile dar, mit dem dann mittels Velocitie oder
 * XSLT diverse Resultate erzeugt werden können.
 * 
 */
package data;

import java.io.Serializable;

/**
 *
 * @author kamir
 */
public class TemplateFile implements Serializable {
    
      public static final String default_LATEX_FILE = "\\begin{figure}[t]\n"+
"\\flushright\\includegraphics[width=0.83\\hsize]{Fig.2.eps}\n"+
"\\caption{Wikipedia edit-event data for the three representative\n"+
"articles previously presented in Fig.~1. The plots show the number of"+
"edits per\nday; the articles were edited 270, 163, and 157 times in total"+
"during the\nrecording period.}\n"+
"\\label{Fig:2}\n"+
"\\end{figure}\n";
      
      public static final String default_GNUPLOT_FILE = "# set terminal png "+
              "transparent nocrop enhanced font arial 8 size 420,320\n"+ 
"# set output 'surface2.1.png'\n"+
"set dummy u,v\n"+
"set key bmargin center horizontal Right noreverse enhanced autotitles nobox\n"+
"set parametric\n"+
"set view 45, 50, 1, 1\n"+
"set isosamples 50, 10\n"+
"set hidden3d offset 1 trianglepattern 3 undefined 1 altdiagonal bentover\n"+
"set ztics border in scale 1,0.5 nomirror norotate  offset character 0, 0, 0 -1.00000,0.25,1.00000\n"+
"set title 'Parametric Sphere' \n"+
"set urange [ -1.57080 : 1.57080 ] noreverse nowriteback\n"+
"set vrange [ 0.00000 : 6.28319 ] noreverse nowriteback\n"+
"splot cos(u)*cos(v),cos(u)*sin(v),sin(u\n";

      
    // ------------------------------------------------------------------------  
      
    public String content;
    
    TemplateFile( String text ) {
        this.content = text; 
    }
    
}
