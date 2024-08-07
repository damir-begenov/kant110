package kz.dossier.repositoryDossier;

import kz.dossier.modelsDossier.AccountantListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountantListEntityRepo extends JpaRepository<AccountantListEntity, Long> {
    @Query(value = "select * from imp_notary.accountant_list  where iin = ?1", nativeQuery = true)
    List<AccountantListEntity> getUsersByLike(String iin);
    @Query(value = "select * from imp_notary.accountant_list  where bin = ?1", nativeQuery = true)
    List<AccountantListEntity> getUsersByLikeBIN(String iin);

    @Query(value = "select count(*) from imp_notary.accountant_list  where bin = ?1 limit 1", nativeQuery = true)
    Integer countByBin(String iin);
}
