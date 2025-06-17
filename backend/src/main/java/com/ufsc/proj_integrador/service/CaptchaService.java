package com.ufsc.proj_integrador.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CaptchaService {

    @Value("${google.recaptcha.secret}")
    private String secret;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    public boolean isCaptchaValid(String token) {
        Map<String, Object> response = restTemplate.postForObject(
                VERIFY_URL + "?secret={secret}&response={token}",
                null,
                Map.class,
                secret,
                token
        );

        return response != null && Boolean.TRUE.equals(response.get("success"));
    }
}