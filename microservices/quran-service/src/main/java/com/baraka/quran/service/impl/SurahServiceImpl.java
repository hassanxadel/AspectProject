package com.baraka.quran.service.impl;

import com.baraka.quran.model.Surah;
import com.baraka.quran.model.dto.SurahDTO;
import com.baraka.quran.repository.SurahRepository;
import com.baraka.quran.service.SurahService;
import com.baraka.quran.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SurahServiceImpl implements SurahService {
    
    private final SurahRepository surahRepository;
    
    @Autowired
    public SurahServiceImpl(SurahRepository surahRepository) {
        this.surahRepository = surahRepository;
    }
    
    @Override
    public List<SurahDTO> getAllSurahs() {
        return surahRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public SurahDTO getSurahById(Long id) {
        return surahRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Surah not found with id: " + id));
    }
    
    @Override
    public List<SurahDTO> searchSurahs(String keyword) {
        return surahRepository.searchByKeyword(keyword).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<SurahDTO> getSurahsByRevelationType(String revelationType) {
        return surahRepository.findByRevelationType(revelationType).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<SurahDTO> getSurahsByVersesGreaterThan(Integer verses) {
        return surahRepository.findByNumberOfVersesGreaterThan(verses).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private SurahDTO convertToDTO(Surah surah) {
        SurahDTO dto = new SurahDTO();
        dto.setId(surah.getId());
        dto.setName(surah.getName());
        dto.setNameArabic(surah.getNameArabic());
        dto.setNumberOfVerses(surah.getNumberOfVerses());
        dto.setRevelationType(surah.getRevelationType());
        dto.setRevelationOrder(surah.getRevelationOrder());
        dto.setTotalAyahs(surah.getNumberOfVerses());
        return dto;
    }
} 