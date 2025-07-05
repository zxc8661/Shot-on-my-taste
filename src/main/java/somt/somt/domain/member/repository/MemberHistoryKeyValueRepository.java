package somt.somt.domain.member.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import somt.somt.domain.member.entity.history.MemberHistoryKeyValue;

@Repository
public interface MemberHistoryKeyValueRepository extends JpaRepository<MemberHistoryKeyValue,Long> {
    MemberHistoryKeyValue findByDomainValue(String historyType);

    MemberHistoryKeyValue findByDatabaseValue(Integer dbValue);
}
