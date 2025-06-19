package somt.somt.member.service;


import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import somt.common.config.PasswordConfig;
import somt.common.exception.CustomException;
import somt.common.exception.ErrorCode;
import somt.somt.member.entity.Member;
import somt.somt.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    final private MemberRepository userRepository;

    public void register(String userName,String nickName,String password){
       String encodePassword = PasswordConfig.encode(password);

       Member user = Member.create(nickName,userName,encodePassword);

       userRepository.save(user);
    }





}
