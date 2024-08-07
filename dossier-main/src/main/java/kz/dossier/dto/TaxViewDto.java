package kz.dossier.dto;

import java.math.BigDecimal;

public class TaxViewDto {
    private String year;
    private BigDecimal totalSum;
    private BigDecimal byEmployees;
    private BigDecimal byOwning;
    private BigDecimal byImport;
    // Constructor
    public TaxViewDto(String year, BigDecimal totalSum, BigDecimal byEmployees, BigDecimal byOwning, BigDecimal byImport) {
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

    public BigDecimal getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(BigDecimal totalSum) {
        this.totalSum = totalSum;
    }

    public BigDecimal getByEmployees() {
        return byEmployees;
    }

    public void setByEmployees(BigDecimal byEmployees) {
        this.byEmployees = byEmployees;
    }

    public BigDecimal getByOwning() {
        return byOwning;
    }

    public void setByOwning(BigDecimal byOwning) {
        this.byOwning = byOwning;
    }

    public BigDecimal getByImport() {
        return byImport;
    }

    public void setByImport(BigDecimal byImport) {
        this.byImport = byImport;
    }
}
