package kz.dossier.tools;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import kz.dossier.dto.*;
import kz.dossier.modelsDossier.*;
import kz.dossier.repositoryDossier.MvUlRepo;
import kz.dossier.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class ULExportPDFService {
    @Autowired
    private MvUlRepo mvUlRepo;
    @Autowired
    ULService ulService;
    @Autowired
    MyService flService;
    @Autowired
    TransportService transportService;
    @Autowired
    TaxService taxService;
    @Autowired
    RnService rnService;

    public Document generate(String bin, ByteArrayOutputStream response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response);
        document.open();
        getBaseFont();
        Font font = new Font(getBaseFont());
        font.setColor(CMYKColor.WHITE);
        PdfPCell heading = new PdfPCell();
        heading.setBackgroundColor(CMYKColor.GRAY);
        heading.setHorizontalAlignment(Element.ALIGN_CENTER);

        addMainTable(bin, document, font);
        addAddress(bin, document, font);
        addCommodityProducers(bin, document, font);
        addULParticipants(bin, document, font);
        addSameAddressUL(bin, document, font);
        addFpgTable(bin, document, font);
        addSamrukGosZakupTable(bin, document, font, true);
        addSamrukGosZakupTable(bin, document, font, false);
        addAccountantTable(bin, document, font);
        addPdlTable(bin, document, font);
        addPensionTable(bin, document, font);
        addContactsTable(bin, document, font);
        addEquipmentTable(bin, document, font);
        addTransportTable(bin, document, font);
        addAutoTransportTable(bin, document, font);
        addAnotherTransport(bin, document, font);
        addTaxTable(bin, document, font);
        addSubsidyTable(bin, document, font);
        addAdministrativeFinesTable(bin, document, font);
        addRealEstateRegistryTable(bin, document, font);
        addDubaiPropertyTable(bin, document, font);
        addFno240PropertyTable(bin,document, font);
        addFno250Table(bin, document, font);
        addFno250AvtoTable(bin, document, font);
        addFno250CompanyTable(bin, document, font);
        addFno250DepositTable(bin, document, font);
//        addIpAddressesTable(bin, document, font);

        document.close();
        return document;
    }


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

    private void addMainTable(String bin, Document document, Font font) throws DocumentException {
        ULDto ul = ulService.getUlByBin(bin);
        java.util.List<String> columnHeaders = new ArrayList<>(Arrays.asList("БИН",
                "Наименование организаци",
                "Наименование ОКЭД",
                "Статус ЮЛ"
        ));
        PdfPTable table = tableHandler("Сведения о юридическом лице", columnHeaders, font);
        table = addCell(table, font, ul.getBin());
        table = addCell(table, font, ul.getFullName() != null ?  ul.getFullName() : "");
        table = addCell(table, font, ul.getOked() != null ?  ul.getOked() : "");
        table = addCell(table, font, ul.getStatus() != null ?  ul.getStatus() : "");

        document.add(table);
    }

    private void addAddress(String bin, Document document, Font font) throws DocumentException  {
        RegAddressULDto regAddressULDto = ulService.getUlAddressByBin(bin);
        if (regAddressULDto==null) {
            return;
        }
        java.util.List<String> columnHeaders = new ArrayList<>(Arrays.asList("Область",
                "Город",
                "Район",
                "Улица",
                "Номер дома"
        ));
        PdfPTable table = tableHandler("Юридический адрес", columnHeaders, font);
        table = addCell(table, font, regAddressULDto.getRegionRu());
        table = addCell(table, font, regAddressULDto.getLocalityRu());
        table = addCell(table, font, regAddressULDto.getDistrict());
        table = addCell(table, font, regAddressULDto.getStreetRu());
        table = addCell(table, font, regAddressULDto.getBuildingNum());

        document.add(table);
    }
    private void addCommodityProducers(String bin, Document document, Font font) throws DocumentException {
        List<CommodityProducersDTO> list = ulService.getComProducersByBin(bin);
        if (list.isEmpty()) {
            return;
        }
        java.util.List<String> columnHeaders = new ArrayList<>(Arrays.asList(
                "№",
                "СЗТП",                    // sztp (Type of Commodity)
                "Количество",             // count (Count or Quantity)
                "Статус",                 // status (Status)
                "Регион"                 // region (Region)
        ));
        PdfPTable table = tableHandler( "Наименование коллапса: \"Отечественные товаропроизводители\"  Количество найденных инф: " + list.size(), columnHeaders, font);
        int number = 1;
        for (CommodityProducersDTO a : list) {
            table = addCell(table, font, number + "");
            table = addCell(table, font, a.getSzpt());
            table = addCell(table, font, a.getCount().toString());
            table = addCell(table, font, a.getStatus());
            table = addCell(table, font, a.getRegion());
            number++;
        }
        document.add(table);
    }
    private void addULParticipants(String bin, Document document, Font font) throws DocumentException {
        List<ULULMemberDTO> list = ulService.getULMembersByBin(bin);
        if (list.isEmpty()) {
            return;
        }
        java.util.List<String> columnHeaders = new ArrayList<>(Arrays.asList("№",
                "БИН/ИИН",
                "Наименование",
                "Идентификатор ЮЛ",
                "Дата регистрации"
        ));
        PdfPTable table = tableHandler( "Наименование коллапса: \"Сведения об участниках ЮЛ\"  Количество найденных инф: " + list.size(), columnHeaders, font);

        int number = 1;
        for (ULULMemberDTO a : list) {
            table = addCell(table, font, number + "");
            table = addCell(table, font, a.getBinIin());
            try {
                table = addCell(table, font, a.getName());
            } catch (Exception e){
                table = addCell(table, font, "");
            }
            table = addCell(table, font, a.getPosition());
            try {
                String regDate = a.getDate().toString();
                table = addCell(table, font, regDate);
            } catch (Exception e) {
                table = addCell(table, font, "Нет даты");
            }

            number++;
        }

        document.add(table);
    }
    private void addSameAddressUL(String bin, Document document, Font font) throws DocumentException {
        List<SameULRegAddressDto> list = ulService.getSameAddressULByBin(bin);
        if (list.isEmpty()) {
            return;
        }
        List<String> columnHeaders = new ArrayList<>(Arrays.asList(
                "№",
                "БИН",
                "Наименование ЮЛ",
                "Юридический адрес",
                "Дата"
                ));
        PdfPTable table = tableHandler( "Наименование коллапса: \"Регистрация ЮЛ на одном адресе\"  Количество найденных инф: " + list.size(), columnHeaders, font);

        int number = 1;
        for (SameULRegAddressDto x: list) {
            table = addCell(table, font, number + "");
            table = addCell(table, font, x.getBin());
            table = addCell(table, font, x.getName());
            table = addCell(table, font, x.getAddress());
            table = addCell(table, font, x.getDate());

            number++;
        }
        document.add(table);
    }
    private void addFpgTable(String bin, Document document, Font font) throws DocumentException {
        List<FPGDto> list = ulService.getFpgsByBin(bin);
        if (list.isEmpty()) {
            return;
        }
        List<String> columnHeaders = new ArrayList<>(Arrays.asList("№",
                "Бенефициар"
        ));
        PdfPTable table = tableHandler("Наименование коллапса: \"ФПГ\"  Количество найденных инф: " + list.size(), columnHeaders, font);

        int number = 1;
        for (FPGDto a : list) {
            table = addCell(table, font, number + "");
            table = addCell(table, font, a.getBeneficiar());

            number++;
        }

        document.add(table);
    }
    private void addAccountantTable(String bin, Document document, Font font) throws DocumentException {
        List<AccountantDto> list = ulService.getAccountantsByBin(bin);
        if (list.isEmpty()) {
            return;
        }
        List<String> columnHeaders = new ArrayList<>(Arrays.asList("№",
                "ИИН",
                "ФИО",
                "Наименование типа должности"
        ));
        PdfPTable table = tableHandler( "Наименование коллапса: \"Бухгалтеры\"  Количество найденных инф: " + list.size(), columnHeaders, font);

        int number = 1;
        for (AccountantDto a : list) {
            table = addCell(table, font, number + "");
            table = addCell(table, font, a.getIin());
            table = addCell(table, font, a.getFio());
            table = addCell(table, font, a.getPosition());

            number++;
        }

        document.add(table);
    }

    private void addPdlTable(String bin, Document document, Font font) throws DocumentException {
        List<PdlDto> list = ulService.getPdlByBin(bin);
        if (list.isEmpty()) {
            return;
        }
        List<String> columnHeaders = new ArrayList<>(Arrays.asList("№",
                "ИИН",
                "ФИО",
                "Должность",
                "Область",
                "Орган",
                "ИИН супруги",
                "ФИО супруги",
                "Госорган супруги",
                "Должность супруги"
        ));
        PdfPTable table = tableHandler( "Наименование коллапса: \"ДЛ\"  Количество найденных инф: " + list.size(), columnHeaders, font);

        int number = 1;
        for (PdlDto a : list) {
            table = addCell(table, font, number + "");
            table = addCell(table, font, a.getIin());
            table = addCell(table, font, a.getFio());
            table = addCell(table, font, a.getPosition());
            table = addCell(table, font, a.getOblast());
            table = addCell(table, font, a.getOrgan());
            table = addCell(table, font, a.getIinSpouse());
            table = addCell(table, font, a.getFullNameSpouse());
            table = addCell(table, font, a.getOrganSpouse());
            table = addCell(table, font, a.getPositionSpouse());

            number++;
        }

        document.add(table);
    }
    public void addSamrukGosZakupTable(String bin, Document document, Font font, Boolean isSamruk) throws DocumentException {
        if (isSamruk) {
            SamrukKazynaForAll result = flService.samrukByBin(bin);
            if (!result.getWhenSupplier().isEmpty()) {
                List<String> columnHeaders = new ArrayList<>(Arrays.asList("№",
                        "Год",
                        "Общая сумма",
                        "Количество договоров",
                        "Поставщики"
                ));
                PdfPTable table = tableHandler( "Наименование коллапса: \"Самрук казына закупки - По поставщикам\"  Количество найденных инф: " + result.getWhenSupplier().size(), columnHeaders, font);

                int number = 1;
                for (SamrukDTO a : result.getWhenSupplier()) {
                    table = addCell(table, font, number + "");
                    table = addCell(table, font, a.getPeriod());
                    table = addCell(table, font, a.getSum());
                    table = addCell(table, font, String.valueOf(a.getNumber()));
                    table = addCell(table, font, String.valueOf(a.getCustomers()));

                    number++;
                }

                document.add(table);
            }
            if (!result.getWhenCustomer().isEmpty()) {
                List<String> columnHeaders = new ArrayList<>(Arrays.asList("№",
                        "Год",
                        "Общая сумма",
                        "Количество договоров",
                        "Поставщики"
                ));
                PdfPTable table = tableHandler( "Наименование коллапса: \"Самрук казына закупки - По заказчикам\"  Количество найденных инф: " + result.getWhenCustomer().size(), columnHeaders, font);

                int number = 1;
                for (SamrukDTO a : result.getWhenCustomer()) {
                    table = addCell(table, font, number + "");
                    table = addCell(table, font, a.getPeriod());
                    table = addCell(table, font, a.getSum());
                    table = addCell(table, font, String.valueOf(a.getNumber()));
                    table = addCell(table, font, String.valueOf(a.getCustomers()));

                    number++;
                }

                document.add(table);
            }
        } else {
            GosZakupForAll result = flService.gosZakupByBin(bin);
            if (!result.getWhenSupplier().isEmpty()) {
                List<String> columnHeaders = new ArrayList<>(Arrays.asList("№",
                        "Год",
                        "Общая сумма",
                        "Количество договоров",
                        "Поставщики"
                ));
                PdfPTable table = tableHandler( "Наименование коллапса: \"Государственные закупки - По поставщикам\"  Количество найденных инф: " + result.getWhenSupplier().size(), columnHeaders, font);

                int number = 1;
                for (GosZakupDTO a : result.getWhenSupplier()) {
                    table = addCell(table, font, number + "");
                    table = addCell(table, font, a.getPeriod());
                    table = addCell(table, font, a.getSum());
                    table = addCell(table, font, String.valueOf(a.getNumber()));
                    table = addCell(table, font, String.valueOf(a.getOpposite()));

                    number++;
                }

                document.add(table);
            }
            if (!result.getWhenCustomer().isEmpty()) {
                List<String> columnHeaders = new ArrayList<>(Arrays.asList("№",
                        "Год",
                        "Общая сумма",
                        "Количество договоров",
                        "Поставщики"
                ));
                PdfPTable table = tableHandler( "Наименование коллапса: \"Государственные закупки - По заказчикам\"  Количество найденных инф: " + result.getWhenCustomer().size(), columnHeaders, font);

                int number = 1;
                for (GosZakupDTO a : result.getWhenCustomer()) {
                    table = addCell(table, font, number + "");
                    table = addCell(table, font, a.getPeriod());
                    table = addCell(table, font, a.getSum());
                    table = addCell(table, font, String.valueOf(a.getNumber()));
                    table = addCell(table, font, String.valueOf(a.getOpposite()));

                    number++;
                }

                document.add(table);
            }
        }
    }

    private void addPensionTable(String bin, Document document, Font font) throws DocumentException {
        List<PensionDto> list = ulService.getPensionByBin(bin);
        if (list.isEmpty()) {
            return;
        }
        List<String> columnHeaders = new ArrayList<>(Arrays.asList("№",
                "Год",
                "Количество сотрудников"
        ));
        PdfPTable table = tableHandler( "Наименование коллапса: \"Пенсионные отчислениe\"  Количество найденных инф: " + list.size(), columnHeaders, font);

        int number = 1;
        for (PensionDto a : list) {
            table = addCell(table, font, number + "");
            table = addCell(table, font, a.getYear());
            table = addCell(table, font, a.getNumberOfEmps());

            number++;
        }

        document.add(table);
    }

    private void addContactsTable(String bin, Document document, Font font) throws DocumentException {
        List<ContactDetailDto> list = ulService.getContactsByBin(bin);
        if (list.isEmpty()) {
            return;
        }
        List<String> columnHeaders = new ArrayList<>(Arrays.asList("№", "Номер телефона", "Email", "ФИО руководителя организации",
                "ФИО/Наименование организации владельца номера", "Источник", "Nickname"
        ));
        PdfPTable table = tableHandler( "Наименование коллапса: \"Контактные данные\"  Количество найденных инф: " + list.size(), columnHeaders, font);

        int number = 1;
        for (ContactDetailDto a : list) {
            table = addCell(table, font, number + "");
            table = addCell(table, font, a.getNumber());
            table = addCell(table, font, a.getEmail());
            table = addCell(table, font, a.getFioOfHead());
            table = addCell(table, font, a.getNameOfOwner());
            table = addCell(table, font, a.getSource());
            table = addCell(table, font, a.getNickname());

            number++;
        }

        document.add(table);
    }

    private void addEquipmentTable(String bin, Document document, Font font) throws DocumentException {
        List<EquipmentDto> list = transportService.getEquimpentByBin(bin);
        if (list.isEmpty()) {
            return;
        }

        List<String> columnHeaders = Arrays.asList(
                "№", "БИН/ИИН владельца машины", "Наименование ЮЛ/ИП", "БИН/ИИН собственника машины",
                "Наименование ЮЛ/ИП", "Государственный номер", "Серия и номер техпаспорта",
                "Дата регистрации", "Причина постановки", "Дата снятия с учета",
                "Причина снятия с учета", "Заводской номер (VIN)", "Номер двигателя",
                "Марка", "Модель", "Завод-изготовитель", "Год выпуска", "В залоге да/нет",
                "В аресте да/нет", "Первичная регистрация да/нет"
        );

        PdfPTable table = tableHandler( "Наименование коллапса: \"Техника\"  Количество найденных инф: " + list.size(), columnHeaders, font);

        int number = 1;
        for (EquipmentDto dto : list) {
            addCell(table, font, String.valueOf(number));
            addCell(table, font, dto.getOwnerIinBin());
            addCell(table, font, dto.getOwnerName());
            addCell(table, font, dto.getProprietorIinBin());
            addCell(table, font, dto.getProprietorName());
            addCell(table, font, dto.getGovNumber());
            addCell(table, font, dto.getRegSeriesNum());
            addCell(table, font, String.valueOf(dto.getRegDate() != null ? dto.getRegDate() : ""));
            addCell(table, font, dto.getRegReason());
            addCell(table, font, String.valueOf(dto.getEndDate() != null ? dto.getEndDate() : ""));
            addCell(table, font, dto.getEndReason());
            addCell(table, font, dto.getVin());
            addCell(table, font, dto.getEngineNum());
            addCell(table, font, dto.getBrand());
            addCell(table, font, dto.getEquipmentModel());
            addCell(table, font, dto.getManufacturer());
            addCell(table, font, String.valueOf(dto.getIssueYear()));
            addCell(table, font, dto.getPledge() != null ? (dto.getPledge() ? "Да" : "Нет") : "");
            addCell(table, font, dto.getArrest() != null ? (dto.getArrest() ? "Да" : "Нет") : "");
            addCell(table, font, dto.getFirstReg() != null ? (dto.getFirstReg() ? "Да" : "Нет") : "");

            number++;
        }

        document.add(table);
    }

    private void addTransportTable(String bin, Document document, Font font) throws DocumentException {
        List<MvAutoDto> list = transportService.getTransportByBin(bin);
        if (list.isEmpty()) {
            return;
        }

        List<String> columnHeaders = Arrays.asList(
                "№", "Статус", "Регистрационный номер", "Марка модель", "Дата выдачи свидетельства",
                "Дата снятия", "Год выпуска", "Категория (управления/ТС)", "VIN/Кузов/Шасси",
                "Серия и регистрационный № свидетельства"
        );

        PdfPTable table = tableHandler( "Наименование коллапса: \"Транспорт\"  Количество найденных инф: " + list.size(), columnHeaders, font);

        int number = 1;
        for (MvAutoDto dto : list) {
            addCell(table, font, String.valueOf(number)); // Serial number
            addCell(table, font, dto.isRegistered() ? "Текущий" : "Исторический");
            addCell(table, font, dto.getRegNumber());
            addCell(table, font, dto.getBrandModel());
            addCell(table, font, dto.getDateCertificate());
            addCell(table, font, dto.getEndDate());
            addCell(table, font, dto.getReleaseYearTc());
            addCell(table, font, dto.getCategoryControlTc());
            addCell(table, font, dto.getVinKuzovShassi());
            addCell(table, font, dto.getSeriesRegNumber());

            number++;
        }

        document.add(table);
    }
    private void addAutoTransportTable(String bin, Document document, Font font) throws DocumentException {
        List<AutoTransportDto> list = transportService.getAutoTransportByBin(bin);
        if (list.isEmpty()) {
            return;
        }

        List<String> columnHeaders = Arrays.asList(
                "№", "Номер автотранспорта", "Марка автотранспорта", "Дата"
        );

        PdfPTable table = tableHandler( "Наименование коллапса: \"Управление автотранспортом\"  Количество найденных инф: " + list.size(), columnHeaders, font);

        int number = 1;
        for (AutoTransportDto dto : list) {
            addCell(table, font, String.valueOf(number)); // Serial number
            addCell(table, font, dto.getNumber());
            addCell(table, font, dto.getBrand());
            addCell(table, font, dto.getDate());

            number++;
        }

        document.add(table);
    }

    private void addAnotherTransport(String bin, Document document, Font font) throws DocumentException {
        addAviaTransportTable(bin, document, font);
        addTrainTable(bin, document, font);
        addWaterTransportTable(bin, document, font);
    }
    private void addAviaTransportTable(String bin, Document document, Font font) throws DocumentException{
        List<AviaTransport> list = transportService.getAviaTransport(bin);
        if (list.isEmpty()) {
            return;
        }
        List<String> columnHeaders = Arrays.asList(
                "№", "Дата регистрации", "Марка", "Бортовой номер", "Эксплуатант", "Дата снятия"
        );

        PdfPTable table = tableHandler( "Наименование коллапса: \"Авиатранспорт\"  Количество найденных инф: " + list.size(), columnHeaders, font);

        int number = 1;
        for (AviaTransport dto : list) {
            addCell(table, font, String.valueOf(number)); // Serial number
            addCell(table, font, String.valueOf(dto.getDate_registration()));
            addCell(table, font, dto.getMarka());
            addCell(table, font, dto.getBort());
            addCell(table, font, dto.getEkspluatant());
            addCell(table, font, String.valueOf(dto.getDate_exclusion()));

            number++;
        }

        document.add(table);
    }
    private void addTrainTable(String bin, Document document, Font font) throws DocumentException {
        List<Trains> result = transportService.getTrains(bin);
        if (result.isEmpty()) {
            return;
        }
        List<String> columnHeaders = Arrays.asList(
                "№", "Дата регистрации", "Тип собственности", "Тип вагона", "Категория вагона",
                "Год выпуска"
        );

        PdfPTable table = tableHandler( "Наименование коллапса: \"Железнодорожный транспорт\"  Количество найденных инф: " + result.size(), columnHeaders, font);

        int number = 1;
        for (Trains dto : result) {
            addCell(table, font, String.valueOf(number)); // Serial number
            addCell(table, font, String.valueOf(dto.getDoc_date()));
            addCell(table, font, dto.getOwnership_type());
            addCell(table, font, dto.getVagon_type());
            addCell(table, font, dto.getVagon_category());
            addCell(table, font, String.valueOf(dto.getVagon_make_year()));

            number++;
        }

        document.add(table);
    }
    private void addWaterTransportTable(String bin, Document document, Font font) throws DocumentException {
        List<WaterTransport> result = transportService.getWaterTransport(bin);
        if (result.isEmpty()) {
            return;
        }
        List<String> columnHeaders = Arrays.asList(
                "№", "Дата регистрации", "Цель использования", "Название водного транспорта",
                "Тип водного транспорта", "Год выпуска"
        );

        PdfPTable table = tableHandler( "Наименование коллапса: \"Водный транспорт\"  Количество найденных инф: " + result.size(), columnHeaders, font);

        int number = 1;
        for (WaterTransport dto : result) {
            addCell(table, font, String.valueOf(number)); // Serial number
            addCell(table, font, String.valueOf(dto.getYear_reestr()));
            addCell(table, font, dto.getPurpose());
            addCell(table, font, dto.getType_vt());
            addCell(table, font, dto.getYear_vt());

            number++;
        }

        document.add(table);
    }
    private void addTaxTable(String bin, Document document, Font font) throws DocumentException {
        List<MvTaxDto> result = taxService.getTaxes(bin);
        if (result.isEmpty()) {
            return;
        }
        List<String> columnHeaders = Arrays.asList(
                "№", "Код бюджетной классификации", "Наименование КБК", "Дата зачисления в бюджет",
                "Сумма платежа", "Номер платежного документа", "Тип платежа", "Тип проводки",
                "Дата списания со счета", "Код ОГД", "Наименование ОГД"
        );

        PdfPTable table = tableHandler( "Наименование коллапса: \"Налоги\"  Количество найденных инф: " + result.size(), columnHeaders, font);

        int number = 1;
        for (MvTaxDto dto : result) {
            addCell(table, font, String.valueOf(number)); // Serial number
            addCell(table, font, dto.getCodeOfBudgetClass());
            addCell(table, font, dto.getKbk());
            addCell(table, font, dto.getBudgetEnrollmentDate());
            addCell(table, font, dto.getSum());
            addCell(table, font, dto.getNumberOfPaymentDoc());
            addCell(table, font, dto.getTypeOfPayment());
            addCell(table, font, dto.getTypeOfProvodka());
            addCell(table, font, dto.getDebitDate());
            addCell(table, font, dto.getOgdCode());
            addCell(table, font, dto.getOgdName());

            number++;
        }

        document.add(table);
    }
    private void addSubsidyTable(String bin, Document document, Font font) throws DocumentException {
        List<SubsidiyDTO> result = ulService.getSubsidies(bin);
        if (result.isEmpty()) {
            return;
        }
        List<String> columnHeaders = Arrays.asList(
                "№", "Название", "Область", "Дата подачи заявки",
                "Наименование заявителя", "Статус заявки", "Дата принятия заявки",
                "Дата отклонения", "Сумма субсидий, тг.", "Причина отклонения",
                "IP подачи заявки", "IP отзыва заявки", "IP принятия заявки", "IP отклонения заявки"
        );

        PdfPTable table = tableHandler( "Наименование коллапса: \"Субсидии\"  Количество найденных инф: " + result.size(), columnHeaders, font);

        int number = 1;
        for (SubsidiyDTO dto : result) {
            addCell(table, font, String.valueOf(number)); // Serial number
            addCell(table, font, dto.getName());
            addCell(table, font, dto.getOblast());
            addCell(table, font, dto.getDate());
            addCell(table, font, dto.getNameOfTeller());
            addCell(table, font, dto.getStatus());
            addCell(table, font, dto.getDateOfTaking());
            addCell(table, font, dto.getDateOfDenying());
            addCell(table, font, dto.getSum());
            addCell(table, font, dto.getDenyingReason());
            addCell(table, font, dto.getIp1());
            addCell(table, font, dto.getIp2());
            addCell(table, font, dto.getIp3());
            addCell(table, font, dto.getIp4());

            number++;
        }

        document.add(table);
    }

    private void addAdministrativeFinesTable(String bin, Document document, Font font) throws DocumentException {
        List<AdmRightsBreakerDTO> result = ulService.getAdmsFines(bin);
        if (result.isEmpty()) {
            return;
        }
        List<String> columnHeaders = Arrays.asList(
                "№", "Орган выявивший правонарушение", "Дата заведения",
                "Номер протокола", "Место работы", "Квалификация", "Принудительное исполнение",
                "На срок до", "Размер наложенного штрафа", "Основания прекращения"
        );

        PdfPTable table = tableHandler( "Наименование коллапса: \"Административные штрафы\"  Количество найденных инф: " + result.size(), columnHeaders, font);

        int number = 1;
        for (AdmRightsBreakerDTO dto : result) {
            addCell(table, font, String.valueOf(number)); // Serial number
            addCell(table, font, dto.getOrgan());
            addCell(table, font, dto.getDateOfStart());
            addCell(table, font, dto.getNumberOfProtocol());
            addCell(table, font, dto.getPlaceOfWork());
            addCell(table, font, dto.getQualification());
            addCell(table, font, dto.getForcedImplementation());
            addCell(table, font, dto.getPeriodTo());
            addCell(table, font, dto.getSumOfFine());
            addCell(table, font, dto.getReasonOfStopping());

            number++;
        }

        document.add(table);
    }
    private void addDubaiPropertyTable(String bin, Document document, Font font) throws DocumentException {
        List<DubaiRnDto> result = rnService.getDubaiRns(bin);
        if (result.isEmpty()) {
            return;
        }
        List<String> columnHeaders = Arrays.asList(
                "№", "Дата покупки", "Площадь", "Название ЖК", "Номер квартиры"
        );

        PdfPTable table = tableHandler( "Наименование коллапса: \"Недвижимость в Дубае\"  Количество найденных инф: " + result.size(), columnHeaders, font);

        int number = 1;
        for (DubaiRnDto dto : result) {
            addCell(table, font, String.valueOf(number)); // Serial number
            addCell(table, font, dto.getDate());
            addCell(table, font, dto.getArea());
            addCell(table, font, dto.getNameOfZhk());
            addCell(table, font, dto.getAppartment());

            number++;
        }

        document.add(table);
    }
    private void addRealEstateRegistryTable(String bin, Document document, Font font) throws DocumentException {
        List<RnDTO> list = rnService.getRns(bin);
        if (list.isEmpty()) {
            return;
        }
        List<String> columnHeaders = Arrays.asList(
                "№", "Кадастровый номер", "Адрес", "Правообладатель", "Этажность",
                "Количество составляющих", "Площадь общая", "Вид документа", "Номер документа",
                "Дата документа", "Сумма сделки", "Жилая площадь", "Статус"
        );

        PdfPTable table = tableHandler( "Наименование коллапса: \"Сведения по реестру недвижимости\"  Количество найденных инф: " + list.size(), columnHeaders, font);

        int number = 1;
        for (RnDTO dto : list) {
            addCell(table, font, String.valueOf(number)); // Serial number
            addCell(table, font, dto.getCadastrialNumber());
            addCell(table, font, dto.getAddress());
            addCell(table, font, dto.getRightOwner());
            addCell(table, font, dto.getFloorness());
            addCell(table, font, "");
            addCell(table, font, dto.getAllArea());
            addCell(table, font, dto.getTypeOfDoc());
            addCell(table, font, dto.getDocumentNumber());
            addCell(table, font, dto.getDate());
            addCell(table, font, dto.getSumOfDeal());
            addCell(table, font, dto.getLivingArea());
            addCell(table, font, dto.getStatusRn());

            number++;
        }

        document.add(table);
    }
    private void addFno240PropertyTable(String bin, Document document, Font font) throws DocumentException {
        List<Fno240> result = rnService.getFno240s(bin);
        if (result.isEmpty()) {
            return;
        }
        List<String> columnHeaders = Arrays.asList(
                "№",  "Вид имущества", "Адрес" , "Доп.информация"
        );

        PdfPTable table = tableHandler( "Наименование коллапса: \"Имущество по ФНО 240\"  Количество найденных инф: " + result.size(), columnHeaders, font);

        int number = 1;
        for (Fno240 dto : result) {
            addCell(table, font, String.valueOf(number)); // Serial number
            addCell(table, font, dto.getTyp());
            addCell(table, font, dto.getAddress());
            addCell(table, font, dto.getComments());

            number++;
        }

        document.add(table);
    }
    private void addFno250Table(String bin, Document document, Font font) throws DocumentException {
        List<Fno250> result = rnService.getFno250s(bin);
        if (result.isEmpty()) {
            return;
        }
        List<String> columnHeaders = Arrays.asList(
                "№",  "Тип недвижимости", "Код страны", "Адрес"
        );

        PdfPTable table = tableHandler( "Наименование коллапса: \"Имущество по ФНО 250\"  Количество найденных инф: " + result.size(), columnHeaders, font);

        int number = 1;
        for (Fno250 dto : result) {
            addCell(table, font, String.valueOf(number)); // Serial number
            addCell(table, font, dto.getTyp());
            addCell(table, font, dto.getCode_country());
            addCell(table, font, dto.getAddress());

            number++;
        }

        document.add(table);
    }
    private void addFno250AvtoTable(String bin, Document document, Font font) throws DocumentException {
        List<Fno250Avto> result = rnService.getFno250Avtos(bin);
        if (result.isEmpty()) {
            return;
        }
        List<String> columnHeaders = Arrays.asList(
                "№",  "Марка", "Код страны", "VIN код"
        );

        PdfPTable table = tableHandler( "Наименование коллапса: \"Автотранспорт по ФНО 250\"  Количество найденных инф: " + result.size(), columnHeaders, font);

        int number = 1;
        for (Fno250Avto dto : result) {
            addCell(table, font, String.valueOf(number)); // Serial number
            addCell(table, font, dto.getMarka());
            addCell(table, font, dto.getCode_country());
            addCell(table, font, dto.getVin_code());

            number++;
        }

        document.add(table);
    }
    private void addFno250CompanyTable(String bin, Document document, Font font) throws DocumentException {
        List<Fno250Company> result = rnService.getFno250Companies(bin);
        if (result.isEmpty()) {
            return;
        }
        List<String> columnHeaders = Arrays.asList(
                "№",  "Наименование компании", "Код страны", "Доля"
        );

        PdfPTable table = tableHandler( "Наименование коллапса: \"Компании по ФНО 250\"  Количество найденных инф: " + result.size(), columnHeaders, font);

        int number = 1;
        for (Fno250Company dto : result) {
            addCell(table, font, String.valueOf(number)); // Serial number
            addCell(table, font, dto.getName_company());
            addCell(table, font, dto.getCode_country());
            addCell(table, font, dto.getDolya());

            number++;
        }

        document.add(table);
    }
    private void addFno250DepositTable(String bin, Document document, Font font) throws DocumentException {
        List<Fno250Deposit> result = rnService.getFno250Deposit(bin);
        if (result.isEmpty()) {
            return;
        }
        List<String> columnHeaders = Arrays.asList(
                "№",  "Название банка", "Код страны", "Код валюты", "Сумма"
        );

        PdfPTable table = tableHandler( "Наименование коллапса: \"Депозит по ФНО 250\"  Количество найденных инф: " + result.size(), columnHeaders, font);

        int number = 1;
        for (Fno250Deposit dto : result) {
            addCell(table, font, String.valueOf(number)); // Serial number
            addCell(table, font, dto.getBank());
            addCell(table, font, dto.getCode_country());
            addCell(table, font, dto.getCurrency());
            addCell(table, font, dto.getSumma());

            number++;
        }

        document.add(table);
    }
    private PdfPTable addCell(PdfPTable table, Font font, String string) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(CMYKColor.WHITE);
        cell.setPadding(5);
        cell.setPhrase(new Phrase(string, font));
        table.addCell(cell);
        return table;
    }

    private PdfPTable tableHandler(String header, List<String> columnHeaders, Font font) throws DocumentException {
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

}
