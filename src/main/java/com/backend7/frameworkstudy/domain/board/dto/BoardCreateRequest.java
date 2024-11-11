package com.backend7.frameworkstudy.domain.board.dto;

import com.backend7.frameworkstudy.domain.board.domain.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardCreateRequest {

    private String title;
    private String content;
    private String username;
    private String password;

    // 테스트 용도
    @Builder
    public BoardCreateRequest(String title, String content, String username, String password) {
        this.title = title;
        this.content = content;
        this.username = username;
        this.password = password;
    }

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .content(content)
                .username(username)
                .password(password)
                .build();
    }
}
