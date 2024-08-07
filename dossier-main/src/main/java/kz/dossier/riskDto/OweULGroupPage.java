package kz.dossier.riskDto;

import java.util.List;

public class OweULGroupPage {
    private List<OweDlDto> list;
    private Integer pages;

    public List<OweDlDto> getList() {
        return list;
    }

    public void setList(List<OweDlDto> list) {
        this.list = list;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }
}
