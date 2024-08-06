package kz.dossier.tools;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPTable;
import kz.dossier.dto.CommodityProducersDTO;
import kz.dossier.dto.RegAddressULDto;
import kz.dossier.dto.ULDto;
import kz.dossier.dto.*;
import kz.dossier.modelsDossier.*;
import kz.dossier.modelsRisk.*;
import kz.dossier.repositoryDossier.FlPensionMiniRepo;
import kz.dossier.repositoryDossier.MvUlRepo;
import kz.dossier.service.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff1;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UlDocxGenerator {
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
    public void generateUl(String bin, ByteArrayOutputStream baos) throws IOException {
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
            addMainTable(bin, doc);
            addAddress(bin, doc);
            addCommodityProducers(bin, doc);
            addULParticipants(bin, doc);
            addSameAddressUL(bin, doc);
            addFpgTable(bin, doc);
            addSamrukGosZakupTable(bin, doc, true);
            addSamrukGosZakupTable(bin, doc, false);
            addAccountantTable(bin, doc);
            addPdlTable(bin, doc);
            addPensionTable(bin, doc); //
            addContactsTable(bin, doc);
            addEquipmentTable(bin, doc);
            addTransportTable(bin, doc);
            addAutoTransportTable(bin, doc);
            addAnotherTransport(bin, doc);
            addTaxTable(bin, doc);
            addSubsidyTable(bin, doc); //
            addAdministrativeFinesTable(bin, doc);
            addRealEstateRegistryTable(bin, doc);
            addDubaiPropertyTable(bin, doc);
            addFno240PropertyTable(bin, doc);
            addFno250Table(bin, doc);
            addFno250AvtoTable(bin, doc);
            addFno250CompanyTable(bin, doc);
            addFno250DepositTable(bin, doc);

            doc.write(baos);
            baos.close();
        }
    }
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

    private void createTitle(XWPFDocument doc,String title){
        XWPFParagraph titleParagraph = doc.createParagraph();
        XWPFRun titleRun = titleParagraph.createRun();
        titleRun.setText(title);
        titleRun.setBold(true);
        titleRun.setFontSize(14);
    }


    public void setCellPadding(XWPFTableCell cell, int top, int left, int bottom, int right) {
        CTTcPr tcPr = cell.getCTTc().addNewTcPr();

        CTTcMar cellMar = tcPr.isSetTcMar() ? tcPr.getTcMar() : tcPr.addNewTcMar();
        cellMar.addNewTop().setW(BigInteger.valueOf(top));
        cellMar.addNewLeft().setW(BigInteger.valueOf(left));
        cellMar.addNewBottom().setW(BigInteger.valueOf(bottom));
        cellMar.addNewRight().setW(BigInteger.valueOf(right));
    }
    private void addMainTable(String bin, XWPFDocument doc) {
        ULDto ul = ulService.getUlByBin(bin);
        createTitle(doc, "Сведения о юридическом лице");
        XWPFTable table = doc.createTable();
        table.setWidth("100%");
        makeTableByProperties(doc, table, "Сведения о юридическом лице", Arrays.asList(
                "БИН",
                "Наименование организации",
                "Наименование ОКЭД",
                "Статус ЮЛ"
        ));
        XWPFTableRow dataRow = table.createRow();
        dataRow.getCell(0).setText(ul.getBin() != null ? ul.getBin() : "");
        dataRow.getCell(1).setText(ul.getFullName() != null ? ul.getFullName() : "");
        dataRow.getCell(2).setText(ul.getOked() != null ? ul.getOked() : "");
        dataRow.getCell(3).setText(ul.getStatus() != null ? ul.getStatus() : "");
        setMarginBetweenTables(doc);

    }
    private void addAddress(String bin, XWPFDocument doc) {
        RegAddressULDto regAddressULDto = ulService.getUlAddressByBin(bin);
        if (regAddressULDto == null) {
            return;
        }

        createTitle(doc, "Юридический адрес");

        // Create the table
        XWPFTable table = doc.createTable();
        table.setWidth("100%");

        // Define column headers
        List<String> columnHeaders = Arrays.asList(
                "Область",
                "Город",
                "Район",
                "Улица",
                "Номер дома"
        );

        // Add column headers
        XWPFTableRow headerRow = table.getRow(0);
        for (String header : columnHeaders) {
            XWPFTableCell cell = headerRow.getCell(columnHeaders.indexOf(header));
            if (cell == null) {
                cell = headerRow.addNewTableCell();
            }
            cell.setText(header);
        }

        // Add data row
        XWPFTableRow dataRow = table.createRow();
        dataRow.getCell(0).setText(regAddressULDto.getRegionRu() != null ? regAddressULDto.getRegionRu() : "");
        dataRow.getCell(1).setText(regAddressULDto.getLocalityRu() != null ? regAddressULDto.getLocalityRu() : "");
        dataRow.getCell(2).setText(regAddressULDto.getDistrict() != null ? regAddressULDto.getDistrict() : "");
        dataRow.getCell(3).setText(regAddressULDto.getStreetRu() != null ? regAddressULDto.getStreetRu() : "");
        dataRow.getCell(4).setText(regAddressULDto.getBuildingNum() != null ? regAddressULDto.getBuildingNum() : "");

        // Optional: Set margins or other formatting
        setMarginBetweenTables(doc);
    }
    private void addCommodityProducers(String bin, XWPFDocument doc) {
        List<CommodityProducersDTO> list = ulService.getComProducersByBin(bin);
        if (list.isEmpty()) {
            return;
        }

        // Create the title
        createTitle(doc, "Отечественные товаропроизводители");

        // Create the table
        XWPFTable table = doc.createTable();
        table.setWidth("100%");

        // Define column headers
        List<String> columnHeaders = Arrays.asList(
                "№",
                "СЗТП",
                "Количество",
                "Статус",
                "Регион"
        );

        // Add column headers
        XWPFTableRow headerRow = table.getRow(0);
        for (String header : columnHeaders) {
            XWPFTableCell cell = headerRow.getCell(columnHeaders.indexOf(header));
            if (cell == null) {
                cell = headerRow.addNewTableCell();
            }
            cell.setText(header);
        }

        // Add data rows
        int number = 1;
        for (CommodityProducersDTO dto : list) {
            XWPFTableRow dataRow = table.createRow();
            dataRow.getCell(0).setText(String.valueOf(number));
            dataRow.getCell(1).setText(dto.getSzpt() != null ? dto.getSzpt() : "");
            dataRow.getCell(2).setText(dto.getCount() != null ? dto.getCount().toString() : "");
            dataRow.getCell(3).setText(dto.getStatus() != null ? dto.getStatus() : "");
            dataRow.getCell(4).setText(dto.getRegion() != null ? dto.getRegion() : "");
            number++;
        }

        // Optional: Set margins or other formatting
        setMarginBetweenTables(doc);
    }
    private void addULParticipants(String bin, XWPFDocument doc) {
        List<ULULMemberDTO> list = ulService.getULMembersByBin(bin);
        if (list.isEmpty()) {
            return;
        }

        // Create the title
        createTitle(doc, "Сведения об участниках ЮЛ");

        // Create the table
        XWPFTable table = doc.createTable();
        table.setWidth("100%");

        // Define column headers
        List<String> columnHeaders = Arrays.asList(
                "№",
                "БИН/ИИН",
                "Наименование",
                "Идентификатор ЮЛ",
                "Дата регистрации"
        );

        // Add column headers
        XWPFTableRow headerRow = table.getRow(0);
        for (String header : columnHeaders) {
            XWPFTableCell cell = headerRow.getCell(columnHeaders.indexOf(header));
            if (cell == null) {
                cell = headerRow.addNewTableCell();
            }
            cell.setText(header);
        }

        // Add data rows
        int number = 1;
        for (ULULMemberDTO dto : list) {
            XWPFTableRow dataRow = table.createRow();
            dataRow.getCell(0).setText(String.valueOf(number));
            dataRow.getCell(1).setText(dto.getBinIin() != null ? dto.getBinIin() : "");

            try {
                dataRow.getCell(2).setText(dto.getName() != null ? dto.getName() : "");
            } catch (Exception e) {
                dataRow.getCell(2).setText("");
            }

            dataRow.getCell(3).setText(dto.getPosition() != null ? dto.getPosition() : "");

            try {
                String regDate = dto.getDate() != null ? dto.getDate().toString() : "Нет даты";
                dataRow.getCell(4).setText(regDate);
            } catch (Exception e) {
                dataRow.getCell(4).setText("Нет даты");
            }

            number++;
        }

        // Optional: Set margins or other formatting
        setMarginBetweenTables(doc);
    }
    private void addSameAddressUL(String bin, XWPFDocument doc) {
        List<SameULRegAddressDto> list = ulService.getSameAddressULByBin(bin);
        if (list.isEmpty()) {
            return;
        }

        // Create the title
        createTitle(doc, "Регистрация ЮЛ на одном адресе");

        // Create the table
        XWPFTable table = doc.createTable();
        table.setWidth("100%");

        // Define column headers
        List<String> columnHeaders = Arrays.asList(
                "№",
                "БИН",
                "Наименование ЮЛ",
                "Юридический адрес",
                "Дата"
        );

        // Add column headers
        XWPFTableRow headerRow = table.getRow(0);
        for (String header : columnHeaders) {
            XWPFTableCell cell = headerRow.getCell(columnHeaders.indexOf(header));
            if (cell == null) {
                cell = headerRow.addNewTableCell();
            }
            cell.setText(header);
        }

        // Add data rows
        int number = 1;
        for (SameULRegAddressDto dto : list) {
            XWPFTableRow dataRow = table.createRow();
            dataRow.getCell(0).setText(String.valueOf(number));
            dataRow.getCell(1).setText(dto.getBin() != null ? dto.getBin() : "");
            dataRow.getCell(2).setText(dto.getName() != null ? dto.getName() : "");
            dataRow.getCell(3).setText(dto.getAddress() != null ? dto.getAddress() : "");
            dataRow.getCell(4).setText(dto.getDate() != null ? dto.getDate().toString() : "");

            number++;
        }

        // Optional: Set margins or other formatting
        setMarginBetweenTables(doc);
    }
    private void addFpgTable(String bin, XWPFDocument doc) {
        List<FPGDto> list = ulService.getFpgsByBin(bin);
        if (list.isEmpty()) {
            return;
        }

        // Create the title
        createTitle(doc, "ФПГ");

        // Create the table
        XWPFTable table = doc.createTable();
        table.setWidth("100%");

        // Define column headers
        List<String> columnHeaders = Arrays.asList(
                "№",
                "Бенефициар"
        );

        // Add column headers
        XWPFTableRow headerRow = table.getRow(0);
        for (int i = 0; i < columnHeaders.size(); i++) {
            XWPFTableCell cell = headerRow.getCell(i);
            if (cell == null) {
                cell = headerRow.addNewTableCell();
            }
            cell.setText(columnHeaders.get(i));
        }

        // Add data rows
        int number = 1;
        for (FPGDto dto : list) {
            XWPFTableRow dataRow = table.createRow();
            dataRow.getCell(0).setText(String.valueOf(number));
            dataRow.getCell(1).setText(dto.getBeneficiar() != null ? dto.getBeneficiar() : "");

            number++;
        }

        // Optional: Set margins or other formatting
        setMarginBetweenTables(doc);
    }
    private void addAccountantTable(String bin, XWPFDocument doc) {
        List<AccountantDto> list = ulService.getAccountantsByBin(bin);
        if (list.isEmpty()) {
            return;
        }

        // Create the title
        createTitle(doc, "Бухгалтеры");

        // Create the table
        XWPFTable table = doc.createTable();
        table.setWidth("100%");

        // Define column headers
        List<String> columnHeaders = Arrays.asList(
                "№",
                "ИИН",
                "ФИО",
                "Наименование типа должности"
        );

        // Add column headers
        XWPFTableRow headerRow = table.getRow(0);
        for (int i = 0; i < columnHeaders.size(); i++) {
            XWPFTableCell cell = headerRow.getCell(i);
            if (cell == null) {
                cell = headerRow.addNewTableCell();
            }
            cell.setText(columnHeaders.get(i));
        }

        // Add data rows
        int number = 1;
        for (AccountantDto dto : list) {
            XWPFTableRow dataRow = table.createRow();
            dataRow.getCell(0).setText(String.valueOf(number));
            dataRow.getCell(1).setText(dto.getIin() != null ? dto.getIin() : "");
            dataRow.getCell(2).setText(dto.getFio() != null ? dto.getFio() : "");
            dataRow.getCell(3).setText(dto.getPosition() != null ? dto.getPosition() : "");

            number++;
        }

        // Optional: Set margins or other formatting
        setMarginBetweenTables(doc);
    }
    private void addPdlTable(String bin, XWPFDocument doc) {
        List<PdlDto> list = ulService.getPdlByBin(bin);
        if (list.isEmpty()) {
            return;
        }

        // Create the title
        createTitle(doc, "ДЛ");

        // Create the table
        XWPFTable table = doc.createTable();
        table.setWidth("100%");

        // Define column headers
        List<String> columnHeaders = Arrays.asList(
                "№",
                "ИИН",
                "ФИО",
                "Должность",
                "Область",
                "Орган",
                "ИИН супруги",
                "ФИО супруги",
                "Госорган супруги",
                "Должность супруги"
        );

        // Add column headers
        XWPFTableRow headerRow = table.getRow(0);
        for (int i = 0; i < columnHeaders.size(); i++) {
            XWPFTableCell cell = headerRow.getCell(i);
            if (cell == null) {
                cell = headerRow.addNewTableCell();
            }
            cell.setText(columnHeaders.get(i));
        }

        // Add data rows
        int number = 1;
        for (PdlDto dto : list) {
            XWPFTableRow dataRow = table.createRow();
            dataRow.getCell(0).setText(String.valueOf(number));
            dataRow.getCell(1).setText(dto.getIin() != null ? dto.getIin() : "");
            dataRow.getCell(2).setText(dto.getFio() != null ? dto.getFio() : "");
            dataRow.getCell(3).setText(dto.getPosition() != null ? dto.getPosition() : "");
            dataRow.getCell(4).setText(dto.getOblast() != null ? dto.getOblast() : "");
            dataRow.getCell(5).setText(dto.getOrgan() != null ? dto.getOrgan() : "");
            dataRow.getCell(6).setText(dto.getIinSpouse() != null ? dto.getIinSpouse() : "");
            dataRow.getCell(7).setText(dto.getFullNameSpouse() != null ? dto.getFullNameSpouse() : "");
            dataRow.getCell(8).setText(dto.getOrganSpouse() != null ? dto.getOrganSpouse() : "");
            dataRow.getCell(9).setText(dto.getPositionSpouse() != null ? dto.getPositionSpouse() : "");

            number++;
        }

        // Optional: Set margins or other formatting
        setMarginBetweenTables(doc);
    }
    private void addSamrukGosZakupTable(String bin, XWPFDocument doc, Boolean isSamruk) {
        if (isSamruk) {
            SamrukKazynaForAll result = flService.samrukByBin(bin);

            if (!result.getWhenSupplier().isEmpty()) {
                // Create the title
                createTitle(doc, "Самрук казына закупки - По поставщикам");

                // Create the table
                XWPFTable table = doc.createTable();
                table.setWidth("100%");

                // Define column headers
                List<String> columnHeaders = Arrays.asList(
                        "№",
                        "Год",
                        "Общая сумма",
                        "Количество договоров",
                        "Поставщики"
                );

                // Add column headers
                XWPFTableRow headerRow = table.getRow(0);
                for (int i = 0; i < columnHeaders.size(); i++) {
                    XWPFTableCell cell = headerRow.getCell(i);
                    if (cell == null) {
                        cell = headerRow.addNewTableCell();
                    }
                    cell.setText(columnHeaders.get(i));
                }

                // Add data rows
                int number = 1;
                for (SamrukDTO dto : result.getWhenSupplier()) {
                    XWPFTableRow dataRow = table.createRow();
                    dataRow.getCell(0).setText(String.valueOf(number));
                    dataRow.getCell(1).setText(dto.getPeriod() != null ? dto.getPeriod() : "");
                    dataRow.getCell(2).setText(dto.getSum() != null ? dto.getSum() : "");
                    dataRow.getCell(3).setText(String.valueOf(dto.getNumber()));
                    dataRow.getCell(4).setText(String.valueOf(dto.getCustomers()));

                    number++;
                }

                // Optional: Set margins or other formatting
                setMarginBetweenTables(doc);
            }

            if (!result.getWhenCustomer().isEmpty()) {
                // Create the title
                createTitle(doc, "Самрук казына закупки - По заказчикам");

                // Create the table
                XWPFTable table = doc.createTable();
                table.setWidth("100%");

                // Define column headers
                List<String> columnHeaders = Arrays.asList(
                        "№",
                        "Год",
                        "Общая сумма",
                        "Количество договоров",
                        "Поставщики"
                );

                // Add column headers
                XWPFTableRow headerRow = table.getRow(0);
                for (int i = 0; i < columnHeaders.size(); i++) {
                    XWPFTableCell cell = headerRow.getCell(i);
                    if (cell == null) {
                        cell = headerRow.addNewTableCell();
                    }
                    cell.setText(columnHeaders.get(i));
                }

                // Add data rows
                int number = 1;
                for (SamrukDTO dto : result.getWhenCustomer()) {
                    XWPFTableRow dataRow = table.createRow();
                    dataRow.getCell(0).setText(String.valueOf(number));
                    dataRow.getCell(1).setText(dto.getPeriod() != null ? dto.getPeriod() : "");
                    dataRow.getCell(2).setText(dto.getSum() != null ? dto.getSum() : "");
                    dataRow.getCell(3).setText(String.valueOf(dto.getNumber()));
                    dataRow.getCell(4).setText(String.valueOf(dto.getCustomers()));

                    number++;
                }

                // Optional: Set margins or other formatting
                setMarginBetweenTables(doc);
            }
        } else {
            GosZakupForAll result = flService.gosZakupByBin(bin);

            if (!result.getWhenSupplier().isEmpty()) {
                // Create the title
                createTitle(doc, "Государственные закупки - По поставщикам");

                // Create the table
                XWPFTable table = doc.createTable();
                table.setWidth("100%");

                // Define column headers
                List<String> columnHeaders = Arrays.asList(
                        "№",
                        "Год",
                        "Общая сумма",
                        "Количество договоров",
                        "Поставщики"
                );

                // Add column headers
                XWPFTableRow headerRow = table.getRow(0);
                for (int i = 0; i < columnHeaders.size(); i++) {
                    XWPFTableCell cell = headerRow.getCell(i);
                    if (cell == null) {
                        cell = headerRow.addNewTableCell();
                    }
                    cell.setText(columnHeaders.get(i));
                }

                // Add data rows
                int number = 1;
                for (GosZakupDTO dto : result.getWhenSupplier()) {
                    XWPFTableRow dataRow = table.createRow();
                    dataRow.getCell(0).setText(String.valueOf(number));
                    dataRow.getCell(1).setText(dto.getPeriod() != null ? dto.getPeriod() : "");
                    dataRow.getCell(2).setText(dto.getSum() != null ? dto.getSum() : "");
                    dataRow.getCell(3).setText(String.valueOf(dto.getNumber()));
                    dataRow.getCell(4).setText(String.valueOf(dto.getOpposite()));

                    number++;
                }

                // Optional: Set margins or other formatting
                setMarginBetweenTables(doc);
            }

            if (!result.getWhenCustomer().isEmpty()) {
                // Create the title
                createTitle(doc, "Государственные закупки - По заказчикам");

                // Create the table
                XWPFTable table = doc.createTable();
                table.setWidth("100%");

                // Define column headers
                List<String> columnHeaders = Arrays.asList(
                        "№",
                        "Год",
                        "Общая сумма",
                        "Количество договоров",
                        "Поставщики"
                );

                // Add column headers
                XWPFTableRow headerRow = table.getRow(0);
                for (int i = 0; i < columnHeaders.size(); i++) {
                    XWPFTableCell cell = headerRow.getCell(i);
                    if (cell == null) {
                        cell = headerRow.addNewTableCell();
                    }
                    cell.setText(columnHeaders.get(i));
                }

                // Add data rows
                int number = 1;
                for (GosZakupDTO dto : result.getWhenCustomer()) {
                    XWPFTableRow dataRow = table.createRow();
                    dataRow.getCell(0).setText(String.valueOf(number));
                    dataRow.getCell(1).setText(dto.getPeriod() != null ? dto.getPeriod() : "");
                    dataRow.getCell(2).setText(dto.getSum() != null ? dto.getSum() : "");
                    dataRow.getCell(3).setText(String.valueOf(dto.getNumber()));
                    dataRow.getCell(4).setText(String.valueOf(dto.getOpposite()));

                    number++;
                }

                // Optional: Set margins or other formatting
                setMarginBetweenTables(doc);
            }
        }
    }

    private void addPensionTable(String bin, XWPFDocument doc) {
        List<PensionDto> list = ulService.getPensionByBin(bin);
        if (list.isEmpty()) {
            return;
        }

        // Create the title
        createTitle(doc, "Пенсионные отчисления");

        // Create the table
        XWPFTable table = doc.createTable();
        table.setWidth("100%");

        // Define column headers
        List<String> columnHeaders = Arrays.asList(
                "№",
                "Год",
                "Количество сотрудников"
        );

        // Add column headers
        XWPFTableRow headerRow = table.getRow(0);
        for (int i = 0; i < columnHeaders.size(); i++) {
            XWPFTableCell cell = headerRow.getCell(i);
            if (cell == null) {
                cell = headerRow.addNewTableCell();
            }
            cell.setText(columnHeaders.get(i));
        }

        // Add data rows
        int number = 1;
        for (PensionDto dto : list) {
            XWPFTableRow dataRow = table.createRow();
            dataRow.getCell(0).setText(String.valueOf(number));
            dataRow.getCell(1).setText(dto.getYear() != null ? dto.getYear() : "");
            dataRow.getCell(2).setText(dto.getNumberOfEmps() != null ? dto.getNumberOfEmps() : "");

            number++;
        }

        // Optional: Set margins or other formatting
        setMarginBetweenTables(doc);
    }
    private void addContactsTable(String bin, XWPFDocument doc) {
        List<ContactDetailDto> list = ulService.getContactsByBin(bin);
        if (list.isEmpty()) {
            return;
        }

        // Create the title
        createTitle(doc, "Контактные данные");

        // Create the table
        XWPFTable table = doc.createTable();
        table.setWidth("100%");

        // Define column headers
        List<String> columnHeaders = Arrays.asList(
                "№",
                "Номер телефона",
                "Email",
                "ФИО руководителя организации",
                "ФИО/Наименование организации владельца номера",
                "Источник",
                "Nickname"
        );

        // Add column headers
        XWPFTableRow headerRow = table.getRow(0);
        for (int i = 0; i < columnHeaders.size(); i++) {
            XWPFTableCell cell = headerRow.getCell(i);
            if (cell == null) {
                cell = headerRow.addNewTableCell();
            }
            cell.setText(columnHeaders.get(i));
        }

        // Add data rows
        int number = 1;
        for (ContactDetailDto dto : list) {
            XWPFTableRow dataRow = table.createRow();
            dataRow.getCell(0).setText(String.valueOf(number));
            dataRow.getCell(1).setText(dto.getNumber() != null ? dto.getNumber() : "");
            dataRow.getCell(2).setText(dto.getEmail() != null ? dto.getEmail() : "");
            dataRow.getCell(3).setText(dto.getFioOfHead() != null ? dto.getFioOfHead() : "");
            dataRow.getCell(4).setText(dto.getNameOfOwner() != null ? dto.getNameOfOwner() : "");
            dataRow.getCell(5).setText(dto.getSource() != null ? dto.getSource() : "");
            dataRow.getCell(6).setText(dto.getNickname() != null ? dto.getNickname() : "");

            number++;
        }

        // Optional: Set margins or other formatting
        setMarginBetweenTables(doc);
    }
    private void addEquipmentTable(String bin, XWPFDocument doc) {
        List<EquipmentDto> list = transportService.getEquimpentByBin(bin);
        if (list.isEmpty()) {
            return;
        }

        // Create the title
        createTitle(doc, "Техника");

        // Create the table
        XWPFTable table = doc.createTable();
        table.setWidth("100%");

        // Define column headers
        List<String> columnHeaders = Arrays.asList(
                "№", "БИН/ИИН владельца машины", "Наименование ЮЛ/ИП", "БИН/ИИН собственника машины",
                "Наименование ЮЛ/ИП", "Государственный номер", "Серия и номер техпаспорта",
                "Дата регистрации", "Причина постановки", "Дата снятия с учета",
                "Причина снятия с учета", "Заводской номер (VIN)", "Номер двигателя",
                "Марка", "Модель", "Завод-изготовитель", "Год выпуска", "В залоге да/нет",
                "В аресте да/нет", "Первичная регистрация да/нет"
        );

        // Add column headers
        XWPFTableRow headerRow = table.getRow(0);
        for (int i = 0; i < columnHeaders.size(); i++) {
            XWPFTableCell cell = headerRow.getCell(i);
            if (cell == null) {
                cell = headerRow.addNewTableCell();
            }
            cell.setText(columnHeaders.get(i));
        }

        // Add data rows
        int number = 1;
        for (EquipmentDto dto : list) {
            XWPFTableRow dataRow = table.createRow();
            dataRow.getCell(0).setText(String.valueOf(number));
            dataRow.getCell(1).setText(dto.getOwnerIinBin() != null ? dto.getOwnerIinBin() : "");
            dataRow.getCell(2).setText(dto.getOwnerName() != null ? dto.getOwnerName() : "");
            dataRow.getCell(3).setText(dto.getProprietorIinBin() != null ? dto.getProprietorIinBin() : "");
            dataRow.getCell(4).setText(dto.getProprietorName() != null ? dto.getProprietorName() : "");
            dataRow.getCell(5).setText(dto.getGovNumber() != null ? dto.getGovNumber() : "");
            dataRow.getCell(6).setText(dto.getRegSeriesNum() != null ? dto.getRegSeriesNum() : "");
            dataRow.getCell(7).setText(dto.getRegDate() != null ? dto.getRegDate().toString() : "");
            dataRow.getCell(8).setText(dto.getRegReason() != null ? dto.getRegReason() : "");
            dataRow.getCell(9).setText(dto.getEndDate() != null ? dto.getEndDate().toString() : "");
            dataRow.getCell(10).setText(dto.getEndReason() != null ? dto.getEndReason() : "");
            dataRow.getCell(11).setText(dto.getVin() != null ? dto.getVin() : "");
            dataRow.getCell(12).setText(dto.getEngineNum() != null ? dto.getEngineNum() : "");
            dataRow.getCell(13).setText(dto.getBrand() != null ? dto.getBrand() : "");
            dataRow.getCell(14).setText(dto.getEquipmentModel() != null ? dto.getEquipmentModel() : "");
            dataRow.getCell(15).setText(dto.getManufacturer() != null ? dto.getManufacturer() : "");
            dataRow.getCell(16).setText(dto.getIssueYear() != null ? String.valueOf(dto.getIssueYear()) : "");
            dataRow.getCell(17).setText(dto.getPledge() != null ? (dto.getPledge() ? "Да" : "Нет") : "");
            dataRow.getCell(18).setText(dto.getArrest() != null ? (dto.getArrest() ? "Да" : "Нет") : "");
            dataRow.getCell(19).setText(dto.getFirstReg() != null ? (dto.getFirstReg() ? "Да" : "Нет") : "");

            number++;
        }

        // Optional: Set margins or other formatting
        setMarginBetweenTables(doc);
    }
    private void addTransportTable(String bin, XWPFDocument doc) {
        List<MvAutoDto> list = transportService.getTransportByBin(bin);
        if (list.isEmpty()) {
            return;
        }

        // Create the title
        createTitle(doc, "Транспорт");

        // Create the table
        XWPFTable table = doc.createTable();
        table.setWidth("100%");

        // Define column headers
        List<String> columnHeaders = Arrays.asList(
                "№", "Статус", "Регистрационный номер", "Марка модель", "Дата выдачи свидетельства",
                "Дата снятия", "Год выпуска", "Категория (управления/ТС)", "VIN/Кузов/Шасси",
                "Серия и регистрационный № свидетельства"
        );

        // Add column headers
        XWPFTableRow headerRow = table.getRow(0);
        for (int i = 0; i < columnHeaders.size(); i++) {
            XWPFTableCell cell = headerRow.getCell(i);
            if (cell == null) {
                cell = headerRow.addNewTableCell();
            }
            cell.setText(columnHeaders.get(i));
        }

        // Add data rows
        int number = 1;
        for (MvAutoDto dto : list) {
            XWPFTableRow dataRow = table.createRow();
            dataRow.getCell(0).setText(String.valueOf(number));
            dataRow.getCell(1).setText(dto.isRegistered() ? "Текущий" : "Исторический");
            dataRow.getCell(2).setText(dto.getRegNumber() != null ? dto.getRegNumber() : "");
            dataRow.getCell(3).setText(dto.getBrandModel() != null ? dto.getBrandModel() : "");
            dataRow.getCell(4).setText(dto.getDateCertificate() != null ? dto.getDateCertificate().toString() : "");
            dataRow.getCell(5).setText(dto.getEndDate() != null ? dto.getEndDate().toString() : "");
            dataRow.getCell(6).setText(dto.getReleaseYearTc() != null ? String.valueOf(dto.getReleaseYearTc()) : "");
            dataRow.getCell(7).setText(dto.getCategoryControlTc() != null ? dto.getCategoryControlTc() : "");
            dataRow.getCell(8).setText(dto.getVinKuzovShassi() != null ? dto.getVinKuzovShassi() : "");
            dataRow.getCell(9).setText(dto.getSeriesRegNumber() != null ? dto.getSeriesRegNumber() : "");

            number++;
        }

        // Optional: Set margins or other formatting
        setMarginBetweenTables(doc);
    }
    private void addAutoTransportTable(String bin, XWPFDocument doc) {
        List<AutoTransportDto> list = transportService.getAutoTransportByBin(bin);
        if (list.isEmpty()) {
            return;
        }

        // Create the title
        createTitle(doc, "Управление автотранспортом");

        // Create the table
        XWPFTable table = doc.createTable();
        table.setWidth("100%");

        // Define column headers
        List<String> columnHeaders = Arrays.asList(
                "№", "Номер автотранспорта", "Марка автотранспорта", "Дата"
        );

        // Add column headers
        XWPFTableRow headerRow = table.getRow(0);
        for (int i = 0; i < columnHeaders.size(); i++) {
            XWPFTableCell cell = headerRow.getCell(i);
            if (cell == null) {
                cell = headerRow.addNewTableCell();
            }
            cell.setText(columnHeaders.get(i));
        }

        // Add data rows
        int number = 1;
        for (AutoTransportDto dto : list) {
            XWPFTableRow dataRow = table.createRow();
            dataRow.getCell(0).setText(String.valueOf(number));
            dataRow.getCell(1).setText(dto.getNumber() != null ? dto.getNumber() : "");
            dataRow.getCell(2).setText(dto.getBrand() != null ? dto.getBrand() : "");
            dataRow.getCell(3).setText(dto.getDate() != null ? dto.getDate().toString() : "");

            number++;
        }

        // Optional: Set margins or other formatting
        setMarginBetweenTables(doc);
    }
    private void addAnotherTransport(String bin, XWPFDocument doc) {
        addAviaTransportTable(bin, doc);
        addTrainTable(bin, doc);
        addWaterTransportTable(bin, doc);
    }
    private void addAviaTransportTable(String bin, XWPFDocument doc) {
        List<AviaTransport> list = transportService.getAviaTransport(bin);
        if (list.isEmpty()) {
            return;
        }

        // Create the title
        createTitle(doc, "Авиатранспорт");

        // Create the table
        XWPFTable table = doc.createTable();
        table.setWidth("100%");

        // Define column headers
        List<String> columnHeaders = Arrays.asList(
                "№", "Дата регистрации", "Марка", "Бортовой номер", "Эксплуатант", "Дата снятия"
        );

        // Add column headers
        XWPFTableRow headerRow = table.getRow(0);
        for (int i = 0; i < columnHeaders.size(); i++) {
            XWPFTableCell cell = headerRow.getCell(i);
            if (cell == null) {
                cell = headerRow.addNewTableCell();
            }
            cell.setText(columnHeaders.get(i));
        }

        // Add data rows
        int number = 1;
        for (AviaTransport dto : list) {
            XWPFTableRow dataRow = table.createRow();
            dataRow.getCell(0).setText(String.valueOf(number));
            dataRow.getCell(1).setText(dto.getDate_registration() != null ? dto.getDate_registration().toString() : "");
            dataRow.getCell(2).setText(dto.getMarka() != null ? dto.getMarka() : "");
            dataRow.getCell(3).setText(dto.getBort() != null ? dto.getBort() : "");
            dataRow.getCell(4).setText(dto.getEkspluatant() != null ? dto.getEkspluatant() : "");
            dataRow.getCell(5).setText(dto.getDate_exclusion() != null ? dto.getDate_exclusion().toString() : "");

            number++;
        }

        // Optional: Set margins or other formatting
        setMarginBetweenTables(doc);
    }
    private void addTrainTable(String bin, XWPFDocument doc) {
        List<Trains> result = transportService.getTrains(bin);
        if (result.isEmpty()) {
            return;
        }

        // Create the title
        createTitle(doc, "Железнодорожный транспорт");

        // Create the table
        XWPFTable table = doc.createTable();
        table.setWidth("100%");

        // Define column headers
        List<String> columnHeaders = Arrays.asList(
                "№", "Дата регистрации", "Тип собственности", "Тип вагона", "Категория вагона",
                "Год выпуска"
        );

        // Add column headers
        XWPFTableRow headerRow = table.getRow(0);
        for (int i = 0; i < columnHeaders.size(); i++) {
            XWPFTableCell cell = headerRow.getCell(i);
            if (cell == null) {
                cell = headerRow.addNewTableCell();
            }
            cell.setText(columnHeaders.get(i));
        }

        // Add data rows
        int number = 1;
        for (Trains dto : result) {
            XWPFTableRow dataRow = table.createRow();
            dataRow.getCell(0).setText(String.valueOf(number));
            dataRow.getCell(1).setText(dto.getDoc_date() != null ? dto.getDoc_date().toString() : "");
            dataRow.getCell(2).setText(dto.getOwnership_type() != null ? dto.getOwnership_type() : "");
            dataRow.getCell(3).setText(dto.getVagon_type() != null ? dto.getVagon_type() : "");
            dataRow.getCell(4).setText(dto.getVagon_category() != null ? dto.getVagon_category() : "");
            dataRow.getCell(5).setText(dto.getVagon_make_year() != null ? dto.getVagon_make_year().toString() : "");

            number++;
        }

        // Optional: Set margins or other formatting
        setMarginBetweenTables(doc);
    }
    private void addWaterTransportTable(String bin, XWPFDocument doc) {
        List<WaterTransport> result = transportService.getWaterTransport(bin);
        if (result.isEmpty()) {
            return;
        }

        // Create the title
        createTitle(doc, "Водный транспорт");

        // Create the table
        XWPFTable table = doc.createTable();
        table.setWidth("100%");

        // Define column headers
        List<String> columnHeaders = Arrays.asList(
                "№", "Дата регистрации", "Цель использования", "Название водного транспорта",
                "Тип водного транспорта", "Год выпуска"
        );

        // Add column headers
        XWPFTableRow headerRow = table.getRow(0);
        for (int i = 0; i < columnHeaders.size(); i++) {
            XWPFTableCell cell = headerRow.getCell(i);
            if (cell == null) {
                cell = headerRow.addNewTableCell();
            }
            cell.setText(columnHeaders.get(i));
        }

        // Add data rows
        int number = 1;
        for (WaterTransport dto : result) {
            XWPFTableRow dataRow = table.createRow();
            dataRow.getCell(0).setText(String.valueOf(number));
            dataRow.getCell(1).setText(dto.getYear_reestr() != null ? dto.getYear_reestr().toString() : "");
            dataRow.getCell(2).setText(dto.getPurpose() != null ? dto.getPurpose() : "");
            dataRow.getCell(3).setText(dto.getType_vt() != null ? dto.getType_vt() : "");
            dataRow.getCell(4).setText(dto.getYear_vt() != null ? dto.getYear_vt().toString() : "");

            number++;
        }

        // Optional: Set margins or other formatting
        setMarginBetweenTables(doc);
    }
    private void addTaxTable(String bin, XWPFDocument doc) {
        List<MvTaxDto> result = taxService.getTaxes(bin);
        if (result.isEmpty()) {
            return;
        }

        // Create the title
        createTitle(doc, "Налоги");

        // Create the table
        XWPFTable table = doc.createTable();
        table.setWidth("100%");

        // Define column headers
        List<String> columnHeaders = Arrays.asList(
                "№", "Код бюджетной классификации", "Наименование КБК", "Дата зачисления в бюджет",
                "Сумма платежа", "Номер платежного документа", "Тип платежа", "Тип проводки",
                "Дата списания со счета", "Код ОГД", "Наименование ОГД"
        );

        // Add column headers
        XWPFTableRow headerRow = table.getRow(0);
        for (int i = 0; i < columnHeaders.size(); i++) {
            XWPFTableCell cell = headerRow.getCell(i);
            if (cell == null) {
                cell = headerRow.addNewTableCell();
            }
            cell.setText(columnHeaders.get(i));
        }

        // Add data rows
        int number = 1;
        for (MvTaxDto dto : result) {
            XWPFTableRow dataRow = table.createRow();
            dataRow.getCell(0).setText(String.valueOf(number)); // Serial number
            dataRow.getCell(1).setText(dto.getCodeOfBudgetClass() != null ? dto.getCodeOfBudgetClass() : "");
            dataRow.getCell(2).setText(dto.getKbk() != null ? dto.getKbk() : "");
            dataRow.getCell(3).setText(dto.getBudgetEnrollmentDate() != null ? dto.getBudgetEnrollmentDate().toString() : "");
            dataRow.getCell(4).setText(dto.getSum() != null ? dto.getSum() : "");
            dataRow.getCell(5).setText(dto.getNumberOfPaymentDoc() != null ? dto.getNumberOfPaymentDoc() : "");
            dataRow.getCell(6).setText(dto.getTypeOfPayment() != null ? dto.getTypeOfPayment() : "");
            dataRow.getCell(7).setText(dto.getTypeOfProvodka() != null ? dto.getTypeOfProvodka() : "");
            dataRow.getCell(8).setText(dto.getDebitDate() != null ? dto.getDebitDate().toString() : "");
            dataRow.getCell(9).setText(dto.getOgdCode() != null ? dto.getOgdCode() : "");
            dataRow.getCell(10).setText(dto.getOgdName() != null ? dto.getOgdName() : "");

            number++;
        }

        // Optional: Set margins or other formatting
        setMarginBetweenTables(doc);
    }

    private void addSubsidyTable(String bin, XWPFDocument doc) {
        List<SubsidiyDTO> result = ulService.getSubsidies(bin);
        if (result.isEmpty()) {
            return;
        }

        // Create the title
        createTitle(doc, "Субсидии");

        // Create the table
        XWPFTable table = doc.createTable();
        table.setWidth("100%");

        // Define column headers
        List<String> columnHeaders = Arrays.asList(
                "№", "Название", "Область", "Дата подачи заявки",
                "Наименование заявителя", "Статус заявки", "Дата принятия заявки",
                "Дата отклонения", "Сумма субсидий, тг.", "Причина отклонения",
                "IP подачи заявки", "IP отзыва заявки", "IP принятия заявки", "IP отклонения заявки"
        );

        // Add column headers
        XWPFTableRow headerRow = table.getRow(0);
        for (int i = 0; i < columnHeaders.size(); i++) {
            XWPFTableCell cell = headerRow.getCell(i);
            if (cell == null) {
                cell = headerRow.addNewTableCell();
            }
            cell.setText(columnHeaders.get(i));
        }

        // Add data rows
        int number = 1;
        for (SubsidiyDTO dto : result) {
            XWPFTableRow dataRow = table.createRow();
            dataRow.getCell(0).setText(String.valueOf(number)); // Serial number
            dataRow.getCell(1).setText(dto.getName() != null ? dto.getName() : "");
            dataRow.getCell(2).setText(dto.getOblast() != null ? dto.getOblast() : "");
            dataRow.getCell(3).setText(dto.getDate() != null ? dto.getDate() : "");
            dataRow.getCell(4).setText(dto.getNameOfTeller() != null ? dto.getNameOfTeller() : "");
            dataRow.getCell(5).setText(dto.getStatus() != null ? dto.getStatus() : "");
            dataRow.getCell(6).setText(dto.getDateOfTaking() != null ? dto.getDateOfTaking() : "");
            dataRow.getCell(7).setText(dto.getDateOfDenying() != null ? dto.getDateOfDenying() : "");
            dataRow.getCell(8).setText(dto.getSum() != null ? dto.getSum() : "");
            dataRow.getCell(9).setText(dto.getDenyingReason() != null ? dto.getDenyingReason() : "");
            dataRow.getCell(10).setText(dto.getIp1() != null ? dto.getIp1() : "");
            dataRow.getCell(11).setText(dto.getIp2() != null ? dto.getIp2() : "");
            dataRow.getCell(12).setText(dto.getIp3() != null ? dto.getIp3() : "");
            dataRow.getCell(13).setText(dto.getIp4() != null ? dto.getIp4() : "");

            number++;
        }

        // Optional: Set margins or other formatting
        setMarginBetweenTables(doc);
    }
    private void addAdministrativeFinesTable(String bin, XWPFDocument doc) {
        List<AdmRightsBreakerDTO> result = ulService.getAdmsFines(bin);
        if (result.isEmpty()) {
            return;
        }

        // Create the title
        createTitle(doc, "Административные штрафы");

        // Create the table
        XWPFTable table = doc.createTable();
        table.setWidth("100%");

        // Define column headers
        List<String> columnHeaders = Arrays.asList(
                "№", "Орган выявивший правонарушение", "Дата заведения",
                "Номер протокола", "Место работы", "Квалификация", "Принудительное исполнение",
                "На срок до", "Размер наложенного штрафа", "Основания прекращения"
        );

        // Add column headers
        XWPFTableRow headerRow = table.getRow(0);
        for (int i = 0; i < columnHeaders.size(); i++) {
            XWPFTableCell cell = headerRow.getCell(i);
            if (cell == null) {
                cell = headerRow.addNewTableCell();
            }
            cell.setText(columnHeaders.get(i));
        }

        // Add data rows
        int number = 1;
        for (AdmRightsBreakerDTO dto : result) {
            XWPFTableRow dataRow = table.createRow();
            dataRow.getCell(0).setText(String.valueOf(number)); // Serial number
            dataRow.getCell(1).setText(dto.getOrgan() != null ? dto.getOrgan() : "");
            dataRow.getCell(2).setText(dto.getDateOfStart() != null ? dto.getDateOfStart() : "");
            dataRow.getCell(3).setText(dto.getNumberOfProtocol() != null ? dto.getNumberOfProtocol() : "");
            dataRow.getCell(4).setText(dto.getPlaceOfWork() != null ? dto.getPlaceOfWork() : "");
            dataRow.getCell(5).setText(dto.getQualification() != null ? dto.getQualification() : "");
            dataRow.getCell(6).setText(dto.getForcedImplementation() != null ? dto.getForcedImplementation() : "");
            dataRow.getCell(7).setText(dto.getPeriodTo() != null ? dto.getPeriodTo() : "");
            dataRow.getCell(8).setText(dto.getSumOfFine() != null ? dto.getSumOfFine() : "");
            dataRow.getCell(9).setText(dto.getReasonOfStopping() != null ? dto.getReasonOfStopping() : "");

            number++;
        }

        // Optional: Set margins or other formatting
        setMarginBetweenTables(doc);
    }
    private void addDubaiPropertyTable(String bin, XWPFDocument doc) {
        List<DubaiRnDto> result = rnService.getDubaiRns(bin);
        if (result.isEmpty()) {
            return;
        }

        // Create the title
        createTitle(doc, "Недвижимость в Дубае");

        // Create the table
        XWPFTable table = doc.createTable();
        table.setWidth("100%");

        // Define column headers
        List<String> columnHeaders = Arrays.asList(
                "№", "Дата покупки", "Площадь", "Название ЖК", "Номер квартиры"
        );

        // Add column headers
        XWPFTableRow headerRow = table.getRow(0);
        for (int i = 0; i < columnHeaders.size(); i++) {
            XWPFTableCell cell = headerRow.getCell(i);
            if (cell == null) {
                cell = headerRow.addNewTableCell();
            }
            cell.setText(columnHeaders.get(i));
        }

        // Add data rows
        int number = 1;
        for (DubaiRnDto dto : result) {
            XWPFTableRow dataRow = table.createRow();
            dataRow.getCell(0).setText(String.valueOf(number)); // Serial number
            dataRow.getCell(1).setText(dto.getDate() != null ? dto.getDate() : "");
            dataRow.getCell(2).setText(dto.getArea() != null ? dto.getArea() : "");
            dataRow.getCell(3).setText(dto.getNameOfZhk() != null ? dto.getNameOfZhk() : "");
            dataRow.getCell(4).setText(dto.getAppartment() != null ? dto.getAppartment() : "");

            number++;
        }

        // Optional: Set margins or other formatting
        setMarginBetweenTables(doc);
    }
    private void addRealEstateRegistryTable(String bin, XWPFDocument doc) {
        List<RnDTO> list = rnService.getRns(bin);
        if (list.isEmpty()) {
            return;
        }

        // Create the title
        createTitle(doc, "Сведения по реестру недвижимости");

        // Create the table
        XWPFTable table = doc.createTable();
        table.setWidth("100%");

        // Define column headers
        List<String> columnHeaders = Arrays.asList(
                "№", "Кадастровый номер", "Адрес", "Правообладатель", "Этажность",
                "Количество составляющих", "Площадь общая", "Вид документа", "Номер документа",
                "Дата документа", "Сумма сделки", "Жилая площадь", "Статус"
        );

        // Add column headers
        XWPFTableRow headerRow = table.getRow(0);
        for (int i = 0; i < columnHeaders.size(); i++) {
            XWPFTableCell cell = headerRow.getCell(i);
            if (cell == null) {
                cell = headerRow.addNewTableCell();
            }
            cell.setText(columnHeaders.get(i));
        }

        // Add data rows
        int number = 1;
        for (RnDTO dto : list) {
            XWPFTableRow dataRow = table.createRow();
            dataRow.getCell(0).setText(String.valueOf(number)); // Serial number
            dataRow.getCell(1).setText(dto.getCadastrialNumber() != null ? dto.getCadastrialNumber() : "");
            dataRow.getCell(2).setText(dto.getAddress() != null ? dto.getAddress() : "");
            dataRow.getCell(3).setText(dto.getRightOwner() != null ? dto.getRightOwner() : "");
            dataRow.getCell(4).setText(dto.getFloorness() != null ? dto.getFloorness() : "");
            dataRow.getCell(5).setText(""); // Placeholder for "Количество составляющих"
            dataRow.getCell(6).setText(dto.getAllArea() != null ? dto.getAllArea() : "");
            dataRow.getCell(7).setText(dto.getTypeOfDoc() != null ? dto.getTypeOfDoc() : "");
            dataRow.getCell(8).setText(dto.getDocumentNumber() != null ? dto.getDocumentNumber() : "");
            dataRow.getCell(9).setText(dto.getDate() != null ? dto.getDate() : "");
            dataRow.getCell(10).setText(dto.getSumOfDeal() != null ? dto.getSumOfDeal() : "");
            dataRow.getCell(11).setText(dto.getLivingArea() != null ? dto.getLivingArea() : "");
            dataRow.getCell(12).setText(dto.getStatusRn() != null ? dto.getStatusRn() : "");

            number++;
        }

        // Optional: Set margins or other formatting
        setMarginBetweenTables(doc);
    }
    private void addFno240PropertyTable(String bin, XWPFDocument doc) {
        List<Fno240> result = rnService.getFno240s(bin);
        if (result.isEmpty()) {
            return;
        }

        // Create the title
        createTitle(doc, "Имущество по ФНО 240");

        // Create the table
        XWPFTable table = doc.createTable();
        table.setWidth("100%");

        // Define column headers
        List<String> columnHeaders = Arrays.asList(
                "№", "Вид имущества", "Адрес", "Доп.информация"
        );

        // Add column headers
        XWPFTableRow headerRow = table.getRow(0);
        for (int i = 0; i < columnHeaders.size(); i++) {
            XWPFTableCell cell = headerRow.getCell(i);
            if (cell == null) {
                cell = headerRow.addNewTableCell();
            }
            cell.setText(columnHeaders.get(i));
        }

        // Add data rows
        int number = 1;
        for (Fno240 dto : result) {
            XWPFTableRow dataRow = table.createRow();
            dataRow.getCell(0).setText(String.valueOf(number)); // Serial number
            dataRow.getCell(1).setText(dto.getTyp() != null ? dto.getTyp() : "");
            dataRow.getCell(2).setText(dto.getAddress() != null ? dto.getAddress() : "");
            dataRow.getCell(3).setText(dto.getComments() != null ? dto.getComments() : "");

            number++;
        }

        // Optional: Set margins or other formatting
        setMarginBetweenTables(doc);
    }
    private void addFno250Table(String bin, XWPFDocument doc) {
        List<Fno250> result = rnService.getFno250s(bin);
        if (result.isEmpty()) {
            return;
        }

        // Create the title
        createTitle(doc, "Имущество по ФНО 250");

        // Create the table
        XWPFTable table = doc.createTable();
        table.setWidth("100%");

        // Define column headers
        List<String> columnHeaders = Arrays.asList(
                "№", "Тип недвижимости", "Код страны", "Адрес"
        );

        // Add column headers
        XWPFTableRow headerRow = table.getRow(0);
        for (int i = 0; i < columnHeaders.size(); i++) {
            XWPFTableCell cell = headerRow.getCell(i);
            if (cell == null) {
                cell = headerRow.addNewTableCell();
            }
            cell.setText(columnHeaders.get(i));
        }

        // Add data rows
        int number = 1;
        for (Fno250 dto : result) {
            XWPFTableRow dataRow = table.createRow();
            dataRow.getCell(0).setText(String.valueOf(number)); // Serial number
            dataRow.getCell(1).setText(dto.getTyp() != null ? dto.getTyp() : "");
            dataRow.getCell(2).setText(dto.getCode_country() != null ? dto.getCode_country() : "");
            dataRow.getCell(3).setText(dto.getAddress() != null ? dto.getAddress() : "");

            number++;
        }

        // Optional: Set margins or other formatting
        setMarginBetweenTables(doc);
    }
    private void addFno250AvtoTable(String bin, XWPFDocument doc) {
        List<Fno250Avto> result = rnService.getFno250Avtos(bin);
        if (result.isEmpty()) {
            return;
        }

        // Create the title
        createTitle(doc, "Автотранспорт по ФНО 250");

        // Create the table
        XWPFTable table = doc.createTable();
        table.setWidth("100%");

        // Define column headers
        List<String> columnHeaders = Arrays.asList(
                "№", "Марка", "Код страны", "VIN код"
        );

        // Add column headers
        XWPFTableRow headerRow = table.getRow(0);
        for (int i = 0; i < columnHeaders.size(); i++) {
            XWPFTableCell cell = headerRow.getCell(i);
            if (cell == null) {
                cell = headerRow.addNewTableCell();
            }
            cell.setText(columnHeaders.get(i));
        }

        // Add data rows
        int number = 1;
        for (Fno250Avto dto : result) {
            XWPFTableRow dataRow = table.createRow();
            dataRow.getCell(0).setText(String.valueOf(number)); // Serial number
            dataRow.getCell(1).setText(dto.getMarka() != null ? dto.getMarka() : "");
            dataRow.getCell(2).setText(dto.getCode_country() != null ? dto.getCode_country() : "");
            dataRow.getCell(3).setText(dto.getVin_code() != null ? dto.getVin_code() : "");

            number++;
        }

        // Optional: Set margins or other formatting
        setMarginBetweenTables(doc);
    }
    private void addFno250CompanyTable(String bin, XWPFDocument doc) {
        List<Fno250Company> result = rnService.getFno250Companies(bin);
        if (result.isEmpty()) {
            return;
        }

        // Create the title
        createTitle(doc, "Компании по ФНО 250");

        // Create the table
        XWPFTable table = doc.createTable();
        table.setWidth("100%");

        // Define column headers
        List<String> columnHeaders = Arrays.asList(
                "№", "Наименование компании", "Код страны", "Доля"
        );

        // Add column headers
        XWPFTableRow headerRow = table.getRow(0);
        for (int i = 0; i < columnHeaders.size(); i++) {
            XWPFTableCell cell = headerRow.getCell(i);
            if (cell == null) {
                cell = headerRow.addNewTableCell();
            }
            cell.setText(columnHeaders.get(i));
        }

        // Add data rows
        int number = 1;
        for (Fno250Company dto : result) {
            XWPFTableRow dataRow = table.createRow();
            dataRow.getCell(0).setText(String.valueOf(number)); // Serial number
            dataRow.getCell(1).setText(dto.getName_company() != null ? dto.getName_company() : "");
            dataRow.getCell(2).setText(dto.getCode_country() != null ? dto.getCode_country() : "");
            dataRow.getCell(3).setText(dto.getDolya() != null ? dto.getDolya() : "");

            number++;
        }

        // Optional: Set margins or other formatting
        setMarginBetweenTables(doc);
    }
    private void addFno250DepositTable(String bin, XWPFDocument doc) {
        List<Fno250Deposit> result = rnService.getFno250Deposit(bin);
        if (result.isEmpty()) {
            return;
        }

        // Create the title
        createTitle(doc, "Депозит по ФНО 250");

        // Create the table
        XWPFTable table = doc.createTable();
        table.setWidth("100%");

        // Define column headers
        List<String> columnHeaders = Arrays.asList(
                "№", "Название банка", "Код страны", "Код валюты", "Сумма"
        );

        // Add column headers
        XWPFTableRow headerRow = table.getRow(0);
        for (int i = 0; i < columnHeaders.size(); i++) {
            XWPFTableCell cell = headerRow.getCell(i);
            if (cell == null) {
                cell = headerRow.addNewTableCell();
            }
            cell.setText(columnHeaders.get(i));
        }

        // Add data rows
        int number = 1;
        for (Fno250Deposit dto : result) {
            XWPFTableRow dataRow = table.createRow();
            dataRow.getCell(0).setText(String.valueOf(number)); // Serial number
            dataRow.getCell(1).setText(dto.getBank() != null ? dto.getBank() : "");
            dataRow.getCell(2).setText(dto.getCode_country() != null ? dto.getCode_country() : "");
            dataRow.getCell(3).setText(dto.getCurrency() != null ? dto.getCurrency() : "");
            dataRow.getCell(4).setText(dto.getSumma() != null ? dto.getSumma() : "");

            number++;
        }

        // Optional: Set margins or other formatting
        setMarginBetweenTables(doc);
    }

}