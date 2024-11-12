package com.backend7.frameworkstudy.domain.board.api;

import com.backend7.frameworkstudy.domain.board.dto.BoardCreateRequest;
import com.backend7.frameworkstudy.domain.board.dto.BoardDeleteRequest;
import com.backend7.frameworkstudy.domain.board.dto.BoardResponse;
import com.backend7.frameworkstudy.domain.board.dto.BoardUpdateRequest;
import com.backend7.frameworkstudy.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    @PostMapping("")
    public ResponseEntity<BoardResponse> createBoard(@RequestBody BoardCreateRequest request) {
        BoardResponse response = boardService.createBoard(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("")
    public ResponseEntity<List<BoardResponse>> findAll() {
        List<BoardResponse> responses = boardService.findAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardResponse> findBoard(@PathVariable("id") Long id) {
        BoardResponse findBoard = boardService.findBoardBy(id);
        return ResponseEntity.ok(findBoard);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BoardResponse> editBoard(
            @PathVariable("id") Long id, @RequestBody BoardUpdateRequest request) {
        BoardResponse response = boardService.editBoard(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(
            @PathVariable("id") Long id, @RequestBody BoardDeleteRequest request) {
        boardService.deleteBoard(id, request);
        return ResponseEntity.noContent().build();
    }
}
