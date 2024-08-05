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
import org.jetbrains.annotations.Nullable;

@Entity
@Table(
        name = "mv_fl",
        schema = "imp_kfm_fl"
)
public class MvFl {
    @Id
    private UUID id;
    private @Nullable String iin;
    private @Nullable String last_name;
    private @Nullable String first_name;
    private @Nullable String patronymic;
    private @Nullable String birth_date;
    private @Nullable String citizenship_id;
    @Transient
    private @Nullable String citizenship_ru_name = "КАЗАХСТАН";
    private @Nullable String nationality_id;
    private @Nullable String nationality_ru_name;
    private @Nullable boolean is_resident;
    private @Nullable String life_status_id;
    private @Nullable String life_status_ru_name;
    private @Nullable String death_date;
    private String district;
    private String region;

    public MvFl() {
    }

    public UUID getId() {
        return this.id;
    }

    public @Nullable String getIin() {
        return this.iin;
    }

    public @Nullable String getLast_name() {
        return this.last_name;
    }

    public @Nullable String getFirst_name() {
        return this.first_name;
    }

    public @Nullable String getPatronymic() {
        return this.patronymic;
    }

    public @Nullable String getBirth_date() {
        return this.birth_date;
    }

    public @Nullable String getCitizenship_id() {
        return this.citizenship_id;
    }

    public @Nullable String getCitizenship_ru_name() {
        return this.citizenship_ru_name;
    }

    public @Nullable String getNationality_id() {
        return this.nationality_id;
    }

    public @Nullable String getNationality_ru_name() {
        return this.nationality_ru_name;
    }

    public @Nullable boolean is_resident() {
        return this.is_resident;
    }

    public @Nullable String getLife_status_id() {
        return this.life_status_id;
    }

    public @Nullable String getLife_status_ru_name() {
        return this.life_status_ru_name;
    }

    public @Nullable String getDeath_date() {
        return this.death_date;
    }

    public String getDistrict() {
        return this.district;
    }

    public String getRegion() {
        return this.region;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public void setIin(final @Nullable String iin) {
        this.iin = iin;
    }

    public void setLast_name(final @Nullable String last_name) {
        this.last_name = last_name;
    }

    public void setFirst_name(final @Nullable String first_name) {
        this.first_name = first_name;
    }

    public void setPatronymic(final @Nullable String patronymic) {
        this.patronymic = patronymic;
    }

    public void setBirth_date(final @Nullable String birth_date) {
        this.birth_date = birth_date;
    }

    public void setCitizenship_id(final @Nullable String citizenship_id) {
        this.citizenship_id = citizenship_id;
    }

    public void setCitizenship_ru_name(final @Nullable String citizenship_ru_name) {
        this.citizenship_ru_name = citizenship_ru_name;
    }

    public void setNationality_id(final @Nullable String nationality_id) {
        this.nationality_id = nationality_id;
    }

    public void setNationality_ru_name(final @Nullable String nationality_ru_name) {
        this.nationality_ru_name = nationality_ru_name;
    }

    public void set_resident(final @Nullable boolean is_resident) {
        this.is_resident = is_resident;
    }

    public void setLife_status_id(final @Nullable String life_status_id) {
        this.life_status_id = life_status_id;
    }

    public void setLife_status_ru_name(final @Nullable String life_status_ru_name) {
        this.life_status_ru_name = life_status_ru_name;
    }

    public void setDeath_date(final @Nullable String death_date) {
        this.death_date = death_date;
    }

    public void setDistrict(final String district) {
        this.district = district;
    }

    public void setRegion(final String region) {
        this.region = region;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof MvFl)) {
            return false;
        } else {
            MvFl other = (MvFl)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.is_resident() != other.is_resident()) {
                return false;
            } else {
                label193: {
                    Object this$id = this.getId();
                    Object other$id = other.getId();
                    if (this$id == null) {
                        if (other$id == null) {
                            break label193;
                        }
                    } else if (this$id.equals(other$id)) {
                        break label193;
                    }

                    return false;
                }

                Object this$iin = this.getIin();
                Object other$iin = other.getIin();
                if (this$iin == null) {
                    if (other$iin != null) {
                        return false;
                    }
                } else if (!this$iin.equals(other$iin)) {
                    return false;
                }

                Object this$last_name = this.getLast_name();
                Object other$last_name = other.getLast_name();
                if (this$last_name == null) {
                    if (other$last_name != null) {
                        return false;
                    }
                } else if (!this$last_name.equals(other$last_name)) {
                    return false;
                }

                label172: {
                    Object this$first_name = this.getFirst_name();
                    Object other$first_name = other.getFirst_name();
                    if (this$first_name == null) {
                        if (other$first_name == null) {
                            break label172;
                        }
                    } else if (this$first_name.equals(other$first_name)) {
                        break label172;
                    }

                    return false;
                }

                Object this$patronymic = this.getPatronymic();
                Object other$patronymic = other.getPatronymic();
                if (this$patronymic == null) {
                    if (other$patronymic != null) {
                        return false;
                    }
                } else if (!this$patronymic.equals(other$patronymic)) {
                    return false;
                }

                Object this$birth_date = this.getBirth_date();
                Object other$birth_date = other.getBirth_date();
                if (this$birth_date == null) {
                    if (other$birth_date != null) {
                        return false;
                    }
                } else if (!this$birth_date.equals(other$birth_date)) {
                    return false;
                }

                label151: {
                    Object this$citizenship_id = this.getCitizenship_id();
                    Object other$citizenship_id = other.getCitizenship_id();
                    if (this$citizenship_id == null) {
                        if (other$citizenship_id == null) {
                            break label151;
                        }
                    } else if (this$citizenship_id.equals(other$citizenship_id)) {
                        break label151;
                    }

                    return false;
                }

                Object this$citizenship_ru_name = this.getCitizenship_ru_name();
                Object other$citizenship_ru_name = other.getCitizenship_ru_name();
                if (this$citizenship_ru_name == null) {
                    if (other$citizenship_ru_name != null) {
                        return false;
                    }
                } else if (!this$citizenship_ru_name.equals(other$citizenship_ru_name)) {
                    return false;
                }

                label137: {
                    Object this$nationality_id = this.getNationality_id();
                    Object other$nationality_id = other.getNationality_id();
                    if (this$nationality_id == null) {
                        if (other$nationality_id == null) {
                            break label137;
                        }
                    } else if (this$nationality_id.equals(other$nationality_id)) {
                        break label137;
                    }

                    return false;
                }

                Object this$nationality_ru_name = this.getNationality_ru_name();
                Object other$nationality_ru_name = other.getNationality_ru_name();
                if (this$nationality_ru_name == null) {
                    if (other$nationality_ru_name != null) {
                        return false;
                    }
                } else if (!this$nationality_ru_name.equals(other$nationality_ru_name)) {
                    return false;
                }

                label123: {
                    Object this$life_status_id = this.getLife_status_id();
                    Object other$life_status_id = other.getLife_status_id();
                    if (this$life_status_id == null) {
                        if (other$life_status_id == null) {
                            break label123;
                        }
                    } else if (this$life_status_id.equals(other$life_status_id)) {
                        break label123;
                    }

                    return false;
                }

                Object this$life_status_ru_name = this.getLife_status_ru_name();
                Object other$life_status_ru_name = other.getLife_status_ru_name();
                if (this$life_status_ru_name == null) {
                    if (other$life_status_ru_name != null) {
                        return false;
                    }
                } else if (!this$life_status_ru_name.equals(other$life_status_ru_name)) {
                    return false;
                }

                label109: {
                    Object this$death_date = this.getDeath_date();
                    Object other$death_date = other.getDeath_date();
                    if (this$death_date == null) {
                        if (other$death_date == null) {
                            break label109;
                        }
                    } else if (this$death_date.equals(other$death_date)) {
                        break label109;
                    }

                    return false;
                }

                label102: {
                    Object this$district = this.getDistrict();
                    Object other$district = other.getDistrict();
                    if (this$district == null) {
                        if (other$district == null) {
                            break label102;
                        }
                    } else if (this$district.equals(other$district)) {
                        break label102;
                    }

                    return false;
                }

                Object this$region = this.getRegion();
                Object other$region = other.getRegion();
                if (this$region == null) {
                    if (other$region != null) {
                        return false;
                    }
                } else if (!this$region.equals(other$region)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof MvFl;
    }

    public int hashCode() {
        int result = 1;
        result = result * 59 + (this.is_resident() ? 79 : 97);
        Object $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        Object $iin = this.getIin();
        result = result * 59 + ($iin == null ? 43 : $iin.hashCode());
        Object $last_name = this.getLast_name();
        result = result * 59 + ($last_name == null ? 43 : $last_name.hashCode());
        Object $first_name = this.getFirst_name();
        result = result * 59 + ($first_name == null ? 43 : $first_name.hashCode());
        Object $patronymic = this.getPatronymic();
        result = result * 59 + ($patronymic == null ? 43 : $patronymic.hashCode());
        Object $birth_date = this.getBirth_date();
        result = result * 59 + ($birth_date == null ? 43 : $birth_date.hashCode());
        Object $citizenship_id = this.getCitizenship_id();
        result = result * 59 + ($citizenship_id == null ? 43 : $citizenship_id.hashCode());
        Object $citizenship_ru_name = this.getCitizenship_ru_name();
        result = result * 59 + ($citizenship_ru_name == null ? 43 : $citizenship_ru_name.hashCode());
        Object $nationality_id = this.getNationality_id();
        result = result * 59 + ($nationality_id == null ? 43 : $nationality_id.hashCode());
        Object $nationality_ru_name = this.getNationality_ru_name();
        result = result * 59 + ($nationality_ru_name == null ? 43 : $nationality_ru_name.hashCode());
        Object $life_status_id = this.getLife_status_id();
        result = result * 59 + ($life_status_id == null ? 43 : $life_status_id.hashCode());
        Object $life_status_ru_name = this.getLife_status_ru_name();
        result = result * 59 + ($life_status_ru_name == null ? 43 : $life_status_ru_name.hashCode());
        Object $death_date = this.getDeath_date();
        result = result * 59 + ($death_date == null ? 43 : $death_date.hashCode());
        Object $district = this.getDistrict();
        result = result * 59 + ($district == null ? 43 : $district.hashCode());
        Object $region = this.getRegion();
        result = result * 59 + ($region == null ? 43 : $region.hashCode());
        return result;
    }

    public String toString() {
        String var10000 = String.valueOf(this.getId());
        return "MvFl(id=" + var10000 + ", iin=" + this.getIin() + ", last_name=" + this.getLast_name() + ", first_name=" + this.getFirst_name() + ", patronymic=" + this.getPatronymic() + ", birth_date=" + this.getBirth_date() + ", citizenship_id=" + this.getCitizenship_id() + ", citizenship_ru_name=" + this.getCitizenship_ru_name() + ", nationality_id=" + this.getNationality_id() + ", nationality_ru_name=" + this.getNationality_ru_name() + ", is_resident=" + this.is_resident() + ", life_status_id=" + this.getLife_status_id() + ", life_status_ru_name=" + this.getLife_status_ru_name() + ", death_date=" + this.getDeath_date() + ", district=" + this.getDistrict() + ", region=" + this.getRegion() + ")";
    }
}
