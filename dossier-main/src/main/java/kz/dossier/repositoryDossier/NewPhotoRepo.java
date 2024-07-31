package kz.dossier.repositoryDossier;

import kz.dossier.modelsDossier.PhotoDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewPhotoRepo extends JpaRepository<PhotoDb, Long> {
    @Query(value= "select distinct * from import_fl.photo where iin = ?1 order by \"date\" desc", nativeQuery = true)

    List<PhotoDb> findAllByIinv(String iin);
    @Query(nativeQuery = true, value = "select * from import_fl.photo p where p.iin=:iin order by \"date\" desc limit 1")
    Optional<PhotoDb> findById(String iin);
    @Query(value= "select distinct * from import_fl.photo where iin = ?1 and date = (select max(date) from  import_fl.photo where iin = ?1) limit 1", nativeQuery = true)
    PhotoDb findLastPhoto(String iin);
}
