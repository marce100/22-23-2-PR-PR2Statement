package uoc.ds.pr.model;

import uoc.ds.pr.UniversityEvents;
import uoc.ds.pr.UniversityEventsPR2;

public class Follower {

    private String followerId;
    private UniversityEventsPR2.RelatedNodeType relatedNodeTypeFollower;
    private String followedId;
    private UniversityEventsPR2.RelatedNodeType relatedNodeTypeFollowed;

    public Follower(String followerId, UniversityEventsPR2.RelatedNodeType relatedNodeTypeFollower, String followedId, UniversityEventsPR2.RelatedNodeType relatedNodeTypeFollowed) {
        this.followerId = followerId;
        this.relatedNodeTypeFollower = relatedNodeTypeFollower;
        this.followedId = followedId;
        this.relatedNodeTypeFollowed = relatedNodeTypeFollowed;
    }

    public String getFollowerId() {
        return followerId;
    }

    public void setFollowerId(String followerId) {
        this.followerId = followerId;
    }

    public UniversityEventsPR2.RelatedNodeType getRelatedNodeTypeFollower() {
        return relatedNodeTypeFollower;
    }

    public void setRelatedNodeTypeFollower(UniversityEventsPR2.RelatedNodeType relatedNodeTypeFollower) {
        this.relatedNodeTypeFollower = relatedNodeTypeFollower;
    }

    public String getFollowedId() {
        return followedId;
    }

    public void setFollowedId(String followedId) {
        this.followedId = followedId;
    }

    public UniversityEventsPR2.RelatedNodeType getRelatedNodeTypeFollowed() {
        return relatedNodeTypeFollowed;
    }

    public void setRelatedNodeTypeFollowed(UniversityEventsPR2.RelatedNodeType relatedNodeTypeFollowed) {
        this.relatedNodeTypeFollowed = relatedNodeTypeFollowed;
    }
}
