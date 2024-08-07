package kz.dossier.repositoryDossier;

import kz.dossier.modelsDossier.MvUlFounderFl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MvUlFounderFlRepo extends JpaRepository<MvUlFounderFl, Long> {
    @Query(value= "select * from imp_kfm_ul.ul_founder_fl where bin_org = ?1 ", nativeQuery = true)
    List<MvUlFounderFl> getUsersByLike(String iin);

    @Query(value= "select count(*) from imp_kfm_ul.ul_founder_fl where bin_org = ?1 limit 1", nativeQuery = true)
    Integer countByBin(String iin);
    @Query(value= "select * from imp_kfm_ul.ul_founder_fl where iin = ?1", nativeQuery = true)
    List<MvUlFounderFl> getUsersByLikeIIN(String iin);
//    @Query(value= "select * from initial_data.ul_founder_fl where iin = ?1 ", nativeQuery = true)
//    List<MvUlFounderFl> getByFounder(String iin);
}