package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.List;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.UniversityEvents;

public abstract class Entity {
    private String id;
    private String name;
    private String description;

    private UniversityEvents.EntityType entityType;

    private final List<Event> events;

	public Entity(String idUser, String name, String description, UniversityEvents.EntityType entityType) {
        this.setId(idUser);
        this.setName(name);
        this.setDescription(description);
        this.setEntityType(entityType);
        this.events = new LinkedList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    public boolean is(String entityId) {
        return id.equals(entityId);
    }
    public void setEntityType(UniversityEvents.EntityType entityType) {
        this.entityType = entityType;
    }


    public void addEvent(Event event) {
        events.insertEnd(event);
    }

    public Iterator<Event> events() {
        return events.values();
    }

    public int numEvents() {
        return events.size();
    }

    public UniversityEvents.EntityType getEntityType() {
        return entityType;
    }

    public int getNumAttendees(){
        // Sin implementar
        return 0;
    }
}
