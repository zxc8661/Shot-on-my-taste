package somt.somt.domain.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import somt.somt.common.CustomResponse.CustomResponse;
import somt.somt.common.security.dto.CustomUserDetails;
import somt.somt.domain.member.dto.member.RegisterRequestDTO;
import somt.somt.domain.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberService userService;

    @PostMapping("/member/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequestDTO registerRequestDTO){

        userService.register(registerRequestDTO);



        return ResponseEntity.status(HttpStatus.CREATED).body(
                new CustomResponse<>(true,"회원가입 성공",null)
        );
    }


    @PostMapping("/member/withdrawal")
    public ResponseEntity<?> withdrawal(@AuthenticationPrincipal CustomUserDetails userDetails){
        userService.withdrawal(userDetails);
        return ResponseEntity.ok("회원탈퇴에 성공했습니다.");
    }

    @GetMapping("/public/member/checkUsername")
    public ResponseEntity<?> checkUsername(@RequestParam("userName") String userName){
        boolean result = userService.checkUserName(userName);

        if(result){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomResponse<>(false,"이미 존재하는 아이디",userName));
        }
        else{
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse<>(true,"아이디 사용가능",userName));

        }
    }



    @GetMapping("/public/member/checkNickname")
    public ResponseEntity<?> checkNickname(@RequestParam("nickname") String nickname){
        boolean result = userService.checkNickname(nickname);

        if(result){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomResponse<>(false,"이미 존재하는 아이디",nickname));

        }else{
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse<>(false,"닉네임 사용가능 ",nickname));

        }
    }
}
