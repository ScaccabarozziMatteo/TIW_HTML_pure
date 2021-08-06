package it.polimi.tiw.progetto1;

import it.polimi.tiw.progetto1.DAO.CustomerDAO;
import it.polimi.tiw.progetto1.DAO.ProductDAO;
import it.polimi.tiw.progetto1.DAO.Ship_PolicyDAO;
import it.polimi.tiw.progetto1.DAO.SupplierDAO;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet(name = "insertProduct", urlPatterns = "/insertProduct")
public class InsertProduct extends HttpServlet {

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

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();

        String nameProduct, categoryProduct, descriptionProduct;
        int codeProduct = -1, quantity = 0;
        nameProduct = request.getParameter("nameProduct");
        categoryProduct = request.getParameter("categoryProduct");
        descriptionProduct = request.getParameter("descriptionProd");
        try {
            codeProduct = Integer.parseInt(request.getParameter("code"));
            quantity = Integer.parseInt(request.getParameter("quantityProduct"));
        }
        catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Inserisci un valore corretto per la quantità!");
        }

        if (nameProduct != null && !nameProduct.equals("")  && (quantity > 0) && descriptionProduct != null && !descriptionProduct.equals("") && categoryProduct != null && !categoryProduct.equals("")) {
            ProductDAO productDAO = new ProductDAO(connection);
            Ship_PolicyDAO ship_policyDAO = new Ship_PolicyDAO(connection);

            if (nameProduct.length() < 46 && descriptionProduct.length() < 301 && categoryProduct.length() < 46) {

                try {
                    if (productDAO.ifExistsProduct(codeProduct)) {
                        // update product quantity


                    } else {
                        // create new product
                        productDAO.createProduct(codeProduct, nameProduct, descriptionProduct, categoryProduct, );

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } else {
                ServletContext servletContext = getServletContext();
                servletContext.setAttribute("errorParameters", "Parametri non validi!");
                response.sendRedirect("index.html");
            }
        }
        else {
            ServletContext servletContext = getServletContext();
            servletContext.setAttribute("errorNoData", "Parametri mancanti!");
            response.sendRedirect("PersonalAreaSupplier");
        }
    }


}
