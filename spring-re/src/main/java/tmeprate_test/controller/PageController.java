package tmeprate_test.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/", "/login"})
public class PageController {

    @Value("${kakao.client_id}")
    private String client_id;
    
    @Value("${kakao.redirect_uri}")
    private String redirect_uri;
    
    @Value("${naver.client_id}")
    private String naverClientId;

    @Value("${naver.redirect_uri}")
    private String naverRedirectUri;

    @GetMapping({"/", "/page"})
    public String loginPage(Model model) throws UnsupportedEncodingException {
        // URL 인코딩을 통해 안전하게 파라미터 처리
        String encodedRedirectUri = URLEncoder.encode(redirect_uri, "UTF-8");
        
        
        //카카오 로그인 URL 인코딩
        String kakaoLocation = "https://kauth.kakao.com/oauth/authorize?response_type=code"
                + "&client_id=" + client_id 
                + "&redirect_uri=" + encodedRedirectUri;
        
     // 네이버 로그인 URL 인코딩
        String encodedNaverRedirectUri = URLEncoder.encode(naverRedirectUri, "UTF-8");
        String naverLocation = "https://nid.naver.com/oauth2.0/authorize?response_type=code"
                + "&client_id=" + naverClientId
                + "&redirect_uri=" + encodedNaverRedirectUri;
        
        model.addAttribute("kakaoLocation", kakaoLocation);
        model.addAttribute("naverLocation", naverLocation);
        
        return "login";  // login.html로 리턴
    }
}