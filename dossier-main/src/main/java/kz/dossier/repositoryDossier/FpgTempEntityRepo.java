package kz.dossier.repositoryDossier;


import kz.dossier.modelsDossier.FpgTempEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FpgTempEntityRepo extends JpaRepository<FpgTempEntity, Long> {
    @Query(value= "select * from imp_kfm_fl.fpg_temp where bin = ?1 ", nativeQuery = true)
    List<FpgTempEntity> getUsersByLike(String iin);

    @Query(value= "select count(*) from imp_kfm_fl.fpg_temp where bin = ?1 limit 1", nativeQuery = true)
    Integer countByBin(String iin);
}