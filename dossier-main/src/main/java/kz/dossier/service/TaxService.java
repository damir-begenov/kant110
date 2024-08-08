package kz.dossier.service;

import kz.dossier.dto.MvTaxDto;
import kz.dossier.dto.TaxViewDto;
import kz.dossier.modelsDossier.MvTaxByBin;
import kz.dossier.repositoryDossier.MvTaxByBinRepo;
import kz.dossier.tools.KbkGroupingForTaxes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaxService {
    @Autowired
    private MvTaxByBinRepo mvTaxByBinRepo;
    KbkGroupingForTaxes kbkGroupingForTaxes = new KbkGroupingForTaxes();

    public List<TaxViewDto> getTaxView(String bin) {
        List<Map<String, Object>> preProductionTaxes = mvTaxByBinRepo.getTaxViewPreMode(bin);


        Map<Integer, Map<Integer, BigDecimal>> groupedData = preProductionTaxes.stream()
                .collect(Collectors.groupingBy(
                        record -> (Integer) record.get("year"),
                        Collectors.groupingBy(
                                record -> kbkGroupingForTaxes.getGroupOfKbk((String) record.get("code")),
                                Collectors.reducing(
                                        BigDecimal.ZERO,
                                        record -> (BigDecimal) record.get("totalSum"),
                                        BigDecimal::add
                                )
                        )
                ));

        BigDecimal totalTotal = BigDecimal.ZERO;
        BigDecimal totalByEmployees = BigDecimal.ZERO;
        BigDecimal totalByOwning = BigDecimal.ZERO;
        BigDecimal totalByImport = BigDecimal.ZERO;

        List<TaxViewDto> taxViewList = groupedData.entrySet().stream()
                .map(entry -> {
                    String year = String.valueOf(entry.getKey());
                    Map<Integer, BigDecimal> groupSums = entry.getValue();

                    BigDecimal byEmployees = groupSums.getOrDefault(0, BigDecimal.ZERO);
                    BigDecimal byOwning = groupSums.getOrDefault(1, BigDecimal.ZERO);
                    BigDecimal byImport = groupSums.getOrDefault(2, BigDecimal.ZERO);
                    BigDecimal total = byEmployees.add(byOwning).add(byImport);


                    return new TaxViewDto(year, total, byEmployees, byOwning, byImport);
                })
                .sorted(Comparator.comparing(TaxViewDto::getYear))
                .collect(Collectors.toList());

        for (TaxViewDto a : taxViewList) {
            totalByImport = totalByImport.add(a.getByImport());
            totalByEmployees = totalByEmployees.add(a.getByEmployees());
            totalByOwning = totalByOwning.add(a.getByOwning());
            totalTotal = totalTotal.add(a.getTotalSum());
        }

        TaxViewDto total = new TaxViewDto("Общая сумма", totalTotal, totalByEmployees, totalByOwning, totalByImport);
        taxViewList.add(total);
        return taxViewList;
    }

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

    public Integer countTaxes(String bin) {
        try {
            Integer number = mvTaxByBinRepo.countByBin(bin);

            return number;
        } catch (Exception e) {
            return 0;
        }
    }

    public Integer getNumberOfTaxPages(String bin, Integer year) {
        try {
            Integer number = mvTaxByBinRepo.countByYearAndBin(bin, year);

            return number;
        } catch (Exception e) {
            return 0;
        }
    }

    public List<MvTaxDto> getTaxesWithPages(String bin, Integer year, Integer page, Integer size) {
        if (page == null || page == 0) {
            return null;
        } else {
            page = page * size - size;
        }

        List<MvTaxByBin> list = mvTaxByBinRepo.findAllByBin(bin, year, page, size);
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
