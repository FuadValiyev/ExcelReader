package az.company.excelreader.service;

import az.company.excelreader.DAO.entity.ExcelEntity;
import az.company.excelreader.DAO.repository.ExcelDataRepository;
import az.company.excelreader.DTO.request.DoRequest;
import az.company.excelreader.DTO.response.DoResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class JobBsn {

    ExcelDataRepository repository;

    @Scheduled(fixedRate = 60000)
    public void executeTask() {
        List<ExcelEntity> dataList = repository.findByState(0);
        if (!dataList.isEmpty()) {

            ObjectMapper mapper = new ObjectMapper();

            for (ExcelEntity data : dataList) {
                DoRequest request = new DoRequest();
                request.setRequestNumber(UUID.randomUUID().toString());
                request.setTin(data.getTin());
                request.setNote(data.getNote());

                var doResponse = doProcess(request);
                try {
                    JsonNode outputJob = mapper.valueToTree(doResponse.getData());
                    data.setOutputJob(outputJob);
                    data.setState(1);
                    data.setStatus("U");
                    data.setUpdateDate(new Date());
                    repository.save(data);
                    log.info("Successfully processed TIN: {}", data.getTin());
                } catch (Exception e) {
                    log.error("Error occurred while saving data: {}", e.getMessage(), e);
                }
            }
        }
    }



    public DoResponse doProcess(DoRequest request) {
        String str = "Process Success";
        DoResponse response = new DoResponse();
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode dataNode = objectMapper.createObjectNode().put("message", str);

        response.setData(dataNode);
        return response;
    }

}
