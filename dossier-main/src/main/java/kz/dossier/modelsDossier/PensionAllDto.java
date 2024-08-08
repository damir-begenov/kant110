package kz.dossier.modelsDossier;

import kz.dossier.dto.PensionGroupDTO;
import kz.dossier.dto.PensionListDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class PensionAllDto {
    private List<PensionGroupDTO> pensionGroupDTOS;
    private List<PensionListDTO> pensionListDTOS;
}
