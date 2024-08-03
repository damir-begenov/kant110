package kz.dossier.repositoryDossier;

import kz.dossier.modelsDossier.Fno250Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Fno250CompanyRepo extends JpaRepository<Fno250Company, Long> {
    @Query(value = "select * from imp_rn.fno250_company where iin = ?1", nativeQuery = true)
    List<Fno250Company> findAllByIin(String iin);
}
