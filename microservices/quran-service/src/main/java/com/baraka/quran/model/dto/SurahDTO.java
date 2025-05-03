package com.baraka.quran.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurahDTO {
    private Long id;
    private String name;
    private String nameArabic;
    private Integer numberOfVerses;
    private String revelationType;
    private Integer revelationOrder;
    private Integer totalAyahs;
} 