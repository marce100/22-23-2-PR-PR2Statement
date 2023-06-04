package uoc.ds.pr.model;

import uoc.ds.pr.UniversityEvents;

public class Professor extends Entity {
    public Professor(String idUser, String name, String surname) {
        super(idUser, name, surname, UniversityEvents.EntityType.PROFESSOR);
    }
}
