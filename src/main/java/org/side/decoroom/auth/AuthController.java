package org.side.decoroom.auth;

import org.side.decoroom.social.service.KakaoService;
import org.side.decoroom.user.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final KakaoService kakaoService;

    public AuthController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }

    @PostMapping("/kakao")
    public ResponseEntity<User> kakaoLogin(@RequestBody Map<String, String> body) {
        String accessToken = body.get("accessToken");
        User user = kakaoService.saveUser(accessToken);
        return ResponseEntity.ok(user);
    }
}
