//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package kz.dossier.modelsDossier;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.Date;
import org.jetbrains.annotations.Nullable;

@Entity
@Table(
        name = "imp_kfm_ul.mv_ul_leader"
)
public class MvUlLeaderEntity {
    @Id
    private @Nullable String id;
    @Column(
            name = "bin_org"
    )
    private @Nullable String binOrg;
    @Column(
            name = "reg_date"
    )
    private @Nullable String regDate;
    @Column(
            name = "iin"
    )
    private @Nullable String iin;
    @Column(
            name = "lastname"
    )
    private @Nullable String lastName;
    @Column(
            name = "firstname"
    )
    private @Nullable String firstName;
    @Column(
            name = "patronymic"
    )
    private @Nullable String patronymic;
    @Column(
            name = "position_id"
    )
    private @Nullable Integer positionId;
    @Column(
            name = "appointment_date"
    )
    private @Nullable Date appointmentDate;
    @Column(
            name = "removal_date"
    )
    private @Nullable Date removalDate;
    @Column(
            name = "is_curr"
    )
    private @Nullable Boolean isCurrent;
    @Column(
            name = "ul_status"
    )
    private @Nullable String ulStatus;
    @Transient
    private String binName;

    public MvUlLeaderEntity() {
    }

    public String getBinName() {
        return this.binName;
    }

    public void setBinName(String binName) {
        this.binName = binName;
    }

    public @Nullable String getId() {
        return this.id;
    }

    public void setId(@Nullable String id) {
        this.id = id;
    }

    public @Nullable String getBinOrg() {
        return this.binOrg;
    }

    public void setBinOrg(@Nullable String binOrg) {
        this.binOrg = binOrg;
    }

    public @Nullable String getRegDate() {
        return this.regDate;
    }

    public void setRegDate(@Nullable String regDate) {
        this.regDate = regDate;
    }

    public @Nullable String getIin() {
        return this.iin;
    }

    public void setIin(@Nullable String iin) {
        this.iin = iin;
    }

    public @Nullable String getLastName() {
        return this.lastName;
    }

    public void setLastName(@Nullable String lastName) {
        this.lastName = lastName;
    }

    public @Nullable String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(@Nullable String firstName) {
        this.firstName = firstName;
    }

    public @Nullable String getPatronymic() {
        return this.patronymic;
    }

    public void setPatronymic(@Nullable String patronymic) {
        this.patronymic = patronymic;
    }

    public @Nullable Integer getPositionId() {
        return this.positionId;
    }

    public void setPositionId(@Nullable Integer positionId) {
        this.positionId = positionId;
    }

    public @Nullable Date getAppointmentDate() {
        return this.appointmentDate;
    }

    public void setAppointmentDate(@Nullable Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public @Nullable Date getRemovalDate() {
        return this.removalDate;
    }

    public void setRemovalDate(@Nullable Date removalDate) {
        this.removalDate = removalDate;
    }

    public @Nullable Boolean getCurrent() {
        return this.isCurrent;
    }

    public void setCurrent(@Nullable Boolean current) {
        this.isCurrent = current;
    }

    public @Nullable String getUlStatus() {
        return this.ulStatus;
    }

    public void setUlStatus(@Nullable String ulStatus) {
        this.ulStatus = ulStatus;
    }
}
