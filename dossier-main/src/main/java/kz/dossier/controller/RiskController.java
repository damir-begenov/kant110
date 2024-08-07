package kz.dossier.controller;

import kz.dossier.dto.AdmRightsBreakerDTO;
import kz.dossier.riskDto.*;
import kz.dossier.security.models.log;
import kz.dossier.service.RiskService;
import kz.dossier.service.ULService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3000)
@RestController
@RequestMapping("/api/pandora/dossier")
public class RiskController {
    @Autowired
    private ULService ulService;
    @Autowired
    private RiskService riskService;

    @GetMapping("/ul/count-risk-percentage")
    public ResponseEntity<Double> getRiskPercentage(@RequestParam String bin) {
        try {
            if (bin == null || bin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            Double dto = riskService.countRisksPercentage(bin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching dto by bin: " + bin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/ul/get-sudispol")
    public ResponseEntity<OweULGroupPage> getSudispolWithPage(@RequestParam String bin, @RequestParam Integer page) {
        try {
            if (bin == null || bin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            OweULGroupPage dto = riskService.getOweUl(bin, page);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching OweULGroupPage by bin: " + bin, e);

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
    @GetMapping("/snyatie-nds")
    public ResponseEntity<List<SnyatUchetNdsDto>> getNds(@RequestParam String bin) {
        try {
            if (bin == null || bin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<SnyatUchetNdsDto> dto = riskService.getNds(bin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching SnyatUchetNdsDto by bin: " + bin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/block-esf")
    public ResponseEntity<List<BlockEsfDto>> getBlockEsfs(@RequestParam String bin) {
        try {
            if (bin == null || bin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<BlockEsfDto> dto = riskService.getBlockEsfs(bin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching BlockEsfDto by bin: " + bin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/ul-card")
    public ResponseEntity<List<CardDto>> getCards(@RequestParam String bin) {
        try {
            if (bin == null || bin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<CardDto> dto = riskService.getCards(bin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching CardDto by bin: " + bin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/omn")
    public ResponseEntity<List<OmnDto>> getOmns(@RequestParam String bin) {
        try {
            if (bin == null || bin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<OmnDto> dto = riskService.getOmns(bin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching OmnDto by bin: " + bin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/opg")
    public ResponseEntity<List<OpgDto>> getOpgs(@RequestParam String bin) {
        try {
            if (bin == null || bin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<OpgDto> dto = riskService.getOpgs(bin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching OpgDto by bin: " + bin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/inactions")
    public ResponseEntity<List<InactionDto>> getInaction(@RequestParam String bin) {
        try {
            if (bin == null || bin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<InactionDto> dto = riskService.getInaction(bin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching InactionDto by bin: " + bin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/test")
    public ResponseEntity<List<TestDto>> getTest(@RequestParam String bin) {
        try {
            if (bin == null || bin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<TestDto> dto = riskService.getTest(bin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching TestDto by bin: " + bin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/first-credit-buro")
    public ResponseEntity<List<FirstCreditBuroDto>> getFCB(@RequestParam String bin) {
        try {
            if (bin == null || bin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<FirstCreditBuroDto> dto = riskService.getFCB(bin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching TestDto by bin: " + bin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/risk-participants")
    public ResponseEntity<List<RiskUlParticipantsDto>> getRiskUlParts(@RequestParam String bin) {
        try {
            if (bin == null || bin.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
            }
            List<RiskUlParticipantsDto> dto = riskService.getRiskUlParts(bin);
            if (dto == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error occurred while fetching RiskUlParticipantsDto by bin: " + bin, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}
