package com.team.comma.domain.track.service;

import com.team.comma.domain.track.track.service.TrackService;
import com.team.comma.global.common.dto.MessageResponse;
import com.team.comma.domain.track.track.domain.Track;
import com.team.comma.domain.track.playcount.domain.TrackPlayCount;
import com.team.comma.domain.track.playcount.dto.TrackPlayCountResponse;
import com.team.comma.domain.track.track.dto.TrackRequest;
import com.team.comma.domain.track.track.exception.TrackException;
import com.team.comma.domain.track.playcount.repository.TrackPlayCountRepository;
import com.team.comma.domain.favorite.track.repository.FavoriteTrackRepository;
import com.team.comma.domain.track.track.repository.TrackRepository;
import com.team.comma.domain.user.user.constant.UserRole;
import com.team.comma.domain.user.user.domain.User;
import com.team.comma.domain.user.user.repository.UserRepository;
import com.team.comma.domain.track.domain.Track;
import com.team.comma.domain.track.domain.TrackPlayCount;
import com.team.comma.domain.track.dto.TrackPlayCountResponse;
import com.team.comma.domain.track.exception.TrackException;
import com.team.comma.domain.track.repository.TrackPlayCountRepository;
import com.team.comma.domain.favorite.repository.FavoriteTrackRepository;
import com.team.comma.domain.track.repository.TrackRepository;
import com.team.comma.global.jwt.support.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.security.auth.login.AccountException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.team.comma.global.common.constant.ResponseCodeEnum.REQUEST_SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class TrackServiceTest {

    @InjectMocks
    TrackService trackService;

    @Mock
    TrackPlayCountRepository trackPlayCountRepository;

    @Mock
    JwtTokenProvider jwtTokenProvider;

    @Mock
    FavoriteTrackRepository favoriteTrackRepository;

    @Mock
    TrackRepository trackRepository;

    @Test
    @DisplayName("TrackPlayCount 탐색 실패")
    void searchTrackPlayCountFail() {
        // given
        doThrow(new TrackException("트랙을 찾을 수 없습니다.")).when(trackPlayCountRepository).findTrackPlayCountByUserEmail(any(String.class), any(String.class));
        doReturn("userEmail").when(jwtTokenProvider).getUserPk("token");
        // when
        Throwable thrown = catchThrowable(() -> trackService.countPlayCount("token", "trackId"));

        // then
        assertThat(thrown.getMessage()).isEqualTo("트랙을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("TrackPlayCount 탐색 성공")
    void searchTrackPlayCount() {
        // given
        doReturn(Optional.of(buildTrackPlayCount())).when(trackPlayCountRepository).findTrackPlayCountByUserEmail(any(String.class), any(String.class));
        doReturn("userEmail").when(jwtTokenProvider).getUserPk("token");
        // when
        MessageResponse result = trackService.countPlayCount("token", "trackId");

        // then
        assertThat(result.getCode()).isEqualTo(REQUEST_SUCCESS.getCode());
        assertThat(result.getMessage()).isEqualTo(REQUEST_SUCCESS.getMessage());
    }

    @Test
    @DisplayName("내가 가장 많이 들은 곡")
    void findMostListenedTrack() {
        // given
        List<TrackPlayCountResponse> trackPlayCounts = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            trackPlayCounts.add(buildTrackPlayCountResponse());
        }
        doReturn(trackPlayCounts).when(trackPlayCountRepository).findTrackPlayCountByMostListenedTrack(any(String.class));
        doReturn("userEmail").when(jwtTokenProvider).getUserPk("token");

        // when
        MessageResponse result = trackService.findMostListenedTrack("token");

        // then
        assertThat(result.getCode()).isEqualTo(REQUEST_SUCCESS.getCode());
        assertThat(result.getMessage()).isEqualTo(REQUEST_SUCCESS.getMessage());
        assertThat(((List) result.getData()).size()).isEqualTo(5);
    }

    @Test
    @DisplayName("친구가 가장 많이 들은 곡")
    void findMostListenedTrackByFriend() {
        // given
        List<TrackPlayCountResponse> trackPlayCounts = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            trackPlayCounts.add(buildTrackPlayCountResponse());
        }
        doReturn(trackPlayCounts).when(trackPlayCountRepository).findTrackPlayCountByFriend(any(String.class));
        doReturn("userEmail").when(jwtTokenProvider).getUserPk("token");

        // when
        MessageResponse result = trackService.findMostListenedTrackByFriend("token");

        // then
        assertThat(result.getCode()).isEqualTo(REQUEST_SUCCESS.getCode());
        assertThat(result.getMessage()).isEqualTo(REQUEST_SUCCESS.getMessage());
        assertThat(((List) result.getData()).size()).isEqualTo(5);
    }

    @Test
    @DisplayName("좋아요 표기한 곡 탐색")
    void findTrackByFavoriteTrack() {
        // given
        List<Track> tracks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            tracks.add(buildTrack("title", "trackId"));
        }

        doReturn(tracks).when(favoriteTrackRepository).findFavoriteTrackByEmail(any(String.class));
        doReturn("userEmail").when(jwtTokenProvider).getUserPk("token");

        // when
        MessageResponse result = trackService.findTrackByFavoriteTrack("token");

        // then
        assertThat(result.getCode()).isEqualTo(REQUEST_SUCCESS.getCode());
        assertThat(result.getMessage()).isEqualTo(REQUEST_SUCCESS.getMessage());
        assertThat(((List) result.getData()).size()).isEqualTo(5);
    }

    @Test
    @DisplayName("가장 많은 추천 곡 가져오기")
    void findTrackMostRecommended() throws AccountException {
        // given
        List<Track> tracks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            tracks.add(buildTrack("title", "trackId"));
        }
        doReturn(tracks).when(trackRepository).findTrackMostRecommended();

        // when
        MessageResponse result = trackService.findTrackByMostFavorite();

        // then
        assertThat(result.getMessage()).isEqualTo(REQUEST_SUCCESS.getMessage());
        assertThat(((List) result.getData()).size()).isEqualTo(5);
    }

    public TrackPlayCountResponse buildTrackPlayCountResponse() {
        return TrackPlayCountResponse.builder()
                .playCount(0)
                .trackId("trackId")
                .trackImageUrl("images")
                .trackName("trackName")
                .trackArtist("trackArtist")
                .build();
    }

    public TrackPlayCount buildTrackPlayCount() {
        return TrackPlayCount.builder()
                .playCount(0)
                .trackId("trackId")
                .trackImageUrl("images")
                .trackName("trackName")
                .trackArtist("trackArtist")
                .build();
    }

    private Track buildTrack(String title, String spotifyId) {
        return Track.builder()
                .trackTitle(title)
                .recommendCount(0L)
                .albumImageUrl("url")
                .spotifyTrackHref("spotifyTrackHref")
                .spotifyTrackId(spotifyId)
                .build();
    }

}
