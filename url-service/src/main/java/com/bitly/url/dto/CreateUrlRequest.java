package main.java.com.bitly.url.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUrlRequest {
    
    @NotBlank(message = "Long URL is required")
    @Pattern(regexp = "^https?://.*", message = "URL must start with http:// or https://")
    private String longUrl;

    @Size(max = 10, message = "Custom slug must be at most 10 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Custom slug can only contain letters and numbers")
    private String customSlug;
}