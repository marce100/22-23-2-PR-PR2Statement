package uoc.ds.pr;

import edu.uoc.ds.adt.nonlinear.DictionaryAVLImpl;
import edu.uoc.ds.adt.nonlinear.HashTable;
import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.List;
import edu.uoc.ds.traversal.Iterator;
import edu.uoc.ds.traversal.IteratorArrayImpl;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.*;
import uoc.ds.pr.util.DSArray;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;


public class UniversityEventsPR2Impl extends UniversityEventsImpl  implements UniversityEventsPR2 {

    private final DSArray<Role> roles;
    private final DSArray<Facility> facilities;

    private final HashTable<String, Worker> workers;

    public UniversityEventsPR2Impl() {

        roles = new DSArray<>(MAX_NUM_ROLES);
        workers = new HashTable<>();
        facilities = new DSArray<>(MAX_NUM_FACILITIES);
    }

    @Override
    public void addRole(String id, String name) {
        Role role = getRole(id);
        if (role != null) {
            role.setName(name);
        } else {
            role = new Role(id, name);
            roles.put(id, role);
        }
    }

    @Override
    public void addWorker(String workerId, String name, String surname, LocalDate birthDay, String roleId) {
        Worker worker = getWorker(workerId);
        if (worker != null) {
            worker.setName(name);
            worker.setSurname(surname);
            worker.setBirthday(birthDay);
            worker.setRoleId(roleId);
        } else {
            worker = new Worker(workerId, name, surname, birthDay, roleId);
            workers.put(workerId, worker);
        }
    }

    @Override
    public void addFacility(String id, String name, String description, InstallationType type) {
        Facility facility = getFacility(id);
        if (facility != null) {
            facility.setName(name);
            facility.setDescription(description);
            facility.setFacilityType(type);
        } else {
            facility = new Facility(id, name, description, type);
            facilities.put(id, facility);
        }
    }

    @Override
    public void assignWorker(String workerId, String eventId) throws EventNotFoundException, WorkerNotFoundException, WorkerAlreadyAssignedException {

        Worker worker = workers.get(workerId);
        if (worker == null) throw new WorkerNotFoundException();

        Event event = (Event) getEvents().get(eventId);
        if (event == null) throw new EventNotFoundException();

        if (event.isInWorkers(workerId))
            throw new WorkerAlreadyAssignedException();

        event.setWorker(worker);

    }

    @Override
    public Iterator<Worker> getWorkersByEvent(String eventId) throws EventNotFoundException, NoWorkersException {

        Event event = (Event) getEvents().get(eventId);
        if (event == null) throw new EventNotFoundException();

        if (event.numWorkers()==0) throw new NoWorkersException();

        Iterator<Worker> iterator= event.getWorkers();

        return iterator;
    }


    @Override
    public Iterator<Worker> getWorkersByRole(String roleId) throws NoWorkersException {
        return null;
    }

    @Override
    public Level getLevelByEntity(String entityId) throws EntityNotFoundException {

        Entity entity = (Entity) getEntities().get(entityId);
        if (entity == null) throw new EntityNotFoundException();

        return entity.getLevel();
    }

    @Override
    public Iterator<Attendee> getSubstitutesByEvent(String eventId) throws EventNotFoundException, NoSubstitutesException {
        return null;
    }

    @Override
    public Attendee getAttendeeByEvent(String phone, String eventId) throws EventNotFoundException, AttendeeNotFoundException {

        Event event = (Event) getEvents().get(eventId);
        if (event == null) throw new EventNotFoundException();

        Enrollment enrollment = event.getEnrollments().get(phone);
        if (enrollment == null) throw new AttendeeNotFoundException();

        return enrollment.getAttendee();
    }

    @Override
    public Iterator<Enrollment> getAttendeesByEvent(String eventId) throws EventNotFoundException, NoAttendeesException {
        Event event = (Event) getEvents().get(eventId);
        if (event == null) throw new EventNotFoundException();

        if (event.numAttendees()==0) throw new NoAttendeesException();

        Iterator<Enrollment> iterator= event.getAttendees();

        return iterator;
    }

    @Override
    public Iterator<Entity> getBest5Entities() throws NoEntitiesException {

        // Devuelve un iterador para recorrer las cinco entidades que más asistentes aportan a la Universidad


        HashTable<String, Entity> entities = getEntities();
        DictionaryAVLImpl attendees = getAttendees();
        DictionaryAVLImpl events = getEvents();

        //If no entity, event, and/or attendee exists , an error will be indicated.
        if (entities.isEmpty() || attendees.isEmpty() || events.isEmpty()) throw new NoEntitiesException();


        System.out.println("-------------------------------------------");
        System.out.println("Total entities: " +entities.size());
        Iterator<Entity> i = entities.values();
        while (i.hasNext()){
            Entity e = i.next();
            System.out.println("entitiId: "+ e.getId() +
                               " Num asistentes: " + e.getNumAttendees() +
                               " Num eventos: " + e.numEvents()
            );
        }
        System.out.println("Total attendees: " +attendees.size());
        Iterator<Attendee> i2 = attendees.values();
        while (i2.hasNext()){
            Attendee a = i2.next();
            System.out.println("attendeeId: "+ a.getId() +
                    " Num eventos: " + a.numEvents()
            );
        }
        System.out.println("Total events: " +events.size());
        Iterator<Event> i3 = events.values();
        while (i3.hasNext()){
            Event e = i3.next();
            System.out.println("eventId: "+ e.getEventId() +
                    " Num Attendees: " + e.numAttendees() +" Num Substitutes: "+ e.numSubstitutes() + " Entity: " +e.getEntity().getId()
            );
        }





//        // Crear colección temporal para devolver ....................
//        java.util.List<Entity> entityList = new ArrayList<>();
//        Iterator<Entity> it = entities.values();
//        while (it.hasNext()){
//            Entity e = it.next();
//            if (e.getNumAttendees()>0) entityList.add(e);
//        }
//        Collections.sort(entityList, Comparator.comparingInt(Entity::getNumAttendees).reversed());
//
//        System.out.println("--");
//        for (Entity entity : entityList) {
//            System.out.println("entitiId: "+ entity.getId() +
//                    " Num asistentes: " + entity.getNumAttendees() +
//                    " Num eventos: " + entity.numEvents()
//            );
//        }
//        return new IteratorArrayImpl(entityList.toArray(), entityList.size(), 0);


        HashTable<String, Entity> entities2 = new HashTable<>();
        Iterator iterator = entities.values();
        while (iterator.hasNext() && entities2.size()<5){
            Entity entity = (Entity) iterator.next();
            if (entity.getNumAttendees()>0) {
                entities2.put(entity.getNumAttendees() + "", entity);
                System.out.println(entity.getNumAttendees());
            }
        }
        System.out.println("---");
        Iterator i0 = entities2.values();
        while (i0.hasNext()){
            Entity entity = (Entity) i0.next();
            System.out.println( "entitiId: "+ entity.getId() + " Num asistentes: " + entity.getNumAttendees() + " Num eventos: " + entity.numEvents() );
        }
        return entities2.values();



    }

    @Override
    public Event getBestEventByNumAttendees() throws NoEventsException {

        DictionaryAVLImpl events = getEvents();
        if (events.isEmpty()) throw new NoEventsException();

        Event found = null;
        Iterator<Event> i = getEvents().values();
        while (i.hasNext()){
            Event e = i.next();
            if (found == null || e.numAttendees() > found.numAttendees())
                found = e;
        }

        return found;
    }

    @Override
    public void addFollower(String followerId, RelatedNodeType relatedNodeTypeFollower, String followedId, RelatedNodeType relatedNodeTypeFollowed) throws FollowerNotFound, FollowedException {

    }

    @Override
    public Iterator<DSNode> getFollowers(String followedId, RelatedNodeType relatedNodeTypeFollowed) throws FollowerNotFound, NoFollowersException {
        return null;
    }

    @Override
    public Iterator<DSNode> getFolloweds(String followerId, RelatedNodeType relatedNodeTypeFollower) throws FollowerNotFound, NoFollowedException {
        return null;
    }

    @Override
    public Iterator<DSNode> recommendations(String followerId, RelatedNodeType relatedNodeTypeFollower) throws FollowerNotFound, NoFollowedException {
        return null;
    }

    @Override
    public Iterator<uoc.ds.pr.model.Rating> getRatingsOftheFollowed(String followerId, RelatedNodeType relatedNodeType) throws FollowerNotFound, NoFollowedException, NoRatingsException {
        return null;
    }

    @Override
    public int numRoles() {
        return roles.size();
    }

    @Override
    public int numWorkers() {
        return workers.size();
    }

    @Override
    public int numFacilities() { return facilities.size(); }

    @Override
    public Role getRole(String id) {
        return roles.get(id);
    }

    @Override
    public Worker getWorker(String id) {
        return workers.get(id);
    }

    @Override
    public Facility getFacility(String id) {
        return facilities.get(id);
    }

    @Override
    public int numWorkersByRole(String id) {
        return 0;
    }

    @Override
    public int numWorkersByEvent(String id) { return getEvent(id).numWorkers(); }

    @Override
    public int numFollowers(String id, RelatedNodeType relatedNodeType) {
        return 0;
    }

    @Override
    public int numFollowings(String id, RelatedNodeType relatedNodeType) {
        return 0;
    }
}
