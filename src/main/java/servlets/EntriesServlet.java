package servlets;

import business.Entries;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/entry")
public class EntriesServlet extends HttpServlet {
    @EJB
    private Entries entries;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String token = req.getParameter("token");
        String answer = entries.getEntries(token);
        if (answer.equals("Unauthorized")) {
            resp.setStatus(403);
        }
        resp.getWriter().write(answer);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String token = req.getParameter("token");
        String x = req.getParameter("x");
        String y = req.getParameter("y");
        String r = req.getParameter("r");
        String answer = entries.addEntry(token, x, y, r);
        if (answer.equals("Unauthorized") || answer.equals("Bad format")) {
            resp.setStatus(403);
        }
        resp.getWriter().write(answer);
    }
}
