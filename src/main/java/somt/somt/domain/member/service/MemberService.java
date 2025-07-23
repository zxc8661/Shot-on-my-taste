package somt.somt.domain.member.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import somt.somt.common.exception.CustomException;
import somt.somt.common.exception.ErrorCode;
import somt.somt.common.security.dto.CustomUserDetails;
import somt.somt.domain.member.dto.member.MemberDetail;
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

        if(memberRepository.existsByNickname(requestDTO.getNickname()))
        {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }


        String encodePassword = bCryptPasswordEncoder.encode(requestDTO.getPassword1());



       Member user = Member.create(requestDTO.getUserName(),
               encodePassword,
               requestDTO.getNickname(),
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

    public boolean checkUserName(String memberName) {
        return memberRepository.existsByUserName(memberName);
    }

    public boolean checkNickname(String nickname){
        return memberRepository.existsByNickname(nickname);
    }

    public MemberDetail memberDetail(CustomUserDetails customUserDetails) {
        Member member = getMember(customUserDetails.getMemberId());

        return new MemberDetail(member.getUserName(),member.getId(),member.getEmail(),member.getNickname(),member.getCreateAt());
    }

    @Transactional
    public void modifyEmail(CustomUserDetails customUserDetails, String email) {
        if(!isValidEmail(email)){
            throw new CustomException(ErrorCode.INVALID_EMAIL_FORMAT);
        }

        Member member = getMember(customUserDetails.getMemberId());

        member.modifyEmail(email);
    }

    @Transactional
    public void modifyNickname(CustomUserDetails customUserDetails, String nickname) {
        if(checkNickname(nickname)){
            throw  new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }

        Member member = getMember(customUserDetails.getMemberId());

        member.modifyNickname(nickname);

    }


    @Transactional
    public void modifyPassword(CustomUserDetails customUserDetails, String newPassword, String oldPassword) {

        Member member = getMember(customUserDetails.getMemberId());

        if(!checkPassword(member,oldPassword)){
            throw new CustomException(ErrorCode.NOT_MATCH_PASSWORDS);
        }


        member.modifyPassword(bCryptPasswordEncoder.encode(newPassword));
    }


    private boolean checkPassword(Member member, String prePassword) {
        return bCryptPasswordEncoder.matches(prePassword,member.getPassword());
    }


    private boolean isValidEmail(String email){
        String emailRegex ="^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

}
