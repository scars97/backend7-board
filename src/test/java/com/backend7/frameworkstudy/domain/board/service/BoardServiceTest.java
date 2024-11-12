package com.backend7.frameworkstudy.domain.board.service;

import com.backend7.frameworkstudy.domain.board.domain.Board;
import com.backend7.frameworkstudy.domain.board.dto.BoardCreateRequest;
import com.backend7.frameworkstudy.domain.board.dto.BoardResponse;
import com.backend7.frameworkstudy.domain.board.repository.BoardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class BoardServiceTest {

    @Autowired
    private BoardService boardService;
    @Autowired
    private BoardRepository boardRepository;

    @DisplayName("게시글이 정상적으로 저장되고, 작성한 게시글이 반환된다.")
    @Test
    void createBoard() {
        // given
        Board board = create();
        BoardCreateRequest request = BoardCreateRequest.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .username(board.getUsername())
                .password(board.getPassword())
                .build();

        // when
        BoardResponse response = boardService.createBoard(request);

        //then
        assertThat(response.getId()).isNotNull();
        assertThat(response)
                .extracting("title", "content", "username")
                .contains("게시글1", "게시글 내용", "test@test.com");
    }

    @DisplayName("해당 id의 게시글이 존재하지 않는 경우 예외가 발생한다.")
    @Test
    void findBoardWithOutId() {
        // given
        Board board = create();
        boardRepository.save(board);

        // when //then
        assertThatThrownBy(() -> boardService.findBoardBy(2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("게시글이 존재하지 않습니다.");
    }

    private Board create() {
        return Board.builder()
                .title("게시글1")
                .content("게시글 내용")
                .username("test@test.com")
                .password("1234")
                .build();
    }
}