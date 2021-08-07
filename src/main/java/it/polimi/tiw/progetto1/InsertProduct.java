package it.polimi.tiw.progetto1;

import it.polimi.tiw.progetto1.DAO.ProductDAO;
import it.polimi.tiw.progetto1.DAO.Ship_PolicyDAO;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
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
        String supplierCode = (String) session.getAttribute("supplierCode");

        String nameProduct, categoryProduct, descriptionProduct;
        InputStream photo;
        int codeProduct = -1, quantity = 0;
        nameProduct = request.getParameter("nameProduct");
        categoryProduct = request.getParameter("categoryProduct");
        descriptionProduct = request.getParameter("descriptionProd");
        photo = request.getInputStream();

        try {
            ImageIO.read(photo);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Inserisci un'immagine valida!");
        }

        try {
            codeProduct = Integer.parseInt(request.getParameter("code"));
            quantity = Integer.parseInt(request.getParameter("quantityProduct"));
        }
        catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Inserisci un valore corretto per la quantità!");
        }

        if (nameProduct != null && !nameProduct.equals("")  && (quantity > 0) && descriptionProduct != null && !descriptionProduct.equals("") && categoryProduct != null && !categoryProduct.equals("") && photo
         != null) {
            ProductDAO productDAO = new ProductDAO(connection);
            Ship_PolicyDAO ship_policyDAO = new Ship_PolicyDAO(connection);

            if (nameProduct.length() < 46 && descriptionProduct.length() < 301 && categoryProduct.length() < 46) {

                try {
                    if (productDAO.ifExistsProduct(codeProduct)) {
                        // update product quantity
                        productDAO.addQuantityProduct(codeProduct, quantity, supplierCode);
                        session.getServletContext().setAttribute("codeResult", 2);
                        response.sendRedirect("PersonalAreaSupplier");

                    } else {
                        // create new product
                        productDAO.createProduct(codeProduct, nameProduct, descriptionProduct, categoryProduct, photo, quantity, supplierCode);
                        session.getServletContext().setAttribute("codeResult", 1);
                        response.sendRedirect("PersonalAreaSupplier");

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
