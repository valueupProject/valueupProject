package com.team.comma.domain.artist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArtistResponse {

    private String spotifyArtistId;

    private String artistName;

    private String artistImageUrl;

    public static ArtistResponse createArtistResponse(Artist artist) {
        return ArtistResponse.builder()
                .spotifyArtistId(artist.getSpotifyArtistId())
                .artistName(artist.getArtistName())
                .artistImageUrl(artist.getArtistImageUrl())
                .build();
    }
}
