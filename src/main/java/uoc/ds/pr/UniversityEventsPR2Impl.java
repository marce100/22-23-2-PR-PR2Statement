package uoc.ds.pr;

import edu.uoc.ds.adt.nonlinear.DictionaryAVLImpl;
import edu.uoc.ds.adt.nonlinear.HashTable;
import edu.uoc.ds.adt.nonlinear.graphs.*;
import edu.uoc.ds.adt.sequential.*;
import edu.uoc.ds.traversal.Iterator;
import edu.uoc.ds.traversal.IteratorArrayImpl;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.*;
import uoc.ds.pr.util.DSArray;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class UniversityEventsPR2Impl extends UniversityEventsImpl  implements UniversityEventsPR2 {

    private final DSArray<Role> roles;
    private final DSArray<Facility> facilities;
    private final HashTable<String, Worker> workers;
    private final DirectedGraph<DSNode, String> graph;

    public UniversityEventsPR2Impl() {
        roles = new DSArray<>(MAX_NUM_ROLES);
        workers = new HashTable<>();
        facilities = new DSArray<>(MAX_NUM_FACILITIES);
        graph = new DirectedGraphImpl<>();
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
        if (worker == null)
            throw new WorkerNotFoundException();

        Event event = (Event) getEvents().get(eventId);
        if (event == null)
            throw new EventNotFoundException();

        if (event.isInWorkers(workerId))
            throw new WorkerAlreadyAssignedException();

        event.setWorker(worker);

    }

    @Override
    public Iterator<Worker> getWorkersByEvent(String eventId) throws EventNotFoundException, NoWorkersException {

        Event event = (Event) getEvents().get(eventId);
        if (event == null)
            throw new EventNotFoundException();

        if (event.numWorkers()==0)
            throw new NoWorkersException();

        Iterator<Worker> iterator= event.getWorkers();

        return iterator;
    }


    @Override
    public Iterator<Worker> getWorkersByRole(String roleId) throws NoWorkersException {

        LinkedList<Worker> workers = roles.get(roleId).getWorkers();

        if (workers.isEmpty())
            throw new NoWorkersException();

        return workers.values();
    }

    @Override
    public Level getLevelByEntity(String entityId) throws EntityNotFoundException {

        Entity entity = (Entity) getEntities().get(entityId);
        if (entity == null)
            throw new EntityNotFoundException();

        return entity.getLevel();
    }

    @Override
    public Iterator<Attendee> getSubstitutesByEvent(String eventId) throws EventNotFoundException, NoSubstitutesException {

        DictionaryAVLImpl events = getEvents();
        Event event = (Event) events.get(eventId);

        if (event == null)
            throw new EventNotFoundException();
        if (event.numSubstitutes()==0)
            throw new NoSubstitutesException();

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
        if (event == null)
            throw new EventNotFoundException();

        Enrollment enrollment = event.getEnrollments().get(phone);
        if (enrollment == null)
            throw new AttendeeNotFoundException();

        return enrollment.getAttendee();
    }

    @Override
    public Iterator<Enrollment> getAttendeesByEvent(String eventId) throws EventNotFoundException, NoAttendeesException {
        Event event = (Event) getEvents().get(eventId);
        if (event == null)
            throw new EventNotFoundException();

        if (event.numAttendees()==0)
            throw new NoAttendeesException();

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

        // Follower does not exists
        if (relatedNodeTypeFollower.equals(RelatedNodeType.ENTITY) && !getEntities().containsKey(followerId) ) throw new FollowerNotFound();
        if (relatedNodeTypeFollower.equals(RelatedNodeType.ATTENDEE) && !getAttendees().containsKey(followerId) ) throw new FollowerNotFound();

        // Followed does not exists
        if (relatedNodeTypeFollowed.equals(RelatedNodeType.ENTITY) && !getEntities().containsKey(followedId)) throw new FollowedException();
        if (relatedNodeTypeFollowed.equals(RelatedNodeType.ATTENDEE) && !getAttendees().containsKey(followedId)) throw new FollowedException();


        DSNode followerNode = new DSNode(followerId, relatedNodeTypeFollower.toString());
        DSNode followedNode = new DSNode(followedId, relatedNodeTypeFollowed.toString());

        Vertex<DSNode> followerVertex = graph.newVertex(followerNode);
        Vertex<DSNode> followedVertex = graph.newVertex(followedNode);

        graph.newEdge(followerVertex, followedVertex);

    }

    @Override
    public Iterator<DSNode> getFollowers(String followedId, RelatedNodeType relatedNodeTypeFollowed) throws FollowerNotFound, NoFollowersException {

        if (relatedNodeTypeFollowed.equals(RelatedNodeType.ENTITY) && !getEntities().containsKey(followedId)) throw new FollowerNotFound();
        if (relatedNodeTypeFollowed.equals(RelatedNodeType.ATTENDEE) && !getAttendees().containsKey(followedId)) throw new FollowerNotFound();
        if (numFollowers(followedId, relatedNodeTypeFollowed)==0) throw new NoFollowersException();

        LinkedList<DSNode> result = new LinkedList<>();
        Iterator<Edge<String, DSNode>> it = graph.edges();
        while (it.hasNext()) {
            DirectedEdge<String, DSNode> _edge = (DirectedEdge<String, DSNode>)it.next();
            if (_edge.getVertexSrc().getValue().getId().equals(followedId)) {
                result.insertEnd(_edge.getVertexDst().getValue());
            }
        }
        return result.values();

    }

    @Override
    public Iterator<DSNode> getFolloweds(String followerId, RelatedNodeType relatedNodeTypeFollower) throws FollowerNotFound, NoFollowedException {

        if (relatedNodeTypeFollower.equals(RelatedNodeType.ENTITY) && !getEntities().containsKey(followerId)) throw new FollowerNotFound();
        if (relatedNodeTypeFollower.equals(RelatedNodeType.ATTENDEE) && !getAttendees().containsKey(followerId)) throw new FollowerNotFound();
        if (numFollowings(followerId, relatedNodeTypeFollower)==0) throw new NoFollowedException();

        LinkedList<DSNode> result = new LinkedList<>();
        Iterator<Edge<String, DSNode>> it = graph.edges();
        while (it.hasNext()) {
            DirectedEdge<String, DSNode> _edge = (DirectedEdge<String, DSNode>)it.next();
            if (_edge.getVertexDst().getValue().getId().equals(followerId)) {
                result.insertEnd(_edge.getVertexSrc().getValue());
            }
        }
        return result.values();
    }

    @Override
    public Iterator<DSNode> recommendations(String followerId, RelatedNodeType relatedNodeTypeFollower) throws FollowerNotFound, NoFollowedException {

        if (relatedNodeTypeFollower.equals(RelatedNodeType.ENTITY) && !getEntities().containsKey(followerId)) throw new FollowerNotFound();
        if (relatedNodeTypeFollower.equals(RelatedNodeType.ATTENDEE) && !getAttendees().containsKey(followerId)) throw new FollowerNotFound();
        if (numFollowings(followerId, relatedNodeTypeFollower)==0) throw new NoFollowedException();

        // Get parent
        String id = null;
        Iterator<Edge<String, DSNode>> it = graph.edges();
        while (it.hasNext()) {
            DirectedEdge<String, DSNode> _edge = (DirectedEdge<String, DSNode>)it.next();
            if (_edge.getVertexDst().getValue().getId().equals(followerId)) {
                id = _edge.getVertexSrc().getValue().getId();
            }
        }

        List<DSNode> result = new LinkedList<>();
        Iterator<Edge<String, DSNode>> it2 = graph.edges();
        while (it2.hasNext()) {
            DirectedEdge<String, DSNode> _edge = (DirectedEdge<String, DSNode>)it2.next();
            if (_edge.getVertexDst().getValue().getId().equals(id)) {
                result.insertEnd(_edge.getVertexSrc().getValue());
            }
        }

        System.out.println("RESULT:");
        Iterator<DSNode> it3 = result.values();
        while (it3.hasNext()){
            DSNode dsNode=it3.next();
            System.out.println(dsNode.getId());
        }

        return result.values();

    }

    @Override
    public Iterator<uoc.ds.pr.model.Rating> getRatingsOftheFollowed(String followerId, RelatedNodeType relatedNodeType) throws FollowerNotFound, NoFollowedException, NoRatingsException {

        if (relatedNodeType.equals(RelatedNodeType.ENTITY) && !getEntities().containsKey(followerId)) throw new FollowerNotFound();
        if (relatedNodeType.equals(RelatedNodeType.ATTENDEE) && !getAttendees().containsKey(followerId)) throw new FollowerNotFound();
        if (numFollowings(followerId, relatedNodeType)==0) throw new NoFollowedException();

        /*
         * Returns an iterator with the following ratings for the given follower. If the follower does not exist or there are no ratings,
         * an error will be indicated.
         */

        List<uoc.ds.pr.model.Rating> result = new LinkedList<>();
        Iterator<Edge<String, DSNode>> it = graph.edges();
        while (it.hasNext()) {
            DirectedEdge<String, DSNode> _edge = (DirectedEdge<String, DSNode>)it.next();
            if (_edge.getVertexDst().getValue().getId().equals(followerId)) {
                if (getAttendees().containsKey(_edge.getVertexSrc().getValue().getId())){
                    Iterator<uoc.ds.pr.model.Rating> it2 = getAttendee(_edge.getVertexSrc().getValue().getId()).getRatings().values();
                    while (it2.hasNext()){
                        uoc.ds.pr.model.Rating rating = it2.next();
                        result.insertEnd(rating);
                    }
                }
            }
        }

        if (result.isEmpty()) throw new NoRatingsException();
        return result.values();

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

        int count = 0;
        Iterator<Edge<String, DSNode>> it = graph.edges();
        while (it.hasNext()) {
            DirectedEdge<String, DSNode> _edge = (DirectedEdge<String, DSNode>)it.next();
            if (_edge.getVertexSrc().getValue().getId().equals(id))
                count++;
        }
        return count;

    }

    @Override
    public int numFollowings(String id, RelatedNodeType relatedNodeType) {

        int count = 0;
        Iterator<Edge<String, DSNode>> it = graph.edges();
        while (it.hasNext()) {
            DirectedEdge<String, DSNode> _edge = (DirectedEdge<String, DSNode>)it.next();
            if (_edge.getVertexDst().getValue().getId().equals(id))
                count++;
        }
        return count;
    }

}
