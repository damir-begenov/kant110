package kz.dossier.repositoryDossier;

import kz.dossier.modelsRisk.Pdl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PdlRepo extends JpaRepository<Pdl, String> {
    @Query(value = "select * FROM imp_kfm_fl.pdl where iin = ?1", nativeQuery = true)
    List<Pdl> getByIIN(String iin);
    @Query(value = "select * FROM imp_kfm_fl.pdl where bin = ?1", nativeQuery = true)
    List<Pdl> getByBin(String iin);

    @Query(value = "select count(*) FROM imp_kfm_fl.pdl where bin = ?1 limit 1", nativeQuery = true)
    Integer countByBin(String iin);
}
