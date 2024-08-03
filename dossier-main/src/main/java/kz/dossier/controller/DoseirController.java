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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
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

    @Autowired
    UlRiskServiceImpl ulRiskService;


    @GetMapping("/ulCard")
    public UlCardDTO getUlCard(@RequestParam String bin) {
        return myService.getUlCard(bin);
    }

    @GetMapping("/ulAddresses")
    public UlAddressInfo getUlAddresses(@RequestParam String bin) {
        return myService.getUlAddresses(bin);
    }

    @GetMapping("/sameAddressFl")
    public List<SearchResultModelFL> sameAddressFls(@RequestParam String iin) {
        System.out.println(iin);
        return myService.getByAddressUsingIin(iin);
    }

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
    @GetMapping("/generalInfoUl")
    public ULGeneralInfoDTO getGeneralInfoUl(@RequestParam String bin) {
        return myService.getUlGeneral(bin);
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

    @GetMapping("/get-subsidiy")
    public List<SubsidiyDTO> getSubsidiy(String bin) {
        return myService.getSubsidies(bin);
    }
    @GetMapping("/goszakup-sum-by-year")
    public GosZakupForAll getGosZakupByBin(@RequestParam String bin) {
        return myService.gosZakupByBin(bin);
    }

    @GetMapping("/goszakup-page")
    public List<GosZakupDetailsDTO> getGoszakupDetails(@RequestParam String bin, @RequestParam Integer year, @RequestParam String isSupplier) {
        if (isSupplier.equals("true")) {
            return myService.getGosZakupDetails(bin, year, true);
        } else {
            return myService.getGosZakupDetails(bin, year, false);
        }
    }

    @GetMapping("/samruk-sum-by-year")
    public SamrukKazynaForAll getSamrukByBin(@RequestParam String bin) {
        return myService.samrukByBin(bin);
    }

    @GetMapping("/samruk-page")
    public List<SamrukDetailsDTO> getSamrukDetails(@RequestParam String bin, @RequestParam Integer year, @RequestParam String isSupplier) {
        if (isSupplier.equals("true")) {
            return myService.getSamrukDetailsBySupplier(bin, year);
        } else {
            return myService.getSamrukDetailsByCustomer(bin, year);
        }
    }

    @GetMapping("/profile")
    public NodesFL getProfile(@RequestParam String iin) {
        return myService.getNode(iin);
    }

    @GetMapping("/getRiskByIin")
    public FLRiskDto getRisk(@RequestParam String iin){
        return flRiskService.findFlRiskByIin(iin);
    }
    @GetMapping("/getRiskByBin")
    public UlRiskDTO getRiskBin(@RequestParam String bin){
        return ulRiskService.findULRiskByIin(bin);
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

    @GetMapping("/bin")
    public List<SearchResultModelUl> findByBin(@RequestParam String bin, @RequestParam String email) {
        log log = new log();
        log.setDate(LocalDateTime.now());
        log.setObwii("Искал в досье " + email + ": " + bin);
        log.setUsername(email);
        logRepo.save(log);
        return myService.searchResultUl(bin);
    }

    @GetMapping("/binname")
    public List<SearchResultModelUl> findBinByName(@RequestParam String name) {
        return myService.searchUlByName(name.replace('$', '%'));
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
