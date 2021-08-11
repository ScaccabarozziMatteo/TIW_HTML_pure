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
        String nameProduct = request.getParameter("search");

        if (nameProduct != null && !nameProduct.equals("")) {
            ProductDAO productDAO = new ProductDAO(connection);
            List<Product> products;
            try {
                products = productDAO.getProductsFromSearchTab(nameProduct);
                ServletContext servletContext = getServletContext();

                servletContext.setAttribute("showProducts",2);

                servletContext.setAttribute("searchedProducts",products);

                response.sendRedirect("PersonalAreaCustomer");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


        }
    }
}


