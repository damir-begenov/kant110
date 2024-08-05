package kz.dossier.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KbkGroupingForTaxes {
    private final List<String> forEmployees = new ArrayList<>(Arrays.asList(
            "101201",
            "101204",
            "103101",
            "105433",
            "901101",
            "902101",
            "903101",
            "904101"
            ));
    private final List<String> forOwning = new ArrayList<>(Arrays.asList(
            "104101",
            "104102",
            "104301",
            "104302",
            "104303",
            "104304",
            "104305",
            "104306",
            "104307",
            "104308",
            "104309",
            "104401",
            "104402",
            "104501",
            "105303",
            "105304",
            "105310",
            "105311",
            "105313",
            "105314",
            "105315"
    ));
    private final List<String> forImport = new ArrayList<>(Arrays.asList(
            "105102",
            "105105",
            "105106",
            "105109",
            "105110",
            "105113",
            "105115",
            "105215",
            "105224",
            "105225",
            "105226",
            "105227",
            "105228",
            "105230",
            "105232",
            "105233",
            "105234",
            "105238",
            "105239",
            "105245",
            "105247",
            "105255",
            "105256",
            "105257",
            "105262",
            "105263",
            "105265",
            "105272",
            "105276",
            "105277",
            "105278",
            "105279",
            "105280",
            "105281",
            "105282",
            "105283"
    ));

    public Integer getGroupOfKbk(String code) {
        if (forEmployees.contains(code)) {
            return 0;
        } else if (forOwning.contains(code)) {
            return 1;
        } else {
            return 2;
        }
    }
}
