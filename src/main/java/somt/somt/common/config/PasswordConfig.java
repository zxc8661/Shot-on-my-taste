package somt.somt.common.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String encode (String rawPassword){
        return passwordEncoder.encode(rawPassword);
    }

    public static boolean matches(String rawPassword, String encodePassword){
        return passwordEncoder.matches(rawPassword,encodePassword);
    }
}
