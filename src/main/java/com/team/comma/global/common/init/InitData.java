package com.team.comma.global.common.init;

import com.team.comma.domain.artist.domain.Artist;
import com.team.comma.domain.artist.repository.ArtistRepository;
import com.team.comma.domain.favorite.artist.domain.FavoriteArtist;
import com.team.comma.domain.favorite.artist.repository.FavoriteArtistRepository;
import com.team.comma.domain.favorite.track.domain.FavoriteTrack;
import com.team.comma.domain.favorite.track.repository.FavoriteTrackRepository;
import com.team.comma.domain.playlist.archive.domain.Archive;
import com.team.comma.domain.playlist.archive.repository.ArchiveRepository;
import com.team.comma.domain.playlist.playlist.domain.Playlist;
import com.team.comma.domain.playlist.playlist.repository.PlaylistRepository;
import com.team.comma.domain.playlist.track.domain.PlaylistTrack;
import com.team.comma.domain.track.artist.domain.TrackArtist;
import com.team.comma.domain.track.artist.repository.TrackArtistRepository;
import com.team.comma.domain.track.playcount.domain.TrackPlayCount;
import com.team.comma.domain.track.playcount.repository.TrackPlayCountRepository;
import com.team.comma.domain.track.track.domain.Track;
import com.team.comma.domain.track.track.repository.TrackRepository;
import com.team.comma.domain.user.detail.domain.UserDetail;
import com.team.comma.domain.user.user.constant.UserRole;
import com.team.comma.domain.user.user.constant.UserType;
import com.team.comma.domain.user.user.domain.User;
import com.team.comma.domain.user.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class InitData {

    private final UserRepository userRepository;
    private final TrackRepository trackRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TrackPlayCountRepository trackPlayCountRepository;
    private final PlaylistRepository playlistRepository;
    private final ArchiveRepository archiveRepository;
    private final FavoriteTrackRepository favoriteTrackRepository;
    private final FavoriteArtistRepository favoriteArtistRepository;
    private final ArtistRepository artistRepository;
    private final TrackArtistRepository trackArtistRepository;

    @Transactional
    @PostConstruct
    public void init() {
        User user1 = User.builder().email("testEmail").password(bCryptPasswordEncoder.encode("password")).role(UserRole.USER).delFlag(false).type(UserType.GENERAL_USER).joinDate(Date.valueOf(LocalDate.of(2023 , 8 , 21))).build();
        User user2 = User.builder().email("toUserEmail").password(bCryptPasswordEncoder.encode("password")).role(UserRole.USER).delFlag(false).type(UserType.GENERAL_USER).joinDate(Date.valueOf(LocalDate.of(2023 , 8 , 25))).build();

        UserDetail userDetail1 = UserDetail.builder().name("name").nickname("nickname").profileImageUrl("no profile image").popupAlertFlag(true).favoritePublicFlag(true).calenderPublicFlag(true).allPublicFlag(true).user(user1).build();
        UserDetail userDetail2 = UserDetail.builder().name("name2").nickname("nickname2").profileImageUrl("no profile image").popupAlertFlag(true).favoritePublicFlag(true).calenderPublicFlag(true).allPublicFlag(true).user(user2).build();
        user1.setUserDetail(userDetail1);
        user2.setUserDetail(userDetail2);

        userRepository.save(user1);
        userRepository.save(user2);

        Track track1 = Track.builder().id(1L).trackTitle("test track").durationTimeMs(210000).recommendCount(0L).albumImageUrl("https://i.scdn.co/image/ab67616d00001e02ff9ca10b55ce82ae553c8228").spotifyTrackId("id123").spotifyTrackHref("href").build();
        Track track2 = Track.builder().id(2L).trackTitle("test track2").durationTimeMs(210000).recommendCount(0L).albumImageUrl("https://i.scdn.co/image/ab67616d00001e02ff9ca10b55ce82ae553c8228").spotifyTrackId("id1234").spotifyTrackHref("href").build();

        TrackPlayCount trackPlayCount1 = TrackPlayCount.builder().playCount(1).track(track1).user(user1).build();

        Artist artist = Artist.builder().spotifyArtistId("artistId").artistName("artistName").build();
        TrackArtist trackArtist = TrackArtist.builder().track(track1).artist(artist).build();

        Playlist playlist1 = Playlist.builder().playlistTitle("test playlist").alarmStartTime(LocalTime.of(12 , 00)).alarmFlag(true).delFlag(false).user(user1).build();
        Playlist playlist2 = Playlist.builder().playlistTitle("test playlist2").alarmStartTime(LocalTime.of(13 , 00)).alarmFlag(true).delFlag(false).user(user1).build();

        PlaylistTrack playlistTrack1 = PlaylistTrack.builder().playSequence(1).trackAlarmFlag(true).playlist(playlist1).track(track1).build();
        playlist1.getPlaylistTrackList().add(playlistTrack1);
        PlaylistTrack playlistTrack2 = PlaylistTrack.builder().playSequence(1).trackAlarmFlag(true).playlist(playlist2).track(track1).build();
        playlist2.getPlaylistTrackList().add(playlistTrack2);
        PlaylistTrack playlistTrack3 = PlaylistTrack.builder().playSequence(1).trackAlarmFlag(true).playlist(playlist2).track(track2).build();
        playlist2.getPlaylistTrackList().add(playlistTrack3);

        Archive archive1 = Archive.builder().comment("archive1").publicFlag(false).createDate(LocalDateTime.of(2023 , 8 , 28 , 21 , 0 , 0)).user(user1).playlist(playlist1).build();
        Archive archive2 = Archive.builder().comment("archive2").publicFlag(false).createDate(LocalDateTime.of(2023 , 8 , 29 , 21 , 0 , 0)).user(user1).playlist(playlist1).build();

        FavoriteTrack favoriteTrack = FavoriteTrack.builder().user(user1).track(track1).build();
        FavoriteArtist favoriteArtist = FavoriteArtist.builder().artist(artist).user(user1).build();

        artistRepository.save(artist);
        trackRepository.save(track1);
        trackRepository.save(track2);
        trackArtistRepository.save(trackArtist);
        trackPlayCountRepository.save(trackPlayCount1);
        playlistRepository.save(playlist1);
        playlistRepository.save(playlist2);
        archiveRepository.save(archive1);
        archiveRepository.save(archive2);
        favoriteTrackRepository.save(favoriteTrack);
        favoriteArtistRepository.save(favoriteArtist);
    }

}
