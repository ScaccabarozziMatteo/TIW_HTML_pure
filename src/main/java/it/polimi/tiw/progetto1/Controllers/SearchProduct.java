package it.polimi.tiw.progetto1.Controllers;

import it.polimi.tiw.progetto1.Beans.Product;
import it.polimi.tiw.progetto1.DAO.ProductDAO;
import org.thymeleaf.context.WebContext;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "SearchProduct", value = "/SearchProduct")
public class SearchProduct extends HttpServlet {

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String strLogin = (String) session.getAttribute("emailCustomer");

        if (strLogin != null) {
            String nameProduct = request.getParameter("search");

            if (nameProduct != null && !nameProduct.equals("") && nameProduct.length() < 301) {
                ProductDAO productDAO = new ProductDAO(connection);
                List<Product> products;
                try {
                    products = productDAO.getProductsFromSearchTab(nameProduct);
                    ServletContext servletContext = getServletContext();
                    servletContext.setAttribute("searchedProducts", products);
                    response.sendRedirect("PersonalAreaCustomer?id=2");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }


            } else
                response.sendError(400, "Parametri non validi");
        }
        else
            response.sendRedirect("index.html");
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



