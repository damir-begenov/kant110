package kz.dossier.repositoryDossier;

import kz.dossier.modelsRisk.Sudispol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SudispolRepo extends JpaRepository<Sudispol, Long> {
    @Query(value = "select * from imp_risk.sudispol where debtor_iin = ?1", nativeQuery = true)
    List<Sudispol> findAllbyIin(String iin);

    @Query(value = "select * from imp_risk.sudispol where debtor_bin = ?1 limit 10 offset ?2", nativeQuery = true)
    List<Sudispol> pageableByBin(String iin, Integer page);

    @Query(value = "select count(*) from imp_risk.sudispol where debtor_bin = ?1", nativeQuery = true)
    Integer countByBin(String iin);
}
