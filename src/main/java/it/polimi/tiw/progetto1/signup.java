package it.polimi.tiw.progetto1;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "signup", value = "/signup-servlet")
public class signup extends HttpServlet {

    private Connection connection = null;

    @Override
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
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);

        String name, password;
        name = request.getParameter("username");
        password = request.getParameter("password");

        String catalog = request.getParameter("catalog"); // nullity test omitted..
        String query = "INSERT INTO dbtest.customers VALUES ('name', 'password');";
        ResultSet result = null;
        PreparedStatement pstatement = null;
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        try {
            pstatement = connection.prepareStatement(query);
            result = pstatement.executeQuery();
            while (result.next()) {
                if(name.equals(result.getString("name")) && password.equals(result.getString("password"))) {
                    response.sendRedirect("AreaPersonale.html");
                }
                out.println("Name: " + result.getString("Name") + " Quantity: " + result.getInt("Quantity") + " Price: " + result.getInt("price"));
            }

        } catch (SQLException e) { out.append("SQL ERROR");}
        finally {
            try {result.close();
            } catch (Exception e1) {out.append("SQL RES ERROR");}
            try {
                assert pstatement != null;
                pstatement.close();
            } catch (Exception e1) {out.append("SQL STMT ERROR");
            }
        }

    }

    public void destroy() {
        try {
            if (connection != null){
                connection.close();
            }
        } catch (SQLException ignored) {

        }
    }
}
