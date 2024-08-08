package kz.dossier.modelsDossier;

import kz.dossier.modelsDossier.IndividualEntrepreneur;
import kz.dossier.modelsDossier.KX;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class IpKxDto {
    List<IndividualEntrepreneur> individualEntrepreneurList;
    List<KX> kxes;
}
