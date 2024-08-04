//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package kz.dossier.modelsRisk;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.jetbrains.annotations.Nullable;

@Entity
@Table(
        name = "adm",
        schema = "imp_kfm_fl"
)
public class Adm {
    @Column(
            name = "2"
    )
    private @Nullable String two;
    private @Nullable String org_identify_crime;
    private @Nullable String authority_detected;
    private @Nullable String divisions_ovd;
    @Id
    private @Nullable String material_num;
    private @Nullable String language_prod;
    private @Nullable String reg_date;
    private @Nullable String protocol_num;
    private @Nullable String protocol_date;
    private @Nullable String kui_number;
    private @Nullable String kui_date;
    private @Nullable String erdr_number;
    private @Nullable String erdr_date;
    @Column(
            name = "15"
    )
    private @Nullable String fifteen;
    @Column(
            name = "16"
    )
    private @Nullable String sixteen;
    @Column(
            name = "17"
    )
    private @Nullable String seventeen;
    @Column(
            name = "18"
    )
    private @Nullable String eighteen;
    @Column(
            name = "19"
    )
    private @Nullable String nineteen;
    @Column(
            name = "20"
    )
    private @Nullable String twenty;
    @Column(
            name = "21"
    )
    private @Nullable String twenty_one;
    private @Nullable String surname;
    private @Nullable String firstname;
    private @Nullable String secondname;
    private @Nullable String birth_date;
    private @Nullable String citizenship;
    private @Nullable String nationality;
    private @Nullable String iin;
    private @Nullable String work_place;
    private @Nullable String phone_num;
    private @Nullable String email;
    private @Nullable String ul_org_name;
    private @Nullable String ul_adress;
    private @Nullable String bin;
    private @Nullable String vehicle_brand;
    private @Nullable String state_auto_num;
    private @Nullable String qualification;
    private @Nullable String enforcement;
    private @Nullable String id;
    private @Nullable String maim_measure;
    private @Nullable String end_date;
    private @Nullable String fine_amount;
    private @Nullable String teminate_reason;
    private @Nullable String source;
    @Column(
            name = "37"
    )
    private @Nullable String thirty_seven;
    @Column(
            name = "38"
    )
    private @Nullable String thirty_eight;
    @Column(
            name = "39"
    )
    private @Nullable String thirty_nine;
    @Column(
            name = "40"
    )
    private @Nullable String fourty;
    @Column(
            name = "42"
    )
    private @Nullable String fourty_two;
    @Column(
            name = "43"
    )
    private @Nullable String fourty_three;
    @Column(
            name = "44"
    )
    private @Nullable String fourty_four;
    @Column(
            name = "49"
    )
    private @Nullable String fourty_nine;
    @Column(
            name = "51"
    )
    private @Nullable String fifty_one;
    @Column(
            name = "52"
    )
    private @Nullable String fifty_two;
    @Column(
            name = "53"
    )
    private @Nullable String fifty_three;
    @Column(
            name = "54"
    )
    private @Nullable String fifty_four;
    @Column(
            name = "55"
    )
    private @Nullable String fifty_five;
    @Column(
            name = "56"
    )
    private @Nullable String fifty_six;
    private String qualification_name;

    public Adm() {
    }

    public String getQualification_name() {
        return this.qualification_name;
    }

    public void setQualification_name(String qualification_name) {
        this.qualification_name = qualification_name;
    }

    public @Nullable String getTwo() {
        return this.two;
    }

    public void setTwo(@Nullable String two) {
        this.two = two;
    }

    public @Nullable String getOrg_identify_crime() {
        return this.org_identify_crime;
    }

    public void setOrg_identify_crime(@Nullable String org_identify_crime) {
        this.org_identify_crime = org_identify_crime;
    }

    public @Nullable String getAuthority_detected() {
        return this.authority_detected;
    }

    public void setAuthority_detected(@Nullable String authority_detected) {
        this.authority_detected = authority_detected;
    }

    public @Nullable String getDivisions_ovd() {
        return this.divisions_ovd;
    }

    public void setDivisions_ovd(@Nullable String divisions_ovd) {
        this.divisions_ovd = divisions_ovd;
    }

    public @Nullable String getMaterial_num() {
        return this.material_num;
    }

    public void setMaterial_num(@Nullable String material_num) {
        this.material_num = material_num;
    }

    public @Nullable String getLanguage_prod() {
        return this.language_prod;
    }

    public void setLanguage_prod(@Nullable String language_prod) {
        this.language_prod = language_prod;
    }

    public @Nullable String getReg_date() {
        return this.reg_date;
    }

    public void setReg_date(@Nullable String reg_date) {
        this.reg_date = reg_date;
    }

    public @Nullable String getProtocol_num() {
        return this.protocol_num;
    }

    public void setProtocol_num(@Nullable String protocol_num) {
        this.protocol_num = protocol_num;
    }

    public @Nullable String getProtocol_date() {
        return this.protocol_date;
    }

    public void setProtocol_date(@Nullable String protocol_date) {
        this.protocol_date = protocol_date;
    }

    public @Nullable String getKui_number() {
        return this.kui_number;
    }

    public void setKui_number(@Nullable String kui_number) {
        this.kui_number = kui_number;
    }

    public @Nullable String getKui_date() {
        return this.kui_date;
    }

    public void setKui_date(@Nullable String kui_date) {
        this.kui_date = kui_date;
    }

    public @Nullable String getErdr_number() {
        return this.erdr_number;
    }

    public void setErdr_number(@Nullable String erdr_number) {
        this.erdr_number = erdr_number;
    }

    public @Nullable String getErdr_date() {
        return this.erdr_date;
    }

    public void setErdr_date(@Nullable String erdr_date) {
        this.erdr_date = erdr_date;
    }

    public @Nullable String getFifteen() {
        return this.fifteen;
    }

    public void setFifteen(@Nullable String fifteen) {
        this.fifteen = fifteen;
    }

    public @Nullable String getSixteen() {
        return this.sixteen;
    }

    public void setSixteen(@Nullable String sixteen) {
        this.sixteen = sixteen;
    }

    public @Nullable String getSeventeen() {
        return this.seventeen;
    }

    public void setSeventeen(@Nullable String seventeen) {
        this.seventeen = seventeen;
    }

    public @Nullable String getEighteen() {
        return this.eighteen;
    }

    public void setEighteen(@Nullable String eighteen) {
        this.eighteen = eighteen;
    }

    public @Nullable String getNineteen() {
        return this.nineteen;
    }

    public void setNineteen(@Nullable String nineteen) {
        this.nineteen = nineteen;
    }

    public @Nullable String getTwenty() {
        return this.twenty;
    }

    public void setTwenty(@Nullable String twenty) {
        this.twenty = twenty;
    }

    public @Nullable String getTwenty_one() {
        return this.twenty_one;
    }

    public void setTwenty_one(@Nullable String twenty_one) {
        this.twenty_one = twenty_one;
    }

    public @Nullable String getSurname() {
        return this.surname;
    }

    public void setSurname(@Nullable String surname) {
        this.surname = surname;
    }

    public @Nullable String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(@Nullable String firstname) {
        this.firstname = firstname;
    }

    public @Nullable String getSecondname() {
        return this.secondname;
    }

    public void setSecondname(@Nullable String secondname) {
        this.secondname = secondname;
    }

    public @Nullable String getBirth_date() {
        return this.birth_date;
    }

    public void setBirth_date(@Nullable String birth_date) {
        this.birth_date = birth_date;
    }

    public @Nullable String getCitizenship() {
        return this.citizenship;
    }

    public void setCitizenship(@Nullable String citizenship) {
        this.citizenship = citizenship;
    }

    public @Nullable String getNationality() {
        return this.nationality;
    }

    public void setNationality(@Nullable String nationality) {
        this.nationality = nationality;
    }

    public @Nullable String getIin() {
        return this.iin;
    }

    public void setIin(@Nullable String iin) {
        this.iin = iin;
    }

    public @Nullable String getWork_place() {
        return this.work_place;
    }

    public void setWork_place(@Nullable String work_place) {
        this.work_place = work_place;
    }

    public @Nullable String getPhone_num() {
        return this.phone_num;
    }

    public void setPhone_num(@Nullable String phone_num) {
        this.phone_num = phone_num;
    }

    public @Nullable String getEmail() {
        return this.email;
    }

    public void setEmail(@Nullable String email) {
        this.email = email;
    }

    public @Nullable String getUl_org_name() {
        return this.ul_org_name;
    }

    public void setUl_org_name(@Nullable String ul_org_name) {
        this.ul_org_name = ul_org_name;
    }

    public @Nullable String getUl_adress() {
        return this.ul_adress;
    }

    public void setUl_adress(@Nullable String ul_adress) {
        this.ul_adress = ul_adress;
    }

    public @Nullable String getBin() {
        return this.bin;
    }

    public void setBin(@Nullable String bin) {
        this.bin = bin;
    }

    public @Nullable String getVehicle_brand() {
        return this.vehicle_brand;
    }

    public void setVehicle_brand(@Nullable String vehicle_brand) {
        this.vehicle_brand = vehicle_brand;
    }

    public @Nullable String getState_auto_num() {
        return this.state_auto_num;
    }

    public void setState_auto_num(@Nullable String state_auto_num) {
        this.state_auto_num = state_auto_num;
    }

    public @Nullable String getQualification() {
        return this.qualification;
    }

    public void setQualification(@Nullable String qualification) {
        this.qualification = qualification;
    }

    public @Nullable String getEnforcement() {
        return this.enforcement;
    }

    public void setEnforcement(@Nullable String enforcement) {
        this.enforcement = enforcement;
    }

    public @Nullable String getId() {
        return this.id;
    }

    public void setId(@Nullable String id) {
        this.id = id;
    }

    public @Nullable String getMaim_measure() {
        return this.maim_measure;
    }

    public void setMaim_measure(@Nullable String maim_measure) {
        this.maim_measure = maim_measure;
    }

    public @Nullable String getEnd_date() {
        return this.end_date;
    }

    public void setEnd_date(@Nullable String end_date) {
        this.end_date = end_date;
    }

    public @Nullable String getFine_amount() {
        return this.fine_amount;
    }

    public void setFine_amount(@Nullable String fine_amount) {
        this.fine_amount = fine_amount;
    }

    public @Nullable String getTeminate_reason() {
        return this.teminate_reason;
    }

    public void setTeminate_reason(@Nullable String teminate_reason) {
        this.teminate_reason = teminate_reason;
    }

    public @Nullable String getSource() {
        return this.source;
    }

    public void setSource(@Nullable String source) {
        this.source = source;
    }

    public @Nullable String getThirty_seven() {
        return this.thirty_seven;
    }

    public void setThirty_seven(@Nullable String thirty_seven) {
        this.thirty_seven = thirty_seven;
    }

    public @Nullable String getThirty_eight() {
        return this.thirty_eight;
    }

    public void setThirty_eight(@Nullable String thirty_eight) {
        this.thirty_eight = thirty_eight;
    }

    public @Nullable String getThirty_nine() {
        return this.thirty_nine;
    }

    public void setThirty_nine(@Nullable String thirty_nine) {
        this.thirty_nine = thirty_nine;
    }

    public @Nullable String getFourty() {
        return this.fourty;
    }

    public void setFourty(@Nullable String fourty) {
        this.fourty = fourty;
    }

    public @Nullable String getFourty_two() {
        return this.fourty_two;
    }

    public void setFourty_two(@Nullable String fourty_two) {
        this.fourty_two = fourty_two;
    }

    public @Nullable String getFourty_three() {
        return this.fourty_three;
    }

    public void setFourty_three(@Nullable String fourty_three) {
        this.fourty_three = fourty_three;
    }

    public @Nullable String getFourty_four() {
        return this.fourty_four;
    }

    public void setFourty_four(@Nullable String fourty_four) {
        this.fourty_four = fourty_four;
    }

    public @Nullable String getFourty_nine() {
        return this.fourty_nine;
    }

    public void setFourty_nine(@Nullable String fourty_nine) {
        this.fourty_nine = fourty_nine;
    }

    public @Nullable String getFifty_one() {
        return this.fifty_one;
    }

    public void setFifty_one(@Nullable String fifty_one) {
        this.fifty_one = fifty_one;
    }

    public @Nullable String getFifty_two() {
        return this.fifty_two;
    }

    public void setFifty_two(@Nullable String fifty_two) {
        this.fifty_two = fifty_two;
    }

    public @Nullable String getFifty_three() {
        return this.fifty_three;
    }

    public void setFifty_three(@Nullable String fifty_three) {
        this.fifty_three = fifty_three;
    }

    public @Nullable String getFifty_four() {
        return this.fifty_four;
    }

    public void setFifty_four(@Nullable String fifty_four) {
        this.fifty_four = fifty_four;
    }

    public @Nullable String getFifty_five() {
        return this.fifty_five;
    }

    public void setFifty_five(@Nullable String fifty_five) {
        this.fifty_five = fifty_five;
    }

    public @Nullable String getFifty_six() {
        return this.fifty_six;
    }

    public void setFifty_six(@Nullable String fifty_six) {
        this.fifty_six = fifty_six;
    }
}
