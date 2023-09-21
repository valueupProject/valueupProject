package com.team.comma.domain.favorite.genre.repository;

import com.team.comma.domain.user.user.domain.User;

import java.util.List;

public interface FavoriteGenreRepositoryCustom {
    List<String> findByGenreNameList(User user);
}
