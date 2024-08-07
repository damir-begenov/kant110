package kz.dossier.riskDto;

public class BankuptDto {
    private String iin;
    private String fio;
    private String numberOfDecision;
    private String dateOfDecision;

    public String getIin() {
        return iin;
    }

    public void setIin(String iin) {
        this.iin = iin;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getNumberOfDecision() {
        return numberOfDecision;
    }

    public void setNumberOfDecision(String numberOfDecision) {
        this.numberOfDecision = numberOfDecision;
    }

    public String getDateOfDecision() {
        return dateOfDecision;
    }

    public void setDateOfDecision(String dateOfDecision) {
        this.dateOfDecision = dateOfDecision;
    }
}
