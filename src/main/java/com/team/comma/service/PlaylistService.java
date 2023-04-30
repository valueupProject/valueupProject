package com.team.comma.service;

import com.team.comma.domain.Playlist;
import com.team.comma.domain.PlaylistTrack;
import com.team.comma.domain.Track;
import com.team.comma.domain.TrackArtist;
import com.team.comma.dto.PlaylistResponse;
import com.team.comma.dto.PlaylistTrackArtistResponse;
import com.team.comma.dto.PlaylistTrackResponse;
import com.team.comma.repository.PlaylistRepository;
import com.team.comma.repository.PlaylistTrackRepository;
import com.team.comma.repository.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final PlaylistRepository playlistRepository;

    public List<PlaylistResponse> getPlaylist(final String email) {
        List<Playlist> playlists = playlistRepository.findAllByUser_Email(email); // email로 playlist 조회
        return createPlaylistResponse(playlists);
    }

    public List<PlaylistResponse> createPlaylistResponse(List<Playlist> playlists){
        List<PlaylistResponse> result = new ArrayList<>();
        for(Playlist playlist : playlists){
            List<PlaylistTrackResponse> trackList = createTrackResponse(playlist.getPlaylistTrackList()); // playlist의 track list
            result.add(PlaylistResponse.of(playlist, trackList));
        }
        return result;
    }

    public List<PlaylistTrackResponse> createTrackResponse(List<PlaylistTrack> playlistTrackList){
        List<PlaylistTrackResponse> result = new ArrayList<>();
        for (PlaylistTrack playlistTrack : playlistTrackList) {
            List<PlaylistTrackArtistResponse> artistList = createArtistResponse(playlistTrack.getTrack().getTrackArtistList()); // track의 artist list
            result.add(PlaylistTrackResponse.of(playlistTrack.getTrack(), playlistTrack.getTrackAlarmFlag(), artistList));
        }
        return result;
    }

    public List<PlaylistTrackArtistResponse> createArtistResponse(List<TrackArtist> artistList){
        List<PlaylistTrackArtistResponse> result = new ArrayList<>();
        for (TrackArtist artist : artistList){
            result.add(PlaylistTrackArtistResponse.of(artist));
        }
        return result;
    }








    public int updateAlarmFlag(Long playlistId, Boolean alarmFlag) {
        return playlistRepository.updateAlarmFlag(playlistId, alarmFlag);
    }

}
