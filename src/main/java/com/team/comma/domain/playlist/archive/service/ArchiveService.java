package com.team.comma.domain.playlist.archive.service;

import com.team.comma.domain.playlist.archive.domain.Archive;
import com.team.comma.domain.playlist.archive.dto.ArchiveResponse;
import com.team.comma.domain.playlist.archive.repository.ArchiveRepository;
import com.team.comma.domain.playlist.playlist.repository.PlaylistRepository;
import com.team.comma.domain.playlist.playlist.exception.PlaylistException;
import com.team.comma.domain.user.user.domain.User;
import com.team.comma.domain.user.user.repository.UserRepository;
import com.team.comma.global.common.dto.MessageResponse;
import com.team.comma.domain.playlist.archive.dto.ArchiveRequest;
import com.team.comma.domain.playlist.playlist.domain.Playlist;
import com.team.comma.global.jwt.support.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountException;

import java.util.List;

import static com.team.comma.global.common.constant.ResponseCodeEnum.REQUEST_SUCCESS;

@Service
@RequiredArgsConstructor
public class ArchiveService {

    private final ArchiveRepository archiveRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;

    @Transactional
    public MessageResponse createArchive(final String token , final ArchiveRequest archiveRequest) throws AccountException {
        final String userEmail = jwtTokenProvider.getUserPk(token);
        final User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new AccountException("사용자를 찾을 수 없습니다."));
        final Playlist playlist = playlistRepository.findById(archiveRequest.getPlaylistId())
                .orElseThrow(() -> new PlaylistException("Playlist를 찾을 수 없습니다."));

        final Archive archive = Archive.buildArchive(user , archiveRequest.getContent() , playlist);
        archiveRepository.save(archive);

        return MessageResponse.of(REQUEST_SUCCESS);
    }

    public MessageResponse findAllArchive(final String token) throws AccountException {
        final String userEmail = jwtTokenProvider.getUserPk(token);
        final User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new AccountException("사용자를 찾을 수 없습니다."));

//        List<Archive> archives = archiveRepository.findAllArchiveByUser(user);


        return MessageResponse.of(REQUEST_SUCCESS);
    }

//    public List<ArchiveResponse>

}
