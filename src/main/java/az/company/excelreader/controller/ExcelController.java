package az.company.excelreader.controller;

import az.company.excelreader.DTO.request.ExcelProcessRequest;
import az.company.excelreader.DTO.response.ExcelProcessResponse;
import az.company.excelreader.service.ExcelProcessService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ExcelController {

    ExcelProcessService excelProcessService;

    @PostMapping("/process")
    public ResponseEntity<ExcelProcessResponse> processExcel(@RequestBody ExcelProcessRequest request) {
        ExcelProcessResponse response = excelProcessService.processExcelFile(request);
        return ResponseEntity.ok(response);
    }

}
