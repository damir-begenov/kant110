package kz.dossier.modelsDossier;

import org.jetbrains.annotations.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "equipment", schema = "imp_kfm_fl")

public class Equipment {

@Nullable

    private String owner_iin_bin;
    @Nullable
    private String owner_name;
    @Nullable

    private String owner_address;
    @Nullable
    private String proprietor_name;
    @Nullable
    private String proprietor_name_iin_bin;
    @Nullable
    private String proprietor_address;
    @Nullable
    private String quipment_location;
    @Nullable
    private String gov_number;
    @Nullable
    private String reg_series_num;
    @Nullable
    private String reg_date;
    @Nullable
    private String reg_reason;
    @Nullable

    private String end_date;
    @Nullable
    private String end_reason;
    @Nullable

    private String vin;
    @Nullable
    private String engine_num;
    @Nullable
    private String equipment_spec;
    @Nullable
    private String equipment_type;
    @Nullable
    private String equipment_form;
    @Nullable
    private String brand;
    @Nullable
    private String equipment_model;
    @Nullable
    private String manufacturer;
    @Nullable
    private String dealer;
    @Nullable
    @Id

    private Long id;
    @Nullable
    private Integer issue_year;
    @Nullable
    private Float power_kw;
    @Nullable
    private Float power_hp;
    @Nullable
    private Boolean is_first_reg;
    @Nullable
    private Boolean is_pledge;
    @Nullable
    private Boolean is_arrest;



    @Nullable
    public String getOwner_iin_bin() {
        return owner_iin_bin;
    }

    public void setOwner_iin_bin(@Nullable String owner_iin_bin) {
        this.owner_iin_bin = owner_iin_bin;
    }

    @Nullable
    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(@Nullable String owner_name) {
        this.owner_name = owner_name;
    }

    @Nullable
    public String getOwner_address() {
        return owner_address;
    }

    public void setOwner_address(@Nullable String owner_address) {
        this.owner_address = owner_address;
    }

    @Nullable
    public String getProprietor_name() {
        return proprietor_name;
    }

    public void setProprietor_name(@Nullable String proprietor_name) {
        this.proprietor_name = proprietor_name;
    }

    @Nullable
    public String getProprietor_name_iin_bin() {
        return proprietor_name_iin_bin;
    }

    public void setProprietor_name_iin_bin(@Nullable String proprietor_name_iin_bin) {
        this.proprietor_name_iin_bin = proprietor_name_iin_bin;
    }

    @Nullable
    public String getProprietor_address() {
        return proprietor_address;
    }

    public void setProprietor_address(@Nullable String proprietor_address) {
        this.proprietor_address = proprietor_address;
    }

    @Nullable
    public String getQuipment_location() {
        return quipment_location;
    }

    public void setQuipment_location(@Nullable String quipment_location) {
        this.quipment_location = quipment_location;
    }

    @Nullable
    public String getGov_number() {
        return gov_number;
    }

    public void setGov_number(@Nullable String gov_number) {
        this.gov_number = gov_number;
    }

    @Nullable
    public String getReg_series_num() {
        return reg_series_num;
    }

    public void setReg_series_num(@Nullable String reg_series_num) {
        this.reg_series_num = reg_series_num;
    }

    @Nullable
    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(@Nullable String reg_date) {
        this.reg_date = reg_date;
    }

    @Nullable
    public String getReg_reason() {
        return reg_reason;
    }

    public void setReg_reason(@Nullable String reg_reason) {
        this.reg_reason = reg_reason;
    }

    @Nullable
    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(@Nullable String end_date) {
        this.end_date = end_date;
    }

    @Nullable
    public String getEnd_reason() {
        return end_reason;
    }

    public void setEnd_reason(@Nullable String end_reason) {
        this.end_reason = end_reason;
    }

    @Nullable
    public String getVin() {
        return vin;
    }

    public void setVin(@Nullable String vin) {
        this.vin = vin;
    }

    @Nullable
    public String getEngine_num() {
        return engine_num;
    }

    public void setEngine_num(@Nullable String engine_num) {
        this.engine_num = engine_num;
    }

    @Nullable
    public String getEquipment_spec() {
        return equipment_spec;
    }

    public void setEquipment_spec(@Nullable String equipment_spec) {
        this.equipment_spec = equipment_spec;
    }

    @Nullable
    public String getEquipment_type() {
        return equipment_type;
    }

    public void setEquipment_type(@Nullable String equipment_type) {
        this.equipment_type = equipment_type;
    }

    @Nullable
    public String getEquipment_form() {
        return equipment_form;
    }

    public void setEquipment_form(@Nullable String equipment_form) {
        this.equipment_form = equipment_form;
    }

    @Nullable
    public String getBrand() {
        return brand;
    }

    public void setBrand(@Nullable String brand) {
        this.brand = brand;
    }

    @Nullable
    public String getEquipment_model() {
        return equipment_model;
    }

    public void setEquipment_model(@Nullable String equipment_model) {
        this.equipment_model = equipment_model;
    }

    @Nullable
    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(@Nullable String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Nullable
    public String getDealer() {
        return dealer;
    }

    public void setDealer(@Nullable String dealer) {
        this.dealer = dealer;
    }

    @Nullable
    public Long getId() {
        return id;
    }

    public void setId(@Nullable Long id) {
        this.id = id;
    }

    @Nullable
    public Integer getIssue_year() {
        return issue_year;
    }

    public void setIssue_year(@Nullable Integer issue_year) {
        this.issue_year = issue_year;
    }

    @Nullable
    public Float getPower_kw() {
        return power_kw;
    }

    public void setPower_kw(@Nullable Float power_kw) {
        this.power_kw = power_kw;
    }

    @Nullable
    public Float getPower_hp() {
        return power_hp;
    }

    public void setPower_hp(@Nullable Float power_hp) {
        this.power_hp = power_hp;
    }

    public Boolean isIs_first_reg() {
        return is_first_reg;
    }

    public void setIs_first_reg(Boolean is_first_reg) {
        this.is_first_reg = is_first_reg;
    }

    public Boolean isIs_pledge() {
        return is_pledge;
    }

    public void setIs_pledge(Boolean is_pledge) {
        this.is_pledge = is_pledge;
    }

    public Boolean isIs_arrest() {
        return is_arrest;
    }

    public void setIs_arrest(Boolean is_arrest) {
        this.is_arrest = is_arrest;
    }
}
