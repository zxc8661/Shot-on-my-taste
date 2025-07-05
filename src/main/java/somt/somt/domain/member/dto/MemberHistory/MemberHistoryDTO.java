package somt.somt.domain.member.dto.MemberHistory;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberHistoryDTO {
    private String memberNickname;
    private Long memberId;
    private String historyType;
    private String message;
    private LocalDateTime eventTime;

}
