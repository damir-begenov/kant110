package kz.dossier.controller;


import com.lowagie.text.*;

import kz.dossier.dto.*;
import jakarta.servlet.http.HttpServletResponse;
import kz.dossier.modelsDossier.*;
import kz.dossier.repositoryDossier.EsfAll2Repo;
import kz.dossier.repositoryDossier.FlRelativesRepository;
import kz.dossier.repositoryDossier.MvAutoFlRepo;
import kz.dossier.repositoryDossier.NewPhotoRepo;
import kz.dossier.security.models.log;
import kz.dossier.security.repository.LogRepo;
import kz.dossier.security.services.LogsService;
import kz.dossier.service.*;
import kz.dossier.tools.DocxGenerator;
import kz.dossier.tools.PdfGenerator;
import kz.dossier.tools.ULExportPDFService;
import kz.dossier.tools.UlDocxGenerator;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.xpath.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;






@CrossOrigin(origins = "*", maxAge = 3000)
@RestController
@RequestMapping("/api/pandora/dossier")
public class DoseirController {
    @Autowired
    NewPhotoRepo newPhotoRepo;
    @Autowired
    EsfAll2Repo esfAll2Repo;
    @Autowired
    MvAutoFlRepo mvAutoFlRepo;
    @Autowired
    MyService myService;
    @Autowired
    FlRelativesRepository relativesRepository;
    @Autowired
    LogRepo logRepo;
    @Autowired
    FlRiskServiceImpl flRiskService;
    @Autowired
    PdfGenerator pdfGenerator;
    @Autowired
    DocxGenerator docxGenerator;
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
    FlService flService;
    @Autowired
    TransportService transportService;
    @Autowired
    ULService ulService;
    @GetMapping("/fl/get-commodity-producers-by-iinBin")
    public ResponseEntity<List<CommodityProducersDTO>> ulCommodProducersByBin(@RequestParam String iin) {
        try {
            if (iin == null || iin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<CommodityProducersDTO> dto = ulService.getComProducersByBin(iin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching Commodity Producers by bin: " + iin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    } @GetMapping("/fl/get-fl-auto-transport")
    public ResponseEntity<List<AutoTransportDto>> getAutoTransportByBinfl(@RequestParam String iin) {
        try {
            if (iin == null || iin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<AutoTransportDto> dto = transportService.getAutoTransportByBin(iin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching AutoTransportDto by bin: " + iin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/fl/get-trains")
    public ResponseEntity<List<Trains>> getTrainsfl(@RequestParam String iin) {
        try {
            if (iin == null || iin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<Trains> dto = transportService.getTrains(iin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching Trains by bin: " + iin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/fl/get-avia-transport")
    public ResponseEntity<List<AviaTransport>> getAviaTransportfl(@RequestParam String iin) {
        try {
            if (iin == null || iin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<AviaTransport> dto = transportService.getAviaTransport(iin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching AviaTransport by bin: " + iin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/fl/get-water-transport")
    public ResponseEntity<List<WaterTransport>> getWaterTransportfl(@RequestParam String iin) {
        try {
            if (iin == null || iin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<WaterTransport> dto = transportService.getWaterTransport(iin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching WaterTransport by bin: " + iin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/get-fl-by-iin")
    public ResponseEntity<MvFlWithPhotoDto> getMvFl(@RequestParam String iin){
        try {
            if (iin == null || iin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            MvFlWithPhotoDto dto = flService.getMvFl(iin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching MvFl by iin: " + iin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/get-fl-doc-iin")
    public ResponseEntity<List<MvIinDoc>> getMvFlDoc(@RequestParam String iin){
        try {
            if (iin == null || iin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<MvIinDoc> dto = flService.getMvDocs(iin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching MvIinDoc by iin: " + iin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/get-fl-address-iin")
    public ResponseEntity<List<MvFlAddress>> getMvAddress(@RequestParam String iin){
        try {
            if (iin == null || iin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<MvFlAddress> dto = flService.getMvFlAddressByIIN(iin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching MvIinDoc by iin: " + iin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }



    @GetMapping("/get-fl-reg-address-iin")
    public ResponseEntity<List<RegistrationTemp>> getRegAddressFl(@RequestParam String iin){
        try {
            if (iin == null || iin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<RegistrationTemp> dto = flService.getRegAddressByIIN(iin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching MvIinDoc by iin: " + iin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/get-fl-historicity-fio")
    public ResponseEntity<List<ChangeFio>> getChangeFio(@RequestParam String iin){
        try {
            if(iin == null || iin.isEmpty()){
                return ResponseEntity.badRequest().body(null);
            }
            List<ChangeFio> dto = flService.getChangeFioByIIN(iin);
            if (dto == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(dto);
        }catch (Exception e){
            log.error("Error occurred while fetching MvIinDoc by iin: " + iin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/get-contacts-details")
    public ResponseEntity<List<FlContacts>> getFlContacts(@RequestParam String iin){
        try {
            if(iin == null || iin.isEmpty()){
                return ResponseEntity.badRequest().body(null);
            }
            List<FlContacts> dto = flService.getContactsByIIN(iin);
            if (dto == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(dto);
        }catch (Exception e){
            log.error("Error occurred while fetching MvIinDoc by iin: " + iin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/fl-lawyers")
    public ResponseEntity<List<AdvocateListEntity>> getAdvocate(@RequestParam String iin){
        try {
            if(iin == null || iin.isEmpty()){
                return ResponseEntity.badRequest().body(null);
            }
            List<AdvocateListEntity> dto = flService.getAdvocateListEntity(iin);
            if (dto == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(dto);
        }catch (Exception e){
            log.error("Error occurred while fetching MvIinDoc by iin: " + iin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/get-accountant")
    public ResponseEntity<List<AccountantListEntity>> getAccountant(@RequestParam String iin){
        try {
            if(iin == null || iin.isEmpty()){
                return ResponseEntity.badRequest().body(null);
            }
            List<AccountantListEntity> dto = flService.getAccountantListEntity(iin);
            if (dto == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(dto);
        }catch (Exception e){
            log.error("Error occurred while fetching MvIinDoc by iin: " + iin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/get-fl-bailiff-iin")
    public ResponseEntity<List<BailiffListEntity>> getBailiffListEntity(@RequestParam String iin){
        try {
            if(iin == null || iin.isEmpty()){
                return ResponseEntity.badRequest().body(null);
            }
            List<BailiffListEntity> dto = flService.getBailiffListEntity(iin);
            if (dto == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(dto);
        }catch (Exception e){
            log.error("Error occurred while fetching MvIinDoc by iin: " + iin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/get-fl-auditor-iin")
    public ResponseEntity<List<AuditorsListEntity>> getAuditorsListEntity(@RequestParam String iin){
        try {
            if(iin == null || iin.isEmpty()){
                return ResponseEntity.badRequest().body(null);
            }
            List<AuditorsListEntity> dto = flService.getAuditorsListEntity(iin);
            if (dto == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(dto);
        }catch (Exception e){
            log.error("Error occurred while fetching MvIinDoc by iin: " + iin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/get-fl-ipgo-iin")
    public ResponseEntity<List<IpgoEmailEntity>> getIpgoEmailEntity(@RequestParam String iin){
        try {
            if(iin == null || iin.isEmpty()){
                return ResponseEntity.badRequest().body(null);
            }
            List<IpgoEmailEntity> dto = flService.getIpgoEmailEntity(iin);
            if (dto == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(dto);
        }catch (Exception e){
            log.error("Error occurred while fetching MvIinDoc by iin: " + iin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }


    @GetMapping("/get-fl-ip-and-kx-iin")
    public ResponseEntity<IpKxDto> getIpKxDto(@RequestParam String iin){
        try {
            if(iin == null || iin.isEmpty()){
                return ResponseEntity.badRequest().body(null);
            }
            IpKxDto dto = flService.getIpKxDto(iin);
            if (dto == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(dto);
        }catch (Exception e){
            log.error("Error occurred while fetching MvIinDoc by iin: " + iin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/get-equipments")
    public ResponseEntity<List<Equipment>> getEquipment(@RequestParam String iin){
        try {
            if(iin == null || iin.isEmpty()){
                return ResponseEntity.badRequest().body(null);
            }
            List<Equipment> dto = transportService.getEquipmentByIin(iin);
            if (dto == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(dto);
        }catch (Exception e){
            log.error("Error occurred while fetching MvIinDoc by iin: " + iin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/get-education")
    public ResponseEntity<EduDto> getEduDto(@RequestParam String iin){
        try {
            if(iin == null || iin.isEmpty()){
                return ResponseEntity.badRequest().body(null);
            }
            EduDto dto = flService.getEduDto(iin);
            if (dto == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(dto);
        }catch (Exception e){
            log.error("Error occurred while fetching MvIinDoc by iin: " + iin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/get-pension")
    public ResponseEntity<PensionAllDto> getPensionAllDto(@RequestParam String iin){
        try {
            if(iin == null || iin.isEmpty()){
                return ResponseEntity.badRequest().body(null);
            }
            PensionAllDto dto = flService.getPensionAllDto(iin);
            if (dto == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(dto);
        }catch (Exception e){
            log.error("Error occurred while fetching MvIinDoc by iin: " + iin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/get-fl-military")
    public ResponseEntity<List<MilitaryAccountingDTO>> getMilitaryAccountingDTO(@RequestParam String iin){
        try {
            if(iin == null || iin.isEmpty()){
                return ResponseEntity.badRequest().body(null);
            }
            List<MilitaryAccountingDTO> dto = flService.getMilitaryAccountingDTO(iin);
            if (dto == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(dto);
        }catch (Exception e){
            log.error("Error occurred while fetching MvIinDoc by iin: " + iin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/get-ul-participants")
    public ResponseEntity<UlParticipantsDto> getUlParticipantsDto(@RequestParam String iin){
        try {
            if(iin == null || iin.isEmpty()){
                return ResponseEntity.badRequest().body(null);
            }
            UlParticipantsDto dto = flService.getUlParticipantsDto(iin);
            if (dto == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(dto);
        }catch (Exception e){
            log.error("Error occurred while fetching MvIinDoc by iin: " + iin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/get-fl-rn")
    public ResponseEntity<List<MvRnOld>> getMvRnOld(@RequestParam String iin){
        try {
            if(iin == null || iin.isEmpty()){
                return ResponseEntity.badRequest().body(null);
            }
            List<MvRnOld> dto = flService.getMvRnOlds(iin);
            if (dto == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(dto);
        }catch (Exception e){
            log.error("Error occurred while fetching MvIinDoc by iin: " + iin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/get-fl-pdls")
    public ResponseEntity<List<PdlDto>> getPdlByIin(@RequestParam String iin) {
        try {
            if (iin == null || iin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<PdlDto> dto = flService.getPdlByIin(iin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching PdlDto by bin: " + iin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/get-fl-transports")
    public ResponseEntity<List<MvAutoDto>> getTransportByFl(@RequestParam String iin) {
        try {
            if (iin == null || iin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<MvAutoDto> dto = transportService.getTransportByBin(iin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching EquipmentDto by bin: " + iin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/sameAddressFl")
    public ResponseEntity<RnFlSameAddressDto> sameAddressFls(@RequestParam String iin) {
        try {
            if (iin == null || iin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            RnFlSameAddressDto dto = myService.getByAddressUsingIin(iin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching EquipmentDto by bin: " + iin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

//    @GetMapping("/getRiskQuantity")
//    public ResponseEntity<Integer> getRiskQuantity(@RequestParam  String iin){
//        try {
//            if (iin == null || iin.isEmpty()){
//                return ResponseEntity.badRequest().body(null);
//            }
//            Integer dto = flRiskService.findFlRiskByIin(iin).getQuantity();
//            if(dto == null){
//                return ResponseEntity.notFound().build();
//            }
//            return ResponseEntity.ok(dto);
//        }catch (Exception e){
//            log.error("Error occurred while fetching getRiskQuantity by bin: " + iin, e);
//
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(null);
//        }
//    }




















    @GetMapping("/sameAddressUl")
    public List<SearchResultModelUl> sameAddressUls(@RequestParam String bin) {
        return myService.getByAddress(bin);
    }

    @GetMapping("/rnDetails")
    public List<RnDTO> getMethodName(@RequestParam String cadastral, @RequestParam String address) {
        return rnService.getDetailedRnView(cadastral, address);
    }

    @GetMapping("/generalInfo")
    public GeneralInfoDTO getGeneralInfo(@RequestParam String iin, Principal principal) {
        String email = principal.getName();

        log log = new log();
        log.setDate(LocalDateTime.now());
        log.setObwii("Искал ФЛ в Досье" + ": " + iin);
        log.setUsername(email);
        logRepo.save(log);
        return myService.generalInfoByIin(iin);
    }

    @GetMapping("/additionalInfo")
    public AdditionalInfoDTO getAdditionalInfo(@RequestParam String iin) {
        return myService.additionalInfoByIin(iin);
    }

    @GetMapping("/getRelativesInfo")
    public List<FlRelativiesDTO> getRelInfo(@RequestParam String iin){
        return myService.getRelativesInfo(iin);
    }

    @GetMapping("/pensionDetails")
    public List<PensionListDTO> getPesionDetails(@RequestParam String iin, @RequestParam String bin, @RequestParam String year) {
        return myService.getPensionDetails(iin, bin, year);
    }


//    @GetMapping("/relativesInfo")
//    public List<FlRelatives> getRelativesInfo(@RequestParam String iin){
//        return myService.getFlRelativesInfo();
//    }

    @GetMapping("/profile")
    public NodesFL getProfile(@RequestParam String iin) {
        return myService.getNode(iin);
    }

    @GetMapping("/getRiskByIin")
    public FLRiskDto getRisk(@RequestParam String iin){
        return flRiskService.findFlRiskByIin(iin);
    }
    @GetMapping("/getFirstRowByIin")
    public FlFirstRowDto getFirstRow(@RequestParam String iin){
        return myService.getFlFirstRow(iin);
    }
    @GetMapping("/cc")
    public NodesUL getChfc(@RequestParam String bin) {
        NodesUL ss = myService.getNodeUL(bin);
        return ss;
    }
    @GetMapping("/taxpage")
    public List<TaxOutEntity> getTax(@RequestParam String bin, @RequestParam(required = false,defaultValue = "0") int page, @RequestParam(required = false,defaultValue = "10") int size) {
        return myService.taxOutEntities(bin,PageRequest.of(page,size));
    }

    @GetMapping("/pensionUl")
    public List<Map<String, Object>> pensionUl(@RequestParam String bin, @RequestParam String year, @RequestParam(required = false,defaultValue = "0") int page, @RequestParam(required = false,defaultValue = "10") int size) {
        return myService.pensionEntityUl(bin, year, PageRequest.of(page,size));
    }

    @GetMapping("/pensionsbyyear")
    public List<Map<String,Object>> pensionUl1(@RequestParam String bin, @RequestParam Double year, @RequestParam Integer page) {
        return myService.pensionEntityUl1(bin, year, page);
    }
    @GetMapping("/hierarchy")
    public FlRelativesLevelDto hierarchy(@RequestParam String iin) throws SQLException {
        return myService.createHierarchyObject(iin);
    }
    @GetMapping("/bin")
    public List<SearchResultModelUl> findByBin(@RequestParam String bin, @RequestParam String email) {
        log log = new log();
        log.setDate(LocalDateTime.now());
        log.setObwii("Искал в досье " + email + ": " + bin);
        log.setUsername(email);
        logRepo.save(log);
        return myService.searchResultUl(bin);
    }
    @GetMapping("/iin")
    public List<SearchResultModelFL> getByIIN(@RequestParam String iin, @RequestParam String email) throws IOException {
//        List<SearchResultModelFL> fl = myService.getByIIN_photo(iin);
        log log = new log();
        log.setDate(LocalDateTime.now());
        log.setObwii("Искал в досье " + email + ": " + iin);
        log.setUsername(email);
        logRepo.save(log);
        return myService.getByIIN_photo(iin);
    }

    @GetMapping("/nomer_doc")
    public List<SearchResultModelFL> getByDoc(@RequestParam String doc) {
        return myService.getByDoc_photo(doc);
    }

    @GetMapping("/additionalfio")
    public List<SearchResultModelFL> getByAdditions(@RequestParam HashMap<String, String> req) {
        System.out.println(req);
        return myService.getWIthAddFields(req);
    }

    @GetMapping("/byphone")
    public List<SearchResultModelFL> getByPhone(@RequestParam String phone) {
        return myService.getByPhone(phone);
    }
    @GetMapping("/byemail")
    public List<SearchResultModelFL> getByEmail(@RequestParam String email) {
        return myService.getByEmail(email);
    }

    @GetMapping("/byvinkuzov")
    public List<SearchResultModelFL> getByVinKuzov(@RequestParam String vin) {
        return myService.getByVinFl(vin.toUpperCase());
    }
    @GetMapping("/byvinkuzovul")
    public List<SearchResultModelUl> getByVinKuzovUl(@RequestParam String vin) {
        return myService.getByVinUl(vin.toUpperCase());
    }

    @GetMapping("/fio")
    public List<SearchResultModelFL> findByFIO(@RequestParam String i, @RequestParam String o, @RequestParam String f) {
        log log = new log();
        log.setDate(LocalDateTime.now());
//        log.setObwii("Искал в досье " + email + ": " + f + " " + i + " " + o);
//        log.setUsername(email);
        logRepo.save(log);
        return myService.getByFIO_photo(i.replace('$', '%'), o.replace('$', '%'), f.replace('$', '%'));
    }

    @GetMapping(value = "/downloadFlPdf/{iin}", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] generatePdfFile(HttpServletResponse response, @PathVariable("iin")String iin)throws IOException, DocumentException {
        response.setContentType("application/pdf");
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=doc" + ".pdf";
        response.setHeader(headerkey, headervalue);
        NodesFL r =  myService.getNode(iin);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        pdfGenerator.generate(r, baos);
        return baos.toByteArray();
    }

    @GetMapping("/downloadFlDoc/{iin}")
    public byte[] generateDoc(@PathVariable String iin, HttpServletResponse response) throws IOException, InvalidFormatException {
        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=document.docx";
        response.setHeader(headerkey,headervalue);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        NodesFL result =  myService.getNode(iin);
        docxGenerator.generateDoc(result,baos);
        return baos.toByteArray();
    }

}
