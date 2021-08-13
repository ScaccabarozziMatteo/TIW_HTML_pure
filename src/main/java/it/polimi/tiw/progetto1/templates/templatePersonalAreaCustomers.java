package it.polimi.tiw.progetto1.templates;

import it.polimi.tiw.progetto1.Beans.Product;
import it.polimi.tiw.progetto1.DAO.ProductDAO;
import it.polimi.tiw.progetto1.DAO.ShipmentPolicyDAO;
import it.polimi.tiw.progetto1.DAO.SupplierDAO;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.Cookie;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "personalAreaCustomer", urlPatterns = "/PersonalAreaCustomer")
    public class templatePersonalAreaCustomers extends HttpServlet {

    private TemplateEngine templateEngine;
    private Connection connection;

    @Override
    public void init() throws UnavailableException {
        ServletContext servletContext = getServletContext();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);


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


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, NumberFormatException {

        HttpSession session = request.getSession();
        String strLogin = (String) session.getAttribute("emailCustomer");

        if (strLogin != null) {

            String path = "/WEB-INF/AreaPersonaleCliente";
            ServletContext servletContext = getServletContext();
            final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
            String id = request.getParameter("id");

            if (id == null || (!id.equals("2") && !id.equals("3"))) {
                servletContext.removeAttribute("searchedProducts");
                try {
                    if (session.getAttribute("viewedProducts") == null)
                        addViewedProduct(-1, request, connection);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else if (id.equals("3")) {
                int codeProduct = Integer.parseInt(request.getParameter("codeProd"));
                try {
                    addViewedProduct(codeProduct, request, connection);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                ProductDAO productDAO = new ProductDAO(connection);
                SupplierDAO supplierDAO = new SupplierDAO(connection);
                ShipmentPolicyDAO sPolicyDAO = new ShipmentPolicyDAO(connection);
                try {
                    ctx.setVariable("product", productDAO.getInfoProduct(codeProduct));
                    ctx.setVariable("listSuppliers", supplierDAO.getInfoSuppliersShipment(codeProduct));
                    ctx.setVariable("shipmentPolicies", sPolicyDAO.shipmentPoliciesProduct(codeProduct));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            ctx.setVariable("viewedProducts", session.getAttribute("viewedProducts"));
            ctx.setVariable("searchedProducts", servletContext.getAttribute("searchedProducts"));
            ctx.setVariable("showProducts", id);

            templateEngine.process(path, ctx, response.getWriter());
        } else {
            response.sendRedirect("index.html");
        }
    }

    protected void addViewedProduct(int codeProd, HttpServletRequest request, Connection connection) throws NullPointerException, SQLException, IOException {

        ArrayList<Product> viewedProducts = (ArrayList<Product>) request.getSession().getAttribute("viewedProducts");
        ProductDAO productDAO = new ProductDAO(connection);
        int i = ArrayContainsProduct(viewedProducts, codeProd);

        if (codeProd == -1 && viewedProducts == null) {
            viewedProducts = new ArrayList<>();
            List<Product> products = productDAO.list();
            Collections.shuffle(products);
            for (int a = 0; a < 5; a++) {
                products.get(a).setSale(true);
                viewedProducts.add(products.get(a));
            }
        }
        else {
            Product product = productDAO.getInfoProduct(codeProd);
        if (viewedProducts == null) {
            viewedProducts = new ArrayList<>();
            viewedProducts.add(product);
            List<Product> products = productDAO.list();
            Collections.shuffle(products);
            int x = 0;
            while (viewedProducts.size() < 5) {
                if (ArrayContainsProduct(viewedProducts, products.get(x).getCode()) == -1) {
                    products.get(x).setSale(true);
                    viewedProducts.add(products.get(x));
                }
                x++;
            }
        } else if (viewedProducts.size() < 5 && i == -1) {
            // Product not viewed - Array not full
            viewedProducts.add(product);
            List<Product> products = productDAO.list();
            Collections.shuffle(products);
            int x = 0;
            while (viewedProducts.size() < 5) {
                if (ArrayContainsProduct(viewedProducts, products.get(x).getCode()) == -1) {
                    products.get(x).setSale(true);
                    viewedProducts.add(products.get(x));
                }
                x++;
            }
        } else if (viewedProducts.size() == 5 && i == -1) {
            // Product not viewed - Array full
            viewedProducts.remove(0);
            viewedProducts.add(product);
        } else if (viewedProducts.size() < 5) {
            // Product already viewed - Array not full
            viewedProducts.remove(i);
            viewedProducts.add(product);
            List<Product> products = productDAO.list();
            Collections.shuffle(products);
            int x = 0;
            while (viewedProducts.size() < 5) {
                if (ArrayContainsProduct(viewedProducts, products.get(x).getCode()) == -1) {
                    products.get(x).setSale(true);
                    viewedProducts.add(products.get(x));
                }
                x++;
            }
        } else if (viewedProducts.size() == 5) {
            // Product already viewed - Array full
            viewedProducts.remove(i);
            viewedProducts.add(product);
        }
        }
        request.getSession().setAttribute("viewedProducts", viewedProducts);

    }

    protected int ArrayContainsProduct(ArrayList<Product> products, int codeProd) {
        int i = 0;
        if (products != null && codeProd > 0) {
            for (Product product : products) {
                if (product.getCode() == codeProd)
                    return i;
                i++;
            }
        }
        return -1;
    }
}