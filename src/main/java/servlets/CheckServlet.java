package servlets;

import business.Auth;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/auth/check")
public class CheckServlet extends HttpServlet {
    @EJB
    private Auth auth;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String token = req.getParameter("token");
        resp.getWriter().write(String.valueOf(auth.checkAuth(token)));
    }
}
