package kz.dossier.riskDto;

public class CardDto {
    private String numberErdr;
    private String dateErdr;
    private String qualification;
    private String statusUd;
    private String action;

    public String getNumberErdr() {
        return numberErdr;
    }

    public void setNumberErdr(String numberErdr) {
        this.numberErdr = numberErdr;
    }

    public String getDateErdr() {
        return dateErdr;
    }

    public void setDateErdr(String dateErdr) {
        this.dateErdr = dateErdr;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getStatusUd() {
        return statusUd;
    }

    public void setStatusUd(String statusUd) {
        this.statusUd = statusUd;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
