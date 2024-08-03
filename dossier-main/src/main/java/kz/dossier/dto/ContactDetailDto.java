package kz.dossier.dto;

public class ContactDetailDto {
    private String number;
    private String email;
    private String fioOfHead;
    private String nameOfOwner;
    private String source;
    private String nickname;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFioOfHead() {
        return fioOfHead;
    }

    public void setFioOfHead(String fioOfHead) {
        this.fioOfHead = fioOfHead;
    }

    public String getNameOfOwner() {
        return nameOfOwner;
    }

    public void setNameOfOwner(String nameOfOwner) {
        this.nameOfOwner = nameOfOwner;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
