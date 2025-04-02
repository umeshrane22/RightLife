package com.jetsynthesys.rightlife.apimodel.affirmations.updateAffirmation;

public class AffirmationRequest {
    private String consumed_cta;
    private String affirmationId;
    private String userId;

    // Constructor
    public AffirmationRequest(String consumed_cta, String affirmationId, String userId) {
        this.consumed_cta = consumed_cta;
        this.affirmationId = affirmationId;
        this.userId = userId;
    }

    // Getters and Setters (optional if using Gson)
}
