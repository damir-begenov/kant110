package kz.dossier.modelsDossier;

import kz.dossier.modelsDossier.MvFl;
import kz.dossier.modelsDossier.PhotoDb;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class MvFlWithPhotoDto {
    private List<MvFl> mvFlList;
    private List<PhotoDb> photoDbs;
    private Double riskPercentage;
}
