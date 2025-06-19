package somt.somt.user.service;


import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import somt.common.config.PasswordConfig;
import somt.common.exception.CustomException;
import somt.common.exception.ErrorCode;
import somt.somt.user.entity.Users;
import somt.somt.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    final private UserRepository userRepository;

    public void register(String userName,String nickName,String password){
       String encodePassword = PasswordConfig.encode(password);

       Users user = Users.create(nickName,userName,encodePassword);

       userRepository.save(user);
    }


    public void login(String userName, String password){
        Users user = userRepository.findByUserName(userName)
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));

        if(PasswordConfig.matches(password,user.getPassword())){
            throw new CustomException(ErrorCode.USERNAME_NOT_MATCH_PASSWORD);
        }
        Cookie cookie ;


    }


    public void logout(Cookie cookie) {

    }
}
