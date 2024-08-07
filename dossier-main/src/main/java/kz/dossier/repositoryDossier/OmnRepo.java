package kz.dossier.repositoryDossier;

import kz.dossier.modelsRisk.Omn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OmnRepo extends JpaRepository<Omn, Long> {
    @Query(value= "select * from imp_risk.omn omn0_ where omn0_.leader_iin = ?1 ", nativeQuery = true)
    List<Omn> getUsersByLike(String iin);
    @Query(value= "select * from imp_risk.omn omn0_ where omn0_.iin_bin = ?1 ", nativeQuery = true)
    List<Omn> getUsersByLikeIin_bin(String BIN);
    @Query(value= "select count(*) from imp_risk.omn omn0_ where omn0_.iin_bin = ?1 ", nativeQuery = true)
    Integer countByBin(String BIN);
    @Query(value= "select * from imp_risk.omn omn0_ where omn0_.iin_bin = ?1 ", nativeQuery = true)
    Omn getUsersByLikeIin_bins(String BIN);
}
