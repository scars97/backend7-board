package com.backend7.frameworkstudy.domain.board.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardUpdateRequest {

    private String title;
    private String content;

    @Builder
    public BoardUpdateRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
