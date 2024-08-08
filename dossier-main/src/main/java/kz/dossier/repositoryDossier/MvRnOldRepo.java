package kz.dossier.repositoryDossier;

import kz.dossier.modelsDossier.MvRnOld;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MvRnOldRepo extends JpaRepository<MvRnOld, Long> {
    @Query(value= "select * from imp_rn.mv_rn where owner_iin_bin = ?1", nativeQuery = true)
    List<MvRnOld> getUsersByLike(String iin);
    @Query(value= "select count(*) from imp_rn.mv_rn where owner_iin_bin = ?1 limit 1", nativeQuery = true)
    Integer countByBin(String iin);
    @Query(value= "select * from imp_rn.mv_rn where owner_iin_bin = ?1 limit ?3 offset ?2", nativeQuery = true)
    List<MvRnOld> getUsersByLike(String iin, Integer page, Integer size);

    @Query(value= "select * from imp_rn.mv_rn where (cadastral_number = ?1 and address_rus = ?2) or (cadastral_number = ?1 and address_kaz = ?2) ORDER BY register_reg_date desc", nativeQuery = true)
    List<MvRnOld> getRowsByCadAndAddress(String cad, String address);
}