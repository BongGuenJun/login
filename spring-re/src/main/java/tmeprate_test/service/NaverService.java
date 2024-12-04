package tmeprate_test.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tmeprate_test.domain.NaverTokenResponseDto;
import tmeprate_test.domain.NaverUserInfoResponseDto;
import tmeprate_test.domain.UserInfo;

@Slf4j
@RequiredArgsConstructor
@Service
public class NaverService {

    private final String NAVER_API_URL_HOST = "https://openapi.naver.com";
    private final WebClient.Builder webClientBuilder;

    @Value("${naver.client_id}")
    private String clientId;

    @Value("${naver.client_secret}")
    private String clientSecret;

    @Value("${naver.redirect_uri}")
    private String redirectUri;

    // 네이버에서 액세스 토큰을 가져오는 메소드
    public String getAccessTokenFromNaver(String code, String state) {
        NaverTokenResponseDto naverTokenResponseDto = webClientBuilder.baseUrl(NAVER_API_URL_HOST)
                .build()
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/oauth2.0/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("client_secret", clientSecret)
                        .queryParam("code", code)
                        .queryParam("state", state)
                        .build())
                .retrieve()
                .bodyToMono(NaverTokenResponseDto.class)
                .block();

        log.info("Naver Access Token: {}", naverTokenResponseDto.getAccessToken());

        return naverTokenResponseDto.getAccessToken();
    }

    // 네이버 API로부터 사용자 정보를 가져오는 메소드
    public UserInfo getUserInfo(String accessToken) {
        NaverUserInfoResponseDto naverUserInfoResponseDto = webClientBuilder.baseUrl("https://openapi.naver.com")
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/nid/me")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(NaverUserInfoResponseDto.class)
                .block();

        log.info("Naver User Info: {}", naverUserInfoResponseDto);

        // 'NaverUserInfoResponseDto' 객체에서 데이터를 추출하여 'UserInfo' 객체로 변환
        return new UserInfo(
            "naver", // 로그인 서비스
            naverUserInfoResponseDto.getId(),
            naverUserInfoResponseDto.getNickname(),
            naverUserInfoResponseDto.getEmail()
        );
    }
}
