package kz.dossier.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.dossier.dto.*;
import kz.dossier.modelsDossier.*;
import kz.dossier.modelsRisk.Pdl;
import kz.dossier.repositoryDossier.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlService {
    private final KxRepo kxRepo;
    private final MvFlRepo mvFlRepo;
    private final MvIinDocRepo mvIinDocRepo;
    private final SchoolRepo schoolRepo;
    private final MvRnOldRepo mvRnOldRepo;
    private final MvUlRepo mvUlRepo;
    private final ChangeFioRepo changeFioRepo;
    private final MvUlFounderFlRepo mvUlFounderFlRepo;
    private final MvUlLeaderEntityRepo mvUlLeaderEntityRepo;
    private final EquipmentRepo equipmentRepo;
    private final FlContactsRepo flContactsRepo;
    private final UniversitiesRepo universitiesRepo;
    private final FlPensionContrRepo flPensionContrRepo;
    private final IpgoEmailEntityRepo ipgoEmailEntityRepo;
    private final BailiffListEntityRepo bailiffListEntityRepo;
    private final AuditorsListEntityRepo auditorsListEntityRepo;
    private final MvFlAddressRepository mvFlAddressRepository;
    private final AdvocateListEntityRepo advocateListEntityRepo;
    private final AccountantListEntityRepo accountantListEntityRepo;
    private final RegistrationTempRepository registrationTempRepository;
    private final IndividualEntrepreneurRepo individualEntrepreneurRepo;
    private final MilitaryAccounting2Repo militaryAccounting2Repo;
    private final MyService myService;
    private final PdlRepo pdlRepo;
    private final FlRiskServiceImpl flRiskService;
    public MvFlWithPhotoDto getMvFl(String iin) {
        MvFlWithPhotoDto mvFlWithPhotoDto = new MvFlWithPhotoDto();
        List<MvFl> myMv_fl = mvFlRepo.getUsersByLike(iin);
        mvFlWithPhotoDto = myService.tryAddPhotoToDto(mvFlWithPhotoDto, iin);
        FLRiskDto flRiskDto = flRiskService.findFlRiskByIin(iin);
        Double percentage = flRiskDto.setPercentage(flRiskDto.getQuantity());
        if (!myMv_fl.isEmpty()) {
            mvFlWithPhotoDto.setMvFlList(myMv_fl);
        }
        if (percentage != null) {
            mvFlWithPhotoDto.setRiskPercentage(percentage);
        }
        if (mvFlWithPhotoDto.getPhotoDbs() == null && mvFlWithPhotoDto.getMvFlList() == null && mvFlWithPhotoDto.getRiskPercentage() == null) {
            return null;
        } else {
            return mvFlWithPhotoDto;
        }
    }

    public List<MvIinDoc> getMvDocs(String iin) {
        List<MvIinDoc> mvIinDocs = mvIinDocRepo.getByIIN(iin);
        if (!mvIinDocs.isEmpty()) {
            return mvIinDocs;
        } else {
            return null;
        }
    }

    public List<MvFlAddress> getMvFlAddressByIIN(String iin) {
        List<MvFlAddress> mvFlAddresses = mvFlAddressRepository.getMvFlAddressByIIN(iin);

        if (!mvFlAddresses.isEmpty()) {
            return mvFlAddresses;
        } else {
            return null;
        }
    }

    public List<RegistrationTemp> getRegAddressByIIN(String iin) {
        List<RegistrationTemp> registrationTemps = registrationTempRepository.getRegAddressByIIN(iin);

        if (!registrationTemps.isEmpty()) {
            return registrationTemps;
        } else {
            return null;
        }
    }

    public List<ChangeFio> getChangeFioByIIN(String iin) {
        List<ChangeFio> changeFios = changeFioRepo.getByIin(iin);

        if (!changeFios.isEmpty()) {
            return changeFios;
        } else {
            return null;
        }
    }

    public List<FlContacts> getContactsByIIN(String iin) {
        List<FlContacts> flContacts = flContactsRepo.findAllByIin(iin);

        if (!flContacts.isEmpty()) {
            return flContacts;
        } else {
            return null;
        }
    }

    public List<AdvocateListEntity> getAdvocateListEntity(String iin) {
        List<AdvocateListEntity> advocateListEntities = advocateListEntityRepo.getUsersByLike(iin);

        if (!advocateListEntities.isEmpty()) {
            return advocateListEntities;
        } else {
            return null;
        }
    }

    public List<AccountantListEntity> getAccountantListEntity(String iin) {
        List<AccountantListEntity> accountantListEntities = accountantListEntityRepo.getUsersByLike(iin);

        if (!accountantListEntities.isEmpty()) {
            return accountantListEntities;
        } else {
            return null;
        }
    }

    public List<BailiffListEntity> getBailiffListEntity(String iin) {
        List<BailiffListEntity> bailiffListEntities = bailiffListEntityRepo.getUsersByLike(iin);

        if (!bailiffListEntities.isEmpty()) {
            return bailiffListEntities;
        } else {
            return null;
        }
    }

    public List<AuditorsListEntity> getAuditorsListEntity(String iin) {
        List<AuditorsListEntity> auditorsListEntities = auditorsListEntityRepo.getUsersByLike(iin);

        if (!auditorsListEntities.isEmpty()) {
            return auditorsListEntities;
        } else {
            return null;
        }
    }

    public List<IpgoEmailEntity> getIpgoEmailEntity(String iin) {
        List<IpgoEmailEntity> ipgoEmailEntities = ipgoEmailEntityRepo.getUsersByLike(iin);

        if (!ipgoEmailEntities.isEmpty()) {
            return ipgoEmailEntities;
        } else {
            return null;
        }
    }

    public IpKxDto getIpKxDto(String iin) {
        IpKxDto ipKxDto = new IpKxDto();
        List<IndividualEntrepreneur> individualEntrepreneurs = individualEntrepreneurRepo.getByIin(iin);
        List<KX> kxes = kxRepo.getKxIin(iin);

        if (!individualEntrepreneurs.isEmpty()) {
            ipKxDto.setIndividualEntrepreneurList(individualEntrepreneurs);
        }
        if (!kxes.isEmpty()) {
            ipKxDto.setKxes(kxes);
        }
        if (ipKxDto.getKxes() == null && ipKxDto.getIndividualEntrepreneurList() == null) {
            return null;
        } else {
            return ipKxDto;
        }
    }



    public EduDto getEduDto(String iin) {
        EduDto eduDto = new EduDto();
        List<Universities> universities = universitiesRepo.getByIIN(iin);
        List<School> schools = schoolRepo.getByIIN(iin);

        if (!universities.isEmpty()) {
            eduDto.setUniversitiesList(universities);
        }
        if (!schools.isEmpty()) {
            eduDto.setSchools(schools);
        }
        if (eduDto.getSchools() == null && eduDto.getUniversitiesList() == null) {
            return null;
        } else {
            return eduDto;
        }
    }

    public PensionAllDto getPensionAllDto(String iin) {
        PensionAllDto pensionAllDto = new PensionAllDto();
        List<String> companyBins = flPensionContrRepo.getUsersByLikeCompany(iin).stream().distinct().collect(Collectors.toList());;

        List<PensionListDTO> pensions = new ArrayList<>();
        List<PensionGroupDTO> result = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(0);
        for (String bin : companyBins) {
            List<Map<String, Object>> fl_pension_contrss = new ArrayList<>();
            fl_pension_contrss = flPensionContrRepo.getAllByCompanies(iin, bin);
            PensionGroupDTO obj = new PensionGroupDTO();
            List<PensionListDTO> group = new ArrayList<>();
            String name = "";
            if (fl_pension_contrss.get(0).get("P_NAME") != null) {
                name = (String) fl_pension_contrss.get(0).get("P_NAME") + ", ";
            }
            if (bin != null) {
                name = name + bin + ", период ";
            }
            List<String> distinctPayDates = fl_pension_contrss.stream()
                    .map(pension -> pension.get("pay_date").toString())
                    .distinct()
                    .collect(Collectors.toList());

            double knp010sum = 0.0;
            double knp012sum = 0.0;

            for (String year : distinctPayDates) {
                if (year != null) {
                    name = name + year.replace(".0", "") + ", ";
                }
                PensionListDTO pensionListEntity = new PensionListDTO();
                pensionListEntity.setBin(bin);
                pensionListEntity.setName((String) fl_pension_contrss.get(0).get("P_NAME"));
                pensionListEntity.setPeriod(year.replace(".0", ""));
                try {
                    double knp010 = fl_pension_contrss.stream()
                            .filter(pension -> pension.get("pay_date").toString().equals(year) && pension.get("KNP").toString().equals("010"))
                            .mapToDouble(pension -> Double.parseDouble(pension.get("AMOUNT").toString()))
                            .sum();

                    pensionListEntity.setSum010(knp010 * 8.5);

                    knp010sum = (knp010sum + knp010)* 8.5;

                } catch (Exception e) {

                }
                try {
                    double knp012 = fl_pension_contrss.stream()
                            .filter(pension -> pension.get("pay_date").toString().equals(year) && pension.get("KNP").toString().equals("012"))
                            .mapToDouble(pension -> Double.parseDouble(pension.get("AMOUNT").toString()))
                            .sum();

                    pensionListEntity.setSum012(knp012);
                    knp012sum = knp012sum + knp012;
                } catch (Exception e) {

                }
                pensions.add(pensionListEntity);
                group.add(pensionListEntity);
            }
            name = name + "общая сумма КНП(010): " + df.format(knp010sum) + ", общая сумма КНП(012): " + df.format(knp012sum);
            obj.setName(name);
            obj.setList(group);
            result.add(obj);
        }

        if (!pensions.isEmpty()) {
            pensionAllDto.setPensionListDTOS(pensions);
        }
        if (!result.isEmpty()) {
            pensionAllDto.setPensionGroupDTOS(result);
        }
        if (pensionAllDto.getPensionGroupDTOS() == null && pensionAllDto.getPensionListDTOS() == null) {
            return null;
        } else {
            return pensionAllDto;
        }
    }


    public List<MilitaryAccountingDTO> getMilitaryAccountingDTO(String iin) {
        ObjectMapper objectMapper = new ObjectMapper();
            List<MilitaryAccounting2Entity> militaryAccounting2Entities = militaryAccounting2Repo.getUsersByLike(iin);
            List<MilitaryAccountingDTO> militaryAccountingDTOS = new ArrayList<>();
            if (!militaryAccounting2Entities.isEmpty() & militaryAccounting2Entities.size() > 0) {
                try {
                    for (MilitaryAccounting2Entity militaryAccounting2Entity : militaryAccounting2Entities) {
                        MilitaryAccountingDTO militaryAccountingDTO = objectMapper.convertValue(militaryAccounting2Entity, MilitaryAccountingDTO.class);
                        militaryAccountingDTO.setBinName(mvUlRepo.getNameByBin(militaryAccountingDTO.getBin()));
                        militaryAccountingDTOS.add(militaryAccountingDTO);
                    }
                } catch (Exception e) {
                }
            }

        if (!militaryAccountingDTOS.isEmpty()) {
            return militaryAccountingDTOS;
        } else {
            return null;
        }
    }

    public UlParticipantsDto getUlParticipantsDto(String iin) {
        UlParticipantsDto ulParticipantsDto = new UlParticipantsDto();
        List<MvUlFounderFl> mvUlFounderFlList = mvUlFounderFlRepo.getUsersByLikeIIN(iin);
        List<MvUlLeaderEntity> mvUlLeaderEntities = mvUlLeaderEntityRepo.getUsersByLikeIin(iin);
        try {
            for(MvUlFounderFl mvUlFounderFl: mvUlFounderFlList){
                mvUlFounderFl.setBinName(mvUlRepo.getNameByBin(mvUlFounderFl.getBin_org()));
            }
        } catch (Exception e) {
        }
        try {
            for(MvUlLeaderEntity mvUlLeaderEntity: mvUlLeaderEntities){
                mvUlLeaderEntity.setBinName(mvUlRepo.getNameByBin(mvUlLeaderEntity.getBinOrg()));
            }
        } catch (Exception e) {
        }
        if (!mvUlFounderFlList.isEmpty()) {
            ulParticipantsDto.setMvUlFounderFlList(mvUlFounderFlList);
        }
        if (!mvUlLeaderEntities.isEmpty()) {
            ulParticipantsDto.setMvUlLeaderEntities(mvUlLeaderEntities);
        }
        if (ulParticipantsDto.getMvUlFounderFlList() == null && ulParticipantsDto.getMvUlLeaderEntities() == null) {
            return null;
        } else {
            return ulParticipantsDto;
        }
    }

    public List<MvRnOld> getMvRnOlds(String iin){
            List<MvRnOld> mvRnOlds = mvRnOldRepo.getUsersByLike(iin);
            List<MvRnOld> list = myService.setNamesByBin(mvRnOlds);

            if (!list.isEmpty()){
                return list;
            }else {
                return null;
            }
    }
    public List<PdlDto> getPdlByIin(String bin) {
        List<Pdl> list = pdlRepo.getByIIN(bin);
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

}
