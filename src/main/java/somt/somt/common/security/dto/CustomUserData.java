package somt.somt.common.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;

@Getter
@AllArgsConstructor
public class CustomUserData {
    private Long memberId;
    private String memberName;
    private String role;
    private String password;
    private String nickname;
}
