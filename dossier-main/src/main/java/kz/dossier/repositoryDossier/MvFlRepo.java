package kz.dossier.repositoryDossier;

import kz.dossier.modelsDossier.MvFl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MvFlRepo extends JpaRepository<MvFl, Long> {
    @Query(value= "select * from imp_kfm_fl.mv_fl_iin_2 mv_fl0_ where mv_fl0_.iin = ?1 ", nativeQuery = true)
    List<MvFl> getUsersByLike(String iin);
    @Query(nativeQuery = true, value = "SELECT EXISTS(SELECT 1 FROM imp_kfm_fl.mv_fl_iin_2 WHERE iin = ?1 AND gender = 1)")

    Boolean getGender(String iin);


    @Query(value= "select * from imp_kfm_fl.mv_fl_iin_2 mv_fl0_ where mv_fl0_.iin = ?1 limit 1", nativeQuery = true)
    MvFl getUserByIin(String iin);

    @Query(value= "select * from imp_kfm_fl.mv_fl_iin_2 mv_fl0_ where mv_fl0_.iin = ?1 limit 1", nativeQuery = true)
    Optional<MvFl> getByIin(String iin);

    @Query(value= "select * from imp_kfm_fl.mv_fl_iin_2 mv_fl0_ where first_name like ?1 and  patronymic like ?2 and last_name like ?3", nativeQuery = true)
    List<MvFl> getUsersByFIO(String name, String patronimic, String last);
}
