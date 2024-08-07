package kz.dossier.repositoryDossier;

import kz.dossier.modelsRisk.VerificationFiled;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VerificationFiledRepo extends JpaRepository<VerificationFiled, Long> {
    @Query(value = "select * from imp_risk.verification_filed where bin = ?1", nativeQuery = true)
    List<VerificationFiled> getByBin(String bin);

    @Query(value = "select count(*) from imp_risk.verification_filed where bin = ?1 limit 1", nativeQuery = true)
    Integer countByBin(String bin);
}
