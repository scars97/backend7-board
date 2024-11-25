package com.backend7.frameworkstudy.domain.member.domain;

import com.backend7.frameworkstudy.domain.board.domain.Board;
import com.backend7.frameworkstudy.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String username;

    private String password;

    @OneToMany(mappedBy = "member")
    private List<Board> boards = new ArrayList<>();

    public Member(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    @Builder
    public Member(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void addBoard(Board board) {
        this.boards.add(board);
        board.setMember(this);
    }

}
