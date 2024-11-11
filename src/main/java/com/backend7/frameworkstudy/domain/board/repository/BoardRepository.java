package com.backend7.frameworkstudy.domain.board.repository;

import com.backend7.frameworkstudy.domain.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findAllByOrderByCreateAtDesc();
}
