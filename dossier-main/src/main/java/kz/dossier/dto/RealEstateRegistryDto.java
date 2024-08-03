package kz.dossier.dto;

public class RealEstateRegistryDto {
    private String cadastral_number;
    private String address_rus;
    private String floor;
    private String area_total;
    private String register_emergence_rights_rus;
    private String register_reg_date;
    private String register_transaction_amount;
    private String area_useful;
    private String register_end_date;

    public String getCadastral_number() {
        return cadastral_number;
    }

    public void setCadastral_number(String cadastral_number) {
        this.cadastral_number = cadastral_number;
    }

    public String getAddress_rus() {
        return address_rus;
    }

    public void setAddress_rus(String address_rus) {
        this.address_rus = address_rus;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getArea_total() {
        return area_total;
    }

    public void setArea_total(String area_total) {
        this.area_total = area_total;
    }

    public String getRegister_emergence_rights_rus() {
        return register_emergence_rights_rus;
    }

    public void setRegister_emergence_rights_rus(String register_emergence_rights_rus) {
        this.register_emergence_rights_rus = register_emergence_rights_rus;
    }

    public String getRegister_reg_date() {
        return register_reg_date;
    }

    public void setRegister_reg_date(String register_reg_date) {
        this.register_reg_date = register_reg_date;
    }

    public String getRegister_transaction_amount() {
        return register_transaction_amount;
    }

    public void setRegister_transaction_amount(String register_transaction_amount) {
        this.register_transaction_amount = register_transaction_amount;
    }

    public String getArea_useful() {
        return area_useful;
    }

    public void setArea_useful(String area_useful) {
        this.area_useful = area_useful;
    }

    public String getRegister_end_date() {
        return register_end_date;
    }

    public void setRegister_end_date(String register_end_date) {
        this.register_end_date = register_end_date;
    }
}
