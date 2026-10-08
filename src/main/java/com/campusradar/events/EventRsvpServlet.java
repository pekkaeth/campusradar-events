package com.campusradar.events;

import com.campusradar.common.User;
import java.io.IOException;
import java.net.URLEncoder;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/events/rsvp", "/events/cancel"})
public class EventRsvpServlet extends HttpServlet {
    private final EventDAO dao = new EventDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) { res.sendRedirect(req.getContextPath() + "/events/devlogin"); return; }
        String msg;
        try {
            int eventId = Integer.parseInt(req.getParameter("eventId"));
            if (req.getServletPath().equals("/events/rsvp")) {
                String r = dao.rsvp(eventId, user.getId());
                msg = (r == null) ? "You are going!" : r;
            } else {
                dao.cancel(eventId, user.getId());
                msg = "RSVP cancelled.";
            }
        } catch (Exception e) {
            msg = "Error: " + e.getMessage();
        }
        res.sendRedirect(req.getContextPath() + "/events/list?msg=" + URLEncoder.encode(msg, "UTF-8"));
    }
}