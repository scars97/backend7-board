package com.backend7.frameworkstudy.domain.board.api;

import com.backend7.frameworkstudy.domain.board.dto.BoardCreateRequest;
import com.backend7.frameworkstudy.domain.board.dto.BoardDeleteRequest;
import com.backend7.frameworkstudy.domain.board.dto.BoardResponse;
import com.backend7.frameworkstudy.domain.board.service.BoardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(BoardController.class)
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BoardService boardService;

    @DisplayName("게시글을 저장한다.")
    @Test
    void createBoard() throws Exception {
        // given
        BoardCreateRequest request = BoardCreateRequest.builder()
                .title("게시글1")
                .content("게시글 작성 내용 ㅇㅇㅇㅇㅇㅇㅇㅇ")
                .username("test@test.com")
                .password("12341234")
                .build();

        given(boardService.createBoard(any(BoardCreateRequest.class)))
                .willReturn(BoardResponse.builder()
                        .id(1L)
                        .title("게시글1")
                        .content("게시글 작성 내용 ㅇㅇㅇㅇㅇㅇㅇㅇ")
                        .username("test@test.com")
                        .createAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .build());

        // when //then
        mockMvc.perform(
                post("/api/board")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("게시글이 저장되었습니다."))
            .andExpect(jsonPath("$.data").isNotEmpty());
    }

    @DisplayName("게시글 목록을 내림차순으로 조회한다.")
    @Test
    void findAll_orderByCreateAtDesc() throws Exception {
        // given
        List<BoardResponse> result = List.of();

        when(boardService.findAll()).thenReturn(result);

        // when //then
        mockMvc.perform(
                get("/api/board")
            )
            .andDo(print())
            .andExpect(status().isOk());
    }

    @DisplayName("게시글 삭제 후 성공 표시를 반환한다.")
    @Test
    void deleteBoard() throws Exception {
        // given
        Long deleteId = 1L;
        BoardDeleteRequest deleteRequest = BoardDeleteRequest.builder().password("12341234").build();

        doNothing().when(boardService).deleteBoard(deleteId, deleteRequest);

        // when //then
        mockMvc.perform(
                    delete("/api/board/{id}", deleteId)
                            .content(objectMapper.writeValueAsString(deleteRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(204))
                .andExpect(jsonPath("$.status").value("NO_CONTENT"))
                .andExpect(jsonPath("$.message").value("게시글이 삭제되었습니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}