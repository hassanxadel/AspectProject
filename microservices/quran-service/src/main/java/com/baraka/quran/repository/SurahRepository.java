package com.baraka.quran.repository;

import com.baraka.quran.model.Surah;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurahRepository extends JpaRepository<Surah, Long> {
    
    @Query("SELECT s FROM Surah s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(s.nameArabic) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Surah> searchByKeyword(@Param("keyword") String keyword);
    
    List<Surah> findByRevelationType(String revelationType);
    
    List<Surah> findByNumberOfVersesGreaterThan(Integer verses);
} 