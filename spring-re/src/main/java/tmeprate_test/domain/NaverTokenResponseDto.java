package tmeprate_test.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class NaverTokenResponseDto {

    @JsonProperty("access_token")
    private String accessToken;  // 네이버 API에서 반환한 액세스 토큰

    @JsonProperty("refresh_token")
    private String refreshToken; // 네이버 API에서 반환한 리프레시 토큰

    @JsonProperty("expires_in")
    private int expiresIn;       // 액세스 토큰의 유효 기간

    @JsonProperty("token_type")
    private String tokenType;    // 액세스 토큰의 타입
}
