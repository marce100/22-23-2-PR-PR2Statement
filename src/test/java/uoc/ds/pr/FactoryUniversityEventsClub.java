package uoc.ds.pr;

import static uoc.ds.pr.util.DateUtils.createLocalDate;

import org.junit.Assert;


public class FactoryUniversityEventsClub {


    public static UniversityEventsPR2 getUniversityEvents() throws Exception {
        UniversityEventsPR2 universityEvents;
        universityEvents = new UniversityEventsPR2Impl();


        ////
        //// ENTITIES
        ////
        universityEvents.addEntity("idEntity1", "Maria Simo", "AI for Human Well-being", UniversityEvents.EntityType.PROFESSOR);
        universityEvents.addEntity("idEntity2", "Àlex Lluna", "eHealhthLab ", UniversityEvents.EntityType.OTHER);
        universityEvents.addEntity("idEntity3", "Pepet Ferra", "Formula Student-UOC", UniversityEvents.EntityType.STUDENT);
        universityEvents.addEntity("idEntity4", "Joana Quilez", "Research group in Epidemiology", UniversityEvents.EntityType.PROFESSOR);
        universityEvents.addEntity("idEntity5", "Armand Morata", "Care and preparedness in the network society", UniversityEvents.EntityType.PROFESSOR);

        ////
        //// ATTENDEES
        ////
        universityEvents.addAttendee("idAttendee1", "Jesus", "Sallent", createLocalDate("07-01-1932"), "+34 666772811");
        universityEvents.addAttendee("idAttendee2", "Anna", "Casals", createLocalDate("09-07-1988"), "+34 615872234");
        universityEvents.addAttendee("idAttendee3", "Mariajo", "Padró", createLocalDate("02-06-1992"), "+34 6150562");
        universityEvents.addAttendee("idAttendee4", "Agustí", "Padró", createLocalDate("15-01-2005"), "+34 6872223");
        universityEvents.addAttendee("idAttendee5", "Pepet", "Marieta", createLocalDate("23-04-2010"), "+34 76222234");
        universityEvents.addAttendee("idAttendee6", "Emma", "Garcia", createLocalDate("23-04-2001"), "934699982");
        universityEvents.addAttendee("idAttendee7", "Pablo", "Gimenez", createLocalDate("23-03-2005"), "937776988");

        return universityEvents;
    }

    public static UniversityEventsPR2 getUniversityEventsPR2() throws Exception {

        UniversityEventsPR2 universityEventsPR2 = (UniversityEventsPR2)getUniversityEvents();
        ////
        /// Roles
        ////
        universityEventsPR2.addRole("R1", "referee");
        universityEventsPR2.addRole("R2", "referee senior");
        universityEventsPR2.addRole("R3", "maintenance");
        universityEventsPR2.addRole("R4", "administrative");
        universityEventsPR2.addRole("R5", "coordinator");


        ////
        /// Workers
        ////
        universityEventsPR2.addWorker("DNIW1", "Rafa", "Vidal",
                createLocalDate("23-04-2008"),  "R1");

        universityEventsPR2.addWorker("DNIW2", "Anna", "Agustí",
                createLocalDate("23-04-2000"), "R2");

        universityEventsPR2.addWorker("DNIW3", "Sebastià", "Sallent",
                createLocalDate("23-04-1960"),  "R3");

        universityEventsPR2.addWorker("DNIW4", "Jesus", "Alcober",
                createLocalDate("23-04-2000"), "R3");

        universityEventsPR2.addWorker("DNIW5", "Juan", "Hdez",
                createLocalDate("23-04-2000"), "R1");

        universityEventsPR2.addWorker("DNIW6", "Quim", "Valls",
                createLocalDate("23-04-1950"), "R4");

        universityEventsPR2.addWorker("DNIW7", "Enric", "Giró",
                createLocalDate("23-04-1955"), "R4");


        return universityEventsPR2;

    }

    public static UniversityEventsPR2 getUniversityEventsPR2Plus() throws Exception {
        UniversityEventsPR2 universityEventsPR2 = (UniversityEventsPR2)getUniversityEventsPR2();
        return universityEventsPR2;
    }

}