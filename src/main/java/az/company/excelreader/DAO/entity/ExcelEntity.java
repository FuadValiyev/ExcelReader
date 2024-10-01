package az.company.excelreader.DAO.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;

import java.util.Date;

import static lombok.AccessLevel.PRIVATE;
import static org.hibernate.type.SqlTypes.JSON;

@Data
@Entity
@FieldDefaults(level = PRIVATE)
@Table(name = "excel_table", schema = "excel")
public class ExcelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "file_id")
    String fileId;

    @Column(name = "tin")
    String tin;

    @Column(name = "note")
    String note;

    @JdbcTypeCode(JSON)
    @Column(name = "output_job", columnDefinition = "jsonb")
    JsonNode outputJob;

    @Column(name = "create_date")
    Date createDate;

    @Column(name = "update_date")
    Date updateDate;

    @Column(name = "status")
    String status = "I";

    @Column(name = "state", columnDefinition = "integer default 0")
    int state;
}