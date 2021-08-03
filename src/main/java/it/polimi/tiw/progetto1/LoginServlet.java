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

        String emailCustomer, passwordCustomer, emailSupplier, passwordSupplier;
        emailCustomer = request.getParameter("emailCustomer");
        passwordCustomer = request.getParameter("passwordCustomer");
        emailSupplier = request.getParameter("emailSupplier");
        passwordSupplier = request.getParameter("passwordSupplier");

        String query1 = "SELECT email, password FROM dbtest.customers";
        String query2 = "SELECT email, password FROM dbtest.suppliers";
        ResultSet result1 = null, result2 = null;
        PreparedStatement pstatement1 = null, pstatement2 = null;


        try {
            if (emailCustomer != null && !emailCustomer.equals("") || passwordCustomer != null && !passwordCustomer.equals("")) {
                pstatement1 = connection.prepareStatement(query1);
                result1 = pstatement1.executeQuery();
                while (result1.next()) {
                    if (emailCustomer.equals(result1.getString("email")) && passwordCustomer.equals(result1.getString("password"))) {
                        Cookie c1 = new Cookie("email", emailCustomer);     //the username and password are encrypted
                        Cookie c2 = new Cookie("password", passwordCustomer);
                        c1.setMaxAge(20000);
                        c2.setMaxAge(20000);
                        session.setAttribute("login", emailCustomer);
                        response.addCookie(c1);
                        response.addCookie(c2); //sends cookies to the browser
                        response.sendRedirect("PersonalArea");
                    }
                    else {
                        ServletContext servletContext = getServletContext();
                        WebContext webContext = new WebContext(request, response, servletContext, request.getLocale());
                        webContext.setVariable("errorLoginCustomer", "Credenziali errate o account non esistente!");
                        templateEngine.process("/index", webContext, response.getWriter());
                    }
                }
            }
            else if (emailSupplier != null && !emailSupplier.equals("")) {
                pstatement2 = connection.prepareStatement(query2);
                result2 = pstatement2.executeQuery();
                while (result2.next()) {
                    if (emailSupplier.equals(result2.getString("email")) && emailSupplier.equals(result2.getString("password"))) {
                        Cookie c1 = new Cookie("email", emailSupplier);     //the username and password are encrypted
                        Cookie c2 = new Cookie("password", passwordSupplier);
                        c1.setMaxAge(20000);
                        c2.setMaxAge(20000);
                        session.setAttribute("login", emailSupplier);
                        response.addCookie(c1);
                        response.addCookie(c2); //sends cookies to the browser
                        response.sendRedirect("PersonalArea");
                    }
                    else {
                        ServletContext servletContext = getServletContext();
                        WebContext webContext = new WebContext(request, response, servletContext, request.getLocale());
                        webContext.setVariable("errorLoginSupplier", "Credenziali errate o account non esistente!");
                        templateEngine.process("/index", webContext, response.getWriter());
                    }
                }
            }
            else if (emailCustomer == null || passwordCustomer == null && emailSupplier == null || passwordSupplier == null) {
                ServletContext servletContext = getServletContext();
                WebContext webContext = new WebContext(request, response, servletContext, request.getLocale());
                webContext.setVariable("errorNoCredential", "Credenziali non valide!");
                templateEngine.process("/index", webContext, response.getWriter());

            }
        } catch (SQLException e) {
            response.sendError(404);
        } finally {
            try {
                assert result1 != null;
                result1.close();
                assert result2 != null;
                result2.close();
                pstatement1.close();
                pstatement2.close();
            } catch (Exception e1) {
                System.out.println("Errore chiusura connessioni con il DB");
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