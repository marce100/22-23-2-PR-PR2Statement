package uoc.ds.pr;

import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.NoEntitiesException;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.*;

import java.time.LocalDate;



public interface UniversityEventsPR2 extends UniversityEvents {



    enum RelatedNodeType {
        ENTITY,
        ATTENDEE
    };

    enum Level {
        DIAMOND(5),
        PLATINUM(4),
        GOLD(3),
        SILVER(2),
        BRONZE(1);
        private final int value;

        private Level(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }


    public static int MAX_NUM_ROLES = 15;
    public static int MAX_NUM_FACILITIES = 25;

    public static int MAX_BEST_ENTITIES = 5;

    public void addRole(String id, String name);
    public void addWorker(String workerId, String name, String surname, LocalDate birthDay, String roleId);
    public void addFacility(String id, String name, String description, UniversityEvents.InstallationType type);
    public void assignWorker(String workerId, String eventId) throws EventNotFoundException, WorkerNotFoundException, WorkerAlreadyAssignedException;
    public Iterator<Worker> getWorkersByEvent(String eventId) throws EventNotFoundException, NoWorkersException;
    public Iterator<Worker> getWorkersByRole(String roleId) throws NoWorkersException;
    public Level getLevelByEntity(String entityId) throws EntityNotFoundException;
    public Iterator<Attendee> getSubstitutesByEvent(String eventId) throws EventNotFoundException, NoSubstitutesException;
    public Attendee getAttendeeByEvent(String phone, String eventId) throws EventNotFoundException, AttendeeNotFoundException;
    public Iterator<Enrollment> getAttendeesByEvent(String eventId) throws EventNotFoundException, NoAttendeesException;
    public Iterator<Entity> getBest5Entities() throws NoEntitiesException;
    public Event getBestEventByNumAttendees() throws NoEventsException;
    public void addFollower(String followerId, RelatedNodeType relatedNodeTypeFollower, String followedId, RelatedNodeType relatedNodeTypeFollowed) throws FollowerNotFound, FollowedException;
    public Iterator<DSNode> getFollowers(String followedId, RelatedNodeType relatedNodeTypeFollowed) throws FollowerNotFound, NoFollowersException;
    public Iterator<DSNode> getFolloweds(String followerId, RelatedNodeType relatedNodeTypeFollower) throws FollowerNotFound, NoFollowedException;
    public Iterator<DSNode> recommendations(String followerId, RelatedNodeType relatedNodeTypeFollower) throws FollowerNotFound, NoFollowedException;
    public Iterator<uoc.ds.pr.model.Rating> getRatingsOftheFollowed(String followerId, RelatedNodeType relatedNodeType) throws FollowerNotFound,NoFollowedException,  NoRatingsException;
    //////
    //////
    public int numRoles();
    public int numWorkers();
    public int numFacilities();
    public Role getRole(String id);
    public Worker getWorker(String id);
    public Facility getFacility(String id);
    public int numWorkersByRole(String id);
    public int numWorkersByEvent(String id);
    public int numFollowers(String id, RelatedNodeType relatedNodeType);
    public int numFollowings(String id, RelatedNodeType relatedNodeType);
}
