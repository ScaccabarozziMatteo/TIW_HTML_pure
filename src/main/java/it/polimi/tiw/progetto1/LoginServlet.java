package it.polimi.tiw.progetto1;

import java.io.*;
import java.sql.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "login", value = "/login-servlet")
public class LoginServlet extends HttpServlet {

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

        String email, password;
        email = request.getParameter("email");
        password = request.getParameter("password");

        String query = "SELECT email, password FROM dbtest.users";
        ResultSet result = null;
        PreparedStatement pstatement = null;
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        Cookie c1 = new Cookie("email", email);//the username and password are //encrypted
        Cookie c2 = new Cookie("password", password);
        c1.setMaxAge(20000);
        c2.setMaxAge(20000);
        response.addCookie(c1);
        response.addCookie(c2);//sends cookies to the browser

        try {
            pstatement = connection.prepareStatement(query);
            result = pstatement.executeQuery();
            while (result.next()) {
                if(email.equals(result.getString("email")) && password.equals(result.getString("password"))) {
                    response.sendRedirect("AreaPersonale.html");
                }
            }

            out.println("Username o password errate");

        } catch (SQLException e) { out.append("SQL ERROR1");}

        finally {
            try {result.close();
            } catch (Exception e1) {out.append("SQL RES ERROR2");}
            try {pstatement.close();
            } catch (Exception e1) {out.append("SQL STMT ERROR3");
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