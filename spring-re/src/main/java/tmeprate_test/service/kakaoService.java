package tmeprate_test.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import io.netty.handler.codec.http.HttpHeaderValues;
import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpHeaders;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import tmeprate_test.domain.KakaoTokenResponseDto;
import tmeprate_test.domain.KakaoUserInfoResponseDto;

@Slf4j
@RequiredArgsConstructor
@Service
public class kakaoService {

	
	private final String KAUTH_TOKEN_URL_HOST = "https://kauth.kakao.com";
    private final String KAUTH_USER_URL_HOST = "https://kapi.kakao.com";
    private final WebClient.Builder webClientBuilder;

    @Value("${kakao.client_id}")
    private String clientId;

    @Value("${kakao.redirect_uri}")
    private String redirectUri;

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

    public KakaoUserInfoResponseDto getUserInfo(String accessToken) {
        KakaoUserInfoResponseDto userInfo = webClientBuilder.baseUrl(KAUTH_USER_URL_HOST)
                .build()
                .get()
                .uri("/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // 액세스 토큰을 이용한 인증
                .retrieve()
                .bodyToMono(KakaoUserInfoResponseDto.class)
                .block();

        log.info("User Info: {}", userInfo);
        return userInfo;
    }
    
    // 로그인 후 세션에 사용자 정보 저장하기
    public void saveUserInfoInSession(KakaoUserInfoResponseDto userInfo, HttpSession session) {
    	session.setAttribute("userInfo",  userInfo);
    }
	
}
