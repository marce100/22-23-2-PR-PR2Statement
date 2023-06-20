package uoc.ds.pr.model;

import uoc.ds.pr.UniversityEvents;

public class Facility {

    private String id;
    private String name;
    private String description;
    private UniversityEvents.InstallationType type;

    public Facility(String id, String name, String description, UniversityEvents.InstallationType type) {
        this.setId(id);
        this.setName(name);
        this.setDescription(description);
        this.setFacilityType(type);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UniversityEvents.InstallationType getFacilityType() {
        return type;
    }

    public void setFacilityType(UniversityEvents.InstallationType type) {
        this.type = type;
    }
}
