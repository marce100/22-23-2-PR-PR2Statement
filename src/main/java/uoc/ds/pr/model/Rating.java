package uoc.ds.pr.model;

import uoc.ds.pr.UniversityEvents;

public class Rating {
    private final UniversityEvents.Rating rating;
    private final String message;
    private final Attendee attendee;

    public Rating(UniversityEvents.Rating rating, String message, Attendee attendee) {
        this.rating = rating;
        this.message = message;
        this.attendee = attendee;
    }

    public UniversityEvents.Rating rating() {

        return this.rating;
    }

    public Attendee getAttendee() {
        return this.attendee;
    }

    public String getMessage(){ return this.message; }

}
