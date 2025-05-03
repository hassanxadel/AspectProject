package com.baraka.quran.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class CachingAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    // Cache for search results to improve performance
    private final ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<>();
    
    // Cache statistics
    private int cacheHits = 0;
    private int cacheMisses = 0;
    
    /**
     * Pointcut for search methods in service layer for both Surah and Ayah services
     */
    @Pointcut("execution(* com.baraka.quran.service.SurahService.search*(..)) || " +
              "execution(* com.baraka.quran.service.SurahService.find*(..)) || " +
              "execution(* com.baraka.quran.service.AyahService.search*(..)) || " +
              "execution(* com.baraka.quran.service.AyahService.find*(..))")
    public void cacheableMethods() {}
    
    /**
     * Around advice for caching method results
     */
    @Around("cacheableMethods()")
    public Object cacheMethodResult(ProceedingJoinPoint joinPoint) throws Throwable {
        // Generate a cache key based on method name and arguments
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        Object[] args = joinPoint.getArgs();
        String cacheKey = className + "." + methodName + ":" + Arrays.toString(args);
        
        // Check if result is in cache
        if (cache.containsKey(cacheKey)) {
            cacheHits++;
            log.debug("Cache HIT for {}, hits: {}, misses: {}", cacheKey, cacheHits, cacheMisses);
            return cache.get(cacheKey);
        }
        
        // Execute method and cache result
        Object result = joinPoint.proceed();
        cache.put(cacheKey, result);
        cacheMisses++;
        
        log.debug("Cache MISS for {}, hits: {}, misses: {}", cacheKey, cacheHits, cacheMisses);
        
        return result;
    }
    
    /**
     * Get cache statistics
     */
    public Map<String, Object> getCacheStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("hits", cacheHits);
        stats.put("misses", cacheMisses);
        stats.put("size", cache.size());
        stats.put("hitRatio", cacheHits + cacheMisses > 0 ? 
                (double) cacheHits / (cacheHits + cacheMisses) : 0);
        
        return stats;
    }
    
    /**
     * Clear the cache
     */
    public void clearCache() {
        cache.clear();
        log.info("Cache cleared");
    }
} 