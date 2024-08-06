package kz.dossier.repositoryDossier;

import kz.dossier.modelsDossier.Goszakup;
import kz.dossier.modelsDossier.Samruk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoszkupRepo extends JpaRepository<Goszakup, Long> {
    @Query(name = "select * from imp_kfm_db.gos_zakup where supplier_bin = ?1", nativeQuery = true)
    List<Goszakup> getBySupplierBin(String bin);

    @Query(name = "select * from imp_kfm_db.gos_zakup where customer_bin = ?1", nativeQuery = true)
    List<Goszakup> getByCustomerBin(String bin);

    @Query(value = "select * from imp_kfm_db.gos_zakup where extract(year from sign_date) = ?2 and supplier_bin = ?1 limit 10 offset ?3", nativeQuery = true)
    List<Goszakup> getBySupplierBinAndYear(String bin, Integer year, Integer page);

    @Query(value = "select * from imp_kfm_db.gos_zakup where extract(year from sign_date) = ?2 and customer_bin = ?1 limit 10 offset ?3", nativeQuery = true)
    List<Goszakup> getByCustomerBinAndYear(String bin, Integer year, Integer page);

}
