package com.backend7.frameworkstudy.domain.board.dto.response;

import com.backend7.frameworkstudy.domain.board.domain.Board;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardResponse {

    private Long id;
    private String title;
    private String content;
    private String username;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;

    @Builder
    private BoardResponse(Long id, String title, String content, String username, LocalDateTime createAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.username = username;
        this.createAt = createAt;
        this.modifiedAt = modifiedAt;
    }

    public static BoardResponse of(Board board) {
        return BoardResponse.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .username(board.getUsername())
                .createAt(board.getCreateAt())
                .modifiedAt(board.getModifiedAt())
                .build();
    }
}
