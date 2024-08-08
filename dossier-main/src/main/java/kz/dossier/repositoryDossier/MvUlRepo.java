package kz.dossier.repositoryDossier;

import kz.dossier.modelsDossier.MvUl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository

public interface MvUlRepo extends JpaRepository<MvUl, Long> {
    @Query(value= "select * from imp_kfm_ul.mv_ul where mv_ul.bin = ?1 limit 1 ", nativeQuery = true)
    List<MvUl> getUsersByLike(String iin);

    @Query(value= "select count(*) from imp_kfm_ul.mv_ul where mv_ul.bin = ?1 limit 1 ", nativeQuery = true)
    Integer countByBin(String iin);
    @Query(value= "select DISTINCT ON (bin) * from imp_kfm_ul.mv_ul where UPPER (full_name_rus) like ?1 or UPPER (full_name_kaz) like ?1", nativeQuery = true)
    List<MvUl> getUlsByName(String name);
    @Query(value="select short_name from imp_kfm_ul.mv_ul where bin = ?1 limit 1", nativeQuery = true)
    String getNameByBin(String bin);
    @Query(value= "select * from imp_kfm_ul.mv_ul where mv_ul.bin = ?1 limit 1 ", nativeQuery = true)
    Optional<MvUl> getUlByBin(String bin);

    @Query(value= "SELECT *\n" +
            "FROM imp_kfm_ul.mv_ul\n" +
            "where UPPER(registration_number) like upper(?2) and (UPPER(full_name_rus) like upper(?1) or UPPER(short_name) like upper(?1)) limit 5 offset ?3", nativeQuery = true)
    List<MvUl> getUlByNameAndRegNumber(String bin, String regNumber, Integer offset);
    
}