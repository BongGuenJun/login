package tmeprate_test.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tmeprate_test.domain.KakaoUserInfoResponseDto;
import tmeprate_test.service.kakaoService;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/kakao")
public class KakaoLoginController {
	
	private final kakaoService kakaoService;
	
	@GetMapping("/callback")
	public String callback(@RequestParam("code") String code, Model model) throws IOException {
		// 카카오로부터 받은 엑세스 토큰을 이용해 사용자 정보 가져오기
		String accessToken = kakaoService.getAccessTokenFromKakao(code);
		
		// 엑세스 토큰을 이용해 사용자 정보 가져오기
		KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);
		
		// 로그 추가
		if (userInfo != null) {
			log.info("Received user info: {}", userInfo);
		} else {
			log.warn("Failed to fetch user info");
		}
		
		// null 체크 추가
		if (userInfo == null) {
			model.addAttribute("error", "Failed to fetch user info");
			return "error"; // error 페이지로 리다이렉트하거나 error 메시지를 출력하도록 처리
		}
		
		// 사용자 정보를 Model에 추가하여 뷰로 전달
		model.addAttribute("userInfo", userInfo);
		
		log.info("User Info: {}", userInfo);
		
		// User 로그인, 또는 회원가입 로직 추가
		return "user_info"; // 사용자 정보를 표시할 뷰
	}
}