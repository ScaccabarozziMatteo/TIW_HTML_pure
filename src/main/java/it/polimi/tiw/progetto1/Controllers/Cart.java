package it.polimi.tiw.progetto1.Controllers;

import it.polimi.tiw.progetto1.Beans.Order;
import it.polimi.tiw.progetto1.Beans.Product;
import it.polimi.tiw.progetto1.DAO.ProductDAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "Cart", value = "/Cart")
public class Cart extends HttpServlet {
    private Connection connection;

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
        List<Order> orders = (List<Order>) session.getAttribute("orders");
        int action = -1, quantity = -1, codeProd = -1;
        String supplier = null;

        try {
            action = Integer.parseInt(request.getParameter("action"));
            quantity = Integer.parseInt(request.getParameter("numArtCart"));
            codeProd = Integer.parseInt(request.getParameter("codeProd"));
            supplier = request.getParameter("supplier");

        } catch (NumberFormatException e) {
            quantity = -1;
        }

        ProductDAO productDAO = new ProductDAO(connection);
        boolean cartContainsProduct = false, cartContainsSupplier = false;

        if (quantity > 0 && codeProd > 0 && supplier != null && !supplier.equals("")) {

            if (orders == null)
                orders = new ArrayList<>();
            if (action == 1) {

                for (Order order : orders) {
                    // cart contains product of specific supplier
                    if (order.getSupplier().equals(supplier))
                        for (Product product : order.getProducts()) {
                            if (product.getCode() == codeProd) {
                                product.setQuantity(product.getQuantity() + quantity);
                                cartContainsProduct = true;
                                cartContainsSupplier = true;
                                break;
                            }
                        }
                    if (!cartContainsProduct) {
                        // cart contains supplier's order but not product
                        order.getProducts().add(new Product(codeProd, quantity));
                        cartContainsSupplier = true;
                    }
                }
                if (!cartContainsSupplier) {
                    // cart not contains supplier
                    Order order = new Order(supplier);
                    orders.add(order);
                    order.getProducts().add(new Product(codeProd, quantity));
                }

                response.sendRedirect("PersonalAreaCustomer?id=4");

            }
            if (action == 0) {

            }
        } else {
            response.sendError(400, "Parametri non validi");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
