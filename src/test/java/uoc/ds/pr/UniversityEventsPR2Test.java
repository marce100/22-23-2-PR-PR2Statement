package uoc.ds.pr;

import edu.uoc.ds.traversal.Iterator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uoc.ds.pr.exceptions.NoEntitiesException;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.*;


import static uoc.ds.pr.util.DateUtils.createLocalDate;

public class UniversityEventsPR2Test extends UniversityEventsPR1Test {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        super.universityEvents = FactoryUniversityEventsClub.getUniversityEventsPR2();
    }

    @After
    public void tearDown() {
        this.universityEvents = null;
    }


    public void initialState() {
        Assert.assertEquals(5, universityEvents.numRoles());
        Assert.assertEquals(7, universityEvents.numWorkers());

    }


    @Test
    public void addRoleTest() {
        // GIVEN:
        initialState();
        //
        universityEvents.addRole("R6", "trainer");
        Assert.assertEquals(6, universityEvents.numRoles());

        universityEvents.addRole("R7", "XXXXX");
        Assert.assertEquals(7, universityEvents.numRoles());
        universityEvents.addRole("R7", "Agent");
        Assert.assertEquals(7, universityEvents.numRoles());
        Role r7 = universityEvents.getRole("R7");
        Assert.assertEquals("Agent", r7.getName());
    }

    @Test
    public void adWorkerTest() {
        // GIVEN:
        initialState();
        //

        Assert.assertEquals(2, universityEvents.numWorkersByRole("R1"));
        Assert.assertEquals(1, universityEvents.numWorkersByRole("R2"));
        Assert.assertEquals(2, universityEvents.numWorkersByRole("R3"));
        Assert.assertEquals(2, universityEvents.numWorkersByRole("R4"));

        ////
        // add W89
        ////
        universityEvents.addWorker("W89", "Josep", "Paradells",
                createLocalDate("23-04-1955"), "R1");

        Assert.assertEquals(8, universityEvents.numWorkers());
        Assert.assertEquals(3, universityEvents.numWorkersByRole("R1"));

        Worker workerW89 = universityEvents.getWorker("W89");
        Assert.assertEquals("Josep", workerW89.getName());

        ////
        // add W99
        ////
        universityEvents.addWorker("W99", "Oscar", "XXXXXXX",
                createLocalDate("23-04-1945"), "R2");

        Assert.assertEquals(9, universityEvents.numWorkers());

        Assert.assertEquals(3, universityEvents.numWorkersByRole("R1"));
        Assert.assertEquals(2, universityEvents.numWorkersByRole("R2"));
        Assert.assertEquals(2, universityEvents.numWorkersByRole("R3"));
        Assert.assertEquals(2, universityEvents.numWorkersByRole("R4"));

        Worker workerW99 = universityEvents.getWorker("W99");
        Assert.assertEquals("Oscar", workerW99.getName());

        ////
        // update W99
        ////
        universityEvents.addWorker("W99", "Oscar", "Sánchez",
                createLocalDate("25-04-1945"), "R2");
        Assert.assertEquals(9, universityEvents.numWorkers());

        Assert.assertEquals(3, universityEvents.numWorkersByRole("R1"));
        Assert.assertEquals(2, universityEvents.numWorkersByRole("R2"));
        Assert.assertEquals(2, universityEvents.numWorkersByRole("R3"));
        Assert.assertEquals(2, universityEvents.numWorkersByRole("R4"));

        workerW99 = universityEvents.getWorker("W99");
        Assert.assertEquals("Oscar", workerW99.getName());
        Assert.assertEquals("Sánchez", workerW99.getSurname());
        Assert.assertEquals("R2", workerW99.getRole().getId());

        ////
        // update W99 - update Role
        ////
        universityEvents.addWorker("W99", "Oscar", "Sánchez",
                createLocalDate("25-04-1945"), "R1");
        Assert.assertEquals(9, universityEvents.numWorkers());
        Assert.assertEquals(4, universityEvents.numWorkersByRole("R1"));
        Assert.assertEquals(1, universityEvents.numWorkersByRole("R2"));
        Assert.assertEquals(2, universityEvents.numWorkersByRole("R3"));
        Assert.assertEquals(2, universityEvents.numWorkersByRole("R4"));

        workerW99 = universityEvents.getWorker("W99");
        Assert.assertEquals("Oscar", workerW99.getName());
        Assert.assertEquals("Sánchez", workerW99.getSurname());
        Assert.assertEquals("R1", workerW99.getRole().getId());

    }

    @Test
    public void addFacilityTest() {

        // GIVEN:
        initialState();
        //

        universityEvents.addFacility("F5", "STUDY-005", "DESCRIPTION 1", UniversityEvents.InstallationType.CASE_STUDY_CLASSROOM);
        Assert.assertEquals(1, universityEvents.numFacilities());

        universityEvents.addFacility("F7", "CONFERENCE-007", "DESCRIPTION 7", UniversityEvents.InstallationType.CONFERENCE_ROOM);
        Assert.assertEquals(2, universityEvents.numFacilities());

        universityEvents.addFacility("F9", "XXXXXXXXXX", "YYYYYYY", UniversityEvents.InstallationType.SEMINAR_CLASSROOM);
        Assert.assertEquals(3, universityEvents.numFacilities());
        universityEvents.addFacility("F9", "SEMINAR-009", "DESCRIPTION 9", UniversityEvents.InstallationType.CASE_STUDY_CLASSROOM);
        Assert.assertEquals(3, universityEvents.numFacilities());
        Assert.assertEquals("SEMINAR-009", universityEvents.getFacility("F9").getName());
        Assert.assertEquals("DESCRIPTION 9", universityEvents.getFacility("F9").getDescription());
        Assert.assertEquals(UniversityEvents.InstallationType.CASE_STUDY_CLASSROOM, universityEvents.getFacility("F9").getFacilityType());

    }

    @Test
    public void assignWorkerTest() throws DSException {
        // GIVEN:
        initialState();
        //
        super.updateEventRequestTest();

        Assert.assertThrows(WorkerNotFoundException.class, () ->
                universityEvents.assignWorker("WP-XXXXXXXXXXX", "EV-1103"));

        Assert.assertThrows(EventNotFoundException.class, () ->
                universityEvents.assignWorker("DNIW1", "EV-XXXXXXX1103"));

        universityEvents.assignWorker("DNIW1", "EV-300");
        universityEvents.assignWorker("DNIW2", "EV-300");
        universityEvents.assignWorker("DNIW3", "EV-300");
        universityEvents.assignWorker("DNIW4", "EV-300");

        universityEvents.assignWorker("DNIW5", "EV-100");
        universityEvents.assignWorker("DNIW6", "EV-100");


        Assert.assertThrows(WorkerAlreadyAssignedException.class, () ->
                universityEvents.assignWorker("DNIW1", "EV-300"));

        Assert.assertEquals(4, universityEvents.numWorkersByEvent("EV-300"));
        Assert.assertEquals(2, universityEvents.numWorkersByEvent("EV-100"));

    }


    @Test
    public void getWorkersByEventTest() throws DSException {
        initialState();
        assignWorkerTest();

        Assert.assertThrows(EventNotFoundException.class, () ->
                universityEvents.getWorkersByEvent("EV-900"));

        Assert.assertThrows(NoWorkersException.class, () ->
                universityEvents.getWorkersByEvent("EV-400"));

        Iterator<Worker> itEV100 = universityEvents.getWorkersByEvent("EV-100");
        Assert.assertEquals("DNIW5", itEV100.next().getWorkerId());
        Assert.assertEquals("DNIW6", itEV100.next().getWorkerId());
        Assert.assertFalse(itEV100.hasNext());

        Iterator<Worker> itEV300 = universityEvents.getWorkersByEvent("EV-300");
        Assert.assertEquals("DNIW1", itEV300.next().getWorkerId());
        Assert.assertEquals("DNIW2", itEV300.next().getWorkerId());
        Assert.assertEquals("DNIW3", itEV300.next().getWorkerId());
        Assert.assertEquals("DNIW4", itEV300.next().getWorkerId());
        Assert.assertFalse(itEV300.hasNext());
    }

    @Test
    public void getWorkersByRole() throws DSException {
        initialState();

        Assert.assertThrows(NoWorkersException.class, () ->
                universityEvents.getWorkersByRole("R5"));

        Iterator<Worker> itR1 = universityEvents.getWorkersByRole("R1");
        Assert.assertEquals("DNIW1", itR1.next().getWorkerId());
        Assert.assertEquals("DNIW5", itR1.next().getWorkerId());
        Assert.assertFalse(itR1.hasNext());

        Iterator<Worker> itR2 = universityEvents.getWorkersByRole("R3");
        Assert.assertEquals("DNIW3", itR2.next().getWorkerId());
        Assert.assertEquals("DNIW4", itR2.next().getWorkerId());
        Assert.assertFalse(itR2.hasNext());

    }

    @Test
    public void getLevelByEntityTest() {
        Assert.assertThrows(EntityNotFoundException.class, () ->
                universityEvents.getLevelByEntity("EV-900"));

        Assert.assertEquals(UniversityEventsPR2.Level.BRONZE, universityEvents.getLevelByEntity("idEntity3"));
        Assert.assertEquals(UniversityEventsPR2.Level.BRONZE, universityEvents.getLevelByEntity("idEntity1"));

    }

    @Test
    public void getSubstitutesByEventTest() throws DSException {
        super.signUpEventTest();
        Assert.assertThrows(EventNotFoundException.class, () ->
                universityEvents.getSubstitutesByEvent("EV-999"));


        Assert.assertThrows(NoSubstitutesException.class, () ->
                universityEvents.getSubstitutesByEvent("EV-300"));

        Iterator<Attendee> it = universityEvents.getSubstitutesByEvent("EV-100");
        Assert.assertEquals("idAttendee6", it.next().getId());
        Assert.assertEquals("idAttendee7", it.next().getId());
        Assert.assertFalse(it.hasNext());

    }


    @Test
    public void getAttendeeByEventTest() throws DSException {
        super.signUpEventTest();

        Assert.assertThrows(EventNotFoundException.class, () ->
                universityEvents.getAttendeeByEvent("+34 666772811","EV-999"));

        Assert.assertThrows(AttendeeNotFoundException.class, () ->
                universityEvents.getAttendeeByEvent("+34 000000000","EV-100"));


        Attendee attendee1 = universityEvents.getAttendeeByEvent("+34 666772811","EV-100");
        Assert.assertEquals("idAttendee1", attendee1.getId());
    }

    @Test
    public void getAttendeesByEventTest() throws DSException {
        super.signUpEventTest();

        Assert.assertThrows(EventNotFoundException.class, () ->
                universityEvents.getAttendeesByEvent("EV-999"));

        Assert.assertThrows(NoAttendeesException.class, () ->
                universityEvents.getAttendeesByEvent("EV-400"));

        Iterator<Enrollment> it = universityEvents.getAttendeesByEvent("EV-100");

        Enrollment enrollment2 = it.next();
        Assert.assertEquals("idAttendee2", enrollment2.getAttendee().getId());
        Assert.assertFalse(enrollment2.isSubstitute());


        Enrollment enrollment5 = it.next();
        Assert.assertEquals("idAttendee5", enrollment5.getAttendee().getId());
        Assert.assertFalse(enrollment5.isSubstitute());

        Enrollment enrollment7 = it.next();
        Assert.assertEquals("idAttendee7", enrollment7.getAttendee().getId());
        Assert.assertTrue(enrollment7.isSubstitute());

        Enrollment enrollment4 = it.next();
        Assert.assertEquals("idAttendee4", enrollment4.getAttendee().getId());
        Assert.assertFalse(enrollment4.isSubstitute());

        Enrollment enrollment6 = it.next();
        Assert.assertEquals("idAttendee6", enrollment6.getAttendee().getId());
        Assert.assertTrue(enrollment6.isSubstitute());

        Enrollment enrollment1 = it.next();
        Assert.assertEquals("idAttendee1", enrollment1.getAttendee().getId());
        Assert.assertFalse(enrollment1.isSubstitute());

        Enrollment enrollment3 = it.next();
        Assert.assertEquals("idAttendee3", enrollment3.getAttendee().getId());
        Assert.assertFalse(enrollment3.isSubstitute());

        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void getBest5EntitiesTest() throws DSException {

        Assert.assertThrows(NoEntitiesException.class, () ->
                universityEvents.getBest5Entities());

        super.signUpEventTest();

        Iterator<Entity> it = universityEvents.getBest5Entities();
        Entity entity1 = it.next();
        Assert.assertEquals("idEntity1", entity1.getId());
        Assert.assertEquals(10, entity1.getNumAttendees());
        Assert.assertFalse(it.hasNext());

        this.universityEvents.signUpEvent("idAttendee2", "EV-500");
        this.universityEvents.signUpEvent("idAttendee3", "EV-500");
        this.universityEvents.signUpEvent("idAttendee4", "EV-500");
        this.universityEvents.signUpEvent("idAttendee5", "EV-500");

        it = universityEvents.getBest5Entities();
        entity1 = it.next();
        Assert.assertEquals("idEntity1", entity1.getId());
        Assert.assertEquals(10, entity1.getNumAttendees());

        Entity entity3 = it.next();
        Assert.assertEquals("idEntity3", entity3.getId());
        Assert.assertEquals(4, entity3.getNumAttendees());

        Assert.assertFalse(it.hasNext());

        universityEvents.addAttendee("idAttendee10", "Julio", "Sallent", createLocalDate("07-01-1932"), "+34 666772811");
        universityEvents.addAttendee("idAttendee20", "Sara", "Casals", createLocalDate("09-07-1988"), "+34 615872234");
        universityEvents.addAttendee("idAttendee30", "Clement", "Padró", createLocalDate("02-06-1992"), "+34 6150562");
        universityEvents.addAttendee("idAttendee40", "Marin", "Padró", createLocalDate("15-01-2005"), "+34 6872223");
        universityEvents.addAttendee("idAttendee50", "Jose Maria", "Marieta", createLocalDate("23-04-2010"), "+34 76222234");
        universityEvents.addAttendee("idAttendee60", "Borja", "Garcia", createLocalDate("23-04-2001"), "934699982");
        universityEvents.addAttendee("idAttendee70", "Cristian", "Gimenez", createLocalDate("23-03-2005"), "937776988");

        this.universityEvents.signUpEvent("idAttendee10", "EV-500");
        this.universityEvents.signUpEvent("idAttendee20", "EV-500");
        this.universityEvents.signUpEvent("idAttendee30", "EV-500");
        this.universityEvents.signUpEvent("idAttendee40", "EV-500");
        this.universityEvents.signUpEvent("idAttendee50", "EV-500");
        this.universityEvents.signUpEvent("idAttendee60", "EV-500");
        this.universityEvents.signUpEvent("idAttendee70", "EV-500");


        it = universityEvents.getBest5Entities();
        entity3 = it.next();
        Assert.assertEquals("idEntity3", entity3.getId());
        Assert.assertEquals(11, entity3.getNumAttendees());

        entity1 = it.next();
        Assert.assertEquals("idEntity1", entity1.getId());
        Assert.assertEquals(10, entity1.getNumAttendees());

        Assert.assertFalse(it.hasNext());
    }


    @Test
    public void getBestEvent() throws DSException {

        Assert.assertThrows(NoEventsException.class, () ->
                universityEvents.getBestEventByNumAttendees());

        super.signUpEventTest();
        Event event = universityEvents.getBestEventByNumAttendees();
        Assert.assertEquals("EV-100", event.getEventId());
        Assert.assertEquals(5, event.numAttendees());
    }

}