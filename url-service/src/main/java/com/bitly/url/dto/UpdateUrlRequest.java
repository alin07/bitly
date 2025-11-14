package main.java.com.bitly.url.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUrlRequest {
    
    @NotBlank(message = "Long URL is required")
    @Pattern(regexp = "^https?://.*", message = "URL must start with http:// or https://")
    private String longUrl;
}