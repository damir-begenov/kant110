package kz.dossier.service;

import kz.dossier.dto.MvTaxDto;
import kz.dossier.modelsDossier.MvTaxByBin;
import kz.dossier.repositoryDossier.MvTaxByBinRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaxService {
    @Autowired
    private MvTaxByBinRepo mvTaxByBinRepo;

    public List<MvTaxDto> getTaxes(String bin) {
        List<MvTaxByBin> list = mvTaxByBinRepo.findAllByBin(bin);
        List<MvTaxDto> result = new ArrayList<>();

        for (MvTaxByBin mvTaxByBin : list) {
            if (mvTaxByBin  == null) {
                continue;
            }
            MvTaxDto dto = new MvTaxDto();
            try {
                dto.setCodeOfBudgetClass(mvTaxByBinRepo.findBudgetClassCode(mvTaxByBin.getBudget_classification_code_id()));
            } catch (Exception e) {
                dto.setCodeOfBudgetClass("");
            }
            try {
                dto.setKbk(mvTaxByBinRepo.findBudgetClassById(mvTaxByBin.getBudget_classification_code_id()));
            } catch (Exception e) {
                dto.setKbk("");
            }
            dto.setBudgetEnrollmentDate(String.valueOf(mvTaxByBin.getBudget_enrollment_date()));
            dto.setSum(String.valueOf(mvTaxByBin.getAmount()));
            dto.setNumberOfPaymentDoc(String.valueOf(mvTaxByBin.getPayment_doc_number()));
            try {
                dto.setTypeOfPayment(mvTaxByBinRepo.findTaxPaymentType(mvTaxByBin.getTax_payment_type_id()));
            } catch (Exception e) {
                dto.setTypeOfPayment("");
            }
            try {
                dto.setTypeOfProvodka(mvTaxByBinRepo.findTaxPostingType(mvTaxByBin.getTax_posting_type_id()));
            } catch (Exception e) {
                dto.setTypeOfProvodka("");
            }
            dto.setDebitDate(String.valueOf(mvTaxByBin.getDebit_date()));
            try {
                dto.setOgdCode(mvTaxByBinRepo.findOGDCode(mvTaxByBin.getGovernment_revenue_authority_id()));
            } catch (Exception e) {
                dto.setOgdCode("");
            }
            try {
                dto.setOgdName(mvTaxByBinRepo.findOGDName(mvTaxByBin.getGovernment_revenue_authority_id()));
            } catch (Exception e) {
                dto.setOgdName("");
            }
            dto.setKnn("");

            result.add(dto);
        }

        return result;
    }

}
