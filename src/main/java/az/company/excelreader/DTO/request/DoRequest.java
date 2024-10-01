package az.company.excelreader.DTO.request;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class DoRequest {
    String requestNumber;
    String tin;
    String note;
}
