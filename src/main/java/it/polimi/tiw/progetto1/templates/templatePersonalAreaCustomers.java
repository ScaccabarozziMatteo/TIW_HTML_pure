package it.polimi.tiw.progetto1.templates;

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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

                if (id == null || (!id.equals("2") && !id.equals("3")))
                    servletContext.removeAttribute("searchedProducts");
                else if (id.equals("3")) {
                    int codeProduct = Integer.parseInt(request.getParameter("codeProd"));
                    addViewedProduct(codeProduct, strLogin, request, response);
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
                ctx.setVariable("searchedProducts", servletContext.getAttribute("searchedProducts"));
                ctx.setVariable("showProducts", id);

                templateEngine.process(path, ctx, response.getWriter());
            }
            else {
                response.sendRedirect("index.html");
            }
        }

        protected void addViewedProduct(int codeProd, String username, HttpServletRequest request, HttpServletResponse response) throws NullPointerException {
            javax.servlet.http.Cookie[] cookies = request.getCookies();
            javax.servlet.http.Cookie cookie = null;
            boolean cookieExist = false;

            for (javax.servlet.http.Cookie cookie1: cookies) {
                if (cookie1.getName().equals("viewedProducts")) {
                    cookie = cookie1;
                    cookieExist = true;
                    break;
                }
            }

            JSONArray jsonArray = null;
            JSONObject jsonObject;

            if(cookieExist) {
                int i = 0;
                String stringArray = cookie.getValue();
                jsonArray = new JSONArray();

                String[] substrings = StringUtils.substringsBetween(stringArray, "{", "}");
                for (String substring : substrings){
                    jsonObject = new JSONObject(substring);
                    if (jsonObject.get("codeProduct")
                    jsonArray.put(i, jsonObject);
                    i++;
                }


                int lenght = jsonArray.length();

                JSONObject existingCookie = Cookie.toJSONObject(cookie.toString());
                existingCookie.get("");

            } else {
                jsonObject = new JSONObject();
                jsonArray = new JSONArray();
                jsonObject.put("username", username);
                jsonObject.put("codeProduct", codeProd);
                jsonArray.put(0, jsonObject);
                String newCookie = jsonArray.toString();
                response.addCookie(new javax.servlet.http.Cookie("viewedProducts", newCookie));

                System.out.println(newCookie);

            }

        }
    }