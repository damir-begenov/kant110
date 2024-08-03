package kz.dossier.repositoryDossier;

import kz.dossier.modelsDossier.Fno250Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Fno250DepositRepo extends JpaRepository<Fno250Deposit, Long> {
    @Query(value = "select * from imp_rn.fno250_deposit where iin = ?1", nativeQuery = true)
    List<Fno250Deposit> findAllByIin(String iin);
}
