package kz.dossier.repositoryDossier;


import kz.dossier.modelsDossier.CommodityProducer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CommodityProducerRepo extends JpaRepository<CommodityProducer, Long> {
    @Query(value= "select * from imp_risk.commodity_producers where iin_bin = ?1", nativeQuery = true)
    List<CommodityProducer> getiin_binByIIN(String IIN);

    @Query(value= "select count(*) from imp_risk.commodity_producers where iin_bin = ?1 limit 1", nativeQuery = true)
    Integer countByBin(String IIN);
}