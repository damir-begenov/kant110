package kz.dossier.dto;

public class RegAddressULDto {
    private String regionRu;
    private String localityRu;
    private String district;
    private String streetRu;
    private String buildingNum;

    public String getRegionRu() {
        return regionRu;
    }

    public void setRegionRu(String regionRu) {
        this.regionRu = regionRu;
    }

    public String getLocalityRu() {
        return localityRu;
    }

    public void setLocalityRu(String localityRu) {
        this.localityRu = localityRu;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreetRu() {
        return streetRu;
    }

    public void setStreetRu(String streetRu) {
        this.streetRu = streetRu;
    }

    public String getBuildingNum() {
        return buildingNum;
    }

    public void setBuildingNum(String buildingNum) {
        this.buildingNum = buildingNum;
    }
}
