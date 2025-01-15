package org.side.decoroom.social.service;

import org.side.decoroom.user.entity.User;
import org.side.decoroom.user.repository.UserRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class KakaoService {
    private final UserRepository userRepository;

    public KakaoService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(String accessToken) {
        String kakaoApiUrl = "https://kapi.kakao.com/v2/user/me";
        RestTemplate restTemplate = new RestTemplate();

        var headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        var request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                kakaoApiUrl, HttpMethod.GET, request, Map.class
        );

        Map<String, Object> kakaoAccount = (Map<String, Object>) response.getBody().get("kakao_account");
        String kakaoId = String.valueOf(response.getBody().get("id"));
        String nickname = (String) ((Map<String, Object>) kakaoAccount.get("profile")).get("nickname");
        String email = (String) kakaoAccount.get("email");

        User user = userRepository.findByKakaoId(kakaoId);
        if (user == null) {
            user = new User();
            user.setKakaoId(kakaoId);
            user.setNickname(nickname);
            user.setEmail(email);
            userRepository.save(user);
        }

        return user;
    }
}
