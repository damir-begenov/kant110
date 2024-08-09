package kz.dossier.tools;

import kz.dossier.dto.GosZakupDTO;
import kz.dossier.dto.GosZakupForAll;
import kz.dossier.dto.PensionGroupDTO;
import kz.dossier.dto.PensionListDTO;
import kz.dossier.modelsDossier.*;
import kz.dossier.modelsRisk.*;
import kz.dossier.repositoryDossier.*;
import kz.dossier.service.MyService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff1;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DocxGenerator {
    @Autowired
    FlPensionMiniRepo flPensionMiniRepo;
    @Autowired
    MyService myService;
    @Autowired
    AdmRepo admRepo;
    @Autowired
    FlContactsRepo flContactsRepo;
    @Autowired
    IndividualEntrepreneurRepo individualEntrepreneurRepo;
    @Autowired
    FlPensionContrRepo flPensionContrRepo;
    @Autowired
    KxRepo kxRepo;
    @Autowired
    MvUlLeaderEntityRepo mvUlLeaderEntityRepo;
    @Autowired
    MvUlRepo mvUlRepo;
    @Autowired
    ChangeFioRepo changeFioRepo;
    @Autowired
    ULExportPDFService ulExportPDFService;
    private XWPFDocument makeTableByProperties(XWPFDocument doc, XWPFTable table, String title, List<String> properties) {
        table.setWidth("100%");
        XWPFTableRow row = table.getRow(0);
        // Create cells for the header row
        for (int i = 0; i < properties.size(); i++) {
            if (i == 0) {
                row.getCell(0).setText(properties.get(i));
            } else {
                row.addNewTableCell().setText(properties.get(i));
            }
        }
        return doc;
    }

    private void setMarginBetweenTables(XWPFDocument doc) {
        XWPFParagraph paragraph = doc.createParagraph();
        XWPFRun run2 = paragraph.createRun();
        run2.addBreak();  // Добавляем перенос строки
        run2.setText(" "); // Добавляем пробел, чтобы создать визуальный отступ
    }

    private void creteTitle(XWPFDocument doc,String title){
        XWPFParagraph titleParagraph = doc.createParagraph();
        XWPFRun titleRun = titleParagraph.createRun();
        titleRun.setText(title);
        titleRun.setBold(true);
        titleRun.setFontSize(14);
    }

    public void generateDoccc(NodesFL result, ByteArrayOutputStream baos) throws IOException {
            try (XWPFDocument doc = new XWPFDocument()) {
                CTDocument1 document = doc.getDocument();
                CTBody body = document.getBody();

                if (!body.isSetSectPr()) {
                    body.addNewSectPr();
                }
                CTSectPr section = body.getSectPr();

                if(!section.isSetPgSz()) {
                    section.addNewPgSz();
                }
                CTPageSz pageSize = section.getPgSz();

                pageSize.setW(BigInteger.valueOf(15840));
                pageSize.setH(BigInteger.valueOf(12240));

                try {
                    if (result.getMvFls() != null || result.getMvFls().size() < 0) {
                        creteTitle(doc,"Сведения о физическом лице");
                        XWPFTable table = doc.createTable();
                        makeTableByProperties(doc, table, "Сведения о физическом лице", Arrays.asList(
                                "Фото",
                                "ИИН",
                                "ФИО",
                                "Резидент",
                                "Национальность",
                                "Дата смерти"));

                        XWPFTableRow row1 = table.createRow();
                        XWPFTableCell cell1 = row1.getCell(0);

                        XWPFParagraph paragraph2 = cell1.addParagraph();

                        setCellPadding(cell1, 200, 200, 200, 200);
                        XWPFRun run1 = paragraph2.createRun();

                        byte[] imageBytes = result.getPhotoDbf().get(0).getPhoto();
                        ByteArrayInputStream imageStream = new ByteArrayInputStream(imageBytes);
                        int imageType = XWPFDocument.PICTURE_TYPE_PNG; // Change according to your image type (e.g., PICTURE_TYPE_JPEG)
                        run1.addPicture(imageStream, imageType, "image.png", Units.toEMU(75), Units.toEMU(100));
                        row1.getCell(1).setText(result.getMvFls().get(0).getIin());
                        row1.getCell(2).setText(result.getMvFls().get(0).getLast_name() + "\n" + result.getMvFls().get(0).getFirst_name() + "\n" + result.getMvFls().get(0).getPatronymic());
                        row1.getCell(3).setText(result.getMvFls().get(0).is_resident() ? "ДА" : "НЕТ");
                        row1.getCell(4).setText(result.getMvFls().get(0).getNationality_ru_name());
                        row1.getCell(5).setText(result.getMvFls().get(0).getDeath_date() !=null ? result.getMvFls().get(0).getDeath_date() : "Отсутсвует");
                        setMarginBetweenTables(doc);
                    }
                } catch (Exception e) {
                    System.out.println("Mv_Fl table add exception");
                }
                try {
                    if (result.getMvRnOlds() != null && result.getMvRnOlds().size() > 0) {
                        creteTitle(doc, "Адреса прописки");

                        XWPFTable table = doc.createTable();
                        makeTableByProperties(doc, table, "Адреса прописки", Arrays.asList(
                                "Страна",
                                "Город",
                                "Адрес",
                                "Регион",
                                "Дата прописки"
                        ));

                        // Populate the table with data
                        for (RegAddressFl regAddressFl : result.getRegAddressFls()) {
                            XWPFTableRow row = table.createRow();
                            row.getCell(0).setText(regAddressFl.getCountry());
                            row.getCell(1).setText(regAddressFl.getCity());
                            row.getCell(2).setText(regAddressFl.getDistrict());
                            row.getCell(3).setText(regAddressFl.getRegion());
                            row.getCell(4).setText(regAddressFl.getReg_date());
                        }
                        setMarginBetweenTables(doc);
                    }
                } catch (Exception e) {
                    System.out.println("MV_Rn_Old table add exception");
                }
            doc.write(baos);
            baos.close();
        }
    }



    public void generateDoc(NodesFL result, ByteArrayOutputStream baos) throws IOException, InvalidFormatException {
        try (XWPFDocument doc = new XWPFDocument()) {
            CTDocument1 document = doc.getDocument();
            CTBody body = document.getBody();

            if (!body.isSetSectPr()) {
                body.addNewSectPr();
            }
            CTSectPr section = body.getSectPr();

            if(!section.isSetPgSz()) {
                section.addNewPgSz();
            }
            CTPageSz pageSize = section.getPgSz();

            pageSize.setW(BigInteger.valueOf(15840));
            pageSize.setH(BigInteger.valueOf(12240));

            try {
                if (result.getMvFls() != null || result.getMvFls().size() < 0) {
                    creteTitle(doc,"Сведения о физическом лице");
                    XWPFTable table = doc.createTable();
                    makeTableByProperties(doc, table, "Сведения о физическом лице", Arrays.asList(
                            "Фото",
                            "ИИН",
                            "ФИО",
                            "Резидент",
                            "Гражданство",
                            "Нация",
                            "Дата рождения",
                            "Место рождения",
                            "Статус",
                            "Дата смерти"
                    ));
                    
                    XWPFTableRow row1 = table.createRow();
                    XWPFTableCell cell1 = row1.getCell(0);

                    XWPFParagraph paragraph2 = cell1.addParagraph();

                    setCellPadding(cell1, 200, 200, 200, 200);
                    XWPFRun run1 = paragraph2.createRun();
                try {
                    byte[] imageBytes = result.getPhotoDbf().get(0).getPhoto();
                    ByteArrayInputStream imageStream = new ByteArrayInputStream(imageBytes);
                    int imageType = XWPFDocument.PICTURE_TYPE_PNG; // Change according to your image type (e.g., PICTURE_TYPE_JPEG)
                    run1.addPicture(imageStream, imageType, "image.png", Units.toEMU(75), Units.toEMU(100));
                }catch (Exception e){
                    row1.getCell(0).setText("Фото не найдено");
                    System.out.println("Фото не найдено");
                }

                    row1.getCell(1).setText(result.getMvFls().get(0).getIin());
                    row1.getCell(2).setText(result.getMvFls().get(0).getLast_name() + "\n" + result.getMvFls().get(0).getFirst_name() + "\n" + result.getMvFls().get(0).getPatronymic());
                    row1.getCell(3).setText(result.getMvFls().get(0).is_resident() ? "ДА" : "НЕТ");
                    row1.getCell(4).setText(result.getMvFls().get(0).getCitizenship_ru_name());
                    row1.getCell(5).setText(result.getMvFls().get(0).getNationality_ru_name());
                    row1.getCell(6).setText(result.getMvFls().get(0).getBirth_date());
                    row1.getCell(7).setText( result.getMvFls().get(0).getDistrict() + ", " + result.getMvFls().get(0).getRegion());
                    row1.getCell(8).setText(result.getMvFls().get(0).getLife_status_ru_name());
                    row1.getCell(9).setText(result.getMvFls().get(0).getDeath_date() !=null ? result.getMvFls().get(0).getDeath_date() : "Отсутсвует");
                    setMarginBetweenTables(doc);
                }
            } catch (Exception e) {
                System.out.println("Mv_Fl table add exception");
            }
            try {
                List<MvIinDoc> docs = result.getMvIinDocs();
                if (docs != null && !docs.isEmpty()) {
                    creteTitle(doc, "Наименование коллапса: \"Документы\" Количество найденных инф: " + docs.size());
                    XWPFTable table = doc.createTable();

                    // Define headers
                    List<String> headers = Arrays.asList("№", "Типа Документа", "Орган выдачи", "Дата выдачи", "Срок до", "Номер документа");
                    makeTableByProperties(doc, table, "Наименование коллапса: \"Документы\" Количество найденных инф: " + docs.size(), headers);

                    int number = 1;
                    for (MvIinDoc doci : docs) {
                        XWPFTableRow dataRow = table.createRow();
                        dataRow.getCell(0).setText(String.valueOf(number));
                        dataRow.getCell(1).setText(doci.getDoc_type_ru_name());
                        dataRow.getCell(2).setText(doci.getIssue_organization_ru_name());
                        dataRow.getCell(3).setText(doci.getIssue_date().toString());
                        dataRow.getCell(4).setText(doci.getExpiry_date().toString());
                        dataRow.getCell(5).setText(doci.getDoc_number());
                        number++;
                    }

                    setMarginBetweenTables(doc);
                }
            } catch (Exception e) {
                System.out.println("MV_Iin_Doc table add exception: " + e.getMessage());
            }
            try {
                List<MvFlAddress> addressFls = result.getMvFlAddresses();
                if (addressFls != null && !addressFls.isEmpty()) {
                    creteTitle(doc, "Наименование коллапса: \"Адрес прописки\" Количество найденных инф: " + addressFls.size());
                    XWPFTable table = doc.createTable();

                    List<String> headers = Arrays.asList("№", "Город", "Улица, дом, квартира", "Район", "Дата регистрации");
                    makeTableByProperties(doc, table, "Наименование коллапса: \"Адрес прописки\" Количество найденных инф: " + addressFls.size(), headers);

                    int number = 1;
                    for (MvFlAddress ar : addressFls) {
                        XWPFTableRow dataRow = table.createRow();
                        dataRow.getCell(0).setText(String.valueOf(number));
                        dataRow.getCell(1).setText(ar.getDistrict());
                        dataRow.getCell(2).setText(ar.getRegion());
                        dataRow.getCell(3).setText(ar.getStreet());
                        dataRow.getCell(4).setText(ar.getBuilding());
                        number++;
                    }

                    setMarginBetweenTables(doc);
                }
            } catch (Exception e) {
                System.out.println("MV_FL_Address table add exception: " + e.getMessage());
            }try {
                List<ChangeFio> changeFios = changeFioRepo.getByIin(result.getMvFls().get(0).getIin());
                if (changeFios != null && !changeFios.isEmpty()) {
                    creteTitle(doc, "Наименование коллапса: \"Смена ФИО\" Количество найденных инф: " + changeFios.size());
                    XWPFTable table = doc.createTable();

                    List<String> headers = Arrays.asList("№", "Предыдущие данные", "Причина смены", "Дата смены ФИО");
                    makeTableByProperties(doc, table, "Наименование коллапса: \"Смена ФИО\" Количество найденных инф: " + changeFios.size(), headers);

                    int number = 1;
                    for (ChangeFio ar : changeFios) {
                        XWPFTableRow dataRow = table.createRow();
                        dataRow.getCell(0).setText(String.valueOf(number));
                        dataRow.getCell(1).setText(
                                (ar.getSurname_before() != null ? ar.getSurname_before() : " ") + " " +
                                        (ar.getName_before() != null ? ar.getName_before() : " ") + " " +
                                        (ar.getSecondname_before() != null ? ar.getSecondname_before() : " ")
                        );
                        dataRow.getCell(2).setText(ar.getRemarks());
                        dataRow.getCell(3).setText(ar.getTo_date());
                        number++;
                    }

                    setMarginBetweenTables(doc);
                }
            } catch (Exception e) {
                System.out.println("ChangeFio table add exception: " + e.getMessage());
            }
            try {
                List<Adm> adms = admRepo.getUsersByLike(result.getMvFls().get(0).getIin());
                if (adms != null && !adms.isEmpty()) {
                    creteTitle(doc, "Наименование коллапса: \"Административные штрафы\" Количество найденных инф: " + adms.size());
                    XWPFTable table = doc.createTable();

                    List<String> headers = Arrays.asList("№", "Орган выявивший правонарушение", "Дата заведения", "Номер протокола", "Место работы", "Квалификация", "Принудительное исполнение", "На срок до", "Размер наложенного штрафа", "Основания прекращения");
                    makeTableByProperties(doc, table, "Наименование коллапса: \"Административные штрафы\" Количество найденных инф: " + adms.size(), headers);

                    int number = 1;
                    for (Adm r : adms) {
                        XWPFTableRow dataRow = table.createRow();
                        dataRow.getCell(0).setText(String.valueOf(number));
                        dataRow.getCell(1).setText(r.getAuthority_detected());
                        dataRow.getCell(2).setText(r.getReg_date());
                        dataRow.getCell(3).setText(r.getProtocol_num().toString());
                        dataRow.getCell(4).setText(r.getWork_place().toString());
                        dataRow.getCell(5).setText(r.getQualification_name());
                        dataRow.getCell(6).setText(r.getEnforcement());
                        dataRow.getCell(7).setText(r.getEnd_date());
                        dataRow.getCell(8).setText(r.getFine_amount());
                        dataRow.getCell(9).setText(r.getTeminate_reason());
                        number++;
                    }

                    setMarginBetweenTables(doc);
                }
            } catch (Exception e) {
                System.out.println("Adm table add exception: " + e.getMessage());
            }
            try {
                List<FlContacts> flContacts = flContactsRepo.findAllByIin(result.getMvIinDocs().get(0).getIin());
                if (flContacts != null && !flContacts.isEmpty()) {
                    creteTitle(doc, "Наименование коллапса: \"Контактные данные\" Количество найденных инф: " + flContacts.size());
                    XWPFTable table = doc.createTable();

                    List<String> headers = Arrays.asList("№", "Номер телефона", "Email", "ФИО руководителя организации", "ФИО/Наименование организации владельца номера", "Источник", "Nickname");
                    makeTableByProperties(doc, table, "Наименование коллапса: \"Контактные данные\" Количество найденных инф: " + flContacts.size(), headers);

                    int number = 1;
                    for (FlContacts flContacts1 : flContacts) {
                        XWPFTableRow dataRow = table.createRow();
                        dataRow.getCell(0).setText(String.valueOf(number));
                        dataRow.getCell(1).setText(flContacts1.getPhone() != null ? flContacts1.getPhone() : "");
                        dataRow.getCell(2).setText(flContacts1.getEmail() != null ? flContacts1.getEmail() : "");
                        dataRow.getCell(3).setText(flContacts1.getLeader_fio() != null ? flContacts1.getLeader_fio().toString() : "");
                        dataRow.getCell(4).setText(flContacts1.getNickname() != null ? flContacts1.getNickname().toString() : "");
                        dataRow.getCell(5).setText(flContacts1.getSource() != null ? flContacts1.getSource() : "");
                        dataRow.getCell(6).setText(flContacts1.getNickname() != null ? flContacts1.getNickname() : "");
                        number++;
                    }

                    setMarginBetweenTables(doc);
                }
            } catch (Exception e) {
                System.out.println("FlContacts table add exception: " + e.getMessage());
            }
            try {
                List<IndividualEntrepreneur> individualEntrepreneurs = individualEntrepreneurRepo.getByIin(result.getMvFls().get(0).getIin());
                List<KX> kxes = kxRepo.getKxIin(result.getMvFls().get(0).getIin());
                if ((individualEntrepreneurs != null && !individualEntrepreneurs.isEmpty()) || (kxes != null && !kxes.isEmpty())) {
                    creteTitle(doc, "Наименование коллапса: \"ИП/КХ\" Количество найденных инф: " + (individualEntrepreneurs.size() + kxes.size()));
                    XWPFTable table = doc.createTable();

                    List<String> headers = Arrays.asList("№", "Наименование", "ИП/КХ");
                    makeTableByProperties(doc, table, "Наименование коллапса: \"ИП/КХ\" Количество найденных инф: " + (individualEntrepreneurs.size() + kxes.size()), headers);

                    int number = 1;
                    if (individualEntrepreneurs != null && !individualEntrepreneurs.isEmpty()) {
                        for (IndividualEntrepreneur individualEntrepreneur : individualEntrepreneurs) {
                            XWPFTableRow dataRow = table.createRow();
                            dataRow.getCell(0).setText(String.valueOf(number));
                            dataRow.getCell(1).setText(individualEntrepreneur.getName_rus() != null ? individualEntrepreneur.getName_rus() : "");
                            dataRow.getCell(2).setText("ИП");
                            number++;
                        }
                    }
                    if (kxes != null && !kxes.isEmpty()) {
                        for (KX kx : kxes) {
                            XWPFTableRow dataRow = table.createRow();
                            dataRow.getCell(0).setText(String.valueOf(number));
                            dataRow.getCell(1).setText(kx.getName_rus() != null ? kx.getName_rus() : "");
                            dataRow.getCell(2).setText("КХ");
                            number++;
                        }
                    }

                    setMarginBetweenTables(doc);
                }
            } catch (Exception e) {
                System.out.println("IndividualEntrepreneur/KX table add exception: " + e.getMessage());
            }
            try {
                GosZakupForAll gosZakupForAll = myService.gosZakupByBin(result.getMvFls().get(0).getIin());
                if ((gosZakupForAll.getWhenCustomer() != null && !gosZakupForAll.getWhenCustomer().isEmpty()) ||
                        (gosZakupForAll.getWhenSupplier() != null && !gosZakupForAll.getWhenSupplier().isEmpty())) {

                    creteTitle(doc, "Наименование коллапса: \"Государственные закупки- По поставщикам\" Количество найденных инф: " + (gosZakupForAll.getWhenSupplier().size() + gosZakupForAll.getWhenCustomer().size()));
                    XWPFTable table = doc.createTable();

                    List<String> headers = Arrays.asList("№", "Год", "Общая сумма", "Количество договоров", "Поставщики");
                    makeTableByProperties(doc, table, "Наименование коллапса: \"Государственные закупки- По поставщикам\" Количество найденных инф: " + (gosZakupForAll.getWhenSupplier().size() + gosZakupForAll.getWhenCustomer().size()), headers);

                    int number = 1;
                    if (gosZakupForAll.getWhenSupplier() != null && !gosZakupForAll.getWhenSupplier().isEmpty()) {
                        for (GosZakupDTO r : gosZakupForAll.getWhenSupplier()) {
                            XWPFTableRow dataRow = table.createRow();
                            dataRow.getCell(0).setText(String.valueOf(number));
                            dataRow.getCell(1).setText(r.getPeriod());
                            dataRow.getCell(2).setText(r.getSum());
                            dataRow.getCell(3).setText(String.valueOf(r.getNumber()));
                            dataRow.getCell(4).setText(r.getOpposite().toString());
                            number++;
                        }
                    }
                    if (gosZakupForAll.getWhenCustomer() != null && !gosZakupForAll.getWhenCustomer().isEmpty()) {
                        for (GosZakupDTO r : gosZakupForAll.getWhenCustomer()) {
                            XWPFTableRow dataRow = table.createRow();
                            dataRow.getCell(0).setText(String.valueOf(number));
                            dataRow.getCell(1).setText(r.getPeriod());
                            dataRow.getCell(2).setText(r.getSum());
                            dataRow.getCell(3).setText(String.valueOf(r.getNumber()));
                            dataRow.getCell(4).setText(r.getOpposite().toString());
                            number++;
                        }
                    }

                    setMarginBetweenTables(doc);
                }
            } catch (Exception e) {
                System.out.println("GosZakupForAll table add exception: " + e.getMessage());
            }
            try {
                List<String> companyBins = flPensionContrRepo.getUsersByLikeCompany(result.getMvFls().get(0).getIin());

                List<PensionListDTO> pensions = new ArrayList<>();
                List<PensionGroupDTO> results = new ArrayList<>();
                DecimalFormat df = new DecimalFormat("#");
                df.setMaximumFractionDigits(0);

                for (String bin : companyBins) {
                    List<Map<String, Object>> fl_pension_contrss = flPensionContrRepo.getAllByCompanies(result.getMvFls().get(0).getIin(), bin);
                    PensionGroupDTO obj = new PensionGroupDTO();
                    List<PensionListDTO> group = new ArrayList<>();
                    StringBuilder name = new StringBuilder();

                    if (fl_pension_contrss.get(0).get("P_NAME") != null) {
                        name.append((String) fl_pension_contrss.get(0).get("P_NAME")).append(", ");
                    }
                    if (bin != null) {
                        name.append(bin).append(", период ");
                    }

                    List<String> distinctPayDates = fl_pension_contrss.stream()
                            .map(pension -> pension.get("pay_date").toString())
                            .distinct()
                            .collect(Collectors.toList());

                    double knp010sum = 0.0;
                    double knp012sum = 0.0;

                    for (String year : distinctPayDates) {
                        if (year != null) {
                            name.append(year.replace(".0", "")).append(", ");
                        }

                        PensionListDTO pensionListEntity = new PensionListDTO();
                        pensionListEntity.setBin(bin);
                        pensionListEntity.setName((String) fl_pension_contrss.get(0).get("P_NAME"));
                        pensionListEntity.setPeriod(year.replace(".0", ""));

                        try {
                            double knp010 = fl_pension_contrss.stream()
                                    .filter(pension -> pension.get("pay_date").toString().equals(year) && pension.get("KNP").toString().equals("010"))
                                    .mapToDouble(pension -> Double.parseDouble(pension.get("AMOUNT").toString()))
                                    .sum();

                            pensionListEntity.setSum010(knp010);
                            knp010sum += knp010;
                        } catch (Exception ignored) {
                        }

                        try {
                            double knp012 = fl_pension_contrss.stream()
                                    .filter(pension -> pension.get("pay_date").toString().equals(year) && pension.get("KNP").toString().equals("012"))
                                    .mapToDouble(pension -> Double.parseDouble(pension.get("AMOUNT").toString()))
                                    .sum();

                            pensionListEntity.setSum012(knp012);
                            knp012sum += knp012;
                        } catch (Exception ignored) {
                        }

                        pensions.add(pensionListEntity);
                        group.add(pensionListEntity);
                    }

                    name.append("общая сумма КНП(010): ").append(df.format(knp010sum)).append(", общая сумма КНП(012): ").append(df.format(knp012sum));
                    obj.setName(name.toString());
                    obj.setList(group);
                    results.add(obj);
                }

                if (results != null && !results.isEmpty()) {
                    creteTitle(doc, "Наименование коллапса: \"Пенсионные отчисления\" Количество найденных инф: " + results.size());
                    XWPFTable table = doc.createTable();

                    List<String> headers = Arrays.asList("№", "БИН, Наименование ЮЛ", "Период", "Общая сумма(010)", "Общая сумма(012)");
                    makeTableByProperties(doc, table, "Наименование коллапса: \"Пенсионные отчисления\" Количество найденных инф: " + results.size(), headers);

                    int number = 1;
                    for (PensionGroupDTO pensionListDTO : results) {
                        XWPFTableRow dataRow = table.createRow();
                        dataRow.getCell(0).setText(String.valueOf(number));
                        dataRow.getCell(1).setText(PdfGenerator.getSubstring(pensionListDTO.getName(), 0, ", п"));
                        dataRow.getCell(2).setText(PdfGenerator.getSubstringByTwoWords(pensionListDTO.getName(), ", период ", ", общая"));
                        dataRow.getCell(3).setText(PdfGenerator.getSubstringByTwoWords(pensionListDTO.getName(), "общая сумма КНП(010): ", ", общая сумма КНП(012)"));
                        dataRow.getCell(4).setText(PdfGenerator.getSubstringByLastWord(pensionListDTO.getName(), "общая сумма КНП(012): ", pensionListDTO.getName()));
                        number++;
                    }

                    setMarginBetweenTables(doc);
                }
            } catch (Exception e) {
                System.out.println("PensionGroupDTO table add exception: " + e.getMessage());
            }
            try {
                // Fetch the data
                List<MvUlLeaderEntity> mvUlLeaderEntities = mvUlLeaderEntityRepo.getUsersByLikeIin(result.getMvFls().get(0).getIin());
                for (MvUlLeaderEntity mvUlLeaderEntity : mvUlLeaderEntities) {
                    mvUlLeaderEntity.setBinName(mvUlRepo.getNameByBin(mvUlLeaderEntity.getBinOrg()));
                }

                List<MvUlFounderFl> mvUlFounderFls = result.getMvUlFounderFls();
                for (MvUlFounderFl mvUlFounderFl : mvUlFounderFls) {
                    mvUlFounderFl.setBinName(mvUlRepo.getNameByBin(mvUlFounderFl.getBin_org()));
                }

                // Check if there is data to display
                if ((mvUlLeaderEntities != null && !mvUlLeaderEntities.isEmpty()) || (mvUlFounderFls != null && !mvUlFounderFls.isEmpty())) {
                    // Create the table
                    creteTitle(doc, "Наименование коллапса: \"Сведения об участниках ЮЛ\" Количество найденных инф: " + (mvUlLeaderEntities.size() + mvUlFounderFls.size()));
                    XWPFTable table = doc.createTable();
                    // Set up table headers
                    List<String> headers = Arrays.asList("№", "Идентификатор ЮЛ", "Дата регистрации", "БИН", "Наименование ЮЛ");
                    makeTableByProperties(doc, table, "Наименование коллапса: \"Сведения об участниках ЮЛ\" Количество найденных инф: " + (mvUlLeaderEntities.size() + mvUlFounderFls.size()), headers);

                    // Add data rows
                    int number = 1;
                    for (MvUlLeaderEntity r : mvUlLeaderEntities) {
                        XWPFTableRow dataRow = table.createRow();
                        dataRow.getCell(0).setText(String.valueOf(number));
                        dataRow.getCell(1).setText("Директор");
                        dataRow.getCell(2).setText(r.getRegDate());
                        dataRow.getCell(3).setText(r.getBinOrg());
                        dataRow.getCell(4).setText(r.getBinName());
                        number++;
                    }
                    for (MvUlFounderFl r : mvUlFounderFls) {
                        XWPFTableRow dataRow = table.createRow();
                        dataRow.getCell(0).setText(String.valueOf(number));
                        dataRow.getCell(1).setText("Учредитель");
                        dataRow.getCell(2).setText(r.getReg_date());
                        dataRow.getCell(3).setText(r.getBin_org());
                        dataRow.getCell(4).setText(r.getBinName());
                        number++;
                    }

                    // Add the table to the document
                    setMarginBetweenTables(doc);
                }
            } catch (Exception e) {
                System.out.println("Svidenia ob uchastnikah YU: Exception: " + e.getMessage());
            }
// Fetch the data
            List<MvRnOld> mvRnOlds = result.getMvRnOlds();

// Check if there is data to display
            if (mvRnOlds != null && !mvRnOlds.isEmpty()) {
                // Create the table
                creteTitle(doc, "Наименование коллапса: \"Сведения по реестру недвижимости\" Количество найденных инф: " + mvRnOlds.size());
                XWPFTable table = doc.createTable();

                // Set up table headers
                List<String> headers = Arrays.asList(
                        "№", "Кадастровый номер", "Адрес", "Правообладатель",
                        "Этажность", "Количество составляющих", "Площадь общая",
                        "Вид документа", "Номер документа", "Дата документа",
                        "Сумма сделки", "Жилая площадь", "Статус"
                );
                makeTableByProperties(doc, table,
                        "Наименование коллапса: \"Сведения по реестру недвижимости\" Количество найденных инф: " + mvRnOlds.size(),
                        headers
                );

                // Add data rows
                int number = 1;
                for (MvRnOld r : mvRnOlds) {
                    XWPFTableRow dataRow = table.createRow();
                    dataRow.getCell(0).setText(String.valueOf(number));
                    dataRow.getCell(1).setText(r.getCadastral_number() != null ? r.getCadastral_number() : "");
                    dataRow.getCell(2).setText(r.getAddress_rus() != null ? r.getAddress_rus() : "");
                    dataRow.getCell(3).setText(r.getOwner_iin_bin() != null ? r.getOwner_iin_bin() : "");
                    dataRow.getCell(4).setText(r.getFloor() != null ? r.getFloor() : "");
                    dataRow.getCell(5).setText(r.getType_of_property_rus() != null ? r.getType_of_property_rus() : "");
                    dataRow.getCell(6).setText(r.getArea_total() != null ? r.getArea_total() : "");
                    dataRow.getCell(7).setText(r.getType_of_property_rus() != null ? r.getType_of_property_rus() : "");
                    dataRow.getCell(8).setText(r.getRegister_emergence_rights_rus() != null ? r.getRegister_emergence_rights_rus() : "");
                    dataRow.getCell(9).setText(r.getRegister_reg_date() != null ? r.getRegister_reg_date() : "");
                    dataRow.getCell(10).setText(r.getRegister_transaction_amount() != null ? r.getRegister_transaction_amount() : "");
                    dataRow.getCell(11).setText(r.getArea_useful() != null ? r.getArea_useful() : "");
                    dataRow.getCell(12).setText(r.getRegister_status_rus() != null ? r.getRegister_status_rus() : "");
                    number++;
                }

                // Add the table to the document
                setMarginBetweenTables(doc);
            } else {
                System.out.println("No data found for \"Сведения по реестру недвижимости\"");
            }
            List<School> schools = result.getSchools();
            if (schools != null && !schools.isEmpty()) {
                XWPFTable schoolTable = doc.createTable();
                creteTitle(doc, "Наименование коллапса: \"Сведение по образование: Среднее образование (Школа)\" Количество найденных инф: " + schools.size());

                // Set table headers
                List<String> headers = Arrays.asList(
                        "БИН", "Название школы", "Класс", "Дата поступления", "Дата окончания"
                );
                makeTableByProperties(doc, schoolTable,
                        "Наименование коллапса: \"Сведение по образование: Среднее образование (Школа)\" Количество найденных инф: " + schools.size(),
                        headers
                );

                // Add data rows
                for (School r : schools) {
                    XWPFTableRow row = schoolTable.createRow();
                    row.getCell(0).setText(r.getSchool_code() != null ? r.getSchool_code() : "");
                    row.getCell(1).setText(r.getSchool_name() != null ? r.getSchool_name() : "");
                    row.getCell(2).setText(r.getGrade() != null ? r.getGrade() : "");
                    row.getCell(3).setText(r.getStart_date() != null ? r.getStart_date().toString() : "");
                    row.getCell(4).setText(r.getEnd_date() != null ? r.getEnd_date().toString() : "");
                }

                setMarginBetweenTables(doc);
            } else {
                System.out.println("No data found for \"Сведение по образование: Среднее образование (Школа)\"");
            }
            try {
                List<Universities> universities = result.getUniversities();
                if (universities != null && !universities.isEmpty()) {
                    // Create the title for the table
                    creteTitle(doc, "Наименование коллапса: \"Сведение по образование: Высшее образование (Университет)\" Количество найденных инф: " + universities.size());

                    // Create the table
                    XWPFTable uniTable = doc.createTable();

                    // Set headers
                    List<String> headers = Arrays.asList("БИН", "Название вуза", "Специализация", "Дата поступления", "Дата окончания", "Длительность обучения", "Курс");
                    makeTableByProperties(doc, uniTable, "Наименование коллапса: \"Сведение по образование: Высшее образование (Университет)\" Количество найденных инф: " + universities.size(), headers);

                    int number = 1;
                    for (Universities r : universities) {
                        XWPFTableRow row = uniTable.createRow();
                        row.getCell(0).setText(r.getStudy_code() != null ? r.getStudy_code() : "");
                        row.getCell(1).setText(r.getStudy_name() != null ? r.getStudy_name() : "");
                        row.getCell(2).setText(r.getSpec_name() != null ? r.getSpec_name() : "");
                        row.getCell(3).setText(r.getStart_date() != null ? r.getStart_date().toString() : "");
                        row.getCell(4).setText(r.getEnd_date() != null ? r.getEnd_date().toString() : "");
                        row.getCell(5).setText(r.getDuration() != null ? r.getDuration() : "");
                        row.getCell(6).setText(r.getCourse() != null ? r.getCourse() : "");
                        number++;
                    }

                    setMarginBetweenTables(doc);
                }
            } catch (Exception e) {
                System.out.println("Universities table add exception: " + e.getMessage());
            }
            try {
                List<MvAutoFl> autos = result.getMvAutoFls();
                if (autos != null && !autos.isEmpty()) {
                    // Create the title for the table
                    creteTitle(doc, "Наименование коллапса: \"Транспорт\" Количество найденных инф: " + autos.size());

                    // Create the table
                    XWPFTable autoTable = doc.createTable();

                    // Set headers
                    List<String> headers = Arrays.asList("№", "Статус", "Регистрационный номер", "Марка модель", "Дата выдачи свидетельства", "Дата снятия", "Год выпуска", "Категория", "VIN/Кузов/Шосси", "Серия");
                    makeTableByProperties(doc, autoTable, "Наименование коллапса: \"Транспорт\" Количество найденных инф: " + autos.size(), headers);

                    int number = 1;
                    for (MvAutoFl r : autos) {
                        XWPFTableRow row = autoTable.createRow();
                        row.getCell(0).setText(String.valueOf(number));

                        try {
                            row.getCell(1).setText(r.isIs_registered() ? "Текущий" : "Исторический");
                        } catch (Exception e) {
                            row.getCell(1).setText("");
                        }

                        try {
                            row.getCell(2).setText(r.getReg_number() != null ? r.getReg_number() : "");
                        } catch (Exception e) {
                            row.getCell(2).setText("");
                        }

                        try {
                            row.getCell(3).setText(r.getBrand_model() != null ? r.getBrand_model() : "");
                        } catch (Exception e) {
                            row.getCell(3).setText("");
                        }

                        try {
                            row.getCell(4).setText(r.getDate_certificate() != null ? r.getDate_certificate().toString() : "");
                        } catch (Exception e) {
                            row.getCell(4).setText("");
                        }

                        try {
                            row.getCell(5).setText(r.getEnd_date() != null ? r.getEnd_date().toString() : "");
                        } catch (Exception e) {
                            row.getCell(5).setText("");
                        }

                        row.getCell(6).setText(r.getRelease_year_tc() != null ? r.getRelease_year_tc() : "");
                        row.getCell(7).setText(r.getCategory_control_tc() != null ? r.getCategory_control_tc() : "");
                        row.getCell(8).setText(r.getVin_kuzov_shassi() != null ? r.getVin_kuzov_shassi() : "");
                        row.getCell(9).setText(r.getSeries_reg_number() != null ? r.getSeries_reg_number() : "");

                        number++;
                    }

                    setMarginBetweenTables(doc);
                }
            } catch (Exception e) {
                System.out.println("MvAutoFl table add exception: " + e.getMessage());
            }
            try {
                List<MillitaryAccount> millitaryAccounts = result.getMillitaryAccounts();
                if (millitaryAccounts != null && !millitaryAccounts.isEmpty()) {
                    // Create the title for the table
                    creteTitle(doc, "Войнский учет");

                    // Create the table
                    XWPFTable maTable = doc.createTable();

                    // Set headers
                    List<String> headers = Arrays.asList("№", "БИН воинской части", "Дата службы с", "Дата службы по");
                    makeTableByProperties(doc, maTable, "Войнский учет", headers);

                    int number = 1;
                    for (MillitaryAccount r : millitaryAccounts) {
                        XWPFTableRow row = maTable.createRow();
                        row.getCell(0).setText(String.valueOf(number));
                        row.getCell(1).setText(r.getBin() != null ? r.getBin() : "");
                        row.getCell(2).setText(r.getDate_start() != null ? r.getDate_start() : "");
                        row.getCell(3).setText(r.getDate_end() != null ? r.getDate_end() : "");
                        number++;
                    }

                    setMarginBetweenTables(doc);
                }
            } catch (Exception e) {
                System.out.println("MillitaryAccount table add exception: " + e.getMessage());
            }
//            try {
//                List<ConvictsJustified> convictsJustifieds = result.getConvictsJustifieds();
//                if (convictsJustifieds != null && !convictsJustifieds.isEmpty()) {
//                    // Create the title for the table
//                    creteTitle(doc, "Наименование риска: \"Осужденные\" Количество найденных инф: " + convictsJustifieds.size());
//
//                    // Create the table
//                    XWPFTable convictsTable = doc.createTable();
//
//                    // Set headers
//                    List<String> headers = Arrays.asList(
//                            "№",
//                            "Дата рассмотрения в суде 1 инстанции",
//                            "Суд 1 инстанции",
//                            "Решение по лицу",
//                            "Мера наказания по договору",
//                            "Квалификация"
//                    );
//                    makeTableByProperties(doc, convictsTable, "Наименование риска: \"Осужденные\" Количество найденных инф: " + convictsJustifieds.size(), headers);
//
//                    int number = 1;
//                    for (ConvictsJustified r : convictsJustifieds) {
//                        XWPFTableRow row = convictsTable.createRow();
//                        row.getCell(0).setText(String.valueOf(number));
//                        row.getCell(1).setText(r.getReg_date() != null ? r.getReg_date() : "");
//                        row.getCell(2).setText(r.getCourt_of_first_instance() != null ? r.getCourt_of_first_instance() : "");
//                        row.getCell(3).setText(r.getDecision_on_person() != null ? r.getDecision_on_person() : "");
//                        row.getCell(4).setText(r.getMeasure_punishment() != null ? r.getMeasure_punishment() : "");
//                        row.getCell(5).setText(r.getQualification() != null ? r.getQualification() : "");
//                        number++;
//                    }
//
//                    setMarginBetweenTables(doc);
//                }
//            } catch (Exception e) {
//                System.out.println("ConvictsJustified table add exception: " + e.getMessage());
//            }
            try {
                List<ConvictsTerminatedByRehab> convictsTerminatedByRehabs = result.getConvictsTerminatedByRehabs();
                if (convictsTerminatedByRehabs != null && !convictsTerminatedByRehabs.isEmpty()) {
                    // Create the title for the table
                    creteTitle(doc, "Заключенный Рехаб. Количество найденных инф: " + convictsTerminatedByRehabs.size());

                    // Create the table
                    XWPFTable ctbrTable = doc.createTable();

                    // Set headers
                    List<String> headers = Arrays.asList(
                            "№",
                            "Орган выявивший правонарушение",
                            "Дата заведения",
                            "Квалификация",
                            "Решение",
                            "Уровень тяжести"
                    );
                    makeTableByProperties(doc, ctbrTable, "Заключенный Рехаб. Количество найденных инф: " + convictsTerminatedByRehabs.size(), headers);

                    int number = 1;
                    for (ConvictsTerminatedByRehab r : convictsTerminatedByRehabs) {
                        XWPFTableRow row = ctbrTable.createRow();
                        row.getCell(0).setText(String.valueOf(number));
                        row.getCell(1).setText(r.getInvestigative_authority() != null ? r.getInvestigative_authority() : "");
                        row.getCell(2).setText(r.getLast_solution_date() != null ? r.getLast_solution_date() : "");
                        row.getCell(3).setText(r.getQualification_desc() != null ? r.getQualification_desc() : "");
                        row.getCell(4).setText(r.getLast_solution() != null ? r.getLast_solution() : "");
                        row.getCell(5).setText(r.getQualification_by_11() != null ? r.getQualification_by_11() : "");
                        number++;
                    }

                    setMarginBetweenTables(doc);
                }
            } catch (Exception e) {
                System.out.println("ConvictsTerminatedByRehab table add exception: " + e.getMessage());
            }


            try {
                List<BlockEsf> blockEsfs = result.getBlockEsfs();
                if (blockEsfs != null && !blockEsfs.isEmpty()) {
                    // Create the title for the table
                    creteTitle(doc, "Блок ЭСФ. Количество найденных инф: " + blockEsfs.size());

                    // Create the table
                    XWPFTable blockesfTable = doc.createTable();

                    // Set headers
                    List<String> headers = Arrays.asList(
                            "№",
                            "Дата блокировки",
                            "Дата восстановления",
                            "Дата обновления"
                    );
                    makeTableByProperties(doc, blockesfTable, "Блок ЭСФ. Количество найденных инф: " + blockEsfs.size(), headers);

                    int number = 1;
                    for (BlockEsf r : blockEsfs) {
                        XWPFTableRow row = blockesfTable.createRow();
                        row.getCell(0).setText(String.valueOf(number));

                        row.getCell(1).setText(r.getStart_dt() != null ? r.getStart_dt().toString() : "");
                        row.getCell(2).setText(r.getEnd_dt() != null ? r.getEnd_dt().toString() : "");
                        row.getCell(3).setText(r.getUpdate_dt() != null ? r.getUpdate_dt().toString() : "");

                        number++;
                    }

                    setMarginBetweenTables(doc);
                }
            } catch (Exception e) {
                System.out.println("BlockEsf table add exception: " + e.getMessage());
            }
            try {
                List<NdsEntity> ndsEntities = result.getNdsEntities();
                if (ndsEntities != null && !ndsEntities.isEmpty()) {
                    // Create the title for the table
                    creteTitle(doc, "НДС. Количество найденных инф: " + ndsEntities.size());

                    // Create the table
                    XWPFTable ndsTable = doc.createTable();

                    // Set headers
                    List<String> headers = Arrays.asList(
                            "№",
                            "Дата начала",
                            "Дата конца",
                            "Дата обновления",
                            "Причина"
                    );
                    makeTableByProperties(doc, ndsTable, "НДС. Количество найденных инф: " + ndsEntities.size(), headers);

                    int number = 1;
                    for (NdsEntity r : ndsEntities) {
                        XWPFTableRow row = ndsTable.createRow();
                        row.getCell(0).setText(String.valueOf(number));

                        row.getCell(1).setText(r.getStartDt() != null ? r.getStartDt().toString() : "");
                        row.getCell(2).setText(r.getEndDt() != null ? r.getEndDt().toString() : "");
                        row.getCell(3).setText(r.getUpdateDt() != null ? r.getUpdateDt().toString() : "");
                        row.getCell(4).setText(r.getReason() != null ? r.getReason() : "");

                        number++;
                    }

                    setMarginBetweenTables(doc);
                }
            } catch (Exception e) {
                System.out.println("NdsEntity table add exception: " + e.getMessage());
            }
            try {
                List<IpgoEmailEntity> ipgoEmailEntities = result.getIpgoEmailEntities();
                if (ipgoEmailEntities != null && !ipgoEmailEntities.isEmpty()) {
                    // Create the title for the table
                    creteTitle(doc, "Сведения по ИПГО. Количество найденных инф: " + ipgoEmailEntities.size());

                    // Create the table
                    XWPFTable ipgoTable = doc.createTable();

                    // Set headers
                    List<String> headers = Arrays.asList(
                            "№",
                            "Департамент",
                            "Должность",
                            "ИПГО почта"
                    );
                    makeTableByProperties(doc, ipgoTable, "Сведения по ИПГО. Количество найденных инф: " + ipgoEmailEntities.size(), headers);

                    int number = 1;
                    for (IpgoEmailEntity r : ipgoEmailEntities) {
                        XWPFTableRow row = ipgoTable.createRow();
                        row.getCell(0).setText(String.valueOf(number));

                        row.getCell(1).setText(r.getOrgan() != null ? r.getOrgan().toString() : "");
                        row.getCell(2).setText(r.getPosition() != null ? r.getPosition() : "");
                        row.getCell(3).setText(r.getEmail() != null ? r.getEmail().toString() : "");

                        number++;
                    }

                    setMarginBetweenTables(doc);
                }
            } catch (Exception e) {
                System.out.println("IpgoEmailEntity table add exception: " + e.getMessage());
            }

            try {
                List<Bankrot> bankrotEntities = result.getBankrots();
                if (bankrotEntities != null && !bankrotEntities.isEmpty()) {
                    creteTitle(doc,"Сведения по банкротам");
                    XWPFTable table = doc.createTable();
                    table.setWidth("100%");
                    makeTableByProperties(doc, table, "Сведения по банкротам", Arrays.asList("№", "ИИН/БИН", "Документ", "Дата обновления", "Причина"));

                    int number = 1;
                    for (Bankrot r : bankrotEntities) {
                        XWPFTableRow dataRow = table.createRow();
                        dataRow.getCell(0).setText(String.valueOf(number));
                        dataRow.getCell(1).setText(r.getIin_bin() != null ? r.getIin_bin() : "");
                        dataRow.getCell(2).setText(r.getDocument() != null ? r.getDocument() : "");
                        dataRow.getCell(3).setText(r.getUpdate_dt() != null ? r.getUpdate_dt().toString() : "");
                        dataRow.getCell(4).setText(r.getReason() != null ? r.getReason() : "");
                        number++;
                    }
                    setMarginBetweenTables(doc);
                    // Save the document as needed
                }
            } catch (Exception e) {
                System.out.println("Exception while adding bankrot entities table: " + e.getMessage());
            }
            try {
                if (result.getFirstCreditBureauEntities() != null && !result.getFirstCreditBureauEntities().isEmpty()) {
                    creteTitle(doc,"Сведения по кредитным бюро");
                    XWPFTable table = doc.createTable();
                    table.setWidth("100%");
                    makeTableByProperties(doc, table, "Сведения по кредитным бюро", Arrays.asList(
                            "№", "Тип", "Кредит в FOID", "Регион", "Количество FPD SPD", "Сумма долга", "Макс. задержка дней", "Фин. учреждения", "Общее количество кредитов"));


                    int number = 1;
                    for (FirstCreditBureauEntity entity : result.getFirstCreditBureauEntities()) {
                        XWPFTableRow dataRow = table.createRow();
                        dataRow.getCell(0).setText(String.valueOf(number));
                        dataRow.getCell(1).setText(entity.getType() != null ? entity.getType() : "");
                        dataRow.getCell(2).setText(entity.getCreditInFoid() != null ? entity.getCreditInFoid().toString() : "");
                        dataRow.getCell(3).setText(entity.getRegion() != null ? entity.getRegion() : "");
                        dataRow.getCell(4).setText(entity.getQuantityFpdSpd() != null ? entity.getQuantityFpdSpd().toString() : "");
                        dataRow.getCell(5).setText(entity.getAmountOfDebt() != null ? entity.getAmountOfDebt().toString() : "");
                        dataRow.getCell(6).setText(entity.getMaxDelayDayNum1() != null ? entity.getMaxDelayDayNum1().toString() : "");
                        dataRow.getCell(7).setText(entity.getFinInstitutionsName() != null ? entity.getFinInstitutionsName() : "");
                        dataRow.getCell(8).setText(entity.getTotalCountOfCredits() != null ? entity.getTotalCountOfCredits().toString() : "");
                        number++;
                    }
                    setMarginBetweenTables(doc);
                    // Save the document as needed
                }
            } catch (Exception e) {
                System.out.println("Exception while adding first credit bureau entities table: " + e.getMessage());
            }
            try {
                if (result.getAmoral() != null && !result.getAmoral().isEmpty()) {
                    creteTitle(doc,"Сведения по аморальному образу жизни");
                    XWPFTable table = doc.createTable();
                    table.setWidth("100%");
                    makeTableByProperties(doc, table, "Сведения по аморальному образу жизни", Arrays.asList("№", "Орган выявивший", "Гражданство", "Дата решения", "Сумма штрафа"));


                    int number = 1;
                    for (ImmoralLifestyle r : result.getAmoral()) {
                        XWPFTableRow dataRow = table.createRow();
                        dataRow.getCell(0).setText(String.valueOf(number));
                        dataRow.getCell(1).setText(r.getAuthority_detected() != null ? r.getAuthority_detected() : "");
                        dataRow.getCell(2).setText(r.getCitizenship_id() != null ? r.getCitizenship_id() : "");
                        dataRow.getCell(3).setText(r.getDecision_date() != null ? r.getDecision_date().toString() : "");
                        dataRow.getCell(4).setText(r.getFine_amount() != null ? r.getFine_amount().toString() : "");
                        number++;
                    }
                    setMarginBetweenTables(doc);
                    // Save the document as needed
                }
            } catch (Exception e) {
                System.out.println("Exception while adding immoral lifestyle entities table: " + e.getMessage());
            }try {
                if (result.getMzEntities() != null && !result.getMzEntities().isEmpty()) {
                    creteTitle(doc,"Сведения по МЗ");
                    XWPFTable table = doc.createTable();
                    table.setWidth("100%");
                    makeTableByProperties(doc, table, "Сведения по МЗ", Arrays.asList("№", "Код болезни", "Регистрация", "Статус МЗ", "Медицинская организация"));


                    int number = 1;
                    for (MzEntity r : result.getMzEntities()) {
                        XWPFTableRow dataRow = table.createRow();
                        dataRow.getCell(0).setText(String.valueOf(number));
                        dataRow.getCell(1).setText(r.getDiseaseCode() != null ? r.getDiseaseCode() : "");
                        dataRow.getCell(2).setText(r.getReg() != null ? r.getReg() : "");
                        dataRow.getCell(3).setText(r.getStatusMz() != null ? r.getStatusMz() : "");
                        dataRow.getCell(4).setText(r.getMedicalOrg() != null ? r.getMedicalOrg() : "");
                        number++;                  }
                    setMarginBetweenTables(doc);
                }
            } catch (Exception e) {
                System.out.println("Exception while adding MZ entities table: " + e.getMessage());
            }try {
                if (result.getWantedListEntities() != null && !result.getWantedListEntities().isEmpty()) {
                    creteTitle(doc,"Сведения по разыскиваемым");
                    XWPFTable table = doc.createTable();
                    table.setWidth("100%");
                    makeTableByProperties(doc, table, "Сведения по разыскиваемым", Arrays.asList("№", "Дни", "Орган", "Статус", "Дата актуальности"));


                    int number = 1;
                    for (WantedListEntity r : result.getWantedListEntities()) {
                        XWPFTableRow dataRow = table.createRow();
                        dataRow.getCell(0).setText(String.valueOf(number));
                        dataRow.getCell(1).setText(r.getDays() != null ? r.getDays().toString() : "");
                        dataRow.getCell(2).setText(r.getOrgan() != null ? r.getOrgan() : "");
                        dataRow.getCell(3).setText(r.getStatus() != null ? r.getStatus() : "");
                        dataRow.getCell(4).setText(r.getRelevanceDate() != null ? r.getRelevanceDate().toString() : "");
                        number++;                    }
                    setMarginBetweenTables(doc);
                    // Save the document as needed
                }
            } catch (Exception e) {
                System.out.println("Exception while adding wanted list entities table: " + e.getMessage());
            }

            try {
                List<FlRelativiesDTO> flRelativiesDTOS = myService.getRelativesInfo(result.getMvIinDocs().get(0).getIin());
                if (flRelativiesDTOS != null && !flRelativiesDTOS.isEmpty()) {
                    // Create the title for the table
                    creteTitle(doc, "Наименование коллапса: \"Сведения о родственных связях данного ФЛ\" Количество найденных инф: " + flRelativiesDTOS.size());

                    // Create the table
                    XWPFTable docTable = doc.createTable();

                    // Set headers
                    List<String> headers = Arrays.asList(
                            "№",
                            "Статус по отношению к родственнику",
                            "ФИО",
                            "Дата регистрации брака",
                            "Дата расторжения брака",
                            "ИИН родственника",
                            "Дата рождения"
                    );
                    makeTableByProperties(doc, docTable, "Наименование коллапса: \"Сведения о родственных связях данного ФЛ\" Количество найденных инф: " + flRelativiesDTOS.size(), headers);

                    // Add data rows
                    int number = 1;
                    for (FlRelativiesDTO flRelativiesDTO : flRelativiesDTOS) {
                        XWPFTableRow row = docTable.createRow();
                        row.getCell(0).setText(String.valueOf(number));
                        row.getCell(1).setText(flRelativiesDTO.getRelative_type() != null ? flRelativiesDTO.getRelative_type() : "");
                        row.getCell(2).setText(flRelativiesDTO.getParent_fio() != null ? flRelativiesDTO.getParent_fio() : "");
                        row.getCell(3).setText(flRelativiesDTO.getMarriage_reg_date() != null ? flRelativiesDTO.getMarriage_reg_date().toString() : "");
                        row.getCell(4).setText(flRelativiesDTO.getMarriage_divorce_date() != null ? flRelativiesDTO.getMarriage_divorce_date().toString() : "");
                        row.getCell(5).setText(flRelativiesDTO.getParent_iin() != null ? flRelativiesDTO.getParent_iin() : "");
                        row.getCell(6).setText(flRelativiesDTO.getParent_birth_date() != null ? flRelativiesDTO.getParent_birth_date() : "");
                        number++;
                    }

                    // Adjust table formatting if needed
                    setMarginBetweenTables(doc);
                }if (flRelativiesDTOS != null && !flRelativiesDTOS.isEmpty()) {
                    for(FlRelativiesDTO flRelativiesDTOs : flRelativiesDTOS) {
                        if (flRelativiesDTOs.getParent_iin() != null) {
                            addForRelativesDoc(myService.getNode(flRelativiesDTOs.getParent_iin()),doc);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("FlRelativiesDTO table add exception: " + e.getMessage());
            }

            doc.write(baos);
            baos.close();
        }
    }

    private XWPFDocument addForRelativesDoc(NodesFL result, XWPFDocument doc){
        try {
            if (result.getMvFls() != null || result.getMvFls().size() < 0) {
                creteTitle(doc,"Сведения о физическом лице");
                XWPFTable table = doc.createTable();
                makeTableByProperties(doc, table, "Сведения о физическом лице", Arrays.asList(
                        "Фото",
                        "ИИН",
                        "ФИО",
                        "Резидент",
                        "Гражданство",
                        "Нация",
                        "Дата рождения",
                        "Место рождения",
                        "Статус",
                        "Дата смерти"
                ));

                XWPFTableRow row1 = table.createRow();
                XWPFTableCell cell1 = row1.getCell(0);

                XWPFParagraph paragraph2 = cell1.addParagraph();

                setCellPadding(cell1, 200, 200, 200, 200);
                XWPFRun run1 = paragraph2.createRun();
                try {
                    byte[] imageBytes = result.getPhotoDbf().get(0).getPhoto();
                    ByteArrayInputStream imageStream = new ByteArrayInputStream(imageBytes);
                    int imageType = XWPFDocument.PICTURE_TYPE_PNG; // Change according to your image type (e.g., PICTURE_TYPE_JPEG)
                    run1.addPicture(imageStream, imageType, "image.png", Units.toEMU(75), Units.toEMU(100));
                }catch (Exception e){
                    row1.getCell(0).setText("Фото не найдено");
                    System.out.println("Фото не найдено");
                }

                row1.getCell(1).setText(result.getMvFls().get(0).getIin());
                row1.getCell(2).setText(result.getMvFls().get(0).getLast_name() + "\n" + result.getMvFls().get(0).getFirst_name() + "\n" + result.getMvFls().get(0).getPatronymic());
                row1.getCell(3).setText(result.getMvFls().get(0).is_resident() ? "ДА" : "НЕТ");
                row1.getCell(4).setText(result.getMvFls().get(0).getCitizenship_ru_name());
                row1.getCell(5).setText(result.getMvFls().get(0).getNationality_ru_name());
                row1.getCell(6).setText(result.getMvFls().get(0).getBirth_date());
                row1.getCell(7).setText( result.getMvFls().get(0).getDistrict() + ", " + result.getMvFls().get(0).getRegion());
                row1.getCell(8).setText(result.getMvFls().get(0).getLife_status_ru_name());
                row1.getCell(9).setText(result.getMvFls().get(0).getDeath_date() !=null ? result.getMvFls().get(0).getDeath_date() : "Отсутсвует");
                setMarginBetweenTables(doc);
            }
        } catch (Exception e) {
            System.out.println("Mv_Fl table add exception");
        }
        try {
            List<MvIinDoc> docs = result.getMvIinDocs();
            if (docs != null && !docs.isEmpty()) {
                creteTitle(doc, "Наименование коллапса: \"Документы\" Количество найденных инф: " + docs.size());
                XWPFTable table = doc.createTable();

                // Define headers
                List<String> headers = Arrays.asList("№", "Типа Документа", "Орган выдачи", "Дата выдачи", "Срок до", "Номер документа");
                makeTableByProperties(doc, table, "Наименование коллапса: \"Документы\" Количество найденных инф: " + docs.size(), headers);

                int number = 1;
                for (MvIinDoc doci : docs) {
                    XWPFTableRow dataRow = table.createRow();
                    dataRow.getCell(0).setText(String.valueOf(number));
                    dataRow.getCell(1).setText(doci.getDoc_type_ru_name());
                    dataRow.getCell(2).setText(doci.getIssue_organization_ru_name());
                    dataRow.getCell(3).setText(doci.getIssue_date().toString());
                    dataRow.getCell(4).setText(doci.getExpiry_date().toString());
                    dataRow.getCell(5).setText(doci.getDoc_number());
                    number++;
                }

                setMarginBetweenTables(doc);
            }
        } catch (Exception e) {
            System.out.println("MV_Iin_Doc table add exception: " + e.getMessage());
        }
        try {
            List<MvFlAddress> addressFls = result.getMvFlAddresses();
            if (addressFls != null && !addressFls.isEmpty()) {
                creteTitle(doc, "Наименование коллапса: \"Адрес прописки\" Количество найденных инф: " + addressFls.size());
                XWPFTable table = doc.createTable();

                List<String> headers = Arrays.asList("№", "Город", "Улица, дом, квартира", "Район", "Дата регистрации");
                makeTableByProperties(doc, table, "Наименование коллапса: \"Адрес прописки\" Количество найденных инф: " + addressFls.size(), headers);

                int number = 1;
                for (MvFlAddress ar : addressFls) {
                    XWPFTableRow dataRow = table.createRow();
                    dataRow.getCell(0).setText(String.valueOf(number));
                    dataRow.getCell(1).setText(ar.getDistrict());
                    dataRow.getCell(2).setText(ar.getRegion());
                    dataRow.getCell(3).setText(ar.getStreet());
                    dataRow.getCell(4).setText(ar.getBuilding());
                    number++;
                }

                setMarginBetweenTables(doc);
            }
        } catch (Exception e) {
            System.out.println("MV_FL_Address table add exception: " + e.getMessage());
        }try {
            List<ChangeFio> changeFios = changeFioRepo.getByIin(result.getMvFls().get(0).getIin());
            if (changeFios != null && !changeFios.isEmpty()) {
                creteTitle(doc, "Наименование коллапса: \"Смена ФИО\" Количество найденных инф: " + changeFios.size());
                XWPFTable table = doc.createTable();

                List<String> headers = Arrays.asList("№", "Предыдущие данные", "Причина смены", "Дата смены ФИО");
                makeTableByProperties(doc, table, "Наименование коллапса: \"Смена ФИО\" Количество найденных инф: " + changeFios.size(), headers);

                int number = 1;
                for (ChangeFio ar : changeFios) {
                    XWPFTableRow dataRow = table.createRow();
                    dataRow.getCell(0).setText(String.valueOf(number));
                    dataRow.getCell(1).setText(
                            (ar.getSurname_before() != null ? ar.getSurname_before() : " ") + " " +
                                    (ar.getName_before() != null ? ar.getName_before() : " ") + " " +
                                    (ar.getSecondname_before() != null ? ar.getSecondname_before() : " ")
                    );
                    dataRow.getCell(2).setText(ar.getRemarks());
                    dataRow.getCell(3).setText(ar.getTo_date());
                    number++;
                }

                setMarginBetweenTables(doc);
            }
        } catch (Exception e) {
            System.out.println("ChangeFio table add exception: " + e.getMessage());
        }
        try {
            List<Adm> adms = admRepo.getUsersByLike(result.getMvFls().get(0).getIin());
            if (adms != null && !adms.isEmpty()) {
                creteTitle(doc, "Наименование коллапса: \"Административные штрафы\" Количество найденных инф: " + adms.size());
                XWPFTable table = doc.createTable();

                List<String> headers = Arrays.asList("№", "Орган выявивший правонарушение", "Дата заведения", "Номер протокола", "Место работы", "Квалификация", "Принудительное исполнение", "На срок до", "Размер наложенного штрафа", "Основания прекращения");
                makeTableByProperties(doc, table, "Наименование коллапса: \"Административные штрафы\" Количество найденных инф: " + adms.size(), headers);

                int number = 1;
                for (Adm r : adms) {
                    XWPFTableRow dataRow = table.createRow();
                    dataRow.getCell(0).setText(String.valueOf(number));
                    dataRow.getCell(1).setText(r.getAuthority_detected());
                    dataRow.getCell(2).setText(r.getReg_date());
                    dataRow.getCell(3).setText(r.getProtocol_num().toString());
                    dataRow.getCell(4).setText(r.getWork_place().toString());
                    dataRow.getCell(5).setText(r.getQualification_name());
                    dataRow.getCell(6).setText(r.getEnforcement());
                    dataRow.getCell(7).setText(r.getEnd_date());
                    dataRow.getCell(8).setText(r.getFine_amount());
                    dataRow.getCell(9).setText(r.getTeminate_reason());
                    number++;
                }

                setMarginBetweenTables(doc);
            }
        } catch (Exception e) {
            System.out.println("Adm table add exception: " + e.getMessage());
        }
        try {
            List<FlContacts> flContacts = flContactsRepo.findAllByIin(result.getMvIinDocs().get(0).getIin());
            if (flContacts != null && !flContacts.isEmpty()) {
                creteTitle(doc, "Контактные данные");
                XWPFTable table = doc.createTable();

                List<String> headers = Arrays.asList("№", "Номер телефона", "Email", "ФИО руководителя организации", "ФИО/Наименование организации владельца номера", "Источник", "Nickname");
                makeTableByProperties(doc, table, "Наименование коллапса: \"Контактные данные\" Количество найденных инф: " + flContacts.size(), headers);

                int number = 1;
                for (FlContacts flContacts1 : flContacts) {
                    XWPFTableRow dataRow = table.createRow();
                    dataRow.getCell(0).setText(String.valueOf(number));
                    dataRow.getCell(1).setText(flContacts1.getPhone() != null ? flContacts1.getPhone() : "");
                    dataRow.getCell(2).setText(flContacts1.getEmail() != null ? flContacts1.getEmail() : "");
                    dataRow.getCell(3).setText(flContacts1.getLeader_fio() != null ? flContacts1.getLeader_fio().toString() : "");
                    dataRow.getCell(4).setText(flContacts1.getNickname() != null ? flContacts1.getNickname().toString() : "");
                    dataRow.getCell(5).setText(flContacts1.getSource() != null ? flContacts1.getSource() : "");
                    dataRow.getCell(6).setText(flContacts1.getNickname() != null ? flContacts1.getNickname() : "");
                    number++;
                }

                setMarginBetweenTables(doc);
            }
        } catch (Exception e) {
            System.out.println("FlContacts table add exception: " + e.getMessage());
        }
        try {
            List<IndividualEntrepreneur> individualEntrepreneurs = individualEntrepreneurRepo.getByIin(result.getMvFls().get(0).getIin());
            List<KX> kxes = kxRepo.getKxIin(result.getMvFls().get(0).getIin());
            if ((individualEntrepreneurs != null && !individualEntrepreneurs.isEmpty()) || (kxes != null && !kxes.isEmpty())) {
                creteTitle(doc, "ИП/КХ");
                XWPFTable table = doc.createTable();

                List<String> headers = Arrays.asList("№", "Наименование", "ИП/КХ");
                makeTableByProperties(doc, table, "Наименование коллапса: \"ИП/КХ\" Количество найденных инф: " + (individualEntrepreneurs.size() + kxes.size()), headers);

                int number = 1;
                if (individualEntrepreneurs != null && !individualEntrepreneurs.isEmpty()) {
                    for (IndividualEntrepreneur individualEntrepreneur : individualEntrepreneurs) {
                        XWPFTableRow dataRow = table.createRow();
                        dataRow.getCell(0).setText(String.valueOf(number));
                        dataRow.getCell(1).setText(individualEntrepreneur.getName_rus() != null ? individualEntrepreneur.getName_rus() : "");
                        dataRow.getCell(2).setText("ИП");
                        number++;
                    }
                }
                if (kxes != null && !kxes.isEmpty()) {
                    for (KX kx : kxes) {
                        XWPFTableRow dataRow = table.createRow();
                        dataRow.getCell(0).setText(String.valueOf(number));
                        dataRow.getCell(1).setText(kx.getName_rus() != null ? kx.getName_rus() : "");
                        dataRow.getCell(2).setText("КХ");
                        number++;
                    }
                }

                setMarginBetweenTables(doc);
            }
        } catch (Exception e) {
            System.out.println("IndividualEntrepreneur/KX table add exception: " + e.getMessage());
        }
        try {
            GosZakupForAll gosZakupForAll = myService.gosZakupByBin(result.getMvFls().get(0).getIin());
            if ((gosZakupForAll.getWhenCustomer() != null && !gosZakupForAll.getWhenCustomer().isEmpty()) ||
                    (gosZakupForAll.getWhenSupplier() != null && !gosZakupForAll.getWhenSupplier().isEmpty())) {

                creteTitle(doc, "Государственные закупки- По поставщикам");
                XWPFTable table = doc.createTable();

                List<String> headers = Arrays.asList("№", "Год", "Общая сумма", "Количество договоров", "Поставщики");
                makeTableByProperties(doc, table, "Наименование коллапса: \"Государственные закупки- По поставщикам\" Количество найденных инф: " + (gosZakupForAll.getWhenSupplier().size() + gosZakupForAll.getWhenCustomer().size()), headers);

                int number = 1;
                if (gosZakupForAll.getWhenSupplier() != null && !gosZakupForAll.getWhenSupplier().isEmpty()) {
                    for (GosZakupDTO r : gosZakupForAll.getWhenSupplier()) {
                        XWPFTableRow dataRow = table.createRow();
                        dataRow.getCell(0).setText(String.valueOf(number));
                        dataRow.getCell(1).setText(r.getPeriod());
                        dataRow.getCell(2).setText(r.getSum());
                        dataRow.getCell(3).setText(String.valueOf(r.getNumber()));
                        dataRow.getCell(4).setText(r.getOpposite().toString());
                        number++;
                    }
                }
                if (gosZakupForAll.getWhenCustomer() != null && !gosZakupForAll.getWhenCustomer().isEmpty()) {
                    for (GosZakupDTO r : gosZakupForAll.getWhenCustomer()) {
                        XWPFTableRow dataRow = table.createRow();
                        dataRow.getCell(0).setText(String.valueOf(number));
                        dataRow.getCell(1).setText(r.getPeriod());
                        dataRow.getCell(2).setText(r.getSum());
                        dataRow.getCell(3).setText(String.valueOf(r.getNumber()));
                        dataRow.getCell(4).setText(r.getOpposite().toString());
                        number++;
                    }
                }

                setMarginBetweenTables(doc);
            }
        } catch (Exception e) {
            System.out.println("GosZakupForAll table add exception: " + e.getMessage());
        }
        try {
            List<String> companyBins = flPensionContrRepo.getUsersByLikeCompany(result.getMvFls().get(0).getIin());

            List<PensionListDTO> pensions = new ArrayList<>();
            List<PensionGroupDTO> results = new ArrayList<>();
            DecimalFormat df = new DecimalFormat("#");
            df.setMaximumFractionDigits(0);

            for (String bin : companyBins) {
                List<Map<String, Object>> fl_pension_contrss = flPensionContrRepo.getAllByCompanies(result.getMvFls().get(0).getIin(), bin);
                PensionGroupDTO obj = new PensionGroupDTO();
                List<PensionListDTO> group = new ArrayList<>();
                StringBuilder name = new StringBuilder();

                if (fl_pension_contrss.get(0).get("P_NAME") != null) {
                    name.append((String) fl_pension_contrss.get(0).get("P_NAME")).append(", ");
                }
                if (bin != null) {
                    name.append(bin).append(", период ");
                }

                List<String> distinctPayDates = fl_pension_contrss.stream()
                        .map(pension -> pension.get("pay_date").toString())
                        .distinct()
                        .collect(Collectors.toList());

                double knp010sum = 0.0;
                double knp012sum = 0.0;

                for (String year : distinctPayDates) {
                    if (year != null) {
                        name.append(year.replace(".0", "")).append(", ");
                    }

                    PensionListDTO pensionListEntity = new PensionListDTO();
                    pensionListEntity.setBin(bin);
                    pensionListEntity.setName((String) fl_pension_contrss.get(0).get("P_NAME"));
                    pensionListEntity.setPeriod(year.replace(".0", ""));

                    try {
                        double knp010 = fl_pension_contrss.stream()
                                .filter(pension -> pension.get("pay_date").toString().equals(year) && pension.get("KNP").toString().equals("010"))
                                .mapToDouble(pension -> Double.parseDouble(pension.get("AMOUNT").toString()))
                                .sum();

                        pensionListEntity.setSum010(knp010);
                        knp010sum += knp010;
                    } catch (Exception ignored) {
                    }

                    try {
                        double knp012 = fl_pension_contrss.stream()
                                .filter(pension -> pension.get("pay_date").toString().equals(year) && pension.get("KNP").toString().equals("012"))
                                .mapToDouble(pension -> Double.parseDouble(pension.get("AMOUNT").toString()))
                                .sum();

                        pensionListEntity.setSum012(knp012);
                        knp012sum += knp012;
                    } catch (Exception ignored) {
                    }

                    pensions.add(pensionListEntity);
                    group.add(pensionListEntity);
                }

                name.append("общая сумма КНП(010): ").append(df.format(knp010sum)).append(", общая сумма КНП(012): ").append(df.format(knp012sum));
                obj.setName(name.toString());
                obj.setList(group);
                results.add(obj);
            }

            if (results != null && !results.isEmpty()) {
                creteTitle(doc, "Пенсионные отчисления");
                XWPFTable table = doc.createTable();

                List<String> headers = Arrays.asList("№", "БИН, Наименование ЮЛ", "Период", "Общая сумма(010)", "Общая сумма(012)");
                makeTableByProperties(doc, table, "Наименование коллапса: \"Пенсионные отчисления\" Количество найденных инф: " + results.size(), headers);

                int number = 1;
                for (PensionGroupDTO pensionListDTO : results) {
                    XWPFTableRow dataRow = table.createRow();
                    dataRow.getCell(0).setText(String.valueOf(number));
                    dataRow.getCell(1).setText(PdfGenerator.getSubstring(pensionListDTO.getName(), 0, ", п"));
                    dataRow.getCell(2).setText(PdfGenerator.getSubstringByTwoWords(pensionListDTO.getName(), ", период ", ", общая"));
                    dataRow.getCell(3).setText(PdfGenerator.getSubstringByTwoWords(pensionListDTO.getName(), "общая сумма КНП(010): ", ", общая сумма КНП(012)"));
                    dataRow.getCell(4).setText(PdfGenerator.getSubstringByLastWord(pensionListDTO.getName(), "общая сумма КНП(012): ", pensionListDTO.getName()));
                    number++;
                }

                setMarginBetweenTables(doc);
            }
        } catch (Exception e) {
            System.out.println("PensionGroupDTO table add exception: " + e.getMessage());
        }
        try {
            // Fetch the data
            List<MvUlLeaderEntity> mvUlLeaderEntities = mvUlLeaderEntityRepo.getUsersByLikeIin(result.getMvFls().get(0).getIin());
            for (MvUlLeaderEntity mvUlLeaderEntity : mvUlLeaderEntities) {
                mvUlLeaderEntity.setBinName(mvUlRepo.getNameByBin(mvUlLeaderEntity.getBinOrg()));
            }

            List<MvUlFounderFl> mvUlFounderFls = result.getMvUlFounderFls();
            for (MvUlFounderFl mvUlFounderFl : mvUlFounderFls) {
                mvUlFounderFl.setBinName(mvUlRepo.getNameByBin(mvUlFounderFl.getBin_org()));
            }

            // Check if there is data to display
            if ((mvUlLeaderEntities != null && !mvUlLeaderEntities.isEmpty()) || (mvUlFounderFls != null && !mvUlFounderFls.isEmpty())) {
                // Create the table
                creteTitle(doc, "Наименование коллапса: \"Сведения об участниках ЮЛ\" Количество найденных инф: " + (mvUlLeaderEntities.size() + mvUlFounderFls.size()));
                XWPFTable table = doc.createTable();

                // Set up table headers
                List<String> headers = Arrays.asList("№", "Идентификатор ЮЛ", "Дата регистрации", "БИН", "Наименование ЮЛ");
                makeTableByProperties(doc, table, "Наименование коллапса: \"Сведения об участниках ЮЛ\" Количество найденных инф: " + (mvUlLeaderEntities.size() + mvUlFounderFls.size()), headers);

                // Add data rows
                int number = 1;
                for (MvUlLeaderEntity r : mvUlLeaderEntities) {
                    XWPFTableRow dataRow = table.createRow();
                    dataRow.getCell(0).setText(String.valueOf(number));
                    dataRow.getCell(1).setText("Директор");
                    dataRow.getCell(2).setText(r.getRegDate());
                    dataRow.getCell(3).setText(r.getBinOrg());
                    dataRow.getCell(4).setText(r.getBinName());
                    number++;
                }
                for (MvUlFounderFl r : mvUlFounderFls) {
                    XWPFTableRow dataRow = table.createRow();
                    dataRow.getCell(0).setText(String.valueOf(number));
                    dataRow.getCell(1).setText("Учредитель");
                    dataRow.getCell(2).setText(r.getReg_date());
                    dataRow.getCell(3).setText(r.getBin_org());
                    dataRow.getCell(4).setText(r.getBinName());
                    number++;
                }

                // Add the table to the document
                setMarginBetweenTables(doc);
            }
        } catch (Exception e) {
            System.out.println("Svidenia ob uchastnikah YU: Exception: " + e.getMessage());
        }
// Fetch the data
        List<MvRnOld> mvRnOlds = result.getMvRnOlds();

// Check if there is data to display
        if (mvRnOlds != null && !mvRnOlds.isEmpty()) {
            // Create the table
            creteTitle(doc, "Наименование коллапса: \"Сведения по реестру недвижимости\" Количество найденных инф: " + mvRnOlds.size());
            XWPFTable table = doc.createTable();

            // Set up table headers
            List<String> headers = Arrays.asList(
                    "№", "Кадастровый номер", "Адрес", "Правообладатель",
                    "Этажность", "Количество составляющих", "Площадь общая",
                    "Вид документа", "Номер документа", "Дата документа",
                    "Сумма сделки", "Жилая площадь", "Статус"
            );
            makeTableByProperties(doc, table,
                    "Наименование коллапса: \"Сведения по реестру недвижимости\" Количество найденных инф: " + mvRnOlds.size(),
                    headers
            );

            // Add data rows
            int number = 1;
            for (MvRnOld r : mvRnOlds) {
                XWPFTableRow dataRow = table.createRow();
                dataRow.getCell(0).setText(String.valueOf(number));
                dataRow.getCell(1).setText(r.getCadastral_number() != null ? r.getCadastral_number() : "");
                dataRow.getCell(2).setText(r.getAddress_rus() != null ? r.getAddress_rus() : "");
                dataRow.getCell(3).setText(r.getOwner_iin_bin() != null ? r.getOwner_iin_bin() : "");
                dataRow.getCell(4).setText(r.getFloor() != null ? r.getFloor() : "");
                dataRow.getCell(5).setText(r.getType_of_property_rus() != null ? r.getType_of_property_rus() : "");
                dataRow.getCell(6).setText(r.getArea_total() != null ? r.getArea_total() : "");
                dataRow.getCell(7).setText(r.getType_of_property_rus() != null ? r.getType_of_property_rus() : "");
                dataRow.getCell(8).setText(r.getRegister_emergence_rights_rus() != null ? r.getRegister_emergence_rights_rus() : "");
                dataRow.getCell(9).setText(r.getRegister_reg_date() != null ? r.getRegister_reg_date() : "");
                dataRow.getCell(10).setText(r.getRegister_transaction_amount() != null ? r.getRegister_transaction_amount() : "");
                dataRow.getCell(11).setText(r.getArea_useful() != null ? r.getArea_useful() : "");
                dataRow.getCell(12).setText(r.getRegister_status_rus() != null ? r.getRegister_status_rus() : "");
                number++;
            }

            // Add the table to the document
            setMarginBetweenTables(doc);
        } else {
            System.out.println("No data found for \"Сведения по реестру недвижимости\"");
        }
        List<School> schools = result.getSchools();
        if (schools != null && !schools.isEmpty()) {
            creteTitle(doc, "Наименование коллапса: \"Сведение по образование: Среднее образование (Школа)\" Количество найденных инф: " + schools.size());

            XWPFTable schoolTable = doc.createTable();
            // Set table headers
            List<String> headers = Arrays.asList(
                    "БИН", "Название школы", "Класс", "Дата поступления", "Дата окончания"
            );
            makeTableByProperties(doc, schoolTable,
                    "Наименование коллапса: \"Сведение по образование: Среднее образование (Школа)\" Количество найденных инф: " + schools.size(),
                    headers
            );

            // Add data rows
            for (School r : schools) {
                XWPFTableRow row = schoolTable.createRow();
                row.getCell(0).setText(r.getSchool_code() != null ? r.getSchool_code() : "");
                row.getCell(1).setText(r.getSchool_name() != null ? r.getSchool_name() : "");
                row.getCell(2).setText(r.getGrade() != null ? r.getGrade() : "");
                row.getCell(3).setText(r.getStart_date() != null ? r.getStart_date().toString() : "");
                row.getCell(4).setText(r.getEnd_date() != null ? r.getEnd_date().toString() : "");
            }

            setMarginBetweenTables(doc);
        } else {
            System.out.println("No data found for \"Сведение по образование: Среднее образование (Школа)\"");
        }
        try {
            List<Universities> universities = result.getUniversities();
            if (universities != null && !universities.isEmpty()) {
                // Create the title for the table
                creteTitle(doc, "Наименование коллапса: \"Сведение по образование: Высшее образование (Университет)\" Количество найденных инф: " + universities.size());

                // Create the table
                XWPFTable uniTable = doc.createTable();

                // Set headers
                List<String> headers = Arrays.asList("БИН", "Название вуза", "Специализация", "Дата поступления", "Дата окончания", "Длительность обучения", "Курс");
                makeTableByProperties(doc, uniTable, "Наименование коллапса: \"Сведение по образование: Высшее образование (Университет)\" Количество найденных инф: " + universities.size(), headers);

                int number = 1;
                for (Universities r : universities) {
                    XWPFTableRow row = uniTable.createRow();
                    row.getCell(0).setText(r.getStudy_code() != null ? r.getStudy_code() : "");
                    row.getCell(1).setText(r.getStudy_name() != null ? r.getStudy_name() : "");
                    row.getCell(2).setText(r.getSpec_name() != null ? r.getSpec_name() : "");
                    row.getCell(3).setText(r.getStart_date() != null ? r.getStart_date().toString() : "");
                    row.getCell(4).setText(r.getEnd_date() != null ? r.getEnd_date().toString() : "");
                    row.getCell(5).setText(r.getDuration() != null ? r.getDuration() : "");
                    row.getCell(6).setText(r.getCourse() != null ? r.getCourse() : "");
                    number++;
                }

                setMarginBetweenTables(doc);
            }
        } catch (Exception e) {
            System.out.println("Universities table add exception: " + e.getMessage());
        }
        try {
            List<MvAutoFl> autos = result.getMvAutoFls();
            if (autos != null && !autos.isEmpty()) {
                // Create the title for the table
                creteTitle(doc, "Наименование коллапса: \"Транспорт\" Количество найденных инф: " + autos.size());

                // Create the table
                XWPFTable autoTable = doc.createTable();

                // Set headers
                List<String> headers = Arrays.asList("№", "Статус", "Регистрационный номер", "Марка модель", "Дата выдачи свидетельства", "Дата снятия", "Год выпуска", "Категория", "VIN/Кузов/Шосси", "Серия");
                makeTableByProperties(doc, autoTable, "Наименование коллапса: \"Транспорт\" Количество найденных инф: " + autos.size(), headers);

                int number = 1;
                for (MvAutoFl r : autos) {
                    XWPFTableRow row = autoTable.createRow();
                    row.getCell(0).setText(String.valueOf(number));

                    try {
                        row.getCell(1).setText(r.isIs_registered() ? "Текущий" : "Исторический");
                    } catch (Exception e) {
                        row.getCell(1).setText("");
                    }

                    try {
                        row.getCell(2).setText(r.getReg_number() != null ? r.getReg_number() : "");
                    } catch (Exception e) {
                        row.getCell(2).setText("");
                    }

                    try {
                        row.getCell(3).setText(r.getBrand_model() != null ? r.getBrand_model() : "");
                    } catch (Exception e) {
                        row.getCell(3).setText("");
                    }

                    try {
                        row.getCell(4).setText(r.getDate_certificate() != null ? r.getDate_certificate().toString() : "");
                    } catch (Exception e) {
                        row.getCell(4).setText("");
                    }

                    try {
                        row.getCell(5).setText(r.getEnd_date() != null ? r.getEnd_date().toString() : "");
                    } catch (Exception e) {
                        row.getCell(5).setText("");
                    }

                    row.getCell(6).setText(r.getRelease_year_tc() != null ? r.getRelease_year_tc() : "");
                    row.getCell(7).setText(r.getCategory_control_tc() != null ? r.getCategory_control_tc() : "");
                    row.getCell(8).setText(r.getVin_kuzov_shassi() != null ? r.getVin_kuzov_shassi() : "");
                    row.getCell(9).setText(r.getSeries_reg_number() != null ? r.getSeries_reg_number() : "");

                    number++;
                }

                setMarginBetweenTables(doc);
            }
        } catch (Exception e) {
            System.out.println("MvAutoFl table add exception: " + e.getMessage());
        }
        try {
            List<MillitaryAccount> millitaryAccounts = result.getMillitaryAccounts();
            if (millitaryAccounts != null && !millitaryAccounts.isEmpty()) {
                // Create the title for the table
                creteTitle(doc, "Войнский учет");

                // Create the table
                XWPFTable maTable = doc.createTable();

                // Set headers
                List<String> headers = Arrays.asList("№", "БИН воинской части", "Дата службы с", "Дата службы по");
                makeTableByProperties(doc, maTable, "Войнский учет", headers);

                int number = 1;
                for (MillitaryAccount r : millitaryAccounts) {
                    XWPFTableRow row = maTable.createRow();
                    row.getCell(0).setText(String.valueOf(number));
                    row.getCell(1).setText(r.getBin() != null ? r.getBin() : "");
                    row.getCell(2).setText(r.getDate_start() != null ? r.getDate_start() : "");
                    row.getCell(3).setText(r.getDate_end() != null ? r.getDate_end() : "");
                    number++;
                }

                setMarginBetweenTables(doc);
            }
        } catch (Exception e) {
            System.out.println("MillitaryAccount table add exception: " + e.getMessage());
        }
//        try {
//            List<ConvictsJustified> convictsJustifieds = result.getConvictsJustifieds();
//            if (convictsJustifieds != null && !convictsJustifieds.isEmpty()) {
//                // Create the title for the table
//                creteTitle(doc, "Наименование риска: \"Осужденные\" Количество найденных инф: " + convictsJustifieds.size());
//
//                // Create the table
//                XWPFTable convictsTable = doc.createTable();
//
//                // Set headers
//                List<String> headers = Arrays.asList(
//                        "№",
//                        "Дата рассмотрения в суде 1 инстанции",
//                        "Суд 1 инстанции",
//                        "Решение по лицу",
//                        "Мера наказания по договору",
//                        "Квалификация"
//                );
//                makeTableByProperties(doc, convictsTable, "Наименование риска: \"Осужденные\" Количество найденных инф: " + convictsJustifieds.size(), headers);
//
//                int number = 1;
//                for (ConvictsJustified r : convictsJustifieds) {
//                    XWPFTableRow row = convictsTable.createRow();
//                    row.getCell(0).setText(String.valueOf(number));
//                    row.getCell(1).setText(r.getReg_date() != null ? r.getReg_date() : "");
//                    row.getCell(2).setText(r.getCourt_of_first_instance() != null ? r.getCourt_of_first_instance() : "");
//                    row.getCell(3).setText(r.getDecision_on_person() != null ? r.getDecision_on_person() : "");
//                    row.getCell(4).setText(r.getMeasure_punishment() != null ? r.getMeasure_punishment() : "");
//                    row.getCell(5).setText(r.getQualification() != null ? r.getQualification() : "");
//                    number++;
//                }
//
//                setMarginBetweenTables(doc);
//            }
//        } catch (Exception e) {
//            System.out.println("ConvictsJustified table add exception: " + e.getMessage());
//        }
        try {
            List<ConvictsTerminatedByRehab> convictsTerminatedByRehabs = result.getConvictsTerminatedByRehabs();
            if (convictsTerminatedByRehabs != null && !convictsTerminatedByRehabs.isEmpty()) {
                // Create the title for the table
                creteTitle(doc, "Заключенный Рехаб. Количество найденных инф: " + convictsTerminatedByRehabs.size());

                // Create the table
                XWPFTable ctbrTable = doc.createTable();

                // Set headers
                List<String> headers = Arrays.asList(
                        "№",
                        "Орган выявивший правонарушение",
                        "Дата заведения",
                        "Квалификация",
                        "Решение",
                        "Уровень тяжести"
                );
                makeTableByProperties(doc, ctbrTable, "Заключенный Рехаб. Количество найденных инф: " + convictsTerminatedByRehabs.size(), headers);

                int number = 1;
                for (ConvictsTerminatedByRehab r : convictsTerminatedByRehabs) {
                    XWPFTableRow row = ctbrTable.createRow();
                    row.getCell(0).setText(String.valueOf(number));
                    row.getCell(1).setText(r.getInvestigative_authority() != null ? r.getInvestigative_authority() : "");
                    row.getCell(2).setText(r.getLast_solution_date() != null ? r.getLast_solution_date() : "");
                    row.getCell(3).setText(r.getQualification_desc() != null ? r.getQualification_desc() : "");
                    row.getCell(4).setText(r.getLast_solution() != null ? r.getLast_solution() : "");
                    row.getCell(5).setText(r.getQualification_by_11() != null ? r.getQualification_by_11() : "");
                    number++;
                }

                setMarginBetweenTables(doc);
            }
        } catch (Exception e) {
            System.out.println("ConvictsTerminatedByRehab table add exception: " + e.getMessage());
        }


        try {
            List<BlockEsf> blockEsfs = result.getBlockEsfs();
            if (blockEsfs != null && !blockEsfs.isEmpty()) {
                // Create the title for the table
                creteTitle(doc, "Блок ЭСФ");

                // Create the table
                XWPFTable blockesfTable = doc.createTable();

                // Set headers
                List<String> headers = Arrays.asList(
                        "№",
                        "Дата блокировки",
                        "Дата восстановления",
                        "Дата обновления"
                );
                makeTableByProperties(doc, blockesfTable, "Блок ЭСФ. Количество найденных инф: " + blockEsfs.size(), headers);

                int number = 1;
                for (BlockEsf r : blockEsfs) {
                    XWPFTableRow row = blockesfTable.createRow();
                    row.getCell(0).setText(String.valueOf(number));

                    row.getCell(1).setText(r.getStart_dt() != null ? r.getStart_dt().toString() : "");
                    row.getCell(2).setText(r.getEnd_dt() != null ? r.getEnd_dt().toString() : "");
                    row.getCell(3).setText(r.getUpdate_dt() != null ? r.getUpdate_dt().toString() : "");

                    number++;
                }

                setMarginBetweenTables(doc);
            }
        } catch (Exception e) {
            System.out.println("BlockEsf table add exception: " + e.getMessage());
        }
        try {
            List<NdsEntity> ndsEntities = result.getNdsEntities();
            if (ndsEntities != null && !ndsEntities.isEmpty()) {
                // Create the title for the table
                creteTitle(doc, "НДС");

                // Create the table
                XWPFTable ndsTable = doc.createTable();

                // Set headers
                List<String> headers = Arrays.asList(
                        "№",
                        "Дата начала",
                        "Дата конца",
                        "Дата обновления",
                        "Причина"
                );
                makeTableByProperties(doc, ndsTable, "НДС. Количество найденных инф: " + ndsEntities.size(), headers);

                int number = 1;
                for (NdsEntity r : ndsEntities) {
                    XWPFTableRow row = ndsTable.createRow();
                    row.getCell(0).setText(String.valueOf(number));

                    row.getCell(1).setText(r.getStartDt() != null ? r.getStartDt().toString() : "");
                    row.getCell(2).setText(r.getEndDt() != null ? r.getEndDt().toString() : "");
                    row.getCell(3).setText(r.getUpdateDt() != null ? r.getUpdateDt().toString() : "");
                    row.getCell(4).setText(r.getReason() != null ? r.getReason() : "");

                    number++;
                }

                setMarginBetweenTables(doc);
            }
        } catch (Exception e) {
            System.out.println("NdsEntity table add exception: " + e.getMessage());
        }
        try {
            List<IpgoEmailEntity> ipgoEmailEntities = result.getIpgoEmailEntities();
            if (ipgoEmailEntities != null && !ipgoEmailEntities.isEmpty()) {
                // Create the title for the table
                creteTitle(doc, "Сведения по ИПГО");

                // Create the table
                XWPFTable ipgoTable = doc.createTable();

                // Set headers
                List<String> headers = Arrays.asList(
                        "№",
                        "Департамент",
                        "Должность",
                        "ИПГО почта"
                );
                makeTableByProperties(doc, ipgoTable, "Сведения по ИПГО. Количество найденных инф: " + ipgoEmailEntities.size(), headers);

                int number = 1;
                for (IpgoEmailEntity r : ipgoEmailEntities) {
                    XWPFTableRow row = ipgoTable.createRow();
                    row.getCell(0).setText(String.valueOf(number));

                    row.getCell(1).setText(r.getOrgan() != null ? r.getOrgan().toString() : "");
                    row.getCell(2).setText(r.getPosition() != null ? r.getPosition() : "");
                    row.getCell(3).setText(r.getEmail() != null ? r.getEmail().toString() : "");

                    number++;
                }

                setMarginBetweenTables(doc);
            }
        } catch (Exception e) {
            System.out.println("IpgoEmailEntity table add exception: " + e.getMessage());
        }

        try {
            List<Bankrot> bankrotEntities = result.getBankrots();
            if (bankrotEntities != null && !bankrotEntities.isEmpty()) {
                creteTitle(doc,"Сведения по банкротам");
                XWPFTable table = doc.createTable();
                table.setWidth("100%");
                makeTableByProperties(doc, table, "Сведения по банкротам", Arrays.asList("№", "ИИН/БИН", "Документ", "Дата обновления", "Причина"));

                int number = 1;
                for (Bankrot r : bankrotEntities) {
                    XWPFTableRow dataRow = table.createRow();
                    dataRow.getCell(0).setText(String.valueOf(number));
                    dataRow.getCell(1).setText(r.getIin_bin() != null ? r.getIin_bin() : "");
                    dataRow.getCell(2).setText(r.getDocument() != null ? r.getDocument() : "");
                    dataRow.getCell(3).setText(r.getUpdate_dt() != null ? r.getUpdate_dt().toString() : "");
                    dataRow.getCell(4).setText(r.getReason() != null ? r.getReason() : "");
                    number++;
                }
                setMarginBetweenTables(doc);
                // Save the document as needed
            }
        } catch (Exception e) {
            System.out.println("Exception while adding bankrot entities table: " + e.getMessage());
        }
        try {
            if (result.getFirstCreditBureauEntities() != null && !result.getFirstCreditBureauEntities().isEmpty()) {
                creteTitle(doc,"Сведения по кредитным бюро");
                XWPFTable table = doc.createTable();
                table.setWidth("100%");
                makeTableByProperties(doc, table, "Сведения по кредитным бюро", Arrays.asList(
                        "№", "Тип", "Кредит в FOID", "Регион", "Количество FPD SPD", "Сумма долга", "Макс. задержка дней", "Фин. учреждения", "Общее количество кредитов"));


                int number = 1;
                for (FirstCreditBureauEntity entity : result.getFirstCreditBureauEntities()) {
                    XWPFTableRow dataRow = table.createRow();
                    dataRow.getCell(0).setText(String.valueOf(number));
                    dataRow.getCell(1).setText(entity.getType() != null ? entity.getType() : "");
                    dataRow.getCell(2).setText(entity.getCreditInFoid() != null ? entity.getCreditInFoid().toString() : "");
                    dataRow.getCell(3).setText(entity.getRegion() != null ? entity.getRegion() : "");
                    dataRow.getCell(4).setText(entity.getQuantityFpdSpd() != null ? entity.getQuantityFpdSpd().toString() : "");
                    dataRow.getCell(5).setText(entity.getAmountOfDebt() != null ? entity.getAmountOfDebt().toString() : "");
                    dataRow.getCell(6).setText(entity.getMaxDelayDayNum1() != null ? entity.getMaxDelayDayNum1().toString() : "");
                    dataRow.getCell(7).setText(entity.getFinInstitutionsName() != null ? entity.getFinInstitutionsName() : "");
                    dataRow.getCell(8).setText(entity.getTotalCountOfCredits() != null ? entity.getTotalCountOfCredits().toString() : "");
                    number++;
                }
                setMarginBetweenTables(doc);
                // Save the document as needed
            }
        } catch (Exception e) {
            System.out.println("Exception while adding first credit bureau entities table: " + e.getMessage());
        }
        try {
            if (result.getAmoral() != null && !result.getAmoral().isEmpty()) {
                creteTitle(doc,"Сведения по аморальному образу жизни");
                XWPFTable table = doc.createTable();
                table.setWidth("100%");
                makeTableByProperties(doc, table, "Сведения по аморальному образу жизни", Arrays.asList("№", "Орган выявивший", "Гражданство", "Дата решения", "Сумма штрафа"));


                int number = 1;
                for (ImmoralLifestyle r : result.getAmoral()) {
                    XWPFTableRow dataRow = table.createRow();
                    dataRow.getCell(0).setText(String.valueOf(number));
                    dataRow.getCell(1).setText(r.getAuthority_detected() != null ? r.getAuthority_detected() : "");
                    dataRow.getCell(2).setText(r.getCitizenship_id() != null ? r.getCitizenship_id() : "");
                    dataRow.getCell(3).setText(r.getDecision_date() != null ? r.getDecision_date().toString() : "");
                    dataRow.getCell(4).setText(r.getFine_amount() != null ? r.getFine_amount().toString() : "");
                    number++;
                }
                setMarginBetweenTables(doc);
                // Save the document as needed
            }
        } catch (Exception e) {
            System.out.println("Exception while adding immoral lifestyle entities table: " + e.getMessage());
        }try {
            if (result.getMzEntities() != null && !result.getMzEntities().isEmpty()) {
                creteTitle(doc,"Сведения по МЗ");
                XWPFTable table = doc.createTable();
                table.setWidth("100%");
                makeTableByProperties(doc, table, "Сведения по МЗ", Arrays.asList("№", "Код болезни", "Регистрация", "Статус МЗ", "Медицинская организация"));


                int number = 1;
                for (MzEntity r : result.getMzEntities()) {
                    XWPFTableRow dataRow = table.createRow();
                    dataRow.getCell(0).setText(String.valueOf(number));
                    dataRow.getCell(1).setText(r.getDiseaseCode() != null ? r.getDiseaseCode() : "");
                    dataRow.getCell(2).setText(r.getReg() != null ? r.getReg() : "");
                    dataRow.getCell(3).setText(r.getStatusMz() != null ? r.getStatusMz() : "");
                    dataRow.getCell(4).setText(r.getMedicalOrg() != null ? r.getMedicalOrg() : "");
                    number++;                  }
                setMarginBetweenTables(doc);
            }
        } catch (Exception e) {
            System.out.println("Exception while adding MZ entities table: " + e.getMessage());
        }try {
            if (result.getWantedListEntities() != null && !result.getWantedListEntities().isEmpty()) {
                creteTitle(doc,"Сведения по разыскиваемым");
                XWPFTable table = doc.createTable();
                table.setWidth("100%");
                makeTableByProperties(doc, table, "Сведения по разыскиваемым", Arrays.asList("№", "Дни", "Орган", "Статус", "Дата актуальности"));


                int number = 1;
                for (WantedListEntity r : result.getWantedListEntities()) {
                    XWPFTableRow dataRow = table.createRow();
                    dataRow.getCell(0).setText(String.valueOf(number));
                    dataRow.getCell(1).setText(r.getDays() != null ? r.getDays().toString() : "");
                    dataRow.getCell(2).setText(r.getOrgan() != null ? r.getOrgan() : "");
                    dataRow.getCell(3).setText(r.getStatus() != null ? r.getStatus() : "");
                    dataRow.getCell(4).setText(r.getRelevanceDate() != null ? r.getRelevanceDate().toString() : "");
                    number++;                    }
                setMarginBetweenTables(doc);
                // Save the document as needed
            }
        } catch (Exception e) {
            System.out.println("Exception while adding wanted list entities table: " + e.getMessage());
        }

        try {
            List<FlRelativiesDTO> flRelativiesDTOS = myService.getRelativesInfo(result.getMvIinDocs().get(0).getIin());
            if (flRelativiesDTOS != null && !flRelativiesDTOS.isEmpty()) {
                // Create the title for the table
                creteTitle(doc, "Сведения о родственных связях данного ФЛ");

                // Create the table
                XWPFTable docTable = doc.createTable();

                // Set headers
                List<String> headers = Arrays.asList(
                        "№",
                        "Статус по отношению к родственнику",
                        "ФИО",
                        "Дата регистрации брака",
                        "Дата расторжения брака",
                        "ИИН родственника",
                        "Дата рождения"
                );
                makeTableByProperties(doc, docTable, "Наименование коллапса: \"Сведения о родственных связях данного ФЛ\" Количество найденных инф: " + flRelativiesDTOS.size(), headers);

                // Add data rows
                int number = 1;
                for (FlRelativiesDTO flRelativiesDTO : flRelativiesDTOS) {
                    XWPFTableRow row = docTable.createRow();
                    row.getCell(0).setText(String.valueOf(number));
                    row.getCell(1).setText(flRelativiesDTO.getRelative_type() != null ? flRelativiesDTO.getRelative_type() : "");
                    row.getCell(2).setText(flRelativiesDTO.getParent_fio() != null ? flRelativiesDTO.getParent_fio() : "");
                    row.getCell(3).setText(flRelativiesDTO.getMarriage_reg_date() != null ? flRelativiesDTO.getMarriage_reg_date().toString() : "");
                    row.getCell(4).setText(flRelativiesDTO.getMarriage_divorce_date() != null ? flRelativiesDTO.getMarriage_divorce_date().toString() : "");
                    row.getCell(5).setText(flRelativiesDTO.getParent_iin() != null ? flRelativiesDTO.getParent_iin() : "");
                    row.getCell(6).setText(flRelativiesDTO.getParent_birth_date() != null ? flRelativiesDTO.getParent_birth_date() : "");
                    number++;
                }

                // Adjust table formatting if needed
                setMarginBetweenTables(doc);
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return doc;
    }

    public void setCellPadding(XWPFTableCell cell, int top, int left, int bottom, int right) {
        CTTcPr tcPr = cell.getCTTc().addNewTcPr();

        org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcMar cellMar = tcPr.isSetTcMar() ? tcPr.getTcMar() : tcPr.addNewTcMar();
        cellMar.addNewTop().setW(BigInteger.valueOf(top));
        cellMar.addNewLeft().setW(BigInteger.valueOf(left));
        cellMar.addNewBottom().setW(BigInteger.valueOf(bottom));
        cellMar.addNewRight().setW(BigInteger.valueOf(right));
    }

    public void generateUl(NodesUL result, ByteArrayOutputStream baos) throws IOException {
        try (XWPFDocument doc = new XWPFDocument()) {
            CTDocument1 document = doc.getDocument();
            CTBody body = document.getBody();

            if (!body.isSetSectPr()) {
                body.addNewSectPr();
            }
            CTSectPr section = body.getSectPr();

            if(!section.isSetPgSz()) {
                section.addNewPgSz();
            }
            CTPageSz pageSize = section.getPgSz();

            pageSize.setW(BigInteger.valueOf(15840));
            pageSize.setH(BigInteger.valueOf(12240));
            try {
                if (result.getMvUls() != null && !result.getMvUls().isEmpty()) {
                    creteTitle(doc, "Сведения о юридическом лице");
                    XWPFTable table = doc.createTable();
                    table.setWidth("100%");
                    makeTableByProperties(doc, table, "Сведения о юридическом лице", Arrays.asList(
                            "БИН",
                            "Наименование организации",
                            "Наименование ОКЭД",
                            "Статус ЮЛ"
                    ));

                    int number = 1;
                    for (MvUl a : result.getMvUls()) {
                        XWPFTableRow dataRow = table.createRow();
                        dataRow.getCell(0).setText(String.valueOf(number));
                        dataRow.getCell(1).setText(a.getBin() != null ? a.getBin() : "");
                        dataRow.getCell(2).setText(a.getFull_name_kaz() != null ? a.getFull_name_kaz() : "");
                        dataRow.getCell(3).setText(a.getHead_organization() != null ? a.getHead_organization() : "");
                        dataRow.getCell(4).setText(a.getUl_status() != null ? a.getUl_status() : "");
                        number++;
                    }
                    setMarginBetweenTables(doc);
                }
            } catch (Exception e) {
                System.out.println("Exception while adding MV UL table: " + e.getMessage());
            }

            try {
                if (result.getMvUlFounderFls() != null && !result.getMvUlFounderFls().isEmpty()) {
                    creteTitle(doc, "Сведения об участниках ЮЛ");
                    XWPFTable table = doc.createTable();
                    makeTableByProperties(doc, table, "Сведения об участниках ЮЛ", Arrays.asList(
                            "№",
                            "БИН",
                            "Наименование ЮЛ",
                            "Дата регистрации"
                    ));

                    int number = 1;
                    for (MvUlFounderFl a : result.getMvUlFounderFls()) {
                        XWPFTableRow dataRow = table.createRow();
                        dataRow.getCell(0).setText(String.valueOf(number));
                        String name = mvUlRepo.getNameByBin(a.getBin_org());
                        dataRow.getCell(1).setText(name != null ? name : "Нет");
                        dataRow.getCell(2).setText(a.getReg_date() != null ? a.getReg_date().toString() : "Нет даты");
                        number++;
                    }
                    setMarginBetweenTables(doc);
                }
            } catch (Exception e) {
                System.out.println("Error adding MV Ul Founder Fl table: " + e.getMessage());
            }

            try {
                if (result.getAccountantListEntities() != null && !result.getAccountantListEntities().isEmpty()) {
                    creteTitle(doc, "Список бухгалтеров");
                    XWPFTable table = doc.createTable();
                    makeTableByProperties(doc, table, "Список бухгалтеров", Arrays.asList(
                            "№",
                            "ИИН",
                            "Проф.",
                            "Фамилия",
                            "Имя"
                    ));

                    int number = 1;
                    for (AccountantListEntity a : result.getAccountantListEntities()) {
                        XWPFTableRow dataRow = table.createRow();
                        dataRow.getCell(0).setText(String.valueOf(number));
                        dataRow.getCell(1).setText(a.getIin() != null ? a.getIin() : "");
                        dataRow.getCell(2).setText(a.getProf() != null ? a.getProf() : "");
                        dataRow.getCell(3).setText(a.getLname() != null ? a.getLname() : "");
                        dataRow.getCell(4).setText(a.getFname() != null ? a.getFname() : "");
                        number++;
                    }
                    setMarginBetweenTables(doc);
                }
            } catch (Exception e) {
                System.out.println("Error adding AccountantListEntities table: " + e.getMessage());
            }

            try {
                List<Omn> omns = result.getOmns();
                if (omns != null && !omns.isEmpty()) {
                    creteTitle(doc, "ОМНС");
                    XWPFTable table = doc.createTable();
                    makeTableByProperties(doc, table, "ОМНС", Arrays.asList(
                            "РНН",
                            "Название налогоплательщика",
                            "ФИО налогоплательщика",
                            "ФИО руководителя",
                            "ИИН руководителя",
                            "РНН руководителя"
                    ));

                    int number = 1;
                    for (Omn a : omns) {
                        XWPFTableRow dataRow = table.createRow();
                        dataRow.getCell(0).setText(String.valueOf(number));
                        dataRow.getCell(1).setText(a.getRnn() != null ? a.getRnn() : "");
                        dataRow.getCell(2).setText(a.getTaxpayer_name() != null ? a.getTaxpayer_name() : "");
                        dataRow.getCell(3).setText(a.getTaxpayer_fio() != null ? a.getTaxpayer_fio() : "");
                        dataRow.getCell(4).setText(a.getLeader_fio() != null ? a.getLeader_fio() : "");
                        dataRow.getCell(5).setText(a.getLeader_iin() != null ? a.getLeader_iin() : "");
                        dataRow.getCell(6).setText(a.getLeader_rnn() != null ? a.getLeader_rnn() : "");
                        number++;
                    }
                    setMarginBetweenTables(doc);
                }
            } catch (Exception e) {
                System.out.println("Error adding OMNS table: " + e.getMessage());
            }

            try {
                List<Equipment> equipmentList = result.getEquipment();
                if (equipmentList != null && !equipmentList.isEmpty()) {
                    creteTitle(doc, "Транспорт");
                    XWPFTable table = doc.createTable();
                    makeTableByProperties(doc, table, "Транспорт", Arrays.asList(
                            "№",
                            "Адрес",
                            "Гос. Номер",
                            "Номер серии рег.",
                            "Номер серии рег.",
                            "Дата регистрации",
                            "Причина",
                            "VIN",
                            "Спец.",
                            "Тип",
                            "Форма",
                            "Брэнд",
                            "Модель"
                    ));

                    int number = 1;
                    for (Equipment a : equipmentList) {
                        XWPFTableRow dataRow = table.createRow();
                        dataRow.getCell(0).setText(String.valueOf(number));
                        dataRow.getCell(1).setText(a.getOwner_address() != null ? a.getOwner_address() : "");
                        dataRow.getCell(2).setText(a.getGov_number() != null ? a.getGov_number() : "");
                        dataRow.getCell(3).setText(a.getReg_series_num() != null ? a.getReg_series_num() : "");
                        dataRow.getCell(4).setText(a.getReg_date() != null ? a.getReg_date() : "");
                        dataRow.getCell(5).setText(a.getReg_reason() != null ? a.getReg_reason() : "");
                        dataRow.getCell(6).setText(a.getVin() != null ? a.getVin() : "");
                        dataRow.getCell(7).setText(a.getEquipment_spec() != null ? a.getEquipment_spec() : "");
                        dataRow.getCell(8).setText(a.getEquipment_type() != null ? a.getEquipment_type() : "");
                        dataRow.getCell(9).setText(a.getEquipment_form() != null ? a.getEquipment_form() : "");
                        dataRow.getCell(10).setText(a.getBrand() != null ? a.getBrand() : "");
                        dataRow.getCell(11).setText(a.getEquipment_model() != null ? a.getEquipment_model() : "");
                        number++;
                    }
                    setMarginBetweenTables(doc);
                }
            } catch (Exception e) {
                System.out.println("Error adding Equipment table: " + e.getMessage());
            }

            try {
                List<Msh> mshes = result.getMshes();
                if (mshes != null && !mshes.isEmpty()) {
                    creteTitle(doc, "МШЭС");
                    XWPFTable table = doc.createTable();
                    makeTableByProperties(doc, table, "МШЭС", Arrays.asList(
                            "№",
                            "Тип оборудования",
                            "Модель оборудования",
                            "VIN",
                            "Гос. номер",
                            "Дата регистрации"
                    ));

                    int number = 1;
                    for (Msh a : mshes) {
                        XWPFTableRow dataRow = table.createRow();
                        dataRow.getCell(0).setText(String.valueOf(number));
                        dataRow.getCell(2).setText(a.getEquipmentModel() != null ? a.getEquipmentModel() : "");
                        dataRow.getCell(3).setText(a.getVin() != null ? a.getVin() : "");
                        dataRow.getCell(4).setText(a.getGovNumber() != null ? a.getGovNumber() : "");
                        dataRow.getCell(5).setText(a.getRegDate() != null ? a.getRegDate().toString() : "");
                        number++;
                    }
                    setMarginBetweenTables(doc);
                }
            } catch (Exception e) {
                System.out.println("Error adding MSHES table: " + e.getMessage());
            }
            try {
                List<Dormant> dormants = result.getDormants();
                if (dormants != null && !dormants.isEmpty()) {
                    creteTitle(doc,"Дорманс");
                    XWPFTable table = doc.createTable();
                    makeTableByProperties(doc, table, "Дорманс", Arrays.asList(
                            "№",
                            "РНН",
                            "Название налогоплательщика",
                            "ФИО налогоплательщика",
                            "ФИО руководителя",
                            "ИИН руководителя",
                            "РНН руководителя",
                            "Дата заказа"
                    ));

                    int number = 1;
                    for (Dormant a : dormants) {
                        XWPFTableRow dataRow = table.createRow();
                        dataRow.getCell(0).setText(String.valueOf(number));
                        dataRow.getCell(1).setText(a.getRnn() != null ? a.getRnn() : "");
                        dataRow.getCell(2).setText(a.getTaxpayer_name() != null ? a.getTaxpayer_name() : "");
                        dataRow.getCell(3).setText(a.getTaxpayer_fio() != null ? a.getTaxpayer_fio() : "");
                        dataRow.getCell(4).setText(a.getLeader_fio() != null ? a.getLeader_fio() : "");
                        dataRow.getCell(5).setText(a.getLeader_iin() != null ? a.getLeader_iin() : "");
                        dataRow.getCell(6).setText(a.getLeader_rnn() != null ? a.getLeader_rnn() : "");
                        dataRow.getCell(7).setText(a.getOrder_date() != null ? a.getOrder_date() : "");
                        number++;
                    }
                    setMarginBetweenTables(doc);
                }
            } catch (Exception e) {
                System.out.println("Error adding Dormant table: " + e.getMessage());
            }

            try {
                List<Bankrot> bankrots = result.getBankrots();
                if (bankrots != null && !bankrots.isEmpty()) {
                    creteTitle(doc, "Банкроты");
                    XWPFTable table = doc.createTable();
                    makeTableByProperties(doc, table, "Банкроты", Arrays.asList(
                            "№",
                            "Документ",
                            "Дата обновления",
                            "Причина"
                    ));

                    int number = 1;
                    for (Bankrot a : bankrots) {
                        XWPFTableRow dataRow = table.getRow(number);
                        dataRow.getCell(0).setText(String.valueOf(number));
                        dataRow.getCell(1).setText(a.getDocument() != null ? a.getDocument() : "");
                        try {
                            dataRow.getCell(2).setText(a.getUpdate_dt() != null ? a.getUpdate_dt().toString() : "Дата отсутствует");
                        } catch (Exception e) {
                            dataRow.getCell(2).setText("Дата отсутствует");
                        }
                        dataRow.getCell(3).setText(a.getReason() != null ? a.getReason() : "");
                        number++;
                    }
                    setMarginBetweenTables(doc);
                }
            } catch (Exception e) {
                System.out.println("Error adding Bankrot table: " + e.getMessage());
            }

            try {
                List<Adm> adms = result.getAdms();
                if (adms != null && !adms.isEmpty()) {
                    creteTitle(doc, "Администрация");
                    XWPFTable table = doc.createTable();
                    makeTableByProperties(doc, table, "Администрация", Arrays.asList(
                            "№",
                            "Номер материала",
                            "Дата регистрации",
                            "15",
                            "16",
                            "17",
                            "Наименование юр. лица",
                            "Адрес юр. лица",
                            "Марка автомобиля",
                            "Гос. Номер авто"
                    ));

                    int number = 1;
                    for (Adm a : adms) {
                        XWPFTableRow dataRow = table.getRow(number);
                        dataRow.getCell(0).setText(String.valueOf(number));
                        dataRow.getCell(1).setText(a.getMaterial_num() != null ? a.getMaterial_num() : "");
                        dataRow.getCell(2).setText(a.getReg_date() != null ? a.getReg_date() : "");
                        dataRow.getCell(3).setText(a.getFifteen() != null ? a.getFifteen() : "");
                        dataRow.getCell(4).setText(a.getSixteen() != null ? a.getSixteen() : "");
                        dataRow.getCell(5).setText(a.getSeventeen() != null ? a.getSeventeen() : "");
                        dataRow.getCell(6).setText(a.getUl_org_name() != null ? a.getUl_org_name() : "");
                        dataRow.getCell(7).setText(a.getUl_adress() != null ? a.getUl_adress() : "");
                        dataRow.getCell(8).setText(a.getVehicle_brand() != null ? a.getVehicle_brand() : "");
                        dataRow.getCell(9).setText(a.getState_auto_num() != null ? a.getState_auto_num() : "");
                        number++;
                    }
                    setMarginBetweenTables(doc);
                }
            } catch (Exception e) {
                System.out.println("Error adding Adm table: " + e.getMessage());
            }

            try {
                List<Criminals> criminals = result.getCriminals();
                if (criminals != null && !criminals.isEmpty()) {
                    creteTitle(doc, "Преступления");
                    XWPFTable table = doc.createTable();
                    makeTableByProperties(doc, table, "Преступления", Arrays.asList(
                            "№",
                            "Наименование суда",
                            "Дата судебного решения",
                            "Решение",
                            "Название преступления",
                            "Приговор",
                            "Дополнительная информация",
                            "Обращение",
                            "ЕРДР"
                    ));

                    int number = 1;
                    for (Criminals a : criminals) {
                        XWPFTableRow dataRow = table.getRow(number);
                        dataRow.getCell(0).setText(String.valueOf(number));
                        dataRow.getCell(1).setText(a.getCourt_name() != null ? a.getCourt_name() : "");
                        dataRow.getCell(2).setText(a.getCourt_dt() != null ? a.getCourt_dt() : "");
                        dataRow.getCell(3).setText(a.getDecision() != null ? a.getDecision() : "");
                        dataRow.getCell(4).setText(a.getCrime_name() != null ? a.getCrime_name() : "");
                        dataRow.getCell(5).setText(a.getSentence() != null ? a.getSentence() : "");
                        dataRow.getCell(6).setText(a.getAdd_info() != null ? a.getAdd_info() : "");
                        dataRow.getCell(7).setText(a.getTreatment() != null ? a.getTreatment() : "");
                        dataRow.getCell(8).setText(a.getErdr() != null ? a.getErdr() : "");
                        number++;
                    }
                    setMarginBetweenTables(doc);
                }
            } catch (Exception e) {
                System.out.println("Error adding Criminals table: " + e.getMessage());
            }

            try {
                List<BlockEsf> blockEsfs = result.getBlockEsfs();
                if (blockEsfs != null && !blockEsfs.isEmpty()) {
                    creteTitle(doc, "Блокировка ЕСФ");
                    XWPFTable table = doc.createTable();
                    makeTableByProperties(doc, table, "Блокировка ЕСФ", Arrays.asList(
                            "№",
                            "Дата начала",
                            "Дата окончания",
                            "Дата обновления"
                    ));

                    int number = 1;
                    for (BlockEsf a : blockEsfs) {
                        XWPFTableRow dataRow = table.getRow(number);
                        dataRow.getCell(0).setText(String.valueOf(number));
                        try {
                            dataRow.getCell(1).setText(a.getStart_dt().toString());
                        } catch (Exception e) {
                            dataRow.getCell(1).setText("Нет");
                        }
                        try {
                            dataRow.getCell(2).setText(a.getEnd_dt().toString());
                        } catch (Exception e) {
                            dataRow.getCell(2).setText("Нет");
                        }
                        try {
                            dataRow.getCell(3).setText(a.getUpdate_dt().toString());
                        } catch (Exception e) {
                            dataRow.getCell(3).setText("Нет");
                        }
                        number++;
                    }
                    setMarginBetweenTables(doc);
                }
            } catch (Exception e) {
                System.out.println("Error adding BlockEsf table: " + e.getMessage());
            }

            try {
                List<NdsEntity> ndsEntities = result.getNdsEntities();
                if (ndsEntities != null && !ndsEntities.isEmpty()) {
                    creteTitle(doc, "Объекты НДС");
                    XWPFTable table = doc.createTable();
                    makeTableByProperties(doc, table, "Объекты НДС", Arrays.asList(
                            "№",
                            "Дата начала",
                            "Дата окончания",
                            "Причина",
                            "Дата обновления"
                    ));

                    int number = 1;
                    for (NdsEntity a : ndsEntities) {
                        XWPFTableRow dataRow = table.getRow(number);
                        dataRow.getCell(0).setText(String.valueOf(number));
                        try {
                            dataRow.getCell(1).setText(a.getStartDt().toString());
                        } catch (Exception e) {
                            dataRow.getCell(1).setText("Нет");
                        }
                        try {
                            dataRow.getCell(2).setText(a.getEndDt().toString());
                        } catch (Exception e) {
                            dataRow.getCell(2).setText("Нет");
                        }
                        dataRow.getCell(3).setText(a.getReason() != null ? a.getReason() : "");
                        try {
                            dataRow.getCell(4).setText(a.getUpdateDt().toString());
                        } catch (Exception e) {
                            dataRow.getCell(4).setText("Нет");
                        }
                        number++;
                    }
                    setMarginBetweenTables(doc);
                }
            } catch (Exception e) {
                System.out.println("Error adding NdsEntity table: " + e.getMessage());
            }

//            try {
//                List<MvRnOld> mvRnOlds = result.getMvRnOlds();
//                if (mvRnOlds != null && !mvRnOlds.isEmpty()) {
//                    creteTitle(doc, "Прежний адрес прописки");
//                    XWPFTable table = doc.createTable();
//                    makeTableByProperties(doc, table, "Прежний адрес прописки", Arrays.asList(
//                            "№",
//                            "Назначение использования",
//                            "Статус недвижимости",
//                            "Адрес",
//                            "История адресов",
//                            "Тип собственности",
//                            "Вид собственности",
//                            "Статус характеристики недвижимости",
//                            "Дата регистрации в реестре",
//                            "Дата окончания регистрации",
//                            "Возникновение права в реестре",
//                            "Статус в реестре"
//                    ));
//
//                    int number = 1;
//                    for (MvRnOld a : mvRnOlds) {
//                        XWPFTableRow dataRow = table.getRow(number);
//                        dataRow.getCell(0).setText(String.valueOf(number));
//                        dataRow.getCell(1).setText(a.getType_of_property_rus() != null ? a.getProperty_type_rus() : "");
//                        dataRow.getCell(2).setText(a.getEstate_status_rus() != null ? a.getProperty_status() : "");
//                        dataRow.getCell(3).setText(a.getAddress() != null ? a.getAddress() : "");
//                        dataRow.getCell(4).setText(a.getAddress_history() != null ? a.getAddress_history() : "");
//                        dataRow.getCell(5).setText(a.getProperty_type() != null ? a.getProperty_type() : "");
//                        dataRow.getCell(6).setText(a.getOwnership_type() != null ? a.getOwnership_type() : "");
//                        dataRow.getCell(7).setText(a.getProperty_status() != null ? a.getProperty_status() : "");
//                        dataRow.getCell(8).setText(a.getReg_date() != null ? a.getReg_date().toString() : "");
//                        dataRow.getCell(9).setText(a.getEnd_reg_date() != null ? a.getEnd_reg_date().toString() : "");
//                        dataRow.getCell(10).setText(a.getRight_occur_date() != null ? a.getRight_occur_date().toString() : "");
//                        dataRow.getCell(11).setText(a.getReg_status() != null ? a.getReg_status() : "");
//                        number++;
//                    }
//                    setMarginBetweenTables(doc);
//                }
//            } catch (Exception e) {
//                System.out.println("Error adding MvRnOld table: " + e.getMessage());
//            }
            try {
                List<FpgTempEntity> fpgTempEntities = result.getFpgTempEntities();
                if (fpgTempEntities != null && !fpgTempEntities.isEmpty()) {
                    creteTitle(doc,"Временные объекты ФПГ");
                    XWPFTable table = doc.createTable();
                    makeTableByProperties(doc, table, "Временные объекты ФПГ", Arrays.asList(
                            "№",
                            "Бенефициар"
                    ));

                    int number = 1;
                    for (FpgTempEntity a : fpgTempEntities) {
                        XWPFTableRow dataRow = table.createRow();
                        dataRow.getCell(0).setText(String.valueOf(number));
                        dataRow.getCell(1).setText(a.getBeneficiary() != null ? a.getBeneficiary() : "");
                        number++;
                    }
                    setMarginBetweenTables(doc);
                }
            } catch (Exception e) {
                System.out.println("Error adding FpgTempEntity table: " + e.getMessage());
            }try {
                List<Pdl> pdls = result.getPdls();
                if (pdls != null && !pdls.isEmpty()) {
                    creteTitle(doc,"ПДЛ");
                    XWPFTable table = doc.createTable();
                    makeTableByProperties(doc, table, "ПДЛ", Arrays.asList(
                            "№",
                            "ИИН",
                            "Полное наименование организации",
                            "ФИО",
                            "Орган",
                            "Область",
                            "ФИО супруг(и)",
                            "Орган супруг(и)",
                            "Должность супруга",
                            "ИИН супруга"
                    ));
                    int number = 1;
                    for (Pdl a : pdls) {
                        XWPFTableRow dataRow = table.createRow();
                        dataRow.getCell(0).setText(String.valueOf(number));
                        dataRow.getCell(1).setText(a.getIin() != null ? a.getIin() : "");
                        dataRow.getCell(2).setText(a.getOrganization_fullname() != null ? a.getOrganization_fullname() : "");
                        dataRow.getCell(3).setText(a.getFio() != null ? a.getFio() : "");
                        dataRow.getCell(4).setText(a.getOrgan() != null ? a.getOrgan() : "");
                        dataRow.getCell(5).setText(a.getOblast() != null ? a.getOblast() : "");
                        dataRow.getCell(6).setText(a.getSpouse_fio() != null ? a.getSpouse_fio() : "");
                        dataRow.getCell(7).setText(a.getSpouse_organ() != null ? a.getSpouse_organ() : "");
                        dataRow.getCell(8).setText(a.getSpouse_position() != null ? a.getSpouse_position() : "");
                        dataRow.getCell(9).setText(a.getSpouse_iin() != null ? a.getSpouse_iin() : "");
                        number++;
                    }
                    setMarginBetweenTables(doc);
                }
            } catch (Exception e) {
                System.out.println("Error adding Pdl table: " + e.getMessage());
            }

            try {
                List<CommodityProducer> commodityProducers = result.getCommodityProducers();
                if (commodityProducers != null && !commodityProducers.isEmpty()) {
                    creteTitle(doc,"Производители товаров");
                    XWPFTable table = doc.createTable();
                    makeTableByProperties(doc, table, "Производители товаров", Arrays.asList(
                            "№",
                            "Наименование ССП",
                            "Количество",
                            "Производитель",
                            "Статус",
                            "Регион",
                            "СЗТП"
                    ));
                    int number = 1;
                    for (CommodityProducer a : commodityProducers) {
                        XWPFTableRow dataRow = table.createRow();
                        dataRow.getCell(0).setText(String.valueOf(number));
                        dataRow.getCell(1).setText(a.getSspName() != null ? a.getSspName() : "");
                        dataRow.getCell(2).setText(String.valueOf(a.getCount()));
                        dataRow.getCell(3).setText(a.getProducer() != null ? a.getProducer() : "");
                        dataRow.getCell(4).setText(a.getStatus() != null ? a.getStatus() : "");
                        dataRow.getCell(5).setText(a.getRegion() != null ? a.getRegion() : "");
                        dataRow.getCell(6).setText(a.getSztp() != null ? a.getSztp() : "");
                    number++;
                    }
                    setMarginBetweenTables(doc);
                }
            } catch (Exception e) {
                System.out.println("Error adding CommodityProducer table: " + e.getMessage());
            }

            try {
                RegAddressUlEntity regAddressUlEntity = result.getRegAddressUlEntities();
                if (regAddressUlEntity != null) {
                    creteTitle(doc,"Адрес");
                    XWPFTable table = doc.createTable();
                    makeTableByProperties(doc, table, "Адрес", Arrays.asList(
                            "Дата регистрации",
                            "Название организации (на русском)",
                            "Регион регистрации (на русском)",
                            "Район регистрации (на русском)",
                            "Сельский район регистрации (на русском)",
                            "Населенный пункт регистрации (на русском)",
                            "Улица регистрации (на русском)",
                            "Номер здания",
                            "Номер блока",
                            "Номер корпуса здания",
                            "Офис (номер)",
                            "Название ОКЭД (на русском)",
                            "Статус ЮЛ",
                            "Активный"
                    ));

                    XWPFTableRow dataRow = table.createRow();
                    try {
                        dataRow.getCell(0).setText(regAddressUlEntity.getRegDate().toString());
                    } catch (Exception e) {
                        dataRow.getCell(0).setText("");
                    }
                    dataRow.getCell(1).setText(regAddressUlEntity.getOrgNameRu() != null ? regAddressUlEntity.getOrgNameRu() : "");
                    dataRow.getCell(2).setText(regAddressUlEntity.getRegAddrRegionRu() != null ? regAddressUlEntity.getRegAddrRegionRu() : "");
                    dataRow.getCell(3).setText(regAddressUlEntity.getRegAddrDistrictRu() != null ? regAddressUlEntity.getRegAddrDistrictRu() : "");
                    dataRow.getCell(4).setText(regAddressUlEntity.getRegAddrRuralDistrictRu() != null ? regAddressUlEntity.getRegAddrRuralDistrictRu() : "");
                    dataRow.getCell(5).setText(regAddressUlEntity.getRegAddrLocalityRu() != null ? regAddressUlEntity.getRegAddrLocalityRu() : "");
                    dataRow.getCell(6).setText(regAddressUlEntity.getRegAddrStreetRu() != null ? regAddressUlEntity.getRegAddrStreetRu() : "");
                    dataRow.getCell(7).setText(regAddressUlEntity.getRegAddrBuildingNum() != null ? regAddressUlEntity.getRegAddrBuildingNum() : "");
                    dataRow.getCell(8).setText(regAddressUlEntity.getRegAddrBlockNum() != null ? regAddressUlEntity.getRegAddrBlockNum() : "");
                    dataRow.getCell(9).setText(regAddressUlEntity.getRegAddrBuildingBodyNum() != null ? regAddressUlEntity.getRegAddrBuildingBodyNum() : "");
                    dataRow.getCell(10).setText(regAddressUlEntity.getRegAddrOffice() != null ? regAddressUlEntity.getRegAddrOffice() : "");
                    dataRow.getCell(11).setText(regAddressUlEntity.getOkedNameRu() != null ? regAddressUlEntity.getOkedNameRu() : "");
                    dataRow.getCell(12).setText(regAddressUlEntity.getUl_status() != null ? regAddressUlEntity.getUl_status() : "");
                    dataRow.getCell(13).setText(regAddressUlEntity.getActive() ? "Активен" : "Неактивен");

                    setMarginBetweenTables(doc);
                }
            } catch (Exception e) {
                System.out.println("Error adding RegAddressUlEntity table: " + e.getMessage());
            }

            try {
                List<SvedenyaObUchastnikovUlEntity> svedenyaObUchastnikovUlEntities = result.getSvedenyaObUchastnikovUlEntities();
                if (svedenyaObUchastnikovUlEntities != null && !svedenyaObUchastnikovUlEntities.isEmpty()) {
                    creteTitle(doc,"Сведения об участниках ЮЛ");
                    XWPFTable table = doc.createTable();
                    makeTableByProperties(doc, table, "Сведения об участниках ЮЛ", Arrays.asList(
                            "№",
                            "ФИО или наименование ЮЛ",
                            "Идентификатор",
                            "Дата регистрации",
                            "Риск"
                    ));
                    int number = 1;
                    for (SvedenyaObUchastnikovUlEntity a : svedenyaObUchastnikovUlEntities) {
                        XWPFTableRow dataRow = table.createRow();
                        dataRow.getCell(0).setText(String.valueOf(number));
                        dataRow.getCell(1).setText(a.getFIOorUlName() != null ? a.getFIOorUlName() : "");
                        dataRow.getCell(2).setText(a.getIdentificator() != null ? a.getIdentificator() : "");
                        dataRow.getCell(3).setText(a.getReg_date() != null ? a.getReg_date() : "");
                        dataRow.getCell(4).setText(a.getRisk() != null ? a.getRisk() : "");
                        number++;
                    }
                    setMarginBetweenTables(doc);
                }
            } catch (Exception e) {
                System.out.println("Error adding SvedenyaObUchastnikovUlEntity table: " + e.getMessage());
            }
            doc.write(baos);
            baos.close();
        }
    }
    private void setTableKeepTogether(XWPFTable table) {
        table.getCTTbl().getTblPr().addNewTblLayout().setType(STTblLayoutType.FIXED);
    }

    private void setRowKeepWithNext(XWPFTableRow row) {
        for (XWPFTableCell cell : row.getTableCells()) {
            cell.getCTTc().getPList().forEach(ctP -> {
                CTPPr ppr = ctP.isSetPPr() ? ctP.getPPr() : ctP.addNewPPr();
                ppr.addNewKeepNext().setVal(STOnOff1.ON);
                ppr.addNewKeepLines().setVal(STOnOff1.ON);
            });
        }
    }
}