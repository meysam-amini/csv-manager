package com.meysam.csvmanager.service;

import com.meysam.csvmanager.repository.CsvRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public non-sealed class CsvFIleServiceImpl implements CsvFileService {

    private final CsvRecordRepository csvRecordRepository;


}
