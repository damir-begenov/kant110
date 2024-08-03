package kz.dossier.repositoryDossier;

import kz.dossier.modelsDossier.Fno250;
import kz.dossier.modelsDossier.Fno250Avto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Fno250AvtoRepo extends JpaRepository<Fno250Avto, Long> {
    @Query(value = "select * from imp_rn.fno250_avto where iin = ?1", nativeQuery = true)
    List<Fno250Avto> findAllByIin(String iin);
}
