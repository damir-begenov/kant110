package kz.dossier.modelsDossier;

import org.jetbrains.annotations.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "mv_fl_iin_2", schema = "imp_kfm_fl")

public class MvFl {
    @Nullable
    private String iin; //
    @Nullable

    private String gender;
    @Nullable
    @Id

    private String birth_date; //
    @Nullable
    private String last_name; //
    @Nullable
    private String first_name;//
    @Nullable
    private String patronymic;//
    @Nullable
    private String citizenship_id;
    @Nullable
    private String citizenship_ru_name;
    @Nullable
    private String nationality_id;
    @Nullable
    private String nationality_ru_name; //
    @Nullable
    private boolean is_resident;//
    @Nullable
    private String life_status_id;
    @Nullable
    private String life_status_ru_name;
    @Nullable

    private String death_date;//

    @Nullable
    public String getIin() {
        return iin;
    }

    public void setIin(@Nullable String iin) {
        this.iin = iin;
    }

    @Nullable
    public String getGender() {
        return gender;
    }

    public void setGender(@Nullable String gender) {
        this.gender = gender;
    }

    @Nullable
    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(@Nullable String birth_date) {
        this.birth_date = birth_date;
    }

    @Nullable
    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(@Nullable String last_name) {
        this.last_name = last_name;
    }

    @Nullable
    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(@Nullable String first_name) {
        this.first_name = first_name;
    }

    @Nullable
    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(@Nullable String patronymic) {
        this.patronymic = patronymic;
    }

    @Nullable
    public String getCitizenship_id() {
        return citizenship_id;
    }

    public void setCitizenship_id(@Nullable String citizenship_id) {
        this.citizenship_id = citizenship_id;
    }

    @Nullable
    public String getCitizenship_ru_name() {
        return citizenship_ru_name;
    }

    public void setCitizenship_ru_name(@Nullable String citizenship_ru_name) {
        this.citizenship_ru_name = citizenship_ru_name;
    }

    @Nullable
    public String getNationality_id() {
        return nationality_id;
    }

    public void setNationality_id(@Nullable String nationality_id) {
        this.nationality_id = nationality_id;
    }

    @Nullable
    public String getNationality_ru_name() {
        return nationality_ru_name;
    }

    public void setNationality_ru_name(@Nullable String nationality_ru_name) {
        this.nationality_ru_name = nationality_ru_name;
    }

    public boolean isIs_resident() {
        return is_resident;
    }

    public void setIs_resident(boolean is_resident) {
        this.is_resident = is_resident;
    }

    @Nullable
    public String getLife_status_id() {
        return life_status_id;
    }

    public void setLife_status_id(@Nullable String life_status_id) {
        this.life_status_id = life_status_id;
    }

    @Nullable
    public String getLife_status_ru_name() {
        return life_status_ru_name;
    }

    public void setLife_status_ru_name(@Nullable String life_status_ru_name) {
        this.life_status_ru_name = life_status_ru_name;
    }

    @Nullable
    public String getDeath_date() {
        return death_date;
    }

    public void setDeath_date(@Nullable String death_date) {
        this.death_date = death_date;
    }


}
