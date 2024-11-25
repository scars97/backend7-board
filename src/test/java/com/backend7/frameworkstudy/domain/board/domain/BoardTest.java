package com.backend7.frameworkstudy.domain.board.domain;

import com.backend7.frameworkstudy.domain.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class BoardTest {

    @DisplayName("게시글 생성")
    @Test
    void createBoard() {
        // given
        Member member = new Member(1L, "test1234", "qwer1234");
        Board board = new Board("게시글", "게시글 내용", "", "1234");

        // when
        member.addBoard(board);

        // then
        assertThat(board.getMember()).isNotNull();
        assertThat(board.getUsername()).isEqualTo("test1234");
        assertThat(member.getBoards()).hasSize(1)
                .extracting("title", "content", "username")
                .contains(
                        tuple("게시글", "게시글 내용", "test1234")
                );
    }

}