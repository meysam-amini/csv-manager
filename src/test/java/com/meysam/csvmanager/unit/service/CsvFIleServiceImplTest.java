package com.meysam.csvmanager.unit.service;

import com.meysam.csvmanager.config.messages.LocaleMessageSourceService;
import com.meysam.csvmanager.exception.exceptions.BusinessException;
import com.meysam.csvmanager.exception.exceptions.FileFormatException;
import com.meysam.csvmanager.repository.CsvRecordRepository;
import com.meysam.csvmanager.service.CsvFIleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CsvFIleServiceImplTest {


    @Mock
    private CsvRecordRepository csvRecordRepository;

    @Mock
    private LocaleMessageSourceService messageSourceService;

    @InjectMocks
    private CsvFIleServiceImpl csvFIleService;

    @Value("${server.file.upload.path}")
    private String UPLOADED_FOLDER;

    @Test
    public void testUpload_emptyFileName() {
        MultipartFile emptyFile = mock(MultipartFile.class);
        when(emptyFile.getOriginalFilename()).thenReturn("");
        assertThrows(BusinessException.class, () -> csvFIleService.upload(emptyFile));
    }

    @Test
    public void testUpload_invalidFileFormat() throws Exception {
        MultipartFile invalidFile = mock(MultipartFile.class);
        when(invalidFile.getOriginalFilename()).thenReturn("test.txt");
        assertThrows(FileFormatException.class, () -> csvFIleService.upload(invalidFile));
    }

    @Test
    public void testUpload_success() throws Exception {
        MultipartFile validFile = mock(MultipartFile.class);
        when(validFile.getOriginalFilename()).thenReturn("test.csv");
        when(validFile.getBytes()).thenReturn("valid,csv,data".getBytes());
        when(messageSourceService.getMessage(anyString())).thenReturn("Success message");
        ReflectionTestUtils.setField(csvFIleService, "UPLOADED_FOLDER", "something");
        csvFIleService.upload(validFile);

        // Verify interactions with mocks (e.g., csvRecordRepository.saveAll)
        verify(csvRecordRepository).saveAll(anyList());
    }

}

