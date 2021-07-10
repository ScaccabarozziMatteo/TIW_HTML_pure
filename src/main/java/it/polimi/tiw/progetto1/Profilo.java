package it.polimi.tiw.progetto1;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "profilo", value = "/profilo-servlet")
public class Profilo extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");

        HttpSession session = request.getSession();

        Cookie[] ck = request.getCookies();
        response.setContentType("text/html");


        PrintWriter out = response.getWriter();
        out.println("<HTML>");
        out.println("<HEAD>");
        out.println("<TITLE>Menu</TITLE>");
        out.println("</HEAD>");
        out.println("<BODY>");
        out.println("Benvenuto " + session.getAttribute("login"));
        out.println("<BR>");
        out.println("Fai un upload di una foto:");
        out.close();


    }
}