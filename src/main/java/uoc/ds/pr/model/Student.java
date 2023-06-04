package uoc.ds.pr.model;

import uoc.ds.pr.UniversityEvents;

public class Student extends Entity {

    public Student(String idUser, String name, String surname) {
        super(idUser, name, surname, UniversityEvents.EntityType.STUDENT);
    }
}
