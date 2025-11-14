package main.java.com.bitly.url.client;

import com.bitly.url.dto.ValidateTokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthServiceClient {

    private final WebClient webClient;

    public AuthServiceClient(@Value("${auth.service.url}") String authServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(authServiceUrl)
                .build();
    }

    public ValidateTokenResponse validateToken(String token) {
        try {
            log.debug("Validating token with auth service");
            
            return webClient.get()
                    .uri("/auth/validate")
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .bodyToMono(ValidateTokenResponse.class)
                    .block(); //TODO: Blocking for simplicity - use reactive in production
                    
        } catch (Exception e) {
            log.error("Error validating token: {}", e.getMessage());
            return new ValidateTokenResponse(null, null, false);
        }
    }
}