package kz.dossier.dto;

public class TaxViewDto {
    private String year;
    private String totalSum;
    private String byEmployees;
    private String byOwning;
    private String byImport;
    // Constructor
    public TaxViewDto(String year, String totalSum, String byEmployees, String byOwning, String byImport) {
        this.year = year;
        this.totalSum = totalSum;
        this.byEmployees = byEmployees;
        this.byOwning = byOwning;
        this.byImport = byImport;
    }
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(String totalSum) {
        this.totalSum = totalSum;
    }

    public String getByEmployees() {
        return byEmployees;
    }

    public void setByEmployees(String byEmployees) {
        this.byEmployees = byEmployees;
    }

    public String getByOwning() {
        return byOwning;
    }

    public void setByOwning(String byOwning) {
        this.byOwning = byOwning;
    }

    public String getByImport() {
        return byImport;
    }

    public void setByImport(String byImport) {
        this.byImport = byImport;
    }
}
