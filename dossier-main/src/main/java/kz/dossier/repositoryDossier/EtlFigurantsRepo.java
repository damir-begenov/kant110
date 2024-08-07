package kz.dossier.repositoryDossier;

import kz.dossier.modelsRisk.EtlFigurants;
import org.apache.bcel.generic.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EtlFigurantsRepo extends JpaRepository<EtlFigurants, Long> {
    @Query(value = "select * from etl.etl_figurants where iin = ?1 limit 1", nativeQuery = true)
    Optional<EtlFigurants> getByIin(String iin);
    @Query(value = "select count(*) from etl.etl_figurants where iin = ?1 limit 1", nativeQuery = true)
    Integer countByIin(String iin);
}
