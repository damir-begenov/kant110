package kz.dossier.modelsDossier;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "water_transport", schema = "imp_kfm_fl")
@Data
public class WaterTransport {
    private String ikt;
    private String reg_number;
    private String iin_bin;
    private String name;
    private String year_reestr;
    private String name_vt;
    private String type_vt;
    private String purpose;
    private String region_vt;
    private String material;
    private String country_vt;
    private String name_engine;
    private String sourse;
    private String year_vt;

    public String getYear_vt() {
        return year_vt;
    }

    public void setYear_vt(String year_vt) {
        this.year_vt = year_vt;
    }

    @Id
    private Long id;

    public String getIkt() {
        return ikt;
    }

    public void setIkt(String ikt) {
        this.ikt = ikt;
    }

    public String getReg_number() {
        return reg_number;
    }

    public void setReg_number(String reg_number) {
        this.reg_number = reg_number;
    }

    public String getIin_bin() {
        return iin_bin;
    }

    public void setIin_bin(String iin_bin) {
        this.iin_bin = iin_bin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear_reestr() {
        return year_reestr;
    }

    public void setYear_reestr(String year_reestr) {
        this.year_reestr = year_reestr;
    }

    public String getName_vt() {
        return name_vt;
    }

    public void setName_vt(String name_vt) {
        this.name_vt = name_vt;
    }

    public String getType_vt() {
        return type_vt;
    }

    public void setType_vt(String type_vt) {
        this.type_vt = type_vt;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getRegion_vt() {
        return region_vt;
    }

    public void setRegion_vt(String region_vt) {
        this.region_vt = region_vt;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getCountry_vt() {
        return country_vt;
    }

    public void setCountry_vt(String country_vt) {
        this.country_vt = country_vt;
    }

    public String getName_engine() {
        return name_engine;
    }

    public void setName_engine(String name_engine) {
        this.name_engine = name_engine;
    }

    public String getSourse() {
        return sourse;
    }

    public void setSourse(String sourse) {
        this.sourse = sourse;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

