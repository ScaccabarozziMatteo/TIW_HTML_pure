package it.polimi.tiw.progetto1.Controllers;

import it.polimi.tiw.progetto1.DAO.CustomerDAO;
import it.polimi.tiw.progetto1.DAO.SupplierDAO;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;

@WebServlet(name = "signupServlet", value = "/signupServlet")
public class Signup extends HttpServlet {

    private Connection connection = null;

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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(true);

        if (request.getParameter("form").equals("customer")) {
            CustomerDAO customerDAO = new CustomerDAO(connection);

            String name, surname, password, password2, address, email, sex;

            name = request.getParameter("name");
            surname = request.getParameter("surname");
            password = request.getParameter("password");
            password2 = request.getParameter("password2");
            address = request.getParameter("address");
            email = request.getParameter("email");
            sex = request.getParameter("sex");

            try {
                if (!name.equals("") && !surname.equals("") && !password.equals("") && !password2.equals("") && !address.equals("") && !email.equals("") && !sex.equals("")) {
                    if (email.length() > 45 || name.length() > 45 || surname.length() > 45 || address.length() > 45 || password.length() > 45 || (!sex.equals("male") && !sex.equals("female") && !sex.equals("notdefine"))) {
                        session.getServletContext().setAttribute("codeResult", 3);
                        response.sendRedirect("signupCustomer");
                    } else if (customerDAO.findIfExistCustomer(email)) {
                        session.getServletContext().setAttribute("codeResult", 2);
                        response.sendRedirect("signupCustomer");
                    } else if (password.equals(password2)) {
                        customerDAO.createCustomer(email, name, surname, address, password, sex);
                        session.getServletContext().setAttribute("codeResult", 1);
                        response.sendRedirect("signupCustomer");
                    } else if (!password.equals(password2)) {
                        session.getServletContext().setAttribute("codeResult", 4);
                        response.sendRedirect("signupCustomer");

                    }
                } else {
                    session.getServletContext().setAttribute("codeResult", 5);
                    response.sendRedirect("signupCustomer");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else if (request.getParameter("form").equals("supplier")) {
            SupplierDAO supplierDAO = new SupplierDAO(connection);

            String code, name, password, password2;

            code = request.getParameter("code");
            name = request.getParameter("name");
            password = request.getParameter("password");
            password2 = request.getParameter("password2");


            try {
                if (!code.equals("") && !name.equals("") && !password.equals("") && !password2.equals("")) {
                    if (code.length() > 45 || name.length() > 45 || password.length() > 45) {
                        session.getServletContext().setAttribute("codeResult", 3);
                        response.sendRedirect("signupSupplier");
                    } else if (supplierDAO.findIfExistSupplier(code)) {
                        session.getServletContext().setAttribute("codeResult", 2);
                        response.sendRedirect("signupSupplier");
                    } else if (password.equals(password2)) {
                        supplierDAO.createSupplier(code, name, password);
                        session.getServletContext().setAttribute("codeResult", 1);
                        response.sendRedirect("signupSupplier");
                    } else if (!password.equals(password2)) {
                        session.getServletContext().setAttribute("codeResult", 4);
                        response.sendRedirect("signupSupplier");

                    }
                } else {
                    session.getServletContext().setAttribute("codeResult", 5);
                    response.sendRedirect("signupSupplier");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void destroy() {
        try {
            if (connection != null){
                connection.close();
            }
        } catch (SQLException ignored) {

        }
    }
}
