package kz.dossier.modelsDossier;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Date;

@Entity
@Table(name = "dubai_rn", schema = "imp_rn")
public class DubaiRn {
    @Id
    private Long id;
    private String iin;
    private Date date_seller;
    private Double volume;
    private String zhk_kompleks;
    private String number_flats;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIin() {
        return iin;
    }

    public void setIin(String iin) {
        this.iin = iin;
    }

    public Date getDate_seller() {
        return date_seller;
    }

    public void setDate_seller(Date date_seller) {
        this.date_seller = date_seller;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public String getZhk_kompleks() {
        return zhk_kompleks;
    }

    public void setZhk_kompleks(String zhk_kompleks) {
        this.zhk_kompleks = zhk_kompleks;
    }

    public String getNumber_flats() {
        return number_flats;
    }

    public void setNumber_flats(String number_flats) {
        this.number_flats = number_flats;
    }
}
