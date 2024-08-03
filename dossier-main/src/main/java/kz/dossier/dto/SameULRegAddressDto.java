package kz.dossier.dto;

public class SameULRegAddressDto {
    private String bin;
    private String name;
    private String address;
    private String iinOfHead;
    private String fio;
    private String position;
    private String date;

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIinOfHead() {
        return iinOfHead;
    }

    public void setIinOfHead(String iinOfHead) {
        this.iinOfHead = iinOfHead;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
