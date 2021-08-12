package it.polimi.tiw.progetto1.Controllers;

import it.polimi.tiw.progetto1.DAO.ShipmentPolicyDAO;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet(name = "insertShipmentPolicy", urlPatterns = "/insertShipmentPolicy")
public class InsertShipmentPolicy extends HttpServlet {

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

        if (supplierCode != null) {

            ShipmentPolicyDAO shipmentPolicyDAO = new ShipmentPolicyDAO(connection);

            int minArticles, maxArticles;
            float shipmentPrice, priceFreeShipment;

            try {
                minArticles = Integer.parseInt(request.getParameter("minArticles"));
                maxArticles = Integer.parseInt(request.getParameter("maxArticles"));
                shipmentPrice = Float.parseFloat(request.getParameter("shipmentPrice"));
            } catch (NumberFormatException e) {
                minArticles = -1;
                maxArticles = -1;
                shipmentPrice = -1;
            }

            try {
                priceFreeShipment = Float.parseFloat(request.getParameter("priceFreeShipment"));
            } catch (NumberFormatException e) {
                priceFreeShipment = -1;
            }

            try {

                if (minArticles > 0 && maxArticles > 0 && shipmentPrice > 0 && priceFreeShipment == -1 && minArticles <= maxArticles) {
                    // Insert shipment policy among 2 articles

                    if (!shipmentPolicyDAO.checkIfExistPolicy(supplierCode, minArticles, maxArticles)) {
                        // successfully insert
                        shipmentPolicyDAO.createShipPolicy(minArticles, maxArticles, supplierCode, shipmentPrice);
                        session.getServletContext().setAttribute("codeResult", 5);
                        response.sendRedirect("PersonalAreaSupplier");
                    } else {
                        // unable to create ship policy
                        session.getServletContext().setAttribute("codeResult", 6);
                        response.sendRedirect("PersonalAreaSupplier");
                    }

                } else if (minArticles == -1 && maxArticles == -1 && shipmentPrice == -1 && priceFreeShipment > 0 && minArticles <= maxArticles) {
                    // Insert free shipment policy


                    if (!shipmentPolicyDAO.checkIfExistPolicy(supplierCode, priceFreeShipment)) {
                        shipmentPolicyDAO.createShipPolicy(supplierCode, priceFreeShipment);
                        session.getServletContext().setAttribute("codeResult", 5);
                        response.sendRedirect("PersonalAreaSupplier");
                    } else {
                        // unable to create ship policy
                        session.getServletContext().setAttribute("codeResult", 8);
                        response.sendRedirect("PersonalAreaSupplier");
                    }


                } else {
                    // Missing or invalid parameters
                    session.getServletContext().setAttribute("codeResult", 7);
                    response.sendRedirect("PersonalAreaSupplier");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else
            response.sendRedirect("index.html");
    }
}
