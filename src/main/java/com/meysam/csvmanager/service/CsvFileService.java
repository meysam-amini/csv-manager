package com.meysam.csvmanager.service;


import com.meysam.csvmanager.model.dto.CsvRecordResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public sealed interface CsvFileService permits CsvFIleServiceImpl {

    String upload(MultipartFile file);

    Page<CsvRecordResponseDto> getAllRecords(Integer pageNumber, Integer pageSize);

    CsvRecordResponseDto getRecordByCode(String code);

    void deleteAllRecords();


}
