package somt.somt.domain.member.service;


import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import somt.somt.common.exception.CustomException;
import somt.somt.common.exception.ErrorCode;
import somt.somt.common.security.dto.CustomUserDetails;
import somt.somt.domain.member.dto.member.RegisterRequestDTO;
import somt.somt.domain.member.entity.Member;
import somt.somt.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    final private MemberRepository memberRepository;
    final private BCryptPasswordEncoder bCryptPasswordEncoder;


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


        String encodePassword = bCryptPasswordEncoder.encode(requestDTO.getPassword1());



       Member user = Member.create(requestDTO.getUserName(),
               encodePassword,
               requestDTO.getNickName(),
               requestDTO.getEmail(),
               requestDTO.getRole());


       try {
           memberRepository.save(user);

       }catch (DataIntegrityViolationException e){
           throw new CustomException(ErrorCode.BAD_REGISTER_REQUEST);
       }
    }




    public void withdrawal(CustomUserDetails userDetails) {
        Member preMember = getMember(userDetails.getMemberId());
        preMember.setIsActive();
        preMember.setModifyAt();
        memberRepository.save(preMember);
    }

    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }
}
