package tmeprate_test.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class NaverUserInfoResponseDto {

    @JsonProperty("id")
    private String id;  // 네이버 사용자 고유 ID

    @JsonProperty("email")
    private String email;  // 사용자 이메일

    @JsonProperty("nickname")
    private String nickname;  // 사용자 닉네임

    @JsonProperty("profile_image")
    private String profileImage;  // 프로필 이미지 URL
}
