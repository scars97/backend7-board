package com.backend7.frameworkstudy.domain.board.repository;

import com.backend7.frameworkstudy.domain.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
}