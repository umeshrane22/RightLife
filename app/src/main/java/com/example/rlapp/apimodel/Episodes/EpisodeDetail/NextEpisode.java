package com.example.rlapp.apimodel.Episodes.EpisodeDetail;

import java.util.List;

public class NextEpisode {
    public String _id;
    public String contentId;
    public String type;
    public String title;
    public int episodeNumber;
    public String desc;
    public List<Artist> artist;
    public Thumbnail thumbnail;
    public String pricingTier;
    public String youtubeUrl;
    public String shareUrl;
    public String createdAt;
    public String updatedAt;
    public int viewCount;
    public Meta meta;
    public boolean isWatched;
    public boolean isFavourited;
}