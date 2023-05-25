package uoc.ds.pr;

import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.Attendee;
import uoc.ds.pr.model.Entity;
import uoc.ds.pr.model.Event;
import uoc.ds.pr.model.EventRequest;

import java.time.LocalDate;



public interface UniversityEvents {

    enum Status {
        PENDING,
        ENABLED,
        DISABLED
    }

    enum EntityType {
        STUDENT,
        PROFESSOR,
        OTHER
    }

    enum InstallationType {
        CONFERENCE_ROOM,
        LECTURE_CLASSROOM,
        SEMINAR_CLASSROOM,
        CASE_STUDY_CLASSROOM
    }

    //
    // Resources
    //

    int MAX_NUM_ENTITIES = 4800;
    int MAX_NUM_ATTENDEES = 185;
    int MAX_NUM_EVENTS = 100;
    int MAX_NUM_REQUESTS = 50;
    int MAX_NUM_ENROLLMENT = 100;


    enum Rating {
        ONE (1),
        TWO (2),
        THREE  (3),
        FOUR (4),
        FIVE (5);

        private final int value;

        Rating(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    //
    // Resources
    //
    byte FLAG_VIDEO_PROJECTOR = 1;    // 0001
    byte FLAG_TOUCH_SCREEN = 2;    // 0010
    byte FLAG_COMPUTER = 4;  // 0100
    byte FLAG_AUXILIARY_MIC = 8;          // 1000
    byte FLAG_ALL_OPTS  = 15;          // 1111

    boolean ALLOW_REGISTER = true;

    /**
     * @pre true.
     * @post if the entity code is new, the entities will be the same plus a new entity.
     * If not, the entity data will have been updated.
     */
    void addEntity(String id, String name, String description, EntityType entityType);

    /**
     * @pre true.
     * @post if the code is new, the attendees will be the same plus a new attendee.
     * If not, the data will have been updated.
     */
    void addAttendee(String id, String name, String surname, LocalDate dateOfBirth, String phone);


    /**
     * @pre the event request and the event do not exist.
     * @post the requests will be the same plus a new one.
     *
     * @throws EntityNotFoundException If the entity does not exist, the error will be reported
     */
    void addEventRequest(String id, String eventId, String entityId, String description, InstallationType installationType,
                         byte resources, int max, LocalDate startDate, LocalDate endDate, boolean allowRegister) throws EntityNotFoundException;


    /**
     * @pre true.
     * @post the status of the first request pending validation is modified.
     * The number of applications pending validation will be the same minus one and, if the event is approved, the number of events will be the same plus one. If the application is not approved, the number of rejected applications will be the same plus one.
     *
     * @throws NoEventRequestException If there are no requests, the error will be reported.
     */
    EventRequest updateEventRequest(Status status, LocalDate date, String message) throws NoEventRequestException;

    /**
     * @pre true.
     * @post the number of attendees registered for an event will be the same plus one.
     * If the maximum number of registered attendees has been exceeded, they will be added as substitutes.
     *
     * @throws AttendeeNotFoundException If the attendee does not exist, the error will be reported.
     * @throws EventNotFoundException If the event does not exist, the error will be reported.
     * @throws NotAllowedException If the event does not admit attendees, the error will be reported.
     * @throws AttendeeAlreadyInEventException If the attendee is already registered in that event, the error will be reported
     */
    void signUpEvent(String attendeeId, String eventId) throws AttendeeNotFoundException, EventNotFoundException, NotAllowedException, AttendeeAlreadyInEventException;

    /**
     * @pre true.
     * @post
     * @return returns a real number with the percentage of requests that have not been approved.
     */
    double getPercentageRejectedRequests();

    /**
     * @pre true.
     * @post
     * @return returns an iterator to loop through all the rejected requests.
     * @throws NoEventRequestException If there are no events, the error will be reported.
     */
    Iterator<EventRequest> getRejectedRequests() throws NoEventRequestException;

    /**
     * @pre the entity exists.
     * @post
     * @return returns an iterator to loop through all the events of an entity.
     * @throws NoEventsException If there are no events, the error will be reported.
     */
    Iterator<Event> getEventsByEntity(String entityId) throws NoEventsException;

    /**
     * @pre true.
     * @post
     * @return returns an iterator to loop through all events.
     * @throws NoEventsException If there are no events, the error will be reported.
     */
    Iterator<Event> getAllEvents() throws NoEventsException;

    /**
     * @pre the attendee exists.
     * @post
     *
     * @return returns an iterator for looping through events attended by an attendee.
     * @throws NoEventsException If the attendee has not participated in any event, the error will be reported
     */
    Iterator<Event> getEventsByAttendee(String attendeeId) throws NoEventsException;

    /**
     * @pre true.
     * @post the ratings will be the same plus one.
     * @throws AttendeeNotFoundException If the attendee does not exist, the error will be reported.
     * @throws EventNotFoundException If the event does not exist, the error will be reported.
     * @throws AttendeeNotInEventException If the attendee did not participate in the event, the error will be reported.
     */
    void addRating(String attendeeId, String eventId, Rating rating, String message) throws AttendeeNotFoundException, EventNotFoundException, AttendeeNotInEventException;

    /**
     * @pre true.
     * @post
     *
     * @return returns an iterator to loop through the ratings of an event.
     * @throws EventNotFoundException If the event does not exist, the error will be reported.
     * @throws NoRatingsException If there are no ratings, the error will be reported.
     */
    Iterator<uoc.ds.pr.model.Rating> getRatingsByEvent(String eventId) throws EventNotFoundException, NoRatingsException;

    /**
     * @pre true.
     * @post
     * If there is a tie, the one that has signed up the most times before is provided.
     * @return returns the attendee who has attended the most events, the most active attendee.
     * @throws AttendeeNotFoundException If none exist, the error will be reported.
     */
    Attendee mostActiveAttendee() throws AttendeeNotFoundException;

    /**
     * @pre true.
     * @post
     * @return returns the highest-ranked event.
     * @throws EventNotFoundException If no event exists, the error will be reported.
     */
    Event bestEvent() throws EventNotFoundException;


    ///////////////////////////////////////////////////////////////////
    // AUXILIARY OPERATIONS
    ///////////////////////////////////////////////////////////////////

    int numEntities();
    int numAttendees();
    int numRequests();
    int numEvents();
    int numEventsByAttendee(String attendeeId);
    int numAttendeesByEvent(String eventId);
    int numSubstitutesByEvent(String eventId);
    int numEventsByEntity(String entityId);
    int numRejectedRequests();

    int numRatingsByEvent(String eventId);

    Entity getEntity(String entityId);
    Attendee getAttendee(String attendeeId);

    Event getEvent(String eventId);

}



