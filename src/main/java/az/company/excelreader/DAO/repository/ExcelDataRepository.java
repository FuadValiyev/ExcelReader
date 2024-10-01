package az.company.excelreader.DAO.repository;

import az.company.excelreader.DAO.entity.ExcelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExcelDataRepository extends JpaRepository<ExcelEntity, Long> {

    List<ExcelEntity> findByState(int state);

    boolean existsByTin(String tin);
}
