package com.team.comma.domain.favorite.controller;

import com.team.comma.domain.favorite.service.FavoriteTrackService;
import com.team.comma.domain.track.dto.TrackRequest;
import com.team.comma.global.common.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountException;

@RequestMapping(value = "/favorite/track")
@RestController
@RequiredArgsConstructor
public class FavoriteTrackController {

    private final FavoriteTrackService favoriteTrackService;

    @PostMapping
    public MessageResponse createFavoriteTrack(
            @CookieValue String accessToken,
            @RequestBody TrackRequest trackRequest) throws AccountException {
        return favoriteTrackService.createFavoriteTrack(accessToken, trackRequest);
    }

    @GetMapping
    public ResponseEntity findAllFavoriteTrack(
            @CookieValue String accessToken) throws AccountException {
        return ResponseEntity.ok()
                .body(favoriteTrackService.findAllFavoriteTrack(accessToken));
    }

}
