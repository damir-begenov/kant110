package kz.dossier.repositoryDossier;

import kz.dossier.modelsDossier.Fno250;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Fno250Repo extends JpaRepository<Fno250, Long> {
    @Query(value = "select * from imp_rn.fno250 where iin = ?1", nativeQuery = true)
    List<Fno250> findAllByIin(String iin);
}
