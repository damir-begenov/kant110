package kz.dossier.modelsRisk;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.jetbrains.annotations.Nullable;

@Entity
@Table(name = "adm", schema = "imp_kfm_fl")
public class Adm {
    @Column(name = "2")
    @Nullable
    private String two;
    @Nullable
    private String org_identify_crime;
    private String authority_detected;
    @Nullable
    private String divisions_ovd;
    @Nullable
    private String material_num;

    @Nullable
    private String language_prod;
    private String reg_date;
    @Id

    private String protocol_num;
    @Nullable
    private String protocol_date;
    @Nullable
    private String kui_number;
    @Nullable
    private String kui_date;
    @Nullable
    private String erdr_number;
    @Nullable
    private String erdr_date;
    @Nullable
    @Column(name = "15")

    private String fifteen;
    @Nullable
    @Column(name = "16")

    private String sixteen;
    @Nullable
    @Column(name = "17")

    private String seventeen;
    @Nullable
    @Column(name = "18")

    private String eighteen;
    @Nullable
    @Column(name = "19")

    private String nineteen;
    @Nullable
    @Column(name = "20")

    private String twenty;
    @Nullable
    @Column(name = "21")

    private String twenty_one;
    @Nullable

    private String surname;
    @Nullable
    private String firstname;
    @Nullable
    private String secondname;
    @Nullable
    private String birth_date;
    @Nullable
    private String citizenship;
    @Nullable
    private String nationality;
    @Nullable

    private String iin;
    private String work_place;
    @Nullable
    private String phone_num;
    @Nullable
    private String email;
    @Nullable
    private String ul_org_name;
    @Nullable
    private String ul_adress;
    @Nullable
    private String bin;
    @Nullable
    private String vehicle_brand;
    @Nullable
    private String state_auto_num;
    private String qualification;
    private String enforcement;
    @Nullable
    private String id;
    @Nullable
    private String maim_measure;
    private String end_date;
    private String fine_amount;
    private String teminate_reason;
    @Nullable
    private String source;
    @Nullable
    @Column(name = "37")

    private String thirty_seven;
    @Nullable
    @Column(name = "38")

    private String thirty_eight;
    @Nullable
    @Column(name = "39")

    private String thirty_nine;
    @Nullable
    @Column(name = "40")

    private String fourty;
    @Nullable
    @Column(name = "42")

    private String fourty_two;
    @Nullable
    @Column(name = "43")

    private String fourty_three;
    @Nullable
    @Column(name = "44")

    private String fourty_four;
    @Nullable
    @Column(name = "49")

    private String fourty_nine;
    @Nullable
    @Column(name = "51")

    private String fifty_one;
    @Nullable
    @Column(name = "52")

    private String fifty_two;
    @Nullable
    @Column(name = "53")

    private String fifty_three;
    @Nullable
    @Column(name = "54")

    private String fifty_four;
    @Nullable
    @Column(name = "55")

    private String fifty_five;
    @Column(name = "56")
    @Nullable

    private String fifty_six;

    @Nullable
    public String getTwo() {
        return two;
    }

    public void setTwo(@Nullable String two) {
        this.two = two;
    }

    @Nullable
    public String getOrg_identify_crime() {
        return org_identify_crime;
    }

    public void setOrg_identify_crime(@Nullable String org_identify_crime) {
        this.org_identify_crime = org_identify_crime;
    }

    @Nullable
    public String getAuthority_detected() {
        return authority_detected;
    }

    public void setAuthority_detected(@Nullable String authority_detected) {
        this.authority_detected = authority_detected;
    }

    @Nullable
    public String getDivisions_ovd() {
        return divisions_ovd;
    }

    public void setDivisions_ovd(@Nullable String divisions_ovd) {
        this.divisions_ovd = divisions_ovd;
    }

    @Nullable
    public String getMaterial_num() {
        return material_num;
    }

    public void setMaterial_num(@Nullable String material_num) {
        this.material_num = material_num;
    }

    @Nullable
    public String getLanguage_prod() {
        return language_prod;
    }

    public void setLanguage_prod(@Nullable String language_prod) {
        this.language_prod = language_prod;
    }

    @Nullable
    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(@Nullable String reg_date) {
        this.reg_date = reg_date;
    }

    @Nullable
    public String getProtocol_num() {
        return protocol_num;
    }

    public void setProtocol_num(@Nullable String protocol_num) {
        this.protocol_num = protocol_num;
    }

    @Nullable
    public String getProtocol_date() {
        return protocol_date;
    }

    public void setProtocol_date(@Nullable String protocol_date) {
        this.protocol_date = protocol_date;
    }

    @Nullable
    public String getKui_number() {
        return kui_number;
    }

    public void setKui_number(@Nullable String kui_number) {
        this.kui_number = kui_number;
    }

    @Nullable
    public String getKui_date() {
        return kui_date;
    }

    public void setKui_date(@Nullable String kui_date) {
        this.kui_date = kui_date;
    }

    @Nullable
    public String getErdr_number() {
        return erdr_number;
    }

    public void setErdr_number(@Nullable String erdr_number) {
        this.erdr_number = erdr_number;
    }

    @Nullable
    public String getErdr_date() {
        return erdr_date;
    }

    public void setErdr_date(@Nullable String erdr_date) {
        this.erdr_date = erdr_date;
    }

    @Nullable
    public String getFifteen() {
        return fifteen;
    }

    public void setFifteen(@Nullable String fifteen) {
        this.fifteen = fifteen;
    }

    @Nullable
    public String getSixteen() {
        return sixteen;
    }

    public void setSixteen(@Nullable String sixteen) {
        this.sixteen = sixteen;
    }

    @Nullable
    public String getSeventeen() {
        return seventeen;
    }

    public void setSeventeen(@Nullable String seventeen) {
        this.seventeen = seventeen;
    }

    @Nullable
    public String getEighteen() {
        return eighteen;
    }

    public void setEighteen(@Nullable String eighteen) {
        this.eighteen = eighteen;
    }

    @Nullable
    public String getNineteen() {
        return nineteen;
    }

    public void setNineteen(@Nullable String nineteen) {
        this.nineteen = nineteen;
    }

    @Nullable
    public String getTwenty() {
        return twenty;
    }

    public void setTwenty(@Nullable String twenty) {
        this.twenty = twenty;
    }

    @Nullable
    public String getTwenty_one() {
        return twenty_one;
    }

    public void setTwenty_one(@Nullable String twenty_one) {
        this.twenty_one = twenty_one;
    }

    @Nullable
    public String getSurname() {
        return surname;
    }

    public void setSurname(@Nullable String surname) {
        this.surname = surname;
    }

    @Nullable
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(@Nullable String firstname) {
        this.firstname = firstname;
    }

    @Nullable
    public String getSecondname() {
        return secondname;
    }

    public void setSecondname(@Nullable String secondname) {
        this.secondname = secondname;
    }

    @Nullable
    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(@Nullable String birth_date) {
        this.birth_date = birth_date;
    }

    @Nullable
    public String getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(@Nullable String citizenship) {
        this.citizenship = citizenship;
    }

    @Nullable
    public String getNationality() {
        return nationality;
    }

    public void setNationality(@Nullable String nationality) {
        this.nationality = nationality;
    }

    @Nullable
    public String getIin() {
        return iin;
    }

    public void setIin(@Nullable String iin) {
        this.iin = iin;
    }

    @Nullable
    public String getWork_place() {
        return work_place;
    }

    public void setWork_place(@Nullable String work_place) {
        this.work_place = work_place;
    }

    @Nullable
    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(@Nullable String phone_num) {
        this.phone_num = phone_num;
    }

    @Nullable
    public String getEmail() {
        return email;
    }

    public void setEmail(@Nullable String email) {
        this.email = email;
    }

    @Nullable
    public String getUl_org_name() {
        return ul_org_name;
    }

    public void setUl_org_name(@Nullable String ul_org_name) {
        this.ul_org_name = ul_org_name;
    }

    @Nullable
    public String getUl_adress() {
        return ul_adress;
    }

    public void setUl_adress(@Nullable String ul_adress) {
        this.ul_adress = ul_adress;
    }

    @Nullable
    public String getBin() {
        return bin;
    }

    public void setBin(@Nullable String bin) {
        this.bin = bin;
    }

    @Nullable
    public String getVehicle_brand() {
        return vehicle_brand;
    }

    public void setVehicle_brand(@Nullable String vehicle_brand) {
        this.vehicle_brand = vehicle_brand;
    }

    @Nullable
    public String getState_auto_num() {
        return state_auto_num;
    }

    public void setState_auto_num(@Nullable String state_auto_num) {
        this.state_auto_num = state_auto_num;
    }

    @Nullable
    public String getQualification() {
        return qualification;
    }

    public void setQualification(@Nullable String qualification) {
        this.qualification = qualification;
    }

    @Nullable
    public String getEnforcement() {
        return enforcement;
    }

    public void setEnforcement(@Nullable String enforcement) {
        this.enforcement = enforcement;
    }

    @Nullable
    public String getId() {
        return id;
    }

    public void setId(@Nullable String id) {
        this.id = id;
    }

    @Nullable
    public String getMaim_measure() {
        return maim_measure;
    }

    public void setMaim_measure(@Nullable String maim_measure) {
        this.maim_measure = maim_measure;
    }

    @Nullable
    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(@Nullable String end_date) {
        this.end_date = end_date;
    }

    @Nullable
    public String getFine_amount() {
        return fine_amount;
    }

    public void setFine_amount(@Nullable String fine_amount) {
        this.fine_amount = fine_amount;
    }

    @Nullable
    public String getTeminate_reason() {
        return teminate_reason;
    }

    public void setTeminate_reason(@Nullable String teminate_reason) {
        this.teminate_reason = teminate_reason;
    }

    @Nullable
    public String getSource() {
        return source;
    }

    public void setSource(@Nullable String source) {
        this.source = source;
    }

    @Nullable
    public String getThirty_seven() {
        return thirty_seven;
    }

    public void setThirty_seven(@Nullable String thirty_seven) {
        this.thirty_seven = thirty_seven;
    }

    @Nullable
    public String getThirty_eight() {
        return thirty_eight;
    }

    public void setThirty_eight(@Nullable String thirty_eight) {
        this.thirty_eight = thirty_eight;
    }

    @Nullable
    public String getThirty_nine() {
        return thirty_nine;
    }

    public void setThirty_nine(@Nullable String thirty_nine) {
        this.thirty_nine = thirty_nine;
    }

    @Nullable
    public String getFourty() {
        return fourty;
    }

    public void setFourty(@Nullable String fourty) {
        this.fourty = fourty;
    }

    @Nullable
    public String getFourty_two() {
        return fourty_two;
    }

    public void setFourty_two(@Nullable String fourty_two) {
        this.fourty_two = fourty_two;
    }

    @Nullable
    public String getFourty_three() {
        return fourty_three;
    }

    public void setFourty_three(@Nullable String fourty_three) {
        this.fourty_three = fourty_three;
    }

    @Nullable
    public String getFourty_four() {
        return fourty_four;
    }

    public void setFourty_four(@Nullable String fourty_four) {
        this.fourty_four = fourty_four;
    }

    @Nullable
    public String getFourty_nine() {
        return fourty_nine;
    }

    public void setFourty_nine(@Nullable String fourty_nine) {
        this.fourty_nine = fourty_nine;
    }

    @Nullable
    public String getFifty_one() {
        return fifty_one;
    }

    public void setFifty_one(@Nullable String fifty_one) {
        this.fifty_one = fifty_one;
    }

    @Nullable
    public String getFifty_two() {
        return fifty_two;
    }

    public void setFifty_two(@Nullable String fifty_two) {
        this.fifty_two = fifty_two;
    }

    @Nullable
    public String getFifty_three() {
        return fifty_three;
    }

    public void setFifty_three(@Nullable String fifty_three) {
        this.fifty_three = fifty_three;
    }

    @Nullable
    public String getFifty_four() {
        return fifty_four;
    }

    public void setFifty_four(@Nullable String fifty_four) {
        this.fifty_four = fifty_four;
    }

    @Nullable
    public String getFifty_five() {
        return fifty_five;
    }

    public void setFifty_five(@Nullable String fifty_five) {
        this.fifty_five = fifty_five;
    }

    @Nullable
    public String getFifty_six() {
        return fifty_six;
    }

    public void setFifty_six(@Nullable String fifty_six) {
        this.fifty_six = fifty_six;
    }
}
