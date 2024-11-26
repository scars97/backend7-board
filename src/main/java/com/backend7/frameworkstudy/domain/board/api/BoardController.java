package com.backend7.frameworkstudy.domain.board.api;

import com.backend7.frameworkstudy.domain.auth.MemberDetail;
import com.backend7.frameworkstudy.domain.board.dto.request.BoardCreateRequest;
import com.backend7.frameworkstudy.domain.board.dto.response.BoardResponse;
import com.backend7.frameworkstudy.domain.board.dto.request.BoardUpdateRequest;
import com.backend7.frameworkstudy.domain.board.service.BoardService;
import com.backend7.frameworkstudy.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.backend7.frameworkstudy.domain.board.exception.enumeration.BoardResultType.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    @PostMapping("")
    public ApiResponse<BoardResponse> createBoard(@RequestBody BoardCreateRequest request, @AuthenticationPrincipal MemberDetail memberDetail) {
        BoardResponse response = boardService.createBoard(memberDetail.getId(), request);
        return ApiResponse.ok(CREATE_BOARD, response);
    }

    @GetMapping("")
    public ApiResponse<List<BoardResponse>> findAll() {
        List<BoardResponse> response = boardService.findAll();
        return ApiResponse.ok(LOADED_BOARD_LIST, response);
    }

    @GetMapping("/{id}")
    public ApiResponse<BoardResponse> findBoard(@PathVariable("id") Long id) {
        BoardResponse response = boardService.findBoardBy(id);
        return ApiResponse.ok(LOADED_BOARD, response);
    }

    @PatchMapping("/{id}")
    public ApiResponse<BoardResponse> editBoard(
            @PathVariable("id") Long boardId, @RequestBody BoardUpdateRequest request, @AuthenticationPrincipal MemberDetail memberDetail) {
        BoardResponse response = boardService.editBoard(memberDetail.getId(), boardId, request);
        return ApiResponse.ok(EDIT_BOARD, response);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteBoard(
            @PathVariable("id") Long boardId, @AuthenticationPrincipal MemberDetail memberDetail) {
        boardService.deleteBoard(memberDetail.getId(), boardId);
        return ApiResponse.ok(DELETE_BOARD);
    }
}
