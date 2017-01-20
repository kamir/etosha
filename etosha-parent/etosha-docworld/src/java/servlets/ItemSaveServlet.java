package servlets;

import admin.DocTabAdmin; 
import data.io.adapter.ObjectEncoder;
import hadoop.cache.doc.HBaseDocWorld;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author kamir
 */
public class ItemSaveServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet itemSaveServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>ItemSaveServlet : " + request.getContextPath() + "</h1><hr>");

            Properties props = new Properties();
            
            for ( String field : DocTabAdmin.FIELDS ) {
                String k = field;
                String v = request.getParameter(field);
                
                out.println("<p>" + k + " # " + v + "</p><br>");
                
                if ( v != null ) 
                    props.put(k, v);
            }
            
            ServletContext context = getServletContext();

            Object odw = context.getAttribute( "dw" );
            
            HBaseDocWorld dw = null;
            
            if ( odw == null ) {
                
                dw = new HBaseDocWorld();
                dw.init();
                
                context.setAttribute( "dw", dw );
                odw = dw;
            }
            else {
                
                dw = (HBaseDocWorld) odw;
                
            }
            String id =  request.getParameter("id");
            
            String isValid = request.getParameter("cbValid");
            String isPublic = request.getParameter("cbPublic");
            String isAnswered = request.getParameter("cbAnswered");
            
            if ( isValid == null ) isValid = "0";
            else isValid = "1";
            
            if ( isPublic == null ) isPublic = "0";
            else isPublic = "1";
            
            if ( isAnswered == null ) isAnswered = "0";
            else isAnswered = "1";

            props.put("isValid" , isValid );
            props.put("isAnswered" , isAnswered );
            props.put("isPublic" , isPublic );
            
            boolean back = dw.putDocStructured( id , ObjectEncoder.toString(props) );
            
            if ( back )
                out.println("<hr>(" + id + ") was stored successfully. </hr>");
            else 
                out.println("<hr>" + id + " could not be stored. Sorry, but I did my best!</hr>");
            
            out.println("<hr>(" + id + ") " + isValid + " # " + isAnswered + " # " +  isPublic + "</hr>");
            
            
            out.println("<hr> [ <a href='/DocWorld/v1/editItem.jsp?id=" + id + "'>EDIT</a>]"+
                        " [ <a href='/DocWorld/v1/showItem.jsp?id=" + id + "'>SHOW</a>]"+
                        " [<a href='http://training09.mtv.cloudera.com:8888/search/?collection=20000004'>SEARCH</a>] <hr>\n" );
            
            out.println("</html>");
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
