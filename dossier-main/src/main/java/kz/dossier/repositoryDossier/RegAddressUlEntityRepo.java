package kz.dossier.repositoryDossier;

import kz.dossier.modelsDossier.RegAddressUlEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RegAddressUlEntityRepo extends JpaRepository<RegAddressUlEntity, Long> {
    @Query(value= "select * from imp_kfm_ul.mv_reg_address_ul mv_ul0_ where mv_ul0_.bin = ?1 AND is_active = true ORDER BY reg_date desc limit 1", nativeQuery = true)
    RegAddressUlEntity findByBin(String bin);

    @Query(value= "select count(*) from imp_kfm_ul.mv_reg_address_ul mv_ul0_ where mv_ul0_.bin = ?1 AND is_active = true limit 1", nativeQuery = true)
    Integer countByBin(String bin);
    @Query(value= "SELECT  * FROM imp_kfm_ul.mv_reg_address_ul where reg_addr_region_ru = ?1 and reg_addr_district_ru = ?2 and reg_addr_locality_ru= ?3 and reg_addr_street_ru = ?4 and reg_addr_bulding_num = ?5 and bin != ?6 order by reg_date desc limit 1", nativeQuery = true)

    RegAddressUlEntity regAddressNaOdnomMeste(String region,String disctrict, String locality, String street, String bulding, String bin);

    @Query(value = "SELECT distinct * FROM imp_kfm_ul.mv_reg_address_ul WHERE  reg_addr_region_ru = ?1 AND reg_addr_district_ru = ?2 AND reg_addr_street_ru = ?3 AND reg_addr_bulding_num = ?4 AND is_active = true AND bin != ?5", nativeQuery = true)
    List<RegAddressUlEntity> getByAddress(String region, String district, String reg_addr_street_ru, String reg_addr_bulding_num,String bin);

    @Query(value= "SELECT  * FROM imp_kfm_ul.mv_reg_address_ul where reg_addr_region_ru = ?1 and reg_addr_district_ru = ?2 and reg_addr_locality_ru= ?3 and reg_addr_street_ru = ?4 and reg_addr_bulding_num = ?5 and bin != ?6 is_active = true", nativeQuery = true)

    List<RegAddressUlEntity> getByFullAddress(String region,String disctrict, String locality, String street, String bulding, String bin);

}