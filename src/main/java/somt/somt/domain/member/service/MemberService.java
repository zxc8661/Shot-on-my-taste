package somt.somt.domain.member.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import somt.somt.common.config.PasswordConfig;
import somt.somt.common.exception.CustomException;
import somt.somt.common.exception.ErrorCode;
import somt.somt.domain.member.dto.RegisterRequestDTO;
import somt.somt.domain.member.entity.Member;
import somt.somt.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    final private MemberRepository userRepository;

    /**
     * 회원가입 메소드
     * @param requestDTO
     * @author 이광석
     * @since 2025-06-19
     */
    public void register(RegisterRequestDTO requestDTO){
        if(!requestDTO.getPassword1().equals(requestDTO.getPassword2())){
            throw new CustomException(ErrorCode.NOT_MATCH_PASSWORDS);
        }


       String encodePassword = PasswordConfig.encode(requestDTO.getPassword1());

       Member user = Member.create(requestDTO.getUserName(),
               encodePassword,
               requestDTO.getNickName(),
               requestDTO.getEmail(),
               "ADMIN");


       userRepository.save(user);
    }





}
