package com.jetsynthesys.rightlife.ui.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchQueryResults {

    @SerializedName("artists")
    @Expose
    private List<Artist> artists;
    @SerializedName("contents")
    @Expose
    private List<Content> contents;
    @SerializedName("instructorProfiles")
    @Expose
    private List<InstructorProfile> instructorProfiles;
    @SerializedName("events")
    @Expose
    private List<Object> events;
    @SerializedName("services")
    @Expose
    private List<Service> services;

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public List<Content> getContents() {
        return contents;
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }

    public List<InstructorProfile> getInstructorProfiles() {
        return instructorProfiles;
    }

    public void setInstructorProfiles(List<InstructorProfile> instructorProfiles) {
        this.instructorProfiles = instructorProfiles;
    }

    public List<Object> getEvents() {
        return events;
    }

    public void setEvents(List<Object> events) {
        this.events = events;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

}