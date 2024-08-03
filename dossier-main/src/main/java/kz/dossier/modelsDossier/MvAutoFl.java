package kz.dossier.modelsDossier;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "mv_auto_fl", schema = "imp_kfm_db")
public class MvAutoFl {

    private String iin;
    private String brand_model;
    private String date_certificate;

    private String series_reg_number;
    private String reg_number;
    private String category_control_tc;
    @Id

    private String vin_kuzov_shassi;

    private String engine_volume;
    private String weight;
    private String max_weight;
    private String owner_category;

    private String end_date;
    private String color;
    private String release_year_tc;
    private boolean is_registered;
    private String special_marks;



    public String getIin() {
        return iin;
    }

    public void setIin(String iin) {
        this.iin = iin;
    }

    public String getBrand_model() {
        return brand_model;
    }

    public void setBrand_model(String brand_model) {
        this.brand_model = brand_model;
    }

    public String getDate_certificate() {
        return date_certificate;
    }

    public void setDate_certificate(String date_certificate) {
        this.date_certificate = date_certificate;
    }

    public String getSeries_reg_number() {
        return series_reg_number;
    }

    public void setSeries_reg_number(String series_reg_number) {
        this.series_reg_number = series_reg_number;
    }

    public String getReg_number() {
        return reg_number;
    }

    public void setReg_number(String reg_number) {
        this.reg_number = reg_number;
    }

    public String getCategory_control_tc() {
        return category_control_tc;
    }

    public void setCategory_control_tc(String category_control_tc) {
        this.category_control_tc = category_control_tc;
    }

    public String getVin_kuzov_shassi() {
        return vin_kuzov_shassi;
    }

    public void setVin_kuzov_shassi(String vin_kuzov_shassi) {
        this.vin_kuzov_shassi = vin_kuzov_shassi;
    }

    public String getEngine_volume() {
        return engine_volume;
    }

    public void setEngine_volume(String engine_volume) {
        this.engine_volume = engine_volume;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getMax_weight() {
        return max_weight;
    }

    public void setMax_weight(String max_weight) {
        this.max_weight = max_weight;
    }

    public String getOwner_category() {
        return owner_category;
    }

    public void setOwner_category(String owner_category) {
        this.owner_category = owner_category;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getRelease_year_tc() {
        return release_year_tc;
    }

    public void setRelease_year_tc(String release_year_tc) {
        this.release_year_tc = release_year_tc;
    }

    public boolean isIs_registered() {
        return is_registered;
    }

    public void setIs_registered(boolean is_registered) {
        this.is_registered = is_registered;
    }

    public String getSpecial_marks() {
        return special_marks;
    }

    public void setSpecial_marks(String special_marks) {
        this.special_marks = special_marks;
    }
}
