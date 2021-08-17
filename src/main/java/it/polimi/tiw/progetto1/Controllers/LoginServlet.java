package it.polimi.tiw.progetto1.Controllers;

import it.polimi.tiw.progetto1.Beans.Customer;
import it.polimi.tiw.progetto1.Beans.Supplier;
import it.polimi.tiw.progetto1.DAO.CustomerDAO;
import it.polimi.tiw.progetto1.DAO.SupplierDAO;

import java.io.*;
import java.sql.*;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "login", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    private Connection connection = null;
    private List<Customer> customers = null;
    private List<Supplier> suppliers = null;

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
        throws IOException {
        HttpSession session = request.getSession();
        ServletContext servletContext = getServletContext();
        servletContext.setAttribute("updateOrders", "T");

        String emailCustomer, passwordCustomer, codeSupplier, passwordSupplier;
        emailCustomer = request.getParameter("emailCustomer");
        passwordCustomer = request.getParameter("passwordCustomer");
        codeSupplier = request.getParameter("codeSupplier");
        passwordSupplier = request.getParameter("passwordSupplier");
            if (emailCustomer != null && !emailCustomer.equals("") && passwordCustomer != null && !passwordCustomer.equals("")) {
                CustomerDAO customerDAO = new CustomerDAO(connection);

                try {
                    Customer customer = customerDAO.getCustomer(emailCustomer, passwordCustomer);
                    if (customer != null) {
                        session.setAttribute("emailCustomer", emailCustomer);
                        session.setAttribute("name", customer.getName());
                        session.setAttribute("sex", customer.getSex());
                        response.sendRedirect("PersonalAreaCustomer");
                    }
                    else {
                        servletContext = getServletContext();
                        servletContext.setAttribute("errorLoginCustomer", "Credenziali errate o account non esistente!");
                        response.sendRedirect("index.html");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else if (codeSupplier != null && !codeSupplier.equals("") && passwordSupplier != null && !passwordSupplier.equals("")) {

                    SupplierDAO supplierDAO = new SupplierDAO(connection);

                try {
                    Supplier supplier = supplierDAO.getSupplier(codeSupplier, passwordSupplier);
                    if (supplier != null) {
                        session.setAttribute("supplierCode", codeSupplier);
                        session.setAttribute("name", supplier.getName());
                        response.sendRedirect("PersonalAreaSupplier");

                    }
                    else {
                        servletContext.setAttribute("errorLoginSupplier", "Credenziali errate o account non esistente!");
                        response.sendRedirect("index.html");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else if (emailCustomer == null || passwordCustomer == null && codeSupplier == null || passwordSupplier == null) {
                servletContext.setAttribute("errorNoCredential", "Credenziali non valide!");
                response.sendRedirect("index.html");
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