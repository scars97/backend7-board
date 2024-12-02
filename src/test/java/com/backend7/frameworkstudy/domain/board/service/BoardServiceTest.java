package com.backend7.frameworkstudy.domain.board.service;

import com.backend7.frameworkstudy.domain.board.domain.Board;
import com.backend7.frameworkstudy.domain.board.dto.request.BoardCreateRequest;
import com.backend7.frameworkstudy.domain.board.dto.request.BoardUpdateRequest;
import com.backend7.frameworkstudy.domain.board.dto.response.BoardResponse;
import com.backend7.frameworkstudy.domain.board.exception.BoardException;
import com.backend7.frameworkstudy.domain.board.exception.BoardResultType;
import com.backend7.frameworkstudy.domain.board.repository.BoardRepository;
import com.backend7.frameworkstudy.domain.member.domain.Member;
import com.backend7.frameworkstudy.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @InjectMocks
    private BoardService boardService;
    @Mock
    private BoardRepository boardRepository;
    @Mock
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
        BoardCreateRequest request = BoardCreateRequest.builder()
                .title("게시글1")
                .content("게시글 내용")
                .build();
        Member member = new Member(1L, "test1234", "qwer1234");
        Board board = request.toEntity();

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        member.addBoard(board);
        given(boardRepository.save(any(Board.class))).willReturn(board);

        // when
        BoardResponse response = boardService.createBoard(1L, request);

        //then
        assertThat(response)
                .extracting("title", "content", "username")
                .contains("게시글1", "게시글 내용", "test1234");

        then(memberRepository).should().findById(anyLong());
        then(boardRepository).should().save(any(Board.class));
    }

    @DisplayName("해당 id의 게시글이 존재하지 않는 경우 예외가 발생한다.")
    @Test
    void findBoard_WithOutId_ThrowException() {
        // given
        Long invalidId = 1L;
        given(boardRepository.findById(invalidId)).willReturn(Optional.empty());

        // when //then
        assertThatThrownBy(() -> boardService.findBoardBy(invalidId))
                .isInstanceOf(BoardException.class)
                .hasFieldOrPropertyWithValue("errorType", BoardResultType.BOARD_NOT_FOUND)
                .extracting("errorType")
                .extracting("status", "message")
                .containsExactly(HttpStatus.NOT_FOUND, "존재하지 않은 게시글입니다.");
        then(boardRepository).should().findById(invalidId);
    }

    @Transactional
    @DisplayName("본인이 작성한 게시글인 경우 정상적으로 수정된다.")
    @Test
    void editBoard_OwnerOfBoard_ShouldUpdateSuccessfully() {
        // given
        Member member = new Member(1L, "test1234", "qwer1234");
        Board board = new Board(1L, "게시글1", "게시글 내용1");
        board.setMember(member);
        BoardUpdateRequest updateRequest = BoardUpdateRequest.builder()
                .title("게시글123123")
                .content("게시글 내용123123")
                .build();

        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));

        // when
        BoardResponse boardResponse = boardService.editBoard(1L, 1L, updateRequest);

        //then
        assertThat(boardResponse)
                .extracting("id", "title", "content", "username")
                .contains(1L, "게시글123123", "게시글 내용123123", "test1234");
        then(boardRepository).should().findById(anyLong());
    }

    @DisplayName("수정할 게시글이 없는 경우 예외가 발생한다.")
    @Test
    void editBoard_BoardNotFound_ThrowException() {
        // given
        BoardUpdateRequest updateRequest = BoardUpdateRequest.builder()
                .title("게시글123123")
                .content("게시글 내용123123")
                .build();

        given(boardRepository.findById(anyLong())).willReturn(Optional.empty());

        // when //then
        assertThatThrownBy(() -> boardService.editBoard(1L, 1L, updateRequest))
                .isInstanceOf(BoardException.class)
                .hasFieldOrPropertyWithValue("errorType", BoardResultType.BOARD_NOT_FOUND)
                .extracting("errorType")
                .extracting("status", "message")
                .containsExactly( HttpStatus.NOT_FOUND, "존재하지 않은 게시글입니다.");
        then(boardRepository).should().findById(anyLong());
    }

    @DisplayName("본인의 게시글이 아닌 경우 수정되지 않고 예외가 발생한다.")
    @Test
    void editBoard_OtherOwnerOfBoard_ThrowException() {
        // given
        Long memberId = 1L; // 요청한 회원 ID
        Long boardId = 100L; // 수정하려는 게시글 ID
        Long otherMemberId = 2L; // 게시글의 작성자 ID (다른 사용자)

        Board board = new Board(boardId, "게시글1", "게시글 내용1");
        Member otherMember = new Member(otherMemberId, "test1234", "qwer1234");
        board.setMember(otherMember);

        BoardUpdateRequest updateRequest = BoardUpdateRequest.builder()
                .title("게시글123123")
                .content("게시글 내용123123")
                .build();

        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));

        // when //then
        assertThatThrownBy(() -> boardService.editBoard(memberId, boardId, updateRequest))
                .isInstanceOf(BoardException.class)
                .hasFieldOrPropertyWithValue("errorType", BoardResultType.CANNOT_EDIT_OTHER_BOARD)
                .extracting("errorType")
                .extracting("status", "message")
                .containsExactly( HttpStatus.FORBIDDEN, "다른 회원의 게시글은 수정할 수 없습니다.");
        then(boardRepository).should().findById(anyLong());
    }

    @DisplayName("본인 게시글인 경우 정상적으로 삭제된다.")
    @Test
    void deleteBoard_OwnerOfBoard_ShouldDeleteSuccessfully() {
        // given
        Member member = new Member(1L, "test1234", "qwer1234");
        Board board = new Board(1L, "게시글1", "게시글 내용1");
        board.setMember(member);

        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));
        given(boardRepository.existsById(anyLong())).willReturn(false);

        // when
        boardService.deleteBoard(1L, 1L);

        //then
        assertThat(boardRepository.existsById(1L)).isFalse();
        then(boardRepository).should().findById(anyLong());
        then(boardRepository).should().existsById(anyLong());
    }

    @DisplayName("삭제할 게시글이 없는 경우 예외가 발생한다.")
    @Test
    void deleteBoard_BoardNotFound_ThrowException() {
        // given
        given(boardRepository.findById(anyLong())).willReturn(Optional.empty());

        // when //then
        assertThatThrownBy(() -> boardService.deleteBoard(1L, 1L))
                .isInstanceOf(BoardException.class)
                .hasFieldOrPropertyWithValue("errorType", BoardResultType.BOARD_NOT_FOUND)
                .extracting("errorType")
                .extracting("status", "message")
                .containsExactly( HttpStatus.NOT_FOUND, "존재하지 않은 게시글입니다.");
        then(boardRepository).should().findById(anyLong());
    }

    @DisplayName("본인 게시글이 아닌 경우 삭제되지 않고 예외가 발생한다.")
    @Test
    void deleteBoard_OtherOwnerOfBoard_ThrowException() {
        // given
        Long memberId = 1L; // 요청한 회원 ID
        Long boardId = 100L; // 삭제하려는 게시글 ID
        Long otherMemberId = 2L; // 게시글의 작성자 ID (다른 사용자)

        Board board = new Board(boardId, "게시글1", "게시글 내용1");
        Member otherMember = new Member(otherMemberId, "test1234", "qwer1234");
        board.setMember(otherMember);

        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));
        given(boardRepository.existsById(anyLong())).willReturn(true);

        // when // then
        assertThatThrownBy(() -> boardService.deleteBoard(memberId, boardId))
                .isInstanceOf(BoardException.class)
                .hasFieldOrPropertyWithValue("errorType", BoardResultType.CANNOT_DELETE_OTHER_BOARD)
                .extracting("errorType")
                .extracting("status", "message")
                .containsExactly( HttpStatus.FORBIDDEN, "다른 회원의 게시글은 삭제할 수 없습니다.");
        assertThat(boardRepository.existsById(1L)).isTrue();
        then(boardRepository).should().findById(anyLong());
        then(boardRepository).should().existsById(anyLong());
    }
}