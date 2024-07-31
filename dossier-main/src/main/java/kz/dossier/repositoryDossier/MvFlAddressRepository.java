package kz.dossier.repositoryDossier;

import kz.dossier.modelsDossier.MvFlAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MvFlAddressRepository extends JpaRepository<MvFlAddress, Long> {
    @Query(value = "select * from imp_kfm_fl.mv_fl_address where \"IIN\" = ?1", nativeQuery = true)
    List<MvFlAddress> getMvFlAddressByIIN(String iin);
    @Query(value = "select * from imp_kfm_fl.mv_fl_address where \"RN_ADDRESS_RU\" = ?1 and \"IIN\" != ?2", nativeQuery = true)
    List<MvFlAddress> getMvFlAddressByRnAddress(String rn_address, String iin);
}
