package com.meysam.csvmanager.model.dto;

import org.springframework.http.HttpStatus;

public record RestResponse<T>(T data,
                              String message,
                              HttpStatus httpStatus) {
}