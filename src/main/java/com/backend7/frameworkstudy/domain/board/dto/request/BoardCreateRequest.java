package com.backend7.frameworkstudy.domain.board.dto.request;

import com.backend7.frameworkstudy.domain.board.domain.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardCreateRequest {

    private String title;
    private String content;

    // 테스트 용도
    @Builder
    public BoardCreateRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .content(content)
                .build();
    }
}
