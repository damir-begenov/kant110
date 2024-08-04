//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package kz.dossier.modelsDossier;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.UUID;

@Entity
@Table(
        name = "ul_founder_fl",
        schema = "imp_kfm_fl"
)
public class MvUlFounderFl {
    @Id
    private UUID id;
    private String reg_date;
    private String bin_org;
    private String iin;
    private String lastname;
    private String firstname;
    private String patronymic;
    private String deposit;
    private String share;
    private boolean is_curr;
    @Transient
    private String binName;

    public MvUlFounderFl() {
    }

    public String getBinName() {
        return this.binName;
    }

    public void setBinName(String binName) {
        this.binName = binName;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getReg_date() {
        return this.reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public boolean isIs_curr() {
        return this.is_curr;
    }

    public void setIs_curr(boolean is_curr) {
        this.is_curr = is_curr;
    }

    public String getBin_org() {
        return this.bin_org;
    }

    public void setBin_org(String bin_org) {
        this.bin_org = bin_org;
    }

    public String getIin() {
        return this.iin;
    }

    public void setIin(String iin) {
        this.iin = iin;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getPatronymic() {
        return this.patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getDeposit() {
        return this.deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getShare() {
        return this.share;
    }

    public void setShare(String share) {
        this.share = share;
    }
}
