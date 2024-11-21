package tech.lastbox;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/testeRota")
    public ResponseEntity index() {
        return ResponseEntity.ok("Deu certo");
    }
}
