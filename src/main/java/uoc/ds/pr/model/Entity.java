package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.List;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.UniversityEvents;
import uoc.ds.pr.UniversityEventsPR2;
import uoc.ds.pr.UniversityEventsPR2Impl;

public abstract class Entity {
    private String id;
    private String name;
    private String description;

    private UniversityEvents.EntityType entityType;

    private UniversityEventsPR2.Level level;

    private final List<Event> events;

	public Entity(String idUser, String name, String description, UniversityEvents.EntityType entityType) {
        this.setId(idUser);
        this.setName(name);
        this.setDescription(description);
        this.setEntityType(entityType);
        this.events = new LinkedList<>();
        this.level = UniversityEventsPR2.Level.BRONZE;
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

//        System.out.println("-----Añado evento-----------------------------");
//        System.out.println(events.size());
//        System.out.println(level);

//            // Update level
//            int aux = events.size();
//            if (aux <= 5) setLevel(UniversityEventsPR2.Level.BRONZE);
//            else if (aux <= 10) setLevel(UniversityEventsPR2.Level.SILVER);
//            else if (aux <= 15) setLevel(UniversityEventsPR2.Level.GOLD);
//            else if (aux <= 20) setLevel(UniversityEventsPR2.Level.PLATINUM);
//            else setLevel(UniversityEventsPR2.Level.DIAMOND);


        events.insertEnd(event);




//        System.out.println(events.size());
//        System.out.println(level);
//        System.out.println("-----Fin añadir evento-----------------------------");

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

    public UniversityEventsPR2.Level getLevel() {
        return level;
    }

    public void setLevel(UniversityEventsPR2.Level level) {
        this.level = level;
    }
}
