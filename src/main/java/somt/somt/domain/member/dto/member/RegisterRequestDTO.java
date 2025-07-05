package somt.somt.domain.member.dto.member;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {
    @NotBlank(message = "아이디는 필수 입력항목입니다")
    @Size(min=2,max = 20, message = "아이디는 2~20자 사이여야 합니다.")
    String userName;

    @NotBlank(message = "비밀번호는 필수 입력항목입니다")
    @Size(min=2,max = 20, message = "비밀번호는 2~20자 사이여야 합니다.")
    String password1;

    @NotBlank(message = "2차 비밀번호는 필 수 입력 항목입니다.")
    @Size(min=2, max =20, message = "비밀번호는 2~20자 사이여야 합니다.")
    String password2;


    @NotBlank(message = "이메일은 필수 입력 항목입니다." )
    @Email(message = "올바른 이메일 형식이 아닙니다")
    String email;

    @NotBlank(message = "닉네임은 필수 입력항목입니다")
    @Size(min=2,max = 20, message = "닉네임은 2~20자 사이여야 합니다.")
    String nickName;

    @NotBlank
    String role;


}
