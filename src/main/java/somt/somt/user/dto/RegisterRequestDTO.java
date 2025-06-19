package somt.somt.user.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {
    @NotBlank(message = "아이디는 필수 입력항목입니다")
    @Size(min=2,max = 20, message = "아이디는 2~20자 사이여야 합니다.")
    String userName;

    @NotBlank(message = "비밀번호는 필수 입력항목입니다")
    @Size(min=2,max = 20, message = "비밀번호는 2~20자 사이여야 합니다.")
    String password;

    @NotBlank(message = "닉네임은 필수 입력항목입니다")
    @Size(min=2,max = 20, message = "닉네임은 2~20자 사이여야 합니다.")
    String nickName;

}
