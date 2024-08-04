package kz.dossier.tools;

import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kz.dossier.dto.GosZakupDTO;
import kz.dossier.dto.GosZakupForAll;
import kz.dossier.dto.PensionGroupDTO;
import kz.dossier.dto.PensionListDTO;
import kz.dossier.modelsDossier.AccountantListEntity;
import kz.dossier.modelsDossier.ChangeFio;
import kz.dossier.modelsDossier.CommodityProducer;
import kz.dossier.modelsDossier.Equipment;
import kz.dossier.modelsDossier.FlContacts;
import kz.dossier.modelsDossier.FlRelativiesDTO;
import kz.dossier.modelsDossier.FpgTempEntity;
import kz.dossier.modelsDossier.IndividualEntrepreneur;
import kz.dossier.modelsDossier.IpgoEmailEntity;
import kz.dossier.modelsDossier.KX;
import kz.dossier.modelsDossier.MillitaryAccount;
import kz.dossier.modelsDossier.Msh;
import kz.dossier.modelsDossier.MvAutoFl;
import kz.dossier.modelsDossier.MvFl;
import kz.dossier.modelsDossier.MvFlAddress;
import kz.dossier.modelsDossier.MvIinDoc;
import kz.dossier.modelsDossier.MvRnOld;
import kz.dossier.modelsDossier.MvUl;
import kz.dossier.modelsDossier.MvUlFounderFl;
import kz.dossier.modelsDossier.MvUlLeaderEntity;
import kz.dossier.modelsDossier.NodesFL;
import kz.dossier.modelsDossier.NodesUL;
import kz.dossier.modelsDossier.PhotoDb;
import kz.dossier.modelsDossier.RegAddressFl;
import kz.dossier.modelsDossier.RegAddressUlEntity;
import kz.dossier.modelsDossier.School;
import kz.dossier.modelsDossier.SvedenyaObUchastnikovUlEntity;
import kz.dossier.modelsDossier.Universities;
import kz.dossier.modelsRisk.Adm;
import kz.dossier.modelsRisk.Bankrot;
import kz.dossier.modelsRisk.BlockEsf;
import kz.dossier.modelsRisk.ConvictsJustified;
import kz.dossier.modelsRisk.ConvictsTerminatedByRehab;
import kz.dossier.modelsRisk.Criminals;
import kz.dossier.modelsRisk.Dormant;
import kz.dossier.modelsRisk.NdsEntity;
import kz.dossier.modelsRisk.Omn;
import kz.dossier.modelsRisk.Pdl;
import kz.dossier.repositoryDossier.AdmRepo;
import kz.dossier.repositoryDossier.ChangeFioRepo;
import kz.dossier.repositoryDossier.FlContactsRepo;
import kz.dossier.repositoryDossier.FlPensionContrRepo;
import kz.dossier.repositoryDossier.FlPensionMiniRepo;
import kz.dossier.repositoryDossier.IndividualEntrepreneurRepo;
import kz.dossier.repositoryDossier.KxRepo;
import kz.dossier.repositoryDossier.MvUlLeaderEntityRepo;
import kz.dossier.repositoryDossier.MvUlRepo;
import kz.dossier.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class PdfGenerator {
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
    private PdfPTable tryAddCell(PdfPTable table, String add, String string) {
        if (string != null) {
            table.addCell(add + string);
        } else {
            table.addCell(add);
        }
        return table;
    }
    public BaseFont getBaseFont() throws IOException, DocumentException {
        try {
            ClassPathResource fontResource = new ClassPathResource("./fonts/fontstimes.ttf");
            try (InputStream fontStream = fontResource.getInputStream()) {
                return BaseFont.createFont("./fonts/fontstimes.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, BaseFont.CACHED, fontStream.readAllBytes(), null);
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return BaseFont.createFont();
    }

    public Document generate(NodesFL result, ByteArrayOutputStream response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response);
        document.open();
        getBaseFont();
        PdfPTable table = new PdfPTable(10);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        table.setSpacingBefore(5);
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(CMYKColor.WHITE);
        cell.setPadding(5);
        Font font = new Font(getBaseFont(), 11);
        font.setColor(CMYKColor.WHITE);
        PdfPCell heading = new PdfPCell();
        heading.setBackgroundColor(CMYKColor.GRAY);
        heading.setPadding(4);
        heading.setColspan(10);
        heading.setHorizontalAlignment(Element.ALIGN_CENTER);
        try {
            heading.setPhrase(new Phrase("Сведения о физическом лице", font));
            table.addCell(heading);
            font.setColor(CMYKColor.BLACK);
            cell.setPhrase(new Phrase("Фото", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("ИИН", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("ФИО", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Резидент", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Гражданство", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Нация", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Дата рождения", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Место рождения", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Статус", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Дата смерти", font));
            table.addCell(cell);
            table.addCell(Image.getInstance(result.getPhotoDbf().get(0).getPhoto()));

            cell.setPhrase(new Phrase(result.getMvFls().get(0).getIin(), font));
            table.addCell(cell);
            cell.setPhrase(new Phrase(result.getMvFls().get(0).getLast_name() + "\n" + result.getMvFls().get(0).getFirst_name() + "\n" + result.getMvFls().get(0).getPatronymic(), font));
            table.addCell(cell);

            if (result.getMvFls().get(0).is_resident()) {
                cell.setPhrase(new Phrase("ДА", font));
            } else {
                cell.setPhrase(new Phrase("НЕТ", font));
            }
            table.addCell(cell);
            cell.setPhrase(new Phrase(result.getMvFls().get(0).getNationality_ru_name(), font));
            table.addCell(cell);
            cell.setPhrase(new Phrase(result.getMvFls().get(0).getCitizenship_ru_name(), font));
            table.addCell(cell);
            cell.setPhrase(new Phrase(result.getMvFls().get(0).getBirth_date(), font));
            table.addCell(cell);
            cell.setPhrase(new Phrase(result.getMvFls().get(0).getDistrict() + ", " + result.getMvFls().get(0).getRegion(), font));
            table.addCell(cell);
            cell.setPhrase(new Phrase(result.getMvFls().get(0).getLife_status_ru_name(), font));
            table.addCell(cell);
            if (result.getMvFls().get(0).getDeath_date() == null || result.getMvFls().get(0).getDeath_date().isEmpty()) {
                cell.setPhrase(new Phrase("Отсутсвует", font));
            } else {
                cell.setPhrase(new Phrase(result.getMvFls().get(0).getDeath_date(), font));
            }
            table.addCell(cell);
            document.add(table);
        }catch (Exception e){
            System.out.println(e);
        }
        // ОСНОВНАЯ ИНФА ОБ ФЛ
        // АДДРЕСА ФЛ

        // ДОКУМЕНТЫ ФЛ
        List<MvIinDoc> docs = result.getMvIinDocs();
        if (docs != null && docs.size() != 0) {
            PdfPTable docTable = new PdfPTable(6);
            docTable.setHorizontalAlignment(Element.ALIGN_CENTER);
            docTable.setWidthPercentage(100f);
            docTable.setWidths(new float[] {1, 1, 1, 1, 1, 1});
            docTable.setSpacingBefore(5);
            heading.setColspan(6);
            heading.setPhrase(new Phrase("Наименование коллапса: \"Документы\" Количество найденных инф: " + docs.size(), font));
            docTable.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Типа Документа", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Орган выдачи", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата выдачи", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Срок до", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Номер документа", font));
            docTable.addCell(cell);
            int number = 1;
            for (MvIinDoc r : docs) {
                docTable.addCell(new Phrase(String.valueOf(number), font));
                docTable.addCell(new Phrase(r.getDoc_type_ru_name(), font));
                docTable.addCell(new Phrase(r.getIssue_organization_ru_name(), font));
                docTable.addCell(new Phrase(r.getIssue_date().toString(), font));
                docTable.addCell(new Phrase(r.getExpiry_date().toString(), font));
                docTable.addCell(new Phrase(r.getDoc_number(), font));
                number++;
            }
            document.add(docTable);
        }
        List<MvFlAddress> addressFls = result.getMvFlAddresses();
        if (addressFls != null && !addressFls.isEmpty()) {

            PdfPTable addresses = new PdfPTable(5);
            addresses.setHorizontalAlignment(Element.ALIGN_CENTER);
            addresses.setWidthPercentage(100f);
            addresses.setWidths(new float[] {1, 1, 1, 1, 1});
            addresses.setSpacingBefore(5);
            heading.setColspan(5);
            heading.setPhrase(new Phrase("Наименование коллапса: \"Адрес прописки\" Количество найденных инф: " + addressFls.size(), font));
            addresses.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            addresses.addCell(cell);
            cell.setPhrase(new Phrase("Город", font));
            addresses.addCell(cell);
            cell.setPhrase(new Phrase("Улица, дом, квартира", font));
            addresses.addCell(cell);
            cell.setPhrase(new Phrase("Район", font));
            addresses.addCell(cell);
            cell.setPhrase(new Phrase("Дата регистрации", font));
            addresses.addCell(cell);
            int number = 1;
            for (MvFlAddress ar : addressFls) {
                addresses.addCell(new Phrase(String.valueOf(number), font));
                addresses.addCell(new Phrase(ar.getDistrict(), font));
                addresses.addCell(new Phrase(ar.getRegion(), font));
                addresses.addCell(new Phrase(ar.getStreet(), font));
                addresses.addCell(new Phrase(ar.getBuilding(), font));
                number++;
            }
            document.add(addresses);
        }
        List<ChangeFio> changeFios = changeFioRepo.getByIin(result.getMvFls().get(0).getIin());
        if (changeFios != null && !changeFios.isEmpty()) {
            PdfPTable addresses = new PdfPTable(4);
            addresses.setWidthPercentage(100f);
            addresses.setWidths(new float[] {1, 1, 1, 1});
            addresses.setSpacingBefore(5);
            heading.setColspan(4);
            heading.setPhrase(new Phrase("Наименование коллапса: \"Смена ФИО\" Количество найденных инф: " + changeFios.size(), font));
            addresses.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            addresses.addCell(cell);
            cell.setPhrase(new Phrase("Предыдущие данные", font));
            addresses.addCell(cell);
            cell.setPhrase(new Phrase("Причина смены", font));
            addresses.addCell(cell);
            cell.setPhrase(new Phrase("Дата смены ФИО", font));
            addresses.addCell(cell);
            int number = 1;
            for (ChangeFio ar : changeFios) {
                addresses.addCell(new Phrase(String.valueOf(number), font));
                addresses.addCell(new Phrase(ar.getSurname_before() !=null ? ar.getSurname_before() : " "  + " " +  ar.getName_before() !=null ? ar.getName_before() : " " + " " + ar.getSecondname_before() !=null ? ar.getSecondname_before() : " ", font));
                addresses.addCell(new Phrase(ar.getRemarks(), font));
                addresses.addCell(new Phrase(ar.getTo_date(), font));
                number++;
            }
            document.add(addresses);
        }
        List<Adm> adms = admRepo.getUsersByLike(result.getMvFls().get(0).getIin());
        if (adms != null && adms.size() != 0) {
            PdfPTable docTable = new PdfPTable(10);
            docTable.setWidthPercentage(100f);
            docTable.setWidths(new float[] {1, 1, 1, 1, 1, 1,1,1,1,1});
            docTable.setSpacingBefore(5);
            heading.setColspan(10);
            heading.setPhrase(new Phrase("Наименование коллапса: \"Административные штрафы\" Количество найденных инф: " + adms.size(), font));
            docTable.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Орган выявивший правонарушение", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата заведения", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Номер протокола", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Место работы", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Квалификация", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Принудительное исполенеия", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("На срок до", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Размер наложенного штрафа", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Основания прекращения", font));
            docTable.addCell(cell);
            int number = 1;
            for (Adm r : adms) {
                docTable.addCell(new Phrase(String.valueOf(number), font));
                docTable.addCell(new Phrase(r.getAuthority_detected(), font));
                docTable.addCell(new Phrase(r.getReg_date(), font));
                docTable.addCell(new Phrase(r.getProtocol_num().toString(), font));
                docTable.addCell(new Phrase(r.getWork_place().toString(), font));
                docTable.addCell(new Phrase(r.getQualification_name(), font));
                docTable.addCell(new Phrase(r.getEnforcement(), font));
                docTable.addCell(new Phrase(r.getEnd_date(), font));
                docTable.addCell(new Phrase(r.getFine_amount(), font));
                docTable.addCell(new Phrase(r.getTeminate_reason(), font));
                number++;
            }
            document.add(docTable);
        }
        List<FlContacts> flContacts = flContactsRepo.findAllByIin(result.getMvIinDocs().get(0).getIin());
        if (flContacts != null && !flContacts.isEmpty()) {
            PdfPTable docTable = new PdfPTable(7);
            docTable.setWidthPercentage(100f);
            docTable.setWidths(new float[] {1, 1, 1, 1, 1,1,1});
            docTable.setSpacingBefore(5);
            heading.setColspan(7);
            heading.setPhrase(new Phrase("Наименование коллапса: \"Контактные данные\" Количество найденных инф: " + flContacts.size(), font));
            docTable.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Номер телефона", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Email", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("ФИО руководителя организации", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("ФИО/Наименование организации владельца номера", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Источник", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Nickname", font));
            docTable.addCell(cell);
            int number = 1;
            for (FlContacts flContacts1 : flContacts) {
                docTable.addCell(new Phrase(String.valueOf(number), font));
                docTable.addCell(new Phrase(flContacts1.getPhone() != null ? flContacts1.getPhone() : "", font));
                docTable.addCell(new Phrase(flContacts1.getEmail() != null ? flContacts1.getEmail() : "", font));
                docTable.addCell(new Phrase(flContacts1.getLeader_fio() != null ? flContacts1.getLeader_fio().toString() : "", font));
                docTable.addCell(new Phrase(flContacts1.getNickname() != null ? flContacts1.getNickname().toString() : "", font));
                docTable.addCell(new Phrase(flContacts1.getSource() != null ? flContacts1.getSource() : "", font));
                docTable.addCell(new Phrase(flContacts1.getNickname() != null ? flContacts1.getNickname() : "", font));
                number++;
            }
            document.add(docTable);
        }
        List<IndividualEntrepreneur> individualEntrepreneurs = individualEntrepreneurRepo.getByIin(result.getMvFls().get(0).getIin());
        List<KX> kxes = kxRepo.getKxIin(result.getMvFls().get(0).getIin());
        if (individualEntrepreneurs != null && !individualEntrepreneurs.isEmpty()) {
            PdfPTable docTable = new PdfPTable(3);
            docTable.setWidthPercentage(100f);
            docTable.setWidths(new float[] {1, 1,1});
            docTable.setSpacingBefore(5);
            heading.setColspan(3);
            heading.setPhrase(new Phrase("Наименование коллапса: \"ИП/КХ\" Количество найденных инф:  " + (individualEntrepreneurs.size()+ kxes.size()), font));
            docTable.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Наименование", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("ИП/КХ", font));
            docTable.addCell(cell);
            int number = 1;
            for (IndividualEntrepreneur individualEntrepreneur : individualEntrepreneurs) {
                docTable.addCell(new Phrase(String.valueOf(number), font));
                docTable.addCell(new Phrase(individualEntrepreneur.getName_rus() != null ? individualEntrepreneur.getName_rus() : "", font));
                docTable.addCell(new Phrase("ИП", font));
                number++;
            }
            if(kxes != null && !kxes.isEmpty()){
                for(KX kx: kxes) {
                    docTable.addCell(new Phrase(String.valueOf(number), font));
                    docTable.addCell(new Phrase(kx.getName_rus() != null ? kx.getName_rus() : "", font));
                    docTable.addCell(new Phrase("КХ", font));
                    number++;
                }
            }
            document.add(docTable);
        }
        GosZakupForAll gosZakupForAll = myService.gosZakupByBin(result.getMvFls().get(0).getIin());
        if ((gosZakupForAll.getWhenCustomer()!= null && !gosZakupForAll.getWhenCustomer().isEmpty()) || (gosZakupForAll.getWhenSupplier()!= null && !gosZakupForAll.getWhenSupplier().isEmpty())) {
            PdfPTable schoolTable = new PdfPTable(5);
            schoolTable.setWidthPercentage(100f);
            schoolTable.setWidths(new float[] {1, 1, 1, 1, 1});
            schoolTable.setSpacingBefore(5);
            heading.setColspan(5);
            heading.setPhrase(new Phrase("Наименование коллапса: \"Государственные закупки- По поставщикам\" Количество найденных инф: " + (gosZakupForAll.getWhenSupplier().size() + gosZakupForAll.getWhenCustomer().size()), font));
            schoolTable.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Год", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Общая сумма", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Количество договоров", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Поставщики", font));
            schoolTable.addCell(cell);
            int number = 1;
            for (GosZakupDTO r : gosZakupForAll.getWhenSupplier()) {
                schoolTable.addCell(new Phrase(String.valueOf(number), font));
                schoolTable.addCell(new Phrase(r.getPeriod(), font));
                schoolTable.addCell(new Phrase(r.getSum(), font));
                schoolTable.addCell(new Phrase(String.valueOf(r.getNumber()), font));
                schoolTable.addCell(new Phrase(r.getOpposite().toString(), font));
                number++;
            }
            for (GosZakupDTO r : gosZakupForAll.getWhenCustomer()) {
                schoolTable.addCell(new Phrase(String.valueOf(number), font));
                schoolTable.addCell(new Phrase(r.getPeriod(), font));
                schoolTable.addCell(new Phrase(r.getSum(), font));
                schoolTable.addCell(new Phrase(String.valueOf(r.getNumber()), font));
                schoolTable.addCell(new Phrase(r.getOpposite().toString(), font));
                number++;
            }
            document.add(schoolTable);
        }


        ulExportPDFService.addSamrukGosZakupTable(result.getMvFls().get(0).getIin(), document, font, true);
        ulExportPDFService.addSamrukGosZakupTable(result.getMvFls().get(0).getIin(), document, font, false);


        List<String> companyBins = flPensionContrRepo.getUsersByLikeCompany(result.getMvFls().get(0).getIin());

        List<PensionListDTO> pensions = new ArrayList<>();
        List<PensionGroupDTO> results = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(0);
        for (String bin : companyBins) {
            List<Map<String, Object>> fl_pension_contrss = new ArrayList<>();
            fl_pension_contrss = flPensionContrRepo.getAllByCompanies(result.getMvFls().get(0).getIin(),bin);
            PensionGroupDTO obj = new PensionGroupDTO();
            List<PensionListDTO> group = new ArrayList<>();
            String name = "";
            if (fl_pension_contrss.get(0).get("P_NAME") != null) {
                name = (String)fl_pension_contrss.get(0).get("P_NAME") + ", ";
            }
            if (bin != null) {
                name = name + bin + ", период ";
            }
            List<String> distinctPayDates = fl_pension_contrss.stream()
                    .map(pension -> pension.get("pay_date").toString())
                    .distinct()
                    .collect(Collectors.toList());

            double knp010sum = 0.0;
            double knp012sum = 0.0;

            for (String year : distinctPayDates) {
                if (year != null) {
                    name = name + year.replace(".0", "") + ", ";
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

                    knp010sum = knp010sum + knp010;

                } catch (Exception e) {

                }
                try {
                    double knp012 = fl_pension_contrss.stream()
                            .filter(pension -> pension.get("pay_date").toString().equals(year) && pension.get("KNP").toString().equals("012"))
                            .mapToDouble(pension -> Double.parseDouble(pension.get("AMOUNT").toString()))
                            .sum();

                    pensionListEntity.setSum012(knp012);
                    knp012sum = knp012sum + knp012;
                } catch (Exception e) {

                }pensions.add(pensionListEntity);
                group.add(pensionListEntity);
            }
            name = name + "общая сумма КНП(010): " + df.format(knp010sum) + ", общая сумма КНП(012): " + df.format(knp012sum);
            obj.setName(name);
            obj.setList(group);
            results.add(obj);
        }

        if (results != null && !results.isEmpty()) {
            PdfPTable docTable = new PdfPTable(5);
            docTable.setWidthPercentage(100f);
            docTable.setWidths(new float[] {1, 1, 1, 1,1});
            docTable.setSpacingBefore(5);
            heading.setColspan(5);
            heading.setPhrase(new Phrase("Наименование коллапса: \"Пенсионные отчисления\" Количество найденных инф: " + results.size(), font));
            docTable.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("БИН, Наименование ЮЛ", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Период", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Общая сумма(010)", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Общая сумма(012)", font));
            docTable.addCell(cell);
            int number = 1;
            for (PensionGroupDTO pensionListDTO : results) {
                docTable.addCell(new Phrase(String.valueOf(number), font));
                docTable.addCell(new Phrase(getSubstring(pensionListDTO.getName(),0, ", п"), font));
                docTable.addCell(new Phrase(getSubstringByTwoWords(pensionListDTO.getName(),", период ", ", общая"), font));
                docTable.addCell(new Phrase(getSubstringByTwoWords(pensionListDTO.getName(),"общая сумма КНП(010): ", ", общая сумма КНП(012)"), font));
                docTable.addCell(new Phrase(getSubstringByLastWord(pensionListDTO.getName(),"общая сумма КНП(012): ", pensionListDTO.getName()), font));
                number++;
            }
            document.add(docTable);
        }

        List<MvUlLeaderEntity> mvUlLeaderEntities  = mvUlLeaderEntityRepo.getUsersByLikeIin(result.getMvFls().get(0).getIin());
        try {
            for(MvUlLeaderEntity mvUlLeaderEntity: mvUlLeaderEntities){
                mvUlLeaderEntity.setBinName(mvUlRepo.getNameByBin(mvUlLeaderEntity.getBinOrg()));
            }
        } catch (Exception e) {
        }
        List<MvUlFounderFl> mvUlFounderFls  = result.getMvUlFounderFls();
        try {
            for(MvUlFounderFl mvUlFounderFl: mvUlFounderFls){
                mvUlFounderFl.setBinName(mvUlRepo.getNameByBin(mvUlFounderFl.getBin_org()));
            }
        } catch (Exception e) {
        }
        if ((mvUlLeaderEntities != null && !mvUlLeaderEntities.isEmpty()) || (mvUlFounderFls != null && !mvUlFounderFls.isEmpty())) {
            PdfPTable docTable = new PdfPTable(5);
            docTable.setWidthPercentage(100f);
            docTable.setWidths(new float[] {1, 1, 1, 1, 1});
            docTable.setSpacingBefore(5);
            heading.setColspan(5);
            heading.setPhrase(new Phrase("Наименование коллапса: \"Сведения об участниках ЮЛ\" Количество найденных инф: " + (mvUlLeaderEntities.size() + mvUlFounderFls.size()), font));
            docTable.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Идентификатор ЮЛ", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата регистрации", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("БИН", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Наименование ЮЛ", font));
            docTable.addCell(cell);
            int number = 1;
            for (MvUlLeaderEntity r : mvUlLeaderEntities) {
                docTable.addCell(new Phrase(String.valueOf(number), font));
                docTable.addCell(new Phrase("Директор", font));
                docTable.addCell(new Phrase(r.getRegDate(), font));
                docTable.addCell(new Phrase(r.getBinOrg(), font));
                docTable.addCell(new Phrase(r.getBinName(), font));
                number++;
            }
            for (MvUlFounderFl r : mvUlFounderFls) {
                docTable.addCell(new Phrase(String.valueOf(number), font));
                docTable.addCell(new Phrase("Учеридитель", font));
                docTable.addCell(new Phrase(r.getReg_date(), font));
                docTable.addCell(new Phrase(r.getBin_org(), font));
                docTable.addCell(new Phrase(r.getBinName(), font));
                number++;
            }
            document.add(docTable);
        }
        List<MvRnOld> mvRnOlds = result.getMvRnOlds();
        if (mvRnOlds != null && mvRnOlds.size() != 0) {
            PdfPTable schoolTable = new PdfPTable(13);
            schoolTable.setWidthPercentage(100f);
            schoolTable.setWidths(new float[] {1, 1, 1, 1, 1,1, 1, 1, 1, 1,1, 1, 1});
            schoolTable.setSpacingBefore(5);
            heading.setColspan(13);
            heading.setPhrase(new Phrase("Наименование коллапса: \"Сведения по реестру недвижимости\" Количество найденных инф: " + mvRnOlds.size(), font));
            schoolTable.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Кадастровый номер", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Адрес", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Правообладатель", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Этажность", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Количество составляющих", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Площадь общая", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Вид документа", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Номер документа", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата документа", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Сумма сделки", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Жилая площадь", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Статус", font));
            schoolTable.addCell(cell);
            int number = 1;
            for (MvRnOld r : mvRnOlds) {
                schoolTable.addCell(new Phrase(String.valueOf(number), font));
                schoolTable.addCell(new Phrase(r.getCadastral_number(), font));
                schoolTable.addCell(new Phrase(r.getAddress_rus(), font));
                schoolTable.addCell(new Phrase(r.getOwner_iin_bin(), font));
                schoolTable.addCell(new Phrase(r.getFloor(), font));
                schoolTable.addCell(new Phrase(r.getType_of_property_rus(), font));
                schoolTable.addCell(new Phrase(r.getArea_total(), font));
                schoolTable.addCell(new Phrase(r.getType_of_property_rus(), font));
                schoolTable.addCell(new Phrase(r.getRegister_emergence_rights_rus(), font));
                schoolTable.addCell(new Phrase(r.getRegister_reg_date(), font));
                schoolTable.addCell(new Phrase(r.getRegister_transaction_amount(), font));
                schoolTable.addCell(new Phrase(r.getArea_useful(), font));
                schoolTable.addCell(new Phrase(r.getRegister_status_rus(), font));
                number++;
            }

            document.add(schoolTable);
        }
        //ШКОЛЫ ФЛ
        List<School> schools = result.getSchools();
        if (schools != null && schools.size() != 0) {
            PdfPTable schoolTable = new PdfPTable(5);
            schoolTable.setWidthPercentage(100f);
            schoolTable.setWidths(new float[] {1, 1, 1, 1, 1});
            schoolTable.setSpacingBefore(5);
            heading.setColspan(5);
            heading.setPhrase(new Phrase("Наименование коллапса: \"Сведение по образование: Среднее образование (Школа)\" Количество найденных инф: " + schools.size(), font));
            schoolTable.addCell(heading);
            cell.setPhrase(new Phrase("БИН", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Название школы", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Класс", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата поступления", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата окончания", font));
            schoolTable.addCell(cell);
            for (School r : schools) {
                schoolTable.addCell(new Phrase(r.getSchool_code(), font));
                schoolTable.addCell(new Phrase(r.getSchool_name(), font));
                schoolTable.addCell(new Phrase(r.getGrade(), font));
                schoolTable.addCell(new Phrase(r.getStart_date().toString(), font));
                schoolTable.addCell(new Phrase(r.getEnd_date().toString(), font));
            }

            document.add(schoolTable);
        }
        //УНИВЕРСИТЕТЫ ФЛ
        List<Universities> universities = result.getUniversities();
        if (universities != null && universities.size()!=0 ) {
            PdfPTable uniTable = new PdfPTable(7);
            uniTable.setWidthPercentage(100f);
            uniTable.setWidths(new float[] {1, 1, 1, 1, 1, 1, 1});
            uniTable.setSpacingBefore(5);
            heading.setColspan(7);
            heading.setPhrase(new Phrase("Наименование коллапса: \"Сведение по образование: Высшее образование (Университет)\" Количество найденных инф: " + universities.size(), font));
            uniTable.addCell(heading);
            cell.setPhrase(new Phrase("БИН", font));
            uniTable.addCell(cell);
            cell.setPhrase(new Phrase("Название вуза", font));
            uniTable.addCell(cell);
            cell.setPhrase(new Phrase("Специализация", font));
            uniTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата поступления", font));
            uniTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата окончания", font));
            uniTable.addCell(cell);
            cell.setPhrase(new Phrase("Длительность обучения", font));
            uniTable.addCell(cell);
            cell.setPhrase(new Phrase("Курс", font));
            uniTable.addCell(cell);
            for (Universities r : universities) {
                uniTable.addCell(new Phrase(r.getStudy_code(), font));
                uniTable.addCell(new Phrase(r.getStudy_name(), font));
                uniTable.addCell(new Phrase(r.getSpec_name(), font));

                if (r.getStart_date() != null) {
                    uniTable.addCell(new Phrase(r.getStart_date().toString(), font));
                } else {
                    uniTable.addCell(new Phrase("", font));
                }

                if (r.getEnd_date() != null) {
                    uniTable.addCell(new Phrase(r.getEnd_date().toString(), font));
                } else {
                    uniTable.addCell(new Phrase("", font));
                }

                uniTable.addCell(new Phrase(r.getDuration(), font));
                uniTable.addCell(new Phrase(r.getCourse(), font));
            }

            document.add(uniTable);
        }
        //ТРАНСПОРТ ФЛ
        List<MvAutoFl> autos = result.getMvAutoFls();
        if (autos != null && autos.size()!=0) {
            PdfPTable autoTable = new PdfPTable(10);
            autoTable.setWidthPercentage(100f);
            autoTable.setWidths(new float[] {0.4f, 1, 1, 1, 1, 1, 1, 1, 1, 1});
            autoTable.setSpacingBefore(5);
            heading.setColspan(10);
            heading.setPhrase(new Phrase("Наименование коллапса: \"Транспорт\" Количество найденных инф: " + autos.size(), font));
            autoTable.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            autoTable.addCell(cell);
            cell.setPhrase(new Phrase("Статус", font));
            autoTable.addCell(cell);
            cell.setPhrase(new Phrase("Регистрационный номер", font));
            autoTable.addCell(cell);
            cell.setPhrase(new Phrase("Марка модель", font));
            autoTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата выдачи свидетельства", font));
            autoTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата снятия", font));
            autoTable.addCell(cell);
            cell.setPhrase(new Phrase("Год выпуска", font));
            autoTable.addCell(cell);
            cell.setPhrase(new Phrase("Категория", font));
            autoTable.addCell(cell);
            cell.setPhrase(new Phrase("VIN/Кузов/Шосси", font));
            autoTable.addCell(cell);
            cell.setPhrase(new Phrase("Серия", font));
            autoTable.addCell(cell);
            int number = 1;
            for (MvAutoFl r : autos) {
                if (r != null) {
                    autoTable.addCell(new Phrase(number + "", font));

                    try {
                        if (r.isIs_registered()) {
                            autoTable.addCell(new Phrase("Текущий", font));
                        } else {
                            autoTable.addCell(new Phrase("Исторический", font));
                        }
                    } catch (Exception e) {
                        autoTable.addCell(new Phrase("", font));
                    }

                    try {
                        autoTable.addCell(new Phrase(r.getReg_number(), font));
                    } catch (Exception e) {
                        autoTable.addCell(new Phrase("", font));
                    }

                    try {
                        autoTable.addCell(new Phrase(r.getBrand_model(), font));
                    } catch (Exception e) {
                        autoTable.addCell(new Phrase("", font));
                    }

                    try {
                        autoTable.addCell(new Phrase(r.getDate_certificate().toString(), font));
                    } catch (Exception e) {
                        autoTable.addCell(new Phrase("", font));
                    }

                    try {
                        autoTable.addCell(new Phrase(r.getEnd_date().toString(), font));
                    } catch (Exception e) {
                        autoTable.addCell(new Phrase("", font));
                    }

                    autoTable.addCell(new Phrase(r.getRelease_year_tc(), font));
                    autoTable.addCell(new Phrase(r.getCategory_control_tc(), font));
                    autoTable.addCell(new Phrase(r.getVin_kuzov_shassi(), font));
                    autoTable.addCell(new Phrase(r.getSeries_reg_number(), font));
                    number++;
                }
            }

            document.add(autoTable);
        }

        //КОНТАКТНЫЕ ДАННЫЕ ФЛ

        //ВОЕННЫЙ УЧЕТ
        List<MillitaryAccount> millitaryAccounts = result.getMillitaryAccounts();
        if (millitaryAccounts != null && millitaryAccounts.size() != 0) {
            PdfPTable MATable = new PdfPTable(4);
            MATable.setWidthPercentage(100f);
            MATable.setWidths(new float[] {0.4f, 1, 1, 1});
            MATable.setSpacingBefore(5);
            heading.setColspan(4);
            heading.setPhrase(new Phrase("Войнский учет", font));
            MATable.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            MATable.addCell(cell);
            cell.setPhrase(new Phrase("БИН воинской части", font));
            MATable.addCell(cell);
            cell.setPhrase(new Phrase("Дата службы с", font));
            MATable.addCell(cell);
            cell.setPhrase(new Phrase("Дата службы по", font));
            MATable.addCell(cell);
            int number = 1;
            for (MillitaryAccount r : millitaryAccounts) {
                MATable.addCell(new Phrase(number + "", font));
                MATable.addCell(new Phrase(r.getBin(), font));
                MATable.addCell(new Phrase(r.getDate_start(), font));
                MATable.addCell(new Phrase(r.getDate_end(), font));
                number++;
            }
            document.add(MATable);
        }
        //ОПРАВДАННЫЕ ПРЕСТУПЛЕНИЯ
        List<ConvictsJustified> convictsJustifieds = result.getConvictsJustifieds();
        if (convictsJustifieds != null && convictsJustifieds.size() != 0) {
            PdfPTable convicts = new PdfPTable(6);
            convicts.setWidthPercentage(100f);
            convicts.setWidths(new float[] {0.4f, 1, 1, 1, 1, 1});
            convicts.setSpacingBefore(5);
            heading.setColspan(6);
            heading.setPhrase(new Phrase("Наименование риска: \"Осужденные\" Количество найденных инф: " + convictsJustifieds.size(), font));
            convicts.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            convicts.addCell(cell);
            cell.setPhrase(new Phrase("Дата рассмотрения в суде 1 инстанции", font));
            convicts.addCell(cell);
            cell.setPhrase(new Phrase("Суд 1 инстанции", font));
            convicts.addCell(cell);
            cell.setPhrase(new Phrase("Решение по лицу", font));
            convicts.addCell(cell);
            cell.setPhrase(new Phrase("Мера наказания по договору", font));
            convicts.addCell(cell);
            cell.setPhrase(new Phrase("Квалификация", font));
            convicts.addCell(cell);
            int number = 1;
            for (ConvictsJustified r : convictsJustifieds) {
                convicts.addCell(new Phrase(number + "", font));
                convicts.addCell(new Phrase(r.getReg_date(), font));
                convicts.addCell(new Phrase(r.getCourt_of_first_instance(), font));

                if (r.getDecision_on_person() != null) {
                    convicts.addCell(new Phrase(r.getDecision_on_person(), font));
                } else {
                    convicts.addCell(new Phrase("", font));
                }

                if (r.getMeasure_punishment() != null) {
                    convicts.addCell(new Phrase(r.getMeasure_punishment(), font));
                } else {
                    convicts.addCell(new Phrase("", font));
                }

                convicts.addCell(new Phrase(r.getQualification(), font));

                number++;
            }

            document.add(convicts);
        }
        //ЕЩЕ КАКИЕТО ПРЕСТУЛПЛЕНИЯ
        List<ConvictsTerminatedByRehab> convictsTerminatedByRehabs = result.getConvictsTerminatedByRehabs();
        if (convictsTerminatedByRehabs != null && convictsTerminatedByRehabs.size()!=0) {
            PdfPTable ctbrTable = new PdfPTable(6);
            ctbrTable.setWidthPercentage(100f);
            ctbrTable.setWidths(new float[] {0.4f, 1, 1, 1, 1, 1});
            ctbrTable.setSpacingBefore(5);
            heading.setColspan(6);
            heading.setPhrase(new Phrase("Административные штрафы", font));
            ctbrTable.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            ctbrTable.addCell(cell);
            cell.setPhrase(new Phrase("Орган выявивший правонарушение", font));
            ctbrTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата заведения", font));
            ctbrTable.addCell(cell);
            cell.setPhrase(new Phrase("Квалификация", font));
            ctbrTable.addCell(cell);
            cell.setPhrase(new Phrase("Решение", font));
            ctbrTable.addCell(cell);
            cell.setPhrase(new Phrase("Уровень тяжести", font));
            ctbrTable.addCell(cell);
            int number = 1;
            for (ConvictsTerminatedByRehab r : convictsTerminatedByRehabs) {
                ctbrTable.addCell(new Phrase(number + "", font));
                ctbrTable.addCell(new Phrase(r.getInvestigative_authority(), font));
                ctbrTable.addCell(new Phrase(r.getLast_solution_date(), font));
                ctbrTable.addCell(new Phrase(r.getQualification_desc(), font));
                ctbrTable.addCell(new Phrase(r.getLast_solution(), font));
                ctbrTable.addCell(new Phrase(r.getQualification_by_11(), font));
                number++;
            }
            document.add(ctbrTable);
        }
        //БЛОКИРОВКА ЭСФ
        List<BlockEsf> blockEsfs = result.getBlockEsfs();
        if ( blockEsfs != null && blockEsfs.size()!=0) {
            PdfPTable blockesfTable = new PdfPTable(4);
            blockesfTable.setWidthPercentage(100f);
            blockesfTable.setWidths(new float[] {0.4f, 1, 1, 1});
            blockesfTable.setSpacingBefore(5);
            heading.setColspan(6);
            heading.setPhrase(new Phrase("Административные штрафы", font));
            blockesfTable.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            blockesfTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата блокировки", font));
            blockesfTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата востановления", font));
            blockesfTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата обновления", font));
            blockesfTable.addCell(cell);
            int number = 1;
            for (BlockEsf r : blockEsfs) {
                blockesfTable.addCell(new Phrase(number + "", font));

                if (r.getStart_dt() != null) {
                    blockesfTable.addCell(new Phrase(r.getStart_dt().toString(), font));
                } else {
                    blockesfTable.addCell(new Phrase("", font));
                }

                if (r.getEnd_dt() != null) {
                    blockesfTable.addCell(new Phrase(r.getEnd_dt().toString(), font));
                } else {
                    blockesfTable.addCell(new Phrase("", font));
                }

                if (r.getUpdate_dt() != null) {
                    blockesfTable.addCell(new Phrase(r.getUpdate_dt().toString(), font));
                } else {
                    blockesfTable.addCell(new Phrase("", font));
                }

                number++;
            }

            document.add(blockesfTable);
        }
        //СВЯЗИ С ЮЛ

        //НДС
        List<NdsEntity> ndsEntities = result.getNdsEntities();
        if (ndsEntities != null && ndsEntities.size()!=0) {
            PdfPTable ndsTable = new PdfPTable(5);
            ndsTable.setWidthPercentage(100f);
            ndsTable.setWidths(new float[] {0.15f, 1, 1, 1, 1});
            ndsTable.setSpacingBefore(5);
            heading.setColspan(5);
            heading.setPhrase(new Phrase("НДС", font));
            ndsTable.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            ndsTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата начала", font));
            ndsTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата конца", font));
            ndsTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата обновления", font));
            ndsTable.addCell(cell);
            cell.setPhrase(new Phrase("Причина", font));
            ndsTable.addCell(cell);
            int number = 1;
            for (NdsEntity r : ndsEntities) {
                ndsTable.addCell(new Phrase(number + "", font));

                if (r.getStartDt() != null) {
                    ndsTable.addCell(new Phrase(r.getStartDt().toString(), font));
                } else {
                    ndsTable.addCell(new Phrase("", font));
                }

                try {
                    ndsTable.addCell(new Phrase(r.getEndDt().toString(), font));
                } catch (Exception e) {
                    ndsTable.addCell(new Phrase("", font));
                }

                if (r.getUpdateDt() != null) {
                    ndsTable.addCell(new Phrase(r.getUpdateDt().toString(), font));
                } else {
                    ndsTable.addCell(new Phrase("", font));
                }

                try {
                    ndsTable.addCell(new Phrase(r.getReason(), font));
                } catch (Exception e) {
                    ndsTable.addCell(new Phrase("", font));
                }

                number++;
            }

            document.add(ndsTable);
        }
        //СВЕДЕНИЯ ИПГО
        List<IpgoEmailEntity> ipgoEmailEntities = result.getIpgoEmailEntities();
        if (ipgoEmailEntities != null && ipgoEmailEntities.size() != 0) {
            PdfPTable ipgoTable = new PdfPTable(4);
            ipgoTable.setWidthPercentage(100f);
            ipgoTable.setWidths(new float[] {0.15f, 1, 1, 1});
            ipgoTable.setSpacingBefore(5);
            heading.setColspan(4);
            heading.setPhrase(new Phrase("Сведения по ИПГО", font));
            ipgoTable.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            ipgoTable.addCell(cell);
            cell.setPhrase(new Phrase("Департамент", font));
            ipgoTable.addCell(cell);
            cell.setPhrase(new Phrase("Должность", font));
            ipgoTable.addCell(cell);
            cell.setPhrase(new Phrase("ИПГО почта", font));
            ipgoTable.addCell(cell);
            int number = 1;
            for (IpgoEmailEntity r : ipgoEmailEntities) {
                ipgoTable.addCell(new Phrase(number + "", font));

                if (r.getOrgan() != null) {
                    ipgoTable.addCell(new Phrase(r.getOrgan().toString(), font));
                } else {
                    ipgoTable.addCell(new Phrase("", font));
                }

                try {
                    ipgoTable.addCell(new Phrase(r.getPosition(), font));
                } catch (Exception e) {
                    ipgoTable.addCell(new Phrase("", font));
                }

                if (r.getEmail() != null) {
                    ipgoTable.addCell(new Phrase(r.getEmail().toString(), font));
                } else {
                    ipgoTable.addCell(new Phrase("", font));
                }

                number++;
            }
            document.add(ipgoTable);
        }
        List<FlRelativiesDTO> flRelativiesDTOS = myService.getRelativesInfo(result.getMvIinDocs().get(0).getIin());
        if (flRelativiesDTOS != null && !flRelativiesDTOS.isEmpty()) {
            PdfPTable docTable = new PdfPTable(7);
            docTable.setWidthPercentage(100f);
            docTable.setWidths(new float[] {1, 1, 1, 1, 1, 1, 1});
            docTable.setSpacingBefore(5);
            heading.setColspan(7);
            heading.setPhrase(new Phrase("Наименование коллапса: \"Сведения о родственных связях данного ФЛ\" Количество найденных инф: " + flRelativiesDTOS.size(), font));
            docTable.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Статус по отношению к родственнику", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("ФИО", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата регистрации брака", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата расторжения брака", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("ИИН родственника", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата рождения", font));
            docTable.addCell(cell);
            int number = 1;
            for (FlRelativiesDTO flRelativiesDTO : flRelativiesDTOS) {
                docTable.addCell(new Phrase(String.valueOf(number), font));
                docTable.addCell(new Phrase(flRelativiesDTO.getRelative_type() != null ? flRelativiesDTO.getRelative_type() : "", font));
                docTable.addCell(new Phrase(flRelativiesDTO.getParent_fio() != null ? flRelativiesDTO.getParent_fio() : "", font));
                docTable.addCell(new Phrase(flRelativiesDTO.getMarriage_reg_date() != null ? flRelativiesDTO.getMarriage_reg_date().toString() : "", font));
                docTable.addCell(new Phrase(flRelativiesDTO.getMarriage_divorce_date() != null ? flRelativiesDTO.getMarriage_divorce_date().toString() : "", font));
                docTable.addCell(new Phrase(flRelativiesDTO.getParent_iin() != null ? flRelativiesDTO.getParent_iin() : "", font));
                docTable.addCell(new Phrase(flRelativiesDTO.getParent_birth_date() != null ? flRelativiesDTO.getParent_birth_date() : "", font));
                number++;
            }
            document.add(docTable);
        }
        if (flRelativiesDTOS != null && !flRelativiesDTOS.isEmpty()) {
            for(FlRelativiesDTO flRelativiesDTOs : flRelativiesDTOS) {
                if (flRelativiesDTOs.getParent_iin() != null) {
                    pdfForRelativesAddition(document, myService.getNode(flRelativiesDTOs.getParent_iin()));
                }
            }
        }

        document.close();
        return document;
    }

    public Document pdfForRelativesAddition(Document document, NodesFL result) throws DocumentException, IOException {
        PdfPTable table = new PdfPTable(10);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        table.setSpacingBefore(5);
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(CMYKColor.WHITE);
        cell.setPadding(5);
        Font font = new Font(getBaseFont(), 11);
        font.setColor(CMYKColor.WHITE);
        PdfPCell heading = new PdfPCell();
        heading.setBackgroundColor(CMYKColor.GRAY);
        heading.setPadding(4);
        heading.setColspan(10);
        heading.setHorizontalAlignment(Element.ALIGN_CENTER);
        try {
            heading.setPhrase(new Phrase("Сведения о физическом лице", font));
            table.addCell(heading);
            font.setColor(CMYKColor.BLACK);

            cell.setPhrase(new Phrase("Фото", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("ИИН", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("ФИО", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Резидент", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Гражданство", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Нация", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Дата рождения", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Место рождения", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Статус", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Дата смерти", font));
            table.addCell(cell);
            try {
                table.addCell(Image.getInstance(result.getPhotoDbf().get(0).getPhoto()));
            }catch (Exception e){
                cell.setPhrase(new Phrase("Фото не найдено", font));
                table.addCell(cell);
                System.out.println("Фото не найдено");
            }

            cell.setPhrase(new Phrase(result.getMvFls().get(0).getIin(), font));
            table.addCell(cell);
            cell.setPhrase(new Phrase(result.getMvFls().get(0).getLast_name() + "\n" + result.getMvFls().get(0).getFirst_name() + "\n" + result.getMvFls().get(0).getPatronymic(), font));
            table.addCell(cell);

            if (result.getMvFls().get(0).is_resident()) {
                cell.setPhrase(new Phrase("ДА", font));
            } else {
                cell.setPhrase(new Phrase("НЕТ", font));
            }
            table.addCell(cell);
            cell.setPhrase(new Phrase(result.getMvFls().get(0).getNationality_ru_name(), font));
            table.addCell(cell);
            cell.setPhrase(new Phrase(result.getMvFls().get(0).getCitizenship_ru_name(), font));
            table.addCell(cell);
            cell.setPhrase(new Phrase(result.getMvFls().get(0).getBirth_date(), font));
            table.addCell(cell);
            cell.setPhrase(new Phrase(result.getMvFls().get(0).getDistrict() + ", " + result.getMvFls().get(0).getRegion(), font));
            table.addCell(cell);
            cell.setPhrase(new Phrase(result.getMvFls().get(0).getLife_status_ru_name(), font));
            table.addCell(cell);
            if (result.getMvFls().get(0).getDeath_date() == null || result.getMvFls().get(0).getDeath_date().isEmpty()) {
                cell.setPhrase(new Phrase("Отсутсвует", font));
            } else {
                cell.setPhrase(new Phrase(result.getMvFls().get(0).getDeath_date(), font));
            }
            table.addCell(cell);
            document.add(table);
        }catch (Exception e){
            System.out.println(e);
        }
        // ОСНОВНАЯ ИНФА ОБ ФЛ
        // АДДРЕСА ФЛ

        // ДОКУМЕНТЫ ФЛ
        List<MvIinDoc> docs = result.getMvIinDocs();
        if (docs != null && docs.size() != 0) {
            PdfPTable docTable = new PdfPTable(6);
            docTable.setWidthPercentage(100f);
            docTable.setWidths(new float[] {1, 1, 1, 1, 1, 1});
            docTable.setSpacingBefore(5);
            heading.setColspan(6);
            heading.setPhrase(new Phrase("Наименование коллапса: \"Документы\" Количество найденных инф: " + docs.size(), font));
            docTable.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Типа Документа", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Орган выдачи", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата выдачи", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Срок до", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Номер документа", font));
            docTable.addCell(cell);
            int number = 1;
            for (MvIinDoc r : docs) {
                docTable.addCell(new Phrase(String.valueOf(number), font));
                docTable.addCell(new Phrase(r.getDoc_type_ru_name(), font));
                docTable.addCell(new Phrase(r.getIssue_organization_ru_name(), font));
                docTable.addCell(new Phrase(r.getIssue_date().toString(), font));
                docTable.addCell(new Phrase(r.getExpiry_date().toString(), font));
                docTable.addCell(new Phrase(r.getDoc_number(), font));
                number++;
            }
            document.add(docTable);
        }
        List<MvFlAddress> addressFls = result.getMvFlAddresses();
        if (addressFls != null && !addressFls.isEmpty()) {

            PdfPTable addresses = new PdfPTable(5);
            addresses.setWidthPercentage(100f);
            addresses.setWidths(new float[] {1, 1, 1, 1, 1});
            addresses.setSpacingBefore(5);
            heading.setColspan(5);
            heading.setPhrase(new Phrase("Наименование коллапса: \"Адрес прописки\" Количество найденных инф: " + addressFls.size(), font));
            addresses.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            addresses.addCell(cell);
            cell.setPhrase(new Phrase("Город", font));
            addresses.addCell(cell);
            cell.setPhrase(new Phrase("Улица, дом, квартира", font));
            addresses.addCell(cell);
            cell.setPhrase(new Phrase("Район", font));
            addresses.addCell(cell);
            cell.setPhrase(new Phrase("Дата регистрации", font));
            addresses.addCell(cell);
            int number = 1;
            for (MvFlAddress ar : addressFls) {
                addresses.addCell(new Phrase(String.valueOf(number), font));
                addresses.addCell(new Phrase(ar.getDistrict(), font));
                addresses.addCell(new Phrase(ar.getRegion(), font));
                addresses.addCell(new Phrase(ar.getStreet(), font));
                addresses.addCell(new Phrase(ar.getBuilding(), font));
                number++;
            }
            document.add(addresses);
        }
        List<ChangeFio> changeFios = changeFioRepo.getByIin(result.getMvFls().get(0).getIin());
        if (changeFios != null && !changeFios.isEmpty()) {
            PdfPTable addresses = new PdfPTable(4);
            addresses.setWidthPercentage(100f);
            addresses.setWidths(new float[] {1, 1, 1, 1});
            addresses.setSpacingBefore(5);
            heading.setColspan(4);
            heading.setPhrase(new Phrase("Наименование коллапса: \"Смена ФИО\" Количество найденных инф: " + changeFios.size(), font));
            addresses.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            addresses.addCell(cell);
            cell.setPhrase(new Phrase("Предыдущие данные", font));
            addresses.addCell(cell);
            cell.setPhrase(new Phrase("Причина смены", font));
            addresses.addCell(cell);
            cell.setPhrase(new Phrase("Дата смены ФИО", font));
            addresses.addCell(cell);
            int number = 1;
            for (ChangeFio ar : changeFios) {
                addresses.addCell(new Phrase(String.valueOf(number), font));
                addresses.addCell(new Phrase(ar.getSurname_before() !=null ? ar.getSurname_before() : " "  + " " +  ar.getName_before() !=null ? ar.getName_before() : " " + " " + ar.getSecondname_before() !=null ? ar.getSecondname_before() : " ", font));
                addresses.addCell(new Phrase(ar.getRemarks(), font));
                addresses.addCell(new Phrase(ar.getTo_date(), font));
                number++;
            }
            document.add(addresses);
        }
        List<Adm> adms = admRepo.getUsersByLike(result.getMvFls().get(0).getIin());
        if (adms != null && !adms.isEmpty()) {
            PdfPTable docTable = new PdfPTable(10);
            docTable.setWidthPercentage(100f);
            docTable.setWidths(new float[] {1, 1, 1, 1, 1, 1,1,1,1,1});
            docTable.setSpacingBefore(5);
            heading.setColspan(10);
            heading.setPhrase(new Phrase("Наименование коллапса: \"Административные штрафы\" Количество найденных инф: " + adms.size(), font));
            docTable.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Орган выявивший правонарушение", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата заведения", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Номер протокола", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Место работы", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Квалификация", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Принудительное исполенеия", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("На срок до", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Размер наложенного штрафа", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Основания прекращения", font));
            docTable.addCell(cell);
            int number = 1;
            for (Adm r : adms) {
                docTable.addCell(new Phrase(String.valueOf(number), font));
                docTable.addCell(new Phrase(r.getAuthority_detected(), font));
                docTable.addCell(new Phrase(r.getReg_date(), font));
                docTable.addCell(new Phrase(r.getProtocol_num(), font));
                docTable.addCell(new Phrase(r.getWork_place(), font));
                docTable.addCell(new Phrase(r.getQualification_name(), font));
                docTable.addCell(new Phrase(r.getEnforcement(), font));
                docTable.addCell(new Phrase(r.getEnd_date(), font));
                docTable.addCell(new Phrase(r.getFine_amount(), font));
                docTable.addCell(new Phrase(r.getTeminate_reason(), font));
                number++;
            }
            document.add(docTable);
        }
        List<FlContacts> flContacts = flContactsRepo.findAllByIin(result.getMvFls().get(0).getIin());
        if (flContacts != null && !flContacts.isEmpty()) {
            PdfPTable docTable = new PdfPTable(7);
            docTable.setWidthPercentage(100f);
            docTable.setWidths(new float[] {1, 1, 1, 1, 1,1,1});
            docTable.setSpacingBefore(5);
            heading.setColspan(7);
            heading.setPhrase(new Phrase("Наименование коллапса: \"Контактные данные\" Количество найденных инф: " + flContacts.size(), font));
            docTable.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Номер телефона", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Email", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("ФИО руководителя организации", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("ФИО/Наименование организации владельца номера", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Источник", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Nickname", font));
            docTable.addCell(cell);
            int number = 1;
            for (FlContacts flContacts1 : flContacts) {
                docTable.addCell(new Phrase(String.valueOf(number), font));
                docTable.addCell(new Phrase(flContacts1.getPhone() != null ? flContacts1.getPhone() : "", font));
                docTable.addCell(new Phrase(flContacts1.getEmail() != null ? flContacts1.getEmail() : "", font));
                docTable.addCell(new Phrase(flContacts1.getLeader_fio() != null ? flContacts1.getLeader_fio().toString() : "", font));
                docTable.addCell(new Phrase(flContacts1.getNickname() != null ? flContacts1.getNickname().toString() : "", font));
                docTable.addCell(new Phrase(flContacts1.getSource() != null ? flContacts1.getSource() : "", font));
                docTable.addCell(new Phrase(flContacts1.getNickname() != null ? flContacts1.getNickname() : "", font));
                number++;
            }
            document.add(docTable);
        }
        List<IndividualEntrepreneur> individualEntrepreneurs = individualEntrepreneurRepo.getByIin(result.getMvFls().get(0).getIin());
        List<KX> kxes = kxRepo.getKxIin(result.getMvFls().get(0).getIin());
        if (individualEntrepreneurs != null && !individualEntrepreneurs.isEmpty()) {
            PdfPTable docTable = new PdfPTable(3);
            docTable.setWidthPercentage(100f);
            docTable.setWidths(new float[] {1, 1,1});
            docTable.setSpacingBefore(5);
            heading.setColspan(3);
            heading.setPhrase(new Phrase("Наименование коллапса: \"ИП/КХ\" Количество найденных инф:  " + (individualEntrepreneurs.size()+ kxes.size()), font));
            docTable.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Наименование", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("ИП/КХ", font));
            docTable.addCell(cell);
            int number = 1;
            for (IndividualEntrepreneur individualEntrepreneur : individualEntrepreneurs) {
                docTable.addCell(new Phrase(String.valueOf(number), font));
                docTable.addCell(new Phrase(individualEntrepreneur.getName_rus() != null ? individualEntrepreneur.getName_rus() : "", font));
                docTable.addCell(new Phrase("ИП", font));
                number++;
            }
            if(kxes != null && !kxes.isEmpty()){
                for(KX kx: kxes) {
                    docTable.addCell(new Phrase(String.valueOf(number), font));
                    docTable.addCell(new Phrase(kx.getName_rus() != null ? kx.getName_rus() : "", font));
                    docTable.addCell(new Phrase("КХ", font));
                    number++;
                }
            }
            document.add(docTable);
        }
        GosZakupForAll gosZakupForAll = myService.gosZakupByBin(result.getMvFls().get(0).getIin());
        if ((gosZakupForAll.getWhenCustomer()!= null && !gosZakupForAll.getWhenCustomer().isEmpty()) || (gosZakupForAll.getWhenSupplier()!= null && !gosZakupForAll.getWhenSupplier().isEmpty())) {
            PdfPTable schoolTable = new PdfPTable(5);
            schoolTable.setWidthPercentage(100f);
            schoolTable.setWidths(new float[] {1, 1, 1, 1, 1});
            schoolTable.setSpacingBefore(5);
            heading.setColspan(5);
            heading.setPhrase(new Phrase("Наименование коллапса: \"Государственные закупки- По поставщикам\" Количество найденных инф: " + (gosZakupForAll.getWhenSupplier().size() + gosZakupForAll.getWhenCustomer().size()), font));
            schoolTable.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Год", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Общая сумма", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Количество договоров", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Поставщики", font));
            schoolTable.addCell(cell);
            int number = 1;
            for (GosZakupDTO r : gosZakupForAll.getWhenSupplier()) {
                schoolTable.addCell(new Phrase(String.valueOf(number), font));
                schoolTable.addCell(new Phrase(r.getPeriod(), font));
                schoolTable.addCell(new Phrase(r.getSum(), font));
                schoolTable.addCell(new Phrase(String.valueOf(r.getNumber()), font));
                schoolTable.addCell(new Phrase(r.getOpposite().toString(), font));
                number++;
            }
            for (GosZakupDTO r : gosZakupForAll.getWhenCustomer()) {
                schoolTable.addCell(new Phrase(String.valueOf(number), font));
                schoolTable.addCell(new Phrase(r.getPeriod(), font));
                schoolTable.addCell(new Phrase(r.getSum(), font));
                schoolTable.addCell(new Phrase(String.valueOf(r.getNumber()), font));
                schoolTable.addCell(new Phrase(r.getOpposite().toString(), font));
                number++;
            }
            document.add(schoolTable);
        }
        List<String> companyBins = flPensionContrRepo.getUsersByLikeCompany(result.getMvFls().get(0).getIin());

        List<PensionListDTO> pensions = new ArrayList<>();
        List<PensionGroupDTO> results = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(0);
        for (String bin : companyBins) {
            List<Map<String, Object>> fl_pension_contrss = new ArrayList<>();
            fl_pension_contrss = flPensionContrRepo.getAllByCompanies(result.getMvFls().get(0).getIin(),bin);
            PensionGroupDTO obj = new PensionGroupDTO();
            List<PensionListDTO> group = new ArrayList<>();
            String name = "";
            if (fl_pension_contrss.get(0).get("P_NAME") != null) {
                name = (String)fl_pension_contrss.get(0).get("P_NAME") + ", ";
            }
            if (bin != null) {
                name = name + bin + ", период ";
            }
            List<String> distinctPayDates = fl_pension_contrss.stream()
                    .map(pension -> pension.get("pay_date").toString())
                    .distinct()
                    .collect(Collectors.toList());

            double knp010sum = 0.0;
            double knp012sum = 0.0;

            for (String year : distinctPayDates) {
                if (year != null) {
                    name = name + year.replace(".0", "") + ", ";
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

                    knp010sum = knp010sum + knp010;

                } catch (Exception e) {

                }
                try {
                    double knp012 = fl_pension_contrss.stream()
                            .filter(pension -> pension.get("pay_date").toString().equals(year) && pension.get("KNP").toString().equals("012"))
                            .mapToDouble(pension -> Double.parseDouble(pension.get("AMOUNT").toString()))
                            .sum();

                    pensionListEntity.setSum012(knp012);
                    knp012sum = knp012sum + knp012;
                } catch (Exception e) {

                }pensions.add(pensionListEntity);
                group.add(pensionListEntity);
            }
            name = name + "общая сумма КНП(010): " + df.format(knp010sum) + ", общая сумма КНП(012): " + df.format(knp012sum);
            obj.setName(name);
            obj.setList(group);
            results.add(obj);
        }

        if (results != null && !results.isEmpty()) {
            PdfPTable docTable = new PdfPTable(5);
            docTable.setWidthPercentage(100f);
            docTable.setWidths(new float[] {1, 1, 1, 1,1});
            docTable.setSpacingBefore(5);
            heading.setColspan(5);
            heading.setPhrase(new Phrase("Наименование коллапса: \"Пенсионные отчисления\" Количество найденных инф: " + results.size(), font));
            docTable.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("БИН, Наименование ЮЛ", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Период", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Общая сумма(010)", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Общая сумма(012)", font));
            docTable.addCell(cell);
            int number = 1;
            for (PensionGroupDTO pensionListDTO : results) {
                docTable.addCell(new Phrase(String.valueOf(number), font));
                docTable.addCell(new Phrase(getSubstring(pensionListDTO.getName(),0, ", п"), font));
                docTable.addCell(new Phrase(getSubstringByTwoWords(pensionListDTO.getName(),", период ", ", общая"), font));
                docTable.addCell(new Phrase(getSubstringByTwoWords(pensionListDTO.getName(),"общая сумма КНП(010): ", ", общая сумма КНП(012)"), font));
                docTable.addCell(new Phrase(getSubstringByLastWord(pensionListDTO.getName(),"общая сумма КНП(012): ", pensionListDTO.getName()), font));
                number++;
            }
            document.add(docTable);
        }

        List<MvUlLeaderEntity> mvUlLeaderEntities  = mvUlLeaderEntityRepo.getUsersByLikeIin(result.getMvFls().get(0).getIin());
        try {
            for(MvUlLeaderEntity mvUlLeaderEntity: mvUlLeaderEntities){
                mvUlLeaderEntity.setBinName(mvUlRepo.getNameByBin(mvUlLeaderEntity.getBinOrg()));
            }
        } catch (Exception e) {
        }
        List<MvUlFounderFl> mvUlFounderFls  = result.getMvUlFounderFls();
        try {
            for(MvUlFounderFl mvUlFounderFl: mvUlFounderFls){
                mvUlFounderFl.setBinName(mvUlRepo.getNameByBin(mvUlFounderFl.getBin_org()));
            }
        } catch (Exception e) {
        }
        if ((mvUlLeaderEntities != null && !mvUlLeaderEntities.isEmpty()) || (mvUlFounderFls != null && !mvUlFounderFls.isEmpty())) {
            PdfPTable docTable = new PdfPTable(5);
            docTable.setWidthPercentage(100f);
            docTable.setWidths(new float[] {1, 1, 1, 1, 1});
            docTable.setSpacingBefore(5);
            heading.setColspan(5);
            heading.setPhrase(new Phrase("Наименование коллапса: \"Сведения об участниках ЮЛ\" Количество найденных инф: " + (mvUlLeaderEntities.size() + mvUlFounderFls.size()), font));
            docTable.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Идентификатор ЮЛ", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата регистрации", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("БИН", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Наименование ЮЛ", font));
            docTable.addCell(cell);
            int number = 1;
            for (MvUlLeaderEntity r : mvUlLeaderEntities) {
                docTable.addCell(new Phrase(String.valueOf(number), font));
                docTable.addCell(new Phrase("Директор", font));
                docTable.addCell(new Phrase(r.getRegDate(), font));
                docTable.addCell(new Phrase(r.getBinOrg(), font));
                docTable.addCell(new Phrase(r.getBinName(), font));
                number++;
            }
            for (MvUlFounderFl r : mvUlFounderFls) {
                docTable.addCell(new Phrase(String.valueOf(number), font));
                docTable.addCell(new Phrase("Учеридитель", font));
                docTable.addCell(new Phrase(r.getReg_date(), font));
                docTable.addCell(new Phrase(r.getBin_org(), font));
                docTable.addCell(new Phrase(r.getBinName(), font));
                number++;
            }
            document.add(docTable);
        }
        List<MvRnOld> mvRnOlds = result.getMvRnOlds();
        if (mvRnOlds != null && !mvRnOlds.isEmpty()) {
            PdfPTable schoolTable = new PdfPTable(13);
            schoolTable.setWidthPercentage(100f);
            schoolTable.setWidths(new float[] {1, 1, 1, 1, 1,1, 1, 1, 1, 1,1, 1, 1});
            schoolTable.setSpacingBefore(5);
            heading.setColspan(13);
            heading.setPhrase(new Phrase("Наименование коллапса: \"Сведения по реестру недвижимости\" Количество найденных инф: " + mvRnOlds.size(), font));
            schoolTable.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Кадастровый номер", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Адрес", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Правообладатель", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Этажность", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Количество составляющих", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Площадь общая", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Вид документа", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Номер документа", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата документа", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Сумма сделки", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Жилая площадь", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Статус", font));
            schoolTable.addCell(cell);
            int number = 1;
            for (MvRnOld r : mvRnOlds) {
                System.out.println(mvRnOlds.size());
                System.out.println(r.getCadastral_number());
                schoolTable.addCell(new Phrase(String.valueOf(number), font));
                schoolTable.addCell(new Phrase(r.getCadastral_number() != null ? r.getCadastral_number() : " ", font));
                schoolTable.addCell(new Phrase(r.getAddress_rus(), font));
                schoolTable.addCell(new Phrase(r.getOwner_iin_bin(), font));
                schoolTable.addCell(new Phrase(r.getFloor(), font));
                schoolTable.addCell(new Phrase(r.getType_of_property_rus(), font));
                schoolTable.addCell(new Phrase(r.getArea_total(), font));
                schoolTable.addCell(new Phrase(r.getType_of_property_rus(), font));
                schoolTable.addCell(new Phrase(r.getRegister_emergence_rights_rus(), font));
                schoolTable.addCell(new Phrase(r.getRegister_reg_date(), font));
                schoolTable.addCell(new Phrase(r.getRegister_transaction_amount(), font));
                schoolTable.addCell(new Phrase(r.getArea_useful(), font));
                schoolTable.addCell(new Phrase(r.getRegister_status_rus(), font));
                number++;
            }

            document.add(schoolTable);
        }
        //ШКОЛЫ ФЛ
        List<School> schools = result.getSchools();
        if (schools != null && !schools.isEmpty()) {
            PdfPTable schoolTable = new PdfPTable(5);
            schoolTable.setWidthPercentage(100f);
            schoolTable.setWidths(new float[] {1, 1, 1, 1, 1});
            schoolTable.setSpacingBefore(5);
            heading.setColspan(5);
            heading.setPhrase(new Phrase("Наименование коллапса: \"Сведение по образование: Среднее образование (Школа)\" Количество найденных инф: " + schools.size(), font));
            schoolTable.addCell(heading);
            cell.setPhrase(new Phrase("БИН", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Название школы", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Класс", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата поступления", font));
            schoolTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата окончания", font));
            schoolTable.addCell(cell);
            for (School r : schools) {
                schoolTable.addCell(new Phrase(r.getSchool_code(), font));
                schoolTable.addCell(new Phrase(r.getSchool_name(), font));
                schoolTable.addCell(new Phrase(r.getGrade(), font));
                schoolTable.addCell(new Phrase(r.getStart_date().toString(), font));
                schoolTable.addCell(new Phrase(String.valueOf(r.getEnd_date()), font));
            }

            document.add(schoolTable);
        }
        //УНИВЕРСИТЕТЫ ФЛ
        List<Universities> universities = result.getUniversities();
        if (universities != null && universities.size()!=0 ) {
            PdfPTable uniTable = new PdfPTable(7);
            uniTable.setWidthPercentage(100f);
            uniTable.setWidths(new float[] {1, 1, 1, 1, 1, 1, 1});
            uniTable.setSpacingBefore(5);
            heading.setColspan(7);
            heading.setPhrase(new Phrase("Наименование коллапса: \"Сведение по образование: Высшее образование (Университет)\" Количество найденных инф: " + universities.size(), font));
            uniTable.addCell(heading);
            cell.setPhrase(new Phrase("БИН", font));
            uniTable.addCell(cell);
            cell.setPhrase(new Phrase("Название вуза", font));
            uniTable.addCell(cell);
            cell.setPhrase(new Phrase("Специализация", font));
            uniTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата поступления", font));
            uniTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата окончания", font));
            uniTable.addCell(cell);
            cell.setPhrase(new Phrase("Длительность обучения", font));
            uniTable.addCell(cell);
            cell.setPhrase(new Phrase("Курс", font));
            uniTable.addCell(cell);
            for (Universities r : universities) {
                uniTable.addCell(new Phrase(r.getStudy_code(), font));
                uniTable.addCell(new Phrase(r.getStudy_name(), font));
                uniTable.addCell(new Phrase(r.getSpec_name(), font));

                if (r.getStart_date() != null) {
                    uniTable.addCell(new Phrase(r.getStart_date().toString(), font));
                } else {
                    uniTable.addCell(new Phrase("", font));
                }

                if (r.getEnd_date() != null) {
                    uniTable.addCell(new Phrase(r.getEnd_date().toString(), font));
                } else {
                    uniTable.addCell(new Phrase("", font));
                }

                uniTable.addCell(new Phrase(r.getDuration(), font));
                uniTable.addCell(new Phrase(r.getCourse(), font));
            }

            document.add(uniTable);
        }
        //ТРАНСПОРТ ФЛ
        List<MvAutoFl> autos = result.getMvAutoFls();
        if (autos != null && autos.size()!=0) {
            PdfPTable autoTable = new PdfPTable(10);
            autoTable.setWidthPercentage(100f);
            autoTable.setWidths(new float[] {0.4f, 1, 1, 1, 1, 1, 1, 1, 1, 1});
            autoTable.setSpacingBefore(5);
            heading.setColspan(10);
            heading.setPhrase(new Phrase("Наименование коллапса: \"Транспорт\" Количество найденных инф: " + autos.size(), font));
            autoTable.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            autoTable.addCell(cell);
            cell.setPhrase(new Phrase("Статус", font));
            autoTable.addCell(cell);
            cell.setPhrase(new Phrase("Регистрационный номер", font));
            autoTable.addCell(cell);
            cell.setPhrase(new Phrase("Марка модель", font));
            autoTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата выдачи свидетельства", font));
            autoTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата снятия", font));
            autoTable.addCell(cell);
            cell.setPhrase(new Phrase("Год выпуска", font));
            autoTable.addCell(cell);
            cell.setPhrase(new Phrase("Категория", font));
            autoTable.addCell(cell);
            cell.setPhrase(new Phrase("VIN/Кузов/Шосси", font));
            autoTable.addCell(cell);
            cell.setPhrase(new Phrase("Серия", font));
            autoTable.addCell(cell);
            int number = 1;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            for (MvAutoFl r : autos) {
                if (r != null) {
                    autoTable.addCell(new Phrase(number + "", font));

                    try {
                        if (r.isIs_registered()) {
                            autoTable.addCell(new Phrase("Текущий", font));
                        } else {
                            autoTable.addCell(new Phrase("Исторический", font));
                        }
                    } catch (Exception e) {
                        autoTable.addCell(new Phrase("", font));
                    }

                    try {
                        autoTable.addCell(new Phrase(r.getReg_number(), font));
                    } catch (Exception e) {
                        autoTable.addCell(new Phrase("", font));
                    }

                    try {
                        autoTable.addCell(new Phrase(r.getBrand_model(), font));
                    } catch (Exception e) {
                        autoTable.addCell(new Phrase("", font));
                    }

                    try {
                        autoTable.addCell(new Phrase(r.getDate_certificate().toString(), font));
                    } catch (Exception e) {
                        autoTable.addCell(new Phrase("", font));
                    }

                    try {
                        autoTable.addCell(new Phrase(r.getEnd_date().toString(), font));
                    } catch (Exception e) {
                        autoTable.addCell(new Phrase("", font));
                    }

                    autoTable.addCell(new Phrase(r.getRelease_year_tc(), font));
                    autoTable.addCell(new Phrase(r.getCategory_control_tc(), font));
                    autoTable.addCell(new Phrase(r.getVin_kuzov_shassi(), font));
                    autoTable.addCell(new Phrase(r.getSeries_reg_number(), font));
                    number++;
                }
            }

            document.add(autoTable);
        }

        //КОНТАКТНЫЕ ДАННЫЕ ФЛ

        //ВОЕННЫЙ УЧЕТ
        List<MillitaryAccount> millitaryAccounts = result.getMillitaryAccounts();
        if (millitaryAccounts != null && !millitaryAccounts.isEmpty()) {
            PdfPTable MATable = new PdfPTable(4);
            MATable.setWidthPercentage(100f);
            MATable.setWidths(new float[] {0.4f, 1, 1, 1});
            MATable.setSpacingBefore(5);
            heading.setColspan(4);
            heading.setPhrase(new Phrase("Войнский учет", font));
            MATable.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            MATable.addCell(cell);
            cell.setPhrase(new Phrase("БИН воинской части", font));
            MATable.addCell(cell);
            cell.setPhrase(new Phrase("Дата службы с", font));
            MATable.addCell(cell);
            cell.setPhrase(new Phrase("Дата службы по", font));
            MATable.addCell(cell);
            int number = 1;
            for (MillitaryAccount r : millitaryAccounts) {
                MATable.addCell(new Phrase(number + "", font));
                MATable.addCell(new Phrase(r.getBin(), font));
                MATable.addCell(new Phrase(r.getDate_start(), font));
                MATable.addCell(new Phrase(r.getDate_end(), font));
                number++;
            }
            document.add(MATable);
        }
        //ОПРАВДАННЫЕ ПРЕСТУПЛЕНИЯ
        List<ConvictsJustified> convictsJustifieds = result.getConvictsJustifieds();
        if (convictsJustifieds != null && !convictsJustifieds.isEmpty()) {
            PdfPTable convicts = new PdfPTable(6);
            convicts.setWidthPercentage(100f);
            convicts.setWidths(new float[] {0.4f, 1, 1, 1, 1, 1});
            convicts.setSpacingBefore(5);
            heading.setColspan(6);
            heading.setPhrase(new Phrase("Наименование риска: \"Осужденные\" Количество найденных инф: " + convictsJustifieds.size(), font));
            convicts.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            convicts.addCell(cell);
            cell.setPhrase(new Phrase("Дата рассмотрения в суде 1 инстанции", font));
            convicts.addCell(cell);
            cell.setPhrase(new Phrase("Суд 1 инстанции", font));
            convicts.addCell(cell);
            cell.setPhrase(new Phrase("Решение по лицу", font));
            convicts.addCell(cell);
            cell.setPhrase(new Phrase("Мера наказания по договору", font));
            convicts.addCell(cell);
            cell.setPhrase(new Phrase("Квалификация", font));
            convicts.addCell(cell);
            int number = 1;
            for (ConvictsJustified r : convictsJustifieds) {
                convicts.addCell(new Phrase(number + "", font));
                convicts.addCell(new Phrase(r.getReg_date(), font));
                convicts.addCell(new Phrase(r.getCourt_of_first_instance(), font));

                if (r.getDecision_on_person() != null) {
                    convicts.addCell(new Phrase(r.getDecision_on_person(), font));
                } else {
                    convicts.addCell(new Phrase("", font));
                }

                if (r.getMeasure_punishment() != null) {
                    convicts.addCell(new Phrase(r.getMeasure_punishment(), font));
                } else {
                    convicts.addCell(new Phrase("", font));
                }

                convicts.addCell(new Phrase(r.getQualification(), font));

                number++;
            }

            document.add(convicts);
        }
        //ЕЩЕ КАКИЕТО ПРЕСТУЛПЛЕНИЯ
        List<ConvictsTerminatedByRehab> convictsTerminatedByRehabs = result.getConvictsTerminatedByRehabs();
        if (convictsTerminatedByRehabs != null && convictsTerminatedByRehabs.size()!=0) {
            PdfPTable ctbrTable = new PdfPTable(6);
            ctbrTable.setWidthPercentage(100f);
            ctbrTable.setWidths(new float[] {0.4f, 1, 1, 1, 1, 1});
            ctbrTable.setSpacingBefore(5);
            heading.setColspan(6);
            heading.setPhrase(new Phrase("Административные штрафы", font));
            ctbrTable.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            ctbrTable.addCell(cell);
            cell.setPhrase(new Phrase("Орган выявивший правонарушение", font));
            ctbrTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата заведения", font));
            ctbrTable.addCell(cell);
            cell.setPhrase(new Phrase("Квалификация", font));
            ctbrTable.addCell(cell);
            cell.setPhrase(new Phrase("Решение", font));
            ctbrTable.addCell(cell);
            cell.setPhrase(new Phrase("Уровень тяжести", font));
            ctbrTable.addCell(cell);
            int number = 1;
            for (ConvictsTerminatedByRehab r : convictsTerminatedByRehabs) {
                ctbrTable.addCell(new Phrase(number + "", font));
                ctbrTable.addCell(new Phrase(r.getInvestigative_authority(), font));
                ctbrTable.addCell(new Phrase(r.getLast_solution_date(), font));
                ctbrTable.addCell(new Phrase(r.getQualification_desc(), font));
                ctbrTable.addCell(new Phrase(r.getLast_solution(), font));
                ctbrTable.addCell(new Phrase(r.getQualification_by_11(), font));
                number++;
            }
            document.add(ctbrTable);
        }
        //БЛОКИРОВКА ЭСФ
        List<BlockEsf> blockEsfs = result.getBlockEsfs();
        if ( blockEsfs != null && blockEsfs.size()!=0) {
            PdfPTable blockesfTable = new PdfPTable(4);
            blockesfTable.setWidthPercentage(100f);
            blockesfTable.setWidths(new float[] {0.4f, 1, 1, 1});
            blockesfTable.setSpacingBefore(5);
            heading.setColspan(6);
            heading.setPhrase(new Phrase("Административные штрафы", font));
            blockesfTable.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            blockesfTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата блокировки", font));
            blockesfTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата востановления", font));
            blockesfTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата обновления", font));
            blockesfTable.addCell(cell);
            int number = 1;
            for (BlockEsf r : blockEsfs) {
                blockesfTable.addCell(new Phrase(number + "", font));

                if (r.getStart_dt() != null) {
                    blockesfTable.addCell(new Phrase(r.getStart_dt().toString(), font));
                } else {
                    blockesfTable.addCell(new Phrase("", font));
                }

                if (r.getEnd_dt() != null) {
                    blockesfTable.addCell(new Phrase(r.getEnd_dt().toString(), font));
                } else {
                    blockesfTable.addCell(new Phrase("", font));
                }

                if (r.getUpdate_dt() != null) {
                    blockesfTable.addCell(new Phrase(r.getUpdate_dt().toString(), font));
                } else {
                    blockesfTable.addCell(new Phrase("", font));
                }

                number++;
            }

            document.add(blockesfTable);
        }
        //СВЯЗИ С ЮЛ

        //НДС
        List<NdsEntity> ndsEntities = result.getNdsEntities();
        if (ndsEntities != null && ndsEntities.size()!=0) {
            PdfPTable ndsTable = new PdfPTable(5);
            ndsTable.setWidthPercentage(100f);
            ndsTable.setWidths(new float[] {0.15f, 1, 1, 1, 1});
            ndsTable.setSpacingBefore(5);
            heading.setColspan(5);
            heading.setPhrase(new Phrase("НДС", font));
            ndsTable.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            ndsTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата начала", font));
            ndsTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата конца", font));
            ndsTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата обновления", font));
            ndsTable.addCell(cell);
            cell.setPhrase(new Phrase("Причина", font));
            ndsTable.addCell(cell);
            int number = 1;
            for (NdsEntity r : ndsEntities) {
                ndsTable.addCell(new Phrase(number + "", font));

                if (r.getStartDt() != null) {
                    ndsTable.addCell(new Phrase(r.getStartDt().toString(), font));
                } else {
                    ndsTable.addCell(new Phrase("", font));
                }

                try {
                    ndsTable.addCell(new Phrase(r.getEndDt().toString(), font));
                } catch (Exception e) {
                    ndsTable.addCell(new Phrase("", font));
                }

                if (r.getUpdateDt() != null) {
                    ndsTable.addCell(new Phrase(r.getUpdateDt().toString(), font));
                } else {
                    ndsTable.addCell(new Phrase("", font));
                }

                try {
                    ndsTable.addCell(new Phrase(r.getReason(), font));
                } catch (Exception e) {
                    ndsTable.addCell(new Phrase("", font));
                }

                number++;
            }

            document.add(ndsTable);
        }
        //СВЕДЕНИЯ ИПГО
        List<IpgoEmailEntity> ipgoEmailEntities = result.getIpgoEmailEntities();
        if (ipgoEmailEntities != null && !ipgoEmailEntities.isEmpty()) {
            PdfPTable ipgoTable = new PdfPTable(4);
            ipgoTable.setWidthPercentage(100f);
            ipgoTable.setWidths(new float[] {0.15f, 1, 1, 1});
            ipgoTable.setSpacingBefore(5);
            heading.setColspan(4);
            heading.setPhrase(new Phrase("Сведения по ИПГО", font));
            ipgoTable.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            ipgoTable.addCell(cell);
            cell.setPhrase(new Phrase("Департамент", font));
            ipgoTable.addCell(cell);
            cell.setPhrase(new Phrase("Должность", font));
            ipgoTable.addCell(cell);
            cell.setPhrase(new Phrase("ИПГО почта", font));
            ipgoTable.addCell(cell);
            int number = 1;
            for (IpgoEmailEntity r : ipgoEmailEntities) {
                ipgoTable.addCell(new Phrase(number + "", font));

                if (r.getOrgan() != null) {
                    ipgoTable.addCell(new Phrase(r.getOrgan().toString(), font));
                } else {
                    ipgoTable.addCell(new Phrase("", font));
                }

                try {
                    ipgoTable.addCell(new Phrase(r.getPosition(), font));
                } catch (Exception e) {
                    ipgoTable.addCell(new Phrase("", font));
                }

                if (r.getEmail() != null) {
                    ipgoTable.addCell(new Phrase(r.getEmail().toString(), font));
                } else {
                    ipgoTable.addCell(new Phrase("", font));
                }

                number++;
            }
            document.add(ipgoTable);
        }
        List<FlRelativiesDTO> flRelativiesDTOS = myService.getRelativesInfo(result.getMvIinDocs().get(0).getIin());
        if (flRelativiesDTOS != null && !flRelativiesDTOS.isEmpty()) {
            PdfPTable docTable = new PdfPTable(7);
            docTable.setWidthPercentage(100f);
            docTable.setWidths(new float[] {1, 1, 1, 1, 1, 1, 1});
            docTable.setSpacingBefore(5);
            heading.setColspan(7);
            heading.setPhrase(new Phrase("Наименование коллапса: \"Сведения о родственных связях данного ФЛ\" Количество найденных инф: " + flRelativiesDTOS.size(), font));
            docTable.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Статус по отношению к родственнику", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("ФИО", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата регистрации брака", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата расторжения брака", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("ИИН родственника", font));
            docTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата рождения", font));
            docTable.addCell(cell);
            int number = 1;
            for (FlRelativiesDTO flRelativiesDTO : flRelativiesDTOS) {
                docTable.addCell(new Phrase(String.valueOf(number), font));
                docTable.addCell(new Phrase(flRelativiesDTO.getRelative_type() != null ? flRelativiesDTO.getRelative_type() : "", font));
                docTable.addCell(new Phrase(flRelativiesDTO.getParent_fio() != null ? flRelativiesDTO.getParent_fio() : "", font));
                docTable.addCell(new Phrase(flRelativiesDTO.getMarriage_reg_date() != null ? flRelativiesDTO.getMarriage_reg_date().toString() : "", font));
                docTable.addCell(new Phrase(flRelativiesDTO.getMarriage_divorce_date() != null ? flRelativiesDTO.getMarriage_divorce_date().toString() : "", font));
                docTable.addCell(new Phrase(flRelativiesDTO.getParent_iin() != null ? flRelativiesDTO.getParent_iin() : "", font));
                docTable.addCell(new Phrase(flRelativiesDTO.getParent_birth_date() != null ? flRelativiesDTO.getParent_birth_date() : "", font));
                number++;
            }
            document.add(docTable);
        }
        return document;
    }

    public Document generateNew(NodesFL result, ByteArrayOutputStream response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response);
        document.open();
        //com/example/backend/tools/fontstimes.ttf
        // C:/Users/user/Desktop/SIP/SID-superset-itap-dossier/back-end/src/main/java/com/example/backend/tools/
        BaseFont baseFont = BaseFont.createFont("/fonts/fontstimes.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(baseFont);
        font.setColor(CMYKColor.WHITE);
        PdfPCell heading = new PdfPCell();
        heading.setBackgroundColor(CMYKColor.GRAY);
        heading.setHorizontalAlignment(Element.ALIGN_CENTER);
        PdfPCell cell = new PdfPCell();

        List<MvFl> mvFls = result.getMvFls();
        if (mvFls.get(0) != null && mvFls != null) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList(
                    "Фото",
                    "ИИН",
                    "ФИО",
                    "Резидент",
                    "Национальность",
                    "Дата смерти"
            ));

            PdfPTable table = tableHandler(document, "Сведения о физическом лице", columnHeaders, font);

            for (MvFl a : mvFls) {
                try {
                    table.addCell(Image.getInstance(result.getPhotoDbf().get(0).getPhoto()));
                } catch (Exception e) {
                    table = addCell(table, font, "");
                }
                table = addCell(table, font, a.getIin());
                table = addCell(table, font, a.getLast_name() + "\n" + a.getFirst_name() + "\n" + a.getPatronymic());
                table = addCell(table, font, a.getCitizenship_ru_name());
                table = addCell(table, font, a.getNationality_ru_name());
                table = addCell(table, font, a.getDeath_date());
            }
            document.add(table);
        }
        List<RegAddressFl> addressFls = result.getRegAddressFls();
        if (addressFls != null && !addressFls.isEmpty()) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList(
                    "Страна",
                    "Город",
                    "Улица, дом, квартира",
                    "Район",
                    "Дата регистрации"
            ));
            PdfPTable table = tableHandler(document, "Адреса прописки", columnHeaders, font);

            for (RegAddressFl ar : addressFls) {
                table = addCell(table, font, ar.getCountry());
                table = addCell(table, font, ar.getDistrict());
                table = addCell(table, font, ar.getStreet() + ", " + ar.getBuilding() + ", " + ar.getApartment_number());
                table = addCell(table, font, ar.getRegion());
                table = addCell(table, font, ar.getReg_date());
            }
            document.add(table);
        }
        List<MvIinDoc> docs = result.getMvIinDocs();
        if (docs != null && !docs.isEmpty()) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList(
                    "Тип Документа",
                    "Орган выдачи",
                    "Дата выдачи",
                    "Срок до",
                    "Номер документа"
            ));
            PdfPTable table = tableHandler(document, "Документы", columnHeaders, font);

            for (MvIinDoc r : docs) {
                table = addCell(table, font, r.getDoc_type_ru_name());
                table = addCell(table, font, r.getIssue_organization_ru_name());
                try {
                    table = addCell(table, font, r.getIssue_date().toString());
                } catch (Exception e) {
                    table = addCell(table, font, "");
                }
                try {
                    table = addCell(table, font, r.getExpiry_date().toString());
                } catch (Exception e) {
                    table = addCell(table, font, "");
                }
                table = addCell(table, font, r.getDoc_number());
            }

            document.add(table);
        }
        List<School> schools = result.getSchools();
        if (schools != null && !schools.isEmpty()) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList(
                    "БИН",
                    "Название школы",
                    "Класс",
                    "Дата поступления",
                    "Дата окончания"
            ));
            PdfPTable table = tableHandler(document, "Школы", columnHeaders, font);
            for (School a : schools) {
                table = addCell(table, font, a.getSchool_code());
                table = addCell(table, font, a.getSchool_name());
                table = addCell(table, font, a.getGrade());
                try {
                    table = addCell(table, font, a.getStart_date().toString());
                } catch (Exception e) {
                    table = addCell(table, font, "");
                }
                try {
                    table = addCell(table, font, a.getEnd_date().toString());
                } catch (Exception e) {
                    table = addCell(table, font, "");
                }
            }
            document.add(table);
        }
        List<Universities> universities = result.getUniversities();
        if (universities.size()!=0 && universities != null) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList(
                    "БИН",
                    "Название вуза",
                    "Специализация",
                    "Дата поступления",
                    "Дата окончания",
                    "Длительность обучения",
                    "Курс"
            ));
            PdfPTable table = tableHandler(document, "Вузы", columnHeaders, font);

            for (Universities r : universities) {
                table = addCell(table, font, r.getStudy_code());
                table = addCell(table, font, r.getStudy_name());
                table = addCell(table, font, r.getSpec_name());
                try {
                    table = addCell(table, font, r.getStart_date().toString());
                } catch (Exception e) {
                    table = addCell(table, font, "");
                }
                try {
                    table = addCell(table, font, r.getEnd_date().toString());
                } catch (Exception e) {
                    table = addCell(table, font, "");
                }
                table = addCell(table, font, r.getDuration());
                table = addCell(table, font, r.getCourse());
            }

            document.add(table);
        }

        //ТРАНСПОРТ ФЛ
        List<MvAutoFl> autos = result.getMvAutoFls();
        if (autos.size()!=0 && autos != null) {
            PdfPTable autoTable = new PdfPTable(10);
            autoTable.setWidthPercentage(100f);
            autoTable.setWidths(new float[] {0.4f, 1, 1, 1, 1, 1, 1, 1, 1, 1});
            autoTable.setSpacingBefore(5);
            heading.setColspan(10);
            heading.setPhrase(new Phrase("Наименование коллапса: \"Транспорт\" Количество найденных инф: " + autos.size(), font));
            autoTable.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            autoTable.addCell(cell);
            cell.setPhrase(new Phrase("Статус", font));
            autoTable.addCell(cell);
            cell.setPhrase(new Phrase("Регистрационный номер", font));
            autoTable.addCell(cell);
            cell.setPhrase(new Phrase("Марка модель", font));
            autoTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата выдачи свидетельства", font));
            autoTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата снятия", font));
            autoTable.addCell(cell);
            cell.setPhrase(new Phrase("Год выпуска", font));
            autoTable.addCell(cell);
            cell.setPhrase(new Phrase("Категория", font));
            autoTable.addCell(cell);
            cell.setPhrase(new Phrase("VIN/Кузов/Шосси", font));
            autoTable.addCell(cell);
            cell.setPhrase(new Phrase("Серия", font));
            autoTable.addCell(cell);
            int number = 1;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            for (MvAutoFl r : autos) {
                if (r != null) {
                    autoTable.addCell(new Phrase(number + "", font));

                    try {
                        if (r.isIs_registered()) {
                            autoTable.addCell(new Phrase("Текущий", font));
                        } else {
                            autoTable.addCell(new Phrase("Исторический", font));
                        }
                    } catch (Exception e) {
                        autoTable.addCell(new Phrase("", font));
                    }

                    try {
                        autoTable.addCell(new Phrase(r.getReg_number(), font));
                    } catch (Exception e) {
                        autoTable.addCell(new Phrase("", font));
                    }

                    try {
                        autoTable.addCell(new Phrase(r.getBrand_model(), font));
                    } catch (Exception e) {
                        autoTable.addCell(new Phrase("", font));
                    }

                    try {
                        autoTable.addCell(new Phrase(r.getDate_certificate().toString(), font));
                    } catch (Exception e) {
                        autoTable.addCell(new Phrase("", font));
                    }

                    try {
                        autoTable.addCell(new Phrase(r.getEnd_date().toString(), font));
                    } catch (Exception e) {
                        autoTable.addCell(new Phrase("", font));
                    }

                    autoTable.addCell(new Phrase(r.getRelease_year_tc(), font));
                    autoTable.addCell(new Phrase(r.getCategory_control_tc(), font));
                    autoTable.addCell(new Phrase(r.getVin_kuzov_shassi(), font));
                    autoTable.addCell(new Phrase(r.getSeries_reg_number(), font));
                    number++;
                }
            }

            document.add(autoTable);
        }
        //РОДСТВЕННЫЕ СВЯЗИ
//        List<FlRelativiesDTO> fl_relatives = result.getFl_relatives();
//        if (fl_relatives.size()!=0 && fl_relatives != null) {
//            PdfPTable relatives = new PdfPTable(7);
//            relatives.setWidthPercentage(100f);
//            relatives.setWidths(new float[] {0.15f, 1, 1, 1, 1, 1, 1});
//            relatives.setSpacingBefore(5);
//            heading.setColspan(7);
//            heading.setPhrase(new Phrase("Родственные связи", font));
//            relatives.addCell(heading);
//            cell.setPhrase(new Phrase("№", font));
//            relatives.addCell(cell);
//            cell.setPhrase(new Phrase("Статус по отношению к родственнику", font));
//            relatives.addCell(cell);
//            cell.setPhrase(new Phrase("ФИО", font));
//            relatives.addCell(cell);
//            cell.setPhrase(new Phrase("ИИН", font));
//            relatives.addCell(cell);
//            cell.setPhrase(new Phrase("Дата рождения", font));
//            relatives.addCell(cell);
//            cell.setPhrase(new Phrase("Дата регистрации брака", font));
//            relatives.addCell(cell);
//            cell.setPhrase(new Phrase("Дата расторжения брака", font));
//            relatives.addCell(cell);
//            int number = 1;
//            for (FlRelativiesDTO r : fl_relatives) {
//                relatives.addCell(new Phrase(number + "", font));
//                relatives.addCell(new Phrase(r.getRelative_type(), font));
//                relatives.addCell(new Phrase(r.getParent_fio(), font));
//
//                if (r.getParent_iin() != null) {
//                    relatives.addCell(new Phrase(r.getParent_iin(), font));
//                } else {
//                    relatives.addCell(new Phrase("", font));
//                }
//
//                if (r.getParent_birth_date() != null) {
//                    relatives.addCell(new Phrase(r.getParent_birth_date().substring(0, 10), font));
//                } else {
//                    relatives.addCell(new Phrase("", font));
//                }
//
//                relatives.addCell(new Phrase(r.getMarriage_reg_date(), font));
//                relatives.addCell(new Phrase(r.getMarriage_divorce_date(), font));
//
//                number++;
//            }
//
//            document.add(relatives);
//        }
        //КОНТАКТНЫЕ ДАННЫЕ ФЛ
        List<FlContacts> contacts = result.getContacts();
        if (contacts.size()!= 0 && contacts != null) {
            PdfPTable contactsTable = new PdfPTable(4);
            contactsTable.setWidthPercentage(100f);
            contactsTable.setWidths(new float[] {0.15f, 1, 1, 1});
            contactsTable.setSpacingBefore(5);
            heading.setColspan(4);
            heading.setPhrase(new Phrase("Контактные данные ФЛ", font));
            contactsTable.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            contactsTable.addCell(cell);
            cell.setPhrase(new Phrase("Телефон", font));
            contactsTable.addCell(cell);
            cell.setPhrase(new Phrase("Почта", font));
            contactsTable.addCell(cell);
            cell.setPhrase(new Phrase("Источник", font));
            contactsTable.addCell(cell);
            int number = 1;
            for (FlContacts r : contacts) {
                contactsTable.addCell(new Phrase(number + "", font));
                contactsTable.addCell(new Phrase(r.getPhone(), font));
                contactsTable.addCell(new Phrase(r.getEmail(), font));
                contactsTable.addCell(new Phrase(r.getSource(), font));
                number++;
            }
            document.add(contactsTable);
        }
        //ВОЕННЫЙ УЧЕТ
        List<MillitaryAccount> millitaryAccounts = result.getMillitaryAccounts();
        if (millitaryAccounts.size() != 0 && millitaryAccounts != null) {
            PdfPTable MATable = new PdfPTable(4);
            MATable.setWidthPercentage(100f);
            MATable.setWidths(new float[] {0.4f, 1, 1, 1});
            MATable.setSpacingBefore(5);
            heading.setColspan(4);
            heading.setPhrase(new Phrase("Войнский учет", font));
            MATable.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            MATable.addCell(cell);
            cell.setPhrase(new Phrase("БИН воинской части", font));
            MATable.addCell(cell);
            cell.setPhrase(new Phrase("Дата службы с", font));
            MATable.addCell(cell);
            cell.setPhrase(new Phrase("Дата службы по", font));
            MATable.addCell(cell);
            int number = 1;
            for (MillitaryAccount r : millitaryAccounts) {
                MATable.addCell(new Phrase(number + "", font));
                MATable.addCell(new Phrase(r.getBin(), font));
                MATable.addCell(new Phrase(r.getDate_start(), font));
                MATable.addCell(new Phrase(r.getDate_end(), font));
                number++;
            }
            document.add(MATable);
        }
        //ОПРАВДАННЫЕ ПРЕСТУПЛЕНИЯ
        List<ConvictsJustified> convictsJustifieds = result.getConvictsJustifieds();
        if (convictsJustifieds.size() != 0 && convictsJustifieds != null) {
            PdfPTable convicts = new PdfPTable(6);
            convicts.setWidthPercentage(100f);
            convicts.setWidths(new float[] {0.4f, 1, 1, 1, 1, 1});
            convicts.setSpacingBefore(5);
            heading.setColspan(6);
            heading.setPhrase(new Phrase("Наименование риска: \"Осужденные\" Количество найденных инф: " + convictsJustifieds.size(), font));
            convicts.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            convicts.addCell(cell);
            cell.setPhrase(new Phrase("Дата рассмотрения в суде 1 инстанции", font));
            convicts.addCell(cell);
            cell.setPhrase(new Phrase("Суд 1 инстанции", font));
            convicts.addCell(cell);
            cell.setPhrase(new Phrase("Решение по лицу", font));
            convicts.addCell(cell);
            cell.setPhrase(new Phrase("Мера наказания по договору", font));
            convicts.addCell(cell);
            cell.setPhrase(new Phrase("Квалификация", font));
            convicts.addCell(cell);
            int number = 1;
            for (ConvictsJustified r : convictsJustifieds) {
                convicts.addCell(new Phrase(number + "", font));
                convicts.addCell(new Phrase(r.getReg_date(), font));
                convicts.addCell(new Phrase(r.getCourt_of_first_instance(), font));

                if (r.getDecision_on_person() != null) {
                    convicts.addCell(new Phrase(r.getDecision_on_person(), font));
                } else {
                    convicts.addCell(new Phrase("", font));
                }

                if (r.getMeasure_punishment() != null) {
                    convicts.addCell(new Phrase(r.getMeasure_punishment(), font));
                } else {
                    convicts.addCell(new Phrase("", font));
                }

                convicts.addCell(new Phrase(r.getQualification(), font));

                number++;
            }

            document.add(convicts);
        }
        //ЕЩЕ КАКИЕТО ПРЕСТУЛПЛЕНИЯ
        List<ConvictsTerminatedByRehab> convictsTerminatedByRehabs = result.getConvictsTerminatedByRehabs();
        if (convictsTerminatedByRehabs.size()!=0 && convictsTerminatedByRehabs != null) {
            PdfPTable ctbrTable = new PdfPTable(6);
            ctbrTable.setWidthPercentage(100f);
            ctbrTable.setWidths(new float[] {0.4f, 1, 1, 1, 1, 1});
            ctbrTable.setSpacingBefore(5);
            heading.setColspan(6);
            heading.setPhrase(new Phrase("Административные штрафы", font));
            ctbrTable.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            ctbrTable.addCell(cell);
            cell.setPhrase(new Phrase("Орган выявивший правонарушение", font));
            ctbrTable.addCell(cell);
            cell.setPhrase(new Phrase("Дата заведения", font));
            ctbrTable.addCell(cell);
            cell.setPhrase(new Phrase("Квалификация", font));
            ctbrTable.addCell(cell);
            cell.setPhrase(new Phrase("Решение", font));
            ctbrTable.addCell(cell);
            cell.setPhrase(new Phrase("Уровень тяжести", font));
            ctbrTable.addCell(cell);
            int number = 1;
            for (ConvictsTerminatedByRehab r : convictsTerminatedByRehabs) {
                ctbrTable.addCell(new Phrase(number + "", font));
                ctbrTable.addCell(new Phrase(r.getInvestigative_authority(), font));
                ctbrTable.addCell(new Phrase(r.getLast_solution_date(), font));
                ctbrTable.addCell(new Phrase(r.getQualification_desc(), font));
                ctbrTable.addCell(new Phrase(r.getLast_solution(), font));
                ctbrTable.addCell(new Phrase(r.getQualification_by_11(), font));
                number++;
            }
            document.add(ctbrTable);
        }
        //СВЕДЕНИЯ ИПГО
        List<IpgoEmailEntity> ipgoEmailEntities = result.getIpgoEmailEntities();
        if (ipgoEmailEntities.size() != 0 && ipgoEmailEntities != null) {
            PdfPTable ipgoTable = new PdfPTable(4);
            ipgoTable.setWidthPercentage(100f);
            ipgoTable.setWidths(new float[] {0.15f, 1, 1, 1});
            ipgoTable.setSpacingBefore(5);
            heading.setColspan(4);
            heading.setPhrase(new Phrase("Сведения по ИПГО", font));
            ipgoTable.addCell(heading);
            cell.setPhrase(new Phrase("№", font));
            ipgoTable.addCell(cell);
            cell.setPhrase(new Phrase("Департамент", font));
            ipgoTable.addCell(cell);
            cell.setPhrase(new Phrase("Должность", font));
            ipgoTable.addCell(cell);
            cell.setPhrase(new Phrase("ИПГО почта", font));
            ipgoTable.addCell(cell);
            int number = 1;
            for (IpgoEmailEntity r : ipgoEmailEntities) {
                ipgoTable.addCell(new Phrase(number + "", font));

                if (r.getOrgan() != null) {
                    ipgoTable.addCell(new Phrase(r.getOrgan().toString(), font));
                } else {
                    ipgoTable.addCell(new Phrase("", font));
                }

                try {
                    ipgoTable.addCell(new Phrase(r.getPosition(), font));
                } catch (Exception e) {
                    ipgoTable.addCell(new Phrase("", font));
                }

                if (r.getEmail() != null) {
                    ipgoTable.addCell(new Phrase(r.getEmail().toString(), font));
                } else {
                    ipgoTable.addCell(new Phrase("", font));
                }

                number++;
            }
            document.add(ipgoTable);
        }

        List<MvUlFounderFl> mvUlFounderFls = result.getMvUlFounderFls();
        if (mvUlFounderFls.size()!=0 && mvUlFounderFls!=null) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList("№",
                    "БИН",
                    "Наименование ЮЛ",
                    "Дата регистрации"
            ));
            PdfPTable table = tableHandler(document, "Сведения об участниках ЮЛ", columnHeaders, font);

            int number = 1;
            for (MvUlFounderFl a : mvUlFounderFls) {
                table = addCell(table, font, number + "");
                table = addCell(table, font, a.getBin_org());
                try {
                    String name = mvUlRepo.getNameByBin(a.getBin_org());
                    table = addCell(table, font, name);
                } catch (Exception e){
                    table = addCell(table, font, "Нет");
                }
                try {
                    String regDate = a.getReg_date().toString();
                    table = addCell(table, font, regDate);
                } catch (Exception e) {
                    table = addCell(table, font, "Нет даты");
                }
                number++;
            }

            document.add(table);
        }
        List<AccountantListEntity> accountantListEntities = result.getAccountantListEntities();
        if (accountantListEntities.size() != 0 && accountantListEntities != null ) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList("ИИН",
                    "Проф.",
                    "Фамилия",
                    "Имя"
            ));
            PdfPTable table = tableHandler(document, "Accountant List Entity", columnHeaders, font);

            for (AccountantListEntity a : accountantListEntities) {
                table = addCell(table, font, a.getIin());
                table = addCell(table, font, a.getProf());
                table = addCell(table, font, a.getLname());
                table = addCell(table, font, a.getFname());
            }

            document.add(table);
        }
        List<Omn> omns = result.getOmns();
        if (omns.size() != 0 && omns != null) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList("rnn",
                    "taxpayer_name",
                    "taxpayer_fio",
                    "leader_fio",
                    "leader_iin",
                    "leader_rnn"
            ));
            PdfPTable table = tableHandler(document, "omns", columnHeaders, font);

            for (Omn a : omns) {
                table = addCell(table, font, a.getRnn());
                table = addCell(table, font, a.getTaxpayer_name());
                table = addCell(table, font, a.getTaxpayer_fio());
                table = addCell(table, font, a.getLeader_fio());
                table = addCell(table, font, a.getLeader_iin());
                table = addCell(table, font, a.getLeader_rnn());
            }

            document.add(table);
        }
        List<Equipment> equipmentList = result.getEquipment();
        if (equipmentList.size() != 0 && equipmentList != null) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList("Адрес",
                    "Гос. Номер",
                    "Номер серии рег.",
                    "Дата регистрации",
                    "Причина",
                    "VIN",
                    "Спец.",
                    "Тип",
                    "Форма",
                    "Брэнд",
                    "Модель"));
            PdfPTable table = tableHandler(document, "Транспорты", columnHeaders, font);

            for (Equipment a : equipmentList) {
                table = addCell(table, font, a.getOwner_address());
                table = addCell(table, font, a.getGov_number());
                table = addCell(table, font, a.getReg_series_num());
                table = addCell(table, font, a.getReg_date());
                table = addCell(table, font, a.getReg_reason());
                table = addCell(table, font, a.getVin());
                table = addCell(table, font, a.getEquipment_spec());
                table = addCell(table, font, a.getEquipment_type());
                table = addCell(table, font, a.getEquipment_form());
                table = addCell(table, font, a.getBrand());
                table = addCell(table, font, a.getEquipment_model());
            }
            document.add(table);
        }
        List<Msh> mshes = result.getMshes();
        if (mshes.size() != 0 && mshes != null) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList(
                    "Тип",
                    "Модель",
                    "VIN",
                    "Гос. Номер",
                    "Дата регистрации"
            ));
            PdfPTable table = tableHandler(document, "Транспорты", columnHeaders, font);

            for (Msh a : mshes) {
                table = addCell(table, font, a.getEquipmentType());
                table = addCell(table, font, a.getEquipmentModel());
                table = addCell(table, font, a.getVin());
                table = addCell(table, font, a.getGovNumber());
                try {
                    table = addCell(table, font, a.getRegDate().toString());
                } catch (Exception e) {
                    table = addCell(table, font, "Нет");
                }
            }
        }
        List<Dormant> dormans = result.getDormants();
        if (dormans.size() != 0 && dormans != null) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList(
                    "РНН",             // rnn
                    "Название налогоплательщика",  // taxpayer_name
                    "ФИО налогоплательщика",      // taxpayer_fio
                    "ФИО руководителя",           // leader_fio
                    "ИИН руководителя",           // leader_iio
                    "РНН руководителя",           // leader_rnn
                    "Дата заказа"                 // order_date
            ));
            PdfPTable table = tableHandler(document, "Дорманы", columnHeaders, font);

            for (Dormant a : dormans) {
                table = addCell(table, font, a.getRnn());
                table = addCell(table, font, a.getTaxpayer_name());
                table = addCell(table, font, a.getTaxpayer_fio());
                table = addCell(table, font, a.getLeader_fio());
                table = addCell(table, font, a.getLeader_iin());
                table = addCell(table, font, a.getLeader_rnn());
                table = addCell(table, font, a.getOrder_date());
            }
            document.add(table);
        }
        List<Bankrot> bankrots = result.getBankrots();
        if (bankrots.size() != 0 && bankrots != null) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList("Документ", "Дата обновления", "Причина"));
            PdfPTable table = tableHandler(document, "Банкроты", columnHeaders, font);
            for (Bankrot a : bankrots) {
                table = addCell(table, font, a.getDocument());
                try {
                    table = addCell(table, font, a.getUpdate_dt().toString());
                } catch (Exception e) {
                    table = addCell(table, font, "Дата отсутствует");
                }
                table = addCell(table, font, a.getReason());
            }
            document.add(table);

        }
        List<Adm> adms = result.getAdms();
        if (adms.size() != 0 && adms != null) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList(
                    "Номер материала",       // material_num
                    "Дата регистрации",      // reg_date
                    "15",            // fifteen
                    "16",           // sixteen
                    "17",            // seventeen
                    "Наименование юр. лица", // ul_org_name
                    "Адрес юр. лица",        // ul_adress
                    "Марка автомобиля",      // vehicle_brand
                    "Гос. Номер авто"        // state_auto_num
            ));
            PdfPTable table = tableHandler(document, "Администрация", columnHeaders, font);
            for (Adm a : adms) {
                table = addCell(table, font, a.getMaterial_num());
                table = addCell(table, font, a.getReg_date());
                table = addCell(table, font, a.getFifteen());
                table = addCell(table, font, a.getSixteen());
                table = addCell(table, font, a.getSeventeen());
                table = addCell(table, font, a.getUl_org_name());
                table = addCell(table, font, a.getUl_adress());
                table = addCell(table, font, a.getVehicle_brand());
                table = addCell(table, font, a.getState_auto_num());
            }
            document.add(table);
        }
        List<Criminals> criminals = result.getCriminals();
        if (criminals != null && criminals.size() != 0) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList(
                    "Наименование суда",    // court_name
                    "Дата судебного решения",  // court_dt
                    "Решение",             // decision
                    "Название преступления",   // crime_name
                    "Приговор",            // sentence
                    "Дополнительная информация", // add_info
                    "Обращение",           // treatment
                    "ЕРДР"                 // erdr
            ));
            PdfPTable table = tableHandler(document, "Преступления", columnHeaders, font);
            for (Criminals a : criminals) {
                table = addCell(table, font, a.getCourt_name());
                table = addCell(table, font, a.getCourt_dt());
                table = addCell(table, font, a.getDecision());
                table = addCell(table, font, a.getCrime_name());
                table = addCell(table, font, a.getSentence());
                table = addCell(table, font, a.getAdd_info());
                table = addCell(table, font, a.getTreatment());
                table = addCell(table, font, a.getErdr());
            }
            document.add(table);
        }
        List<BlockEsf> blockEsfs = result.getBlockEsfs();
        if (blockEsfs != null && blockEsfs.size() != 0) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList(
                    "Дата начала",    // start_dt
                    "Дата окончания", // end_dt
                    "Дата обновления" // update_dt
            ));
            PdfPTable table = tableHandler(document, "Блокировка ЕСФ", columnHeaders, font);
            for (BlockEsf a : blockEsfs) {
                try {
                    table = addCell(table, font, a.getStart_dt().toString());
                } catch (Exception e) {
                    table = addCell(table, font, "Нет");
                }
                try {
                    table = addCell(table, font, a.getEnd_dt().toString());
                } catch (Exception e) {
                    table = addCell(table, font, "Нет");
                }
                try {
                    table = addCell(table, font, a.getUpdate_dt().toString());
                } catch (Exception e) {
                    table = addCell(table, font, "Нет");
                }
            }
            document.add(table);
        }
        List<NdsEntity> ndsEntities = result.getNdsEntities();
        if (ndsEntities != null && ndsEntities.size() != 0) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList(
                    "startDt",
                    "endDt",
                    "reason",
                    "updateDt"
            ));
            PdfPTable table = tableHandler(document, "ndsEntities", columnHeaders, font);
            for (NdsEntity a : ndsEntities) {
                try {
                    table = addCell(table, font, a.getStartDt().toString());
                } catch (Exception e) {
                    table = addCell(table, font, "Нет");
                }
                try {
                    table = addCell(table, font, a.getEndDt().toString());
                } catch (Exception e) {
                    table = addCell(table, font, "Нет");
                }
                table = addCell(table, font, a.getReason());
                try {
                    table = addCell(table, font, a.getUpdateDt().toString());
                } catch (Exception e) {
                    table = addCell(table, font, "Нет");
                }
            }
            document.add(table);
        }
        List<MvRnOld> mvRnOlds = result.getMvRnOlds();
        if (mvRnOlds != null && mvRnOlds.size() != 0) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList(
                    "intended_use_rus",
                    "estate_status_rus",
                    "address_rus",
                    "address_history_rus",
                    "type_of_property_rus",
                    "property_type_rus",
                    "estate_characteristic_status_rus",
                    "register_reg_date",
                    "register_end_date",
                    "register_emergence_rights_rus",
                    "register_status_rus"
            ));
            PdfPTable table = tableHandler(document, "ndsEntities", columnHeaders, font);
            for (MvRnOld a : mvRnOlds) {
                table = addCell(table, font, a.getIntended_use_rus());
                table = addCell(table, font, a.getEstate_status_rus());
                table = addCell(table, font, a.getAddress_rus());
                table = addCell(table, font, a.getAddress_history_rus());
                table = addCell(table, font, a.getType_of_property_rus());
                table = addCell(table, font, a.getProperty_type_rus());
                table = addCell(table, font, a.getEstate_characteristic_status_rus());
                table = addCell(table, font, a.getRegister_reg_date());
                table = addCell(table, font, a.getRegister_end_date());
                table = addCell(table, font, a.getRegister_emergence_rights_rus());
                table = addCell(table, font, a.getRegister_status_rus());
            }
            document.add(table);
        }
        List<Pdl> pdls = result.getPdls();
        if (pdls != null && pdls.size() != 0) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList(
                    "iin",
                    "organization_fullname",
                    "fio",
                    "organ",
                    "oblast",
                    "spouse_fio",
                    "spouse_organ",
                    "spouse_position",
                    "spouse_iin"
            ));
            PdfPTable table = tableHandler(document, "pdl", columnHeaders, font);
            for (Pdl a : pdls) {
                table = addCell(table, font, a.getIin());
                table = addCell(table, font, a.getOrganization_fullname());
                table = addCell(table, font, a.getFio());
                table = addCell(table, font, a.getOrgan());
                table = addCell(table, font, a.getOblast());
                table = addCell(table, font, a.getSpouse_fio());
                table = addCell(table, font, a.getSpouse_organ());
                table = addCell(table, font, a.getSpouse_position());
                table = addCell(table, font, a.getSpouse_iin());
            }
            document.add(table);
        }
        List<CommodityProducer> commodityProducers = result.getCommodityProducers();
        if (commodityProducers != null && commodityProducers.size() != 0) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList(
                    "sspName",
                    "count",
                    "producer",
                    "status",
                    "region",
                    "sztp"
            ));
            PdfPTable table = tableHandler(document, "pdl", columnHeaders, font);
            for (CommodityProducer a : commodityProducers) {
                table = addCell(table, font, a.getSspName());
                table = addCell(table, font, a.getCount() + "");
                table = addCell(table, font, a.getProducer());
                table = addCell(table, font, a.getStatus());
                table = addCell(table, font, a.getRegion());
                table = addCell(table, font, a.getSztp());
            }
            document.add(table);
        }
        document.close();
        return document;
    }


    public Document generate(NodesUL result, ByteArrayOutputStream response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response);
        document.open();
        //com/example/backend/tools/fontstimes.ttf
        // C:/Users/user/Desktop/SIP/SID-superset-itap-dossier/back-end/src/main/java/com/example/backend/tools/
        getBaseFont();
        Font font = new Font(getBaseFont());
        font.setColor(CMYKColor.WHITE);
        PdfPCell heading = new PdfPCell();
        heading.setBackgroundColor(CMYKColor.GRAY);
        heading.setHorizontalAlignment(Element.ALIGN_CENTER);
        PdfPCell cell = new PdfPCell();

        List<MvUl> mvUl = result.getMvUls();
        if (mvUl != null && mvUl.get(0) != null ) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList("БИН",
                    "Наименование организаци",
                    "Наименование ОКЭД",
                    "Статус ЮЛ"
            ));
            PdfPTable table = tableHandler(document, "Сведения о юридическом лице", columnHeaders, font);

            for (MvUl a : mvUl) {
                table = addCell(table, font, a.getBin());
                table = addCell(table, font, a.getFull_name_kaz());
                table = addCell(table, font, a.getHead_organization());
                table = addCell(table, font, a.getUl_status());
            }
            document.add(table);
        }
        List<MvUlFounderFl> mvUlFounderFls = result.getMvUlFounderFls();
        if (mvUlFounderFls!=null && mvUlFounderFls.size()!=0) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList("№",
                    "БИН",
                    "Наименование ЮЛ",
                    "Дата регистрации"
            ));
            PdfPTable table = tableHandler(document, "Сведения об участниках ЮЛ", columnHeaders, font);

            int number = 1;
            for (MvUlFounderFl a : mvUlFounderFls) {
                table = addCell(table, font, number + "");
                table = addCell(table, font, a.getBin_org());
                try {
                    String name = mvUlRepo.getNameByBin(a.getBin_org());
                    table = addCell(table, font, name);
                } catch (Exception e){
                    table = addCell(table, font, "Нет");
                }
                try {
                    String regDate = a.getReg_date().toString();
                    table = addCell(table, font, regDate);
                } catch (Exception e) {
                    table = addCell(table, font, "Нет даты");
                }
                number++;
            }

            document.add(table);
        }
        List<AccountantListEntity> accountantListEntities = result.getAccountantListEntities();
        if (accountantListEntities != null && accountantListEntities.size() != 0 ) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList(
                    "ИИН",
                    "Проф.",
                    "Фамилия",
                    "Имя"
            ));
            PdfPTable table = tableHandler(document, "Список бухгалтеров", columnHeaders, font);

            for (AccountantListEntity a : accountantListEntities) {
                table = addCell(table, font, a.getIin());
                table = addCell(table, font, a.getProf());
                table = addCell(table, font, a.getLname());
                table = addCell(table, font, a.getFname());
            }

            document.add(table);
        }
        List<Omn> omns = result.getOmns();
        if (omns != null && omns.size() != 0) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList(
                    "РНН",                   // rnn (Taxpayer Identification Number)
                    "Название налогоплательщика", // taxpayer_name (Taxpayer Name)
                    "ФИО налогоплательщика",   // taxpayer_fio (Taxpayer Full Name)
                    "ФИО руководителя",       // leader_fio (Leader Full Name)
                    "ИИН руководителя",       // leader_iin (Leader Individual Identification Number)
                    "РНН руководителя"        // leader_rnn (Leader Taxpayer Identification Number)
            ));
            PdfPTable table = tableHandler(document, "ОМНС", columnHeaders, font);

            for (Omn a : omns) {
                table = addCell(table, font, a.getRnn());
                table = addCell(table, font, a.getTaxpayer_name());
                table = addCell(table, font, a.getTaxpayer_fio());
                table = addCell(table, font, a.getLeader_fio());
                table = addCell(table, font, a.getLeader_iin());
                table = addCell(table, font, a.getLeader_rnn());
            }

            document.add(table);
        }
        List<Equipment> equipmentList = result.getEquipment();
        if (equipmentList != null && equipmentList.size() != 0) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList("Адрес",
                    "Гос. Номер",
                    "Номер серии рег.",
                    "Дата регистрации",
                    "Причина",
                    "VIN",
                    "Спец.",
                    "Тип",
                    "Форма",
                    "Брэнд",
                    "Модель"));
            PdfPTable table = tableHandler(document, "Транспорт", columnHeaders, font);

            for (Equipment a : equipmentList) {
                table = addCell(table, font, a.getOwner_address());
                table = addCell(table, font, a.getGov_number());
                table = addCell(table, font, a.getReg_series_num());
                table = addCell(table, font, a.getReg_date());
                table = addCell(table, font, a.getReg_reason());
                table = addCell(table, font, a.getVin());
                table = addCell(table, font, a.getEquipment_spec());
                table = addCell(table, font, a.getEquipment_type());
                table = addCell(table, font, a.getEquipment_form());
                table = addCell(table, font, a.getBrand());
                table = addCell(table, font, a.getEquipment_model());
            }
            document.add(table);
        }
        List<Msh> mshes = result.getMshes();
        if (mshes != null && mshes.size() != 0) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList(
                    "Тип оборудования",    // equipmentType
                    "Модель оборудования", // equipmentModel
                    "VIN",                 // vin
                    "Гос. номер",          // govNumber
                    "Дата регистрации"     // regDate
            ));
            PdfPTable table = tableHandler(document, "МШЭС", columnHeaders, font);

            for (Msh a : mshes) {
                table = addCell(table, font, a.getEquipmentType());
                table = addCell(table, font, a.getEquipmentModel());
                table = addCell(table, font, a.getVin());
                table = addCell(table, font, a.getGovNumber());
                try {
                    table = addCell(table, font, a.getRegDate().toString());
                } catch (Exception e) {
                    table = addCell(table, font, "Нет");
                }
            }
        }
        List<Dormant> dormans = result.getDormants();
        if (dormans != null && dormans.size() != 0) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList(
                    "РНН",                     // rnn (Taxpayer Identification Number)
                    "Название налогоплательщика", // taxpayer_name (Taxpayer Name)
                    "ФИО налогоплательщика",     // taxpayer_fio (Taxpayer Full Name)
                    "ФИО руководителя",         // leader_fio (Leader Full Name)
                    "ИИН руководителя",         // leader_fio (Leader Full Name) - assuming this is a mistake
                    "РНН руководителя",         // leader_rnn (Leader Taxpayer Identification Number)
                    "Дата заказа"               // order_date (Order Date)
            ));
            PdfPTable table = tableHandler(document, "Дорманс", columnHeaders, font);

            for (Dormant a : dormans) {
                table = addCell(table, font, a.getRnn());
                table = addCell(table, font, a.getTaxpayer_name());
                table = addCell(table, font, a.getTaxpayer_fio());
                table = addCell(table, font, a.getLeader_fio());
                table = addCell(table, font, a.getLeader_iin());
                table = addCell(table, font, a.getLeader_rnn());
                table = addCell(table, font, a.getOrder_date());
            }
            document.add(table);
        }
        List<Bankrot> bankrots = result.getBankrots();
        if (bankrots != null && bankrots.size() != 0) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList(
                    "Документ",      // document
                    "Дата обновления",  // update_dt
                    "Причина"          // reason
            ));
            PdfPTable table = tableHandler(document, "Банкроты", columnHeaders, font);
            for (Bankrot a : bankrots) {
                table = addCell(table, font, a.getDocument());
                try {
                    table = addCell(table, font, a.getUpdate_dt().toString());
                } catch (Exception e) {
                    table = addCell(table, font, "Дата отсутствует");
                }
                table = addCell(table, font, a.getReason());
            }
            document.add(table);

        }
        List<Adm> adms = result.getAdms();
        if (adms != null && adms.size() != 0) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList(
                    "Номер материала",       // material_num
                    "Дата регистрации",      // reg_date
                    "15",            // fifteen
                    "16",           // sixteen
                    "17",            // seventeen
                    "Наименование юр. лица", // ul_org_name
                    "Адрес юр. лица",        // ul_adress
                    "Марка автомобиля",      // vehicle_brand
                    "Гос. Номер авто"        // state_auto_num
            ));
            PdfPTable table = tableHandler(document, "Администрация", columnHeaders, font);
            for (Adm a : adms) {
                table = addCell(table, font, a.getMaterial_num());
                table = addCell(table, font, a.getReg_date());
                table = addCell(table, font, a.getFifteen());
                table = addCell(table, font, a.getSixteen());
                table = addCell(table, font, a.getSeventeen());
                table = addCell(table, font, a.getUl_org_name());
                table = addCell(table, font, a.getUl_adress());
                table = addCell(table, font, a.getVehicle_brand());
                table = addCell(table, font, a.getState_auto_num());
            }
            document.add(table);
        }
        List<Criminals> criminals = result.getCriminals();
        if (criminals != null && criminals.size() != 0) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList(
                    "Наименование суда",    // court_name
                    "Дата судебного решения",  // court_dt
                    "Решение",             // decision
                    "Название преступления",   // crime_name
                    "Приговор",            // sentence
                    "Дополнительная информация", // add_info
                    "Обращение",           // treatment
                    "ЕРДР"                 // erdr
            ));
            PdfPTable table = tableHandler(document, "Преступления", columnHeaders, font);
            for (Criminals a : criminals) {
                table = addCell(table, font, a.getCourt_name());
                table = addCell(table, font, a.getCourt_dt());
                table = addCell(table, font, a.getDecision());
                table = addCell(table, font, a.getCrime_name());
                table = addCell(table, font, a.getSentence());
                table = addCell(table, font, a.getAdd_info());
                table = addCell(table, font, a.getTreatment());
                table = addCell(table, font, a.getErdr());
            }
            document.add(table);
        }
        List<BlockEsf> blockEsfs = result.getBlockEsfs();
        if (blockEsfs != null && blockEsfs.size() != 0) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList(
                    "Дата начала",    // start_dt
                    "Дата окончания", // end_dt
                    "Дата обновления" // update_dt
            ));
            PdfPTable table = tableHandler(document, "Блокировка ЕСФ", columnHeaders, font);
            for (BlockEsf a : blockEsfs) {
                try {
                    table = addCell(table, font, a.getStart_dt().toString());
                } catch (Exception e) {
                    table = addCell(table, font, "Нет");
                }
                try {
                    table = addCell(table, font, a.getEnd_dt().toString());
                } catch (Exception e) {
                    table = addCell(table, font, "Нет");
                }
                try {
                    table = addCell(table, font, a.getUpdate_dt().toString());
                } catch (Exception e) {
                    table = addCell(table, font, "Нет");
                }
            }
            document.add(table);
        }
        List<NdsEntity> ndsEntities = result.getNdsEntities();
        if (ndsEntities != null && ndsEntities.size() != 0) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList(
                    "Дата начала",     // startDt
                    "Дата окончания",  // endDt
                    "Причина",         // reason
                    "Дата обновления" // updateDt
            ));
            PdfPTable table = tableHandler(document, "Объекты НДС", columnHeaders, font);
            for (NdsEntity a : ndsEntities) {
                try {
                    table = addCell(table, font, a.getStartDt().toString());
                } catch (Exception e) {
                    table = addCell(table, font, "Нет");
                }
                try {
                    table = addCell(table, font, a.getEndDt().toString());
                } catch (Exception e) {
                    table = addCell(table, font, "Нет");
                }
                table = addCell(table, font, a.getReason());
                try {
                    table = addCell(table, font, a.getUpdateDt().toString());
                } catch (Exception e) {
                    table = addCell(table, font, "Нет");
                }
            }
            document.add(table);
        }
        List<MvRnOld> mvRnOlds = result.getMvRnOlds();
        if (mvRnOlds != null && mvRnOlds.size() != 0) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList(
                    "Назначение использования",           // intended_use_rus
                    "Статус недвижимости",               // estate_status_rus
                    "Адрес",                             // address_rus
                    "История адресов",                   // address_history_rus
                    "Тип собственности",                 // type_of_property_rus
                    "Вид собственности",                 // property_type_rus
                    "Статус характеристики недвижимости", // estate_characteristic_status_rus
                    "Дата регистрации в реестре",        // register_reg_date
                    "Дата окончания регистрации",        // register_end_date
                    "Возникновение права в реестре",     // register_emergence_rights_rus
                    "Статус в реестре"                  // register_status_rus
            ));
            PdfPTable table = tableHandler(document, "mv_rn_old", columnHeaders, font);
            for (MvRnOld a : mvRnOlds) {
                table = addCell(table, font, a.getIntended_use_rus());
                table = addCell(table, font, a.getEstate_status_rus());
                table = addCell(table, font, a.getAddress_rus());
                table = addCell(table, font, a.getAddress_history_rus());
                table = addCell(table, font, a.getType_of_property_rus());
                table = addCell(table, font, a.getProperty_type_rus());
                table = addCell(table, font, a.getEstate_characteristic_status_rus());
                table = addCell(table, font, a.getRegister_reg_date());
                table = addCell(table, font, a.getRegister_end_date());
                table = addCell(table, font, a.getRegister_emergence_rights_rus());
                table = addCell(table, font, a.getRegister_status_rus());
            }
            document.add(table);
        }
        List<FpgTempEntity> fpgTempEntities = result.getFpgTempEntities();
        if (fpgTempEntities != null && fpgTempEntities.size() != 0) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList(
                    "№",
                    "Бенефициар"
            ));
            PdfPTable table = tableHandler(document, "Временные объекты ФПГ", columnHeaders, font);
            int number = 1;
            for (FpgTempEntity a : fpgTempEntities) {
                table = addCell(table, font, number + "");
                table = addCell(table, font, a.getBeneficiary());
                number++;
            }
            document.add(table);
        }
        List<Pdl> pdls = result.getPdls();
        if (pdls != null && pdls.size() != 0) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList(
                    "ИИН",                 // iin (Individual Identification Number)
                    "Полное наименование организации", // organization_fullname (Organization Full Name)
                    "ФИО",                 // fio (Full Name)
                    "Орган",               // organ (Agency)
                    "Область",             // oblast (Region)
                    "ФИО супруг(и)",       // spouse_fio (Spouse Full Name)
                    "Орган супруг(и)",     // spouse_organ (Spouse Agency)
                    "Должность супруга",   // spouse_position (Spouse Position)
                    "ИИН супруга"          // spouse_iin (Spouse Individual Identification Number)
            ));
            PdfPTable table = tableHandler(document, "ПДЛ", columnHeaders, font);
            for (Pdl a : pdls) {
                table = addCell(table, font, a.getIin());
                table = addCell(table, font, a.getOrganization_fullname());
                table = addCell(table, font, a.getFio());
                table = addCell(table, font, a.getOrgan());
                table = addCell(table, font, a.getOblast());
                table = addCell(table, font, a.getSpouse_fio());
                table = addCell(table, font, a.getSpouse_organ());
                table = addCell(table, font, a.getSpouse_position());
                table = addCell(table, font, a.getSpouse_iin());
            }
            document.add(table);
        }
        List<CommodityProducer> commodityProducers = result.getCommodityProducers();
        if (commodityProducers != null && commodityProducers.size() != 0) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList(
                    "Наименование ССП",       // sspName (Name of the Commodity Producer)
                    "Количество",             // count (Count or Quantity)
                    "Производитель",          // producer (Producer)
                    "Статус",                 // status (Status)
                    "Регион",                 // region (Region)
                    "СЗТП"                    // sztp (Type of Commodity)
            ));
            PdfPTable table = tableHandler(document, "Производители товаров", columnHeaders, font);
            for (CommodityProducer a : commodityProducers) {
                table = addCell(table, font, a.getSspName());
                table = addCell(table, font, a.getCount() + "");
                table = addCell(table, font, a.getProducer());
                table = addCell(table, font, a.getStatus());
                table = addCell(table, font, a.getRegion());
                table = addCell(table, font, a.getSztp());
            }
            document.add(table);
        }
        RegAddressUlEntity regAddressUlEntity = result.getRegAddressUlEntities();
        if (regAddressUlEntity != null) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList(
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
            PdfPTable table = tableHandler(document, "Адрес", columnHeaders, font);
            try {
                table = addCell(table, font, regAddressUlEntity.getRegDate().toString());
            } catch (Exception e) {
                table = addCell(table, font, "");
            }
            table = addCell(table, font, regAddressUlEntity.getOrgNameRu());
            table = addCell(table, font, regAddressUlEntity.getRegAddrRegionRu());
            table = addCell(table, font, regAddressUlEntity.getRegAddrDistrictRu());
            table = addCell(table, font, regAddressUlEntity.getRegAddrRuralDistrictRu());
            table = addCell(table, font, regAddressUlEntity.getRegAddrLocalityRu());
            table = addCell(table, font, regAddressUlEntity.getRegAddrStreetRu());
            table = addCell(table, font, regAddressUlEntity.getRegAddrBuildingNum());
            table = addCell(table, font, regAddressUlEntity.getRegAddrBlockNum());
            table = addCell(table, font, regAddressUlEntity.getRegAddrBuildingBodyNum());
            table = addCell(table, font, regAddressUlEntity.getRegAddrOffice());
            table = addCell(table, font, regAddressUlEntity.getOkedNameRu());
            table = addCell(table, font, regAddressUlEntity.getUl_status());
            if (regAddressUlEntity.getActive()) {
                table = addCell(table, font, "Активен");
            } else {
                table = addCell(table, font, "Неактивен");
            }
            document.add(table);
        }
        List<SvedenyaObUchastnikovUlEntity> svedenyaObUchastnikovUlEntities = result.getSvedenyaObUchastnikovUlEntities();
        if (svedenyaObUchastnikovUlEntities != null && svedenyaObUchastnikovUlEntities.size() != 0) {
            List<String> columnHeaders = new ArrayList<>(Arrays.asList(
                    "ФИО или наименование ЮЛ",
                    "Идентификатор",
                    "Дата регистрации",
                    "Риск"
            ));
            PdfPTable table = tableHandler(document, "Сведения об участниках ЮЛ", columnHeaders, font);
            for (SvedenyaObUchastnikovUlEntity a : svedenyaObUchastnikovUlEntities) {
                table = addCell(table, font, a.getFIOorUlName());
                table = addCell(table, font, a.getIdentificator());
                table = addCell(table, font, a.getReg_date());
                table = addCell(table, font, a.getRisk());
            }
            document.add(table);
        }
        document.close();
        return document;
    }
    private PdfPTable addCell(PdfPTable table, Font font, String string) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(CMYKColor.WHITE);
        cell.setPadding(5);
        cell.setPhrase(new Phrase(string, font));
        table.addCell(cell);
        return table;
    }

    private PdfPTable tableHandler(Document document, String header, List<String> columnHeaders, Font font) throws DocumentException {
        Integer columnNumber = columnHeaders.size();
        PdfPTable table = new PdfPTable(columnNumber);
        table.setWidthPercentage(100f);
        float[] widths = new float[columnNumber];
        Arrays.fill(widths, 1);
        table.setWidths(widths);
        table.setSpacingBefore(5);
        PdfPCell heading = new PdfPCell();
        heading.setBackgroundColor(CMYKColor.GRAY);
        heading.setHorizontalAlignment(Element.ALIGN_CENTER);
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(CMYKColor.WHITE);
        cell.setPadding(5);
        heading.setPadding(4);
        heading.setColspan(columnNumber);
        heading.setPhrase(new Phrase(header, font));
        table.addCell(heading);
        font.setColor(CMYKColor.BLACK);

        for (String s : columnHeaders) {
            table = addCell(table, font, s);
        }

        return table;
    }
    public static String getSubstring(String str, int startIndex, String word){
        int wordIndex = str.indexOf(word);

        if(wordIndex != -1 && wordIndex >= startIndex){
            return str.substring(startIndex, wordIndex);
        }
        return "Word not found";
    }
    public static String getSubstringByTwoWords(String str, String startWord, String word){
        int wordIndex = str.indexOf(word);
        int startIndex = str.lastIndexOf(startWord);
        if(wordIndex != -1 && wordIndex >= startIndex){
            return str.substring(startIndex + startWord.length(), wordIndex);
        }
        return "Word not found";
    }
    public static String getSubstringByLastWord(String str, String startWord, String word){
        int wordIndex = word.length();
        int startIndex = str.lastIndexOf(startWord);
        if(wordIndex != -1 && wordIndex >= startIndex){
            return str.substring(startIndex + startWord.length(), word.length() );
        }
        return "Word not founds";
    }
}
