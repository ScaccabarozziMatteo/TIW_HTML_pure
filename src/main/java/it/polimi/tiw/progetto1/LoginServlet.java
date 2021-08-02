package it.polimi.tiw.progetto1;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import java.io.*;
import java.sql.*;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "login", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    private Connection connection = null;
    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {

        try {
            ServletContext context = getServletContext();
            String driver = context.getInitParameter("dbDriver");
            String url = context.getInitParameter("dbUrl");
            String user = context.getInitParameter("dbUser");
            String password = context.getInitParameter("dbPassword");
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);

        } catch (ClassNotFoundException e) {
            throw new UnavailableException("Impossibile caricare dbDriver");
        } catch (SQLException e) {
            throw new UnavailableException("Impossibile connettersi");
        }

        ServletContext servletContext = getServletContext();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setSuffix(".html");
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        HttpSession session = request.getSession();

        String email, password;
        email = request.getParameter("email");
        password = request.getParameter("password");

        String query = "SELECT email, password FROM dbtest.accounts";
        ResultSet result = null;
        PreparedStatement pstatement = null;


        try {
            pstatement = connection.prepareStatement(query);
            result = pstatement.executeQuery();
            while (result.next()) {
                if(email.equals(result.getString("email")) && password.equals(result.getString("password"))) {
                    Cookie c1 = new Cookie("email", email);     //the username and password are encrypted
                    Cookie c2 = new Cookie("password", password);
                    c1.setMaxAge(20000);
                    c2.setMaxAge(20000);
                    session.setAttribute("login", email);
                    response.addCookie(c1);
                    response.addCookie(c2); //sends cookies to the browser
                    response.sendRedirect("PersonalArea");
                }
                else if (email.equals("") || password.equals("")) {
                    ServletContext servletContext = getServletContext();
                    WebContext webContext = new WebContext(request, response, servletContext, request.getLocale());
                    webContext.setVariable("errorNoCredential", "Credenziali non valide!");
                    templateEngine.process("/index", webContext, response.getWriter());

                }
                else {
                    ServletContext servletContext = getServletContext();
                    WebContext webContext = new WebContext(request, response, servletContext, request.getLocale());
                    webContext.setVariable("errorLogin", "Credenziali errate o account non esistente!");
                    templateEngine.process("/index", webContext, response.getWriter());
                }
            }

        } catch (SQLException e) {
            response.sendError(404);
        }

        finally {
            try {
                assert result != null;
                result.close();
                pstatement.close();
            } catch (Exception e1) {
                response.sendError(404);
            }
        }

    }

    public void destroy() {
        try {
            if (connection != null){
                connection.close();
            }
        } catch (SQLException ignored) {

        }
    }

}