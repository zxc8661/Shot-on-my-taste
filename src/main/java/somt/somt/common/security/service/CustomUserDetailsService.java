package somt.somt.common.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import somt.somt.common.exception.CustomException;
import somt.somt.common.exception.ErrorCode;
import somt.somt.common.security.dto.CustomUserData;
import somt.somt.common.security.dto.CustomUserDetails;
import somt.somt.domain.member.entity.Member;
import somt.somt.domain.member.repository.MemberRepository;



@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUserName(username)
                .orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        if(member==null){
            throw new CustomException(ErrorCode.NOT_FOUND_MEMBER);

        }


        CustomUserData customUserData = new CustomUserData(member.getId(), member.getUserName(), member.getRole(), member.getPassword(), member.getNickName());


        return new CustomUserDetails(customUserData);
    }
}
