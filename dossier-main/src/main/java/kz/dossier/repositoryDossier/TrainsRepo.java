package kz.dossier.repositoryDossier;

import kz.dossier.modelsDossier.Trains;
import kz.dossier.modelsDossier.Universities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrainsRepo extends JpaRepository<Trains, Long> {
    @Query(value = "select * from imp_kfm_fl.trains where iin_bin = ?1", nativeQuery = true)
    List<Trains> getByIIN(String iin);

    @Query(value = "select count(*) from imp_kfm_fl.trains where iin_bin = ?1 limit 1", nativeQuery = true)
    Integer countByBin(String iin);
}
