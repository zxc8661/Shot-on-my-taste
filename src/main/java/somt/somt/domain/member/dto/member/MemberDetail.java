package somt.somt.domain.member.dto.member;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDetail {
    private String userName;
    private Long memberId;
    private String email;
    private String nickname;
    private LocalDateTime createAt;
}
