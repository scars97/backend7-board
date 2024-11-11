package com.backend7.frameworkstudy.domain.board.repository;

import com.backend7.frameworkstudy.domain.board.domain.Board;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @DisplayName("작성한 게시글이 정상적으로 저장된다.")
    @Test
    void writeBoard() {
        // given
        Board board = Board.builder()
                .title("게시글1")
                .content("게시글 내용")
                .username("test@test.com")
                .password("1234")
                .build();

        // when
        Board saveBoard = boardRepository.save(board);

        //then
        assertThat(saveBoard.getId()).isNotNull();
        assertThat(saveBoard)
                .extracting("title", "content", "username", "password")
                .contains("게시글1", "게시글 내용", "test@test.com", "1234");
    }
}