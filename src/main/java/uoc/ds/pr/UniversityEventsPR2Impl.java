package uoc.ds.pr;

import edu.uoc.ds.adt.helpers.Position;
import edu.uoc.ds.adt.nonlinear.DictionaryAVLImpl;
import edu.uoc.ds.adt.nonlinear.HashTable;
import edu.uoc.ds.adt.nonlinear.graphs.*;
import edu.uoc.ds.adt.sequential.*;
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

        /*
        Follower follower = new Follower(followerId,relatedNodeTypeFollower, followedId, relatedNodeTypeFollowed);
        Vertex<Follower> vFollower = followers.newVertex(follower);
        */
/*
        DSNode ds1 = null;
        DSNode ds2 = null;
        if (relatedNodeTypeFollower.equals(RelatedNodeType.ENTITY)){
            ds1 = new DSNode(followerId, "AAAAAAAAAAAA");
        }
        if (relatedNodeTypeFollower.equals(RelatedNodeType.ATTENDEE)){
            ds1 = new DSNode(followerId, "AAAAAAAAAAAA");
        }
        if (relatedNodeTypeFollowed.equals(RelatedNodeType.ENTITY)){
            ds2 = new DSNode(followedId, "BBBBBDBBBBBB");
        }
        if (relatedNodeTypeFollowed.equals(RelatedNodeType.ATTENDEE)){
            ds2 = new DSNode(followedId, "BBBBBDBBBBBB");
        }
        ds1 = new DSNode(followerId, "AAAAAAAAAAAA");
        ds2 = new DSNode(followedId, "BBBBBDBBBBBB");
        nodes.put(followerId,ds1);
        nodes.put(followedId,ds2);



        Vertex<DSNode> vElmo = graph.newVertex(nodes.get(followerId));
        Vertex<DSNode> vPiggy = graph.newVertex(nodes.get(followedId));


        //Piggy is follower of Elmo
        Edge<String, DSNode> edge1a = graph.newEdge(vElmo, vPiggy);
        edge1a.setLabel("follower");
        //Elmo is followed of Piggy
        Edge<String, DSNode> edge1b = graph.newEdge(vPiggy, vElmo);
        edge1b.setLabel("followed");

        System.out.println("------------------------------------");
        System.out.println("(followerId) "+followerId);
        System.out.println("------------------------------------");
        DirectedVertexImpl<DSNode, String> _vElmo = (DirectedVertexImpl<DSNode, String>) graph.getVertex(nodes.get(followerId));
        Iterator<Edge<String, DSNode>> it = _vElmo.edges();
        DirectedEdge<String, DSNode> _edge1 ;
        while (it.hasNext()) {
            _edge1 = (DirectedEdge<String, DSNode>) it.next();
            System.out.println("" + _edge1.getLabel());
            System.out.println("" + _edge1.getVertexSrc().getValue().getId()+"  "+_edge1.getVertexSrc().getValue().getName());
            System.out.println("" + _edge1.getVertexDst().getValue().getId()+"  "+_edge1.getVertexDst().getValue().getName());
        }
        System.out.println("------------------------------------");
        System.out.println("");
*/





        DSNode followerNode = new DSNode(followerId, relatedNodeTypeFollower.toString());
        DSNode followedNode = new DSNode(followedId, relatedNodeTypeFollowed.toString());

        Vertex<DSNode> followerVertex = graph.newVertex(followerNode);
        Vertex<DSNode> followedVertex = graph.newVertex(followedNode);

        /*Edge<String, DSNode> edge =*/ graph.newEdge(followerVertex, followedVertex);













//followers.newEdge();


/*
        System.out.println("*****************");
        System.out.println("* INICIO PRUEBA *");
        System.out.println("*****************");


        DSNode elmo = new DSNode("ELM1980", "Elmo");
        DSNode piggy = new DSNode("PIG1974", "Miss Piggy");
        DSNode kermit = new DSNode("KERM1955", "Kermit the Frog");
        DSNode rowlf = new DSNode("Rowlf1962", "Rowlf the Dog");

        Vertex<DSNode> vElmo = graph.newVertex(elmo);
        Vertex<DSNode> vPiggy = graph.newVertex(piggy);
        Vertex<DSNode> vKermit = graph.newVertex(kermit);
        Vertex<DSNode> vRowlf = graph.newVertex(rowlf);

        //Piggy is follower of Elmo
        Edge<String, DSNode> edge1a = graph.newEdge(vElmo, vPiggy);
        edge1a.setLabel("follower");
        //Elmo is followed of Piggy
        Edge<String, DSNode> edge1b = graph.newEdge(vPiggy, vElmo);
        edge1b.setLabel("followed");

        //Kermit is follower of Elmo
        Edge<String, DSNode> edge2a = graph.newEdge(vElmo, vKermit);
        edge2a.setLabel("follower");
        //Elmo is followed of Kermit
        Edge<String, DSNode> edge2b = graph.newEdge(vKermit, vElmo);
        edge2b.setLabel("followed");

        // Rowlf is follower of Elmo
        Edge<String, DSNode> edge3a = graph.newEdge(vElmo, vRowlf);
        edge3a.setLabel("follower");
        // Elmo is followed of Rowlf
        Edge<String, DSNode> edge3b = graph.newEdge(vRowlf, vElmo);
        edge3b.setLabel("followed");

        // Kermit is follower of Piggy
        Edge<String, DSNode> edge4a = graph.newEdge(vPiggy, vKermit);
        edge4a.setLabel("follower");
        // Piggy is followed of Kermit
        Edge<String, DSNode> edge4b = graph.newEdge(vKermit, vPiggy);
        edge4b.setLabel("followed");



        // ELMO
        DirectedVertexImpl<DSNode, String> _vElmo = (DirectedVertexImpl<DSNode, String>) graph.getVertex(elmo);

        Iterator<Edge<String, DSNode>> it = _vElmo.edges();

        DirectedEdge<String, DSNode> _edge1 ;
        while (it.hasNext()) {
            _edge1 = (DirectedEdge<String, DSNode>) it.next();
            System.out.println("follower" + _edge1.getLabel());
            System.out.println("Elmo" + _edge1.getVertexSrc().getValue().getName());
            System.out.println("Miss Piggy" + _edge1.getVertexDst().getValue().getName());
        }




        System.out.println("*****************");
        System.out.println("* FIN PRUEBA    *");
        System.out.println("*****************");
*/














        
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
                //System.out.println(_edge.getLabel() + " " + _edge.getVertexDst().getValue().getId());
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
                //System.out.println(_edge.getLabel() + " " + _edge.getVertexSrc().getValue().getId());
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
                // System.out.println(_edge.getLabel() + " " + _edge.getVertexSrc().getValue().getId());
                id = _edge.getVertexSrc().getValue().getId();
            }
        }

//        System.out.println("???????????????????????????????");
//        System.out.println("Buscar seguidores de: "+followerId);
//        System.out.println("Anterior: "+id);
//        System.out.println("???????????????????????????????");

        ArrayList<DSNode> result = new ArrayList<>();
        Iterator<Edge<String, DSNode>> it2 = graph.edges();
        while (it2.hasNext()) {
            DirectedEdge<String, DSNode> _edge = (DirectedEdge<String, DSNode>)it2.next();
            if (_edge.getVertexDst().getValue().getId().equals(id)) {
//                System.out.println(
//                        _edge.getLabel() + " " +
//                        _edge.getVertexSrc().getValue().getId() + " "+
//                        _edge.getVertexSrc().getValue().getName()+" "+
//                        _edge.hashCode()  );
                result.add(_edge.getVertexSrc().getValue());
            }
        }

        for (DSNode dsNode :result) {
            System.out.println(dsNode.getId());
        }


        //Collections.sort(result, Comparator.comparing(DSNode::getId, Comparator.reverseOrder()));
        return new IteratorArrayImpl(result.toArray(), result.size(), 0);
        //return result.values();

    }

    @Override
    public Iterator<uoc.ds.pr.model.Rating> getRatingsOftheFollowed(String followerId, RelatedNodeType relatedNodeType) throws FollowerNotFound, NoFollowedException, NoRatingsException {

        if (relatedNodeType.equals(RelatedNodeType.ENTITY) && !getEntities().containsKey(followerId)) throw new FollowerNotFound();
        if (relatedNodeType.equals(RelatedNodeType.ATTENDEE) && !getAttendees().containsKey(followerId)) throw new FollowerNotFound();
        if (numFollowings(followerId, relatedNodeType)==0) throw new NoFollowedException();

        /*
         *
         *
         *
         *
         *
         */

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


//        int count = 0;
//        DSNode nodeToFind = new DSNode(id, relatedNodeType.toString());
//        Iterator<Vertex<DSNode>> it = graph.vertexs();
//        while (it.hasNext()){
//
//            DSNode node = it.next().getValue();
//
//            if (node.getId().equals(nodeToFind.getId())) {
//
//                System.out.println(node.getId()+" "+node.getName());
//
//                Iterator<Edge<String, DSNode>> it2 = graph.edges();
//
//              while (it2.hasNext()) {
//                    DirectedEdge<String, DSNode> _edge1 = (DirectedEdge<String, DSNode>)it2.next();
//                    if (_edge1.getVertexSrc().getValue().getId()==node.getId())
//                    System.out.println(_edge1.getLabel()+" "+_edge1.getVertexDst().getValue().getId());
//                }
//            }
//        }



        int count = 0;
        Iterator<Edge<String, DSNode>> it = graph.edges();
        while (it.hasNext()) {
            DirectedEdge<String, DSNode> _edge = (DirectedEdge<String, DSNode>)it.next();
            if (_edge.getVertexSrc().getValue().getId().equals(id)) {
                //System.out.println(_edge1.getLabel() + " " + _edge1.getVertexDst().getValue().getId());
                count++;
            }
        }

        return count;

    }

    @Override
    public int numFollowings(String id, RelatedNodeType relatedNodeType) {

        int count = 0;
        Iterator<Edge<String, DSNode>> it = graph.edges();
        while (it.hasNext()) {
            DirectedEdge<String, DSNode> _edge = (DirectedEdge<String, DSNode>)it.next();
            if (_edge.getVertexDst().getValue().getId().equals(id)) {
                //System.out.println(_edge1.getLabel() + " " + _edge1.getVertexDst().getValue().getId());
                count++;
            }
        }
        return count;
    }

}
