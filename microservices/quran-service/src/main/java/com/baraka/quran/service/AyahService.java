package com.baraka.quran.service;

import com.baraka.quran.model.dto.AyahDTO;
import java.util.List;

public interface AyahService {
    List<AyahDTO> getAllAyahs();
    AyahDTO getAyahById(Long id);
    List<AyahDTO> searchAyahs(String keyword);
    List<AyahDTO> getAyahsBySurahId(Long surahId);
    List<AyahDTO> getAyahsBySurahIdAndVerseRange(Long surahId, Integer startVerse, Integer endVerse);
    AyahDTO getAyahBySurahIdAndVerseNumber(Long surahId, Integer verseNumber);
} 