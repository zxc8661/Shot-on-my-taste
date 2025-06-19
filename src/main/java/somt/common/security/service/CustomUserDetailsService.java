package somt.common.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import somt.common.exception.CustomException;
import somt.common.exception.ErrorCode;
import somt.common.security.dto.CustomUserData;
import somt.common.security.dto.CustomUserDetails;
import somt.somt.member.entity.Member;
import somt.somt.member.repository.MemberRepository;


/**
 * UserDetailsService 구현체
 *
 * Db에서 사용자 정보를 가져와 CustomUserData에 저장
 *
 * @since 2025-03-26
 * @author 이광석
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    /**
     * Db에서 사용자 정보를 가져와 CustomUserDetails에 저장하는 메소드
     * @param username
     * @return
     * @throws UsernameNotFoundException
     *
     * @since 2025-03-26
     * @author 이광석
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username);

        if(member==null){
            throw new CustomException(ErrorCode.NOT_FOUND_MEMBER);

        }


        CustomUserData customUserData = new CustomUserData(member.getId(), member.getUserName(), member.getRole(), member.getPassword(), member.getNickName());


        return new CustomUserDetails(customUserData);
    }
}
