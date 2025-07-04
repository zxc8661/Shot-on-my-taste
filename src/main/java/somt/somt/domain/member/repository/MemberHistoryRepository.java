package somt.somt.domain.member.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import somt.somt.domain.member.entity.history.MemberHistory;

@Repository
public interface MemberHistoryRepository extends JpaRepository<MemberHistory,Long> {

    Page findByMemberId(Long memberId, Pageable pageable);
}
