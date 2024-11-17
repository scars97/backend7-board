package com.backend7.frameworkstudy.domain.member.repository;

import com.backend7.frameworkstudy.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
