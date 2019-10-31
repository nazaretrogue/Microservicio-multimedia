package ApiRest;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import ServicioMensajeria.Sender;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * @author nazaret
 */
@WebServlet(urlPatterns = {"/img"})
public class API extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Procesa imagen</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet API at " + request.getContextPath() + "</h1>");
            out.println("<h1>Imagen lista para ser procesada</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    protected void processResponse(HttpServletResponse response, File f) 
            throws ServletException, IOException {
        response.setContentType("image/jpeg");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Imagen procesada</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<img src='"+f.getAbsolutePath()+"' alt='fotico'>");
            out.println("</body>");
            out.println("</html>");
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
        String nombre = (String)request.getParameter("img");
        
        if(nombre != null){
            File f = new File("imagenes");
            File[] matching_files = f.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.startsWith("archivo_procesado_");
                }
            });
            
            if(matching_files.length == 0)
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            
            File a_buscar = new File(nombre);
            boolean existe = Arrays.asList(matching_files).contains(a_buscar);
            
            if(!existe)
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            
            else{
                processResponse(response, a_buscar);
            }
        }
            
        else
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
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
        String tipo = request.getContentType();
        InputStream imagen_full;
        
        //multipart/form-data
        if(tipo.equals("image/jpeg")){
            imagen_full = request.getInputStream();
            
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] buffer = new byte[409600];
            
            try{
                int linea;
                while((linea = imagen_full.read(buffer)) != -1){
                    os.write(buffer, 0, linea);
                }
            } catch(Exception e){
                System.err.println(e);
            }
            
            byte[] bytes = os.toByteArray();
            
            try{
                Sender s = new Sender(bytes);
                processRequest(request, response);
            } catch(Exception e){
                System.err.println(e);
            }
        }
        
        else
            response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
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
