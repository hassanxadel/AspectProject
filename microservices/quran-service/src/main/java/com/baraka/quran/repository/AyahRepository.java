package com.baraka.quran.repository;

import com.baraka.quran.model.Ayah;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AyahRepository extends JpaRepository<Ayah, Long> {
    
    @Query("SELECT a FROM Ayah a WHERE LOWER(a.text) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(a.textArabic) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Ayah> searchByKeyword(@Param("keyword") String keyword);
    
    List<Ayah> findBySurahId(Long surahId);
    
    List<Ayah> findBySurahIdAndVerseNumberBetween(Long surahId, Integer startVerse, Integer endVerse);
    
    @Query("SELECT a FROM Ayah a WHERE a.surah.id = :surahId AND a.verseNumber = :verseNumber")
    Ayah findBySurahIdAndVerseNumber(@Param("surahId") Long surahId, @Param("verseNumber") Integer verseNumber);
} 