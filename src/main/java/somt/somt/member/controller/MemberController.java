package somt.somt.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import somt.somt.member.dto.LoginRequestDTO;
import somt.somt.member.dto.RegisterRequestDTO;
import somt.somt.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class MemberController {

    private final MemberService userService;

    @PostMapping("/regist")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequestDTO registerRequestDTO){

        userService.register(registerRequestDTO.getUserName(),registerRequestDTO.getNickName(),registerRequestDTO.getPassword());

        return ResponseEntity.ok("회원가입에 성공했습니다");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO,
                                   HttpServletResponse response){

        return ResponseEntity.ok("로그인에 성공했습니다.");
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout (HttpServletRequest request,HttpServletResponse response){



        return ResponseEntity.ok("로그아웃에 성공했습니다.");
    }

    @PostMapping("withdrawal")
    public ResponseEntity<?> withdrawal(HttpServletRequest request,HttpServletResponse response){

        return ResponseEntity.ok("회원탈퇴에 성공했습니다.");
    }

}
