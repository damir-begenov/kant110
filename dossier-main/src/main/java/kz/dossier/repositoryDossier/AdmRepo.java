package kz.dossier.repositoryDossier;

import kz.dossier.modelsRisk.Adm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdmRepo extends JpaRepository<Adm, Long> {
    @Query(value= "select * from imp_kfm_fl.adm where iin = ?1 ", nativeQuery = true)
    List<Adm> getUsersByLike(String iin);
    @Query(value= "select * from imp_kfm_fl.adm  where bin = ?1 ", nativeQuery = true)
    List<Adm> getUsersByLikeBin(String iin);

    @Query(value = "select name_ru from dictionary.d_adm_qualification where code = ?1", nativeQuery = true)
    String findQualificationByCode(String code);

}
