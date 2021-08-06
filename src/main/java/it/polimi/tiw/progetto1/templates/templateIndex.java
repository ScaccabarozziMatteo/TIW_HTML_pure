package it.polimi.tiw.progetto1.templates;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "index", urlPatterns = "/index.html")
public class templateIndex extends HttpServlet {

    private TemplateEngine templateEngine;

    @Override
    public void init() {
        ServletContext servletContext = getServletContext();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setSuffix(".html");
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = "/index";
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

        ctx.setVariable("errorNoCredential", servletContext.getAttribute("errorNoCredential"));
        ctx.setVariable("errorLoginSupplier", servletContext.getAttribute("errorLoginSupplier"));
        ctx.setVariable("errorLoginCustomer", servletContext.getAttribute("errorLoginCustomer"));
        templateEngine.process(path, ctx, response.getWriter());
        ctx.removeVariable("errorNoCredential");
        ctx.removeVariable("errorLoginSupplier");
        ctx.removeVariable("errorLoginCustomer");
        servletContext.removeAttribute("errorNoCredential");
        servletContext.removeAttribute("errorLoginSupplier");
        servletContext.removeAttribute("errorLoginCustomer");
    }
}