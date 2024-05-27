package LAP.Blockchain.controller;


import LAP.Blockchain.dto.Payment;
import LAP.Blockchain.service.BlockchainService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/blockchain")
@RestController
public class BlockchainController {
    private final BlockchainService blockchainService;

    @PostMapping(path = "/addTransaction") // RECEIVE TRANSACTION FROM HERE
    @ResponseBody
    public ResponseEntity<String> insertTransaction(@RequestBody Payment payment) {
        try {
            int res = blockchainService.addTransaction(payment);
            if (res == 1) {
                return ResponseEntity.status(HttpStatus.OK).body("YOUR TRANSACTION HAS BEEN SUCCESSFULLY REGISTERED");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("YOUR TRANSACTION HAS BEEN REFUSED");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("TRANSACTION ERROR "+ e.getMessage());
        }
    }
}
