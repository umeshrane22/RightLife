package com.example.rlapp.ui.therledit;

public class EpisodeTrackRequest {
    private String userId;
    private String moduleId;
    private String contentId;
    private int duration;
    private int watchDuration;
    private String contentType;

    public EpisodeTrackRequest(String userId, String moduleId, String contentId, int duration, int watchDuration, String contentType) {
        this.userId = userId;
        this.moduleId = moduleId;
        this.contentId = contentId;
        this.duration = duration;
        this.watchDuration = watchDuration;
        this.contentType = contentType;
    }
}