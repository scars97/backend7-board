package com.backend7.frameworkstudy.domain.board.api;

import com.backend7.frameworkstudy.domain.board.dto.BoardCreateRequest;
import com.backend7.frameworkstudy.domain.board.dto.BoardResponse;
import com.backend7.frameworkstudy.domain.board.service.BoardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

        // when //then
        mockMvc.perform(
                post("/api/board")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk());
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
}