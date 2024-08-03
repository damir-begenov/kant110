package kz.dossier.modelsDossier;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Date;

@Entity
@Table(name = "taxes_out", schema = "imp_tax_out")
public class MvTaxByBin {
    @Id
    private Integer amount;

    private String bin;
    private Integer government_revenue_authority_id;
    private Integer budget_classification_code_id;
    private String payment_doc_number;
    private Integer tax_payment_type_id;
    private Integer tax_posting_type_id;
    private Date debit_date;
    private Date budget_enrollment_date;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public Integer getGovernment_revenue_authority_id() {
        return government_revenue_authority_id;
    }

    public void setGovernment_revenue_authority_id(Integer government_revenue_authority_id) {
        this.government_revenue_authority_id = government_revenue_authority_id;
    }

    public Integer getBudget_classification_code_id() {
        return budget_classification_code_id;
    }

    public void setBudget_classification_code_id(Integer budget_classification_code_id) {
        this.budget_classification_code_id = budget_classification_code_id;
    }

    public String getPayment_doc_number() {
        return payment_doc_number;
    }

    public void setPayment_doc_number(String payment_doc_number) {
        this.payment_doc_number = payment_doc_number;
    }

    public Integer getTax_payment_type_id() {
        return tax_payment_type_id;
    }

    public void setTax_payment_type_id(Integer tax_payment_type_id) {
        this.tax_payment_type_id = tax_payment_type_id;
    }

    public Integer getTax_posting_type_id() {
        return tax_posting_type_id;
    }

    public void setTax_posting_type_id(Integer tax_posting_type_id) {
        this.tax_posting_type_id = tax_posting_type_id;
    }

    public Date getDebit_date() {
        return debit_date;
    }

    public void setDebit_date(Date debit_date) {
        this.debit_date = debit_date;
    }

    public Date getBudget_enrollment_date() {
        return budget_enrollment_date;
    }

    public void setBudget_enrollment_date(Date budget_enrollment_date) {
        this.budget_enrollment_date = budget_enrollment_date;
    }
}
