package kz.dossier.service;

import kz.dossier.dto.ULAdditionalInfoDTO;
import kz.dossier.dto.*;
import kz.dossier.modelsDossier.*;
import kz.dossier.modelsRisk.Adm;
import kz.dossier.modelsRisk.Pdl;
import kz.dossier.repositoryDossier.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ULService {
    @Autowired
    private MvRnOldRepo mv_rn_oldRepo;
    @Autowired
    private MvUlRepo mv_ul_repo;
    @Autowired
    private MvAutoFlRepo mvAutoFlRepo;
    @Autowired
    private EquipmentRepo equipment_repo;
    @Autowired
    private TrainsRepo trainsRepo;
    @Autowired
    private WaterTransportRepo waterTransportRepo;
    @Autowired
    private AutoTransportRepo autoTransportRepo;
    @Autowired
    private AutoPostanovkaRepo autoPostanovkaRepo;
    @Autowired
    private AutoSnyatieRepo autoSnyatieRepo;
    @Autowired
    private AviaTransportRepo aviaTransportRepo;
    @Autowired
    FpgTempEntityRepo fpgTempEntityRepo;
    @Autowired
    private MvUlLeaderRepository mvUlLeaderRepository;
    @Autowired
    MvUlFounderUlRepo mvUlFounderUlRepo;
    @Autowired
    private MvUlFounderFlRepo mvUlFounderFlRepo;
    @Autowired
    FlRiskServiceImpl flRislimplementation;
    @Autowired
    private CommodityProducerRepo commodityProducerRepo;
    @Autowired
    private RegAddressUlEntityRepo regAddressUlEntityRepo;
    @Autowired
    private AccountantListEntityRepo accountantListEntityRepo;
    @Autowired
    private PdlRepo pdlRepo;
    @Autowired
    private MvFlRepo mvFlRepo;
    @Autowired
    private FlPensionContrRepo flPensionContrRepo;
    @Autowired
    private FlContactsRepo flContactsRepo;
    @Autowired
    private QoldauRepo qoldauRepo;
    @Autowired
    private AdmRepo admRepo;


    public List<AdmRightsBreakerDTO> getAdmsFines(String bin) {
        List<Adm> list = admRepo.getUsersByLikeBin(bin);
        List<AdmRightsBreakerDTO> result = new ArrayList<>();

        for (Adm a : list) {
            if (a == null) {
                continue;
            }
            AdmRightsBreakerDTO admRightsBreakerDTO = new AdmRightsBreakerDTO();
            admRightsBreakerDTO.setOrgan(a.getAuthority_detected());
            admRightsBreakerDTO.setDateOfStart(a.getReg_date());
            admRightsBreakerDTO.setNumberOfProtocol(a.getProtocol_num());
            admRightsBreakerDTO.setPlaceOfWork(a.getWork_place());
            try {
                admRightsBreakerDTO.setQualification(admRepo.findQualificationByCode(a.getQualification()));
            } catch (Exception e) {
                admRightsBreakerDTO.setQualification("");
            }
            admRightsBreakerDTO.setForcedImplementation(a.getEnforcement());
            admRightsBreakerDTO.setPeriodTo(a.getEnd_date());
            admRightsBreakerDTO.setSumOfFine(a.getFine_amount());
            admRightsBreakerDTO.setReasonOfStopping(a.getTeminate_reason());

            result.add(admRightsBreakerDTO);
        }

        return result;
    }

    public List<SubsidiyDTO> getSubsidies(String bin) {
        List<QoldauSubsidy> subsidies = new ArrayList<>();
        try {
            subsidies = qoldauRepo.getByIIN(bin);
        } catch (Exception e) {
            return new ArrayList<>();
        }

        return transformSubsidiesDTO(subsidies);
    }

    private List<SubsidiyDTO> transformSubsidiesDTO(List<QoldauSubsidy> subsidies) {
        List<SubsidiyDTO> result = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(0);
        subsidies.forEach(x -> {
            SubsidiyDTO obj = new SubsidiyDTO();
            obj.setDenyingReason(x.getRejectionReason() != null ? x.getRejectionReason().toString() : "");
            obj.setIp1(x.getIpSubmissionApp() != null ? x.getIpSubmissionApp().toString() : "");
            obj.setIp2(x.getIpWithdrawalApp() != null ? x.getIpWithdrawalApp().toString() : "");
            obj.setIp3(x.getIpAcceptanceApp() != null ? x.getIpAcceptanceApp().toString() : "");
            obj.setIp4(x.getIpRejectionApp() != null ? x.getIpRejectionApp().toString() : "");
            obj.setSum(x.getSubsidiesAmount() != null ? df.format(x.getSubsidiesAmount()) : "");
            obj.setDateOfDenying(x.getRejectionDate() != null ? x.getRejectionDate().toString() : "");
            obj.setDateOfTaking(x.getDateOfAcceptance() != null ? x.getDateOfAcceptance().toString() : "");
            obj.setStatus(x.getApplicationStatus() != null ? x.getApplicationStatus() : "");
            obj.setNameOfTeller(x.getApplicantName() != null ? x.getApplicantName() : "");
            obj.setDate(x.getDateOfApplication() != null ? x.getDateOfApplication().toString() : "");
            obj.setOblast(x.getRegion() != null ? x.getRegion() : "");
            obj.setName(x.getName() != null ? x.getName() : "");

            result.add(obj);
        });

        return result;
    }



    public ULDto getUlByBin(String bin) {
        Optional<MvUl> ul = mv_ul_repo.getUlByBin(bin);
        RegAddressUlEntity address = regAddressUlEntityRepo.findByBin(bin);
        ULDto ulDto = new ULDto();
        if (ul.isPresent()) {
            MvUl ulEntity = ul.get();
            ulDto.setBin(bin);
            ulDto.setFullName(ulEntity.getFull_name_rus());
            ulDto.setStatus(ulEntity.getUl_status());
            ulDto.setRegDate(ulEntity.getOrg_reg_date());
            ulDto.setResident(ulEntity.getIs_resident() != null ? ulEntity.getIs_resident().equals("1") ? true : false : false);
        } else {
            return null;
        }
        if (address != null) {
            ulDto.setOked(address.getOkedNameRu());
        } else {
            ulDto.setOked("Нет");
        }


        return ulDto;
    }

//    public Double countInfoPercentageForUl(String bin) {
//
//    }

    public List<CommodityProducersDTO> getComProducersByBin(String bin) {
        List<CommodityProducer> list = commodityProducerRepo.getiin_binByIIN(bin);
        List<CommodityProducersDTO> result = new ArrayList<>();
        list.forEach(x -> {
            CommodityProducersDTO obj = new CommodityProducersDTO();
            obj.setCount(x.getCount());
            obj.setRegion(x.getRegion());
            obj.setStatus(x.getStatus());
            obj.setSzpt(x.getSspName());

            result.add(obj);
        });

        return result;
    }

    public List<ULULMemberDTO> getULMembersByBin(String bin) {
        List<ULULMemberDTO> ululMemberDTOS = new ArrayList<>();
        try {
            List<MvUlFounderUl> ulFounders = mvUlFounderUlRepo.getUsersByLike(bin);
            ulFounders.forEach(x -> {
                ULULMemberDTO obj = new ULULMemberDTO();
                obj.setDate(x.getRegDate());
                obj.setBinIin(x.getFounderBin());
                obj.setName(x.getFounderNameRu());
                if (x.isCurrent()) {
                    obj.setPosition("Учредитель ЮЛ");
                } else {
                    obj.setPosition("Учредитель ЮЛ (Исторический)");
                }
//                obj.setRisksNumber();
                ululMemberDTOS.add(obj);
            });
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            List<MvUlFounderFl> ulFounders = mvUlFounderFlRepo.getUsersByLike(bin);
            ulFounders.forEach(x -> {
                ULULMemberDTO obj = new ULULMemberDTO();
                obj.setDate(x.getReg_date());
                obj.setBinIin(x.getIin());
                try {
                    String name = x.getLastname() + " " + x.getFirstname() + " " + x.getPatronymic();
                    obj.setName(name);
                } catch (Exception e) {

                }
                if (x.isIs_curr()) {
                    obj.setPosition("Учредитель ФЛ");
                } else {
                    obj.setPosition("Учредитель ФЛ (Исторический)");
                }
//                obj.setRisksNumber();
                ululMemberDTOS.add(obj);
            });
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            List<MvUlLeader> flFounders = mvUlLeaderRepository.findAllByBinOrg(bin);
            flFounders.forEach(x -> {
                ULULMemberDTO obj = new ULULMemberDTO();
                obj.setDate(x.getReg_date() != null ? x.getReg_date().toString() : "");
                obj.setBinIin(x.getIin());
                try {
                    String name = x.getLast_name() + " " + x.getFirst_name() + " " + x.getPatronymic();
                    obj.setName(name);
                } catch (Exception e) {

                }
                if (x.isIs_curr()) {
                    obj.setPosition("Директор");
                } else {
                    obj.setPosition("Директор (Исторический)");
                }
//                FLRiskDto risks = flRislimplementation.findFlRiskByIin(x.getIin());
//                obj.setRisksNumber(risks.getQuantity());
                ululMemberDTOS.add(obj);

            });
        } catch (Exception e) {
            System.out.println(e);
        }
        return ululMemberDTOS;
    }

    public RegAddressULDto getUlAddressByBin(String bin) {
        RegAddressUlEntity addressUlEntity = regAddressUlEntityRepo.findByBin(bin);
        RegAddressULDto result = new RegAddressULDto();
        result.setRegionRu(addressUlEntity.getRegAddrRegionRu());
        result.setLocalityRu(addressUlEntity.getRegAddrLocalityRu());
        result.setDistrict(addressUlEntity.getRegAddrDistrictRu());
        result.setStreetRu(addressUlEntity.getRegAddrStreetRu());
        result.setBuildingNum(addressUlEntity.getRegAddrBuildingNum());
        return result;
    }

    public List<SameULRegAddressDto> getSameAddressULByBin(String bin) {
        RegAddressUlEntity addressUlEntity = regAddressUlEntityRepo.findByBin(bin);
        System.out.println(addressUlEntity.getRegAddrDistrictRu() + " " + addressUlEntity.getRegAddrStreetRu() + " " + addressUlEntity.getRegAddrBuildingNum());
        List<RegAddressUlEntity> units = regAddressUlEntityRepo.getByAddress(addressUlEntity.getRegAddrRegionRu(), addressUlEntity.getRegAddrDistrictRu(), addressUlEntity.getRegAddrStreetRu(), addressUlEntity.getRegAddrBuildingNum(),bin );
//        List<RegAddressUlEntity> units = regAddressUlEntityRepo.getByFullAddress(addressUlEntity.getRegAddrRegionRu(), addressUlEntity.getRegAddrDistrictRu(), addressUlEntity.getRegAddrLocalityRu(), addressUlEntity.getRegAddrStreetRu(), addressUlEntity.getRegAddrBuildingNum(), bin);

        List<SameULRegAddressDto> list = new ArrayList<>();
        for (RegAddressUlEntity l: units) {
            if (l.getActive()) {
                Optional<MvUl> ul = mv_ul_repo.getUlByBin(l.getBin());
                if (ul.isPresent()) {
                    SameULRegAddressDto res = new SameULRegAddressDto();
                    res.setDate(ul.get().getOrg_reg_date());
                    res.setBin(ul.get().getBin());
                    res.setName(ul.get().getShort_name());
                    res.setAddress(addressUlEntity.getRegAddrRegionRu() + " " + addressUlEntity.getRegAddrDistrictRu() + " " + addressUlEntity.getRegAddrStreetRu() + " " + addressUlEntity.getRegAddrBuildingNum());
                    list.add(res);
                }
            }
        }
        return list;
    }

    public List<FPGDto> getFpgsByBin(String bin) {
        List<FpgTempEntity> list = fpgTempEntityRepo.getUsersByLike(bin);
        List<FPGDto> result = new ArrayList<>();
        for (FpgTempEntity a : list) {
            if (a  == null) {
                continue;
            }
            FPGDto obj = new FPGDto();
            obj.setBeneficiar(a.getBeneficiary());
            result.add(obj);
        }

        return result;
    }

    public List<AccountantDto> getAccountantsByBin(String bin) {
        List<AccountantListEntity> list = accountantListEntityRepo.getUsersByLikeBIN(bin);
        List<AccountantDto> result = new ArrayList<>();
        for (AccountantListEntity a : list) {
            if (a  == null) {
                continue;
            }
            AccountantDto obj = new AccountantDto();
            obj.setFio(String.join(" ", a.getLname(), a.getFname()));
            obj.setIin(a.getIin());
            obj.setPosition(a.getProf());
            result.add(obj);
        }
        return result;
    }

    public List<PdlDto> getPdlByBin(String bin) {
        List<Pdl> list = pdlRepo.getByBin(bin);
        List<PdlDto> result = new ArrayList<>();
        for (Pdl a : list) {
            if (a  == null) {
                continue;
            }
            PdlDto obj = new PdlDto();
            obj.setIin(a.getIin());
            try {
                String name =  mvFlRepo.getNameByIIN(a.getIin());
                obj.setFio(name);
            } catch (Exception e) {
                obj.setFio("");
            }
            obj.setOblast(a.getOblast());
            obj.setOrgan(a.getOrgan());
            obj.setPosition(a.getPosition());
            obj.setIinSpouse(a.getSpouse_iin());
            obj.setFullNameSpouse(a.getSpouse_fio());
            obj.setOrganSpouse(a.getSpouse_organ());
            obj.setPositionSpouse(a.getSpouse_position());
            result.add(obj);
        }
        return result;
    }

    public List<PensionDto> getPensionByBin(String bin) {
        List<Map<String, Object>> list = flPensionContrRepo.getYearGroupedForBin(bin);
        List<PensionDto> result = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(0);
        for (Map<String, Object> a : list) {
            if (a  == null) {
                continue;
            }
            PensionDto obj = new PensionDto();
            obj.setYear(df.format(a.get("year")));
            obj.setNumberOfEmps(a.get("person_count").toString());
            result.add(obj);
        }
        return result;
    }


    public ULAdditionalInfoDTO additionalByBin(String bin) {
        ULAdditionalInfoDTO result = new ULAdditionalInfoDTO();
        try {
            List<MvRnOld> mvRnOlds = mv_rn_oldRepo.getUsersByLike(bin);
            List<MvRnOld> list = setNamesByBin(mvRnOlds);
            result.setMvRnOlds(list);
        } catch (Exception e){
            System.out.println("Error:" + e);
        }
        try {
            List<MvAutoFl> myMv_auto_fl =  mvAutoFlRepo.getUsersByLike(bin);
            try {
                result.setMvAutoFls(myMv_auto_fl);
            } catch (Exception e) {
                System.out.println("mv_auto_fl Error: " + e);
            }

        } catch (Exception e){
            System.out.println("mv_auto_fl WRAP Error:" + e);
        }
        try {
            List<Equipment> myEquipment =  equipment_repo.getUsersByLike(bin);
            result.setEquipment(myEquipment);
        } catch (Exception e){
            System.out.println("Error:" + e);
        }
        try {
            List<Trains> trains =  trainsRepo.getByIIN(bin);
            result.setTrains(trains);
        } catch (Exception e){
            System.out.println("Error:" + e);
        }
        try {
            List<WaterTransport> waterTransports =  waterTransportRepo.getWaterByIin(bin);
            result.setWaterTransports(waterTransports);
        } catch (Exception e){
            System.out.println("Error:" + e);
        }
        try {
            List<AutoTransport> autoTransports =  autoTransportRepo.getAutoByIin(bin);
            result.setAutoTransports(autoTransports);
        } catch (Exception e){
            System.out.println("Error:" + e);
        }
        try {
            result.setAutoPostanovkas(autoPostanovkaRepo.getAutoPostanovkaByBin(bin));
        } catch (Exception e){
            System.out.println("Error:" + e);
        }
        try {
            result.setAutoSnyaties(autoSnyatieRepo.getAutoSnyatieByIin(bin));
        } catch (Exception e){
            System.out.println("Error:" + e);
        }
        try {
            List<AviaTransport> aviaTransports =  aviaTransportRepo.getAviaByIin(bin);
            result.setAviaTransports(aviaTransports);
        } catch (Exception e){
            System.out.println("Error:" + e);
        }
        try {
            List<FpgTempEntity> fpgTempEntities = fpgTempEntityRepo.getUsersByLike(bin);
            List<String> fpgs = new ArrayList<>();
            fpgTempEntities.forEach(x -> {
                fpgs.add(x.getBeneficiary() != null ? x.getBeneficiary() : "");
            });
            result.setFpg(fpgs);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        List<ULULMemberDTO> ululMemberDTOS = new ArrayList<>();
        try {
            List<MvUlFounderUl> ulFounders = mvUlFounderUlRepo.getUsersByLike(bin);
            ulFounders.forEach(x -> {
                ULULMemberDTO obj = new ULULMemberDTO();
                obj.setDate(x.getRegDate());
                obj.setBinIin(x.getFounderBin());
                obj.setName(x.getFounderNameRu());
                if (x.isCurrent()) {
                    obj.setPosition("Учредитель ЮЛ");
                } else {
                    obj.setPosition("Учредитель ЮЛ (Исторический)");
                }
//                obj.setRisksNumber();
                ululMemberDTOS.add(obj);
            });
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            List<MvUlLeader> flFounders = mvUlLeaderRepository.findAllByBinOrg(bin);
            flFounders.forEach(x -> {
                ULULMemberDTO obj = new ULULMemberDTO();
                obj.setDate(x.getReg_date() != null ? x.getReg_date().toString() : "");
                obj.setBinIin(x.getIin());
                try {
                    String name = x.getLast_name() + " " + x.getFirst_name() + x.getPatronymic();
                    obj.setName(name);
                } catch (Exception e) {

                }
                if (x.isIs_curr()) {
                    obj.setPosition("Директор");
                } else {
                    obj.setPosition("Директор (Исторический)");
                }
                FLRiskDto risks = flRislimplementation.findFlRiskByIin(x.getIin());
                obj.setRisksNumber(risks.getQuantity());
                ululMemberDTOS.add(obj);

            });
        } catch (Exception e) {
            System.out.println(e);
        }
        result.setUlMembers(ululMemberDTOS);

        return result;
    }

    public List<ContactDetailDto> getContactsByBin(String bin) {
        List<FlContacts> list = flContactsRepo.findAllByIin(bin);
        List<ContactDetailDto> result = new ArrayList<>();
        for (FlContacts a : list) {
            ContactDetailDto obj = new ContactDetailDto();
            obj.setEmail(a.getEmail());
            obj.setNickname(a.getNickname());
            obj.setNumber(a.getPhone());
            obj.setSource(a.getSource());
            obj.setFioOfHead(a.getLeader_fio());
            obj.setNameOfOwner(a.getFio());
            result.add(obj);
        }
        return result;
    }
    private List<MvRnOld> setNamesByBin(List<MvRnOld> list) {
        for (MvRnOld a : list) {
            String name = mv_ul_repo.getNameByBin(a.getOwner_iin_bin());
            if (name != null) {
                a.setOwner_full_name(name);
            }
        }
        return list;
    }
}
