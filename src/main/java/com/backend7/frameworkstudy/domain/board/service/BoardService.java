package com.backend7.frameworkstudy.domain.board.service;

import com.backend7.frameworkstudy.domain.board.domain.Board;
import com.backend7.frameworkstudy.domain.board.dto.BoardCreateRequest;
import com.backend7.frameworkstudy.domain.board.dto.BoardResponse;
import com.backend7.frameworkstudy.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
