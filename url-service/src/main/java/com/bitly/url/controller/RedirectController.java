package com.bitly.url.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.bitly.url.service.UrlService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RedirectController {
  private final UrlService urlService;

  @GetMapping("/{shortCode}")
  public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Location", urlService.getLongUrl(shortCode));
    return new ResponseEntity<>(headers, HttpStatus.TEMPORARY_REDIRECT);
  }

}
