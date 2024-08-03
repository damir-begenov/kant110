package kz.dossier.repositoryDossier;

import kz.dossier.modelsDossier.DubaiRn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DubaiRnRepo extends JpaRepository<DubaiRn, Long> {
    @Query(value = "select * from imp_rn.dubai_rn where iin = ?1 order by date_seller desc", nativeQuery = true)
    List<DubaiRn> findAllByIin(String iin);

}
