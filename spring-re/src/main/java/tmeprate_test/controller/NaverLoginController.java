package tmeprate_test.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tmeprate_test.domain.NaverUserInfoResponseDto;
import tmeprate_test.domain.UserInfo;
import tmeprate_test.service.NaverService; // 네이버 로그인 처리 서비스

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/naver")  // 네이버 로그인 관련 URL
public class NaverLoginController {

    private final NaverService naverService;  // 네이버 서비스 객체
    
    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code, @RequestParam("state") String state, Model model) throws IOException {
        // 네이버로부터 받은 엑세스 토큰을 이용해 사용자 정보 가져오기
        String accessToken = naverService.getAccessTokenFromNaver(code, state);  // 서비스에서 엑세스 토큰 받기
        
        // 엑세스 토큰을 이용해 사용자 정보 가져오기
        UserInfo userInfo = naverService.getUserInfo(accessToken);  // 사용자 정보 받기
        
        // 로그 추가
        if (userInfo != null) {
            log.info("Received user info: {}", userInfo);
        } else {
            log.warn("Failed to fetch user info");
        }
        
        // null 체크 추가
        if (userInfo == null) {
            model.addAttribute("error", "Failed to fetch user info");
            return "error";  // error 페이지로 리다이렉트하거나 error 메시지를 출력하도록 처리
        }
        
        // 사용자 정보를 Model에 추가하여 뷰로 전달
        model.addAttribute("userInfo", userInfo);
        
        // user_info 뷰로 이동
        return "user_info";
    }
}
