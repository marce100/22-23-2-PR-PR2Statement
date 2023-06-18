package uoc.ds.pr.model;

public class Enrollment {
    public static final boolean SUBTITUTE = true;
    Attendee attendee;
    boolean isSubstitute;

    public Enrollment(Attendee attendee) {
        this(attendee, false);
    }
    public Enrollment(Attendee attendee, boolean isSubstitute) {
        this.attendee = attendee;
        this.isSubstitute = isSubstitute;
    }

    public Attendee getAttendee() {
        return attendee;
    }

    public boolean isSubstitute() {
        return isSubstitute;
    }
}
