package com.backend7.frameworkstudy.domain.board.service;

import com.backend7.frameworkstudy.domain.auth.JwtAuthenticationFilter;
import com.backend7.frameworkstudy.domain.auth.JwtTokenProvider;
import com.backend7.frameworkstudy.domain.board.domain.Board;
import com.backend7.frameworkstudy.domain.board.dto.request.BoardCreateRequest;
import com.backend7.frameworkstudy.domain.board.dto.request.BoardDeleteRequest;
import com.backend7.frameworkstudy.domain.board.dto.response.BoardResponse;
import com.backend7.frameworkstudy.domain.board.dto.request.BoardUpdateRequest;
import com.backend7.frameworkstudy.domain.board.exception.BoardException;
import com.backend7.frameworkstudy.domain.board.exception.enumeration.BoardResultType;
import com.backend7.frameworkstudy.domain.board.repository.BoardRepository;
import com.backend7.frameworkstudy.domain.member.domain.Member;
import com.backend7.frameworkstudy.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class BoardServiceTest {

    // MockBean을 하지 않으면 테스트 진행 X
    // JWT 와 관련 없는 도메인 테스트에 굳이 선언을 해줘야 하는 건가
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private BoardService boardService;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        boardRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("게시글이 정상적으로 저장되고, 작성한 게시글이 반환된다.")
    @Test
    void createBoard() {
        // given
        Member member = new Member(1L, "test1234", "qwer1234");
        memberRepository.save(member);
        BoardCreateRequest request = BoardCreateRequest.builder()
                .title("게시글1")
                .content("게시글 내용")
                .build();

        // when
        BoardResponse response = boardService.createBoard(member.getId(), request);

        //then
        assertThat(response.getId()).isNotNull();
        assertThat(response)
                .extracting("title", "content", "username")
                .contains("게시글1", "게시글 내용", "test1234");
    }

    @DisplayName("해당 id의 게시글이 존재하지 않는 경우 예외가 발생한다.")
    @Test
    void findBoardWithOutId() {
        // given
        Board board = create();
        boardRepository.save(board);

        // when //then
        assertThatThrownBy(() -> boardService.findBoardBy(2L))
                .isInstanceOf(BoardException.class)
                .hasFieldOrPropertyWithValue("errorType", BoardResultType.BOARD_NOT_FOUND)
                .extracting("errorType")
                .extracting("status", "message")
                .containsExactly(HttpStatus.NOT_FOUND, "존재하지 않은 게시글입니다.");
    }

    @Transactional
    @DisplayName("비밀번호 일치여부를 확인하고 게시글을 수정한다.")
    @Test
    void checkForPasswordMatchThenEditBoard() {
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

    // 수정, 삭제 시 비밀번호 일치 여부 확인 필요.
    // 2개의 테스트를 만들기엔 중복이 많다.
    // 통합적인 테스트를 작성해볼 수는 없을까?
    @DisplayName("비밀번호가 일치하지 않는 경우 게시글이 수정되지 않고 예외가 발생한다.")
    @Test
    void editBoard_passwordNotMatchThenExceptionThrown() {
        // given
        Board saveBoard = boardRepository.save(create());

        Long updateId = saveBoard.getId();
        BoardUpdateRequest updateRequest = BoardUpdateRequest.builder()
                .title("게시글123123")
                .content("게시글 내용123123")
                .username("test1111@test.com")
                .password("11111")
                .build();

        // when // then
        assertThatThrownBy(() -> boardService.editBoard(updateId, updateRequest))
                .isInstanceOf(BoardException.class)
                .hasFieldOrPropertyWithValue("errorType", BoardResultType.PASSWORD_IS_NOT_MATCH)
                .extracting("errorType")
                .extracting("status", "message")
                .containsExactly( HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");

        BoardResponse response = boardService.findBoardBy(updateId);
        assertThat(response.getTitle()).isNotEqualTo(updateRequest.getTitle());
    }

    @DisplayName("비밀번호 일치여부를 확인하고 게시글을 삭제한다.")
    @Test
    void deleteBoard() {
        // given
        Board saveBoard = boardRepository.save(create());

        Long deleteId = saveBoard.getId();
        BoardDeleteRequest deleteRequest = BoardDeleteRequest.builder()
                .password("1234")
                .build();

        // when
        boardService.deleteBoard(deleteId, deleteRequest);

        //then
        assertThat(boardRepository.existsById(deleteId)).isFalse();
    }

    @DisplayName("비밀번호가 일치하지 않는 경우 게시글이 삭제되지 않고 예외가 발생한다.")
    @Test
    void deleteBoard_passwordNotMatchThenExceptionThrown() {
        // given
        Board saveBoard = boardRepository.save(create());

        Long deleteId = saveBoard.getId();
        BoardDeleteRequest deleteRequest = BoardDeleteRequest.builder()
                .password("11111")
                .build();

        // when // then
        assertThatThrownBy(() -> boardService.deleteBoard(deleteId, deleteRequest))
                .isInstanceOf(BoardException.class)
                .hasFieldOrPropertyWithValue("errorType", BoardResultType.PASSWORD_IS_NOT_MATCH)
                .extracting("errorType")
                .extracting("status", "message")
                .containsExactly( HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");

        assertThat(boardRepository.existsById(deleteId)).isTrue();
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