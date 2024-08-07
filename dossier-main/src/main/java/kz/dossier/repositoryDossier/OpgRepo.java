package kz.dossier.repositoryDossier;

import kz.dossier.modelsRisk.OpgEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OpgRepo extends JpaRepository<OpgEntity, Long> {
    @Query(value= "select * from imp_risk.opg where bin = ?1", nativeQuery = true)
    List<OpgEntity> getopgByIIN(String IIN);
    @Query(value= "select count(*) from imp_risk.opg where bin = ?1 limit 1", nativeQuery = true)
    Integer countByBin(String IIN);
}