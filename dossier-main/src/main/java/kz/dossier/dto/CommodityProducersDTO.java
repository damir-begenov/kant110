package kz.dossier.dto;

public class CommodityProducersDTO {
    private String szpt;
    private Integer count;
    private String status;
    private String region;

    public String getSzpt() {
        return szpt;
    }

    public void setSzpt(String szpt) {
        this.szpt = szpt;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
