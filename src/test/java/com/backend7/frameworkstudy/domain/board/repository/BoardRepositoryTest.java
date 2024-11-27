package com.backend7.frameworkstudy.domain.board.repository;

import com.backend7.frameworkstudy.domain.auth.JwtAuthenticationFilter;
import com.backend7.frameworkstudy.domain.auth.JwtTokenProvider;
import com.backend7.frameworkstudy.domain.board.domain.Board;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class BoardRepositoryTest {

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private BoardRepository boardRepository;

    @AfterEach
    void tearDown() {
        boardRepository.deleteAllInBatch();
    }

    @DisplayName("작성한 게시글이 정상적으로 저장된다.")
    @Test
    void createBoard() {
        // given
        Board board = create("게시글1", "게시글 내용");

        // when
        Board saveBoard = boardRepository.save(board);

        //then
        assertThat(saveBoard.getId()).isNotNull();
        assertThat(saveBoard)
                .extracting("title", "content")
                .contains("게시글1", "게시글 내용");
    }

    @DisplayName("저장된 게시글 목록을 조회한다.")
    @Test
    void findAllBoard() {
        // given
        Board board1 = create("게시글1", "게시글 내용1");
        Board board2 = create("게시글2", "게시글 내용2");
        Board board3 = create("게시글3", "게시글 내용3");
        boardRepository.saveAll(List.of(board1, board2, board3));

        // when
        List<Board> findBoards = boardRepository.findAll();

        //then
        assertThat(findBoards).hasSize(3)
                .extracting("title", "content")
                .contains(
                        tuple("게시글1", "게시글 내용1"),
                        tuple("게시글2", "게시글 내용2"),
                        tuple("게시글3", "게시글 내용3")
                );
    }

    @DisplayName("게시글 목록 조회 시 작성날짜 기준으로 내림차순 정렬한다.")
    @Test
    void findAll_orderByCreateDesc() {
        // given
        Board board1 = create("게시글1", "게시글 내용1");
        Board board2 = create("게시글2", "게시글 내용2");
        Board board3 = create("게시글3", "게시글 내용3");

        boardRepository.save(board1);
        boardRepository.save(board3);
        boardRepository.save(board2);

        // when
        List<Board> findBoards = boardRepository.findAllByOrderByCreateAtDesc();

        //then
        assertThat(findBoards).hasSize(3)
                .extracting("title", "content")
                .containsExactly(
                        tuple("게시글2", "게시글 내용2"),
                        tuple("게시글3", "게시글 내용3"),
                        tuple("게시글1", "게시글 내용1")
                );
    }

    private Board create(String title, String content) {
        return Board.builder()
                .title(title)
                .content(content)
                .build();
    }

}