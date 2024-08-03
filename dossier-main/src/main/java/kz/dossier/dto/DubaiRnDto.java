package kz.dossier.dto;

import java.sql.Date;

public class DubaiRnDto {
    private String date;
    private String area;
    private String nameOfZhk;
    private String appartment;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getNameOfZhk() {
        return nameOfZhk;
    }

    public void setNameOfZhk(String nameOfZhk) {
        this.nameOfZhk = nameOfZhk;
    }

    public String getAppartment() {
        return appartment;
    }

    public void setAppartment(String appartment) {
        this.appartment = appartment;
    }
}
