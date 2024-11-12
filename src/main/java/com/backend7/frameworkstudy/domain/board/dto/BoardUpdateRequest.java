package com.backend7.frameworkstudy.domain.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardUpdateRequest {

    private String title;
    private String username;
    private String content;
    private String password;

    @Builder
    public BoardUpdateRequest(String title, String username, String content, String password) {
        this.title = title;
        this.username = username;
        this.content = content;
        this.password = password;
    }
}
