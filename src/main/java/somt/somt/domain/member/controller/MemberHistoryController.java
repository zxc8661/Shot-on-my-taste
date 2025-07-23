package somt.somt.domain.member.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import somt.somt.common.CustomResponse.CustomPageResponse;
import somt.somt.common.CustomResponse.CustomResponse;
import somt.somt.domain.member.dto.MemberHistory.MemberHistoryDTO;
import somt.somt.domain.member.entity.history.MemberHistory;
import somt.somt.domain.member.repository.MemberHistoryRepository;
import somt.somt.domain.member.service.MemberHistoryService;
import somt.somt.domain.member.service.MemberService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class MemberHistoryController {

    private final MemberHistoryService memberHistoryService;


    /**
     * @RequestParam 에서 값이 들어오지 않으면 400 에러 발생
     * required = false 를 하거나 defaultValue를 ""으로 해줘야됨
     */
    @GetMapping("/history")
    public ResponseEntity<?> getHistory(
            @RequestParam(name = "page",defaultValue = "0") int page,
            @RequestParam(name ="size" , defaultValue = "30")  int size,
            @RequestParam(name = "memberId", required = false) Long memberId){
        CustomPageResponse<MemberHistoryDTO> response = memberHistoryService.getHistory(page,size,memberId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomResponse.success(response,"멤버 히스토리 요청 성공"));
    }
}
