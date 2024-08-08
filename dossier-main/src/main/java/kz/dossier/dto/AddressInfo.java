package kz.dossier.dto;

public class AddressInfo {
    private String region;
    private String district;
    private String city;
    private String street;
    private String rka;
    private String building;
    private String korpus;
    private String apartment_number;
    private String rnRegAddress;

    public String getRka() {
        return rka;
    }

    public void setRka(String rka) {
        this.rka = rka;
    }

    public String getRnRegAddress() {
        return rnRegAddress;
    }

    public void setRnRegAddress(String rnRegAddress) {
        this.rnRegAddress = rnRegAddress;
    }

    public String getRegion() {
        return region;
    }
    public String getDistrict() {
        return district;
    }
    public String getCity() {
        return city;
    }
    public String getStreet() {
        return street;
    }
    public String getBuilding() {
        return building;
    }
    public String getKorpus() {
        return korpus;
    }
    public String getApartment_number() {
        return apartment_number;
    }
    public void setRegion(String region) {
        this.region = region;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public void setApartment_number(String apartment_number) {
        this.apartment_number = apartment_number;
    }
    public void setBuilding(String building) {
        this.building = building;
    }
    public void setDistrict(String district) {
        this.district = district;
    }
    public void setKorpus(String korpus) {
        this.korpus = korpus;
    }
    public void setStreet(String street) {
        this.street = street;
    }
}
