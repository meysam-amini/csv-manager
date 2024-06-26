package com.meysam.csvmanager.controller;

import com.meysam.csvmanager.config.messages.LocaleMessageSourceService;
import com.meysam.csvmanager.model.dto.CsvRecordResponseDto;
import com.meysam.csvmanager.model.dto.RestResponse;
import com.meysam.csvmanager.service.CsvFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/csv-file")
public class CsvFileController {

    private final CsvFileService csvFileService;
    private final LocaleMessageSourceService messageSourceService;

    @PostMapping(value = "upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RestResponse<String>> upload(@RequestParam("file") MultipartFile file){

        String result = csvFileService.upload(file);

        return ResponseEntity.ok(
                new RestResponse<>(result,messageSourceService.getMessage("SUCCESS"),HttpStatus.OK));

    }


    @GetMapping("fetch-all-data")
    public ResponseEntity<RestResponse<Page<CsvRecordResponseDto>>> fetchAllData(Integer pageNumber, Integer pageSize){
        Page<CsvRecordResponseDto> data = csvFileService.getAllRecords(pageNumber,pageSize);
        return ResponseEntity.ok(
                new RestResponse<>(data,messageSourceService.getMessage("SUCCESS"), HttpStatus.OK)
        );
    }


    @GetMapping("fetch-by-code/{code}")
    public ResponseEntity<RestResponse<CsvRecordResponseDto>> fetchByCode(@PathVariable("code") String code){
        CsvRecordResponseDto data = csvFileService.getRecordByCode(code);
        return ResponseEntity.ok(
                new RestResponse<>(data,messageSourceService.getMessage("SUCCESS"), HttpStatus.OK)
        );
    }

    @DeleteMapping
    @GetMapping("delete-all")
    public ResponseEntity<RestResponse<?>> deleteAll(){
        csvFileService.deleteAllRecords();
        return ResponseEntity.ok(
                new RestResponse<>(null,messageSourceService.getMessage("SUCCESS"), HttpStatus.OK)
        );
    }


}
