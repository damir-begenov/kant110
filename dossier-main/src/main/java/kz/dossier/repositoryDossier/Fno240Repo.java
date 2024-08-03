package kz.dossier.repositoryDossier;

import kz.dossier.modelsDossier.DubaiRn;
import kz.dossier.modelsDossier.Fno240;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Fno240Repo extends JpaRepository<Fno240, Long> {
    @Query(value = "select * from imp_rn.fno240 where iin = ?1", nativeQuery = true)
    List<Fno240> findAllByIin(String iin);
}
