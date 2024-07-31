package kz.dossier.service;

import kz.dossier.dto.FLRiskDto;
import kz.dossier.repositoryDossier.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class FlRiskServiceImpl {

    @Autowired
    private AdmRepo admRepo;
    @Autowired
    private OrphansRepo orphans_repo;
    @Autowired
    private OmnRepo omn_repo;
    @Autowired
    private BankrotRepo bankrotRepo;
    @Autowired
    private BlockEsfRepo block_esfRepo;
    @Autowired
    private ConvictsJustifiedRepo convicts_justifiedRepo;
    @Autowired
    private ConvictsTerminatedtByRehabRepo convicts_terminated_by_rehabRepo;
    @Autowired
    private CriminalsRepo criminalsRepo;
    @Autowired
    private DismissalRepo dismissalRepo;
    @Autowired
    private FirstCreditBureauEntityRepo firstCreditBureauEntityRepo;
    @Autowired
    private FpgTempEntityRepo fpgTempEntityRepo;
    @Autowired
    private MzEntityRepo mzEntityRepo;
    @Autowired
    private NdsEntityRepo ndsEntityRepo;
    @Autowired
    private OpgRepo opgRepo;
    @Autowired
    private WantedListRepo wantedListRepo;
    @Autowired
    DormantRepo dormantRepo;

    @Autowired
    BeneficiariesListRepo beneficiariesListRepo;
    @Autowired
    ConvictsAbroadRepo convictsAbroadRepo;
    @Autowired
    KuisRepo kuisRepo;
    @Autowired
    IncapacitatedRepo incapacitatedRepo;
    @Autowired
    DrugAddictsRepo drugAddictsRepo;
    @Autowired
    ImmoralLifestlyeRepo immoralLifestlyeRepo;

    @Autowired
    SudispolRepo sudispolRepo;
    @Autowired
    UnemployedRepo unemployedRepo;
    public FLRiskDto findFlRiskByIin(String iin) {
        FLRiskDto flRiskDto = new FLRiskDto();
        flRiskDto.setIin(iin);

        try {
            flRiskDto.setBankrots(bankrotRepo.getbankrotByByIIN(iin));
        } catch (Exception e) {
            // Catch block left empty
        }

        try {
            flRiskDto.setIncapacitateds(incapacitatedRepo.getIncapacitatedByIIN(iin));
        } catch (Exception e) {
            // Catch block left empty
        }

        try {
            flRiskDto.setKuis(kuisRepo.getKuisByIIN(iin));
        } catch (Exception e) {
            // Catch block left empty
        }



        try {
            flRiskDto.setCriminals(criminalsRepo.getcriminalsByByIIN(iin));
        } catch (Exception e) {
            // Catch block left empty
        }  try {
            flRiskDto.setBeneficiariesLists(beneficiariesListRepo.getBenByIIN(iin));
        } catch (Exception e) {
            // Catch block left empty
        }
        try {
            flRiskDto.setUnemployeds(unemployedRepo.getUnemployedByIIN(iin));
        } catch (Exception e) {
            // Catch block left empty
        }
        try {
            flRiskDto.setSudispols(sudispolRepo.findAllbyIin(iin));
        } catch (Exception e) {
            // Catch block left empty
        }

        try {
            flRiskDto.setDismissals(dismissalRepo.getDismissalByIIN(iin));
        } catch (Exception e) {
            // Catch block left empty
        }

        try {
            flRiskDto.setOpgEntities(opgRepo.getopgByIIN(iin));
        } catch (Exception e) {
            // Catch block left empty
        }

        try {
            flRiskDto.setNdsEntities(ndsEntityRepo.getUsersByLike(iin));
        } catch (Exception e) {
            // Catch block left empty
        }

        try {
            flRiskDto.setImmoralLifestyles(immoralLifestlyeRepo.getImmoByIIN(iin));
        } catch (Exception e) {
            // Catch block left empty
        }


        try {
            flRiskDto.setDormants(dormantRepo.getUsersByLike(iin));
        } catch (Exception e) {
            // Catch block left empty
        }

        try {
            flRiskDto.setBlockEsfs(block_esfRepo.getblock_esfByIIN(iin));
        } catch (Exception e) {
            // Catch block left empty
        }


        try {
            flRiskDto.setConvictsAbroads(convictsAbroadRepo.getConvictsAbroadByIIN(iin));
        } catch (Exception e) {
            // Catch block left empty
        }

        try {
            flRiskDto.setConvictsJustifieds(convicts_justifiedRepo.getconvicts_justifiedByByIIN(iin));
        } catch (Exception e) {
            // Catch block left empty
        }

        try {
            flRiskDto.setDrugAddicts(drugAddictsRepo.getDrugAddictsByIIN(iin));
        } catch (Exception e) {
            // Catch block left empty
        }

        try {
            flRiskDto.setOmns(omn_repo.getUsersByLikeIin_bin(iin));
        } catch (Exception e) {
            // Catch block left empty
        }

        try {
            flRiskDto.setOrphans(orphans_repo.getUsersByLike(iin));
        } catch (Exception e) {
            // Catch block left empty
        }

        try {
            flRiskDto.setMzEntities(mzEntityRepo.getMzByIIN(iin));
        } catch (Exception e) {
            // Catch block left empty
        }

        try {
            flRiskDto.setWantedListEntities(wantedListRepo.getByIIN(iin));
        } catch (Exception e) {
            // Catch block left empty
        }

        try {
            flRiskDto.setFirstCreditBureauEntities(firstCreditBureauEntityRepo.getUsersByLike(iin));
        } catch (Exception e) {
            // Catch block left empty
        }

        try {
            flRiskDto.setConvictsTerminatedByRehabs(convicts_terminated_by_rehabRepo.getconvicts_terminated_by_rehabByByIIN(iin));
        } catch (Exception e) {
            // Catch block left empty
        }

        int sum =
                (Optional.ofNullable(flRiskDto.getBankrots()).orElse(Collections.emptyList()).size() > 0 ? 1 : 0) +
                (Optional.ofNullable(flRiskDto.getDormants()).orElse(Collections.emptyList()).size() > 0 ? 1 : 0) +
                (Optional.ofNullable(flRiskDto.getFirstCreditBureauEntities()).orElse(Collections.emptyList()).size() > 0 ? 1 :0) +
                (Optional.ofNullable(flRiskDto.getConvictsAbroads()).orElse(Collections.emptyList()).size() > 0 ? 1 : 0) +
                (Optional.ofNullable(flRiskDto.getIncapacitateds()).orElse(Collections.emptyList()).size() > 0 ? 1 : 0) +
                (Optional.ofNullable(flRiskDto.getKuis()).orElse(Collections.emptyList()).size() > 0 ? 1 : 0) +
                (Optional.ofNullable(flRiskDto.getDismissals()).orElse(Collections.emptyList()).size() > 0 ? 1 : 0) +
                (Optional.ofNullable(flRiskDto.getDrugAddicts()).orElse(Collections.emptyList()).size() > 0 ? 1 : 0) +
                (Optional.ofNullable(flRiskDto.getWantedListEntities()).orElse(Collections.emptyList()).size() > 0 ? 1 : 0) +
                (Optional.ofNullable(flRiskDto.getOpgEntities()).orElse(Collections.emptyList()).size() > 0 ? 1 : 0) +
                (Optional.ofNullable(flRiskDto.getBeneficiariesLists()).orElse(Collections.emptyList()).size() > 0 ? 1 : 0) +
                (Optional.ofNullable(flRiskDto.getOmns()).orElse(Collections.emptyList()).size() > 0 ? 1 : 0) +
                (Optional.ofNullable(flRiskDto.getSudispols()).orElse(Collections.emptyList()).size() > 0 ? 1 : 0) +
                (Optional.ofNullable(flRiskDto.getConvictsJustifieds()).orElse(Collections.emptyList()).size() > 0 ? 1 : 0) +
                (Optional.ofNullable(flRiskDto.getUnemployeds()).orElse(Collections.emptyList()).size() > 0 ? 1 : 0) +
                (Optional.ofNullable(flRiskDto.getCriminals()).orElse(Collections.emptyList()).size() > 0 ? 1 : 0) +
                (Optional.ofNullable(flRiskDto.getNdsEntities()).orElse(Collections.emptyList()).size() > 0 ? 1 : 0) +
                (Optional.ofNullable(flRiskDto.getBlockEsfs()).orElse(Collections.emptyList()).size() > 0 ? 1 : 0) +
                (Optional.ofNullable(flRiskDto.getConvictsTerminatedByRehabs()).orElse(Collections.emptyList()).isEmpty() ? 0 : 1) +
                (Optional.ofNullable(flRiskDto.getImmoralLifestyles()).orElse(Collections.emptyList()).isEmpty() ? 0 : 1);

        flRiskDto.setQuantity(sum);

        return flRiskDto;
    }

}
