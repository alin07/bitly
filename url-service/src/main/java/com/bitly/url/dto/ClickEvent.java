package main.java.com.bitly.url.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClickEvent {
    private String shortCode;
    private LocalDateTime timestamp;
    private String referrer;
    private String userAgent;
    private String ipAddress;
}