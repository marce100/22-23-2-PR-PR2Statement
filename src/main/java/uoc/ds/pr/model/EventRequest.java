package uoc.ds.pr.model;

import uoc.ds.pr.UniversityEvents;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Locale;

public class EventRequest implements Comparable<EventRequest> {

    //public static final Comparator<EventRequest> CMP_V = (se1, se2)->Double.compare(se1.requestId(), se2.requestId());
    public static final Comparator<EventRequest> CMP_V = (se1, se2)->se1.getDateStatus().compareTo(se2.getDateStatus()); ////// Que tengo que poner aqui ???????????
    private final String requestId;
    private UniversityEvents.Status status;
    private String descriptionStatus;

    private LocalDate dateStatus;
    private final Event event;

    public EventRequest(String requestId, String eventId, Entity entity, String description, UniversityEvents.InstallationType installationType,
                        byte resources, int max, LocalDate startDate, LocalDate endDate, boolean allowRegister) {
        this.requestId = requestId;
        this.status = UniversityEvents.Status.PENDING;
        this.event = new Event(eventId, entity, description, installationType, resources, max, startDate, endDate, allowRegister);
    }

    public void update(UniversityEvents.Status status, LocalDate date, String message) {
        this.setStatus(status);
        this.setDateStatus(date);
        this.setDescriptionStatus(message);
    }

    public boolean isEnabled() {
        return this.status == UniversityEvents.Status.ENABLED;
    }

    public String getRequestId() {
        return requestId;
    }

    private void setDescriptionStatus(String message) {
        this.descriptionStatus = message;
    }

    private void setDateStatus(LocalDate date) {
        this.dateStatus = date;
    }

    private void setStatus(UniversityEvents.Status status) {
        this.status = status;
    }


    public Event getEvent() {
        return event;
    }

    public Entity getEntity() {
        return getEvent().getEntity();
    }


    public LocalDate getDateStatus() {
        if (dateStatus==null) {
            return LocalDate.of(1,1,1);
        }
        return dateStatus;
    }

    @Override
    public int compareTo(EventRequest o) {
        return this.requestId.compareTo(o.requestId);
    }
}
