package kz.dossier.modelsDossier;

import org.jetbrains.annotations.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "mv_rn", schema = "imp_rn")
public class MvRnOld {
    @Nullable
    @Id

    private String cadastral_number;
    @Nullable
    private String rka_code;

    public String getRka_code() {
        return rka_code;
    }

    public void setRka_code(String rka_code) {
        this.rka_code = rka_code;
    }

    @Nullable

    private String estate_remove_date;
    @Nullable

    private String intended_use_rus;
    @Nullable

    private String intended_use_kaz;
    @Nullable

    private String estate_status_rus;
    @Nullable

    private String estate_status_kaz;
    @Nullable

    private String address_rus;
    @Nullable

    private String address_kaz;
    @Nullable

    private String address_history_rus;
    @Nullable

    private String address_history_kaz;
    @Nullable

    private String type_of_property_rus;
    @Nullable

    private String type_of_property_kaz;
    @Nullable

    private String property_type_rus;
    @Nullable

    private String property_type_kaz;
    @Nullable

    private String area_total;
    @Nullable

    private String area_useful;
    @Nullable

    private String floor;
    @Nullable

    private String estate_characteristic_status_rus;
    @Nullable

    private String estate_characteristic_status_kaz;
    @Nullable

    private String register_record_number;
    @Nullable

    private String register_transaction_amount;
    @Nullable

    private String register_reg_date;
    @Nullable

    private String register_end_date;
    @Nullable

    private String register_emergence_rights_rus;
    @Nullable

    private String register_emergence_rights_kaz;
    @Nullable

    private String register_termination_reason;
    @Nullable

    private String register_status_rus;
    @Nullable

    private String register_status_kaz;
    @Nullable

    private String owner_full_name;

    @Nullable


    private String owner_iin_bin;

    @Nullable
    public String getCadastral_number() {
        return cadastral_number;
    }

    public void setCadastral_number(@Nullable String cadastral_number) {
        this.cadastral_number = cadastral_number;
    }

    @Nullable
    public String getEstate_remove_date() {
        return estate_remove_date;
    }

    public void setEstate_remove_date(@Nullable String estate_remove_date) {
        this.estate_remove_date = estate_remove_date;
    }

    @Nullable
    public String getIntended_use_rus() {
        return intended_use_rus;
    }

    public void setIntended_use_rus(@Nullable String intended_use_rus) {
        this.intended_use_rus = intended_use_rus;
    }

    @Nullable
    public String getIntended_use_kaz() {
        return intended_use_kaz;
    }

    public void setIntended_use_kaz(@Nullable String intended_use_kaz) {
        this.intended_use_kaz = intended_use_kaz;
    }

    @Nullable
    public String getEstate_status_rus() {
        return estate_status_rus;
    }

    public void setEstate_status_rus(@Nullable String estate_status_rus) {
        this.estate_status_rus = estate_status_rus;
    }

    @Nullable
    public String getEstate_status_kaz() {
        return estate_status_kaz;
    }

    public void setEstate_status_kaz(@Nullable String estate_status_kaz) {
        this.estate_status_kaz = estate_status_kaz;
    }

    @Nullable
    public String getAddress_rus() {
        return address_rus;
    }

    public void setAddress_rus(@Nullable String address_rus) {
        this.address_rus = address_rus;
    }

    @Nullable
    public String getAddress_kaz() {
        return address_kaz;
    }

    public void setAddress_kaz(@Nullable String address_kaz) {
        this.address_kaz = address_kaz;
    }

    @Nullable
    public String getAddress_history_rus() {
        return address_history_rus;
    }

    public void setAddress_history_rus(@Nullable String address_history_rus) {
        this.address_history_rus = address_history_rus;
    }

    @Nullable
    public String getAddress_history_kaz() {
        return address_history_kaz;
    }

    public void setAddress_history_kaz(@Nullable String address_history_kaz) {
        this.address_history_kaz = address_history_kaz;
    }

    @Nullable
    public String getType_of_property_rus() {
        return type_of_property_rus;
    }

    public void setType_of_property_rus(@Nullable String type_of_property_rus) {
        this.type_of_property_rus = type_of_property_rus;
    }

    @Nullable
    public String getType_of_property_kaz() {
        return type_of_property_kaz;
    }

    public void setType_of_property_kaz(@Nullable String type_of_property_kaz) {
        this.type_of_property_kaz = type_of_property_kaz;
    }

    @Nullable
    public String getProperty_type_rus() {
        return property_type_rus;
    }

    public void setProperty_type_rus(@Nullable String property_type_rus) {
        this.property_type_rus = property_type_rus;
    }

    @Nullable
    public String getProperty_type_kaz() {
        return property_type_kaz;
    }

    public void setProperty_type_kaz(@Nullable String property_type_kaz) {
        this.property_type_kaz = property_type_kaz;
    }

    @Nullable
    public String getArea_total() {
        return area_total;
    }

    public void setArea_total(@Nullable String area_total) {
        this.area_total = area_total;
    }

    @Nullable
    public String getArea_useful() {
        return area_useful;
    }

    public void setArea_useful(@Nullable String area_useful) {
        this.area_useful = area_useful;
    }

    @Nullable
    public String getFloor() {
        return floor;
    }

    public void setFloor(@Nullable String floor) {
        this.floor = floor;
    }

    @Nullable
    public String getEstate_characteristic_status_rus() {
        return estate_characteristic_status_rus;
    }

    public void setEstate_characteristic_status_rus(@Nullable String estate_characteristic_status_rus) {
        this.estate_characteristic_status_rus = estate_characteristic_status_rus;
    }

    @Nullable
    public String getEstate_characteristic_status_kaz() {
        return estate_characteristic_status_kaz;
    }

    public void setEstate_characteristic_status_kaz(@Nullable String estate_characteristic_status_kaz) {
        this.estate_characteristic_status_kaz = estate_characteristic_status_kaz;
    }

    @Nullable
    public String getRegister_record_number() {
        return register_record_number;
    }

    public void setRegister_record_number(@Nullable String register_record_number) {
        this.register_record_number = register_record_number;
    }

    @Nullable
    public String getRegister_transaction_amount() {
        return register_transaction_amount;
    }

    public void setRegister_transaction_amount(@Nullable String register_transaction_amount) {
        this.register_transaction_amount = register_transaction_amount;
    }

    @Nullable
    public String getRegister_reg_date() {
        return register_reg_date;
    }

    public void setRegister_reg_date(@Nullable String register_reg_date) {
        this.register_reg_date = register_reg_date;
    }

    @Nullable
    public String getRegister_end_date() {
        return register_end_date;
    }

    public void setRegister_end_date(@Nullable String register_end_date) {
        this.register_end_date = register_end_date;
    }

    @Nullable
    public String getRegister_emergence_rights_rus() {
        return register_emergence_rights_rus;
    }

    public void setRegister_emergence_rights_rus(@Nullable String register_emergence_rights_rus) {
        this.register_emergence_rights_rus = register_emergence_rights_rus;
    }

    @Nullable
    public String getRegister_emergence_rights_kaz() {
        return register_emergence_rights_kaz;
    }

    public void setRegister_emergence_rights_kaz(@Nullable String register_emergence_rights_kaz) {
        this.register_emergence_rights_kaz = register_emergence_rights_kaz;
    }

    @Nullable
    public String getRegister_termination_reason() {
        return register_termination_reason;
    }

    public void setRegister_termination_reason(@Nullable String register_termination_reason) {
        this.register_termination_reason = register_termination_reason;
    }

    @Nullable
    public String getRegister_status_rus() {
        return register_status_rus;
    }

    public void setRegister_status_rus(@Nullable String register_status_rus) {
        this.register_status_rus = register_status_rus;
    }

    @Nullable
    public String getRegister_status_kaz() {
        return register_status_kaz;
    }

    public void setRegister_status_kaz(@Nullable String register_status_kaz) {
        this.register_status_kaz = register_status_kaz;
    }

    @Nullable
    public String getOwner_full_name() {
        return owner_full_name;
    }

    public void setOwner_full_name(@Nullable String owner_full_name) {
        this.owner_full_name = owner_full_name;
    }

    @Nullable
    public String getOwner_iin_bin() {
        return owner_iin_bin;
    }

    public void setOwner_iin_bin(@Nullable String owner_iin_bin) {
        this.owner_iin_bin = owner_iin_bin;
    }
}
