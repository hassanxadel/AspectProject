package com.baraka.quran.model;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ayah")
public class Ayah {
    @Id
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "surah_id", nullable = false)
    private Surah surah;
    
    @Column(name = "verse_number", nullable = false)
    private Integer verseNumber;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;
    
    @Column(name = "text_arabic", nullable = false, columnDefinition = "TEXT")
    private String textArabic;
} 