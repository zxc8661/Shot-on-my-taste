package somt.somt.domain.member.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import somt.somt.domain.member.entity.Member;

import java.util.Optional;


@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByUserName(String userName);


    boolean existsByUserName(String userName);
    boolean existsByNickname(String nickName);

}
