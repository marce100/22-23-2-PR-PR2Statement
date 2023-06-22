package uoc.ds.pr;

import edu.uoc.ds.adt.helpers.Position;
import edu.uoc.ds.adt.nonlinear.DictionaryAVLImpl;
import edu.uoc.ds.adt.nonlinear.HashTable;
import edu.uoc.ds.adt.nonlinear.graphs.*;
import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.List;
import edu.uoc.ds.adt.sequential.Stack;
import edu.uoc.ds.adt.sequential.StackArrayImpl;
import edu.uoc.ds.exceptions.InvalidPositionException;
import edu.uoc.ds.traversal.BidirectionalIterator;
import edu.uoc.ds.traversal.Iterator;
import edu.uoc.ds.traversal.IteratorArrayImpl;
import edu.uoc.ds.traversal.Traversal;
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

    private final DirectedGraph<Follower, String> followers;

    public UniversityEventsPR2Impl() {

        roles = new DSArray<>(MAX_NUM_ROLES);
        workers = new HashTable<>();
        facilities = new DSArray<>(MAX_NUM_FACILITIES);

        followers = new DirectedGraphImpl<>();
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
            getRole(worker.getRoleId()).deleteWorker(worker);
            worker.setName(name);
            worker.setSurname(surname);
            worker.setBirthday(birthDay);
            worker.setRoleId(roleId);
            worker.setRole(getRole(roleId));
            getRole(roleId).addWorker(worker);
        } else {
            worker = new Worker(workerId, name, surname, birthDay, roleId);
            worker.setRole(getRole(roleId));
            getRole(roleId).addWorker(worker);
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

        LinkedList<Worker> workers = roles.get(roleId).getWorkers();

        if (workers.isEmpty()) throw new NoWorkersException();

        return workers.values();
    }

    @Override
    public Level getLevelByEntity(String entityId) throws EntityNotFoundException {

        Entity entity = (Entity) getEntities().get(entityId);
        if (entity == null) throw new EntityNotFoundException();

        return entity.getLevel();
    }

    @Override
    public Iterator<Attendee> getSubstitutesByEvent(String eventId) throws EventNotFoundException, NoSubstitutesException {

        DictionaryAVLImpl events = getEvents();
        Event event = (Event) events.get(eventId);

        if (event == null) throw new EventNotFoundException();
        if (event.numSubstitutes()==0) throw new NoSubstitutesException();

        List<Attendee> attendees = new LinkedList<>();
        HashTable<String, Enrollment> enrollments = event.getEnrollments();
        Iterator<Enrollment> iterator= enrollments.values();
        while (iterator.hasNext()){
            Enrollment enrollment = iterator.next();
            Attendee attendee = enrollment.getAttendee();
            if (enrollment.isSubstitute()) attendees.insertBeginning(attendee);
        }

        return attendees.values();
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

        HashTable<String, Entity> entities = getEntities();
        DictionaryAVLImpl attendees = getAttendees();
        DictionaryAVLImpl events = getEvents();

        // If no entity, event, and/or attendee exists , an error will be indicated.
        if (entities.isEmpty() || attendees.isEmpty() || events.isEmpty()) throw new NoEntitiesException();

        // Temp collection
        ArrayList<Entity> firstFiveEntities = new ArrayList<>();
        Iterator<Entity> it = entities.values();
        while (it.hasNext()){
            Entity e = it.next();
            if (e.getNumAttendees()>0) firstFiveEntities.add(e);
        }

        // Sort
        Collections.sort(firstFiveEntities, Comparator.comparingInt(Entity::getNumAttendees).reversed());

        return new IteratorArrayImpl(firstFiveEntities.toArray(), Math.min(firstFiveEntities.size(), 5), 0);
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

        //System.out.println("https://eimtgit.uoc.edu/DS/DSLib/-/tree/master");

        // Seguidor no existe
        if (relatedNodeTypeFollower.equals(RelatedNodeType.ENTITY) && !getEntities().containsKey(followerId) ) throw new FollowerNotFound();
        if (relatedNodeTypeFollower.equals(RelatedNodeType.ATTENDEE) && !getAttendees().containsKey(followerId) ) throw new FollowerNotFound();

        // Seguido no existe
        if (relatedNodeTypeFollowed.equals(RelatedNodeType.ENTITY) && !getEntities().containsKey(followedId)) throw new FollowedException();
        if (relatedNodeTypeFollowed.equals(RelatedNodeType.ATTENDEE) && !getAttendees().containsKey(followedId)) throw new FollowedException();

        Follower follower = new Follower(followerId,relatedNodeTypeFollower, followedId, relatedNodeTypeFollowed);
        /*Vertex<Follower> vFollower = */followers.newVertex(follower);




    }

    @Override
    public Iterator<DSNode> getFollowers(String followedId, RelatedNodeType relatedNodeTypeFollowed) throws FollowerNotFound, NoFollowersException {

        if (relatedNodeTypeFollowed.equals(RelatedNodeType.ENTITY) && !getEntities().containsKey(followedId)) throw new FollowerNotFound();
        if (relatedNodeTypeFollowed.equals(RelatedNodeType.ATTENDEE) && !getAttendees().containsKey(followedId)) throw new FollowerNotFound();
        if (numFollowers(followedId, relatedNodeTypeFollowed)==0) throw new NoFollowersException();

        LinkedList<DSNode> aux = new LinkedList<>();
        Iterator<Vertex<Follower>> it = followers.vertexs();
        while (it.hasNext()){
            Follower follower = it.next().getValue();
            if (follower.getFollowerId().equals(followedId) && follower.getRelatedNodeTypeFollowed().equals(relatedNodeTypeFollowed))
                aux.insertBeginning(new DSNode(follower.getFollowedId(),
                        ( relatedNodeTypeFollowed==RelatedNodeType.ENTITY ) ? getEntities().get(follower.getFollowedId()).getName():getAttendee(follower.getFollowedId()).getName() ));
        }
        return aux.values();

    }

    @Override
    public Iterator<DSNode> getFolloweds(String followerId, RelatedNodeType relatedNodeTypeFollower) throws FollowerNotFound, NoFollowedException {

        if (relatedNodeTypeFollower.equals(RelatedNodeType.ENTITY) && !getEntities().containsKey(followerId)) throw new FollowerNotFound();
        if (relatedNodeTypeFollower.equals(RelatedNodeType.ATTENDEE) && !getAttendees().containsKey(followerId)) throw new FollowerNotFound();
        if (numFollowings(followerId, relatedNodeTypeFollower)==0) throw new NoFollowedException();

        LinkedList<DSNode> aux = new LinkedList<>();
        Iterator<Vertex<Follower>> it = followers.vertexs();
        while (it.hasNext()){
            Follower follower = it.next().getValue();
            if (follower.getFollowedId().equals(followerId) && follower.getRelatedNodeTypeFollower().equals(relatedNodeTypeFollower))
                aux.insertBeginning(new DSNode(follower.getFollowerId(),
                        (relatedNodeTypeFollower == RelatedNodeType.ENTITY) ? getEntities().get(follower.getFollowerId()).getName() : getAttendee(follower.getFollowerId()).getName()));
        }
        return aux.values();

    }

    @Override
    public Iterator<DSNode> recommendations(String followerId, RelatedNodeType relatedNodeTypeFollower) throws FollowerNotFound, NoFollowedException {

        if (relatedNodeTypeFollower.equals(RelatedNodeType.ENTITY) && !getEntities().containsKey(followerId)) throw new FollowerNotFound();
        if (relatedNodeTypeFollower.equals(RelatedNodeType.ATTENDEE) && !getAttendees().containsKey(followerId)) throw new FollowerNotFound();
        if (numFollowings(followerId, relatedNodeTypeFollower)==0) throw new NoFollowedException();

        System.out.println("--------------------------");
        System.out.println(followerId);
        System.out.println("--------------------------");

        System.out.println("Anterior: ");



        Iterator<Edge<String, Follower>> it2 = followers.edges();


        Follower anterior;

        while (it2.hasNext()){
            Follower follower = it2.
            if (follower.getFollowedId().equals(followerId) && follower.getRelatedNodeTypeFollower().equals(relatedNodeTypeFollower))
                anterior=follower.
        }

        LinkedList<DSNode> aux = new LinkedList<>();
        Iterator<Vertex<Follower>> it = followers.vertexs();
        while (it.hasNext()){
            Follower follower = it.next().getValue();
            System.out.println(" Follower: "+follower.getFollowerId()+" Followed: "+follower.getFollowedId());
            if (follower.getFollowedId().equals(followerId) && follower.getRelatedNodeTypeFollower().equals(relatedNodeTypeFollower))
                aux.insertBeginning(new DSNode(follower.getFollowerId(),
                        (relatedNodeTypeFollower == RelatedNodeType.ENTITY) ? getEntities().get(follower.getFollowerId()).getName() : getAttendee(follower.getFollowerId()).getName()));
        }
        return aux.values();


        // idEntity10: { idEntity3, idEntity2, idAttendee1 }
        // idEntity7: { idEntity1, idEntity3 }








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
        return roles.get(id).getWorkers().size();
    }

    @Override
    public int numWorkersByEvent(String id) { return getEvent(id).numWorkers(); }

    @Override
    public int numFollowers(String id, RelatedNodeType relatedNodeType) {


//        System.out.println("----------------------------------");
        Iterator<Vertex<Follower>> it = followers.vertexs();
        int numFollowers=0;
        while (it.hasNext()){
            Follower follower = it.next().getValue();
//            System.out.println(""+
//                    follower.getFollowerId()+" "+
//                    follower.getRelatedNodeTypeFollower()+" "+
//                    follower.getFollowedId()+" "+
//                    follower.getRelatedNodeTypeFollowed());
            if (follower.getFollowerId().equals(id) && follower.getRelatedNodeTypeFollower() == relatedNodeType)
            numFollowers++;
        }
        return numFollowers;





    }

    @Override
    public int numFollowings(String id, RelatedNodeType relatedNodeType) {

//        System.out.println("----------------------------------");
        Iterator<Vertex<Follower>> it = followers.vertexs();
        int numFollowings=0;
        while (it.hasNext()){
            Follower follower = it.next().getValue();
//            System.out.println(""+
//                    follower.getFollowerId()+" "+
//                    follower.getRelatedNodeTypeFollower()+" "+
//                    follower.getFollowedId()+" "+
//                    follower.getRelatedNodeTypeFollowed());
            if (follower.getFollowedId().equals(id) && follower.getRelatedNodeTypeFollower() == relatedNodeType)
                numFollowings++;
        }
        return numFollowings;


    }

}
