package com.backend7.frameworkstudy.domain.board.service;

import com.backend7.frameworkstudy.domain.board.domain.Board;
import com.backend7.frameworkstudy.domain.board.dto.BoardCreateRequest;
import com.backend7.frameworkstudy.domain.board.dto.BoardResponse;
import com.backend7.frameworkstudy.domain.board.dto.BoardUpdateRequest;
import com.backend7.frameworkstudy.domain.board.repository.BoardRepository;
import org.junit.jupiter.api.AfterEach;
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

    @AfterEach
    void tearDown() {
        boardRepository.deleteAllInBatch();
    }

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

    @DisplayName("비밀번호 일치여부를 확인하고 게시글을 수정한다.")
    @Test
    void editBoardAfterCheckForPasswordMatch() {
        // given
        Board saveBoard = boardRepository.save(create());

        Long updateId = saveBoard.getId();
        BoardUpdateRequest updateRequest = BoardUpdateRequest.builder()
                .title("게시글123123")
                .content("게시글 내용123123")
                .username("test1111@test.com")
                .password("1234")
                .build();

        // when
        Board findBoard = boardRepository.findById(updateId).orElse(saveBoard);

        //then
        assertThat(findBoard.getPassword()).isEqualTo(updateRequest.getPassword());

        findBoard.update(updateRequest);
        assertThat(findBoard)
                .extracting("title", "content", "username")
                .contains("게시글123123", "게시글 내용123123", "test1111@test.com");
    }

    @DisplayName("비밀번호가 일치하지 않는 경우 게시글이 수정되지 않고 예외가 발생한다.")
    @Test
    void passwordNotMatchThenExceptionThrown() {
        // given
        Board saveBoard = boardRepository.save(create());

        Long updateId = saveBoard.getId();
        BoardUpdateRequest updateRequest = BoardUpdateRequest.builder()
                .title("게시글123123")
                .content("게시글 내용123123")
                .username("test1111@test.com")
                .password("11111")
                .build();

        // when //then
        assertThatThrownBy(() -> boardService.editBoard(updateId, updateRequest))
                .hasMessage("비밀번호가 일치하지 않습니다.")
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(saveBoard.getTitle()).isNotEqualTo(updateRequest.getTitle());
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