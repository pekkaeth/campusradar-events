package com.campusradar.events;

import com.campusradar.common.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {

    public List<Event> listAll(int userId) throws Exception {
        String sql = "SELECT e.id, e.title, e.description, e.category, e.venue_note, e.start_time, e.end_time, "
                + "e.capacity, u.name AS organizer, "
                + "(SELECT COUNT(*) FROM event_rsvp r WHERE r.event_id = e.id) AS going, "
                + "(SELECT COUNT(*) FROM event_rsvp r2 WHERE r2.event_id = e.id AND r2.user_id = ?) AS mine "
                + "FROM events e JOIN users u ON u.id = e.created_by ORDER BY e.start_time ASC";
        List<Event> list = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Event e = new Event();
                    e.setId(rs.getInt("id"));
                    e.setTitle(rs.getString("title"));
                    e.setDescription(rs.getString("description"));
                    e.setCategory(rs.getString("category"));
                    e.setVenueNote(rs.getString("venue_note"));
                    e.setStartTime(rs.getTimestamp("start_time"));
                    e.setEndTime(rs.getTimestamp("end_time"));
                    e.setCapacity(rs.getInt("capacity"));
                    e.setGoingCount(rs.getInt("going"));
                    e.setOrganizerName(rs.getString("organizer"));
                    e.setGoing(rs.getInt("mine") > 0);
                    if (now.isBefore(e.getStartTime().toLocalDateTime())) e.setStatus("UPCOMING");
                    else if (now.isAfter(e.getEndTime().toLocalDateTime())) e.setStatus("ENDED");
                    else e.setStatus("ONGOING");
                    list.add(e);
                }
            }
        }
        return list;
    }

    public void create(String title, String description, String category, String venueNote,
                       Timestamp start, Timestamp end, int capacity, int userId) throws Exception {
        String sql = "INSERT INTO events (title, description, category, venue_note, start_time, end_time, capacity, created_by) "
                + "VALUES (?,?,?,?,?,?,?,?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, category);
            ps.setString(4, venueNote);
            ps.setTimestamp(5, start);
            ps.setTimestamp(6, end);
            ps.setInt(7, capacity);
            ps.setInt(8, userId);
            ps.executeUpdate();
        }
    }

    // Returns null if the RSVP worked, otherwise an error message.
    // FOR UPDATE locks the event row so two students cannot take the last seat together.
    public String rsvp(int eventId, int userId) throws Exception {
        try (Connection con = DBUtil.getConnection()) {
            con.setAutoCommit(false);
            try {
                int capacity;
                try (PreparedStatement ps = con.prepareStatement(
                        "SELECT capacity FROM events WHERE id = ? FOR UPDATE")) {
                    ps.setInt(1, eventId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) { con.rollback(); return "Event not found."; }
                        capacity = rs.getInt(1);
                    }
                }
                int count;
                try (PreparedStatement ps = con.prepareStatement(
                        "SELECT COUNT(*) FROM event_rsvp WHERE event_id = ?")) {
                    ps.setInt(1, eventId);
                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        count = rs.getInt(1);
                    }
                }
                if (count >= capacity) { con.rollback(); return "This event is full."; }
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT IGNORE INTO event_rsvp (event_id, user_id) VALUES (?,?)")) {
                    ps.setInt(1, eventId);
                    ps.setInt(2, userId);
                    ps.executeUpdate();
                }
                con.commit();
                return null;
            } catch (Exception e) {
                con.rollback();
                throw e;
            }
        }
    }

    public void cancel(int eventId, int userId) throws Exception {
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "DELETE FROM event_rsvp WHERE event_id = ? AND user_id = ?")) {
            ps.setInt(1, eventId);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }
}