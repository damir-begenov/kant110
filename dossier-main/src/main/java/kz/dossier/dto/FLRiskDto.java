package kz.dossier.dto;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import kz.dossier.modelsDossier.FpgTempEntity;
import kz.dossier.modelsDossier.Msh;
import kz.dossier.modelsDossier.Unemployed;
import kz.dossier.modelsRisk.NdsEntity;
import kz.dossier.modelsRisk.*;
import kz.dossier.modelsRisk.OpgEntity;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.List;

@Data
@JsonPropertyOrder({ "flCard","criminalRecordDtos","convictsRiskList","minsdravInfoList","compulsaryTreatmentRiskDtos",
        "orphansRawDtoList","omnDtoList","dormantsDtoList","removedAccountNdsRiskDtos","blockingEcfList","kgdBankruptsList",
        "pfrList","drugAddictsRisks","erdrUdCardDtoList","ulParticipantInfo","iin","quantity","percentage"})
public class FLRiskDto {

    private String iin;
    private List<Bankrot> bankrots; //--Принудительное лечение
    private List<BlockEsf> blockEsfs;
    private List<ConvictsJustified> convictsJustifieds; //--Снятые с учета по НДС
    private List<ConvictsTerminatedByRehab> convictsTerminatedByRehabs; //--Блокировка выписки ЭСФ
    private List<ConvictsAbroad> convictsAbroads; //--Блокировка выписки ЭСФ
    private List<Criminals> criminals; //--Банкроты
    private List<Dismissal> dismissals; //--Банкроты
    private List<Dormant> dormants; //--Участие в рисковых ЮЛ
    private List<Kuis> kuis; //--Участие в рисковых ЮЛ
    private List<Incapacitated> incapacitateds; //--Участие в рисковых ЮЛ
    private List<FirstCreditBureauEntity> firstCreditBureauEntities; //--Сироты
    private List<ImmoralLifestyle> immoralLifestyles; //--Отсутствие по месту нахождения(ОМН)
    private List<MzEntity> mzEntities; //--Осужденныен по статьям
    private List<NdsEntity> ndsEntities; //--1Д
    private List<Omn> omns;
    private List<OpgEntity> opgEntities; //-- Cписок льготников
    private List<Orphans> orphans; //-- Увольнения по отрицательным мотивам
    private List<BeneficiariesList> beneficiariesLists;
    private List<WantedListEntity> wantedListEntities;
    private List<DrugAddicts> drugAddicts;
    private List<Sudispol> sudispols;
    private List<Unemployed> unemployeds;
    private List<Adm> adms;
    private Double percentage;
    private int quantity; //--количество

    public void setQuantity(int quantity) {
        int riskListCount = countRiskLists();
        this.percentage = (double) (quantity * 100 / riskListCount);
        this.quantity = quantity;
    }
    public Double setPercentage(int quantity){
        int riskListCount = countRiskLists();
        this.percentage = (double) (quantity * 100 / riskListCount);
        this.quantity = quantity;
        return this.percentage;
    }
    private int countRiskLists() {
        Field[] fields = FLRiskDto.class.getDeclaredFields();
        int count = 0;
        for (Field field : fields) {
            if (List.class.isAssignableFrom(field.getType())) {
                count++;
            }
        }
        return count;
    }
}
