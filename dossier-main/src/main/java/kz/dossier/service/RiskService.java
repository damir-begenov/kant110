package kz.dossier.service;

import kz.dossier.dto.ULULMemberDTO;
import kz.dossier.modelsRisk.*;
import kz.dossier.repositoryDossier.*;
import kz.dossier.riskDto.*;
import kz.dossier.security.models.log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RiskService {
    @Autowired
    private ULService ulService;
    @Autowired
    private NdsEntityRepo ndsEntityRepo;
    @Autowired
    private MvUlRepo mvUlRepo;
    @Autowired
    private BlockEsfRepo blockEsfRepo;
    @Autowired
    private Erdr216Repo erdr216Repo;
    @Autowired
    private OmnRepo omnRepo;
    @Autowired
    private OpgRepo opgRepo;
    @Autowired
    private DormantRepo dormantRepo;
    @Autowired
    private VerificationFiledRepo verificationFiledRepo;
    @Autowired
    private FirstCreditBureauEntityRepo firstCreditBureauEntityRepo;
    @Autowired
    private EtlFigurantsRepo etlFigurantsRepo;
    @Autowired
    private AdmRepo admRepo;
    @Autowired
    private SudispolRepo sudispolRepo;

    public Double countRisksPercentage(String bin) {
        Integer count = 0;
        try {
            Integer number = ndsEntityRepo.countByBin(bin);
            if (number > 0) {
                count++; // Return 404 Not Found
            }
        } catch (Exception e) {
            log.error("ERROR: ", e);
        }
        try {
            Integer number = blockEsfRepo.countByBin(bin);
            if (number > 0) {
                count++; // Return 404 Not Found
            }
        } catch (Exception e) {
            log.error("ERROR: ", e);
        }
        try {
            Integer number = erdr216Repo.countByBin(bin);
            if (number > 0) {
                count++; // Return 404 Not Found
            }
        } catch (Exception e) {
            log.error("ERROR: ", e);
        }
        try {
            Integer number = omnRepo.countByBin(bin);
            if (number > 0) {
                count++; // Return 404 Not Found
            }
        } catch (Exception e) {
            log.error("ERROR: ", e);
        }
        try {
            Integer number = opgRepo.countByBin(bin);
            if (number > 0) {
                count++; // Return 404 Not Found
            }
        } catch (Exception e) {
            log.error("ERROR: ", e);
        }
        try {
            Integer number = dormantRepo.countByBin(bin);
            if (number > 0) {
                count++; // Return 404 Not Found
            }
        } catch (Exception e) {
            log.error("ERROR: ", e);
        }
        try {
            Integer number = verificationFiledRepo.countByBin(bin);
            if (number > 0) {
                count++; // Return 404 Not Found
            }
        } catch (Exception e) {
            log.error("ERROR: ", e);
        }
        try {
            Integer number = firstCreditBureauEntityRepo.countByBin(bin);
            if (number > 0) {
                count++; // Return 404 Not Found
            }
        } catch (Exception e) {
            log.error("ERROR: ", e);
        }
        try {
            List<RiskUlParticipantsDto> list = getRiskUlParts(bin);
            Integer number = 0;
            for (RiskUlParticipantsDto a : list) {
                number += etlFigurantsRepo.countByIin(a.getIinBin());
            }
            if (number > 0) {
                count++; // Return 404 Not Found
            }
        } catch (Exception e) {
            log.error("ERROR: ", e);
        }
        try {
            Integer number = admRepo.countByBin(bin);
            if (number > 0) {
                count++; // Return 404 Not Found
            }
        } catch (Exception e) {
            log.error("ERROR: ", e);
        }
        Double percentage = count.doubleValue() * 100 / 12; //count * 100% / totalNumber of exs;
        return percentage;
    }

    private String mergeStrings(String base, String string) {
        if (string != null && string.length() > 0) {
            base += "," + string;
        } else {
            base += ",---";
        }
        return base;
    }

    public OweULGroupPage getOweUl(String bin, Integer page) {
        if (page == null || page == 0) {
            return null;
        } else {
            page = page * 10 - 10;
        }
        OweULGroupPage fin = new OweULGroupPage();
        if (page == 1) {
            Integer pages = sudispolRepo.countByBin(bin);
            Integer res = (pages + 10 - 1) / 10;
            fin.setPages(res);
        }
        List<Sudispol> list = sudispolRepo.pageableByBin(bin, page);
        List<OweDlDto> result = new ArrayList<>();
        for (Sudispol a: list) {
            OweDlDto dto = new OweDlDto();
            dto.setNameOfTerrit(a.getName_of_organ());
            dto.setFioOfSud(a.getName_of_layer());
            dto.setNumber(a.getNumber_of_man());
            dto.setDate(a.getDate_initiation() != null ? a.getDate_initiation().substring(0, 10) : "");
            dto.setCategory(a.getCategory_requirements());
            dto.setAdditionalForCat(a.getAddition_category());
            dto.setSum(a.getAmount());
            dto.setIinBin(a.getDebtor_iin());
            String name = "";
            if (a.getDebtor_lastname() != null)
                name += a.getDebtor_lastname();
            if (a.getDebtor_firstname() != null)
                name += a.getDebtor_firstname();
            if (a.getDebtor_middlename() != null)
                name += a.getDebtor_middlename();
            dto.setFioName(name);
            String dop = "";
            dop = mergeStrings(dop, a.getOver_proceeding());
            dop = mergeStrings(dop, a.getGrounds_termination());
            dop = mergeStrings(dop, a.getReturn_doc());
            dop = mergeStrings(dop, a.getProtest());
            dop = mergeStrings(dop, a.getDate_termination());
            dop = mergeStrings(dop, a.getSuspended());
            dop = mergeStrings(dop, a.getDenied_initiation());
            dto.setAddInfo(dop);
            result.add(dto);
        }
        fin.setList(result);

        return fin;
    }
    public List<SnyatUchetNdsDto> getNds(String bin) {
        List<NdsEntity> ndss = ndsEntityRepo.getUsersByLike(bin);
        List<SnyatUchetNdsDto> result = new ArrayList<>();
        for (NdsEntity a : ndss) {
            SnyatUchetNdsDto dto = new SnyatUchetNdsDto();
            dto.setReason(a.getReason());
            dto.setName(mvUlRepo.getNameByBin(bin));
            dto.setDateOfPostanovka(String.valueOf(a.getStartDt()));
            dto.setDateOfSnyiatie(String.valueOf(a.getEndDt()));
            result.add(dto);
        }

        return result;
    }

    public List<BlockEsfDto> getBlockEsfs(String bin) {
        List<BlockEsf> list = blockEsfRepo.getblock_esfByIIN(bin);
        List<BlockEsfDto> result = new ArrayList<>();
        for (BlockEsf a : list) {
            BlockEsfDto dto = new BlockEsfDto();
            dto.setName(mvUlRepo.getNameByBin(bin));
            dto.setDateOfBlock(String.valueOf(a.getStart_dt()));
            dto.setDateOfRevert(a.getEnd_dt() != null ? String.valueOf(a.getEnd_dt()) : "");
            result.add(dto);
        }
        return result;
    }

    public List<CardDto> getCards(String bin) {
        List<Erdr216> list = erdr216Repo.findByBin(bin);
        List<CardDto> result = new ArrayList<>();
        for (Erdr216 a : list) {
            Map<String, Object> card = erdr216Repo.findByErdrNumber(a.getErdr());
            CardDto dto = new CardDto();
            dto.setNumberErdr(a.getErdr());
            if (!card.isEmpty()) {
                dto.setDateErdr(card.get("erdr_create_timestamp") != null ? String.valueOf(card.get("erdr_create_timestamp")).substring(0, 10) : "");
                dto.setQualification(card.get("qualification_ru") != null ? String.valueOf(card.get("qualification_ru")) : "");
            }
            dto.setStatusUd("");
            result.add(dto);
        }
        return result;
    }

    public List<OmnDto> getOmns(String bin) {
        List<Omn> list = omnRepo.getUsersByLikeIin_bin(bin);
        List<OmnDto> result = new ArrayList<>();
        for (Omn a : list) {
            OmnDto dto = new OmnDto();
            dto.setIin(a.getLeader_iin());
            dto.setFio(a.getLeader_fio());
            dto.setDate(a.getDecision_date());
            dto.setNumber(a.getDecision_number());
            result.add(dto);
        }
        return result;
    }

    public List<OpgDto> getOpgs(String bin) {
        List<OpgEntity> list = opgRepo.getopgByIIN(bin);
        List<OpgDto> result = new ArrayList<>();
        for (OpgEntity a : list) {
            OpgDto dto = new OpgDto();
            dto.setRegion(a.getRegion());
            dto.setOpg(a.getGroupe());
            result.add(dto);
        }
        return result;
    }

    public List<InactionDto> getInaction(String bin) {
        List<Dormant> list = dormantRepo.getUsersByLike(bin);
        List<InactionDto> result = new ArrayList<>();
        for (Dormant a : list) {
            InactionDto dto = new InactionDto();
            dto.setIin(a.getLeader_iin());
            dto.setFio(a.getLeader_fio());
            dto.setNumber(a.getOrder_number());
            dto.setDate(a.getOrder_date());
            result.add(dto);
        }
        return result;
    }

    public List<TestDto> getTest(String bin) {
        List<VerificationFiled> list = verificationFiledRepo.getByBin(bin);
        List<TestDto> result = new ArrayList<>();
        for (VerificationFiled a : list) {
            TestDto dto = new TestDto();
            dto.setRegNumber(a.getRegNumber() != null ? a.getRegNumber() : "");
            dto.setDateOfReg(a.getRegDate() != null ? String.valueOf(a.getRegDate()) : "");
            dto.setDateOfDeny(a.getRefusalDate() != null ? String.valueOf(a.getRefusalDate()) : "");
            dto.setNumberOfTest(a.getVerificationNum() != null ? a.getVerificationNum() : "");
            dto.setDateOfAction(a.getActionDate() != null ? String.valueOf(a.getActionDate()) : "");
            dto.setTypeOfTest(a.getVerificationType() != null ? a.getVerificationType() : "");
            dto.setReasonOfTest(a.getBasisOfVerification() != null ? a.getBasisOfVerification() : "");
            dto.setSrokOfTest(a.getVerificationPeriod() != null ? a.getVerificationPeriod() : "");
            dto.setSfera(a.getScopeOfControl() != null ? a.getScopeOfControl() : "");
            dto.setResult(a.getVerificationResult() != null ? a.getVerificationResult() : "");
            dto.setMeri(a.getTakenMeasures() != null ? a.getTakenMeasures() : "");
            dto.setVolume(a.getScopOfVerification() != null ? a.getScopOfVerification() : "");
            dto.setOrgan(a.getState() != null ? a.getState() : "");
            dto.setOrgan2(a.getVerificationOrgan() != null ? a.getVerificationOrgan() : "");
            dto.setMoratory(a.getMoratoriumBasis() != null ? a.getMoratoriumBasis() : "");
            result.add(dto);
        }
        return result;
    }
    public List<FirstCreditBuroDto> getFCB(String bin) {
        List<FirstCreditBureauEntity> list = firstCreditBureauEntityRepo.getUsersByLike(bin);
        List<FirstCreditBuroDto> result = new ArrayList<>();
        for (FirstCreditBureauEntity a : list) {
            FirstCreditBuroDto dto = new FirstCreditBuroDto();
            dto.setRegion(a.getRegion() != null ? a.getRegion() : "");
            dto.setAmount(a.getQuantityFpdSpd() != null ? String.valueOf(a.getQuantityFpdSpd()) : "");
            dto.setSum(a.getAmountOfDebt() != null ? String.valueOf(a.getAmountOfDebt()) : "");
            dto.setMaxDay(a.getMaxDelayDayNum1() != null ? String.valueOf(a.getMaxDelayDayNum1()) : "");
            dto.setActuality(a.getRelevanceDate() != null ? String.valueOf(a.getRelevanceDate()) : "");
            dto.setNameOfFin(a.getFinInstitutionsName() != null ? String.valueOf(a.getFinInstitutionsName()) : "");
            dto.setTotalAmount(a.getTotalCountOfCredits() != null ? String.valueOf(a.getTotalCountOfCredits()) : "");
            dto.setTotalSum(a.getTotalSumOfCredits() != null ? String.valueOf(a.getTotalSumOfCredits()) : "");
            dto.setMaxDay2(a.getMaxDelayDayNum2() != null ? String.valueOf(a.getMaxDelayDayNum2()) : "");
            result.add(dto);
        }
        return result;
    }

    public List<RiskUlParticipantsDto> getRiskUlParts(String bin) {
        List<ULULMemberDTO> list = ulService.getULMembersByBin(bin);
        List<RiskUlParticipantsDto> result = new ArrayList<>();
        for (ULULMemberDTO a : list) {
            try {
                Optional<EtlFigurants> etl = etlFigurantsRepo.getByIin(a.getBinIin());
                if (etl.isPresent()) {
                    RiskUlParticipantsDto dto = new RiskUlParticipantsDto();
                    dto.setErdr(etl.get().getErdrUdNum());
                    dto.setIinBin(a.getBinIin());
                    dto.setPosition(a.getPosition());
                    dto.setFioName(a.getName());
                    result.add(dto);
                }
            } catch (Exception e) {
                log.error("Error: ", e);
            }
        }

        return result;
    }
}
