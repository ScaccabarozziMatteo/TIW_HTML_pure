package it.polimi.tiw.progetto1;

import it.polimi.tiw.progetto1.DAO.CustomerDAO;
import it.polimi.tiw.progetto1.templates.templateSignUp;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "signupServlet", value = "/signupServlet")
public class signup extends HttpServlet {

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
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        CustomerDAO customerDAO = new CustomerDAO(connection);

        String name, surname, password, password2, address, email, sex;
        String query;
        templateSignUp templateSignUp = new templateSignUp();
        name = request.getParameter("name");
        surname = request.getParameter("surname");
        password = request.getParameter("password");
        password2 = request.getParameter("password2");
        address = request.getParameter("address");
        email = request.getParameter("email");
        sex = request.getParameter("sex");

        try {
            if (!name.equals("") && !password.equals("") && !password2.equals("") && !address.equals("") && !email.equals("")) {
                if (email.length() > 45 || name.length() > 45 || surname.length() > 45 || address.length() > 45 || password.length() > 45 || (!sex.equals("male") && !sex.equals("female") && !sex.equals("notdefine"))) {
                    session.setAttribute("codeResult", 3);
                    response.sendRedirect("signup");
                }
                else if (customerDAO.findIfExistCustomer(email)) {
                    session.setAttribute("codeResult", 2);
                    response.sendRedirect("signup");
                }
                else if (password.equals(password2)) {
                    customerDAO.createCustomer(email, name, surname, address, password, sex);
                    session.setAttribute("codeResult", 1);
                    response.sendRedirect("signup");
                }
                else if (!password.equals(password2)) {
                    session.setAttribute("codeResult", 4);
                    response.sendRedirect("signup");

                }
            } else {
                session.setAttribute("codeResult", 5);
                response.sendRedirect("signup");
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
