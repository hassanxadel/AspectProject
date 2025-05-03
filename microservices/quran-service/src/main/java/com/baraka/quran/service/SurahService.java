package com.baraka.quran.service;

import com.baraka.quran.model.dto.SurahDTO;
import java.util.List;

public interface SurahService {
    List<SurahDTO> getAllSurahs();
    SurahDTO getSurahById(Long id);
    List<SurahDTO> searchSurahs(String keyword);
    List<SurahDTO> getSurahsByRevelationType(String revelationType);
    List<SurahDTO> getSurahsByVersesGreaterThan(Integer verses);
} 