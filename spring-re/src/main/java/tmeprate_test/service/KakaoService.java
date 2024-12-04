package tmeprate_test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import tmeprate_test.domain.KakaoTokenResponseDto;
import tmeprate_test.domain.KakaoUserInfoResponseDto;
import tmeprate_test.domain.UserInfo;

@Slf4j
@Service
public class KakaoService {

    private final String KAUTH_TOKEN_URL_HOST = "https://kauth.kakao.com";
    private final String KAUTH_USER_URL_HOST = "https://kapi.kakao.com";
    @Autowired
    private WebClient.Builder webClientBuilder;
    
    @Value("${kakao.client_id}")
    private String clientId;

    @Value("${kakao.redirect_uri}")
    private String redirectUri;

    // 액세스 토큰을 카카오로부터 받아오기
    public String getAccessTokenFromKakao(String code) {
        KakaoTokenResponseDto kakaoTokenResponseDto = webClientBuilder.baseUrl(KAUTH_TOKEN_URL_HOST)
                .build()
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("code", code)
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .retrieve()
                .bodyToMono(KakaoTokenResponseDto.class)
                .block();

        log.info("Access Token: {}", kakaoTokenResponseDto.getAccessToken());
        return kakaoTokenResponseDto.getAccessToken();
    }

    // 액세스 토큰을 이용해 카카오 사용자 정보 가져오기
    public UserInfo getUserInfo(String accessToken) {
        KakaoUserInfoResponseDto kakaoUserInfoResponseDto = webClientBuilder.baseUrl(KAUTH_USER_URL_HOST)
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/user/me")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(KakaoUserInfoResponseDto.class)
                .block();

        log.info("Kakao User Info: {}", kakaoUserInfoResponseDto);

        // UserInfo 객체로 반환
        return new UserInfo(
                "kakao",  // 로그인 서비스 이름
                kakaoUserInfoResponseDto.getId().toString(),  // 아이디
                kakaoUserInfoResponseDto.getKakaoAccount().getProfile().getNickName(),  // 닉네임
                kakaoUserInfoResponseDto.getKakaoAccount().getEmail()  // 이메일
        );
    }
}
