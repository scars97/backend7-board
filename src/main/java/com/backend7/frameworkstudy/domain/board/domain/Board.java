package com.backend7.frameworkstudy.domain.board.domain;

import com.backend7.frameworkstudy.domain.board.dto.request.BoardUpdateRequest;
import com.backend7.frameworkstudy.domain.common.BaseEntity;
import com.backend7.frameworkstudy.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String title;

    private String content;

    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 테스트용
    public Board(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    @Builder
    public Board(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void setMember(Member member) {
        this.member = member;
        this.username = member.getUsername();
    }

    public void update(BoardUpdateRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
    }
}
