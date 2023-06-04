package uoc.ds.pr.model;

import uoc.ds.pr.UniversityEvents;

public class Organism extends Entity {

    public Organism(String idUser, String name, String surname) {
        super(idUser, name, surname, UniversityEvents.EntityType.OTHER);
    }
}
