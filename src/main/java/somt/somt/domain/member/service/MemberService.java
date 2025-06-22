package somt.somt.domain.member.service;


import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import somt.somt.common.config.PasswordConfig;
import somt.somt.common.exception.CustomException;
import somt.somt.common.exception.ErrorCode;
import somt.somt.common.security.dto.CustomUserDetails;
import somt.somt.domain.member.dto.RegisterRequestDTO;
import somt.somt.domain.member.entity.Member;
import somt.somt.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    final private MemberRepository memberRepository;

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

        if(memberRepository.existsByUserName(requestDTO.getUserName())){
            throw new CustomException(ErrorCode.DUPLICATE_USERNAME);
        }

        if(memberRepository.existsByNickName(requestDTO.getNickName()))
        {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }


        String encodePassword = PasswordConfig.encode(requestDTO.getPassword1());


       Member user = Member.create(requestDTO.getUserName(),
               encodePassword,
               requestDTO.getNickName(),
               requestDTO.getEmail(),
               "ADMIN");


       try {
           memberRepository.save(user);

       }catch (DataIntegrityViolationException e){
           throw new CustomException(ErrorCode.BAD_REGISTER_REQUEST);
       }
    }




    public void withdrawal(CustomUserDetails userDetails) {
        Member preMember = memberRepository.findById(userDetails.getMemberId())
                .orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        preMember.setIsActive();
        preMember.setModifyAt();
        memberRepository.save(preMember);
    }
}
