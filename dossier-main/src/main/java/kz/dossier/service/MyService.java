package kz.dossier.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.dossier.dto.*;
import kz.dossier.modelsDossier.*;
import kz.dossier.modelsRisk.*;
import kz.dossier.repositoryDossier.*;
import kz.dossier.extractor.Mv_fl_extractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialBlob;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class MyService {
    @Autowired
    QoldauRepo QoldauRepo;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    CommodityProducerRepo commodityProducerRepo;
    @Autowired
    TIpEntityRepo TIpEntityRepo;
    @Autowired
    BankrotRepo bankrotRepo;
    @Autowired
    MshRepo mshRepo;
    @Autowired
    OpgRepo opgRepo;
    @Autowired
    BlockEsfRepo block_esfRepo;
    @Autowired
    ConvictsTerminatedtByRehabRepo convicts_terminated_by_rehabRepo;
    @Autowired
    TaxOutEntityRepo taxOutEntityRepo;
    @Autowired
    CriminalsRepo criminalsRepo;
    @Autowired
    FlPensionContrRepo flPensionContrRepo;
    @Autowired
    MzEntityRepo mzEntityRepo;
    @Autowired
    WantedListRepo wantedListRepo;
    @Autowired
    FlPensionMiniRepo flPensionMiniRepo;
    @Autowired
    MilitaryAccounting2Repo MilitaryAccounting2Repo;
    @Autowired
    MvUlFounderFlRepo mvUlFounderFlRepo;
    @Autowired
    ConvictsJustifiedRepo convicts_justifiedRepo;
    @Autowired
    IpgoEmailEntityRepo IpgoEmailEntityRepo;
    @Autowired
    MvUlFounderUlRepo mvUlFounderUlRepo;
    @Autowired
    MvUlLeaderEntityRepo mvUlLeaderEntityRepo;
    @Autowired
    RegAddressUlEntityRepo RegAddressUlEntityRepo;
    @Autowired
    AdvocateListEntityRepo advocateListEntityRepo;
    @Autowired
    AuditorsListEntityRepo auditorsListEntityRepo;
    @Autowired
    BailiffListEntityRepo bailiffListEntityRepo;
    @Autowired
    AccountantListEntityRepo accountantListEntityRepo;
    @Autowired
    FirstCreditBureauEntityRepo FirstCreditBureauEntityRepo;
    @Autowired
    NewPhotoRepo newPhotoRepo;
    @Autowired
    private MvAutoFlRepo mvAutoFlRepo;
    @Autowired
    FpgTempEntityRepo fpgTempEntityRepo;
    @Autowired
    private MvFlRepo mv_FlRepo;
    @Autowired
    private OmnRepo omn_repos;
    @Autowired
    private OrphansRepo orphans_repo;
    @Autowired
    private AdmRepo admRepo;
    @Autowired
    private EquipmentRepo equipment_repo;
    @Autowired
    private MvRnOldRepo mv_rn_oldRepo;
    @Autowired
    private DormantRepo dormantRepo;

    @Autowired
    private FlRelativesRepository fl_relativesRepository;
    @Autowired
    private RegAddressFlRepo regAddressFlRepo;
    @Autowired
    private PdlRepo pdlReposotory;
    @Autowired
    private MvUlRepo mv_ul_repo;
    @Autowired
    private MvIinDocRepo mvIinDocRepo;
    @Autowired
    private UniversitiesRepo uniRepo;
    @Autowired
    private NdsEntityRepo ndsEntityRepo;
    @Autowired
    private SchoolRepo schoolRepo;
    @Autowired
    private FlContactsRepo flContactsRepo;
    @Autowired
    private militaryAccountRepo militaryAccountRepo;
    @Autowired
    MvIinDocRepo mv_iin_docRepo;
    @Autowired
    FlRiskServiceImpl flRiskService;
    @Autowired
    DirectorFounderService directorFounderService;
    @Autowired
    private MvUlLeaderRepository mvUlLeaderRepository;
    @Autowired
    private RegAddressUlEntityRepo regAddressUlEntityRepo;
    @Autowired
    private DismissalRepo dismissalRepo;
    @Autowired
    private ImmoralLifestlyeRepo immoral_lifestlyeRepo;
    @Autowired
    private BeneficiariesListRepo beneficiariesListRepo;
    @Autowired
    private ConvictsAbroadRepo convictsAbroadRepo;
    @Autowired
    private DrugAddictsRepo drugAddictsRepo;
    @Autowired
    private IncapacitatedRepo incapacitatedRepo;
    @Autowired
    private KuisRepo kuisRepo;
    @Autowired
    MvFlAddressRepository mvFlAddressRepository;
    @Autowired
    IndividualEntrepreneurRepo individualEntrepreneurRepo;
    @Autowired
    IpgoEmailEntityRepo ipgoEmailEntityRepo;
    @Autowired
    private RegistrationTempRepository registrationTempRepository;
    @Autowired
    private LawyersRepo lawyersRepo;
    @Autowired
    private ChangeFioRepo changeFioRepo;
    @Autowired
    private KxRepo kxRepo;
    @Autowired
    private AutoTransportRepo autoTransportRepo;
    @Autowired
    private AviaTransportRepo aviaTransportRepo;
    @Autowired
    private TrainsRepo trainsRepo;
    @Autowired
    private WaterTransportRepo waterTransportRepo;
    @Autowired
    private AutoPostanovkaRepo autoPostanovkaRepo;
    @Autowired
    private AutoSnyatieRepo autoSnyatieRepo;

    @Autowired
    private SamrukRepo samrukRepo;
    @Autowired
    private GoszkupRepo goszkupRepo;

    @Autowired
    private QoldauRepo qoldauRepo;




    public ULGeneralInfoDTO getUlGeneral(String bin){
        int total = 6;
        int actual = 0;
        ULGeneralInfoDTO ulGeneralInfoDTO = new ULGeneralInfoDTO();
        try {
            ulGeneralInfoDTO.setMvUlList(mv_ul_repo.getUlByBin(bin));
            actual++;
        }catch (Exception e){
            System.out.println(e);
        }try {
            ulGeneralInfoDTO.setRegAddressUlEntity(regAddressUlEntityRepo.findByBin(bin));
            actual++;

        }catch (Exception e){
            System.out.println(e);
        }
        try {
            ulGeneralInfoDTO.setCommodityProducers(commodityProducerRepo.getiin_binByIIN(bin));
            actual++;

        }catch (Exception e){
            System.out.println(e);
        }
        try {
            ulGeneralInfoDTO.setFlContacts(flContactsRepo.findAllByIin(bin));
            actual++;

        }catch (Exception e){
            System.out.println(e);
        }try {
            ulGeneralInfoDTO.setAccountantListEntities(accountantListEntityRepo.getUsersByLikeBIN(bin));
            actual++;

        }catch (Exception e){
            System.out.println(e);
        }
        try {
            ulGeneralInfoDTO.setPdls(pdlReposotory.getByBin(bin));
            actual++;

        }catch (Exception e){
            System.out.println(e);
        }
        Double percentage = (double) (actual * 100 / total);

        return ulGeneralInfoDTO;

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



    private List<GosZakupDTO> transforToGosZakupDto(List<Goszakup> goszakups, Boolean countSupplier) {
        List<GosZakupDTO> result = new ArrayList<>();
        Map<Integer, List<Goszakup>> goszakupsGroupedByYear = goszakups.stream()
                .collect(Collectors.groupingBy( s -> {
                    Date date = s.getSignDate();
                    return (date != null) ? ((java.sql.Date) date).toLocalDate().getYear() : 0;
                }));
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(0);
        goszakupsGroupedByYear.forEach((year, goszakupList) -> {
            GosZakupDTO obj = new GosZakupDTO();
            Double totalAmount = goszakupList.stream()
                    .mapToDouble(Goszakup::getContractSumWnds)
                    .sum();
            obj.setSum(df.format(totalAmount));
            obj.setPeriod(year != null ? year.toString() : "");
            if (countSupplier) {
                Long distinctCustomer = goszakupList.stream()
                        .map(Goszakup::getSupplierBin)
                        .distinct()
                        .count();
                obj.setOpposite(distinctCustomer);
            } else {
                Long distinctCustomer = goszakupList.stream()
                        .map(Goszakup::getCustomerBin)
                        .distinct()
                        .count();
                obj.setOpposite(distinctCustomer);
            }
            obj.setNumber((long) goszakupList.size());

            result.add(obj);
        });
        return result;
    }

    public GosZakupForAll gosZakupByBin(String bin) {
        GosZakupForAll result = new GosZakupForAll();
        try {
            List<Goszakup> goszakups = goszkupRepo.getBySupplierBin(bin);
            if (goszakups != null) {
                List<GosZakupDTO> whenSupplier = transforToGosZakupDto(goszakups, false);
                result.setWhenSupplier(whenSupplier);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            List<Goszakup> goszakups = goszkupRepo.getByCustomerBin(bin);
            if (goszkupRepo != null) {
                List<GosZakupDTO> whenCustomer = transforToGosZakupDto(goszakups, true);
                result.setWhenCustomer(whenCustomer);
            }
        } catch (Exception e) {
            System.out.println(e);
        }


        return result;
    }

    private List<GosZakupDetailsDTO> tranformToGosZakupDetails(List<Goszakup> goszakups, Boolean searchSupplier) {
        List<GosZakupDetailsDTO> result = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(0);
        goszakups.forEach(x -> {
            GosZakupDetailsDTO obj = new GosZakupDetailsDTO();
            try {
                obj.setAmount(df.format(x.getContractSumWnds()));
            } catch (Exception e) {
                obj.setAmount("");
            }

            obj.setDate(x.getSignDate() != null ? x.getSignDate().toString() : null);
            obj.setMethodOfBuying(x.getFaktTradeMethodsId() != null ? x.getFaktTradeMethodsId().toString() : null);
            if (searchSupplier) {
                obj.setOppositeBin(x.getSupplierBin() != null ? x.getSupplierBin().toString() : null);
            } else {
                obj.setOppositeBin(x.getCustomerBin() != null ? x.getCustomerBin().toString() : null);
            }

            obj.setContractNumber(x.getContractNumber() != null ? x.getContractNumber().toString() : null);
            obj.setDealStatus(x.getRefContractStatusId() != null ? x.getRefContractStatusId().toString() : null);
            obj.setDealType(x.getRefContractTypeId() != null ? x.getRefContractTypeId().toString() : null);
            String name = new String();
            try {
                name = mv_ul_repo.getNameByBin(obj.getOppositeBin());
                obj.setNameOfOpposite(name != null ? name : "");
            } catch (Exception e) {
                obj.setNameOfOpposite("");
                System.out.println(e);
            }
            result.add(obj);
        });
        return result;
    }

    public List<GosZakupDetailsDTO> getGosZakupDetails(String bin, Integer year, Boolean isSupplier) {
        List<Goszakup> goszakups = new ArrayList<>();
        if(isSupplier) {
            try {
                goszakups = goszkupRepo.getBySupplierBinAndYear(bin, year);
            } catch (Exception e) {
                return new ArrayList<>();
            }
            return tranformToGosZakupDetails(goszakups, false);
        } else {
            try {
                goszakups = goszkupRepo.getByCustomerBinAndYear(bin, year);
            } catch (Exception e) {
                return new ArrayList<>();
            }
            return tranformToGosZakupDetails(goszakups, true);

        }
    }

    private List<SamrukDTO> tranformToSamrukDTO(List<Samruk> samruks, Boolean countSupplier) {
        List<SamrukDTO> result = new ArrayList<>();

        Map<Integer, List<Samruk>> samruksGroupedByYear = samruks.stream()
                .collect(Collectors.groupingBy( s -> {
                    Date date = s.getContract_date();
                    return (date != null) ? ((java.sql.Date) date).toLocalDate().getYear() : 0;
                }));
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(0);

        samruksGroupedByYear.forEach((year, samrukList) -> {
            SamrukDTO obj = new SamrukDTO();
            Double totalAmount = samrukList.stream()
                    .mapToDouble(Samruk::getAmount_with_nds)
                    .sum();
            obj.setSum(df.format(totalAmount));
            obj.setPeriod(year != null ? year.toString() : "");
            if (countSupplier) {
                Long distinctCustomer = samrukList.stream()
                        .map(Samruk::getCustomer)
                        .distinct()
                        .count();
                obj.setCustomers(distinctCustomer);
            } else {
                Long distinctCustomer = samrukList.stream()
                        .map(Samruk::getSupplier)
                        .distinct()
                        .count();
                obj.setCustomers(distinctCustomer);
            }

            obj.setNumber((long) samrukList.size());

            result.add(obj);
        });

        return result;
    }



    private List<SamrukDetailsDTO> tranformToSamrukDetails(List<Samruk> samruks, Boolean searchSupplier) {
        List<SamrukDetailsDTO> result = new ArrayList<>();
        samruks.forEach(x -> {
            SamrukDetailsDTO obj = new SamrukDetailsDTO();
            obj.setAmount(x.getAmount_with_nds() != null ? x.getAmount_with_nds().toString() : null);
            obj.setDate(x.getContract_date() != null ? x.getContract_date().toString() : null);
            obj.setMethodOfBuying(x.getPurchase_method() != null ? x.getPurchase_method().toString() : null);
            if(searchSupplier) {
                obj.setOppositeBin(x.getSupplier() != null ? x.getSupplier().toString() : null);
            } else {
                obj.setOppositeBin(x.getCustomer() != null ? x.getCustomer().toString() : null);
            }

            obj.setNumberOfDeal(x.getContract_number() != null ? x.getContract_number().toString() : null);
            obj.setStatusOfDeal(x.getContract_status() != null ? x.getContract_status().toString() : null);
            obj.setTypeOfDeal(x.getContract_type() != null ? x.getContract_type().toString() : null);
            String name = new String();
            try {
                name = mv_ul_repo.getNameByBin(obj.getOppositeBin());
                obj.setNameOfOpposite(name != null ? name : "");
            } catch (Exception e) {
                obj.setNameOfOpposite("");
                System.out.println(e);
            }
            result.add(obj);
        });
        return result;
    }
    public List<SamrukDetailsDTO> getSamrukDetailsBySupplier(String bin, Integer year) {
        List<Samruk> samruks = new ArrayList<>();
        try {
            if (year == 0) {
                samruks = samrukRepo.getBySupplierAndNullYear(bin);
            } else {
                samruks = samrukRepo.getBySupplierAndYear(bin, year);
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }

        return tranformToSamrukDetails(samruks, false);
    }

    public List<SamrukDetailsDTO> getSamrukDetailsByCustomer(String bin, Integer year) {
        List<Samruk> samruks = new ArrayList<>();
        try {
            if (year == 0) {
                samruks = samrukRepo.getByCustomerAndNullYear(bin);
            } else {
                samruks = samrukRepo.getByCustomerAndYear(bin, year);
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }
        return tranformToSamrukDetails(samruks, true);
    }
    public SamrukKazynaForAll samrukByBin(String bin) {
        SamrukKazynaForAll result = new SamrukKazynaForAll();
        try {
            List<Samruk> samruks = samrukRepo.getBySupplier(bin);
            if (samruks != null) {
                List<SamrukDTO> whenSupplier = tranformToSamrukDTO(samruks, false);
                result.setWhenSupplier(whenSupplier);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            List<Samruk> samruks = samrukRepo.getByCustomer(bin);
            if (samruks != null) {
                List<SamrukDTO> whenCustomer = tranformToSamrukDTO(samruks, true);
                result.setWhenCustomer(whenCustomer);
            }
        } catch (Exception e) {
            System.out.println(e);
        }


        return result;
    }
 
    public UlCardDTO getUlCard(String bin) {
        UlCardDTO ulCardDTO = new UlCardDTO();
        try {
            Optional<MvUl> ul = mv_ul_repo.getUlByBin(bin);

            if (ul.isPresent()) {
                ulCardDTO.setBin(bin);
                ulCardDTO.setName(ul.get().getFull_name_rus());
                ulCardDTO.setStatus(ul.get().getUl_status());
                ulCardDTO.setRegDate(ul.get().getOrg_reg_date());
            }

            return ulCardDTO;
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return ulCardDTO;
        }
    }


    public UlAddressInfo getUlAddresses(String bin) {
        RegAddressUlEntity address = regAddressUlEntityRepo.findByBin(bin);
        UlAddressInfo ulAddressInfo = new UlAddressInfo();
        if (address != null) {
            ulAddressInfo.setReg_addr_region_ru(address.getRegAddrRegionRu());
            ulAddressInfo.setReg_addr_district_ru(address.getRegAddrRegionRu());
            ulAddressInfo.setReg_addr_rural_district_ru(address.getRegAddrRuralDistrictRu());
            ulAddressInfo.setReg_addr_locality_ru(address.getRegAddrLocalityRu());
            ulAddressInfo.setReg_addr_street_ru(address.getRegAddrStreetRu());
            ulAddressInfo.setReg_addr_bulding_num(address.getRegAddrBuildingNum());
            ulAddressInfo.setReg_addr_block_num(address.getRegAddrBlockNum());
            ulAddressInfo.setReg_addr_builing_body_num(address.getRegAddrBuildingBodyNum());
            ulAddressInfo.setReg_addr_office(address.getRegAddrOffice());
            ulAddressInfo.setOked(address.getOkedNameRu());
        }

        return ulAddressInfo;
    }

    public List<FlRelativiesDTO> getRelativesInfo(String iin){
        List<Object[]> flRelativesObj;
        flRelativesObj = fl_relativesRepository.findAllByIin(iin);
        List<FlRelativiesDTO> flRelativesDtoList = new ArrayList<>();
        for (Object[] relatives:flRelativesObj) {
            System.out.println(relatives);
            FlRelativiesDTO dto = new FlRelativiesDTO();
            //--Фио
            dto.setParent_fio(relatives[3] +" "+relatives[4] +" " +relatives[5]);
            System.out.println(relatives[3] +" "+relatives[4] +" " +relatives[5]);
            if(relatives[8]!=null){ //--Круг
                dto.setLevel(String.valueOf(relatives[8]));
            }
            if(relatives[8].toString().equals("1")){ //--Статус родственника
                if(relatives[0]!=null){
                    dto.setRelative_type(relatives[0].toString());
                }
            } else if(relatives[8].toString().equals("2")) {
                if(relatives[0]!=null) {
                    dto.setRelative_type(relatives[0] +" ("+relatives[19]+")");
                }
            } else if (relatives[8].toString().equals("3")){
                String relation="";
                if(relatives[19]!=null){
                    relation=" ("+relatives[19]+")";
                }
                if(relatives[0]!=null) {
                    dto.setRelative_type(relatives[0] +relation);
                }
            } else {
                if(relatives[0]!=null){
                    dto.setRelative_type(String.valueOf(relatives[0]));
                }
            }

            //--Дата рождения
            if(relatives[6]!=null) {
                if(relatives[6].toString().length()==10){
                    try{
                        dto.setParent_birth_date(String.valueOf(LocalDate.parse(relatives[6].toString())));
                    }catch (Exception e){
                        try {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                            LocalDate localDateTime = LocalDate.parse(relatives[6].toString(), formatter);
                            dto.setParent_birth_date(String.valueOf(localDateTime));
                        }catch (Exception ex){
                        }
                    }

                } else if(relatives[6].toString().length()==22){
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd:hh:mm:ss a");
                        LocalDateTime localDateTime = LocalDateTime.parse(relatives[6].toString(), formatter);
                        LocalDate localDate = localDateTime.toLocalDate();
                        dto.setParent_birth_date(String.valueOf(localDate));
                    }catch (Exception e){
                    }
                } else if(relatives[6].toString().length()==24){
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[yyyy/MM/dd:hh:mm:ss a]");
                        LocalDateTime localDateTime = LocalDateTime.parse(relatives[6].toString().substring(1,23), formatter);
                        LocalDate localDate = localDateTime.toLocalDate();
                        dto.setBirth_date(String.valueOf(localDate));
                    }catch (Exception e){
                    }
                }
            }

            //--Дата регистрация брака
            if(relatives[10]!=null) {
                if(relatives[10].toString().length()==10){
                    try{
                        dto.setMarriage_reg_date(String.valueOf(LocalDate.parse(relatives[10].toString())));
                    }catch (Exception e){
                        try {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                            LocalDate localDateTime = LocalDate.parse(relatives[10].toString(), formatter);
                            dto.setMarriage_reg_date(String.valueOf(localDateTime));
                        }catch (Exception ex){
                        }
                    }
                } else if(relatives[10].toString().length()==22){
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd:hh:mm:ss a");
                        LocalDateTime localDateTime = LocalDateTime.parse(relatives[10].toString(), formatter);
                        LocalDate localDate = localDateTime.toLocalDate();
                        dto.setMarriage_reg_date(String.valueOf(localDate));
                    }catch (Exception e){
                    }
                } else if(relatives[10].toString().length()==24){
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[yyyy/MM/dd:hh:mm:ss a]");
                        LocalDateTime localDateTime = LocalDateTime.parse(relatives[10].toString().substring(1,23), formatter);
                        LocalDate localDate = localDateTime.toLocalDate();
                        dto.setMarriage_reg_date(String.valueOf(localDate));
                    }catch (Exception e){
                    }
                }
            }

            //--Дата Рассторжения брака
            if(relatives[11]!=null && !relatives[11].toString().equals("(null)")) {
                if(relatives[11].toString().length()==10){
                    try{
                        dto.setMarriage_divorce_date(String.valueOf(LocalDate.parse(relatives[11].toString())));
                    }catch (Exception e){
                        try {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                            LocalDate localDateTime = LocalDate.parse(relatives[11].toString(), formatter);
                            dto.setMarriage_divorce_date(String.valueOf(localDateTime));
                        }catch (Exception ex){
                        }
                    }
                } else if(relatives[11].toString().length()==22) {
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd:hh:mm:ss a");
                        LocalDateTime localDateTime = LocalDateTime.parse(relatives[11].toString(), formatter);
                        LocalDate localDate = localDateTime.toLocalDate();
                        dto.setMarriage_divorce_date(String.valueOf(localDate));
                    } catch (Exception e){
                    }
                } else if(relatives[11].toString().length()==24) {
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[yyyy/MM/dd:hh:mm:ss a]");
                        LocalDateTime localDateTime = LocalDateTime.parse(relatives[11].toString().substring(1,23), formatter);
                        LocalDate localDate = localDateTime.toLocalDate();
                        dto.setMarriage_divorce_date(String.valueOf(localDate));
                    }catch (Exception e){
                    }
                }
            }

            //--ИИН родственника
            if(relatives[2]!=null && !relatives[2].toString().equals("---")) {
                dto.setParent_iin(relatives[2].toString());
            }

            if(!dto.getParent_fio().equals("null null null")){
                boolean isRel = true;

                if(!flRelativesDtoList.isEmpty()){
                    for(FlRelativiesDTO rel: flRelativesDtoList){
                        if((rel.getParent_iin()!=null && dto.getParent_iin()!=null) && rel.getParent_iin().equals(dto.getParent_iin())){
                            isRel=false;

                            break;
                        }

                        if((rel.getParent_fio()!=null && dto.getParent_fio()!=null) && ( rel.getParent_fio().equals(dto.getParent_fio()) )){
                            isRel=false;
                            break;
                        }

                    }
                }

                if(isRel){
                    flRelativesDtoList.add(dto);

                }
            }
        }
        return flRelativesDtoList;
    }
    public List<SearchResultModelUl> searchUlByName(String name) {
        List<MvUl> mvUls = mv_ul_repo.getUlsByName(name.replace("$", "%"));
        List<SearchResultModelUl> list = new ArrayList<>();
        for (MvUl l: mvUls) {
            SearchResultModelUl res = new SearchResultModelUl();
            res.setBin(l.getBin());
            res.setName(l.getShort_name());
            list.add(res);
        }
        return list;
    }

    public List<SearchResultModelFL> getWIthAddFields(HashMap<String, String> req) {
        List<MvAutoFl> list = new ArrayList<>();
        if (req.get("vin") != "") {
            list =  mvAutoFlRepo.findBYVIN(req.get("vin"));
        }
        if (list.size() < 1) {
            String sql = createAdditionSQL(req);
            List<MvFl> fls = jdbcTemplate.query(sql, new Mv_fl_extractor());
            System.out.println(sql);
            List<SearchResultModelFL> result = findWithPhoto(fls);
            return result;
        } else {
            List<MvFl> fls = new ArrayList<>();
            for (MvAutoFl i: list) {
                try {
                    MvFl r = mv_FlRepo.getUserByIin(i.getIin());
                    fls.add(r);
                } catch (Exception e) {
                }
            }
            List<SearchResultModelFL> result = findWithPhoto(fls);
            return result;
        }
    }

//    public FlRelatives getFlRelativesInfo()

    public List<SearchResultModelFL> getByAddressUsingIin(String iin) {
        List<MvFlAddress> address = mvFlAddressRepository.getMvFlAddressByIIN(iin);
        System.out.println(address.size());
        if(address != null) {
            AddressInfo addressInfo = new AddressInfo();
            if (address.size() > 0) {
                addressInfo.setRegion(address.get(0).getRegion());
                addressInfo.setDistrict(address.get(0).getDistrict());
                addressInfo.setCity(address.get(0).getCity());
                addressInfo.setStreet(address.get(0).getStreet());
                addressInfo.setBuilding(address.get(0).getBuilding());
                addressInfo.setKorpus(address.get(0).getCorpus());
                addressInfo.setApartment_number(address.get(0).getFlat());
                addressInfo.setRnRegAddress(address.get(0).getRnAddressRu());
            }
            System.out.println(addressInfo.getRnRegAddress());
            List<MvFlAddress> mvFlAddresses = mvFlAddressRepository.getMvFlAddressByRnAddress(addressInfo.getRnRegAddress(), iin);
            List<MvFl> fls = new ArrayList<>();
            for (MvFlAddress ad : mvFlAddresses) {
                try {
                    Optional<MvFl> fl = mv_FlRepo.getByIin(ad.getIin());
                    if (fl.isPresent()) {
                        fls.add(fl.get());
                    }
                } catch (Exception e) {
                    MvFl obj = new MvFl();
                    obj.setIin(ad.getIin());
                    obj.setLast_name(ad.getFio());
                    fls.add(obj);
                }
            }
            List<SearchResultModelFL> result = findWithoutPhoto(fls);
            return result;
        }
        return new ArrayList<>();
    }

    public List<SearchResultModelUl> getByAddress(String bin) {
        RegAddressUlEntity addressUlEntity = regAddressUlEntityRepo.findByBin(bin);
        System.out.println(addressUlEntity.getRegAddrDistrictRu() + " " + addressUlEntity.getRegAddrStreetRu() + " " + addressUlEntity.getRegAddrBuildingNum());
        List<RegAddressUlEntity> units = regAddressUlEntityRepo.getByAddress(addressUlEntity.getRegAddrRegionRu(), addressUlEntity.getRegAddrDistrictRu(), addressUlEntity.getRegAddrStreetRu(), addressUlEntity.getRegAddrBuildingNum(),bin );
//        List<RegAddressUlEntity> units = regAddressUlEntityRepo.getByFullAddress(addressUlEntity.getRegAddrRegionRu(), addressUlEntity.getRegAddrDistrictRu(), addressUlEntity.getRegAddrLocalityRu(), addressUlEntity.getRegAddrStreetRu(), addressUlEntity.getRegAddrBuildingNum(), bin);

        List<SearchResultModelUl> list = new ArrayList<>();
        for (RegAddressUlEntity l: units) {
            if (l.getActive()) {
                Optional<MvUl> ul = mv_ul_repo.getUlByBin(l.getBin());
                if (ul.isPresent()) {
                    SearchResultModelUl res = new SearchResultModelUl();
                    res.setBin(ul.get().getBin());
                    res.setName(ul.get().getShort_name());
                    res.setRegion(addressUlEntity.getRegAddrRegionRu() + " " + addressUlEntity.getRegAddrDistrictRu() + " " + addressUlEntity.getRegAddrStreetRu() + " " + addressUlEntity.getRegAddrBuildingNum());
                    list.add(res);
                }
            }
        }
        return list;
    }

    private String createAdditionSQL(HashMap<String, String> req) {
        String sql = "select * from ser.mv_fl where first_name like '" + req.get("i").replace('$', '%') + "' and  patronymic like '" + req.get("o").replace('$', '%') + "' and last_name like '" + req.get("f").replace('$', '%') + "' ";
        if (req.get("dateFrom") != "") {
            sql = sql + "AND toDate(birth_date, 'YYYY-MM-DD') > toDate('" + req.get("dateFrom") + "', 'YYYY-MM-DD') ";
        }
        if (req.get("dateTo") != "") {
            sql = sql + "AND toDate(birth_date, 'YYYY-MM-DD') < toDate('" + req.get("dateTo") + "', 'YYYY-MM-DD') ";
        }
        if (req.get("gender") != "") {
            sql = sql + "AND gender = '" + req.get("gender") + "' ";
        }
        if (req.get("nation") != "") {
            sql = sql + "AND nationality_ru_name = '" + req.get("nation").toUpperCase() + "' ";
        }
        if (req.get("city") != "") {
            sql = sql + "AND district = '" + req.get("city").toUpperCase() + "' ";
        }
        if (req.get("country") != "") {
            sql = sql + "AND citizenship_ru_name = '" + req.get("country").toUpperCase() + "' ";
        }
        if (req.get("region") != "") {
            sql = sql + "AND region = '" + req.get("region").toUpperCase() + "' ";
        }
        if (req.get("region") != "") {
            sql = sql + "AND region = '" + req.get("region").toUpperCase() + "' ";
        }
        return sql;
    }
    public List<SearchResultModelFL> getByIIN_photo(String IIN) {
        List<MvFl> fls = mv_FlRepo.getUsersByLike(IIN);
        List<SearchResultModelFL> result = findWithPhoto(fls);
        return result;
    }
    public List<SearchResultModelFL> getByDocNumber_photo(String doc_number) {
        String iin = mvIinDocRepo.getIinByDoc_Number(doc_number);
        List<MvFl> fls = mv_FlRepo.getUsersByLike(iin);
        List<SearchResultModelFL> result = findWithPhoto(fls);
        return result;
    }

    public List<SearchResultModelFL> getByPhone(String phone) {
        List<String> iin = flContactsRepo.getByPhoneNumber(phone);
        List<MvFl> fls = new ArrayList<>();
        for (String ii: iin) {
            MvFl person = mv_FlRepo.getUserByIin(ii);
            fls.add(person);
        }
        List<SearchResultModelFL> result = findWithPhoto(fls);
        return result;
    }
    public List<SearchResultModelFL> getByEmail(String email) {
        List<String> iin = flContactsRepo.getByEmail(email);
        List<MvFl> fls = new ArrayList<>();
        for (String ii: iin) {
            MvFl person = mv_FlRepo.getUserByIin(ii);
            fls.add(person);
        }
        List<SearchResultModelFL> result = findWithPhoto(fls);
        return result;
    }
    public List<SearchResultModelFL> getByVinFl(String vin) {
        List<String> iin = mvAutoFlRepo.getByVin(vin);
        List<MvFl> fls = new ArrayList<>();
        for (String ii: iin) {
            MvFl person = mv_FlRepo.getUserByIin(ii);
            fls.add(person);
        }
        try {
            List<SearchResultModelFL> result = findWithPhoto(fls);
            return result;

        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }
    public List<SearchResultModelUl> getByVinUl(String vin) {
        String VIN_upper = vin.toUpperCase();
        List<String> iin = mvAutoFlRepo.getByVin(VIN_upper);
        List<SearchResultModelUl> list = new ArrayList<>();
        if (iin.size() > 0) {
            List<MvUl> mvUls = mv_ul_repo.getUsersByLike(iin.get(0));
            for (MvUl l: mvUls) {
                SearchResultModelUl res = new SearchResultModelUl();
                res.setBin(l.getBin());
                res.setName(l.getFull_name_rus());
                list.add(res);
            }
            return list;
        } else {
            return list;
        }

    }

    public List<SearchResultModelFL> getByDoc_photo(String doc) {
        List<MvIinDoc> fls = mv_iin_docRepo.getByDoc_number(doc);
        List<MvFl> fls1 = new ArrayList<>();
        for(MvIinDoc flss : fls){
            System.out.println(flss.getIin());
            fls1 = mv_FlRepo.getUsersByLike(flss.getIin());
        }
        List<SearchResultModelFL> result = findWithPhoto(fls1);
        return result;
    }

    public List<SearchResultModelFL> getByFIO_photo(String i, String o, String f) {
        List<MvFl> fls = mv_FlRepo.getUsersByFIO(i, o, f);

        List<SearchResultModelFL> result = findWithPhoto(fls);
        return result;
    }

    private List<SearchResultModelFL> findWithPhoto(List<MvFl> fls) {
        List<SearchResultModelFL> result = new ArrayList<>();
        for (MvFl fl: fls) {
            SearchResultModelFL person = new SearchResultModelFL();
            person.setFirst_name(fl.getFirst_name());
            person.setLast_name(fl.getLast_name());
            person.setPatronymic(fl.getPatronymic());
            person.setIin(fl.getIin());
            tryAddPhoto(person, fl.getIin());

            result.add(person);
        }
        return result;
    }
    private List<SearchResultModelFL> findWithoutPhoto(List<MvFl> fls) {
        List<SearchResultModelFL> result = new ArrayList<>();
        for (MvFl fl: fls) {
            SearchResultModelFL person = new SearchResultModelFL();
            person.setFirst_name(fl.getFirst_name());
            person.setLast_name(fl.getLast_name());
            person.setPatronymic(fl.getPatronymic());
            person.setIin(fl.getIin());

            result.add(person);
        }
        return result;
    }

    private SearchResultModelFL tryAddPhoto(SearchResultModelFL fl, String IIN) {
        try {
            Optional<PhotoDb> flRawPhoto = newPhotoRepo.findById(IIN);
            fl.setPhoto(flRawPhoto.get().getPhoto());
            return fl;
        } catch (Exception e) {
            System.out.println(e);
        }
        return fl;
    }
    private NodesFL tryAddPhoto(NodesFL node, String IIN) {
        try {
            List<PhotoDb> photos = new ArrayList<>();
            photos = newPhotoRepo.findAllByIinv(IIN);
            List<PhotoDb> photoDbs = new ArrayList<>();
            for(PhotoDb photoDb1: photos){
                photoDbs.add(photoDb1);
                node.setPhotoDbf(photoDbs);
            }
            return node;
        } catch (Exception e) {
            System.out.println(e);
        }
        return node;
    }
    private FlFirstRowDto tryAddPhoto(FlFirstRowDto fl, String IIN) {
        try {
            List<PhotoDb> photos = new ArrayList<>();
            photos = newPhotoRepo.findAllByIinv(IIN);
            List<PhotoDb> photoDbs = new ArrayList<>();
            for(PhotoDb photoDb1: photos){
                photoDbs.add(photoDb1);
                fl.setPhotoDbf(photoDbs);
            }
            return fl;
        } catch (Exception e) {
            System.out.println(e);
        }
        return fl;
    }
    private Children tryAddPhoto(Children hierarchy, String IIN) {
        try {
            List<PhotoDb> photos = new ArrayList<>();
            photos = newPhotoRepo.findAllByIinv(IIN);
            List<PhotoDb> photoDbs = new ArrayList<>();
            for(PhotoDb photoDb1: photos){
                photoDbs.add(photoDb1);
                hierarchy.image = Arrays.toString(photoDbs.get(0).getPhoto());
            }
            return hierarchy;
        } catch (Exception e) {
            System.out.println(e);
        }
        return hierarchy;
    }
    private Map<String, Object> getPropertyMap(Object obj) {
        Map<String, Object> properties = new HashMap<>();

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field: fields) {
            try {
                Object value = field.get(obj);
                properties.put(field.getName(), value);
            } catch (IllegalAccessException e){
//                e.printStackTrace();
            }
        }
        return properties;
    }

    //General info by iin
    public GeneralInfoDTO generalInfoByIin(String iin) {
        GeneralInfoDTO generalInfoDTO = new GeneralInfoDTO();
        int total = 13;
        int actual = 0;
        try {
            List<FlContacts> contacts = flContactsRepo.findAllByIin(iin);
            if (contacts != null) {
                actual++;
                generalInfoDTO.setContacts(contacts);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            List<AccountantListEntity> accountantListEntities = accountantListEntityRepo.getUsersByLike(iin);
            if (accountantListEntities != null) {
                actual++;

                generalInfoDTO.setAccountantListEntities(accountantListEntities);
                try {
                    for(AccountantListEntity accountantListEntity: accountantListEntities){
                        accountantListEntity.setBinName(mv_ul_repo.getNameByBin(accountantListEntity.getBin()));
                    }
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            List<AdvocateListEntity> advocateListEntities = advocateListEntityRepo.getUsersByLike(iin);
            if (advocateListEntities != null) {
                actual++;

                generalInfoDTO.setAdvocateListEntities(advocateListEntities);

            }
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            List<IpgoEmailEntity> ipgoEmailEntities = ipgoEmailEntityRepo.getUsersByLike(iin);
            if (ipgoEmailEntities != null) {
                actual++;

                generalInfoDTO.setIpgoEmailEntities(ipgoEmailEntities);

            }
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            List<AuditorsListEntity> auditorsListEntities = auditorsListEntityRepo.getUsersByLike(iin);
            if (auditorsListEntities != null) {
                actual++;

                generalInfoDTO.setAuditorsListEntities(auditorsListEntities);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            List<BailiffListEntity> bailiffListEntities = bailiffListEntityRepo.getUsersByLike(iin);
            if (bailiffListEntities != null) {
                actual++;

                generalInfoDTO.setBailiffListEntities(bailiffListEntities);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            List<MvUlFounderFl> mvUlFounderFls = mvUlFounderFlRepo.getUsersByLikeIIN(iin);
            if (mvUlFounderFls != null) {
                actual++;

                generalInfoDTO.setMvUlFounderFls(mvUlFounderFls);
            }
        } catch (Exception e) {
            System.out.println(e);
            System.out.println(e);
        }
        try {
            List<RegAddressFl> address = regAddressFlRepo.getByPermanentIin(iin);
            if(address != null) {
                AddressInfo addressInfo = new AddressInfo();
                if (address.size() > 0) {
                    actual++;

                    addressInfo.setRegion(address.get(0).getRegion());
                    addressInfo.setDistrict(address.get(0).getDistrict());
                    addressInfo.setCity(address.get(0).getCity());
                    addressInfo.setStreet(address.get(0).getStreet());
                    addressInfo.setBuilding(address.get(0).getBuilding());
                    addressInfo.setKorpus(address.get(0).getKorpus());
                    addressInfo.setApartment_number(address.get(0).getApartment_number());
                }

                List<RegAddressFl> units = regAddressFlRepo.getByAddress(addressInfo.getRegion(), addressInfo.getDistrict(), addressInfo.getCity(), addressInfo.getStreet(), addressInfo.getBuilding(), addressInfo.getKorpus(), addressInfo.getApartment_number());
                List<MvFl> fls = new ArrayList<>();
                for (RegAddressFl ad : units) {
                    Optional<MvFl> fl = mv_FlRepo.getByIin(ad.getIin());
                    if (fl.isPresent()) {
                        fls.add(fl.get());
                    }
                }
                List<SearchResultModelFL> result = findWithoutPhoto(fls);
                generalInfoDTO.setSameAddressFls(result);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            List<Lawyers> lawyers = lawyersRepo.getByIin(iin);

            if (lawyers != null) {
                actual++;

                generalInfoDTO.setLawyers(lawyers);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
//            List<ChangeFioDTO> changeFioDTOS = new ArrayList<>();
            List<ChangeFio> changeFio = changeFioRepo.getByIin(iin);
//            changeFio.forEach(x -> {
//                ChangeFioDTO obj = new ChangeFioDTO();
//                obj.setDateOfChange(x.getTo_date());
//                String name = "";
//                if (x.getSurname_before() != null) {
//                    name = x.getSurname_before() + " ";
//                }
//                if (x.getName_before() != null) {
//                    name = x.getName_before() + " ";
//                }
//                if (x.getSecondname_before() != null) {
//                    name = x.getSecondname_before();
//                }
//                obj.setHistoricalFIO(name);
//                obj.setReasonOfChange(x.getRemarks() != null ? x.getRemarks() : "");
//            });
            generalInfoDTO.setChangeFio(changeFio);
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            List<Pdl> pdls = pdlReposotory.getByIIN(iin);
            actual++;

            generalInfoDTO.setPdls(pdls);
        } catch (Exception e) {
            System.out.println(e);
        }
        Double percentage = Double.valueOf(actual * 100 / total);
        generalInfoDTO.setPercent(percentage);
        return generalInfoDTO;
    }


    public List<PensionListDTO> getPensionDetails(String iin, String bin, String year) {
        List<PensionListDTO> pensions = new ArrayList<>();
        List<Map<String, Object>> fl_pension_contrss = new ArrayList<>();
        fl_pension_contrss = flPensionContrRepo.getAllByCompanies(iin,bin, Integer.parseInt(year));


        for (Map<String, Object> pen : fl_pension_contrss) {
            PensionListDTO pensionListEntity = new PensionListDTO();
            pensionListEntity.setBin(bin);
            pensionListEntity.setName((String)fl_pension_contrss.get(0).get("P_NAME"));
            pensionListEntity.setPeriod(pen.get("pay_month").toString());
            pensionListEntity.setSum010((double)pen.get("AMOUNT"));

            pensions.add(pensionListEntity);
        }

        return pensions;
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

    //Additional Info by iin
    public AdditionalInfoDTO additionalInfoByIin(String iin) {
        AdditionalInfoDTO additionalInfoDTO = new AdditionalInfoDTO();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<MilitaryAccounting2Entity> militaryAccounting2Entities = MilitaryAccounting2Repo.getUsersByLike(iin);
            List<MilitaryAccountingDTO> militaryAccountingDTOS = new ArrayList<>();
            if(!militaryAccounting2Entities.isEmpty() & militaryAccounting2Entities.size() > 0){
            try {
                for(MilitaryAccounting2Entity militaryAccounting2Entity: militaryAccounting2Entities){
                    MilitaryAccountingDTO militaryAccountingDTO = objectMapper.convertValue(militaryAccounting2Entity, MilitaryAccountingDTO.class);
                    militaryAccountingDTO.setBinName(mv_ul_repo.getNameByBin(militaryAccountingDTO.getBin()));
                    militaryAccountingDTOS.add(militaryAccountingDTO);
                }
                additionalInfoDTO.setMilitaryAccounting2Entities(militaryAccountingDTOS);
            } catch (Exception e) {
            }
            }
        } catch (Exception e){
        }
        try {
            List<KX> kxes = kxRepo.getKxIin(iin);
            if (kxes != null) {

                additionalInfoDTO.setKxes(kxes);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            List<MvUlLeaderEntity> mvUlLeaders = mvUlLeaderEntityRepo.getUsersByLikeIin(iin);
            if (mvUlLeaders != null) {

                additionalInfoDTO.setUl_leaderList(mvUlLeaders);
            }
        } catch (Exception e) {
            System.out.println(e);
        }try {
            List<IndividualEntrepreneur> individualEntrepreneurs = individualEntrepreneurRepo.getByIin(iin);

            additionalInfoDTO.setIndividualEntrepreneurs(individualEntrepreneurs);
        }catch (Exception e){
            System.out.println(e);
        }
        try {
            List<MvRnOld> mvRnOlds = mv_rn_oldRepo.getUsersByLike(iin);
            List<MvRnOld> list = setNamesByBin(mvRnOlds);
            additionalInfoDTO.setMvRnOlds(list);
        } catch (Exception e){
            System.out.println("Error:" + e);
        }
        try {
            List<MvAutoFl> myMv_auto_fl =  mvAutoFlRepo.getUsersByLike(iin);
            try {
                additionalInfoDTO.setMvAutoFls(myMv_auto_fl);
            } catch (Exception e) {
                System.out.println("mv_auto_fl Error: " + e);
            }

        } catch (Exception e){
            System.out.println("mv_auto_fl WRAP Error:" + e);
        }
        try {
            List<Equipment> myEquipment =  equipment_repo.getUsersByLike(iin);
            additionalInfoDTO.setEquipment(myEquipment);
        } catch (Exception e){
            System.out.println("Error:" + e);
        }
        try {
            List<Trains> trains =  trainsRepo.getByIIN(iin);
            additionalInfoDTO.setTrains(trains);
        } catch (Exception e){
            System.out.println("Error:" + e);
        }
        try {
            List<WaterTransport> waterTransports =  waterTransportRepo.getWaterByIin(iin);
            additionalInfoDTO.setWaterTransports(waterTransports);
        } catch (Exception e){
            System.out.println("Error:" + e);
        }
        try {
            List<AutoTransport> autoTransports =  autoTransportRepo.getAutoByIin(iin);
            additionalInfoDTO.setAutoTransports(autoTransports);
        } catch (Exception e){
            System.out.println("Error:" + e);
        }
        try {
            List<Equipment> equipment =  equipment_repo.getUsersByLike(iin);
            additionalInfoDTO.setEquipment(equipment);
        } catch (Exception e){
            System.out.println("Error:" + e);
        }
        try {
            additionalInfoDTO.setAutoPostanovkas(autoPostanovkaRepo.getAutoPostanovkaByIin(iin));
        } catch (Exception e){
            System.out.println("Error:" + e);
        }
        try {
            additionalInfoDTO.setAutoSnyaties(autoSnyatieRepo.getAutoSnyatieByIin(iin));
        } catch (Exception e){
            System.out.println("Error:" + e);
        }
        try {
            List<AviaTransport> aviaTransports =  aviaTransportRepo.getAviaByIin(iin);
            additionalInfoDTO.setAviaTransports(aviaTransports);
        } catch (Exception e){
            System.out.println("Error:" + e);
        }
        try {
            additionalInfoDTO.setUniversities(uniRepo.getByIIN(iin));
        } catch (Exception e){
            System.out.println("Error:" + e);
        }

        try {
            additionalInfoDTO.setSchools(schoolRepo.getByIIN(iin));
        } catch (Exception e){
            System.out.println("Error:" + e);
        }try {
            additionalInfoDTO.setCommodityProducers(commodityProducerRepo.getiin_binByIIN(iin));
        } catch (Exception e){
            System.out.println("Error:" + e);
        }

        try {
            List<String> companyBins = flPensionContrRepo.getUsersByLikeCompany(iin);
        
            List<PensionListDTO> pensions = new ArrayList<>();
            List<PensionGroupDTO> result = new ArrayList<>();
            DecimalFormat df = new DecimalFormat("#");
            df.setMaximumFractionDigits(0);
            for (String bin : companyBins) {
                List<Map<String, Object>> fl_pension_contrss = new ArrayList<>();
                fl_pension_contrss = flPensionContrRepo.getAllByCompanies(iin,bin);
                PensionGroupDTO obj = new PensionGroupDTO();
                List<PensionListDTO> group = new ArrayList<>();
                String name = "";
                if (fl_pension_contrss.get(0).get("P_NAME") != null) {
                    name = (String)fl_pension_contrss.get(0).get("P_NAME") + ", ";
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
                    pensionListEntity.setName((String)fl_pension_contrss.get(0).get("P_NAME"));
                    pensionListEntity.setPeriod(year.replace(".0", ""));
                    try {
                        double knp010 = fl_pension_contrss.stream()
                            .filter(pension -> pension.get("pay_date").toString().equals(year) && pension.get("KNP").toString().equals("010"))
                            .mapToDouble(pension -> Double.parseDouble(pension.get("AMOUNT").toString()))
                            .sum();

                        pensionListEntity.setSum010(knp010);

                        knp010sum = knp010sum + knp010;

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

            additionalInfoDTO.setPensions(pensions);
            additionalInfoDTO.setNumber();
            additionalInfoDTO.setPensionsGrouped(result);
        } catch (Exception e){
            System.out.println("Error:" + e);
        }
        return additionalInfoDTO;
    }


    public FlRelativesLevelDto createHierarchyObject(String IIN) throws SQLException {
        List<MvFl> myMv_fl =  mv_FlRepo.getUsersByLike(IIN);
        Children hierarchy = new Children();
        hierarchy.name = myMv_fl.get(0).getIin();
        hierarchy.value = "MAIN";
        tryAddPhoto(hierarchy, IIN);
        List<FlRelativiesDTO> flRelativesDtos = new ArrayList<>();
        FlRelativesLevelDto nodes = new FlRelativesLevelDto();

        MvFl flRaw = mv_FlRepo.getUserByIin(IIN);

        if(flRaw.getIin()!=null){
            nodes.setIin(flRaw.getIin());
        }

        int mainQuintity = flRiskService.findFlRiskByIin(IIN).getQuantity();
        //--Основной ФЛ
        nodes.setName(IIN+", " + (flRaw.getLast_name()!=null?flRaw.getLast_name():"") +" " + (flRaw.getFirst_name()!=null?flRaw.getFirst_name():"") +" "
                + (flRaw.getPatronymic()!=null?flRaw.getPatronymic():"")+", Риски к-во: " + mainQuintity);

        if(mainQuintity!=0){
            nodes.setHaveRisk(true);
        } else{
            nodes.setHaveRisk(false);
        }
        int dirFounderQuantity = directorFounderService.getDirectorOrFounder(IIN).getQuantity();
        System.out.println(dirFounderQuantity);
        if(dirFounderQuantity!=0){
            nodes.setIsDirector(true);
        } else{
            nodes.setHaveRisk(false);
        }
        nodes.setFio((flRaw.getLast_name()!=null?flRaw.getLast_name():"") +" " + (flRaw.getFirst_name()!=null?flRaw.getFirst_name():"") +" "
                + (flRaw.getPatronymic()!=null?flRaw.getPatronymic():""));


        Optional<PhotoDb> flRawPhoto = newPhotoRepo.findById(IIN);

        if (flRawPhoto.isPresent()) {
            try {
                nodes.setPhoto(new SerialBlob(flRawPhoto.get().getPhoto()));
            } catch (SQLException e) {
            }
        }
        List<FlRelativesLevelDto> relativesNodes = new ArrayList<>();
        List<Object[]> flRelativesObj = fl_relativesRepository.findAllByIin(IIN);

        FlRelativiesDTO relativesDto = new FlRelativiesDTO();
        relativesDto.setParent_iin(nodes.getIin());
        relativesDto.setParent_fio(nodes.getFio());
        flRelativesDtos.add(relativesDto);

        //--1-Круг
        for (Object[] flRelObj:flRelativesObj.stream()
                .filter(objects -> objects[8].toString().equals("1"))
                .collect(Collectors.toList())){

            FlRelativesLevelDto firstLevel = new FlRelativesLevelDto();
            int firstLevelCnt = 0;
            int dirFounderQuantityfirstlvl = 0;
            firstLevel.setName(flRelObj[2]+", " + flRelObj[3] +" "+flRelObj[4] +" " +flRelObj[5] +", " + flRelObj[0]);

            firstLevel.setFio(flRelObj[3] +" "+flRelObj[4] +" " +flRelObj[5]);

            List<FlRelativesLevelDto> relativesNodesList = new ArrayList<>();

            if(flRelObj[2]!=null && !flRelObj[2].toString().equals("---")){

                if(flRelObj[2].toString().length()==12){
                    Optional<PhotoDb> flRawPhoto1Level = newPhotoRepo.findById(flRelObj[2].toString());
                    if (flRawPhoto1Level.isPresent()) {
                        try {
                            firstLevel.setPhoto(new SerialBlob(flRawPhoto1Level.get().getPhoto()));
                        } catch (SQLException e) {
                        }
                    }
                    firstLevelCnt = flRiskService.findFlRiskByIin(flRelObj[2].toString()).getQuantity();
                    dirFounderQuantityfirstlvl = directorFounderService.getDirectorOrFounder(flRelObj[2].toString()).getQuantity();

                }

                firstLevel.setIin(String.valueOf(flRelObj[2]));

                FlRelativiesDTO firstLvlDto = new FlRelativiesDTO();
                firstLvlDto.setParent_iin(firstLevel.getIin());
                firstLvlDto.setParent_fio(firstLevel.getFio());
                flRelativesDtos.add(firstLvlDto);

                //--2-Круг
                for (Object[] rel2Level:flRelativesObj.stream().filter(objects -> objects[8].toString().equals("2"))
                        .collect(Collectors.toList())){

                    if(flRelObj[2].toString().equals(rel2Level[1].toString())){
                        FlRelativesLevelDto secondLevel = new FlRelativesLevelDto();
                        List<FlRelativesLevelDto> relativesNodesList3Level = new ArrayList<>();
                        String relation="";
                        int secondLevelCnt =0;
                        int dirFounderQuantitysecondlvl = 0;
                        if(rel2Level[19]!=null){
                            relation=" ("+rel2Level[19]+")";
                        }
                        secondLevel.setName(rel2Level[2]+", "+rel2Level[3] +" "+rel2Level[4] +" " +rel2Level[5] +", " +rel2Level[0] +relation);
                        secondLevel.setFio(rel2Level[3] +" "+rel2Level[4] +" " +rel2Level[5]);

                        if(rel2Level[2].toString().length()==12){
                            Optional<PhotoDb> flRawPhoto2Level = newPhotoRepo.findById(rel2Level[2].toString());
                            if (flRawPhoto2Level.isPresent()) {
                                try {
                                    secondLevel.setPhoto(new SerialBlob(flRawPhoto2Level.get().getPhoto()));
                                } catch (SQLException e) {
                                }
                            }
                            secondLevelCnt = flRiskService.findFlRiskByIin(rel2Level[2].toString()).getQuantity();
                            dirFounderQuantitysecondlvl = directorFounderService.getDirectorOrFounder(rel2Level[2].toString()).getQuantity();
                        }

                        if(secondLevelCnt!=0){
                            secondLevel.setHaveRisk(true);
                        } else{
                            secondLevel.setHaveRisk(false);
                        }
                        if(dirFounderQuantitysecondlvl!=0){
                            secondLevel.setIsDirector(true);
                        } else{
                            secondLevel.setIsDirector(false);
                        }
                        secondLevel.setName(secondLevel.getName() + ", Риски к-во: " + secondLevelCnt);


                        if(rel2Level[2]!=null && !rel2Level[2].toString().equals("---")){
                            secondLevel.setIin(String.valueOf(rel2Level[2]));

                            //--3 - Круг
                            for (Object[] rel3Level:flRelativesObj.stream().filter(objects -> objects[8].toString().equals("3"))
                                    .collect(Collectors.toList())){

                                if(rel2Level[2].toString().equals(rel3Level[1].toString())) {
                                    FlRelativesLevelDto thirdLevel = new FlRelativesLevelDto();
                                    String relation3Level="";
                                    if(rel3Level[19]!=null){
                                        relation3Level=" ("+rel3Level[19]+")";
                                    }
                                    int thirdLevelCnt =0;
                                    int dirFounderQuantitythirdlvl =0;
                                    thirdLevel.setFio(rel3Level[3] +" "+rel3Level[4] +" " +rel3Level[5]);

                                    if(rel3Level[2]!=null && !rel3Level[2].toString().equals("---")){
                                        thirdLevel.setIin(String.valueOf(rel3Level[2]));

                                        Optional<PhotoDb> flRawPhoto3Level = newPhotoRepo.findById(rel3Level[2].toString());
                                        if (flRawPhoto3Level.isPresent()) {
                                            try {
                                                thirdLevel.setPhoto(new SerialBlob(flRawPhoto3Level.get().getPhoto()));
                                            } catch (SQLException e) {
                                            }
                                        }

                                        thirdLevelCnt = flRiskService.findFlRiskByIin(rel3Level[2].toString()).getQuantity();
                                        dirFounderQuantitythirdlvl = directorFounderService.getDirectorOrFounder(rel3Level[2].toString()).getQuantity();
                                    }

                                    if(thirdLevelCnt!=0){
                                        thirdLevel.setHaveRisk(true);
                                    } else{
                                        thirdLevel.setHaveRisk(false);
                                    }
                                    if(dirFounderQuantitythirdlvl!=0){
                                        thirdLevel.setIsDirector(true);
                                    } else{
                                        thirdLevel.setIsDirector(false);
                                    }
                                    thirdLevel.setName(rel3Level[2]+", " + rel3Level[3] +" "+rel3Level[4] +" " +rel3Level[5] + ", " +rel3Level[0] + relation3Level +", Риски к-во: " + thirdLevelCnt);
                                    if(!thirdLevel.getFio().equals("null null null")){

                                        FlRelativiesDTO thirdLevelDto = new FlRelativiesDTO();
                                        thirdLevelDto.setParent_iin(thirdLevelDto.getIin());
                                        thirdLevelDto.setParent_fio(thirdLevelDto.getFio());
//                                           thirdLevelDto.setRelativeBirthDate(getBirthDate(relation3Level[6]));

                                        boolean isRel = true;

                                        if(!flRelativesDtos.isEmpty()){
                                            for(FlRelativiesDTO rel: flRelativesDtos){
                                                if((rel.getParent_iin()!=null && thirdLevelDto.getIin()!=null) && rel.getParent_iin().equals(thirdLevelDto.getIin())){
                                                    isRel=false;
                                                    break;
                                                }

                                                if((rel.getParent_fio()!=null && thirdLevelDto.getFio()!=null) && ( rel.getParent_fio().equals(thirdLevelDto.getFio()) )){
                                                    isRel=false;
                                                    break;
                                                }
                                            }
                                        }

                                        if(isRel){
                                            flRelativesDtos.add(thirdLevelDto);
                                            relativesNodesList3Level.add(thirdLevel);
                                        }
                                    }
                                }

                            }
                        }

                        secondLevel.setChildren(relativesNodesList3Level);
                        if(!secondLevel.getFio().equals("null null null")){

                            boolean isRel = true;

                            FlRelativiesDTO secondLvlDto = new FlRelativiesDTO();
                            secondLvlDto.setParent_iin(secondLevel.getIin());
                            secondLvlDto.setParent_fio(secondLevel.getFio());
//                            secondLvlDto.setBirthDate(getBirthDate(rel2Level[6]));
                            if(!flRelativesDtos.isEmpty()){
                                for(FlRelativiesDTO rel: flRelativesDtos){
                                    if((rel.getParent_iin()!=null && secondLevel.getIin()!=null) && rel.getParent_iin().equals(secondLevel.getIin())){
                                        isRel=false;
                                        break;
                                    }

                                    if((rel.getParent_fio()!=null && secondLevel.getFio()!=null) && ( rel.getParent_fio().equals(secondLevel.getFio()) )){
                                        isRel=false;
                                        break;
                                    }
                                }
                            }

                            if(isRel){
                                flRelativesDtos.add(secondLvlDto);
                                relativesNodesList.add(secondLevel);
                            }
                        }
                    }
                }
            }

            if(firstLevelCnt!=0){
                firstLevel.setHaveRisk(true);
            } else{
                firstLevel.setHaveRisk(false);
            }
            if(dirFounderQuantityfirstlvl!=0){
                firstLevel.setIsDirector(true);
            } else{
                firstLevel.setIsDirector(false);
            }
            firstLevel.setName(firstLevel.getName() + ", Риски к-во: " + firstLevelCnt);

            firstLevel.setChildren(relativesNodesList);

            if(!firstLevel.getFio().equals("null null null")){
                relativesNodes.add(firstLevel);
            }
        }
        nodes.setChildren(relativesNodes);
//            return nodes;
//        }
        return nodes;
    }
    public NodesFL getNode(String IIN){
        NodesFL myNode = new NodesFL();
        try {
            List<MvUlLeader> mv_ul_leaders =  mvUlLeaderRepository.findAllByIin(IIN);
            try {
                myNode.setUl_leaderList(mv_ul_leaders);
            } catch (Exception e) {
                System.out.println("mv_ul_leader Error: " + e);
            }
        } catch (Exception e){
            System.out.println("mv_ul_leader WRAP Error:" + e);
        }
        try {
            List<BeneficiariesList> beneficiariesLists =  beneficiariesListRepo.getBenByIIN(IIN);
            try {
                myNode.setBeneficiariesLists(beneficiariesLists);
            } catch (Exception e) {
                System.out.println("dismissals Error: " + e);
            }
        } catch (Exception e){
            System.out.println("dismissals WRAP Error:" + e);
        }
        try {
            List<ImmoralLifestyle> immoralLifestyles =  immoral_lifestlyeRepo.getImmoByIIN(IIN);
            try {
                myNode.setAmoral(immoralLifestyles);
            } catch (Exception e) {
                System.out.println("immoralLifestyles Error: " + e);
            }
        } catch (Exception e){
            System.out.println("immoralLifestyles WRAP Error:" + e);
        }try {
            List<DrugAddicts> drugAddicts =  drugAddictsRepo.getDrugAddictsByIIN(IIN);
            try {
                myNode.setDrugAddicts(drugAddicts);
            } catch (Exception e) {
                System.out.println("immoralLifestyles Error: " + e);
            }
        } catch (Exception e){
            System.out.println("immoralLifestyles WRAP Error:" + e);
        }try {
            List<Incapacitated> incapacitateds =  incapacitatedRepo.getIncapacitatedByIIN(IIN);
            try {
                myNode.setIncapacitateds(incapacitateds);
            } catch (Exception e) {
                System.out.println("incapacitateds Error: " + e);
            }
        } catch (Exception e){
            System.out.println("incapacitateds WRAP Error:" + e);
        }try {
            List<MvFlAddress> mvFlAddresses =  mvFlAddressRepository.getMvFlAddressByIIN(IIN);
            try {
                myNode.setMvFlAddresses(mvFlAddresses);
            } catch (Exception e) {
                System.out.println("mvFlAddresses Error: " + e);
            }
        } catch (Exception e){
            System.out.println("mvFlAddresses WRAP Error:" + e);
        }try {
            List<RegistrationTemp> registrationTemps =  registrationTempRepository.getRegAddressByIIN(IIN);
            try {
                myNode.setRegistrationTemps(registrationTemps);
            } catch (Exception e) {
                System.out.println("registrationTemps Error: " + e);
            }
        } catch (Exception e){
            System.out.println("registrationTemps WRAP Error:" + e);
        }try {
            List<Dismissal> dismissals =  dismissalRepo.getDismissalByIIN(IIN);
            try {
                myNode.setDismissals(dismissals);
            } catch (Exception e) {
                System.out.println("incapacitateds Error: " + e);
            }
        } catch (Exception e){
            System.out.println("incapacitateds WRAP Error:" + e);
        }try {
            List<ConvictsAbroad> convictsAbroads =  convictsAbroadRepo.getConvictsAbroadByIIN(IIN);
            try {
                myNode.setConvictsAbroads(convictsAbroads);
            } catch (Exception e) {
                System.out.println("immoralLifestyles Error: " + e);
            }
        } catch (Exception e){
            System.out.println("immoralLifestyles WRAP Error:" + e);
        }
        try {
            List<MvAutoFl> myMv_auto_fl =  mvAutoFlRepo.getUsersByLike(IIN);
            try {
                myNode.setMvAutoFls(myMv_auto_fl);
            } catch (Exception e) {
                System.out.println("mv_auto_fl Error: " + e);
            }
        } catch (Exception e){
            System.out.println("mv_auto_fl WRAP Error:" + e);
        }
        try {
            List<MvFl> myMv_fl =  mv_FlRepo.getUsersByLike(IIN);
            try {
                myNode.setMvFls(myMv_fl);
            } catch (Exception e) {
                System.out.println("mv_fl Error: " + e);
            }
        } catch (Exception e){
            System.out.println("mv_fl WRAP Error:" + e);
        }
        try {
            List<Kuis> kuis =  kuisRepo.getKuisByIIN(IIN);
            try {
                myNode.setKuis(kuis);
            } catch (Exception e) {
                System.out.println("kuis Error: " + e);
            }
        } catch (Exception e){
            System.out.println("kuis WRAP Error:" + e);
        }

        try {
            List<Orphans> myOrphans =  orphans_repo.getUsersByLike(IIN);
            try {
                myNode.setOrphans(myOrphans);
            } catch (Exception e) {
                System.out.println("orphans Error: " + e);
            }
        } catch (Exception e){
            System.out.println("orphans WRAP Error:" + e);
        }
        try {
            List<MzEntity> mzEntities =  mzEntityRepo.getMzByIIN(IIN);
            try {
                myNode.setMzEntities(mzEntities);
            } catch (Exception e) {
                System.out.println("orphans Error: " + e);
            }
        } catch (Exception e){
            System.out.println("orphans WRAP Error:" + e);
        }
        try {
            List<Dormant> myDormant =  dormantRepo.getUsersByLike(IIN);
            try {
                myNode.setDormants(myDormant);
            } catch (Exception e) {
                System.out.println("dormant Error: " + e);
            }
        } catch (Exception e){
            System.out.println("dormant WRAP Error:" + e);
        }
        try {
            List<MilitaryAccounting2Entity> militaryAccounting2Entities = MilitaryAccounting2Repo.getUsersByLike(IIN);
            try {
                myNode.setMilitaryAccounting2Entities(militaryAccounting2Entities);
            } catch (Exception e) {
                System.out.println("MilitaryAccounting2Entity Error: " + e);
            }
        } catch (Exception e){
            System.out.println("MilitaryAccounting2Entity WRAP Error:" + e);
        }
        try {
            List<MvRnOld> mvRnOlds = mv_rn_oldRepo.getUsersByLike(IIN);
            myNode.setMvRnOlds(mvRnOlds);
        } catch (Exception e){
            System.out.println("Error:" + e);
        }
        try {
            List<Equipment> myEquipment =  equipment_repo.getUsersByLike(IIN);
            myNode.setEquipment(myEquipment);
        } catch (Exception e){
            System.out.println("Error:" + e);
        }
        try {
            List<RegAddressFl> addressFls = regAddressFlRepo.getByIIN(IIN);
            myNode.setRegAddressFls(addressFls);
        } catch (Exception e){
            System.out.println("Error:" + e);
        }
        try {
            List<String> flPensionContrs = flPensionContrRepo.getUsersByLikeCompany(IIN);
            System.out.println(flPensionContrs);

            List<FlPensionFinal> flPensionFinals = new ArrayList<>();
            for(String flPension : flPensionContrs){
                FlPensionFinal flPensionFinal = new FlPensionFinal();
                List<Map<String, Object>> fl_pension_contrss = new ArrayList<>();
                fl_pension_contrss = flPensionContrRepo.getAllByCompanies(IIN,flPension);
                List<Map<String, Object>> r = flPensionContrRepo.findAmountOfAmountByKNP(IIN,flPension);
                List<String> fff = flPensionMiniRepo.getAllByCompaniesYear(IIN,flPension);
                flPensionFinal.setFlPensionMinis(fl_pension_contrss);
                flPensionFinal.setNakoplenya(r);
                flPensionFinal.setYears(fff);
                flPensionFinal.setCompanyBin(flPension);
                flPensionFinals.add(flPensionFinal);
//            System.out.println(findAmountOfAmountByKNPf);
            }
            myNode.setFlPensionContrs(flPensionFinals);
        } catch (Exception e){
            System.out.println("Error:" + e);
        }
        try {
            List<Msh> mshes = mshRepo.getUsersByLike(IIN);
            myNode.setMshes(mshes);
        } catch (Exception e){
            System.out.println("Error:" + e);
        }
        try {
            List<IpgoEmailEntity> ipgoEmailEntities = IpgoEmailEntityRepo.getUsersByLike(IIN);
            myNode.setIpgoEmailEntities(ipgoEmailEntities);
        } catch (Exception e){
            System.out.println("Error:" + e);
        }
        try {
            List<TIpEntity> TIpEntity = TIpEntityRepo.getUsersByLike(IIN);
            try {
                myNode.setTIpEntity(TIpEntity);
            } catch (Exception e) {
                System.out.println("TIpEntity Error: " + e);
            }
        } catch (Exception e){
            System.out.println("TIpEntity WRAP Error:" + e);
        }
        try {
            List<AccountantListEntity> accountantListEntities = accountantListEntityRepo.getUsersByLike(IIN);
            try {
                myNode.setAccountantListEntities(accountantListEntities);
            } catch (Exception e) {
                System.out.println("AccountantListEntity Error: " + e);
            }
        } catch (Exception e){
            System.out.println("AccountantListEntity WRAP Error:" + e);
        }
        try {
            List<AdvocateListEntity> advocateListEntities = advocateListEntityRepo.getUsersByLike(IIN);
            myNode.setAdvocateListEntities(advocateListEntities);
        } catch (Exception e){
            System.out.println("Error:" + e);
        }
        try {
            List<AuditorsListEntity> auditorsListEntities = auditorsListEntityRepo.getUsersByLike(IIN);
            myNode.setAuditorsListEntities(auditorsListEntities);
        } catch (Exception e){
            System.out.println("Error:" + e);
        }
        try {
            List<BailiffListEntity> bailiffListEntities = bailiffListEntityRepo.getUsersByLike(IIN);
            myNode.setBailiffListEntities(bailiffListEntities);
        } catch (Exception e){
            System.out.println("Error:" + e);
        }
        try {
            List<BlockEsf> blockEsfs = block_esfRepo.getblock_esfByIIN(IIN);
            myNode.setBlockEsfs(blockEsfs);
        } catch (Exception e){
            System.out.println("Error:" + e);
        }
        try {
            List<MvUlFounderFl> mvUlFounderFls = mvUlFounderFlRepo.getUsersByLikeIIN(IIN);
            myNode.setMvUlFounderFls(mvUlFounderFls);
        } catch (Exception e){
            System.out.println("Error:" + e);
        }
        try {
            List<NdsEntity> ndsEntities = ndsEntityRepo.getUsersByLike(IIN);
            myNode.setNdsEntities(ndsEntities);
        } catch (Exception e){
            System.out.println("Error:" + e);
        }
        try {
            List<CommodityProducer> commodityProducers = commodityProducerRepo.getiin_binByIIN(IIN);
            myNode.setCommodityProducers(commodityProducers);
        } catch (Exception e){
            System.out.println("Error:" + e);
        }
        try {
            myNode.setContacts(flContactsRepo.findAllByIin(IIN));
        } catch (Exception e){
            System.out.println("Error:" + e);
        }
        try {
            myNode.setPdls(pdlReposotory.getByIIN(IIN));
        } catch (Exception e){
            System.out.println("Error:" + e);
        }
        try {
            myNode.setMvIinDocs(mvIinDocRepo.getByIIN(IIN));
        } catch (Exception e){
            System.out.println("Error:" + e);
        }
        try {
            myNode.setUniversities(uniRepo.getByIIN(IIN));
        } catch (Exception e){
            System.out.println("Error:" + e);
        }
        try {
            myNode.setSchools(schoolRepo.getByIIN(IIN));
        } catch (Exception e){
            System.out.println("Error:" + e);
        }
//        try {
//            myNode.setMillitaryAccounts(militaryAccountRepo.findAllByIin(IIN));
//        } catch (Exception e){
//            System.out.println("Error:" + e);
//        }
//        List<flPensionMini> flPensionContrs1 = new ArrayList<>();
//        List<String> CompanyNames = flPensionContrRepo.getUsersByLikeCompany(IIN);
        myNode = tryAddPhoto(myNode,IIN);
        return myNode;
    }




    public List<SearchResultModelUl> searchResultUl(String bin) {
        List<MvUl> mvUls = mv_ul_repo.getUsersByLike(bin);
        List<SearchResultModelUl> list = new ArrayList<>();
        for (MvUl l: mvUls) {
            SearchResultModelUl res = new SearchResultModelUl();
            res.setBin(l.getBin());
            res.setName(l.getShort_name());
            list.add(res);
        }

        return list;
    }
    public List<TaxOutEntity> taxOutEntities(String bin, PageRequest pageRequest){
        Page<TaxOutEntity> taxOutEntityPage = taxOutEntityRepo.getUsersByLike(bin,pageRequest);
        return taxOutEntityPage.getContent();
    }
    public List<Map<String, Object>> pensionEntityUl(String bin, String year, PageRequest pageRequest){
        Page<Map<String,Object>> pens = flPensionContrRepo.getPension(bin, year, pageRequest);
        return pens.getContent();
    }
    public List<Map<String,Object>> pensionEntityUl1(String bin, Double year, Integer page){
        Integer offset = page * 10;
        List<Map<String,Object>> pens = flPensionContrRepo.getPension1(bin, year, offset);
        return pens;
    }



    public NodesUL getNodeUL(String BIN) {
        NodesUL myNode = new NodesUL();
        try {
            List<MvUlFounderFl> mvUlFounderFls = mvUlFounderFlRepo.getUsersByLike(BIN);
            myNode.setMvUlFounderFls(mvUlFounderFls);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        try {
            List<Bankrot> bankrots = bankrotRepo.getbankrotByByIIN(BIN);
            myNode.setBankrots(bankrots);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        try {
            List<MvUl> mvUls = mv_ul_repo.getUsersByLike(BIN);
            myNode.setMvUls(mvUls);
            for(MvUl mv_ul : mvUls){
                mv_ul.setFull_name_rus(mv_ul.getShort_name());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        try {
            myNode.setFl_contacts(flContactsRepo.findAllByIin(BIN));
        } catch (Exception e) {
            System.out.println("Error:" + e);
        }
        try {
            List<Adm> MyAdm = admRepo.getUsersByLikeBin(BIN);
            myNode.setAdms(MyAdm);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        try {
            List<Dormant> myDormant = dormantRepo.getUsersByLike(BIN);
            myNode.setDormants(myDormant);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        try {
            RegAddressUlEntity address = regAddressUlEntityRepo.findByBin(BIN);
            myNode.setRegAddressUlEntities(address);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        try {
            List<Equipment> myEquipment = equipment_repo.getUsersByLike(BIN);
            myNode.setEquipment(myEquipment);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        try {
            List<Omn> myOmns = omn_repos.getUsersByLikeIin_bin(BIN);
            myNode.setOmns(myOmns);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        try {
            List<Msh> mshes = mshRepo.getUsersByLike(BIN);
            myNode.setMshes(mshes);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        try {
            List<Criminals> criminals = criminalsRepo.getcriminalsByByIIN(BIN);
            myNode.setCriminals(criminals);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        try {
            List<BlockEsf> blockEsfs = block_esfRepo.getblock_esfByIIN(BIN);
            myNode.setBlockEsfs(blockEsfs);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        try {
            List<OpgEntity> opgEntities = opgRepo.getopgByIIN(BIN);
            myNode.setOpgEntities(opgEntities);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        try {
            List<AccountantListEntity> accountantListEntities = accountantListEntityRepo.getUsersByLikeBIN(BIN);
            myNode.setAccountantListEntities(accountantListEntities);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        try {
            List<NdsEntity> ndsEntities = ndsEntityRepo.getUsersByLike(BIN);
            myNode.setNdsEntities(ndsEntities);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        try {
            List<FpgTempEntity> fpgTempEntities = fpgTempEntityRepo.getUsersByLike(BIN);
            myNode.setFpgTempEntities(fpgTempEntities);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        try {
            List<Pdl> pdls = pdlReposotory.getByBin(BIN);
            myNode.setPdls(pdls);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        try {
            List<QoldauSubsidy> q = QoldauRepo.getByIIN(BIN);
            myNode.setQoldauSubsidy(q);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        try {
            List<CommodityProducer> commodityProducers = commodityProducerRepo.getiin_binByIIN(BIN);
            myNode.setCommodityProducers(commodityProducers);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        try {
            List<MvRnOld> mvRnOlds = mv_rn_oldRepo.getUsersByLike(BIN);
            myNode.setMvRnOlds(mvRnOlds);
        } catch (Exception e) {
            System.out.println("Error: " + e);}
            try {
                RegAddressUlEntity address = regAddressUlEntityRepo.findByBin(BIN);
                RegAddressUlEntity setRegUlNaOdnom = regAddressUlEntityRepo.regAddressNaOdnomMeste(address.getRegAddrRegionRu(), address.getRegAddrDistrictRu()
                        , address.getRegAddrLocalityRu(), address.getRegAddrStreetRu(), address.getRegAddrBuildingNum(), BIN);
                myNode.setRegUlNaOdnomMeste(setRegUlNaOdnom);
                System.out.println(address.getRegAddrRegionKz() + " " + address.getRegAddrDistrictKz() + " " + address.getRegAddrBuildingNum() );
            } catch (Exception e) {
                System.out.println("Error: " + e);

//         }try {
//             List<TaxOutEntity> taxOutEntitiest = taxOutEntityRepo.getUsersByLike(BIN);
//             myNode.setTaxOutEntities(taxOutEntitiest);
//         } catch (Exception e) {
//             System.out.println("Error: " + e);
            }
            try {
                List<MvAutoFl> mvAutoFls = mvAutoFlRepo.getUsersByLike(BIN);
                myNode.setMvAutoFls(mvAutoFls);
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }

            List<MvUlFounderUl> mvUlFounderUls = mvUlFounderUlRepo.getUsersByLike(BIN);
            List<SvedenyaObUchastnikovUlEntity> svedenyaObUchastnikovUlEntities = new ArrayList<>();
            for (MvUlFounderUl mvUlFUl : mvUlFounderUls) {
                SvedenyaObUchastnikovUlEntity svedenyaObUchastnikovUlEntity = new SvedenyaObUchastnikovUlEntity();
                svedenyaObUchastnikovUlEntity.setIin_bin(mvUlFUl.getFounderBin());
                svedenyaObUchastnikovUlEntity.setFIOorUlName(mvUlFUl.getFounderNameRu());
                svedenyaObUchastnikovUlEntity.setReg_date(mvUlFUl.getRegDate());
                if (mvUlFUl.isCurrent()) {
                    svedenyaObUchastnikovUlEntity.setIdentificator("Учредитель ЮЛ");
                } else {
                    svedenyaObUchastnikovUlEntity.setIdentificator("Учредитель ЮЛ (исторический)");
                }
                svedenyaObUchastnikovUlEntities.add(svedenyaObUchastnikovUlEntity);
            }
            List<MvUlLeaderEntity> mvUlLeaderEntities = mvUlLeaderEntityRepo.getUsersByLike(BIN);
            for (MvUlLeaderEntity mvUlFUl : mvUlLeaderEntities) {
                SvedenyaObUchastnikovUlEntity svedenyaObUchastnikovUlEntity = new SvedenyaObUchastnikovUlEntity();
                svedenyaObUchastnikovUlEntity.setIin_bin(mvUlFUl.getIin());
                svedenyaObUchastnikovUlEntity.setFIOorUlName(mvUlFUl.getLastName() + " " + mvUlFUl.getFirstName() + " " + mvUlFUl.getPatronymic());
                svedenyaObUchastnikovUlEntity.setReg_date(mvUlFUl.getRegDate());
                if (mvUlFUl.getCurrent() == true) {
                    svedenyaObUchastnikovUlEntity.setIdentificator("Директор");
                } else {
                    svedenyaObUchastnikovUlEntity.setIdentificator("Директор (исторический)");
                }
                svedenyaObUchastnikovUlEntities.add(svedenyaObUchastnikovUlEntity);

            }
            List<Map<String, Object>> r = flPensionContrRepo.findAmountOfEmployeesOfEveryYear(BIN);
            myNode.setPensionYearAndEmpNum(r);
            myNode.setSvedenyaObUchastnikovUlEntities(svedenyaObUchastnikovUlEntities);
            try {
                if (myNode.getOmns().size() == 0
                        & myNode.getBankrots().size() == 0
                        & myNode.getAdms().size() == 0
                        & myNode.getOpgEntities().size() == 0
                        & myNode.getCriminals().size() == 0
                        & myNode.getBlockEsfs().size() == 0
                        & myNode.getFpgTempEntities().size() == 0) {
                    myNode.setPerson_with_risk(false);
                } else {
                    myNode.setPerson_with_risk(true);
                }
            }catch (Exception e){
                System.out.println("ne poluchiolos");
            }

//         List<FL_PENSION_FINAL> flPensionFinals = new ArrayList<>();
//         FL_PENSION_FINAL flPensionFinal = new FL_PENSION_FINAL();
//         flPensionFinal.setNakoplenya(flPensionContrRepo.findAmountOfEmployeesOfEveryYear(BIN));
//         flPensionFinals.add(flPensionFinal);
//         myNode.setFlPensionContrs(flPensionFinals);

//         for(String flPension : flPensionContrs){
//             List<flPensionMini> fl_pension_contrss = new ArrayList<>();
//             fl_pension_contrss = flPensionMiniRepo.getAllByCompanies(IIN,flPension);
//             List<String> fff = flPensionMiniRepo.getAllByCompaniesYear(IIN,flPension);
////            System.out.println(flPensionContrRepo.findAmountOfAmountByKNP(IIN,flPension));
////            Object findAmountOfAmountByKNPf = flPensionContrRepo.findAmountOfAmountByKNP(IIN,flPension);
////            System.out.printf(String.valueOf(findAmountOfAmountByKNPf.getClass().getName()));
//             flPensionFinal.setFlPensionMinis(fl_pension_contrss);
//             flPensionFinal.setNakoplenya(r);
//             flPensionFinal.setYears(fff);
//             flPensionFinal.setCompanyBin(flPension);
//             flPensionFinals.add(flPensionFinal);
////            System.out.println(findAmountOfAmountByKNPf);
//         }
//         myNode.setFlPensionContrs(flPensionFinals);
//         List<TaxOutEntity> taxOutEntities = taxOutEntityRepo.getUsersByLike(BIN);
            //     myNode.setTaxOutEntities(taxOutEntities);
//         List<FL_PENSION_FINAL> flPensionFinals = new ArrayList<>();
//         List<Integer> adad = flPensionContrRepo.amountOfYears(BIN);
//         for(Integer add : adad){
//             FL_PENSION_FINAL flPensionFinal = new FL_PENSION_FINAL();
//             System.out.println(add);
//             flPensionFinal.setAmountOfEmp(flPensionContrRepo.amountOfEmp(BIN,add));

//             flPensionFinal.setNakoplenya(r);
//             flPensionFinal.setYear(add);
//             flPensionFinals.add(flPensionFinal);
//         }
//         myNode.setFlPensionContrs(flPensionFinals);
        try {
            Integer number = taxOutEntityRepo.getTaxAmount(BIN);
            myNode.setTaxCount(number);
        } catch (Exception e) {
            System.out.println("Tax error: " + e);
        }
            return myNode;
        }


        public FlFirstRowDto getFlFirstRow(String IIN){
            FlFirstRowDto flFirstRowDto = new FlFirstRowDto();
            try {
                List<MvFl> myMv_fl =  mv_FlRepo.getUsersByLike(IIN);
                try {
                    flFirstRowDto.setMvFls(myMv_fl);
                } catch (Exception e) {
                    System.out.println("mv_fl Error: " + e);
                }
            } catch (Exception e){
                System.out.println("mv_fl WRAP Error:" + e);
            }try {
                List<MvIinDoc> mvIinDocs =  mvIinDocRepo.getByIIN(IIN);
                try {
                    flFirstRowDto.setMvIinDocList(mvIinDocs);
                } catch (Exception e) {
                    System.out.println("mvIinDocs Error: " + e);
                }
            } catch (Exception e){
                System.out.println("mvIinDocs WRAP Error:" + e);
            }try {
                FLRiskDto flRiskDto =  flRiskService.findFlRiskByIin(IIN);
                try {
                    flFirstRowDto.setRiskPercentage(flRiskDto.getPercentage());
                } catch (Exception e) {
                    System.out.println("mvIinDocs Error: " + e);
                }
            } catch (Exception e){
                System.out.println("mvIinDocs WRAP Error:" + e);
            }try {
                List<MvFlAddress> mvFlAddresses = mvFlAddressRepository.getMvFlAddressByIIN(IIN);
                flFirstRowDto.setMvFlAddresses(mvFlAddresses);
            } catch (Exception e){
                System.out.println("Error:" + e);
            }try {
                List<RegistrationTemp> registrationTemps = registrationTempRepository.getRegAddressByIIN(IIN);
                flFirstRowDto.setRegistrationTemps(registrationTemps);
            } catch (Exception e){
                System.out.println("Error:" + e);
            }
            flFirstRowDto = tryAddPhoto(flFirstRowDto,IIN);

            return flFirstRowDto;
        }

    }
