package com.bitly.url.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bitly.url.client.AuthServiceClient;
import com.bitly.url.dto.CreateUrlRequest;
import com.bitly.url.dto.UrlResponse;
import com.bitly.url.service.UrlService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/urls")
@RequiredArgsConstructor
@Slf4j
public class UrlController {
  private final AuthServiceClient authServiceClient;
  private final UrlService urlService;

  @PostMapping
  public UrlResponse createShortUrl(@RequestBody CreateUrlRequest req, @RequestHeader("Authorization") String authHeader) {
    Long userId = authServiceClient.validateToken(authHeader).getUserId();
    return urlService.createShortUrl(req, userId);
  }

  @GetMapping("/{shortCode}")
  public UrlResponse getUrlDetails(@PathVariable String shortCode, @RequestHeader("Authorization") String authHeader) {
    Long userId = authServiceClient.validateToken(authHeader).getUserId();
    return urlService.getUrlDetails(shortCode, userId);
  }

  @DeleteMapping("/{shortCode}")
  public void deleteUrl(@PathVariable String shortCode, @RequestHeader("Authorization") String authHeader) {
    Long userId = authServiceClient.validateToken(authHeader).getUserId();
    urlService.deleteUrl(shortCode, userId);
  }
}