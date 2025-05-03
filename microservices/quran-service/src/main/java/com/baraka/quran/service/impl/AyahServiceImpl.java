package com.baraka.quran.service.impl;

import com.baraka.quran.model.Ayah;
import com.baraka.quran.model.dto.AyahDTO;
import com.baraka.quran.repository.AyahRepository;
import com.baraka.quran.service.AyahService;
import com.baraka.quran.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AyahServiceImpl implements AyahService {
    
    private final AyahRepository ayahRepository;
    
    @Autowired
    public AyahServiceImpl(AyahRepository ayahRepository) {
        this.ayahRepository = ayahRepository;
    }
    
    @Override
    public List<AyahDTO> getAllAyahs() {
        return ayahRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public AyahDTO getAyahById(Long id) {
        return ayahRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Ayah not found with id: " + id));
    }
    
    @Override
    public List<AyahDTO> searchAyahs(String keyword) {
        return ayahRepository.searchByKeyword(keyword).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<AyahDTO> getAyahsBySurahId(Long surahId) {
        return ayahRepository.findBySurahId(surahId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<AyahDTO> getAyahsBySurahIdAndVerseRange(Long surahId, Integer startVerse, Integer endVerse) {
        return ayahRepository.findBySurahIdAndVerseNumberBetween(surahId, startVerse, endVerse).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public AyahDTO getAyahBySurahIdAndVerseNumber(Long surahId, Integer verseNumber) {
        Ayah ayah = ayahRepository.findBySurahIdAndVerseNumber(surahId, verseNumber);
        if (ayah == null) {
            throw new ResourceNotFoundException(
                String.format("Ayah not found for Surah ID: %d and verse number: %d", surahId, verseNumber));
        }
        return convertToDTO(ayah);
    }
    
    private AyahDTO convertToDTO(Ayah ayah) {
        AyahDTO dto = new AyahDTO();
        dto.setId(ayah.getId());
        dto.setSurahId(ayah.getSurah().getId());
        dto.setSurahName(ayah.getSurah().getName());
        dto.setVerseNumber(ayah.getVerseNumber());
        dto.setText(ayah.getText());
        dto.setTextArabic(ayah.getTextArabic());
        return dto;
    }
} 