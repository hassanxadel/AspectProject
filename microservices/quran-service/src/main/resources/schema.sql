
DROP TABLE IF EXISTS ayah;
DROP TABLE IF EXISTS surah;

CREATE TABLE surah (
    id BIGINT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    name_arabic VARCHAR(100) NOT NULL,
    number_of_verses INT NOT NULL,
    revelation_type VARCHAR(20) NOT NULL,
    revelation_order INT NOT NULL
);

CREATE TABLE ayah (
    id BIGINT PRIMARY KEY,
    surah_id BIGINT NOT NULL,
    verse_number INT NOT NULL,
    text TEXT NOT NULL,
    text_arabic TEXT NOT NULL,
    FOREIGN KEY (surah_id) REFERENCES surah(id)
); 