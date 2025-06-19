package somt.somt.member.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import somt.somt.member.entity.Member;

import java.util.Optional;


@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {


    Optional<Member> findByUserName(String userName);
}
