package kz.dossier.repositoryDossier;

import kz.dossier.modelsDossier.MvAutoFl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MvAutoFlRepo extends JpaRepository<MvAutoFl, Long> {
    @Query(value= "select * from imp_kfm_db.mv_auto_fl mv_auto_fl0_ where mv_auto_fl0_.iin = ?1 order by is_registered desc", nativeQuery = true)
    List<MvAutoFl> getUsersByLike(String iin);

    @Query(value= "select * from imp_kfm_db.mv_auto_fl where vin_kuzov_shassi = ?1", nativeQuery = true)
    List<MvAutoFl> findBYVIN(String Vin_kuzov_shassi);
    @Query(value = "SELECT iin from imp_kfm_db.mv_auto_fl where vin_kuzov_shassi = ?1 and iin != ''", nativeQuery = true)
    List<String> getByVin(String vin);
}
