package com.campusradar.events;

import java.sql.Timestamp;

public class Event {
    private int id;
    private String title;
    private String description;
    private String category;
    private String venueNote;
    private Timestamp startTime;
    private Timestamp endTime;
    private int capacity;
    private int goingCount;
    private String organizerName;
    private String status;     // UPCOMING, ONGOING, ENDED
    private boolean going;     // has the current user RSVPed?

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getVenueNote() { return venueNote; }
    public void setVenueNote(String venueNote) { this.venueNote = venueNote; }
    public Timestamp getStartTime() { return startTime; }
    public void setStartTime(Timestamp startTime) { this.startTime = startTime; }
    public Timestamp getEndTime() { return endTime; }
    public void setEndTime(Timestamp endTime) { this.endTime = endTime; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public int getGoingCount() { return goingCount; }
    public void setGoingCount(int goingCount) { this.goingCount = goingCount; }
    public String getOrganizerName() { return organizerName; }
    public void setOrganizerName(String organizerName) { this.organizerName = organizerName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public boolean isGoing() { return going; }
    public void setGoing(boolean going) { this.going = going; }
}