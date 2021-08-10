package it.polimi.tiw.progetto1.templates;

import it.polimi.tiw.progetto1.DAO.ProductDAO;
import it.polimi.tiw.progetto1.DAO.ShipmentPolicyDAO;
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

@WebServlet(name = "personalAreaSupplier", urlPatterns = "/PersonalAreaSupplier")
public class templatePersonalAreaSuppliers extends HttpServlet {

    private TemplateEngine templateEngine;
    private Connection connection;

    @Override
    public void init() throws UnavailableException {
        ServletContext servletContext = getServletContext();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setSuffix(".html");
        this.templateEngine = new TemplateEngine();
        templateResolver.setCharacterEncoding("UTF-8");
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


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession();
        String strLogin = (String) session.getAttribute("supplierCode");
        ShipmentPolicyDAO shipmentPolicyDAO = new ShipmentPolicyDAO(connection);
        ProductDAO productDAO = new ProductDAO(connection);

        if (strLogin != null) {

            String path = "/AreaPersonaleVenditore";
            ServletContext servletContext = getServletContext();
            final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
            ctx.setVariable("codeResult", servletContext.getAttribute("codeResult"));
            try {
                ctx.setVariable("shipmentPolicies", shipmentPolicyDAO.shipmentPolicyList((String) session.getAttribute("supplierCode")));
                ctx.setVariable("allProducts", productDAO.supplierProducts((String) session.getAttribute("supplierCode")));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            templateEngine.process(path, ctx, response.getWriter());
            ctx.removeVariable("codeResult");
            servletContext.removeAttribute("codeResult");
            ctx.removeVariable("shipmentPolicies");
            ctx.removeVariable("allProducts");

        }
        else
            response.sendRedirect("index.html");
    }
}
