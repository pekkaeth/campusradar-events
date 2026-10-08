package com.campusradar.events;

import com.campusradar.common.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/events/list")
public class EventListServlet extends HttpServlet {
    private final EventDAO dao = new EventDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/events/devlogin");
            return;
        }
        try {
            req.setAttribute("events", dao.listAll(user.getId()));
        } catch (Exception e) {
            req.setAttribute("error", "Could not load events: " + e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/events/list.jsp").forward(req, res);
    }
}