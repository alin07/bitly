package com.bitly.url.service;

import com.bitly.url.model.Url;
import com.bitly.url.repository.UrlRepository;
import com.bitly.url.util.Base62Encoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.bitly.url.dto.ClickEvent;
import com.bitly.url.dto.CreateUrlRequest;
import com.bitly.url.dto.UpdateUrlRequest;
import com.bitly.url.dto.UrlResponse;
import com.bitly.url.util.Base62Encoder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UrlService {
  private final UrlRepository urlRepository;
  private final Base62Encoder base62Encoder;
  private final RedisTemplate<String, String> redisTemplate;
  private final KafkaTemplate<String, ClickEvent> kafkaTemplate;

  @Value("${app.base-url}")
  private String baseUrl;

  private static final String REDIS_PREFIX = "url:";
  private static final String KAFKA_TOPIC = "click-events";

  @Transactional
  public UrlResponse createShortUrl(CreateUrlRequest request, Long userId) {
      log.info("Creating short URL for: {}", request.getLongUrl());
      String shortCodeOrSlug = request.getCustomSlug();
      boolean isCustom = false;

      if (shortCodeOrSlug != null && !shortCodeOrSlug.isEmpty()) {
        if (urlRepository.existsByShortCode(shortCodeOrSlug)) {
          throw new RuntimeException("Slug already exists");
        }
        isCustom = true;
      }
      Url url = Url.builder()
                    .longUrl(request.getLongUrl())
                    .userId(userId)
                    .isCustom(isCustom)
                    .build();
      if (isCustom) {
        url.setShortCode(shortCodeOrSlug);
        urlRepository.save(url);
        return toUrlResponse(url);
      }
      
      Url saved = urlRepository.save(url);      
      shortCodeOrSlug = base62Encoder.encode(saved.getId());
      saved.setShortCode(shortCodeOrSlug);
      urlRepository.save(saved);
      return toUrlResponse(saved);
  }

    public UrlResponse getUrlDetails(String shortCode, Long userId) {
      Url url = urlRepository.findByShortCode(shortCode)
                                 .orElseThrow(() -> new RuntimeException("Short code not found. " + shortCode));
      if (!url.getUserId().equals(userId)) {
          throw new RuntimeException("Unauthorized access");
      }
      return toRedirectResponse(url);
    }

    public void deleteUrl(String shortCode, Long userId) {
      urlRepository.deleteByShortCodeAndUserId(shortCode, userId);
    }

    private UrlResponse toRedirectResponse(Url url) {
      return UrlResponse.builder().longUrl(url.getLongUrl()).build();
    }

    private UrlResponse toUrlResponse(Url url) {
        return UrlResponse.builder()
                .shortUrl(baseUrl + "/" + url.getShortCode())
                .longUrl(url.getLongUrl())
                .shortCode(url.getShortCode())
                .createdAt(url.getCreatedAt())
                .build();
    }
}