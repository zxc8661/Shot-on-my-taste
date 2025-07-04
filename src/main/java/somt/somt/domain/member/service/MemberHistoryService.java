package somt.somt.domain.member.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import somt.somt.domain.member.entity.history.MemberHistory;
import somt.somt.domain.member.repository.MemberHistoryRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MemberHistoryService {

    private final MemberHistoryRepository memberHistoryRepository;




    public Map<String,Object> getHistory(int page, int size, Long memberId) {
        Pageable pageable = PageRequest.of(page,size,Sort.by("happenTime").descending());
        Page<MemberHistory> historyPage;
        if(memberId==null){
            historyPage = memberHistoryRepository.findAll(pageable);
        }else{
            historyPage = memberHistoryRepository.findByMemberId(memberId,pageable);
        }

        List<MemberHistory> historyList = historyPage.stream().toList();

        Map<String, Object> response = new HashMap<>();

        response.put("content",historyList);
        response.put("totalPage",historyPage.getTotalPages());
        response.put("currentPage",historyPage.getNumber());
        response.put("totalElements",historyPage.getTotalElements());
        return response;
    }
}
