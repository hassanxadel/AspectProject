package com.baraka.quran.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AyahDTO {
    private Long id;
    private Long surahId;
    private String surahName;
    private Integer verseNumber;
    private String text;
    private String textArabic;
} 