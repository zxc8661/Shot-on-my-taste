package somt.somt.domain.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import somt.somt.common.CustomResponse.CustomResponse;
import somt.somt.common.security.dto.CustomUserDetails;
import somt.somt.domain.member.dto.member.MemberDetail;
import somt.somt.domain.member.dto.member.RegisterRequestDTO;
import somt.somt.domain.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/member/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequestDTO registerRequestDTO){

        memberService.register(registerRequestDTO);



        return ResponseEntity.status(HttpStatus.CREATED).body(
                new CustomResponse<>(true,"회원가입 성공","Not data",null)
        );
    }


    @PostMapping("/member/withdrawal")
    public ResponseEntity<?> withdrawal(@AuthenticationPrincipal CustomUserDetails userDetails){
        memberService.withdrawal(userDetails);
        return ResponseEntity.ok("회원탈퇴에 성공했습니다.");
    }

    @GetMapping("/public/member/checkUsername")
    public ResponseEntity<?> checkUsername(@RequestParam("userName") String userName){
        boolean result = memberService.checkUserName(userName);

        if(result){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomResponse<>(false,"이미 존재하는 아이디","userName",userName));
        }
        else{
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse<>(true,"아이디 사용가능","userName",userName));

        }
    }



    @GetMapping("/public/member/checkNickname")
    public ResponseEntity<?> checkNickname(@RequestParam("nickname") String nickname){
        boolean result = memberService.checkNickname(nickname);

        if(result){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomResponse<>(false,"이미 존재하는 아이디","nickname",nickname));

        }else{
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse<>(true,"닉네임 사용가능 ","nickname",nickname));

        }
    }

    @GetMapping("/user/member/detail")
    public ResponseEntity<?> memberDetail(
            @AuthenticationPrincipal CustomUserDetails customUserDetails){
        MemberDetail memberDetail = memberService.memberDetail(customUserDetails);
        return ResponseEntity.ok(new CustomResponse<>(true,"멤버 상세 조회 성공","memberDetail",memberDetail));
    }

    @PutMapping("/user/member/modify/email")
    public ResponseEntity<?> memberModify(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam("email") String email){

        memberService.modifyEmail(customUserDetails, email);
        return ResponseEntity.ok(new CustomResponse<>(true,"이메일 수정 성공","email",email));
    }

    @PutMapping("/user/member/modify/nickname")
    public ResponseEntity<?> memberNickname(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam("nickname") String nickname){

        memberService.modifyNickname(customUserDetails,nickname);
        return ResponseEntity.ok(new CustomResponse<>(true,"닉네임 수정 성공","nickname",nickname));
    }

    @PutMapping("/user/member/modify/password")
    public ResponseEntity<?> memberPassword(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam("newpPassword") String newPassword,
            @RequestParam("oldPassword")String oldPassword){

        memberService.modifyPassword(customUserDetails,newPassword,oldPassword);
        return ResponseEntity.ok(new CustomResponse<>(true,"비밀번호 수정 성공","Not data",null ));
    }




}
