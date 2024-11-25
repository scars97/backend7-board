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

    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Board(String title, String content, String username, String password) {
        this.title = title;
        this.content = content;
        this.username = username;
        this.password = password;
    }

    public void setMember(Member member) {
        this.member = member;
        this.username = member.getUsername();
    }

    public void update(BoardUpdateRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.username = request.getUsername();
    }

    // 고민필요 - 수정뿐만 아니라 삭제도 password 체크를 해야 한다.
    public boolean isNotSamePassword(String requestPassword) {
        return !this.password.equals(requestPassword);
    }
}
