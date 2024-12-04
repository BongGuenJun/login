package tmeprate_test.domain;

public class UserInfo {
    private String loginService; // 카카오, 네이버 등 로그인 서비스명
    private String id; // 사용자 아이디
    private String nickName; // 사용자 닉네임
    private String email; // 사용자 이메일

    // 4개의 필드를 받는 생성자 추가
    public UserInfo(String loginService, String id, String nickName, String email) {
        this.loginService = loginService;
        this.id = id;
        this.nickName = nickName;
        this.email = email;
    }

    // Getter와 Setter들 추가 (필요 시)
    public String getLoginService() {
        return loginService;
    }

    public void setLoginService(String loginService) {
        this.loginService = loginService;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
