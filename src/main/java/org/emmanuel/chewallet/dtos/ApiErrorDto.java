package org.emmanuel.chewallet.dtos;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiErrorDto(
        LocalDateTime timestamp,
        int status,
        String error,
        Map<String,String> details
) {
}
