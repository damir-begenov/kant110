//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package kz.dossier.repositoryDossier;

import java.util.List;
import java.util.Optional;
import kz.dossier.modelsDossier.MvFl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MvFlRepo extends JpaRepository<MvFl, Long> {
    @Query(
            value = "SELECT   mv_fl0_.* , \n               nat.\"RU_NAME\" AS nationality_ru_name\n \n               FROM  imp_kfm_fl.mv_fl mv_fl0_\n            INNER JOIN \n                dictionary.d_nationality_new as nat ON nat.\"ID\"::text = mv_fl0_.nationality_id \n            WHERE \n                mv_fl0_.iin = ?1",
            nativeQuery = true
    )
    List<MvFl> getUsersByLike(String iin);

    @Query(
            nativeQuery = true,
            value = "SELECT EXISTS(SELECT 1 FROM imp_kfm_fl.mv_fl WHERE iin = ?1 AND gender = 1)"
    )
    Boolean getGender(String iin);

    @Query(
            value = "select * from imp_kfm_fl.mv_fl mv_fl0_ where mv_fl0_.iin = ?1 limit 1",
            nativeQuery = true
    )
    MvFl getUserByIin(String iin);

    @Query(
            value = "select * from imp_kfm_fl.mv_fl mv_fl0_ where mv_fl0_.iin = ?1 limit 1",
            nativeQuery = true
    )
    Optional<MvFl> getByIin(String iin);

    @Query(
            value = "SELECT   mv_fl0_.* , \n               nat.\"RU_NAME\" AS nationality_ru_name\n \n from imp_kfm_fl.mv_fl mv_fl0_  INNER JOIN \n                dictionary.d_nationality_new as nat ON nat.\"ID\"::text = mv_fl0_.nationality_id where mv_fl0_.first_name like ?1 and  mv_fl0_.patronymic like ?2 and mv_fl0_.last_name like ?3",
            nativeQuery = true
    )
    List<MvFl> getUsersByFIO(String name, String patronimic, String last);
    @Query(value = "SELECT CONCAT(last_name, ' ', first_name, ' ', patronymic) FROM imp_kfm_fl.mv_fl where iin = ?1", nativeQuery = true)
    String getNameByIIN(String iin);
}
