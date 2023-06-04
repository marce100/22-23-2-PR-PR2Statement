package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.List;
import edu.uoc.ds.traversal.Iterator;


import java.time.LocalDate;

public class Attendee  {
    private String id;
    private String name;
    private String surname;

    private LocalDate dateOfBirth;
    private final List<Event> events;

	public Attendee(String idUser, String name, String surname, LocalDate dateOfBirth) {
        this.setId(idUser);
        this.setName(name);
        this.setSurname(surname);
        this.setDateOfBirth(dateOfBirth);
        this.events = new LinkedList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }
    public boolean is(String entityId) {
        return id.equals(entityId);
    }
/*
    public void addEvent(SportEvent sportEvent) {
        events.insertEnd(sportEvent);
    }

    public int numEvents() {
        return events.size();
    }

    public boolean isInSportEvent(String eventId) {
        boolean found = false;
        SportEvent sportEvent = null;
        Iterator<SportEvent> it = getEvents();
        while (it.hasNext() && !found) {
            sportEvent = it.next();
            found = sportEvent.is(eventId);
        }
        return found;
    }

    public int numSportEvents() {
        return events.size();
    }

    public Iterator<SportEvent> getEvents() {
        return events.values();
    }

    public boolean hasEvents() {
        return this.events.size()>0;
    }
*/
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }


    public int numEvents() {
        return events.size();
    }

    public void addEvent(Event event) {
        events.insertEnd(event);
    }

    public Iterator<Event> events() {
        return events.values();
    }

    public boolean isInEvent(String eventId) {
        boolean found = false;
        Event event = null;
        Iterator<Event> it = events.values();
        while (it.hasNext() && !found) {
            event = it.next();
            found = event.getEventId().equals(eventId);
        }
        return found;
    }

}
