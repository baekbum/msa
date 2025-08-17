package com.example.order_service.client.error;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default(); // Feign의 기본 디코더

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 400 || response.status() == 404) {
            if (methodKey.contains("getUserByUserId")) {
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 사용자의 정보가 존재하지 않습니다.");
            }
        }

        return null;
    }
}
