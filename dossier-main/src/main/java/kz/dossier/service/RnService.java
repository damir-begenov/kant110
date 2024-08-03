package kz.dossier.service;

import java.util.ArrayList;
import java.util.List;

import kz.dossier.dto.DubaiRnDto;
import kz.dossier.modelsDossier.*;
import kz.dossier.repositoryDossier.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kz.dossier.dto.RnDTO;

@Service
public class RnService {
    @Autowired
    MvRnOldRepo mvRnOldRepo;
    @Autowired
    DubaiRnRepo dubaiRnRepo;
    @Autowired
    Fno240Repo fno240Repo;
    @Autowired
    Fno250Repo fno250Repo;
    @Autowired
    Fno250AvtoRepo fno250AvtoRepo;
    @Autowired
    Fno250CompanyRepo fno250CompanyRepo;
    @Autowired
    Fno250DepositRepo fno250DepositRepo;

    public List<Fno240> getFno240s(String iin) {
        List<Fno240> result = fno240Repo.findAllByIin(iin);
        return result;
    }
    public List<Fno250> getFno250s(String iin) {
        List<Fno250> result = fno250Repo.findAllByIin(iin);
        return result;
    }
    public List<Fno250Avto> getFno250Avtos(String iin) {
        List<Fno250Avto> result = fno250AvtoRepo.findAllByIin(iin);
        return result;
    }
    public List<Fno250Company> getFno250Companies(String iin) {
        List<Fno250Company> result = fno250CompanyRepo.findAllByIin(iin);
        return result;
    }
    public List<Fno250Deposit> getFno250Deposit(String iin) {
        List<Fno250Deposit> result = fno250DepositRepo.findAllByIin(iin);
        return result;
    }

    public List<DubaiRnDto> getDubaiRns(String iinbin) {
        List<DubaiRn> rns = dubaiRnRepo.findAllByIin(iinbin);
        List<DubaiRnDto> result = new ArrayList<>();
        for (DubaiRn rn : rns) {
            DubaiRnDto dto = new DubaiRnDto();
            dto.setAppartment(rn.getNumber_flats());
            dto.setArea(String.valueOf(rn.getVolume()));
            dto.setNameOfZhk(rn.getZhk_kompleks());
            dto.setDate(String.valueOf(rn.getDate_seller()));

            result.add(dto);
        }
        return result;
    }

    public List<RnDTO> getRns(String bin) {
        List<MvRnOld> rns = mvRnOldRepo.getUsersByLike(bin);
        List<RnDTO> rnDTOs = new ArrayList<>();
        for (MvRnOld rn: rns) {
            if (rn  == null) {
                continue;
            }
            RnDTO rnDTO = new RnDTO();

            rnDTO.setNameOfKind("");
            rnDTO.setCadastrialNumber(rn.getCadastral_number());
            rnDTO.setRightOwner("");
            rnDTO.setAddress(rn.getAddress_rus());
            rnDTO.setFloorness(rn.getFloor());
            rnDTO.setSumOfDeal(rn.getRegister_transaction_amount());
            rnDTO.setAllArea(rn.getArea_total());
            rnDTO.setLivingArea(rn.getArea_useful());
            rnDTO.setTypeOfDoc("");
            rnDTO.setDocumentNumber(rn.getRegister_emergence_rights_rus());
            rnDTO.setDate(rn.getRegister_reg_date());
            if (rn.getRegister_end_date() != null) {
                rnDTO.setStatusRn("Исторический");
            } else {
                rnDTO.setStatusRn("Текущий");
            }
            rnDTO.setInOfOwner(rn.getOwner_iin_bin());
            rnDTO.setOwnerName(rn.getOwner_full_name());
            rnDTO.setDateOfRegistration(rn.getRegister_reg_date() + " - " + rn.getRegister_end_date());

            rnDTOs.add(rnDTO);
        }
        return rnDTOs;
    }

    public List<RnDTO> getDetailedRnView(String cadastrial_number, String address) {
        List<MvRnOld> rns = mvRnOldRepo.getRowsByCadAndAddress(cadastrial_number, address);
        List<RnDTO> rnDTOs = new ArrayList<>();
        for (MvRnOld rn: rns) {

            RnDTO rnDTO = new RnDTO();

            rnDTO.setNameOfKind("");
            rnDTO.setCadastrialNumber(cadastrial_number);
            rnDTO.setRightOwner("");
            rnDTO.setAddress(address);
            rnDTO.setFloorness(rn.getFloor());
            rnDTO.setSumOfDeal(rn.getRegister_transaction_amount());
            rnDTO.setAllArea(rn.getArea_total());
            rnDTO.setLivingArea(rn.getArea_useful());
            rnDTO.setTypeOfDoc("");
            rnDTO.setDocumentNumber(rn.getRegister_emergence_rights_rus());
            rnDTO.setDate("");
            if (rn.getRegister_end_date() != null) {
                rnDTO.setStatusRn("Исторический");
            } else {
                rnDTO.setStatusRn("Текущий");
            }
            rnDTO.setInOfOwner(rn.getOwner_iin_bin());
            rnDTO.setOwnerName(rn.getOwner_full_name());
            rnDTO.setDateOfRegistration(rn.getRegister_reg_date() + " - " + rn.getRegister_end_date());

            rnDTOs.add(rnDTO);
        }
        return rnDTOs;
    }
}
