package com.meysam.csvmanager.service;

import com.meysam.csvmanager.model.dto.CsvRecordResponseDto;
import com.meysam.csvmanager.repository.CsvRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public non-sealed class CsvFIleServiceImpl implements CsvFileService {

    private final CsvRecordRepository csvRecordRepository;


    @Override
    public String upload() {
        return null;
    }

    @Override
    public Page<CsvRecordResponseDto> getAllRecords(int pageNumber, int pageSize) {
        return null;
    }

    @Override
    public CsvRecordResponseDto getRecordByCode(String code) {
        return null;
    }

    @Override
    public void deleteAllRecords() {

    }
}
