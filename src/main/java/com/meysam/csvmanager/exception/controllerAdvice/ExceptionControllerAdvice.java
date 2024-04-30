package com.meysam.csvmanager.exception.controllerAdvice;

import com.meysam.csvmanager.config.messages.LocaleMessageSourceService;
import com.meysam.csvmanager.exception.exceptions.BusinessException;
import com.meysam.csvmanager.exception.exceptions.DbException;
import com.meysam.csvmanager.model.dto.RestResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBufferLimitException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.concurrent.TimeoutException;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ExceptionControllerAdvice {

    private final LocaleMessageSourceService messageSourceService;

    @Value("${spring.servlet.multipart.max-file-size:#{5}}")
    private String MAX_FILE_VOLUME;

    @Value("${files.max-file-bytes:#{3000000}}")
    private String MAX_FILE_BYTES;

    @ResponseBody
    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<RestResponse<?>>  businessException(BusinessException ex) {
        log.error("handling BusinessException at time :{}, exception is : {}",System.currentTimeMillis(),ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestResponse<>(null,messageSourceService.getMessage(ex.getMessage()),HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(DataBufferLimitException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public @ResponseBody
    ResponseEntity<?> fileException(DataBufferLimitException ex) {
        log.error("handling DataBufferLimitException at time :{}, exception is : {}",System.currentTimeMillis(),ex);
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(new RestResponse<>(null,"Maximum uploading file size exceeded: "+ MAX_FILE_BYTES +" bytes",HttpStatus.PAYLOAD_TOO_LARGE));
    }


    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    ResponseEntity<?> fileException(MaxUploadSizeExceededException ex) {
        log.error("handling MaxUploadSizeExceededException at time :{}, exception is : {}",System.currentTimeMillis(),ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestResponse<>(null,"Maximum uploading file size exceeded: "+MAX_FILE_VOLUME,HttpStatus.BAD_REQUEST));

    }



    @ResponseBody
    @ExceptionHandler(value = DbException.class)
    public ResponseEntity<RestResponse<?>>  dbException(DbException ex) {
        log.error("handling DbException at time :{}, exception is : {}",System.currentTimeMillis(),ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RestResponse<>(null,messageSourceService.getMessage(ex.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResponse<?>> exception(Exception ex){
        log.error("handling exception at time :{} , exception is : {}",System.currentTimeMillis(),ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RestResponse<>(null,messageSourceService.getMessage(ex.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
