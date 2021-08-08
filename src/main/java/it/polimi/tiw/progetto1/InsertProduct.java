package it.polimi.tiw.progetto1;

import it.polimi.tiw.progetto1.DAO.ProductDAO;

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
        int codeProduct = -1, quantity;
        nameProduct = request.getParameter("nameProduct");
        categoryProduct = request.getParameter("categoryProduct");
        descriptionProduct = request.getParameter("descriptionProd");
        photo = request.getInputStream();

        try {
            codeProduct = Integer.parseInt(request.getParameter("code"));
            quantity = Integer.parseInt(request.getParameter("quantityProduct"));
        }
        catch (NumberFormatException e) {
            quantity = 0;
        }

        try {
            ImageIO.read(photo);
        } catch (IllegalArgumentException e) {
            quantity = 0;
        }

        if (nameProduct != null && !nameProduct.equals("") && descriptionProduct != null && !descriptionProduct.equals("") && categoryProduct != null && !categoryProduct.equals("") && photo
         != null) {
            ProductDAO productDAO = new ProductDAO(connection);

            if (quantity != 0 && nameProduct.length() < 46 && descriptionProduct.length() < 301 && categoryProduct.length() < 46) {

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
                session.getServletContext().setAttribute("codeResult", 3);
                response.sendRedirect("PersonalAreaSupplier");
            }
        }
        else {
            session.getServletContext().setAttribute("codeResult", 4);
            response.sendRedirect("PersonalAreaSupplier");
        }
    }


}
