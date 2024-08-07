package kz.dossier.repositoryDossier;


import kz.dossier.modelsDossier.FlPensionContr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface FlPensionContrRepo extends JpaRepository<FlPensionContr, Long> {


    @Query(value = "select extract(year from \"PAY_DATE\") as year, count(distinct \"IIN\") as person_count from imp_kfm_fl.fl_pension_contr where \"P_RNN\" = ?1 group by extract(year from \"PAY_DATE\") order by extract(year from \"PAY_DATE\") desc", nativeQuery = true)
    List<Map<String, Object>> getYearGroupedForBin(String bin);
    @Query(value = "select count(distinct \"IIN\") from imp_kfm_fl.fl_pension_contr where \"P_RNN\" = ?1 limit 1", nativeQuery = true)
    Integer countByBin(String bin);
    @Query(value = "select distinct(extract (year from \"PAY_DATE\")) as year from imp_kfm_fl.fl_pension_contr where \"P_RNN\" = ?1 order BY year DESC", nativeQuery = true)
    List<Integer> getYearsOfPension(String bin);
    @Query(value= "select * from imp_kfm_fl.fl_pension_contr  where \"IIN\" = ?1", nativeQuery = true)
    List<FlPensionContr> getUsersByLike(String iin);
    @Query(value= "select distinct(\"P_RNN\") from imp_kfm_fl.fl_pension_contr  where \"IIN\" = ?1 ", nativeQuery = true)
    List<String> getUsersByLikeCompany(String iin);
//    SELECT  SUM("AMOUNT") AS AMOUNT, "KNP"
//    FROM imp_kfm_fl.fl_pension_contr
//    WHERE "IIN" = '810615301348' and "P_RNN" = '061600005040'
//    GROUP BY "KNP"
    @Query(value= "SELECT  cast(SUM(\"AMOUNT\") as text) AS AMOUNT, \"KNP\"\n" +
            "             FROM imp_kfm_fl.fl_pension_contr \n" +
            "            WHERE \"IIN\" = ?1 and \"P_RNN\" = ?2\n" +
            "                GROUP BY \"KNP\" ", nativeQuery = true)
    List<Map<String,Object>> findAmountOfAmountByKNP(String iin, String bin);
    @Query(value= "SELECT DISTINCT \"IIN\", " +
            "cast(concat(\"SURNAME\" || ' ' || \"FIRSTNAME\" || ' ' || \"SECONDNAME\") as text) as fio, " +
            "cast(SUM(case when \"KNP\"='010' then \"AMOUNT\" else 0 END) as text) AS zeroten, " +
            "cast(SUM(case when \"KNP\"='012' then \"AMOUNT\" else 0 END) as text) AS zerotwelve " +
            "FROM imp_kfm_fl.fl_pension_contr " +
            "WHERE cast(extract(year from \"PAY_DATE\") as text) = :year and \"P_RNN\" = :bin and \"FIRSTNAME\" notnull " +
            "GROUP BY concat(\"SURNAME\" || ' ' || \"FIRSTNAME\" || ' ' || \"SECONDNAME\"), \"IIN\"", nativeQuery = true)
    Page<Map<String,Object>> getPension(String bin, String year, PageRequest pageRequest);
    @Query(value= "SELECT DISTINCT \"IIN\", " +
            "cast(concat(\"SURNAME\" || ' ' || \"FIRSTNAME\" || ' ' || \"SECONDNAME\") as text) as fio, " +
            "cast(SUM(case when \"KNP\"='010' then \"AMOUNT\" else 0 END) as text) AS zeroten, " +
            "cast(SUM(case when \"KNP\"='012' then \"AMOUNT\" else 0 END) as text) AS zerotwelve " +
            "FROM imp_kfm_fl.fl_pension_contr " +
            "WHERE extract(year from \"PAY_DATE\") = ?2 and \"P_RNN\" = ?1 and \"FIRSTNAME\" notnull " +
            "GROUP BY concat(\"SURNAME\" || ' ' || \"FIRSTNAME\" || ' ' || \"SECONDNAME\"), \"IIN\" LIMIT 10 OFFSET ?3", nativeQuery = true)
    List<Map<String,Object>> getPension1(String bin, Double year, Integer offset);
    @Query(value = "SELECT extract(year from \"PAY_DATE\"), COUNT(DISTINCT \"IIN\") AS iin_count\n" +
            "FROM imp_kfm_fl.fl_pension_contr \n" +
            "WHERE \"P_RNN\" = ?1 \n" +
            "GROUP BY extract(year from \"PAY_DATE\")", nativeQuery = true)
    List<Map<String,Object>> findAmountOfEmployeesOfEveryYear(String Bin);
    @Query(value = "SELECT EXTRACT(YEAR FROM \"PAY_DATE\") AS PAY_DATE,\n" +
            "       \"P_NAME\", \"KNP\",\n" +
            "    SUM(\"AMOUNT\") AS \"AMOUNT\"\n" +
            "    FROM imp_kfm_fl.fl_pension_contr \n" +
            "    WHERE \"IIN\" = ?1 and \"P_RNN\" = ?2 \n" +
            "    GROUP BY EXTRACT(YEAR FROM \"PAY_DATE\"), \"P_NAME\", \"KNP\" ORDER BY EXTRACT(YEAR FROM \"PAY_DATE\") DESC", nativeQuery = true)
    List<Map<String,Object>> getAllByCompanies(String iin , String bin);

    @Query(value = "SELECT TO_CHAR(DATE_TRUNC('month', \"PAY_DATE\"), 'YYYY-MM') AS PAY_MONTH,\"P_NAME\",\n" + //
                    "       SUM(\"AMOUNT\") AS \"AMOUNT\"\n" + //
                    "FROM imp_kfm_fl.fl_pension_contr\n" + //
                    "WHERE \"IIN\" = ?1 and \"KNP\" = '010' and \"P_RNN\" = ?2\n" + //
                    "AND EXTRACT(YEAR FROM \"PAY_DATE\") = ?3\n" + //
                    "GROUP BY TO_CHAR(DATE_TRUNC('month', \"PAY_DATE\"), 'YYYY-MM'), \"P_NAME\" ORDER BY PAY_MONTH DESC", nativeQuery = true)
        List<Map<String,Object>> getAllByCompanies(String iin, String bin, int year);

    @Query(value = "SELECT distinct (extract(year from \"PAY_DATE\"))\n" +
            "FROM imp_kfm_fl.fl_pension_contr where \"P_RNN\" = '091040009041'  group by \"PAY_DATE\"", nativeQuery = true)
    List<Integer> amountOfYears(String BIN);
    @Query(value = "SELECT  COUNT(DISTINCT \"IIN\") AS iin_count\n" +
            "FROM imp_kfm_fl.fl_pension_contr \n" +
            "WHERE \"P_RNN\" = ?1 and extract(year from \"PAY_DATE\") = ?2 \n" +
            "GROUP BY extract(year from \"PAY_DATE\")", nativeQuery = true)
    Integer amountOfEmp(String BIN, Integer year);



    @Query(value = "select distinct (extract (year from fpc.\"PAY_DATE\")) from imp_kfm_fl.fl_pension_contr where \"P_RNN\" = ?1", nativeQuery = true)
    List<Integer> getYearThatExist(String bin);
}
