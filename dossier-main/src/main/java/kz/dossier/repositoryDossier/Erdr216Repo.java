package kz.dossier.repositoryDossier;

import kz.dossier.modelsRisk.Erdr216;
import kz.dossier.modelsRisk.EtlErdrCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface Erdr216Repo extends JpaRepository<Erdr216, Long> {
    @Query(value = "select * from imp_risk.erdr216 where bin = ?1", nativeQuery = true)
    List<Erdr216> findByBin(String bin);

    @Query(value = "select * from etl.etl_erdr_card where erdr_ud_number = ?1 limit 1", nativeQuery = true)
    Map<String, Object> findByErdrNumber(String number);
    @Query(value = "select count(*) from imp_risk.erdr216 where bin = ?1 and reg_dt is not null", nativeQuery = true)
    Integer countByBin(String bin);
}
