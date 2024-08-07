package kz.dossier.repositoryDossier;

import kz.dossier.modelsDossier.AutoTransport;
import kz.dossier.modelsDossier.AviaTransport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AviaTransportRepo extends JpaRepository<AviaTransport, Long> {
    @Query(value = "select * from imp_kfm_fl.avia_transport where iin = ?1", nativeQuery = true)
    List<AviaTransport> getAviaByIin(String iin);

    @Query(value = "select count(*) from imp_kfm_fl.avia_transport where iin = ?1 limit 1", nativeQuery = true)
    Integer countByBin(String iin);
}
