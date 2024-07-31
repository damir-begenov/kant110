package kz.dossier.dto;

import java.util.List;

import kz.dossier.modelsDossier.*;
import kz.dossier.modelsDossier.AccountantListEntity;
import kz.dossier.modelsRisk.Pdl;

public class GeneralInfoDTO {
    private Double percent;
    private List<FlContacts> contacts;

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    private List<SearchResultModelFL> sameAddressFls;

    private List<AccountantListEntity> accountantListEntities; // БУХГАЛТЭРЫ
    private List<AdvocateListEntity> advocateListEntities;
    private List<AuditorsListEntity> auditorsListEntities;
    private List<BailiffListEntity> bailiffListEntities; //частный суд исполнитель
    private List<IpgoEmailEntity> ipgoEmailEntities;
    private List<MvUlFounderFl> mvUlFounderFls;

    public List<MvUlFounderFl> getMvUlFounderFls() {
        return mvUlFounderFls;
    }


    public void setMvUlFounderFls(List<MvUlFounderFl> mvUlFounderFls) {
        this.mvUlFounderFls = mvUlFounderFls;
    }


    public List<IpgoEmailEntity> getIpgoEmailEntities() {
        return ipgoEmailEntities;
    }

    public void setIpgoEmailEntities(List<IpgoEmailEntity> ipgoEmailEntities) {
        this.ipgoEmailEntities = ipgoEmailEntities;
    }

    public List<AdvocateListEntity> getAdvocateListEntities() {
        return advocateListEntities;
    }

    public void setAdvocateListEntities(List<AdvocateListEntity> advocateListEntities) {
        this.advocateListEntities = advocateListEntities;
    }

    public List<AuditorsListEntity> getAuditorsListEntities() {
        return auditorsListEntities;
    }

    public void setAuditorsListEntities(List<AuditorsListEntity> auditorsListEntities) {
        this.auditorsListEntities = auditorsListEntities;
    }

    public List<BailiffListEntity> getBailiffListEntities() {
        return bailiffListEntities;
    }

    public void setBailiffListEntities(List<BailiffListEntity> bailiffListEntities) {
        this.bailiffListEntities = bailiffListEntities;
    }

    private List<Lawyers> lawyers;
    private List<ChangeFio> changeFio;

    private List<Pdl> pdls;


    public List<AccountantListEntity> getAccountantListEntities() {
        return accountantListEntities;
    }

    public void setAccountantListEntities(List<AccountantListEntity> accountantListEntities) {
        this.accountantListEntities = accountantListEntities;
    }

    public List<Pdl> getPdls() {
        return pdls;
    }

    public void setPdls(List<Pdl> pdls) {
        this.pdls = pdls;
    }

    public List<ChangeFio> getChangeFio() {
        return changeFio;
    }

    public void setChangeFio(List<ChangeFio> changeFio) {
        this.changeFio = changeFio;
    }

    public List<Lawyers> getLawyers() {
        return lawyers;
    }

    public void setLawyers(List<Lawyers> lawyers) {
        this.lawyers = lawyers;
    }

    public List<FlContacts> getContacts() {
        return contacts;
    }
    public List<SearchResultModelFL> getSameAddressFls() {
        return sameAddressFls;
    }


    public void setContacts(List<FlContacts> contacts) {
        this.contacts = contacts;
    }
    public void setSameAddressFls(List<SearchResultModelFL> sameAddressFls) {
        this.sameAddressFls = sameAddressFls;
    }
}
