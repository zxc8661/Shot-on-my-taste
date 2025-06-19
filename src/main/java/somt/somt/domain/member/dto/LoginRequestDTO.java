package somt.somt.domain.member.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequestDTO {

    @NotBlank(message = "아이디는 필수 입력 항목입니다.")
    String userName;
    @NotBlank(message = "패스워드는 필수 입력 항목입니다.")
    String password;
}
