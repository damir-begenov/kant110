package kz.dossier.modelsDossier;

import kz.dossier.modelsDossier.MvUlFounderFl;
import kz.dossier.modelsDossier.MvUlLeaderEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UlParticipantsDto {
    private List<MvUlFounderFl> mvUlFounderFlList;
    private List<MvUlLeaderEntity> mvUlLeaderEntities;
}
