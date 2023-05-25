package uoc.ds.pr;

import edu.uoc.ds.traversal.Iterator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.*;
import uoc.ds.pr.model.Rating;
import uoc.ds.pr.util.ResourceUtil;

import static uoc.ds.pr.UniversityEvents.*;
import static uoc.ds.pr.util.DateUtils.createLocalDate;

public class UniversityEventsPR1Test {

    protected UniversityEventsPR2 universityEvents;

    @Before
    public void setUp() throws Exception {
        this.universityEvents = FactoryUniversityEventsClub.getUniversityEvents();
    }

    @After
    public void tearDown() {
        this.universityEvents = null;
    }



    @Test
    public void addEntityTest() {
        this.universityEvents.addEntity("idEntity1000", "Robert López", "Behavioural design", UniversityEvents.EntityType.PROFESSOR);
        Professor professor1 = (Professor) this.universityEvents.getEntity("idEntity1000");
        Assert.assertEquals("Robert López", professor1.getName());
        Assert.assertEquals("Behavioural design", professor1.getDescription());
        Assert.assertEquals(6, this.universityEvents.numEntities());


        this.universityEvents.addEntity("idEntity9999", "XXXXX", "YYYYY", UniversityEvents.EntityType.OTHER);
        Organism organism = (Organism) this.universityEvents.getEntity("idEntity9999");
        Assert.assertEquals("XXXXX", organism.getName());
        Assert.assertEquals("YYYYY", organism.getDescription());
        Assert.assertEquals(7, this.universityEvents.numEntities());

        this.universityEvents.addEntity("idEntity9999", "Lluis Casals", "ADaS Lab", EntityType.OTHER);
        organism = (Organism) this.universityEvents.getEntity("idEntity9999");
        Assert.assertEquals("Lluis Casals", organism.getName());
        Assert.assertEquals("ADaS Lab", organism.getDescription());
        Assert.assertEquals(7, this.universityEvents.numEntities());

        this.universityEvents.addEntity("idEntity200", "Maria Sabater", "Lore ipsum", EntityType.STUDENT);
        Student student1 = (Student) this.universityEvents.getEntity("idEntity200");
        Assert.assertEquals("Maria Sabater", student1.getName());
        Assert.assertEquals("Lore ipsum", student1.getDescription());
        Assert.assertEquals(8, this.universityEvents.numEntities());

        this.universityEvents.addEntity("idEntity400", "Miriam López", "Lore ipsum II", EntityType.STUDENT);
        Student student2 = (Student) this.universityEvents.getEntity("idEntity400");
        Assert.assertEquals("Miriam López", student2.getName());
        Assert.assertEquals("Lore ipsum II", student2.getDescription());
        Assert.assertEquals(9, this.universityEvents.numEntities());

        this.universityEvents.addEntity("idEntity400", "Miriam López", "Lore ipsum II", EntityType.PROFESSOR);
        Professor professor2 = (Professor) this.universityEvents.getEntity("idEntity400");
        Assert.assertEquals("Miriam López", professor2.getName());
        Assert.assertEquals("Lore ipsum II", professor2.getDescription());
        Assert.assertEquals(9, this.universityEvents.numEntities());

    }


    @Test
    public void addAttendeeTest() {

        universityEvents.addAttendee("idAttendee1", "Jesus", "Sallent", createLocalDate("07-01-1932"), "phone11");

        this.universityEvents.addAttendee("idAttendee1000", "Blai", "Gómez", createLocalDate("03-04-1955"), "phone1000");
        Assert.assertEquals("Blai", this.universityEvents.getAttendee("idAttendee1000").getName());
        Assert.assertEquals("Gómez", this.universityEvents.getAttendee("idAttendee1000").getSurname());
        Assert.assertEquals(8, this.universityEvents.numAttendees());

        this.universityEvents.addAttendee("idAttendee9999", "XXXXX", "YYYYY", createLocalDate("13-06-1975"), "phone9999");
        Assert.assertEquals("XXXXX", this.universityEvents.getAttendee("idAttendee9999").getName());
        Assert.assertEquals("YYYYY", this.universityEvents.getAttendee("idAttendee9999").getSurname());
        Assert.assertEquals(9, this.universityEvents.numAttendees());

        this.universityEvents.addAttendee("idAttendee9999", "Alba", "Rema", createLocalDate("23-07-1992"), "phonw9999");
        Assert.assertEquals("Alba", this.universityEvents.getAttendee("idAttendee9999").getName());
        Assert.assertEquals("Rema", this.universityEvents.getAttendee("idAttendee9999").getSurname());
        Assert.assertEquals(9, this.universityEvents.numAttendees());
    }

    @Test
    public void addEventRequestTest() throws DSException {

        this.universityEvents.addEventRequest("ER-100", "EV-100", "idEntity1",
                "Formula Student Spain is a competition that challenges teams of university students...",
                InstallationType.CONFERENCE_ROOM,
                ResourceUtil.getFlag(FLAG_COMPUTER, FLAG_AUXILIARY_MIC, FLAG_TOUCH_SCREEN), 5,
                createLocalDate("10-04-2023"), createLocalDate("15-04-2023"), ALLOW_REGISTER);
        Assert.assertEquals(1, this.universityEvents.numRequests());


        Assert.assertThrows(EntityNotFoundException.class, () ->
                this.universityEvents.addEventRequest("ER-XXX", "EV-XXX", "idEntityXXXX",
                        "lore ipsum ...",
                        InstallationType.CONFERENCE_ROOM,
                        ResourceUtil.getFlag(FLAG_COMPUTER, FLAG_AUXILIARY_MIC, FLAG_TOUCH_SCREEN), 100,
                        createLocalDate("10-04-2023"), createLocalDate("15-04-2023"), ALLOW_REGISTER));
        Assert.assertEquals(1, this.universityEvents.numRequests());

        this.universityEvents.addEventRequest("ER-200", "EV-200", "idEntity1",
                "Joy of learning ...",
                InstallationType.CONFERENCE_ROOM,
                ResourceUtil.getFlag(FLAG_ALL_OPTS), 50,
                createLocalDate("10-06-2023"), createLocalDate("15-06-2023"), ALLOW_REGISTER);
        Assert.assertEquals(2, this.universityEvents.numRequests());

        this.universityEvents.addEventRequest("ER-300", "EV-300", "idEntity1",
                "Labs for learning ...",
                InstallationType.SEMINAR_CLASSROOM,
                ResourceUtil.getFlag(FLAG_ALL_OPTS), 50,
                createLocalDate("10-06-2023"), createLocalDate("15-06-2023"), ALLOW_REGISTER);
        Assert.assertEquals(3, this.universityEvents.numRequests());

        this.universityEvents.addEventRequest("ER-400", "EV-400", "idEntity3",
                "Labs for gaming ...",
                InstallationType.LECTURE_CLASSROOM,
                ResourceUtil.getFlag(FLAG_TOUCH_SCREEN, FLAG_AUXILIARY_MIC, FLAG_VIDEO_PROJECTOR), 50,
                createLocalDate("10-06-2023"), createLocalDate("15-06-2023"), !ALLOW_REGISTER);
        Assert.assertEquals(4, this.universityEvents.numRequests());

        this.universityEvents.addEventRequest("ER-500", "EV-500", "idEntity3",
                "Labs for gaming ...",
                InstallationType.LECTURE_CLASSROOM,
                ResourceUtil.getFlag(FLAG_ALL_OPTS), 10,
                createLocalDate("10-07-2023"), createLocalDate("15-07-2023"), ALLOW_REGISTER);
        Assert.assertEquals(5, this.universityEvents.numRequests());

    }

    @Test
    public void updateEventRequestTest() throws DSException {
        Assert.assertEquals(0, this.universityEvents.numRequests());
        addEventRequestTest();
        Assert.assertEquals(5, this.universityEvents.numRequests());

        EventRequest request1 = this.universityEvents.updateEventRequest(Status.ENABLED, createLocalDate("10-04-2023"), "not appropriate");
        Assert.assertEquals(4, this.universityEvents.numRequests());
        Assert.assertEquals("ER-100", request1.getRequestId());
        Assert.assertEquals(1, this.universityEvents.numEvents());
        Assert.assertEquals(0, this.universityEvents.numRejectedRequests());


        EventRequest request2 = this.universityEvents.updateEventRequest(Status.ENABLED, createLocalDate("12-04-2023"), "OK!!!");
        Assert.assertEquals(3, this.universityEvents.numRequests());
        Assert.assertEquals("ER-300", request2.getRequestId());
        Assert.assertEquals(2, this.universityEvents.numEvents());
        Assert.assertEquals(0, this.universityEvents.numRejectedRequests());

        EventRequest request3 = this.universityEvents.updateEventRequest(Status.ENABLED, createLocalDate("13-04-2023"), "OK!!!");
        Assert.assertEquals(2, this.universityEvents.numRequests());
        Assert.assertEquals("ER-400", request3.getRequestId());
        Assert.assertEquals(3, this.universityEvents.numEvents());
        Assert.assertEquals(0, this.universityEvents.numRejectedRequests());

        EventRequest request4 = this.universityEvents.updateEventRequest(Status.DISABLED, createLocalDate("14-04-2023"), "OK!!!");
        Assert.assertEquals(1, this.universityEvents.numRequests());
        Assert.assertEquals("ER-200", request4.getRequestId());
        Assert.assertEquals(3, this.universityEvents.numEvents());
        Assert.assertEquals(1, this.universityEvents.numRejectedRequests());


        EventRequest request5 = this.universityEvents.updateEventRequest(Status.ENABLED, createLocalDate("14-04-2023"), "OK!!!");
        Assert.assertEquals(0, this.universityEvents.numRequests());
        Assert.assertEquals("ER-500", request5.getRequestId());
        Assert.assertEquals(4, this.universityEvents.numEvents());
        Assert.assertEquals(1, this.universityEvents.numRejectedRequests());

        Assert.assertThrows(NoEventRequestException.class, () ->
                this.universityEvents.updateEventRequest(Status.DISABLED,
                        createLocalDate("10-04-2023"), "not appropriate"));
    }

    @Test
    public void signUpEventTest() throws DSException {
        updateEventRequestTest();

        Assert.assertThrows(AttendeeNotFoundException.class, () ->
                this.universityEvents.signUpEvent("idAttendeeXXX", "EV-100"));

        Assert.assertThrows(EventNotFoundException.class, () ->
                this.universityEvents.signUpEvent("idAttendee01", "EV-XXX"));


        this.universityEvents.signUpEvent("idAttendee1", "EV-100");
        Assert.assertEquals(1, this.universityEvents.numEventsByAttendee("idAttendee1"));

        Assert.assertThrows(AttendeeAlreadyInEventException.class, () ->
                this.universityEvents.signUpEvent("idAttendee1", "EV-100"));

        this.universityEvents.signUpEvent("idAttendee1", "EV-300");
        Assert.assertEquals(2, this.universityEvents.numEventsByAttendee("idAttendee1"));

        Assert.assertThrows(NotAllowedException.class, () ->
                this.universityEvents.signUpEvent("idAttendee1", "EV-400"));

        Assert.assertEquals(2, this.universityEvents.numEventsByAttendee("idAttendee1"));


        this.universityEvents.signUpEvent("idAttendee2", "EV-100");
        this.universityEvents.signUpEvent("idAttendee3", "EV-100");
        this.universityEvents.signUpEvent("idAttendee4", "EV-100");
        this.universityEvents.signUpEvent("idAttendee5", "EV-100");

        Assert.assertEquals(5, this.universityEvents.numAttendeesByEvent("EV-100"));
        Assert.assertEquals(0, this.universityEvents.numSubstitutesByEvent("EV-100"));

        this.universityEvents.signUpEvent("idAttendee6", "EV-100");
        this.universityEvents.signUpEvent("idAttendee7", "EV-100");

        Assert.assertEquals(5, this.universityEvents.numAttendeesByEvent("EV-100"));
        Assert.assertEquals(2, this.universityEvents.numSubstitutesByEvent("EV-100"));

        this.universityEvents.signUpEvent("idAttendee2", "EV-300");
        this.universityEvents.signUpEvent("idAttendee3", "EV-300");

        Assert.assertEquals(3, this.universityEvents.numAttendeesByEvent("EV-300"));


    }

    @Test
    public void getPercentageRejectedRequestsTest() throws DSException {
        Assert.assertEquals(0.0, this.universityEvents.getPercentageRejectedRequests(), 0.1);
        updateEventRequestTest();
        Assert.assertEquals(0.25, this.universityEvents.getPercentageRejectedRequests(), 0.1);
    }


    @Test
    public void getRejectedRequestsTest() throws DSException {
        Assert.assertThrows(NoEventRequestException.class, () ->
                this.universityEvents.getRejectedRequests());

        updateEventRequestTest();
        Iterator<EventRequest> it = this.universityEvents.getRejectedRequests();
        EventRequest eventRequest = it.next();
        Assert.assertEquals("ER-200", eventRequest.getRequestId());

        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void getEventsByEntityTest() throws DSException {
        updateEventRequestTest();

        Assert.assertThrows(NoEventsException.class, () ->
                this.universityEvents.getEventsByEntity("idEntity2"));

        Iterator<Event> it1 = this.universityEvents.getEventsByEntity("idEntity1");
        Assert.assertEquals(2, this.universityEvents.numEventsByEntity("idEntity1"));
        Assert.assertEquals("EV-100", it1.next().getEventId());
        Assert.assertEquals("EV-300", it1.next().getEventId());
        Assert.assertFalse(it1.hasNext());

        Iterator<Event> it2 = this.universityEvents.getEventsByEntity("idEntity3");
        Assert.assertEquals(2, this.universityEvents.numEventsByEntity("idEntity3"));
        Assert.assertEquals("EV-400", it2.next().getEventId());
        Assert.assertEquals("EV-500", it2.next().getEventId());
        Assert.assertFalse(it2.hasNext());
    }


    @Test
    public void getAllEventsTest() throws DSException {
        Assert.assertThrows(NoEventsException.class, () ->
                this.universityEvents.getAllEvents());

        updateEventRequestTest();

        Iterator<Event> it = this.universityEvents.getAllEvents();
        Assert.assertEquals("EV-100", it.next().getEventId());
        Assert.assertEquals("EV-300", it.next().getEventId());
        Assert.assertEquals("EV-400", it.next().getEventId());
        Assert.assertEquals("EV-500", it.next().getEventId());
        Assert.assertFalse(it.hasNext());

    }

    @Test
    public void getEventsByAttendeeTest() throws DSException {
        Assert.assertThrows(NoEventsException.class, () ->
                this.universityEvents.getEventsByAttendee("idAttendee1"));

        signUpEventTest();

        Assert.assertEquals(2, this.universityEvents.numEventsByAttendee("idAttendee1"));
        Iterator<Event> it = this.universityEvents.getEventsByAttendee("idAttendee1");
        Assert.assertEquals("EV-100", it.next().getEventId());
        Assert.assertEquals("EV-300", it.next().getEventId());
        Assert.assertFalse(it.hasNext());

    }

    @Test
    public void addRatingTest() throws DSException {

        Assert.assertThrows(EventNotFoundException.class, () ->
                universityEvents.getRatingsByEvent("EV-1101"));
        Assert.assertThrows(EventNotFoundException.class, () ->
                universityEvents.getRatingsByEvent("EV-1102"));

        //
        signUpEventTest();
        //

        Assert.assertThrows(AttendeeNotInEventException.class, () ->
                this.universityEvents.addRating("idAttendee7", "EV-300",
                        UniversityEvents.Rating.TWO, "CHIPI - CHAPI"));


        Event eventEV100 = this.universityEvents.getEvent("EV-100");
        Event eventEV300 = this.universityEvents.getEvent("EV-300");
        Event eventEV400 = this.universityEvents.getEvent("EV-400");

        Assert.assertEquals(0, eventEV100.rating(), 0);

        this.universityEvents.addRating("idAttendee1", "EV-100",
                UniversityEvents.Rating.FIVE, "Very good");

        Assert.assertEquals(5, eventEV100.rating(), 0);

        this.universityEvents.addRating("idAttendee3", "EV-100",
                UniversityEvents.Rating.FOUR, "Good");

        Assert.assertEquals(4.5, eventEV100.rating(), 0);

        this.universityEvents.addRating("idAttendee2", "EV-300",
                UniversityEvents.Rating.TWO, "CHIPI - CHAPI");
        Assert.assertEquals(2, eventEV300.rating(), 0.09);
    }

    @Test
    public void getRatingsByEventTest() throws DSException {
        addRatingTest();

        this.universityEvents.getRatingsByEvent("EV-100");
        Assert.assertEquals(2, universityEvents.numRatingsByEvent("EV-100"));

        Iterator<Rating> it = this.universityEvents.getRatingsByEvent("EV-100");

        Assert.assertEquals("idAttendee1", it.next().getAttendee().getId());
        Assert.assertEquals("idAttendee3", it.next().getAttendee().getId());
        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void mostActiveAttendeeTest() throws DSException {
        signUpEventTest();

        Attendee mostActiveAttendee = null;
        mostActiveAttendee = universityEvents.mostActiveAttendee();
        Assert.assertEquals("idAttendee1", mostActiveAttendee.getId());
        Assert.assertEquals(2, mostActiveAttendee.numEvents());

        universityEvents.signUpEvent("idAttendee2", "EV-500");

        mostActiveAttendee = universityEvents.mostActiveAttendee();
        Assert.assertEquals("idAttendee2", mostActiveAttendee.getId());
        Assert.assertEquals(3, mostActiveAttendee.numEvents());
    }

    @Test
    public void bestEventTest() throws DSException {
        addRatingTest();
        Event bestEvent = null;

        bestEvent = universityEvents.bestEvent();
        Assert.assertEquals("EV-100", bestEvent.getEventId());
        Assert.assertEquals(4.5, bestEvent.rating(), 0.01);


        this.universityEvents.addRating("idAttendee2", "EV-100",
                UniversityEvents.Rating.ONE, "BAD !!!");

        Event eventEV100 = universityEvents.getEvent("EV-100");
        Assert.assertEquals(3.33, eventEV100.rating(), 0.01);

        bestEvent = universityEvents.bestEvent();
        Assert.assertEquals("EV-100", bestEvent.getEventId());
        Assert.assertEquals(3.33, bestEvent.rating(), 0.09);

        this.universityEvents.addRating("idAttendee1", "EV-300",
                UniversityEvents.Rating.FIVE, "VERY GOOD !!!");

        bestEvent = universityEvents.bestEvent();
        Assert.assertEquals("EV-300", bestEvent.getEventId());
        Assert.assertEquals(3.5, bestEvent.rating(), 0.01);
        Assert.assertEquals(3.33, eventEV100.rating(), 0.01);

    }

}