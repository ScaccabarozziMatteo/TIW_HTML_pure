package it.polimi.tiw.progetto1.Controllers;

import it.polimi.tiw.progetto1.DAO.ProductDAO;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.MissingResourceException;

@WebServlet(name = "insertProduct", urlPatterns = "/insertProduct")
@MultipartConfig
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
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        String supplierCode = (String) session.getAttribute("supplierCode");

        String PATH = getServletContext().getRealPath(File.separator + "upload") + File.separator;

        String nameProduct, categoryProduct, descriptionProduct, photoName = null, photoExtension, photoPath;
        Part photoPart;
        int codeProduct = -1, quantity;
        float price = 0;

        nameProduct = request.getParameter("nameProduct");
        categoryProduct = request.getParameter("categoryProduct");
        descriptionProduct = request.getParameter("descriptionProd");
        photoPart = request.getPart("imageInput");
        try {
            photoName = Paths.get(photoPart.getSubmittedFileName()).getFileName().toString();
        } catch (MissingResourceException ignored) {
        }

        try {
            codeProduct = Integer.parseInt(request.getParameter("code"));
            quantity = Integer.parseInt(request.getParameter("quantityProduct"));
            price = Float.parseFloat(request.getParameter("priceProduct"));
        }
        catch (NumberFormatException e) {
            quantity = 0;
        }

        if (nameProduct != null && !nameProduct.equals("") && descriptionProduct != null && !descriptionProduct.equals("") && categoryProduct != null && !categoryProduct.equals("") && !photoName.equals("")) {
            ProductDAO productDAO = new ProductDAO(connection);

            if (price > 0 && quantity != 0 && nameProduct.length() < 46 && descriptionProduct.length() < 301 && categoryProduct.length() < 46) {

                try {
                    if (productDAO.ifExistsProduct(codeProduct) && productDAO.supplierHasProduct(supplierCode, codeProduct)) {
                        // update product quantity that exists in catalogue
                        productDAO.addQuantityProduct(codeProduct, quantity, supplierCode);
                        session.getServletContext().setAttribute("codeResult", 2);
                        response.sendRedirect("PersonalAreaSupplier");

                    } else if (productDAO.ifExistsProduct(codeProduct) && !productDAO.supplierHasProduct(supplierCode, codeProduct)) {
                        // add existing product in catalogue

                        productDAO.addProductInCatalogue(codeProduct, quantity, price, supplierCode);
                        session.getServletContext().setAttribute("codeResult", 1);
                        response.sendRedirect("PersonalAreaSupplier");
                    }
                    else {
                        // create new product

                        InputStream inputStream = photoPart.getInputStream();
                        photoExtension = photoName.substring(photoName.lastIndexOf(".") +1);
                        photoName = codeProduct + "." + photoExtension;
                        photoPath = PATH + photoName;

                        byte[] photoBytes;
                        int len;
                        photoBytes = photoPath.getBytes();

                        File photoFile = new File(PATH + photoName);
                        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(photoFile));
                        while ((len = bufferedInputStream.read(photoBytes)) > 0) {
                            bufferedOutputStream.write(photoBytes, 0, len);
                        }
                        bufferedInputStream.close();
                        bufferedOutputStream.close();


                        productDAO.createProduct(codeProduct, nameProduct, descriptionProduct, categoryProduct, photoName, quantity, supplierCode, price);
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
