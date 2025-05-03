package com.baraka.quran.controller;
import com.baraka.quran.model.dto.AyahDTO;
import com.baraka.quran.service.AyahService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ayahs")
public class AyahController {
    
    private final AyahService ayahService;
    
    @Autowired
    public AyahController(AyahService ayahService) {
        this.ayahService = ayahService;
    }
    
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchByKeyword(@RequestParam String keyword) {
        List<AyahDTO> results = ayahService.searchAyahs(keyword);
        
        Map<String, Object> response = new HashMap<>();
        response.put("totalResults", results.size());
        response.put("results", results);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/surah/{surahId}")
    public ResponseEntity<List<AyahDTO>> getBySurahId(@PathVariable Long surahId) {
        List<AyahDTO> ayahs = ayahService.getAyahsBySurahId(surahId);
        return ResponseEntity.ok(ayahs);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AyahDTO> getById(@PathVariable Long id) {
        AyahDTO ayah = ayahService.getAyahById(id);
        return ResponseEntity.ok(ayah);
    }
} 