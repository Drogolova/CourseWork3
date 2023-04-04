package socks.shop.coursework3.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jdk.jshell.Snippet;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import socks.shop.coursework3.models.Socks;
import socks.shop.coursework3.services.SocksService;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/socks")
public class SocksController {

    private final SocksService socksService;

    public SocksController(SocksService socksService) {
        this.socksService = socksService;
    }

    @PostMapping()
   @Operation(description = "Регистрирует приход товара на склад")
    public ResponseEntity<Socks> addSocks(@RequestBody Socks newSocks) {
        if (newSocks.getColor() != null && newSocks.getSize() != null && newSocks.getCottonPart() >= 0
                && newSocks.getCottonPart() <= 100 && newSocks.getQuantity() > 0) {
            final Socks socks = socksService.addSocks(newSocks);
            return ResponseEntity.ok(socks);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping()
    @Operation(description = "Регистрирует отпуск носков со склада")
    public ResponseEntity<Socks> releaseSocks(@RequestBody Socks newSocks) {
        if (newSocks.getColor() != null && newSocks.getSize() != null && newSocks.getCottonPart() >= 0
                && newSocks.getCottonPart() <= 100 && newSocks.getQuantity() > 0) {
            final Socks socks = socksService.releaseSocks(newSocks);
            if (socks != null ) {
                return ResponseEntity.ok(socks);
            } else {
                return ResponseEntity.noContent().build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    @Operation(description = "Возвращает общее количество носков на складе, соответствующих переданным в параметрах критериям запроса")
    public ResponseEntity<Integer> filterByColorSizeCottonPart(@RequestParam String color, @RequestParam int size,
                                                               @RequestParam int minCottonPart, @RequestParam int maxCottonPart) {
        if (color != null && size >=36 && size <= 40 && minCottonPart >= 0 && minCottonPart < maxCottonPart && maxCottonPart <= 100 ) {
            Integer quantity = socksService.filterByColorSizeCottonPart(color, size, minCottonPart, maxCottonPart);
            return ResponseEntity.ok(quantity);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping()
    @Operation(description = "Регистрирует списание испорченных (бракованных) носков")
    public ResponseEntity<Socks> deleteDamagedSocks(@RequestBody Socks newSocks) {
        if (newSocks.getColor() != null && newSocks.getSize() != null && newSocks.getCottonPart() >= 0
                && newSocks.getCottonPart() <= 100 && newSocks.getQuantity() > 0) {
            final Socks socks = socksService.deleteDamagedSocks(newSocks);
            if (socks != null ) {
                return ResponseEntity.ok(socks);
            } else {
                return ResponseEntity.noContent().build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
