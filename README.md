# CampusRadar: Events

Module 3 of CampusRadar. Organizers publish campus events, and students RSVP until the capacity is reached.

## Features

- Organizer or admin creates an event: title, category, venue, start and end time, capacity
- Students RSVP and cancel their RSVP
- Each event shows Upcoming, LIVE NOW or Ended
- A full event refuses new RSVPs

## How it works

- **Who can create:** `EventCreateServlet` checks for the role ORGANIZER or ADMIN in both `doGet` and `doPost`. A student who types the URL gets HTTP 403.
- **Capacity:** `EventDAO.rsvp` locks the event row with `SELECT ... FOR UPDATE`, counts the RSVPs and inserts only if there is room, all in one transaction.
- **Status** is computed in Java, comparing the current Asia/Kolkata time with `start_time` and `end_time`, so there is no time-zone mismatch with MySQL.
- Validation: title length, end time after start time, capacity at least 1.

## Files

```
src/main/java/com/campusradar/events/
  Event.java, EventDAO.java
  EventListServlet.java, EventCreateServlet.java, EventRsvpServlet.java
  DevLoginServlet.java        (temporary, for testing this module alone)
src/main/webapp/WEB-INF/views/events/
  list.jsp, create.jsp
```

`DBUtil.java` and `User.java` in the `common` package are shared with the other modules.

## Database

```sql
CREATE TABLE events (
  id INT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(120) NOT NULL,
  description TEXT,
  category VARCHAR(40),
  location_id INT NULL,
  venue_note VARCHAR(120),
  start_time DATETIME NOT NULL,
  end_time DATETIME NOT NULL,
  capacity INT DEFAULT 100,
  created_by INT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (created_by) REFERENCES users(id)
);

CREATE TABLE event_rsvp (
  event_id INT NOT NULL,
  user_id INT NOT NULL,
  rsvp_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (event_id, user_id),
  FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES users(id)
);
```

It also reads the shared `users` table.

## Run this module alone

```
$env:DB_URL  = "jdbc:mysql://HOST:PORT/campusradar?sslMode=REQUIRED"
$env:DB_USER = "your-db-user"
$env:DB_PASS = "your-db-password"
mvn package cargo:run
```

Open `http://localhost:8080/campusradar/events/devlogin?id=1`. The user with that id must have the role ORGANIZER to see the Create button. Database credentials are read from environment variables and must never be committed.

## Test checklist

- [ ] As an organizer, create an event with capacity 2
- [ ] RSVP as three different users: the third sees "This event is full."
- [ ] Cancel an RSVP; the count drops
- [ ] As a student, the Create button is hidden and `/events/create` gives 403
- [ ] An event whose start time has passed shows LIVE NOW, and one whose end time has passed shows Ended

## Integration

In the combined project, `DevLoginServlet` is removed, `AuthFilter` protects `/events/*`, and the pages use the shared header and footer.

## Team and ownership

| Role | Person | Notes |
|---|---|---|
| Original development and integration | Himanshu (`pekkaeth`) | Repository owner |
| Module lead (study, testing, documentation, extension) | Harsh (`harsh-sankhla`) | Collaborator |

**Module lead responsibilities:** understand the organizer-only check, the locked RSVP and how event status is computed; test every flow in the checklist above; keep this README accurate.

| Contribution | Status | Commits |
|---|---|---|
| Test checklist completed and results recorded | Planned | |
| Extension: [choose one with the module lead] | Planned | |
