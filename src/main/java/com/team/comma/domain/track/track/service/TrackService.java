package com.team.comma.domain.track.track.service;

import com.team.comma.domain.track.track.domain.Track;
import com.team.comma.domain.track.playcount.dto.TrackPlayCountResponse;
import com.team.comma.domain.track.track.dto.TrackRequest;
import com.team.comma.domain.track.playcount.repository.TrackPlayCountRepository;
import com.team.comma.domain.favorite.track.repository.FavoriteTrackRepository;
import com.team.comma.domain.track.track.repository.TrackRepository;
import com.team.comma.domain.track.playcount.domain.TrackPlayCount;
import com.team.comma.spotify.service.SearchService;
import com.team.comma.domain.track.track.domain.Track;
import com.team.comma.domain.track.playcount.dto.TrackPlayCountResponse;
import com.team.comma.domain.track.playcount.repository.TrackPlayCountRepository;
import com.team.comma.domain.favorite.track.repository.FavoriteTrackRepository;
import com.team.comma.domain.track.track.repository.TrackRepository;
import com.team.comma.domain.track.playcount.domain.TrackPlayCount;
import com.team.comma.spotify.service.SearchService;
import com.team.comma.global.common.dto.MessageResponse;
import com.team.comma.domain.favorite.track.domain.FavoriteTrack;
import com.team.comma.domain.track.track.exception.TrackException;
import com.team.comma.domain.user.user.domain.User;
import com.team.comma.domain.user.user.repository.UserRepository;
import com.team.comma.domain.track.track.exception.TrackException;
import com.team.comma.global.jwt.support.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.team.comma.global.common.constant.ResponseCodeEnum.REQUEST_SUCCESS;

@Service
@RequiredArgsConstructor
public class TrackService {

    private final JwtTokenProvider jwtTokenProvider;
    private final TrackPlayCountRepository trackPlayCountRepository;
    private final FavoriteTrackRepository favoriteTrackRepository;
    private final TrackRepository trackRepository;
    private final SearchService searchService;

    @Transactional
    public MessageResponse countPlayCount(String accessToken , String trackId) {
        String userEmail = jwtTokenProvider.getUserPk(accessToken);
        TrackPlayCount trackPlayCount = trackPlayCountRepository.findTrackPlayCountByUserEmail(userEmail , trackId)
                .orElseThrow(() -> new TrackException("트랙을 찾을 수 없습니다."));

        trackPlayCount.updatePlayCount();

        return MessageResponse.of(REQUEST_SUCCESS);
    }

    public MessageResponse findMostListenedTrack(String accessToken) {
        String userEmail = jwtTokenProvider.getUserPk(accessToken);
        List<TrackPlayCountResponse> result = trackPlayCountRepository.findTrackPlayCountByMostListenedTrack(userEmail);

        return MessageResponse.of(REQUEST_SUCCESS , result);
    }

    public MessageResponse findMostListenedTrackByFriend(String accessToken) {
        String userEmail = jwtTokenProvider.getUserPk(accessToken);
        List<TrackPlayCountResponse> result = trackPlayCountRepository.findTrackPlayCountByFriend(userEmail);

        return MessageResponse.of(REQUEST_SUCCESS , result);
    }

    public MessageResponse findTrackByFavoriteTrack(String accessToken) {
        String userEmail = jwtTokenProvider.getUserPk(accessToken);
        List<Track> result = favoriteTrackRepository.findFavoriteTrackByEmail(userEmail);

        return MessageResponse.of(REQUEST_SUCCESS , result);
    }

    public MessageResponse findTrackByMostFavorite() {
        List<Track> result = trackRepository.findTrackMostRecommended();

        return MessageResponse.of(REQUEST_SUCCESS , result);
    }

    public Track findTrackOrSave(final String spotifyTrackId) {
        return trackRepository.findBySpotifyTrackId(spotifyTrackId)
                .orElseGet(() -> saveTrack(spotifyTrackId));
    }

    public Track saveTrack(final String spotifyTrackId) {
        return trackRepository.save(searchService.searchTrackByTrackId(spotifyTrackId));
    }

}
