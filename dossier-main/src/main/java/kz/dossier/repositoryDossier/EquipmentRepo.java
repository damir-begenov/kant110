package kz.dossier.repositoryDossier;


import kz.dossier.modelsDossier.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EquipmentRepo extends JpaRepository<Equipment, Long> {
    @Query(value= "select * from imp_kfm_fl.equipment equipment0_ where equipment0_.proprietor_name_iin_bin = ?1", nativeQuery = true)
    List<Equipment> getUsersByLike(String iin);

    @Query(value= "select count(*) from imp_kfm_fl.equipment equipment0_ where equipment0_.proprietor_name_iin_bin = ?1 limit 1", nativeQuery = true)
    Integer countByBin(String iin);

}
