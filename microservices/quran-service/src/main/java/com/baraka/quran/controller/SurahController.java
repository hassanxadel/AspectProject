package com.baraka.quran.controller;

import com.baraka.quran.model.dto.SurahDTO;
import com.baraka.quran.service.SurahService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/surahs")
public class SurahController {
    
    private final SurahService surahService;
    
    @Autowired
    public SurahController(SurahService surahService) {
        this.surahService = surahService;
    }
    
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchByKeyword(@RequestParam String keyword) {
        List<SurahDTO> results = surahService.searchSurahs(keyword);
        
        Map<String, Object> response = new HashMap<>();
        response.put("totalResults", results.size());
        response.put("results", results);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SurahDTO> getById(@PathVariable Long id) {
        SurahDTO surah = surahService.getSurahById(id);
        return ResponseEntity.ok(surah);
    }
    
    @GetMapping
    public ResponseEntity<List<SurahDTO>> getAll() {
        return ResponseEntity.ok(surahService.getAllSurahs());
    }
} 