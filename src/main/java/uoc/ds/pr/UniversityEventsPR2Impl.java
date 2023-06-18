package uoc.ds.pr;

import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.*;
import uoc.ds.pr.util.DSArray;

import java.time.LocalDate;

public class UniversityEventsPR2Impl extends UniversityEventsImpl  implements UniversityEventsPR2 {

    private final DSArray<Role> roles;

    public UniversityEventsPR2Impl() {
        roles = new DSArray<>(MAX_NUM_ROLES);
    }

    @Override
    public void addRole(String id, String name) {

    }

    @Override
    public void addWorker(String workerId, String name, String surname, LocalDate birthDay, String roleId) {

    }

    @Override
    public void addFacility(String id, String name, String description, InstallationType type) {

    }

    @Override
    public void assignWorker(String workerId, String eventId) throws EventNotFoundException, WorkerNotFoundException, WorkerAlreadyAssignedException {

    }

    @Override
    public Iterator<Worker> getWorkersByEvent(String eventId) throws EventNotFoundException, NoWorkersException {
        return null;
    }


    @Override
    public Iterator<Worker> getWorkersByRole(String roleId) throws NoWorkersException {
        return null;
    }

    @Override
    public Level getLevelByEntity(String entityId) throws EntityNotFoundException {
        return null;
    }

    @Override
    public Iterator<Attendee> getSubstitutesByEvent(String eventId) throws EventNotFoundException, NoSubstitutesException {
        return null;
    }

    @Override
    public Attendee getAttendeeByEvent(String phone, String eventId) throws EventNotFoundException, AttendeeNotFoundException {
        return null;
    }

    @Override
    public Iterator<Enrollment> getAttendeesByEvent(String eventId) throws EventNotFoundException, NoAttendeesException {
        return null;
    }

    @Override
    public Iterator<Entity> getBest5Entities() throws NoEntitiesException {
        return null;
    }

    @Override
    public Event getBestEventByNumAttendees() throws NoEventsException {
        return null;
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
        return 0;
    }

    @Override
    public int numWorkers() {
        return 0;
    }

    @Override
    public int numFacilities() {
        return 0;
    }

    @Override
    public Role getRole(String id) {
        return null;
    }

    @Override
    public Worker getWorker(String id) {
        return null;
    }

    @Override
    public Facility getFacility(String id) {
        return null;
    }

    @Override
    public int numWorkersByRole(String id) {
        return 0;
    }

    @Override
    public int numWorkersByEvent(String id) {
        return 0;
    }

    @Override
    public int numFollowers(String id, RelatedNodeType relatedNodeType) {
        return 0;
    }

    @Override
    public int numFollowings(String id, RelatedNodeType relatedNodeType) {
        return 0;
    }
}
