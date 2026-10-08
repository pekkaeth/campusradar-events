package com.campusradar.events;

import com.campusradar.common.User;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/events/create")
public class EventCreateServlet extends HttpServlet {
    private final EventDAO dao = new EventDAO();

    private boolean allowed(User u) {
        return u != null && (u.getRole().equals("ORGANIZER") || u.getRole().equals("ADMIN"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) { res.sendRedirect(req.getContextPath() + "/events/devlogin"); return; }
        if (!allowed(user)) { res.sendError(403, "Only organizers can create events."); return; }
        req.getRequestDispatcher("/WEB-INF/views/events/create.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) { res.sendRedirect(req.getContextPath() + "/events/devlogin"); return; }
        if (!allowed(user)) { res.sendError(403, "Only organizers can create events."); return; }

        String title = req.getParameter("title");
        String error = null;
        try {
            LocalDateTime start = LocalDateTime.parse(req.getParameter("start"));
            LocalDateTime end = LocalDateTime.parse(req.getParameter("end"));
            int capacity = Integer.parseInt(req.getParameter("capacity"));
            if (title == null || title.trim().length() < 3) error = "Title must be at least 3 characters.";
            else if (!end.isAfter(start)) error = "End time must be after the start time.";
            else if (capacity < 1) error = "Capacity must be at least 1.";
            if (error == null) {
                String desc = req.getParameter("description");
                String venue = req.getParameter("venue");
                dao.create(title.trim(), desc == null ? "" : desc.trim(), req.getParameter("category"),
                        venue == null ? "" : venue.trim(), Timestamp.valueOf(start), Timestamp.valueOf(end),
                        capacity, user.getId());
                res.sendRedirect(req.getContextPath() + "/events/list?msg=Event+created");
                return;
            }
        } catch (Exception e) {
            error = "Please fill in all fields correctly.";
        }
        req.setAttribute("error", error);
        req.getRequestDispatcher("/WEB-INF/views/events/create.jsp").forward(req, res);
    }
}