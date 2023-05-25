package uoc.ds.pr;

import edu.uoc.ds.traversal.Iterator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uoc.ds.pr.exceptions.DSException;
import uoc.ds.pr.exceptions.FollowerNotFound;
import uoc.ds.pr.exceptions.NoFollowedException;
import uoc.ds.pr.model.DSNode;
import uoc.ds.pr.model.Rating;

public class UniversityEventsPR2TestPlus extends UniversityEventsPR1Test {
    

    @Before
    public void setUp() throws Exception {

        universityEvents = FactoryUniversityEventsClub.getUniversityEventsPR2Plus();

    }

    @After
    public void tearDown() {
        universityEvents=null;
    }

    /**
     *
     * @POST
     * followers(1): {2, 3, 4, 5}
     * followers(2): {1, 3, 6, 7}
     * followers(3): {1, 2, 4, 5}
     * followers(5): {10, 11}
     *
     * followings(1): {2, 3}
     * followings(2): {1, 3}
     * followings(3): {1, 2}
     * followings(4): {1, 3}
     * followings(5): {1, 3}
     * followings(6): {2}
     * followings(7): {2}
     * followings(10): {5}
     * followings(11): {5}
     */
    @Test
    public void addFollowerTest() throws DSException {

        universityEvents.addEntity("idEntity6", "Elena Jugany", "AI for Human Well-being", UniversityEvents.EntityType.PROFESSOR);
        universityEvents.addEntity("idEntity7", "Dustis Comas", "eHealhthLab ", UniversityEvents.EntityType.OTHER);
        universityEvents.addEntity("idEntity8", "Jordi Richarte", "Formula Student-UOC", UniversityEvents.EntityType.STUDENT);
        universityEvents.addEntity("idEntity9", "Miriam Diaz", "Research group in Epidemiology", UniversityEvents.EntityType.PROFESSOR);
        universityEvents.addEntity("idEntity10", "Pedro Torrano", "Care and preparedness in the network society", UniversityEvents.EntityType.PROFESSOR);
        universityEvents.addEntity("idEntity11", "Zola", "Care and preparedness in the network society", UniversityEvents.EntityType.PROFESSOR);

        universityEvents.addFollower("idEntity1", UniversityEventsPR2.RelatedNodeType.ENTITY,
                "idEntity2", UniversityEventsPR2.RelatedNodeType.ENTITY);

        universityEvents.addFollower("idEntity1", UniversityEventsPR2.RelatedNodeType.ENTITY,
                "idEntity3", UniversityEventsPR2.RelatedNodeType.ENTITY);

        universityEvents.addFollower("idEntity1", UniversityEventsPR2.RelatedNodeType.ENTITY,
                "idEntity4", UniversityEventsPR2.RelatedNodeType.ENTITY);

        universityEvents.addFollower("idEntity1", UniversityEventsPR2.RelatedNodeType.ENTITY,
                "idEntity5", UniversityEventsPR2.RelatedNodeType.ENTITY);


        universityEvents.addFollower("idEntity2", UniversityEventsPR2.RelatedNodeType.ENTITY,
                "idEntity1", UniversityEventsPR2.RelatedNodeType.ENTITY);

        universityEvents.addFollower("idEntity2", UniversityEventsPR2.RelatedNodeType.ENTITY,
                "idEntity3", UniversityEventsPR2.RelatedNodeType.ENTITY);

        universityEvents.addFollower("idEntity2", UniversityEventsPR2.RelatedNodeType.ENTITY,
                "idEntity6", UniversityEventsPR2.RelatedNodeType.ENTITY);

        universityEvents.addFollower("idEntity2", UniversityEventsPR2.RelatedNodeType.ENTITY,
                "idEntity7", UniversityEventsPR2.RelatedNodeType.ENTITY);


        universityEvents.addFollower("idEntity3", UniversityEventsPR2.RelatedNodeType.ENTITY,
                "idEntity1", UniversityEventsPR2.RelatedNodeType.ENTITY);

        universityEvents.addFollower("idEntity3", UniversityEventsPR2.RelatedNodeType.ENTITY,
                "idEntity2", UniversityEventsPR2.RelatedNodeType.ENTITY);

        universityEvents.addFollower("idEntity3", UniversityEventsPR2.RelatedNodeType.ENTITY,
                "idEntity4", UniversityEventsPR2.RelatedNodeType.ENTITY);

        universityEvents.addFollower("idEntity3", UniversityEventsPR2.RelatedNodeType.ENTITY,
                "idEntity5", UniversityEventsPR2.RelatedNodeType.ENTITY);

        universityEvents.addFollower("idEntity5", UniversityEventsPR2.RelatedNodeType.ENTITY,
                "idEntity10", UniversityEventsPR2.RelatedNodeType.ENTITY);

        universityEvents.addFollower("idEntity5", UniversityEventsPR2.RelatedNodeType.ENTITY,
                "idEntity11", UniversityEventsPR2.RelatedNodeType.ENTITY);

        Assert.assertEquals(4, universityEvents.numFollowers("idEntity1", UniversityEventsPR2.RelatedNodeType.ENTITY));
        Assert.assertEquals(4, universityEvents.numFollowers("idEntity2", UniversityEventsPR2.RelatedNodeType.ENTITY));
        Assert.assertEquals(4, universityEvents.numFollowers("idEntity3", UniversityEventsPR2.RelatedNodeType.ENTITY));
        Assert.assertEquals(2, universityEvents.numFollowers("idEntity5", UniversityEventsPR2.RelatedNodeType.ENTITY));

        Assert.assertEquals(2, universityEvents.numFollowings("idEntity1", UniversityEventsPR2.RelatedNodeType.ENTITY));
        Assert.assertEquals(2, universityEvents.numFollowings("idEntity2", UniversityEventsPR2.RelatedNodeType.ENTITY));
        Assert.assertEquals(2, universityEvents.numFollowings("idEntity3", UniversityEventsPR2.RelatedNodeType.ENTITY));
        Assert.assertEquals(2, universityEvents.numFollowings("idEntity4", UniversityEventsPR2.RelatedNodeType.ENTITY));
        Assert.assertEquals(2, universityEvents.numFollowings("idEntity5", UniversityEventsPR2.RelatedNodeType.ENTITY));
        Assert.assertEquals(1, universityEvents.numFollowings("idEntity6", UniversityEventsPR2.RelatedNodeType.ENTITY));
        Assert.assertEquals(1, universityEvents.numFollowings("idEntity10", UniversityEventsPR2.RelatedNodeType.ENTITY));
        Assert.assertEquals(1, universityEvents.numFollowings("idEntity11", UniversityEventsPR2.RelatedNodeType.ENTITY));


        Assert.assertThrows(FollowerNotFound.class, () ->
                universityEvents.addFollower("idEntityXXXXXX", UniversityEventsPR2.RelatedNodeType.ENTITY ,
                        "idEntity1", UniversityEventsPR2.RelatedNodeType.ENTITY));

        Assert.assertEquals(4, universityEvents.numFollowers("idEntity1", UniversityEventsPR2.RelatedNodeType.ENTITY));
        Assert.assertEquals(1, universityEvents.numFollowings("idEntity10", UniversityEventsPR2.RelatedNodeType.ENTITY));

        universityEvents.addFollower("idEntity1", UniversityEventsPR2.RelatedNodeType.ENTITY,
                "idEntity10", UniversityEventsPR2.RelatedNodeType.ENTITY);
        Assert.assertEquals(5, universityEvents.numFollowers("idEntity1", UniversityEventsPR2.RelatedNodeType.ENTITY));
        Assert.assertEquals(2, universityEvents.numFollowings("idEntity10", UniversityEventsPR2.RelatedNodeType.ENTITY));
    }


    @Test
    public void addFollowerTest2() throws DSException {
        addFollowerTest();

        universityEvents.addFollower("idAttendee1", UniversityEventsPR2.RelatedNodeType.ATTENDEE,
                "idEntity1", UniversityEventsPR2.RelatedNodeType.ENTITY);

        universityEvents.addFollower("idAttendee3", UniversityEventsPR2.RelatedNodeType.ATTENDEE,
                "idAttendee1", UniversityEventsPR2.RelatedNodeType.ATTENDEE);

        universityEvents.addFollower("idAttendee2", UniversityEventsPR2.RelatedNodeType.ATTENDEE,
                "idAttendee1", UniversityEventsPR2.RelatedNodeType.ATTENDEE);


        Assert.assertEquals(5, universityEvents.numFollowers("idEntity1", UniversityEventsPR2.RelatedNodeType.ENTITY));
        Assert.assertEquals(2, universityEvents.numFollowings("idAttendee1", UniversityEventsPR2.RelatedNodeType.ATTENDEE));

    }


        /**
         * followers(1): {2, 3, 4, 5}
         * followers(2): {1, 3, 6, 7}
         * followers(3): {1, 2, 4, 5}
         * followers(5): {10, 11}
         *
         * followings(1): {2, 3}
         * followings(2): {1, 3}
         * followings(3): {1, 2}
         * followings(4): {1, 3}
         * followings(5): {1, 3}
         * followings(6): {2}
         * followings(7): {2}
         * followings(10): {5,1}
         * followings(11): {5}
         */
    @Test
    public void getFollowersTest() throws DSException {
        addFollowerTest();
        Iterator<DSNode> it = universityEvents.getFollowers("idEntity1", UniversityEventsPR2.RelatedNodeType.ENTITY);
        Assert.assertEquals("idEntity2", it.next().getId());
        Assert.assertEquals("idEntity3", it.next().getId());
        Assert.assertEquals("idEntity4", it.next().getId());
        Assert.assertEquals("idEntity5", it.next().getId());
        
    }

    /**
     *
     * followers(1): {2, 3, 4, 5}
     * followers(2): {1, 3, 6, 7}
     * followers(3): {1, 2, 4, 5}
     * followers(5): {10, 11}
     *
     * followings(1): {2, 3}
     * followings(2): {1, 3}
     * followings(3): {1, 2}
     * followings(4): {1, 3}
     * followings(5): {1, 3}
     * followings(6): {2}
     * followings(7): {2}
     * followings(10): {5,1}
     * followings(11): {5}
     */
    @Test
    public void getFollowedsTest() throws DSException {

        addFollowerTest();
        Assert.assertThrows(FollowerNotFound.class, () ->
                universityEvents.getFolloweds("idEntityXXXXX", UniversityEventsPR2.RelatedNodeType.ENTITY));

        Assert.assertThrows(NoFollowedException.class, () ->
                universityEvents.getFolloweds("idEntity8", UniversityEventsPR2.RelatedNodeType.ENTITY));

        Iterator<DSNode> it = universityEvents.getFolloweds("idEntity1",UniversityEventsPR2.RelatedNodeType.ENTITY );
        Assert.assertEquals("idEntity2", it.next().getId());
        Assert.assertEquals("idEntity3", it.next().getId());
        Assert.assertFalse(it.hasNext());

        it = universityEvents.getFolloweds("idEntity2", UniversityEventsPR2.RelatedNodeType.ENTITY);
        Assert.assertEquals("idEntity1", it.next().getId());
        Assert.assertEquals("idEntity3", it.next().getId());
        Assert.assertFalse(it.hasNext());

        it = universityEvents.getFolloweds("idEntity3", UniversityEventsPR2.RelatedNodeType.ENTITY);
        Assert.assertEquals("idEntity1", it.next().getId());
        Assert.assertEquals("idEntity2", it.next().getId());
        Assert.assertFalse(it.hasNext());

        it = universityEvents.getFolloweds("idEntity4", UniversityEventsPR2.RelatedNodeType.ENTITY);
        Assert.assertEquals("idEntity1", it.next().getId());
        Assert.assertEquals("idEntity3", it.next().getId());
        Assert.assertFalse(it.hasNext());

        it = universityEvents.getFolloweds("idEntity5", UniversityEventsPR2.RelatedNodeType.ENTITY);
        Assert.assertEquals("idEntity1", it.next().getId());
        Assert.assertEquals("idEntity3", it.next().getId());
        Assert.assertFalse(it.hasNext());

        it = universityEvents.getFolloweds("idEntity6", UniversityEventsPR2.RelatedNodeType.ENTITY);
        Assert.assertEquals("idEntity2", it.next().getId());
        Assert.assertFalse(it.hasNext());

        it = universityEvents.getFolloweds("idEntity7", UniversityEventsPR2.RelatedNodeType.ENTITY);
        Assert.assertEquals("idEntity2", it.next().getId());
        Assert.assertFalse(it.hasNext());

        it = universityEvents.getFolloweds("idEntity10", UniversityEventsPR2.RelatedNodeType.ENTITY);
        Assert.assertEquals("idEntity5", it.next().getId());
        Assert.assertEquals("idEntity1", it.next().getId());
        Assert.assertFalse(it.hasNext());

        it = universityEvents.getFolloweds("idEntity11", UniversityEventsPR2.RelatedNodeType.ENTITY);
        Assert.assertEquals("idEntity5", it.next().getId());
        Assert.assertFalse(it.hasNext());
    }

    /**
     *  followings(1): {2, 3}
     *  followings(2): {1, 3}
     *  followings(3): {1, 2}
     *  followings(4): {1, 3}
     *  followings(5): {1, 3}
     *  followings(6): {2}
     *  followings(7): {2}
     *  followings(10): {5}
     *  followings(11): {5}
     */
    @Test
    public void recommendationTest() throws DSException {

        addFollowerTest2();

        Assert.assertThrows(FollowerNotFound.class, () ->
                universityEvents.recommendations("idEntityXXXXX", UniversityEventsPR2.RelatedNodeType.ENTITY));

        Assert.assertThrows(NoFollowedException.class, () ->
                universityEvents.recommendations("idEntity8", UniversityEventsPR2.RelatedNodeType.ENTITY));

        Iterator<DSNode> it =  universityEvents.recommendations("idEntity10", UniversityEventsPR2.RelatedNodeType.ENTITY);
        Assert.assertEquals("idEntity3", it.next().getId());
        Assert.assertEquals("idEntity2", it.next().getId());
        Assert.assertEquals("idAttendee1", it.next().getId());
        Assert.assertFalse(it.hasNext());

        it =  universityEvents.recommendations("idEntity7", UniversityEventsPR2.RelatedNodeType.ENTITY);
        Assert.assertEquals("idEntity1", it.next().getId());
        Assert.assertEquals("idEntity3", it.next().getId());
        Assert.assertFalse(it.hasNext());

    }

    @Test
    public void getRatingsOfTheFollowedTest() throws DSException {

        Assert.assertThrows(FollowerNotFound.class, () ->
                universityEvents.getRatingsOftheFollowed("idAttendee1", UniversityEventsPR2.RelatedNodeType.ENTITY));

        addFollowerTest2();

        Assert.assertThrows(FollowerNotFound.class, () ->
                universityEvents.getRatingsOftheFollowed("idAttendee1", UniversityEventsPR2.RelatedNodeType.ENTITY));


        super.addRatingTest();

        Iterator<Rating> it = universityEvents.getRatingsOftheFollowed("idAttendee1", UniversityEventsPR2.RelatedNodeType.ATTENDEE);
        Rating r1 = it.next();

        Assert.assertEquals("idAttendee3", r1.getAttendee().getId());
        Assert.assertEquals(UniversityEvents.Rating.FOUR, r1.rating());
        Assert.assertEquals("Good", r1.getMessage());

        Rating r2 = it.next();

        Assert.assertEquals("idAttendee2", r2.getAttendee().getId());
        Assert.assertEquals(UniversityEvents.Rating.TWO, r2.rating());
        Assert.assertEquals("CHIPI - CHAPI", r2.getMessage());
        Assert.assertFalse(it.hasNext());

        Iterator<Rating> it2 = universityEvents.getRatingsOftheFollowed("idEntity1", UniversityEventsPR2.RelatedNodeType.ENTITY);
        r1 = it2.next();

        Assert.assertEquals("idAttendee1", r1.getAttendee().getId());
        Assert.assertEquals(UniversityEvents.Rating.FIVE, r1.rating());
        Assert.assertEquals("Very good", r1.getMessage());
        Assert.assertFalse(it2.hasNext());
    }

}