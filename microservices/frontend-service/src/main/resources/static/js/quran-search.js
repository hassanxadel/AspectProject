$(document).ready(function() {
    // URLs for API calls
    const SURAH_SEARCH_URL = '/api/surahs/search';
    const AYAH_SEARCH_URL = '/api/ayahs/search';
    const SURAH_SUGGESTIONS_URL = '/api/surahs/suggestions';
    
    // Arabic text detection regex
    const arabicRegex = /[\u0600-\u06FF]/;
    
    // Maximum number of recent searches to store
    const MAX_RECENT_SEARCHES = 5;
    
    // Initialize recent searches from localStorage
    let surahRecentSearches = JSON.parse(localStorage.getItem('surahRecentSearches')) || [];
    let ayahRecentSearches = JSON.parse(localStorage.getItem('ayahRecentSearches')) || [];
    
    // Display recent searches if available
    displayRecentSearches('surah', surahRecentSearches);
    displayRecentSearches('ayah', ayahRecentSearches);
    
    // Set input direction based on text
    $('#surah-search-input, #ayah-search-input').on('input', function() {
        const input = $(this);
        const text = input.val();
        
        if (arabicRegex.test(text)) {
            input.css('direction', 'rtl');
            input.css('text-align', 'right');
        } else {
            input.css('direction', 'ltr');
            input.css('text-align', 'left');
        }
    });
    
    // Initialize autocomplete for Surah search input
    $('#surah-search-input').on('input', function() {
        const keyword = $(this).val().trim();
        if (keyword.length >= 2) {
            $.ajax({
                url: SURAH_SEARCH_URL,
                method: 'GET',
                data: { keyword: keyword },
                success: function(response) {
                    showSuggestions(response.results, 'surah');
                }
            });
        } else {
            $('#surah-suggestions').hide();
        }
    });
    
    // Initialize autocomplete for Ayah search input
    $('#ayah-search-input').on('input', function() {
        const keyword = $(this).val().trim();
        if (keyword.length >= 3) {
            $.ajax({
                url: AYAH_SEARCH_URL,
                method: 'GET',
                data: { keyword: keyword },
                success: function(response) {
                    showSuggestions(response.results, 'ayah');
                }
            });
        } else {
            $('#ayah-suggestions').hide();
        }
    });
    
    // Function to display suggestions
    function showSuggestions(results, type) {
        let suggestionsHtml = '';
        let suggestionsContainer = $(`#${type}-suggestions`);
        
        // Create suggestions container if it doesn't exist
        if (suggestionsContainer.length === 0) {
            suggestionsContainer = $('<div class="suggestions-container"></div>');
            suggestionsContainer.attr('id', `${type}-suggestions`);
            $(`#${type}-search-input`).parent().append(suggestionsContainer);
            
            // Position it correctly below the input
            const inputPos = $(`#${type}-search-input`).position();
            const inputHeight = $(`#${type}-search-input`).outerHeight();
            const inputWidth = $(`#${type}-search-input`).outerWidth();
            
            suggestionsContainer.css({
                'position': 'absolute',
                'top': inputHeight + 'px',
                'width': inputWidth + 'px',
                'z-index': '1000'
            });
        }
        
        if (results && results.length > 0) {
            results.slice(0, 5).forEach(item => {
                if (type === 'surah') {
                    const nameArabic = item.nameArabic || item.name || '';
                    const name = item.name || 'Unknown';
                    suggestionsHtml += `<div class="suggestion-item" data-type="${type}" data-value="${name}">${name} ${nameArabic ? `(${nameArabic})` : ''}</div>`;
                } else {
                    // For ayahs, use a truncated text
                    const text = item.text || 'No text available';
                    const truncatedText = text.length > 50 ? text.substring(0, 50) + '...' : text;
                    suggestionsHtml += `<div class="suggestion-item" data-type="${type}" data-value="${truncatedText}">${truncatedText}</div>`;
                }
            });
            
            suggestionsContainer.html(suggestionsHtml);
            suggestionsContainer.show();
            
            // Click handler for suggestion items
            $('.suggestion-item').on('click', function() {
                const type = $(this).data('type');
                const value = $(this).data('value');
                
                $(`#${type}-search-input`).val(value);
                $(`#${type}-suggestions`).hide();
                
                if (type === 'surah') {
                    searchSurah(value);
                    addToRecentSearches('surah', value);
                } else {
                    searchAyah(value);
                    addToRecentSearches('ayah', value);
                }
            });
            
        } else {
            suggestionsContainer.hide();
        }
    }
    
    // Hide suggestions when clicking outside
    $(document).on('click', function(e) {
        if (!$(e.target).closest('.suggestion-item, #surah-search-input, #ayah-search-input').length) {
            $('.suggestions-container').hide();
        }
    });
    
    // Surah search form submission
    $('#surah-search-form').on('submit', function(e) {
        e.preventDefault();
        const keyword = $('#surah-search-input').val().trim();
        
        if (keyword) {
            searchSurah(keyword);
            addToRecentSearches('surah', keyword);
        }
    });
    
    // Ayah search form submission
    $('#ayah-search-form').on('submit', function(e) {
        e.preventDefault();
        const keyword = $('#ayah-search-input').val().trim();
        
        if (keyword) {
            searchAyah(keyword);
            addToRecentSearches('ayah', keyword);
        }
    });
    
    // Click event for recent search items
    $(document).on('click', '.recent-search-item', function() {
        const type = $(this).data('type');
        const keyword = $(this).text();
        
        if (type === 'surah') {
            $('#surah-search-input').val(keyword);
            searchSurah(keyword);
        } else if (type === 'ayah') {
            $('#ayah-search-input').val(keyword);
            searchAyah(keyword);
        }
    });
    
    // Function to search for Surahs
    function searchSurah(keyword) {
        $('#surah-results').html('<div class="text-center"><i class="fas fa-spinner fa-spin fa-2x"></i><p>Searching...</p></div>');
        
        $.ajax({
            url: SURAH_SEARCH_URL,
            method: 'GET',
            data: { keyword: keyword },
            success: function(response) {
                displaySurahResults(response);
            },
            error: function(error) {
                $('#surah-results').html('<div class="alert alert-danger">Error searching for Surahs. Please try again.</div>');
                console.error('Error searching for Surahs:', error);
            }
        });
    }
    
    // Function to search for Ayahs
    function searchAyah(keyword) {
        $('#ayah-results').html('<div class="text-center"><i class="fas fa-spinner fa-spin fa-2x"></i><p>Searching...</p></div>');
        
        $.ajax({
            url: AYAH_SEARCH_URL,
            method: 'GET',
            data: { keyword: keyword },
            success: function(response) {
                displayAyahResults(response);
            },
            error: function(error) {
                $('#ayah-results').html('<div class="alert alert-danger">Error searching for Ayahs. Please try again.</div>');
                console.error('Error searching for Ayahs:', error);
            }
        });
    }
    
    // Function to display Surah search results
    function displaySurahResults(response) {
        const resultsContainer = $('#surah-results');
        resultsContainer.empty();
        
        if (response.totalResults === 0) {
            resultsContainer.html('<div class="alert alert-info">No Surahs found. Try a different search term.</div>');
            return;
        }
        
        const results = response.results;
        resultsContainer.append(`<h4>${response.totalResults} Surah(s) found:</h4>`);
        
        results.forEach(surah => {
            const resultCard = $('<div class="result-card">');
            
            // Ensure all properties exist before using them
            const nameArabic = surah.nameArabic || surah.name || 'Unknown';
            const name = surah.name || 'Unknown';
            const numberOfAyahs = surah.totalAyahs || surah.numberOfVerses || 0;
            const revelationType = surah.revelationType || 'Unknown';
            
            resultCard.append(`<h5>${name}</h5>`);
            resultCard.append(`<div class="arabic-text">${nameArabic}</div>`);
            resultCard.append(`<p><strong>Number of Ayahs:</strong> ${numberOfAyahs}</p>`);
            resultCard.append(`<p><strong>Revelation Type:</strong> ${revelationType}</p>`);
            resultCard.append(`<a href="/api/ayahs/surah/${surah.id}" class="btn btn-sm btn-outline-primary">View Ayahs</a>`);
            
            resultsContainer.append(resultCard);
        });
    }
    
    // Function to display Ayah search results
    function displayAyahResults(response) {
        const resultsContainer = $('#ayah-results');
        resultsContainer.empty();
        
        if (response.totalResults === 0) {
            resultsContainer.html('<div class="alert alert-info">No Ayahs found. Try a different search term.</div>');
            return;
        }
        
        const results = response.results;
        resultsContainer.append(`<h4>${response.totalResults} Ayah(s) found:</h4>`);
        
        results.forEach(ayah => {
            const resultCard = $('<div class="result-card">');
            
            // Ensure properties exist
            const surahId = ayah.surahId || 1;
            const surahName = ayah.surahName || 'Unknown Surah';
            const numberInSurah = ayah.numberInSurah || 0;
            const text = ayah.text || 'No text available';
            const translation = ayah.translation || 'No translation available';
            
            resultCard.append(`<p><strong>Surah ID:</strong> ${surahId} - <strong>Surah Name:</strong> ${surahName}</p>`);
            resultCard.append(`<p><strong>Ayah Number:</strong> ${numberInSurah}</p>`);
            resultCard.append(`<div class="arabic-text">${text}</div>`);
            resultCard.append(`<p><em>${translation}</em></p>`);
            
            resultsContainer.append(resultCard);
        });
    }
    
    // Function to add a search term to recent searches
    function addToRecentSearches(type, keyword) {
        let recentSearches = type === 'surah' ? surahRecentSearches : ayahRecentSearches;
        
        // Add to front of array if not already present, or move to front if exists
        const index = recentSearches.indexOf(keyword);
        if (index !== -1) {
            recentSearches.splice(index, 1);
        }
        
        recentSearches.unshift(keyword);
        
        // Limit the number of recent searches
        if (recentSearches.length > MAX_RECENT_SEARCHES) {
            recentSearches = recentSearches.slice(0, MAX_RECENT_SEARCHES);
        }
        
        // Update the appropriate list
        if (type === 'surah') {
            surahRecentSearches = recentSearches;
            localStorage.setItem('surahRecentSearches', JSON.stringify(surahRecentSearches));
        } else {
            ayahRecentSearches = recentSearches;
            localStorage.setItem('ayahRecentSearches', JSON.stringify(ayahRecentSearches));
        }
        
        // Display the updated recent searches
        displayRecentSearches(type, recentSearches);
    }
    
    // Function to display recent searches
    function displayRecentSearches(type, searches) {
        const container = $(`#${type}-recent-searches`);
        container.empty();
        
        if (searches.length > 0) {
            $(`#${type}-recents`).removeClass('d-none');
            
            searches.forEach(search => {
                const searchItem = $('<span class="recent-search-item"></span>');
                searchItem.text(search);
                searchItem.data('type', type);
                container.append(searchItem);
            });
        } else {
            $(`#${type}-recents`).addClass('d-none');
        }
    }
}); 