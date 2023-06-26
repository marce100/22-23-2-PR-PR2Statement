package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.List;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.UniversityEvents;
import java.time.LocalDate;
import java.util.Comparator;
import edu.uoc.ds.adt.nonlinear.HashTable;


import static uoc.ds.pr.UniversityEvents.MAX_NUM_ENROLLMENT;

public class Event implements Comparable<Event> {
   public static final Comparator<Event> CMP_V = (se1, se2)->Double.compare(se1.rating(), se2.rating());
    private final String eventId;
    private final String description;
    private final UniversityEvents.InstallationType installationType;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final int max;
    private final byte resources;
    private EventRequest request;
    private Entity entity;
    private final boolean allowRegister;
    private final List<Rating> ratings;
    private double sumRating;
    private int numSubstitutes;
    private List<Attendee> attenders;
    private List<Worker> workers;
    private HashTable<String, Enrollment> enrollments;

    public Event(String eventId, Entity entity, String description, UniversityEvents.InstallationType installationType,
                 byte resources, int max, LocalDate startDate, LocalDate endDate, boolean allowRegister) {
        this.eventId = eventId;
        this.entity = entity;
        this.description = description;
        this.installationType = installationType;
        this.resources = resources;
        this.max = max;
        this.startDate = startDate;
        this.endDate = endDate;
        this.allowRegister = allowRegister;
        this.ratings = new LinkedList<>();
        this.workers = new LinkedList<>();
        this.enrollments = new HashTable<>();
    }

    public String getEventId() {
        return eventId;
    }

    public boolean isAllowedRegister() {
        return allowRegister;
    }

    public boolean isFull() {
        return  (enrollments.size()>=this.max);
    }

    public void addAttendee(Enrollment enrollment) {
        enrollments.put(enrollment.getAttendee().getPhone(),enrollment);
    }

    public Entity getEntity() {
        return entity;
    }

    public int numAttendees() {
        return enrollments.size()-numSubstitutes;
    }

    public void incSubstitute() {
        numSubstitutes++;
    }

    public int numSubstitutes() {
        return numSubstitutes;
    }

    public double rating() {
        return (this.ratings.size()>0?(sumRating / this.ratings.size()):0);
    }

    public void addRating(UniversityEvents.Rating rating, String message, Attendee attendee) {
        Rating newRating = new Rating(rating, message, attendee);
        ratings.insertEnd(newRating);
        sumRating+=rating.getValue();
        attendee.getRatings().insertEnd(newRating);
    }

    public boolean hasRatings() {
        return ratings.size()>0;
    }

    public Iterator<Rating> ratings() {
        return ratings.values();
    }

    @Override
    public int compareTo(Event o) {
        return this.eventId.compareTo(o.eventId);
    }

    public int numRatings() {
        return ratings.size();
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public boolean isInWorkers(String id) {
        boolean found = false;
        Iterator<Worker> it = workers.values();
        while (it.hasNext() && !found) {
            found = it.next().getWorkerId().equals(id);
        }
        return found;
    }

    public void setWorker(Worker w) {
        workers.insertEnd(w);
    }

    public int numWorkers(){
        return workers.size();
    }

    public Iterator<Worker> getWorkers(){
        return workers.values();
    }

    public Iterator<Enrollment> getAttendees(){ return enrollments.values(); }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public HashTable<String, Enrollment> getEnrollments() {
        return enrollments;
    }
}
