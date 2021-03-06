package servlets;


import business.Auth;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/auth/register")
public class RegisterServlet extends HttpServlet{
    @EJB
    private Auth auth;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        System.out.println(username);
        System.out.println(password);
        String result = auth.register(username, password);
        if(result.equals("User already exists") || result.equals("Error")){
            resp.setStatus(403);
        }
        resp.getWriter().write(result);
    }
}
