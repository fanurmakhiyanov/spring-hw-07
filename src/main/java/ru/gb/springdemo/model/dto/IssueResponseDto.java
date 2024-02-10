package ru.gb.springdemo.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record IssueResponseDto(@Schema(maxLength = 255) String bookName,
                               @Schema(maxLength = 255) String readerName,
                               @Schema(type = "date-time") LocalDateTime get,
                               @Schema(type = "date-time") LocalDateTime set) {
}