package kz.dossier.repositoryDossier;

import kz.dossier.modelsDossier.MvTaxByBin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MvTaxByBinRepo extends JpaRepository<MvTaxByBin, Integer> {
    @Query(value = "select * from imp_tax_out.taxes_out where bin = ?1", nativeQuery = true)
    List<MvTaxByBin> findAllByBin(String bin);


    @Query(value = "select name_rus from imp_tax_out.d_budget_classification_code where id = ?1", nativeQuery = true)
    String findBudgetClassById(Integer id);
    @Query(value = "select code from imp_tax_out.d_budget_classification_code where id = ?1", nativeQuery = true)
    String findBudgetClassCode(Integer id);
    @Query(value = "select name_rus from imp_tax_out.d_government_revenue_authority where id = ?1", nativeQuery = true)
    String findOGDName(Integer id);
    @Query(value = "select code from imp_tax_out.d_government_revenue_authority where id = ?1", nativeQuery = true)
    String findOGDCode(Integer id);
    @Query(value = "select name_rus from imp_tax_out.d_tax_payment_type where id = ?1", nativeQuery = true)
    String findTaxPaymentType(Integer id);
    @Query(value = "select name_rus from imp_tax_out.d_tax_posting_type where id = ?1", nativeQuery = true)
    String findTaxPostingType(Integer id);

}
