package com.team.comma.spotify.track.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TrackPlayCountRequest {
    private String trackId;

    private String trackImageUrl;

    private String trackName;

    private String trackArtist;
}
