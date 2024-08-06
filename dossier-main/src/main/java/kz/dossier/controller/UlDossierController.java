package kz.dossier.controller;

import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import kz.dossier.dto.*;
import kz.dossier.modelsDossier.AviaTransport;
import kz.dossier.modelsDossier.SearchResultModelUl;
import kz.dossier.modelsDossier.Trains;
import kz.dossier.modelsDossier.WaterTransport;
import kz.dossier.security.models.log;
import kz.dossier.security.repository.LogRepo;
import kz.dossier.security.services.LogsService;
import kz.dossier.service.*;
import kz.dossier.tools.ULExportPDFService;
import kz.dossier.tools.UlDocxGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3000)
@RestController
@RequestMapping("/api/pandora/dossier")
public class UlDossierController {
    @Autowired
    ULService ulService;
    @Autowired
    LogRepo logRepo;
    @Autowired
    RnService rnService;
    @Autowired
    ULService ulAdditionalService;
    @Autowired
    LogsService logsService;
    @Autowired
    ULExportPDFService ulExportPDFService;
    @Autowired
    UlDocxGenerator ulDocxGenerator;
    @Autowired
    UlRiskServiceImpl ulRiskService;
    @Autowired
    TransportService transportService;
    @Autowired
    TaxService taxService;

    //Сведения о ЮЛ надо посчитать проценты сведении и рисков
    @GetMapping("/ul/get")
    public ResponseEntity<ULDto> ulDtoByBin(@RequestParam String bin, Principal principal) {
        try {
            if (bin == null || bin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            ULDto ulDto = ulService.getUlByBin(bin);
            if (ulDto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            String email = principal.getName();
            log log = new log();
            log.setDate(LocalDateTime.now());
            log.setObwii("Поиск ЮЛ в Досье: " + bin);
            log.setUsername(email);
            logRepo.save(log);
            return ResponseEntity.ok(ulDto);
        } catch (Exception e) {
            log.error("Error occurred while fetching ULDto by bin: " + bin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    //Отечественные товаропроизводители
    @GetMapping("/ul/get-commodity-producers-by-iinBin")
    public ResponseEntity<List<CommodityProducersDTO>> ulCommodProducersByBin(@RequestParam String bin) {
        try {
            if (bin == null || bin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<CommodityProducersDTO> dto = ulService.getComProducersByBin(bin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching Commodity Producers by bin: " + bin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    //UL participants
    @GetMapping("/ul/get-ul-participants-by-bin")
    public ResponseEntity<List<ULULMemberDTO>> getULMembersByBin(@RequestParam String bin) {
        try {
            if (bin == null || bin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<ULULMemberDTO> dto = ulService.getULMembersByBin(bin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching Ul participants by bin: " + bin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    //Ul on same address
    @GetMapping("/ul/get-ul-same-address-by-bin")
    public ResponseEntity<List<SameULRegAddressDto>> getSameAddressULByBin(@RequestParam String bin) {
        try {
            if (bin == null || bin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<SameULRegAddressDto> dto = ulService.getSameAddressULByBin(bin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching SameULRegAddressDto by bin: " + bin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/ul/get-ul-fpg")
    public ResponseEntity<List<FPGDto>> getFpgsByBin(@RequestParam String bin) {
        try {
            if (bin == null || bin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<FPGDto> dto = ulService.getFpgsByBin(bin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching FPGDto by bin: " + bin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/ul/get-ul-accountants")
    public ResponseEntity<List<AccountantDto>> getAccountantsByBin(@RequestParam String bin) {
        try {
            if (bin == null || bin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<AccountantDto> dto = ulService.getAccountantsByBin(bin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching AccountantDto by bin: " + bin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/ul/get-ul-pdls")
    public ResponseEntity<List<PdlDto>> getPdlByBin(@RequestParam String bin) {
        try {
            if (bin == null || bin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<PdlDto> dto = ulService.getPdlByBin(bin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching PdlDto by bin: " + bin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    //Pension grouped by year
    @GetMapping("/ul/get-ul-pension")
    public ResponseEntity<List<PensionDto>> getPensionByBin(@RequestParam String bin) {
        try {
            if (bin == null || bin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<PensionDto> dto = ulService.getPensionByBin(bin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching PensionDto by bin: " + bin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    //Pension details by year and bin

    //Contacts
    @GetMapping("/ul/get-ul-contacts")
    public ResponseEntity<List<ContactDetailDto>> getContactsByBin(@RequestParam String bin) {
        try {
            if (bin == null || bin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<ContactDetailDto> dto = ulService.getContactsByBin(bin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching ContactDetailDto by bin: " + bin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    //Equipment
    @GetMapping("/ul/get-ul-equipments")
    public ResponseEntity<List<EquipmentDto>> getEquimpentByBin(@RequestParam String bin) {
        try {
            if (bin == null || bin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<EquipmentDto> dto = transportService.getEquimpentByBin(bin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching EquipmentDto by bin: " + bin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/ul/get-ul-transports")
    public ResponseEntity<List<MvAutoDto>> getTransportByBin(@RequestParam String bin) {
        try {
            if (bin == null || bin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<MvAutoDto> dto = transportService.getTransportByBin(bin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching EquipmentDto by bin: " + bin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/ul/get-ul-auto-transport")
    public ResponseEntity<List<AutoTransportDto>> getAutoTransportByBin(@RequestParam String bin) {
        try {
            if (bin == null || bin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<AutoTransportDto> dto = transportService.getAutoTransportByBin(bin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching AutoTransportDto by bin: " + bin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/ul/get-trains")
    public ResponseEntity<List<Trains>> getTrains(@RequestParam String bin) {
        try {
            if (bin == null || bin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<Trains> dto = transportService.getTrains(bin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching Trains by bin: " + bin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/ul/get-avia-transport")
    public ResponseEntity<List<AviaTransport>> getAviaTransport(@RequestParam String bin) {
        try {
            if (bin == null || bin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<AviaTransport> dto = transportService.getAviaTransport(bin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching AviaTransport by bin: " + bin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/ul/get-water-transport")
    public ResponseEntity<List<WaterTransport>> getWaterTransport(@RequestParam String bin) {
        try {
            if (bin == null || bin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<WaterTransport> dto = transportService.getWaterTransport(bin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching WaterTransport by bin: " + bin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    //Tax
    @GetMapping("/ul/tax")
    public ResponseEntity<List<TaxViewDto>> getTaxesView(@RequestParam String bin) {
        try {
            if (bin == null || bin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<TaxViewDto> dto = taxService.getTaxView(bin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching TaxViewDto by bin: " + bin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/ul/tax-pages-num")
    public ResponseEntity<Integer> getNumberOfTaxPages(@RequestParam String bin, @RequestParam Integer year) {
        try {
            if (bin == null || bin.isEmpty() || year == null ) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            Integer number = taxService.getNumberOfTaxPages(bin, year);
            return ResponseEntity.ok(number);
        } catch (Exception e) {
            log.error("Error occurred while fetching number by bin: " + bin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/ul/get-tax-by-bin")
    public ResponseEntity<List<MvTaxDto>> getTaxes(@RequestParam String bin,  @RequestParam Integer year,  @RequestParam Integer page) {
        try {
            if (bin == null || bin.isEmpty() || year == null ) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<MvTaxDto> dto = taxService.getTaxesWithPages(bin, year, page);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching MvTaxDto by bin: " + bin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/ul/get-adm-fines")
    public ResponseEntity<List<AdmRightsBreakerDTO>> getAdmsFines(@RequestParam String bin) {
        try {
            if (bin == null || bin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<AdmRightsBreakerDTO> dto = ulService.getAdmsFines(bin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching AdmRightsBreakerDTO by bin: " + bin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/ul/rn-pages")
    public ResponseEntity<List<RnListDto>> getRnPages(@RequestParam String bin, @RequestParam Integer page) {
        try {
            if (bin == null || bin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<RnListDto> dto = rnService.getRnPages(bin, page);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching AdmRightsBreakerDTO by bin: " + bin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }




    //    @GetMapping("/ulCard")
//    public UlCardDTO getUlCard(@RequestParam String bin) {
//        return myService.getUlCard(bin);
//    }
//    @GetMapping("/ulAddresses")
//    public UlAddressInfo getUlAddresses(@RequestParam String bin) {
//        return myService.getUlAddresses(bin);
//    }
    @GetMapping("/additionalInfoUL")
    public ULAdditionalInfoDTO getAdditionalUL(String bin, Principal principal) {
        String email = principal.getName();
        log log = new log();
        log.setDate(LocalDateTime.now());
        log.setObwii("Искал ЮЛ в Досье" + ": " + bin);
        log.setUsername(email);
        logRepo.save(log);
        return ulAdditionalService.additionalByBin(bin);
    }
//    @GetMapping("/generalInfoUl")
//    public ULGeneralInfoDTO getGeneralInfoUl(@RequestParam String bin) {
//        return myService.getUlGeneral(bin);
//    }

//    @GetMapping("/getRiskByBin")
//    public UlRiskDTO getRiskBin(@RequestParam String bin){
//        return ulRiskService.findULRiskByIin(bin);
//    }
//    @GetMapping("/bin")
//    public List<SearchResultModelUl> findByBin(@RequestParam String bin, @RequestParam String email) {
//        log log = new log();
//        log.setDate(LocalDateTime.now());
//        log.setObwii("Искал в досье " + email + ": " + bin);
//        log.setUsername(email);
//        logRepo.save(log);
//        return myService.searchResultUl(bin);
//    }
//
//    @GetMapping("/binname")
//    public List<SearchResultModelUl> findBinByName(@RequestParam String name) {
//        return myService.searchUlByName(name.replace('$', '%'));
//    }
//
//

    @GetMapping(value = "/downloadUlPdf/{bin}", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] generateUlPdfFile(HttpServletResponse response, @PathVariable("bin")String bin) throws IOException, DocumentException {
        response.setContentType("application/pdf");
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=doc" + ".pdf";
        response.setHeader(headerkey, headervalue);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ulExportPDFService.generate(bin, baos);
        return baos.toByteArray();
    }
    @GetMapping(value = "/downloadUlDoc/{bin}")
    public byte[] generateUlWordFile(HttpServletResponse response, @PathVariable("bin")String bin) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=document.docx";
        response.setHeader(headerkey,headervalue);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ulDocxGenerator.generateUl(bin, baos);
        return baos.toByteArray();
    }
}
