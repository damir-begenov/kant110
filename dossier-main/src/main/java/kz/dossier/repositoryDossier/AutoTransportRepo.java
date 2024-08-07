package kz.dossier.repositoryDossier;

import kz.dossier.modelsDossier.AutoTransport;
import kz.dossier.modelsDossier.BailiffListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AutoTransportRepo extends JpaRepository<AutoTransport, Long> {
    @Query(value = "select * from imp_kfm_fl.auto_transport where iin_bin = ?1 limit 100", nativeQuery = true)
    List<AutoTransport> getAutoByIin(String iin);

    @Query(value = "select count(*) from imp_kfm_fl.auto_transport where iin_bin = ?1 limit 1", nativeQuery = true)
    Integer countByBin(String iin);
}
