package kz.dossier.riskDto;

public class SnyatUchetNdsDto {
    private String name;
    private String dateOfPostanovka;
    private String dateOfSnyiatie;
    private String reason;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfPostanovka() {
        return dateOfPostanovka;
    }

    public void setDateOfPostanovka(String dateOfPostanovka) {
        this.dateOfPostanovka = dateOfPostanovka;
    }

    public String getDateOfSnyiatie() {
        return dateOfSnyiatie;
    }

    public void setDateOfSnyiatie(String dateOfSnyiatie) {
        this.dateOfSnyiatie = dateOfSnyiatie;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
