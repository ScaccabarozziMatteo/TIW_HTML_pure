package it.polimi.tiw.progetto1.Controllers;

import org.json.JSONObject;

import javax.json.JsonObject;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ViewedProducts", value = "/ViewedProducts")
public class ViewedProducts extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String codeProd = request.getParameter("");

        JSONObject jsonObject = new JSONObject();

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
