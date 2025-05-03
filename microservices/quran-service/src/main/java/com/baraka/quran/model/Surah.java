package com.baraka.quran.model;

import javax.persistence.*;

@Entity
@Table(name = "surah")
public class Surah {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "name_arabic", nullable = false)
    private String nameArabic;
    
    @Column(name = "number_of_verses", nullable = false)
    private Integer numberOfVerses;
    
    @Column(name = "revelation_type", nullable = false)
    private String revelationType;
    
    @Column(name = "revelation_order", nullable = false)
    private Integer revelationOrder;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getNameArabic() {
        return nameArabic;
    }
    
    public void setNameArabic(String nameArabic) {
        this.nameArabic = nameArabic;
    }
    
    public Integer getNumberOfVerses() {
        return numberOfVerses;
    }
    
    public void setNumberOfVerses(Integer numberOfVerses) {
        this.numberOfVerses = numberOfVerses;
    }
    
    public String getRevelationType() {
        return revelationType;
    }
    
    public void setRevelationType(String revelationType) {
        this.revelationType = revelationType;
    }
    
    public Integer getRevelationOrder() {
        return revelationOrder;
    }
    
    public void setRevelationOrder(Integer revelationOrder) {
        this.revelationOrder = revelationOrder;
    }
} 