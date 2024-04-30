package com.meysam.csvmanager.exception.controllerAdvice;

import com.meysam.csvmanager.config.messages.LocaleMessageSourceService;
import com.meysam.csvmanager.exception.exceptions.BusinessException;
import com.meysam.csvmanager.model.dto.RestResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.TimeoutException;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ExceptionControllerAdvice {

    private final LocaleMessageSourceService messageSourceService;

    @ResponseBody
    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<RestResponse<?>>  businessException(BusinessException ex) {
        log.error("handling BusinessException at time :{}, exception is : {}",System.currentTimeMillis(),ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestResponse<>(null,messageSourceService.getMessage(ex.getMessage()),HttpStatus.BAD_REQUEST));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResponse<?>> exception(Exception ex){
        log.error("handling exception at time :{} , exception is : {}",System.currentTimeMillis(),ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RestResponse<>(null,messageSourceService.getMessage(ex.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
