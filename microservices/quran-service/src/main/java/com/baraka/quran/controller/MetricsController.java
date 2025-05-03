package com.baraka.quran.controller;

import com.baraka.quran.aspect.CachingAspect;
import com.baraka.quran.aspect.SearchMetricsAspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/metrics")
public class MetricsController {
    
    private final CachingAspect cachingAspect;
    private final SearchMetricsAspect searchMetricsAspect;
    
    @Autowired
    public MetricsController(CachingAspect cachingAspect, SearchMetricsAspect searchMetricsAspect) {
        this.cachingAspect = cachingAspect;
        this.searchMetricsAspect = searchMetricsAspect;
    }
    
    @GetMapping("/cache")
    public ResponseEntity<Map<String, Object>> getCacheStats() {
        return ResponseEntity.ok(cachingAspect.getCacheStats());
    }
    
    @DeleteMapping("/cache")
    public ResponseEntity<Void> clearCache() {
        cachingAspect.clearCache();
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<Map<String, Map<String, Object>>> getSearchMetrics() {
        return ResponseEntity.ok(searchMetricsAspect.getAllMetrics());
    }
    
    @GetMapping("/search/surah")
    public ResponseEntity<Map<String, Object>> getSurahSearchMetrics() {
        Map<String, Map<String, Object>> allMetrics = searchMetricsAspect.getAllMetrics();
        return ResponseEntity.ok(allMetrics.get("surah"));
    }
    
    @GetMapping("/search/ayah")
    public ResponseEntity<Map<String, Object>> getAyahSearchMetrics() {
        Map<String, Map<String, Object>> allMetrics = searchMetricsAspect.getAllMetrics();
        return ResponseEntity.ok(allMetrics.get("ayah"));
    }
} 