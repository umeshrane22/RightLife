package com.jetsynthesys.rightlife.ui.therledit;

public class EpisodeTrackRequest {
    private String userId;
    private String moduleId;
    private String contentId;
    private String duration;
    private String watchDuration;
    private String contentType;

    public EpisodeTrackRequest(String userId, String moduleId, String contentId, String duration, String watchDuration, String contentType) {
        this.userId = userId;
        this.moduleId = moduleId;
        this.contentId = contentId;
        this.duration = duration;
        this.watchDuration = watchDuration;
        this.contentType = contentType;
    }
}