package az.company.excelreader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ExcelReaderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExcelReaderApplication.class, args);
    }
}
