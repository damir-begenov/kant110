package kz.dossier.modelsRisk;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Date;

@Entity
@Table(name = "etl.etl_erdr_card")
public class EtlErdrCard {
    @Id
    private Long id;
    @Column(name = "erdr_create_timestamp")
    private Date erdrCreateTimeStamp;
    @Column(name = "erdr_ud_number")
    private String erdrUdNumber;
    @Column(name = "erdr_ud_registration_authority_ru")
    private String erdrUdRegistrationAuthorityRu;
    @Column(name = "qualification_ru")
    private String qualificationRu;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getErdrCreateTimeStamp() {
        return erdrCreateTimeStamp;
    }

    public void setErdrCreateTimeStamp(Date erdrCreateTimeStamp) {
        this.erdrCreateTimeStamp = erdrCreateTimeStamp;
    }

    public String getErdrUdNumber() {
        return erdrUdNumber;
    }

    public void setErdrUdNumber(String erdrUdNumber) {
        this.erdrUdNumber = erdrUdNumber;
    }

    public String getErdrUdRegistrationAuthorityRu() {
        return erdrUdRegistrationAuthorityRu;
    }

    public void setErdrUdRegistrationAuthorityRu(String erdrUdRegistrationAuthorityRu) {
        this.erdrUdRegistrationAuthorityRu = erdrUdRegistrationAuthorityRu;
    }

    public String getQualificationRu() {
        return qualificationRu;
    }

    public void setQualificationRu(String qualificationRu) {
        this.qualificationRu = qualificationRu;
    }
}
