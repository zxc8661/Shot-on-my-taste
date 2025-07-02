package somt.somt.common.test;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import somt.somt.common.security.dto.CustomUserDetails;

@RestController
public class TestController {


    @GetMapping("/api/admin/test")
    public ResponseEntity<?> testAdmin(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.ok(customUserDetails.getRole());
    }




    @GetMapping("/api/public/test")
    public ResponseEntity<?> testPublic(@AuthenticationPrincipal CustomUserDetails customUserDetails
                                        ){
        return ResponseEntity.ok(customUserDetails.getRole());
    }



    @GetMapping("/api/user/test")
    public ResponseEntity<?> testUser(){
        return ResponseEntity.ok("user 테스트 성공");
    }


    @GetMapping("/api/test")
    public ResponseEntity<?> test(@AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return ResponseEntity.ok(customUserDetails.getRole());
    }
}
