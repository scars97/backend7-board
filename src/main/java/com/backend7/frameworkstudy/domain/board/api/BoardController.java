package com.backend7.frameworkstudy.domain.board.api;

import com.backend7.frameworkstudy.domain.auth.MemberDetail;
import com.backend7.frameworkstudy.domain.board.dto.request.BoardCreateRequest;
import com.backend7.frameworkstudy.domain.board.dto.response.BoardResponse;
import com.backend7.frameworkstudy.domain.board.dto.request.BoardUpdateRequest;
import com.backend7.frameworkstudy.domain.board.service.BoardService;
import com.backend7.frameworkstudy.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.backend7.frameworkstudy.domain.board.exception.BoardResultType.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
@Tag(name = "게시판 API", description = "게시판 조회, 작성, 수정, 삭제")
public class BoardController {

    private final BoardService boardService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "게시글 작성")
    public ApiResponse<BoardResponse> createBoard(@RequestBody BoardCreateRequest request, @AuthenticationPrincipal MemberDetail memberDetail) {
        BoardResponse response = boardService.createBoard(memberDetail.getId(), request);
        return ApiResponse.ok(CREATE_BOARD, response);
    }

    @GetMapping("")
    @Operation(summary = "게시글 목록 조회")
    public ApiResponse<List<BoardResponse>> findAll() {
        List<BoardResponse> response = boardService.findAll();
        return ApiResponse.ok(LOADED_BOARD_LIST, response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "게시글 상세 조회")
    public ApiResponse<BoardResponse> findBoard(@PathVariable("id") Long id) {
        BoardResponse response = boardService.findBoardBy(id);
        return ApiResponse.ok(LOADED_BOARD, response);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "게시글 수정")
    public ApiResponse<BoardResponse> editBoard(
            @PathVariable("id") Long boardId, @RequestBody BoardUpdateRequest request, @AuthenticationPrincipal MemberDetail memberDetail) {
        BoardResponse response = boardService.editBoard(memberDetail.getId(), boardId, request);
        return ApiResponse.ok(EDIT_BOARD, response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "게시글 삭제")
    public ApiResponse<Void> deleteBoard(
            @PathVariable("id") Long boardId, @AuthenticationPrincipal MemberDetail memberDetail) {
        boardService.deleteBoard(memberDetail.getId(), boardId);
        return ApiResponse.ok(DELETE_BOARD);
    }
}
