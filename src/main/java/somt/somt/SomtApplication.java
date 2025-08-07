package somt.somt;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@OpenAPIDefinition(
		info = @Info(
				title = "shot on my taste API",
				description =  "게임 쇼핑몰",
				version ="v1"
		)
)
  @SecurityScheme(
     name = "bearerAuth", // @SecurityRequirement에서 사용할 이름
     type = SecuritySchemeType.HTTP, // HTTP 기반
     scheme = "bearer", // Bearer 토큰 사용
     bearerFormat = "JWT", // 토큰 형식은 JWT
     in = SecuritySchemeIn.HEADER, // 토큰은 헤더에 위치
     paramName = "Authorization" // 헤더 이름은 Authorization
		   )

@SpringBootApplication
public class SomtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SomtApplication.class, args);
	}

}
