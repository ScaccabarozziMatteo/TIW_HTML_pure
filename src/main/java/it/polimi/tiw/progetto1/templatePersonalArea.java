package it.polimi.tiw.progetto1;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet(name = "personalArea", urlPatterns = "/PersonalArea")
    public class templatePersonalArea extends HttpServlet {

    private TemplateEngine templateEngine;
    private Connection connection = null;

        @Override
        public void init() throws UnavailableException {
            ServletContext servletContext = getServletContext();
            ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
            templateResolver.setTemplateMode(TemplateMode.HTML);
            templateResolver.setSuffix(".html");
            this.templateEngine = new TemplateEngine();
            this.templateEngine.setTemplateResolver(templateResolver);

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
        }


        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

            HttpSession session = request.getSession();
            String strLogin = (String) session.getAttribute("login");

            if (strLogin != null) {
                    String name = null;

                    String query = "SELECT name FROM dbtest.customers WHERE email LIKE '" + strLogin + "'";
                    ResultSet result = null;
                    PreparedStatement pstatement = null;


                    try {
                    pstatement = connection.prepareStatement(query);
                    result = pstatement.executeQuery();
                    while (result.next()) {
                        name = result.getString("name");
                    }

                    } catch (SQLException e) {
                        System.out.println("Errore query SQL");
                        //response.sendError(404);
                    }

                    finally {
                        try {
                            assert result != null;
                            result.close();
                            pstatement.close();
                        } catch (Exception e1) {
                            System.out.println("Impossibile chiudere la connessione con il database");
                            response.sendError(404);
                        }
                    }



                String path = "/WEB-INF/AreaPersonale";
                ServletContext servletContext = getServletContext();
                final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
                ctx.setVariable("name", name);
                templateEngine.process(path, ctx, response.getWriter());
            }
            else {
                response.sendRedirect("index.html");
            }
        }
    }
