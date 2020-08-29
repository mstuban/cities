package com.infinum.cities.model.errors;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.infinum.cities.auth.constants.StandardConstants.DATE_TIME_FORMAT;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {

    private Integer status;

    private String message;

    @JsonFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime timestamp;
}
