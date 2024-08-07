package kz.dossier.repositoryDossier;


import kz.dossier.modelsRisk.FirstCreditBureauEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FirstCreditBureauEntityRepo extends JpaRepository<FirstCreditBureauEntity, Long> {
    @Query(value = "select * from imp_kfm_fl.first_credit_bureau  where iin = ?1", nativeQuery = true)
    List<FirstCreditBureauEntity> getUsersByLike(String iin);
    @Query(value = "select count(*) from imp_kfm_fl.first_credit_bureau  where iin = ?1 limit 1", nativeQuery = true)
    Integer countByBin(String iin);
}
