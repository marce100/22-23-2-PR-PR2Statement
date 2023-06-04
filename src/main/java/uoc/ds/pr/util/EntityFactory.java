package uoc.ds.pr.util;

import uoc.ds.pr.UniversityEvents;
import uoc.ds.pr.model.Entity;
import uoc.ds.pr.model.Organism;
import uoc.ds.pr.model.Professor;
import uoc.ds.pr.model.Student;

public class EntityFactory {

    public static Entity getEntity(UniversityEvents.EntityType entityType, String id, String name, String description) {

        Entity entity = null;
        switch (entityType) {
            case PROFESSOR: entity = new Professor(id, name, description);break;
            case STUDENT: entity = new Student(id, name, description);break;
            default: entity = new Organism(id, name, description);break;
        }
        return entity;
    }

}
