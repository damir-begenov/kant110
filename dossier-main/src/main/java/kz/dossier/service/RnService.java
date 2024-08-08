package kz.dossier.service;

import java.util.ArrayList;
import java.util.List;

import kz.dossier.dto.DubaiRnDto;
import kz.dossier.dto.RnListDto;
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

    public List<RnListDto> getRnPages(String bin, Integer page, Integer size) {
        if (page == null || page == 0) {
            return null;
        } else {
            page = page * size - size;
        }
        List<MvRnOld> rns = mvRnOldRepo.getUsersByLike(bin, page, size);
        List<RnListDto> rnDTOs = new ArrayList<>();
        for (MvRnOld rn: rns) {
            if (rn  == null) {
                continue;
            }
            RnListDto rnDTO = new RnListDto();
            String header = "Наименование вида недвижимости: ";
            header += rn.getIntended_use_rus();
            if (rn.getRegister_end_date() != null) {
                header += ", Статус: \"исторический\"";
            } else {
                header += ", Статус: \"текущий\"";
            }
            String date = "";
            if (rn.getRegister_reg_date() != null && rn.getRegister_reg_date().length() > 10) {
                date += rn.getRegister_reg_date().substring(0, 10) + " - ";
            }
            if (rn.getRegister_end_date() != null && rn.getRegister_end_date().length() > 10) {
                date += rn.getRegister_end_date().substring(0, 10);
            }
            rnDTO.setHeader(header + ", " + date);
            rnDTO.setCadastrial(rn.getCadastral_number());
            rnDTO.setRightOwner("");
            rnDTO.setAddress(rn.getAddress_rus());
            rnDTO.setFloorness(rn.getFloor());
            rnDTO.setSum(rn.getRegister_transaction_amount());
            rnDTO.setAreaTotal(rn.getArea_total());
            rnDTO.setLivingArea(rn.getArea_useful());
            rnDTO.setTypeOfDoc("");
            rnDTO.setNumberOfDoc(rn.getRegister_emergence_rights_rus());
            if (rn.getRegister_reg_date() != null && rn.getRegister_reg_date().length() > 10) {
                rnDTO.setDateOfReg(rn.getRegister_reg_date().substring(0, 10));
            }
            if (rn.getRegister_end_date() != null && rn.getRegister_end_date().length() > 10) {
                rnDTO.setDateOfStop(rn.getRegister_end_date().substring(0, 10));
            }
            rnDTO.setDateOfDoc(rn.getRegister_reg_date());
            rnDTO.setNumberOfSostav("");
            rnDTO.setIinBin(rn.getOwner_iin_bin());
            rnDTO.setNameOfIinBin(rn.getOwner_full_name());

            rnDTOs.add(rnDTO);
        }
        return rnDTOs;
    }

    public Integer countRns(String bin) {
        return mvRnOldRepo.countByBin(bin);
    }
    public List<RnDTO> getRns(String bin) {
        List<MvRnOld> rns = mvRnOldRepo.getUsersByLike(bin);
        List<RnDTO> rnDTOs = new ArrayList<>();
        for (MvRnOld rn: rns) {
            if (rn  == null) {
                continue;
            }
            RnDTO rnDTO = new RnDTO();

            rnDTO.setNameOfKind(rn.getIntended_use_rus());
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
            String date = "";
            if (rn.getRegister_reg_date() != null && rn.getRegister_reg_date().length() > 10) {
                date += rn.getRegister_reg_date().substring(0, 10) + " - ";
            }
            if (rn.getRegister_end_date() != null && rn.getRegister_end_date().length() > 10) {
                date += rn.getRegister_end_date().substring(0, 10);
            }
            rnDTO.setDateOfRegistration(date);

            rnDTOs.add(rnDTO);
        }
        return rnDTOs;
    }

    public List<RnDTO> getDetailedRnView(String cadastrial_number, String address) {
        List<MvRnOld> rns = mvRnOldRepo.getRowsByCadAndAddress(cadastrial_number, address);
        List<RnDTO> rnDTOs = new ArrayList<>();
        for (MvRnOld rn: rns) {

            RnDTO rnDTO = new RnDTO();

            rnDTO.setNameOfKind(rn.getIntended_use_rus());
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
            String date = "";
            if (rn.getRegister_reg_date() != null && rn.getRegister_reg_date().length() > 10) {
                date += rn.getRegister_reg_date().substring(0, 10) + " - ";
            }
            if (rn.getRegister_end_date() != null && rn.getRegister_end_date().length() > 10) {
                date += rn.getRegister_end_date().substring(0, 10);
            }
            rnDTO.setDateOfRegistration(date);

            rnDTOs.add(rnDTO);
        }
        return rnDTOs;
    }
}
