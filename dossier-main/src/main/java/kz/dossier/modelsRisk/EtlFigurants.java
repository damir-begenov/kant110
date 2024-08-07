package kz.dossier.modelsRisk;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "etl.etl_figurants")
public class EtlFigurants {
    @Id
    private Long id;
    @Column(name = "iin")
    private String iin;
    @Column(name = "erdr_ud_number")
    private String erdrUdNum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIin() {
        return iin;
    }

    public void setIin(String iin) {
        this.iin = iin;
    }

    public String getErdrUdNum() {
        return erdrUdNum;
    }

    public void setErdrUdNum(String erdrUdNum) {
        this.erdrUdNum = erdrUdNum;
    }
}
