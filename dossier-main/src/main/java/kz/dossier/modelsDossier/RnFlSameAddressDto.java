package kz.dossier.modelsDossier;

import kz.dossier.modelsDossier.MvRnOld;
import kz.dossier.modelsDossier.SearchResultModelFL;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class RnFlSameAddressDto {
    private List<SearchResultModelFL> searchResultModelFLList;
    private List<RnOwnerDto> rnOwnerDtos;
}
