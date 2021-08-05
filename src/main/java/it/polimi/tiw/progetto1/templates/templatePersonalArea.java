package it.polimi.tiw.progetto1.templates;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "personalArea", urlPatterns = "/PersonalArea")
    public class templatePersonalArea extends HttpServlet {

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

            HttpSession session = request.getSession();
            String strLogin = (String) session.getAttribute("login");

            if (strLogin != null) {

                String path = "/WEB-INF/AreaPersonale";
                ServletContext servletContext = getServletContext();
                final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
                templateEngine.process(path, ctx, response.getWriter());
            }
            else {
                response.sendRedirect("index.html");
            }
        }
    }