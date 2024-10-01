package az.company.excelreader.service;

import az.company.excelreader.DAO.entity.ExcelEntity;
import az.company.excelreader.DAO.repository.ExcelDataRepository;
import az.company.excelreader.DTO.request.ExcelProcessRequest;
import az.company.excelreader.DTO.response.ExcelProcessResponse;
import az.company.excelreader.utils.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

import static lombok.AccessLevel.PRIVATE;
import static org.apache.logging.log4j.util.Strings.isBlank;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ExcelProcessService {

    ExcelDataRepository excelDataRepository;
    ObjectMapper objectMapper = new ObjectMapper();

    public ExcelProcessResponse processExcelFile(ExcelProcessRequest request) {

        if (isBlank(request.getFileName())) {
            return new ExcelProcessResponse(Message.INVALID_REQUEST_BODY.name());
        }
        File excelFile = new File("D:/Projects/ExcelFolder/" + request.getFileName() + ".xlsx");

        if (!excelFile.exists()) {
            return new ExcelProcessResponse(Message.FILE_NOT_FOUND.name());
        }

        try {
            FileInputStream inputStream = new FileInputStream(excelFile);

            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            Row headerRow = sheet.getRow(0);
            if (headerRow == null || headerRow.getPhysicalNumberOfCells() != 2
                    || !"TIN".equalsIgnoreCase(headerRow.getCell(0).getStringCellValue())
                    || !"NOTE".equalsIgnoreCase(headerRow.getCell(1).getStringCellValue())) {
                workbook.close();
                return new ExcelProcessResponse(Message.EXCEL_HEADER_INVALID.name());
            }

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                String tin = getCellValueAsString(row.getCell(0));
                String note = getCellValueAsString(row.getCell(1));

                boolean matches = tin.matches("\\d+");
                if (!matches) {
                    return new ExcelProcessResponse(Message.FILE_FORMAT_ERROR.name());
                }

                boolean exists = excelDataRepository.existsByTin(tin);
                if (!exists) {
                    ExcelEntity excelData = new ExcelEntity();
                    excelData.setTin(tin);
                    excelData.setNote(note);
                    excelData.setCreateDate(new Date());
                    excelData.setFileId(request.getFileName());
                    excelDataRepository.save(excelData);
                } else {
                    log.warn("TIN already exists: {}", tin);
                }
            }
            workbook.close();
            return new ExcelProcessResponse(Message.SUCCESS.name());

        } catch (Exception e) {
            log.error("Error occurred while processing the Excel file: {}", e.getMessage(), e);
            return new ExcelProcessResponse(Message.FILE_READ_ERROR.name());
        }
    }

    private String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((int) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}