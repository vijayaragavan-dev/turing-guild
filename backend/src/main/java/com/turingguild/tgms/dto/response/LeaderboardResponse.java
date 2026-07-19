package com.turingguild.tgms.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LeaderboardResponse {
    private Integer rank;
    private Long userId;
    private String userBatchNumber;
    private String userFullName;
    private Long eventId;
    private String eventTitle;
    private Integer totalScore;
}
