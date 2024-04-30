package com.meysam.csvmanager.service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.meysam.csvmanager.config.messages.LocaleMessageSourceService;
import com.meysam.csvmanager.exception.exceptions.BusinessException;
import com.meysam.csvmanager.exception.exceptions.DbException;
import com.meysam.csvmanager.exception.exceptions.FileFormatException;
import com.meysam.csvmanager.model.dto.CsvRecordResponseDto;
import com.meysam.csvmanager.model.entity.CsvRecord;
import com.meysam.csvmanager.repository.CsvRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public non-sealed class CsvFIleServiceImpl implements CsvFileService {

    private final CsvRecordRepository csvRecordRepository;
    private final LocaleMessageSourceService messageSourceService;
    @Value("${server.file.upload.path}")
    private String UPLOADED_FOLDER;

    @Override
    public String upload(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        //validate file name and extension
        fileName = validateFileName(fileName);

        String[] formatAndName = fileName.split("\\.");
        fileName = formatAndName[0] + "_" + new Date().getTime();
        String fileFormat = formatAndName[1];
        validateFileFormat(fileFormat);
        fileName += "." + fileFormat;


        try {
            Path dir = Paths.get(UPLOADED_FOLDER).toAbsolutePath();
            log.info("Saving file with name: " + fileName +
                    ", at time: " + System.currentTimeMillis() +
                    ", to path: " + dir);

            File input = new File( file.getOriginalFilename() );
            FileOutputStream fos = new FileOutputStream( input );
            fos.write( file.getBytes() );
            fos.close();

            CsvSchema schema = CsvSchema.builder()
                    .setUseHeader(true)
                    .addColumn("source", CsvSchema.ColumnType.STRING)
                    .addColumn("codeListCode", CsvSchema.ColumnType.STRING)
                    .addColumn("code", CsvSchema.ColumnType.STRING)
                    .addColumn("displayValue", CsvSchema.ColumnType.STRING)
                    .addColumn("longDescription", CsvSchema.ColumnType.STRING)
                    .addColumn("fromDate", CsvSchema.ColumnType.STRING)
                    .addColumn("toDate", CsvSchema.ColumnType.STRING)
                    .addColumn("sortingPriority", CsvSchema.ColumnType.STRING)
                    .build();

            CsvMapper mapper = new CsvMapper();
            MappingIterator readAll = mapper.readerFor(CsvRecord.class).
                    with(schema).readValues(input);

            List<CsvRecord> csvRecords = new ArrayList<>();
            while ((readAll).hasNext()) {
                csvRecords.add((CsvRecord) readAll.next());
            }
            try {
                csvRecordRepository.saveAll(csvRecords);
            }catch (DataIntegrityViolationException e){
                log.error("DataIntegrityViolationException on saving new csv records at time:{}, exception is :{}",System.currentTimeMillis(),e);
                throw new BusinessException("YOUR_FILE_MAY_HAVE_AN_ALREADY_PERSISTED_CSV");
            }
        }
        catch (DbException | BusinessException e){
            throw e;
        } catch (Exception e) {
            log.error("Exception on upload process at time:{}, exception is:{}", System.currentTimeMillis(), e);
            throw new BusinessException("SERVER_ERROR_ON_UPLOADING_FILE");
        }
        return messageSourceService.getMessage("SUCCESSFULLY_UPLOADED_FILE_")+" "+fileName;
    }

    @Override
    public Page<CsvRecordResponseDto> getAllRecords(Integer pageNumber, Integer pageSize) {
        return csvRecordRepository.findAll(getPageable(pageNumber,pageSize))
                .map(CsvRecordResponseDto::mapFromCsvRecord);
    }

    @Override
    public CsvRecordResponseDto getRecordByCode(String code) {
        return csvRecordRepository.findByCode(code)
                .map(CsvRecordResponseDto::mapFromCsvRecord)
                .orElseThrow(()->new BusinessException("RECORD_WITH_THIS_CODE_DOES_NOT_EXIST"));
    }

    @Transactional
    @Override
    public void deleteAllRecords() {

        try {
            csvRecordRepository.deleteAll();
        }catch (Exception e){
            log.error("Exception on deleting all data from db at time:{}, exception is{}",System.currentTimeMillis(),e);
            throw new DbException("PROBLEM_ON_DELETING_DATA");
        }

    }

    private String validateFileName(String name) {
        if (name.isEmpty() || Objects.isNull(name)) {
            throw new BusinessException("FILE_NAME_MUST_NOT_BE_NULL_OR_EMPTY");
        }

        long dotCount = name.chars().filter(ch -> ch == '.').count();
        if (dotCount > 1) {
            throw new BusinessException("FILE_NAME_MUST_CONTAIN_ONLY_ONE_DOT");
        }

        Pattern pt = Pattern.compile("[^a-zA-Z0-9/?:().,'+/-]");
        Matcher match = pt.matcher(name);
        if (!match.matches()) {
            name = name.replaceAll(pt.pattern(), "");
        }

        return name;

    }

    private void validateFileFormat(String fileFormat){
        String[] validFormats = {"csv"};
        if(!Arrays.asList(validFormats).contains(fileFormat)){
            throw new FileFormatException(messageSourceService.getMessage("FILE_FORMAT_NOT_SUPPORTED")+". Valid formats: "+ Arrays.stream(validFormats).toList());
        }
    }



    private Pageable getPageable(Integer pageNumber, Integer pageSize){

        if(Objects.isNull(pageNumber))
            pageNumber = 0;
        if(Objects.isNull(pageSize))
            pageSize = 20;

        return PageRequest.of(pageNumber,pageSize);
    }
}
