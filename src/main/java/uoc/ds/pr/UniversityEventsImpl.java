package uoc.ds.pr;

import java.time.LocalDate;

import edu.uoc.ds.adt.nonlinear.DictionaryAVLImpl;
import edu.uoc.ds.adt.nonlinear.HashTable;
import edu.uoc.ds.adt.nonlinear.PriorityQueue;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.List;
import edu.uoc.ds.adt.sequential.Queue;
import edu.uoc.ds.adt.sequential.QueueArrayImpl;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.*;
import uoc.ds.pr.util.DSArray;
import uoc.ds.pr.util.EntityFactory;
import uoc.ds.pr.util.OrderedVector;


public class UniversityEventsImpl implements UniversityEvents {

    //private final DSArray<Entity> entities;
    private final HashTable<String, Entity> entities;
    private final DSArray<Attendee> attendees;
    //private final DSArray<Event> events;
    private final DictionaryAVLImpl events;

    //private final Queue<EventRequest> requestQueue;
    private final PriorityQueue<EventRequest> requestQueue;

    private final LinkedList<EventRequest> rejectedRequests;

    private Attendee mostActiveAttendee;
    private final OrderedVector<Event> bestEvent;

    public UniversityEventsImpl() {
        //entities = new DSArray<>(MAX_NUM_ENTITIES);
        entities = new HashTable<>();
        attendees = new DSArray<>(MAX_NUM_ATTENDEES);
        //events = new DSArray<>(MAX_NUM_EVENTS);
        events = new DictionaryAVLImpl();
        requestQueue = new PriorityQueue<>(MAX_NUM_REQUESTS, EventRequest.CMP_V);
        rejectedRequests = new LinkedList<>();
        bestEvent = new OrderedVector<>(MAX_NUM_EVENTS, Event.CMP_V);

    }


    public void addEntity(String id, String name, String description, EntityType entityType){
        Entity entity = getEntity(id);
        if (entity != null) {
            entity.setName(name);
            entity.setDescription(description);
            if (entity.getEntityType() != entityType) {
                entity = EntityFactory.getEntity(entityType, id, name, description);
                entities.delete(id);
                entities.put(id, entity);
            }
        } else {
            entity = EntityFactory.getEntity(entityType, id, name, description);
            entities.put(id, entity);
        }
    }


    public void addAttendee(String id, String name, String surname, LocalDate dateOfBirth, String phone) {
        Attendee attendee = getAttendee(id);
        if (attendee != null) {
            attendee.setName(name);
            attendee.setSurname(surname);
            attendee.setDateOfBirth(dateOfBirth);
            attendee.setPhone(phone);
        } else {
            attendee = new Attendee(id, name, surname, dateOfBirth, phone);
            attendees.put(id, attendee);
        }

    }





    public void addEventRequest(String id, String eventId, String entityId, String description, InstallationType installationType,
                                byte resources, int max, LocalDate startDate, LocalDate endDate, boolean allowRegister) throws EntityNotFoundException {

        Entity entity = getEntity(entityId);
        if (entity == null) throw new EntityNotFoundException();

        //System.out.println("Añadir request");
        requestQueue.add(new EventRequest(id, eventId, entity, description, installationType, resources, max, startDate, endDate, allowRegister));


//        /*aqui*/
//        System.out.println("---------------------------");
//        Iterator i= requestQueue.values();
//        while (i.hasNext()) {
//            EventRequest e= (EventRequest) i.next();
//            System.out.println(e.getRequestId()+"    "+e.getEvent().getStartDate());
//        }
//        System.out.println("---------------------------");
//        /*aqui*/
    }


    public EventRequest updateEventRequest(Status status, LocalDate date, String message) throws NoEventRequestException {



        /*aqui*
        System.out.println("update request");
        System.out.println("---------------------------");
        for (EventRequest element : requestQueue) {
            System.out.println(element.getRequestId()+"    "+element.getDateStatus());
        }
        System.out.println("---------------------------");
        /*aqui*/



        //System.out.println("tamaño cola: "+requestQueue.size());

        if (requestQueue.size() == 0) throw new NoEventRequestException();
        EventRequest request = requestQueue.poll();
        if (request  == null) {
        	throw new NoEventRequestException();
        }


        //System.out.println("Update request");
        request.update(status, date, message);

        //requestQueue.remove(request);
        //requestQueue.add(request);
        //request = requestQueue.poll();

        //System.out.println("tamaño cola: "+requestQueue.size());

        if (request.isEnabled()) {
            Event event = request.getEvent();
            Entity entity = request.getEntity();

            events.put(event.getEventId(), event);
            entity.addEvent(event);
        }
        else {
        	rejectedRequests.insertEnd(request);
        }

        /*aqui
        System.out.println("fin update");
        System.out.println("---------------------------");
        for (EventRequest element : requestQueue) {
            System.out.println(element.getRequestId()+"    "+element.getDateStatus());
        }
        System.out.println("---------------------------");
        /*aqui*/

        //requestQueue.peek()request); ///////////////////añadir otra vez


        /*aqui
        Iterator<EventRequest> it = requestQueue.iterator();
        while (it.hasNext()){
            EventRequest eventRequest = it.next();
            System.out.println(eventRequest.getRequestId());
        }
        /*aqui*/

        return request;
    }

    @Override
    public void signUpEvent(String attendeeId, String eventId) throws AttendeeNotFoundException, EventNotFoundException, NotAllowedException, AttendeeAlreadyInEventException {
        Event event = getEvent(eventId);
        if (event==null) throw new EventNotFoundException();

        Attendee attendee = getAttendee(attendeeId);
        if (attendee==null) throw new AttendeeNotFoundException();

        if (!event.isAllowedRegister()) {
            throw new NotAllowedException();
        }

        if (attendee.isInEvent(eventId)) {
            throw new AttendeeAlreadyInEventException();
        }

            if (!event.isFull()) {
            event.addAttendee(new Enrollment(attendee));
        }
        else {
            event.addAttendee(new Enrollment(attendee, Enrollment.SUBTITUTE));
            event.incSubstitute();
        }

        attendee.addEvent(event);

        updateMostActiveAttendee(attendee);
    }

    public double getPercentageRejectedRequests() {
        int total = events.size()+rejectedRequests.size();
        return (total!=0?(double) rejectedRequests.size() / total:0);
    }

    public Iterator<EventRequest> getRejectedRequests() throws NoEventRequestException {
        if (rejectedRequests.size()==0) throw new NoEventRequestException();

        return rejectedRequests.values();
    }

    public Iterator<Event> getEventsByEntity(String entityId) throws NoEventsException {
        Entity entity = getEntity(entityId);
        Iterator<Event> it = entity.events();
        if (!it.hasNext()) throw new NoEventsException();

        return it;

    }

    public Iterator<Event> getAllEvents() throws NoEventsException {
        if (events.size()==0) throw new NoEventsException();

        return events.values();
    }

    public Iterator<Event> getEventsByAttendee(String attendeeId) throws NoEventsException {
        Attendee attendee = getAttendee(attendeeId);
        Iterator<Event> it = attendee.events();
        if (!it.hasNext()) throw new NoEventsException();

        return it;
    }


    public void addRating(String attendeeId, String eventId, Rating rating, String message) throws AttendeeNotFoundException, EventNotFoundException, AttendeeNotInEventException {
        Event event = getEvent(eventId);
        if (event == null) {
        	throw new EventNotFoundException();
        }

        Attendee attendee = getAttendee(attendeeId);
        if (attendee == null) {
        	throw new AttendeeNotFoundException();
        }

        if (!attendee.isInEvent(eventId)) {
        	throw new AttendeeNotInEventException();
        }

        event.addRating(rating, message, attendee);
        updateBestEvent(event);
    }


    private void updateBestEvent(Event event) {
        bestEvent.delete(event);
        bestEvent.update(event);
    }


    public Iterator<uoc.ds.pr.model.Rating> getRatingsByEvent(String eventId) throws EventNotFoundException, NoRatingsException {
        Event event = getEvent(eventId);
        if (event  == null) {
        	throw new EventNotFoundException();
        }

        if (!event.hasRatings()) {
        	throw new NoRatingsException();
        }

        return event.ratings();
    }




    public Attendee mostActiveAttendee() throws AttendeeNotFoundException {
        if (mostActiveAttendee == null) {
        	throw new AttendeeNotFoundException();
        }

        return mostActiveAttendee;
    }

    public Event bestEvent() throws EventNotFoundException {
        if (bestEvent.size() == 0) {
        	throw new EventNotFoundException();
        }

        return bestEvent.elementAt(0);
    }

    private void updateMostActiveAttendee(Attendee attendee) {
        if (mostActiveAttendee == null) {
            mostActiveAttendee = attendee;
        }
        else if (attendee.numEvents() > mostActiveAttendee.numEvents()) {
            mostActiveAttendee = attendee;
        }
    }

    
    public int numEntities() {
        return entities.size();
    }

    public int numAttendees() {
        return attendees.size();
    }

    public int numRequests() {
        return requestQueue.size();
    }

    public int numEvents() {
        return events.size();
    }

    public int numEventsByAttendee(String attendeeId) {
        Attendee attendee = getAttendee(attendeeId);
        return (attendee!=null?attendee.numEvents():0);
    }
    public int numEventsByEntity(String entityId) {
        Entity entity = getEntity(entityId);
        return (entity!=null?entity.numEvents():0);
    }

    public int numAttendeesByEvent(String eventId) {
        Event event = getEvent(eventId);
        return (event!=null?event.numAttendees():0);
    }

    public int numSubstitutesByEvent(String eventId) {
        Event event = getEvent(eventId);
        return (event!=null?event.numSubstitutes():0);
    }

    public int numRejectedRequests() {
        return this.rejectedRequests.size();
    }

    public int numRatingsByEvent(String eventId) {
        Event event = getEvent(eventId);

        return (event!=null?event.numRatings():0);
    }


    @Override
    public Entity getEntity(String entityId) {
        Entity entity = entities.get(entityId);
        return entity;
    }

    @Override
    public Attendee getAttendee(String attendeeId) {
        Attendee attendee = attendees.get(attendeeId);
        return attendee;
    }

    @Override
    public Event getEvent(String eventId) {
        return (Event) events.get(eventId);
    }

    public DictionaryAVLImpl getEvents(){
        return events;
    }
}
