package com.backend7.frameworkstudy.domain.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardDeleteRequest {

    private String password;

    @Builder
    public BoardDeleteRequest(String password) {
        this.password = password;
    }
}
