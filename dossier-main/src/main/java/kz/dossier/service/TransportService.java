package kz.dossier.service;

import kz.dossier.dto.AutoTransportDto;
import kz.dossier.dto.EquipmentDto;
import kz.dossier.dto.MvAutoDto;
import kz.dossier.modelsDossier.*;
import kz.dossier.repositoryDossier.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransportService {
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

    public Integer countEquipments(String bin) {
        return equipment_repo.countByBin(bin);
    }
    public Integer countTransport(String bin) {
        return mvAutoFlRepo.countByBin(bin);
    }
    public Integer countAutoTransport(String bin) {
        return autoTransportRepo.countByBin(bin);
    }
    public Integer countTrains(String bin) {
        return trainsRepo.countByBin(bin);
    }
    public Integer countAviaTransport(String bin) {
        return aviaTransportRepo.countByBin(bin);
    }
    public Integer countWaterTransport(String bin) {
        return waterTransportRepo.countByBin(bin);
    }

    public List<Equipment> getEquipmentByIin(String iin) {
        List<Equipment> equipment = equipment_repo.getUsersByLike(iin);

        if (!equipment.isEmpty()) {
            return equipment;
        } else {
            return null;
        }
    }
    public List<EquipmentDto> getEquimpentByBin(String bin) {
        List<Equipment> equipments = equipment_repo.getUsersByLike(bin);
        List<EquipmentDto> result = new ArrayList<>();
        for (Equipment equipment : equipments) {
            if (equipment  == null) {
                continue;
            }
            EquipmentDto dto = new EquipmentDto();
            String name = "/ Специализация: ";
            if (equipment.getEquipment_spec() != null) {
                name += equipment.getEquipment_spec();
            }
            if (equipment.getEquipment_type() != null) {
                name += "/ Тип: " + equipment.getEquipment_type();
            }
            if (equipment.getEquipment_form() != null) {
                name += "/ Вид: " + equipment.getEquipment_form();
            }
            dto.setNameOfCollapse(name);
            dto.setOwnerIinBin(equipment.getOwner_iin_bin());
            dto.setOwnerName(equipment.getOwner_name());
            dto.setProprietorIinBin(equipment.getProprietor_name_iin_bin());
            dto.setProprietorName(equipment.getProprietor_name());
            dto.setGovNumber(equipment.getGov_number());
            dto.setRegSeriesNum(equipment.getReg_series_num());
            dto.setRegDate(equipment.getReg_date());
            dto.setRegReason(equipment.getReg_reason());
            dto.setEndDate(equipment.getEnd_date());
            dto.setEndReason(equipment.getEnd_reason());
            dto.setVin(equipment.getVin());
            dto.setEngineNum(equipment.getEngine_num());
            dto.setBrand(equipment.getBrand());
            dto.setEquipmentModel(equipment.getEquipment_model());
            dto.setManufacturer(equipment.getManufacturer());
            dto.setIssueYear(equipment.getIssue_year());
            dto.setPledge(equipment.isIs_pledge());
            dto.setArrest(equipment.isIs_arrest());
            dto.setFirstReg(equipment.isIs_first_reg());
            result.add(dto);
        }

        return result;
    }

    public List<MvAutoDto> getTransportByBin(String bin) {
        List<MvAutoFl> list = mvAutoFlRepo.getUsersByLike(bin);
        List<MvAutoDto> result = new ArrayList<>();
        for (MvAutoFl mvAutoFl : list) {
            if (mvAutoFl  == null) {
                continue;
            }
            MvAutoDto dto = new MvAutoDto();
            dto.setIin(mvAutoFl.getIin());
            dto.setBrandModel(mvAutoFl.getBrand_model());
            dto.setDateCertificate(mvAutoFl.getDate_certificate());
            dto.setSeriesRegNumber(mvAutoFl.getSeries_reg_number());
            dto.setRegNumber(mvAutoFl.getReg_number());
            dto.setCategoryControlTc(mvAutoFl.getCategory_control_tc());
            dto.setVinKuzovShassi(mvAutoFl.getVin_kuzov_shassi());
            dto.setEngineVolume(mvAutoFl.getEngine_volume());
            dto.setWeight(mvAutoFl.getWeight());
            dto.setMaxWeight(mvAutoFl.getMax_weight());
            dto.setOwnerCategory(mvAutoFl.getOwner_category());
            dto.setEndDate(mvAutoFl.getEnd_date());
            dto.setColor(mvAutoFl.getColor());
            dto.setReleaseYearTc(mvAutoFl.getRelease_year_tc());
            dto.setRegistered(mvAutoFl.isIs_registered());
            dto.setSpecialMarks(mvAutoFl.getSpecial_marks());
            result.add(dto);
        }
        return result;
    }

    public List<AutoTransportDto> getAutoTransportByBin(String bin) {
        List<AutoTransport> list = autoTransportRepo.getAutoByIin(bin);
        List<AutoTransportDto> result = new ArrayList<>();
        for (AutoTransport autoTransport : list) {
            if (autoTransport  == null) {
                continue;
            }
            AutoTransportDto dto = new AutoTransportDto();
            dto.setBrand(autoTransport.getBrand());
            dto.setNumber(autoTransport.getNumber());
            dto.setDate(autoTransport.getDate());
            result.add(dto);
        }

        return result;
    }

    public List<Trains> getTrains(String bin) {
        List<Trains> list = trainsRepo.getByIIN(bin);
        if (list.isEmpty())
            return new ArrayList<>();
        return list;
    }
    public List<AviaTransport> getAviaTransport(String bin) {
        List<AviaTransport> list = aviaTransportRepo.getAviaByIin(bin);
        if (list.isEmpty())
            return new ArrayList<>();
        return list;
    }
    public List<WaterTransport> getWaterTransport(String bin) {
        List<WaterTransport> list = waterTransportRepo.getWaterByIin(bin);
        if (list.isEmpty())
            return new ArrayList<>();
        return list;
    }


}
