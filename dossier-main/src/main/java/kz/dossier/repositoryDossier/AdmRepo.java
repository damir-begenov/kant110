//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package kz.dossier.repositoryDossier;

import java.util.List;
import kz.dossier.modelsRisk.Adm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdmRepo extends JpaRepository<Adm, Long> {
    @Query(
            value = "select a.*, CONCAT(q.short_name_qq, ' ' , q.name_ru) as qualification_name  from imp_kfm_fl.adm as a inner join dictionary.d_adm_qualification as q on q.code = a.qualification where iin = ?1 ",
            nativeQuery = true
    )
    List<Adm> getUsersByLike(String iin);

    @Query(
            value = "select * from imp_kfm_fl.adm  where bin = ?1 ",
            nativeQuery = true
    )
    List<Adm> getUsersByLikeBin(String iin);
    @Query(value = "select name_ru from dictionary.d_adm_qualification where code = ?1", nativeQuery = true)
    String findQualificationByCode(String code);

}
