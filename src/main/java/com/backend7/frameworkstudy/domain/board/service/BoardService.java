package com.backend7.frameworkstudy.domain.board.service;

import com.backend7.frameworkstudy.domain.board.domain.Board;
import com.backend7.frameworkstudy.domain.board.dto.request.BoardCreateRequest;
import com.backend7.frameworkstudy.domain.board.dto.request.BoardDeleteRequest;
import com.backend7.frameworkstudy.domain.board.dto.response.BoardResponse;
import com.backend7.frameworkstudy.domain.board.dto.request.BoardUpdateRequest;
import com.backend7.frameworkstudy.domain.board.exception.BoardException;
import com.backend7.frameworkstudy.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.backend7.frameworkstudy.domain.board.exception.enumeration.ErrorType.*;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public BoardResponse createBoard(BoardCreateRequest request) {
        Board board = request.toEntity();

        Board saveBoard = boardRepository.save(board);

        return BoardResponse.of(saveBoard);
    }

    public List<BoardResponse> findAll() {
        List<Board> findBoards = boardRepository.findAllByOrderByCreateAtDesc();

        return findBoards.stream()
                .map(BoardResponse::of)
                .toList();
    }

    public BoardResponse findBoardBy(Long id) {
        Board findBoard = boardRepository.findById(id)
                .orElseThrow(() -> new BoardException(BOARD_NOT_FOUND));

        return BoardResponse.of(findBoard);
    }

    @Transactional
    public BoardResponse editBoard(Long id, BoardUpdateRequest request) {
        Board findBoard = boardRepository.findById(id)
                .orElseThrow(() -> new BoardException(BOARD_NOT_FOUND));

        if (findBoard.isNotSamePassword(request.getPassword())) {
            throw new BoardException(PASSWORD_IS_NOT_MATCH);
        }

        findBoard.update(request);
        return BoardResponse.of(findBoard);
    }

    @Transactional
    public void deleteBoard(Long id, BoardDeleteRequest request) {
        Board findBoard = boardRepository.findById(id)
                .orElseThrow(() -> new BoardException(BOARD_NOT_FOUND));

        if (findBoard.isNotSamePassword(request.getPassword())) {
            throw new BoardException(PASSWORD_IS_NOT_MATCH);
        }

        boardRepository.deleteById(id);
    }
}
