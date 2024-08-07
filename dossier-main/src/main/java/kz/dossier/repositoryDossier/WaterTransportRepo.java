package kz.dossier.repositoryDossier;

import kz.dossier.modelsDossier.AviaTransport;
import kz.dossier.modelsDossier.WaterTransport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaterTransportRepo extends JpaRepository<WaterTransport, Long> {
    @Query(value = "select * from imp_kfm_fl.water_transport where iin_bin = ?1", nativeQuery = true)
    List<WaterTransport> getWaterByIin(String iin);
    @Query(value = "select count(*) from imp_kfm_fl.water_transport where iin_bin = ?1 limit 1", nativeQuery = true)
    Integer countByBin(String iin);
}
