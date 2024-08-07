package kz.dossier.repositoryDossier;

import kz.dossier.modelsRisk.NdsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NdsEntityRepo extends JpaRepository<NdsEntity, Long> {
    @Query(value = "select * from imp_risk.nds where iin_bin = ?1 ", nativeQuery = true)
    List<NdsEntity> getUsersByLike(String iin);
    @Query(value = "select count(*) from imp_risk.nds where iin_bin = ?1 ", nativeQuery = true)
    Integer countByBin(String iin);
}