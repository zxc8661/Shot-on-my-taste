package somt.somt.domain.member.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import somt.somt.common.CustomResponse.CustomPageResponse;
import somt.somt.common.exception.CustomException;
import somt.somt.common.exception.ErrorCode;
import somt.somt.domain.member.dto.MemberHistory.MemberHistoryDTO;
import somt.somt.domain.member.entity.Member;
import somt.somt.domain.member.entity.history.MemberHistory;
import somt.somt.domain.member.entity.history.MemberHistoryKeyValue;
import somt.somt.domain.member.repository.MemberHistoryKeyValueRepository;
import somt.somt.domain.member.repository.MemberHistoryRepository;
import somt.somt.domain.member.repository.MemberRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MemberHistoryService {

    private final MemberHistoryRepository memberHistoryRepository;
    private final MemberHistoryKeyValueRepository memberHistoryKeyValueRepository;
    private final MemberRepository memberRepository;

    public CustomPageResponse<MemberHistoryDTO> getHistory(int page, int size, Long memberId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("happenTime").descending());
        Page<MemberHistory> historyPage;
        if (memberId == null) {
            historyPage = memberHistoryRepository.findAll(pageable);
        } else {
            historyPage = memberHistoryRepository.findByMemberId(memberId, pageable);
        }

        List<MemberHistoryDTO> historyList = historyPage.stream()
                .map(p -> new MemberHistoryDTO(
                        p.getMember().getNickname(),
                        p.getMember().getId(),
                        toDomainValue(p.getHistoryType()),
                        p.getMessage(),
                        p.getHappenTime()))
                .toList();


        return CustomPageResponse.of(
                historyList,
                historyPage.getTotalPages(),
                historyPage.getTotalElements(),
                historyPage.getNumber()

        );
    }

    @Transactional
    public void createHistory(String historyType, String message, Long memberId) {
        Integer exchangeHistoryType = toDatabaseValue(historyType);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND_MEMBER));


        MemberHistory memberHistory = new MemberHistory(member,exchangeHistoryType,message );

        memberHistoryRepository.save(memberHistory);
    }


    private String toDomainValue(Integer dbValue){
        MemberHistoryKeyValue memberHistoryKeyValue =
                memberHistoryKeyValueRepository.findByDatabaseValue(dbValue);

        return memberHistoryKeyValue.getDomainValue();
    }

    private Integer toDatabaseValue(String domainValue){
        MemberHistoryKeyValue memberHistoryKeyValue =
               memberHistoryKeyValueRepository.findByDomainValue(domainValue);

        return memberHistoryKeyValue.getDatabaseValue();

    }

}