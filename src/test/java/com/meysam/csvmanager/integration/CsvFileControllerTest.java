package com.meysam.csvmanager.integration;

import com.meysam.csvmanager.controller.CsvFileController;
import com.meysam.csvmanager.model.dto.CsvRecordResponseDto;
import com.meysam.csvmanager.model.dto.RestResponse;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CsvFileControllerTest {

    @Autowired
    private CsvFileController csvFileController;

    @Test
    @Order(1)
    public void uploadCsvFile_validCsv_uploadsAndReturnsSuccess() throws Exception {
        ClassPathResource resource = new ClassPathResource("exercise.csv");
        byte[] bytes = resource.getContentAsByteArray();

        MultipartFile mockMultipartFile = new MockMultipartFile(
                resource.getFilename(),
                resource.getFilename(),
                "multipart/form-data",
                bytes);

        ResponseEntity<RestResponse<String>> response = csvFileController.upload(mockMultipartFile);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    @Order(2)
    public void fetchAllData_validRequest_returnsUploadedData() throws Exception {

        ResponseEntity<RestResponse<Page<CsvRecordResponseDto>>> response = csvFileController.fetchAllData(1, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    @Order(3)
    public void fetchByCode_wrongCode_throwsProperException() throws Exception {

        String code = "wrongCode";

        try {
            ResponseEntity<RestResponse<CsvRecordResponseDto>> response = csvFileController.fetchByCode(code);
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }catch (Exception e){
            assertEquals("RECORD_WITH_THIS_CODE_DOES_NOT_EXIST",e.getMessage());
        }
    }


    @Test
    @Order(4)
    public void deleteAllRecords_success_deletesData() throws Exception {
        ResponseEntity<RestResponse<?>> response = csvFileController.deleteAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


}