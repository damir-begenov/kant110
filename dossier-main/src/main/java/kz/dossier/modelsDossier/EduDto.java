package kz.dossier.modelsDossier;

import kz.dossier.modelsDossier.School;
import kz.dossier.modelsDossier.Universities;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EduDto {
    private List<Universities> universitiesList;
    private List<School> schools;
}
