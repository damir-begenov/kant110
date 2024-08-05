package kz.dossier.repositoryDossier;

import kz.dossier.modelsDossier.MvTaxByBin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MvTaxByBinRepo extends JpaRepository<MvTaxByBin, Integer> {
    @Query(value = "select * from imp_tax_out.taxes_out where bin = ?1", nativeQuery = true)
    List<MvTaxByBin> findAllByBin(String bin);

    @Query(value = "select * from imp_tax_out.taxes_out where bin = ?1 order by budget_enrollment_date desc limit 20 offset ?2", nativeQuery = true)
    List<MvTaxByBin> findAllByBin(String bin, Integer page);

    @Query(value = "select tx.year, bc.code,  sum(tx.amount) as totalSum\n" +
            "\tfrom imp_tax_out.taxes_out tx\n" +
            "\tinner join imp_tax_out.d_budget_classification_code bc \n" +
            "\t\t\ton tx.budget_classification_code_id = bc.id\n" +
            "\twhere bin = ?1 \n" +
            "\tgroup by year, bc.code\n" +
            "\torder by tx.year asc", nativeQuery = true)
    List<Map<String, Object>> getTaxViewPreMode(String bin);

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
