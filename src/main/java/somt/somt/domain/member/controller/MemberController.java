package somt.somt.domain.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import somt.somt.common.security.dto.CustomUserDetails;
import somt.somt.domain.member.dto.LoginRequestDTO;
import somt.somt.domain.member.dto.RegisterRequestDTO;
import somt.somt.domain.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequestDTO registerRequestDTO){

        userService.register(registerRequestDTO);

        return ResponseEntity.ok("회원가입에 성공했습니다");
    }






    @PostMapping("/withdrawal")
    public ResponseEntity<?> withdrawal(@AuthenticationPrincipal CustomUserDetails userDetails){
        userService.withdrawal(userDetails);
        return ResponseEntity.ok("회원탈퇴에 성공했습니다.");
    }

}
