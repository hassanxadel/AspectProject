package com.baraka.quran.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Aspect
@Component
public class SearchMetricsAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    // Track the number of searches performed for each service
    private final AtomicLong surahSearchCount = new AtomicLong(0);
    private final AtomicLong ayahSearchCount = new AtomicLong(0);
    
    // Track the average search execution time for each service
    private final AtomicLong surahTotalSearchTime = new AtomicLong(0);
    private final AtomicLong ayahTotalSearchTime = new AtomicLong(0);
    
    // Track the most common search terms for each service
    private final ConcurrentHashMap<String, AtomicLong> surahSearchTerms = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicLong> ayahSearchTerms = new ConcurrentHashMap<>();
    
    /**
     * Pointcut for search methods in controllers
     */
    @Pointcut("execution(* com.baraka.quran.controller.SurahController.search*(..)) && args(keyword,..)")
    public void surahSearchMethods(String keyword) {}
    
    @Pointcut("execution(* com.baraka.quran.controller.AyahController.search*(..)) && args(keyword,..)")
    public void ayahSearchMethods(String keyword) {}
    
    /**
     * Around advice to track Surah search metrics
     */
    @Around("surahSearchMethods(keyword)")
    public Object trackSurahSearchMetrics(ProceedingJoinPoint joinPoint, String keyword) throws Throwable {
        return trackSearchMetrics(joinPoint, keyword, true);
    }
    
    /**
     * Around advice to track Ayah search metrics
     */
    @Around("ayahSearchMethods(keyword)")
    public Object trackAyahSearchMetrics(ProceedingJoinPoint joinPoint, String keyword) throws Throwable {
        return trackSearchMetrics(joinPoint, keyword, false);
    }
    
    /**
     * Common method to track search metrics
     */
    private Object trackSearchMetrics(ProceedingJoinPoint joinPoint, String keyword, boolean isSurah) throws Throwable {
        // Increment search count
        AtomicLong searchCount = isSurah ? surahSearchCount : ayahSearchCount;
        long currentCount = searchCount.incrementAndGet();
        
        // Record search term
        ConcurrentHashMap<String, AtomicLong> searchTerms = isSurah ? surahSearchTerms : ayahSearchTerms;
        searchTerms.computeIfAbsent(keyword, k -> new AtomicLong(0)).incrementAndGet();
        
        // Measure execution time
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - startTime;
        
        // Update total search time
        AtomicLong totalSearchTime = isSurah ? surahTotalSearchTime : ayahTotalSearchTime;
        totalSearchTime.addAndGet(executionTime);
        
        // Log metrics
        String serviceType = isSurah ? "Surah" : "Ayah";
        log.info("{} search metrics - Count: {}, Avg Time: {} ms, Term: '{}', Occurrences: {}", 
                serviceType,
                currentCount, 
                totalSearchTime.get() / currentCount,
                keyword,
                searchTerms.get(keyword));
        
        return result;
    }
    
    /**
     * Get the current search count for a specific service
     */
    public long getSearchCount(boolean isSurah) {
        return isSurah ? surahSearchCount.get() : ayahSearchCount.get();
    }
    
    /**
     * Get the average search time in milliseconds for a specific service
     */
    public double getAverageSearchTime(boolean isSurah) {
        AtomicLong searchCount = isSurah ? surahSearchCount : ayahSearchCount;
        AtomicLong totalSearchTime = isSurah ? surahTotalSearchTime : ayahTotalSearchTime;
        
        long count = searchCount.get();
        return count > 0 ? (double) totalSearchTime.get() / count : 0;
    }
    
    /**
     * Get the most common search terms for a specific service
     */
    public ConcurrentHashMap<String, AtomicLong> getSearchTerms(boolean isSurah) {
        return new ConcurrentHashMap<>(isSurah ? surahSearchTerms : ayahSearchTerms);
    }
    
    /**
     * Get all search metrics
     */
    public Map<String, Map<String, Object>> getAllMetrics() {
        Map<String, Map<String, Object>> allMetrics = new HashMap<>();
        
        // Surah metrics
        Map<String, Object> surahMetrics = new HashMap<>();
        surahMetrics.put("searchCount", surahSearchCount.get());
        surahMetrics.put("averageSearchTime", getAverageSearchTime(true));
        surahMetrics.put("searchTerms", convertSearchTermsToMap(surahSearchTerms));
        allMetrics.put("surah", surahMetrics);
        
        // Ayah metrics
        Map<String, Object> ayahMetrics = new HashMap<>();
        ayahMetrics.put("searchCount", ayahSearchCount.get());
        ayahMetrics.put("averageSearchTime", getAverageSearchTime(false));
        ayahMetrics.put("searchTerms", convertSearchTermsToMap(ayahSearchTerms));
        allMetrics.put("ayah", ayahMetrics);
        
        return allMetrics;
    }
    
    /**
     * Convert ConcurrentHashMap of search terms to a regular Map
     */
    private Map<String, Long> convertSearchTermsToMap(ConcurrentHashMap<String, AtomicLong> searchTerms) {
        Map<String, Long> result = new HashMap<>();
        searchTerms.forEach((term, count) -> result.put(term, count.get()));
        return result;
    }
} 