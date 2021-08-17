package it.polimi.tiw.progetto1.Controllers;

import it.polimi.tiw.progetto1.Beans.Order;
import it.polimi.tiw.progetto1.Beans.Product;
import it.polimi.tiw.progetto1.Beans.ShipmentPolicy;
import it.polimi.tiw.progetto1.DAO.OrderDAO;
import it.polimi.tiw.progetto1.DAO.ProductDAO;
import it.polimi.tiw.progetto1.DAO.ShipmentPolicyDAO;
import it.polimi.tiw.progetto1.DAO.SupplierDAO;

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
        String strLogin = (String) session.getAttribute("emailCustomer");
        if (strLogin != null) {
            List<Order> orders = (List<Order>) session.getAttribute("cart");
            ServletContext servletContext = getServletContext();
            int action = -1, quantity, codeProd = -1;
            String supplier = null;
            boolean end = false;

            try {
                action = Integer.parseInt(request.getParameter("action"));

            } catch (NumberFormatException e) {
                action = -1;
            }

            ProductDAO productDAO = new ProductDAO(connection);
            boolean cartContainsProduct = false, cartContainsSupplier = false;

            if (action == 1) {

                try {
                    quantity = Integer.parseInt(request.getParameter("numArtCart"));
                    codeProd = Integer.parseInt(request.getParameter("codeProd"));
                    supplier = request.getParameter("supplier");

                } catch (NumberFormatException e) {
                    quantity = -1;
                }

                if (quantity > 0 && codeProd > 0 && supplier != null && !supplier.equals("") && supplier.length() < 46) {

                    if (orders == null)
                        orders = new ArrayList<>();

                    for (Order order : orders) {
                        // cart contains product of specific supplier
                        if (order.getSupplierCode().equals(supplier)) {
                            for (Product product : order.getProducts()) {
                                if (product.getCode() == codeProd) {
                                    product.setQuantity(product.getQuantity() + quantity);
                                    cartContainsProduct = true;
                                    cartContainsSupplier = true;
                                    end = true;
                                    break;
                                }
                            }
                            if (!cartContainsProduct) {
                                // cart contains supplier's order but not product
                                Product product = null;
                                try {
                                    product = productDAO.getInfoProduct(codeProd);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                product.setQuantity(quantity);
                                order.getProducts().add(product);
                                cartContainsSupplier = true;
                                end = true;
                            }
                        }
                        if (end) {
                            order.setTotQuantity(setTotalQuantity(order));
                            order.setTotal(setSubtotal(order));
                            try {
                                order.setShipmentFees(setShipmentFees(order, connection));
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                    if (!cartContainsSupplier) {
                        // cart not contains supplier
                        Order order = new Order(supplier);
                        Product product = null;
                        SupplierDAO supplierDAO = new SupplierDAO(connection);

                        try {
                            order.setSupplierName(supplierDAO.getSupplierName(supplier));
                            product = productDAO.getInfoProduct(codeProd);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        product.setQuantity(quantity);
                        order.getProducts().add(product);
                        order.setTotQuantity(setTotalQuantity(order));
                        order.setTotal(setSubtotal(order));
                        try {
                            order.setShipmentFees(setShipmentFees(order, connection));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        orders.add(order);
                    }
                    session.setAttribute("cart", orders);
                    response.sendRedirect("PersonalAreaCustomer?id=4");

                } else {
                    response.sendError(400, "Parametri non validi");
                }
            } else if (action == 0) {

                try {
                    codeProd = Integer.parseInt(request.getParameter("codeProd"));
                    supplier = request.getParameter("supplier");

                } catch (NumberFormatException e) {
                    codeProd = -1;
                }

                end = false;

                if (codeProd > 0 && supplier != null && !supplier.equals("") && supplier.length() < 46) {
                    for (Order order : orders) {
                        if (order.getSupplierName().equals(supplier)) {
                            for (Product product : order.getProducts()) {
                                if (product.getCode() == codeProd) {
                                    order.setTotQuantity(setTotalQuantity(order));
                                    order.setTotal(setSubtotal(order));
                                    try {
                                        order.setShipmentFees(setShipmentFees(order, connection));
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                    order.getProducts().remove(product);
                                    end = true;
                                    break;
                                }
                            }
                            if (order.getProducts().isEmpty())
                                orders.remove(order);
                        }
                        if (end)
                            break;
                    }
                    session.setAttribute("cart", orders);
                    response.sendRedirect("PersonalAreaCustomer?id=4");
                } else {
                    response.sendError(400, "Parametri non validi");
                }
            } else if (action == 2) {
                supplier = request.getParameter("supplier");
                if (supplier != null && !supplier.equals("") && supplier.length() < 46) {

                    for (Order order : orders) {
                        if (order.getSupplierName().equals(supplier)) {
                            OrderDAO orderDAO = new OrderDAO(connection);
                            try {
                                orderDAO.sentOrder(order, strLogin);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            orders.remove(order);
                            break;
                        }
                    }
                    if (orders.isEmpty())
                        session.setAttribute("cart", null);
                    else
                        session.setAttribute("cart", orders);
                    servletContext.setAttribute("updateOrders", "T");
                    response.sendRedirect("PersonalAreaCustomer?id=5");
                }
                else
                    response.sendError(400, "Parametri non validi");
            }
            else
                response.sendError(400, "Parametri non validi");
        } else {
            response.sendRedirect("index.html");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected float setShipmentFees(Order order, Connection connection) throws SQLException {
        float fees = 0, quantity;
        float subtotal = order.getTotal();
        List<ShipmentPolicy> policyList;

        quantity = order.getTotQuantity();
        ShipmentPolicyDAO shipmentPolicyDAO = new ShipmentPolicyDAO(connection);
        policyList = shipmentPolicyDAO.shipmentPolicyList(order.getSupplierCode());

        for (ShipmentPolicy shipPolicy: policyList) {
            if (shipPolicy.getMin_articles() == 999999999 && shipPolicy.getFreeShipment() <= subtotal) {
                fees = 0;
                return fees;
            }
            else if (quantity >= shipPolicy.getMin_articles() && quantity <= shipPolicy.getMax_articles()) {
                fees = shipPolicy.getCostShipment();
            }
        }

        return fees;
    }

    protected int setTotalQuantity(Order order) {
        int quantity = 0;

        for (Product product: order.getProducts()) {
            quantity += product.getQuantity();
        }
        return quantity;
    }

    protected float setSubtotal(Order order) {
        float total = 0;

        for (Product product: order.getProducts()) {
            total += product.getPrice()*product.getQuantity();
        }
        return total;
    }
}