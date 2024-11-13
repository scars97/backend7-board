package com.backend7.frameworkstudy.domain.board.api;

import com.backend7.frameworkstudy.domain.board.dto.BoardCreateRequest;
import com.backend7.frameworkstudy.domain.board.dto.BoardDeleteRequest;
import com.backend7.frameworkstudy.domain.board.dto.BoardResponse;
import com.backend7.frameworkstudy.domain.board.dto.BoardUpdateRequest;
import com.backend7.frameworkstudy.domain.board.exception.SuccessType;
import com.backend7.frameworkstudy.domain.board.service.BoardService;
import com.backend7.frameworkstudy.global.common.ApiResponse;
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
    public ApiResponse<BoardResponse> createBoard(@RequestBody BoardCreateRequest request) {
        BoardResponse response = boardService.createBoard(request);
        return ApiResponse.ok(SuccessType.CREATE_BOARD, response);
    }

    @GetMapping("")
    public ApiResponse<List<BoardResponse>> findAll() {
        List<BoardResponse> response = boardService.findAll();
        return ApiResponse.ok(SuccessType.LOADED_BOARD_LIST, response);
    }

    @GetMapping("/{id}")
    public ApiResponse<BoardResponse> findBoard(@PathVariable("id") Long id) {
        BoardResponse response = boardService.findBoardBy(id);
        return ApiResponse.ok(SuccessType.LOADED_BOARD, response);
    }

    @PatchMapping("/{id}")
    public ApiResponse<BoardResponse> editBoard(
            @PathVariable("id") Long id, @RequestBody BoardUpdateRequest request) {
        BoardResponse response = boardService.editBoard(id, request);
        return ApiResponse.ok(SuccessType.EDIT_BOARD, response);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteBoard(
            @PathVariable("id") Long id, @RequestBody BoardDeleteRequest request) {
        boardService.deleteBoard(id, request);
        return ApiResponse.ok(SuccessType.DELETE_BOARD);
    }
}
