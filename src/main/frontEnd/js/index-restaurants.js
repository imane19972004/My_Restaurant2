// ============================================
// index-restaurants.js 
// Affichage des cartes sans liste de plats
// ============================================

let currentPage = 1;
let currentLimit = 6;

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

async function loadRestaurants() {
    try {
        const params = new URLSearchParams();
        params.append('page', currentPage);
        params.append('limit', currentLimit);

        const availabilityFilter = document.getElementById('availability').value;
        const categoryFilter = document.getElementById('category').value;
        const cuisineFilter = document.getElementById('cuisine').value;

        if (availabilityFilter) params.append('availableNow', availabilityFilter);
        if (categoryFilter) params.append('category', categoryFilter);
        if (cuisineFilter) params.append('cuisineType', cuisineFilter);

        const url = `/api/restaurants?${params.toString()}`;
        window.history.pushState({ path: url }, '', url);

        const response = await fetch(url);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        displayRestaurants(data.data || []);
        displayPagination(data.page, data.totalItems, currentLimit);

    } catch (error) {
        console.error('Error loading restaurants:', error);
        document.getElementById('restaurants-grid').innerHTML =
            `<div class="error">Failed to load restaurants.</div>`;
    }
}

// ✅ NOUVELLE VERSION AMÉLIORÉE - Affiche uniquement les infos essentielles
function displayRestaurants(restaurants) {
    const grid = document.getElementById('restaurants-grid');

    if (!restaurants || restaurants.length === 0) {
        grid.innerHTML = '<div class="no-results">No restaurants found.</div>';
        return;
    }

    grid.innerHTML = restaurants.map(restaurant => {
        const dishes = restaurant.dishes || [];
        const cuisineType = restaurant.cuisineType || 'GENERAL';
        const cuisineBadgeClass = cuisineType.toLowerCase();
        
        // ✅ Déterminer si le restaurant est ouvert
        const isOpen = restaurant.open !== false;
        const statusBadge = isOpen 
            ? '<span class="status-badge open">🟢 Open</span>' 
            : '<span class="status-badge closed">🔴 Closed</span>';
        
        // ✅ Utiliser le priceRange du backend et mapper si nécessaire
        let priceRange = '€'; // Valeur par défaut
        
        if (restaurant.priceRange && restaurant.priceRange !== '?') {
            const priceRangeValue = restaurant.priceRange.toUpperCase().trim();
            
            // Mapper les différents formats vers les symboles
            switch(priceRangeValue) {
                case 'LOW':
                case 'CHEAP':
                case '€':
                    priceRange = '€';
                    break;
                case 'MEDIUM':
                case 'MODERATE':
                case '€€':
                    priceRange = '€€';
                    break;
                case 'HIGH':
                case 'EXPENSIVE':
                case '€€€':
                    priceRange = '€€€';
                    break;
                default:
                    // Si c'est déjà un symbole valide, l'utiliser
                    if (['€', '€€', '€€€'].includes(restaurant.priceRange)) {
                        priceRange = restaurant.priceRange;
                    } else {
                        // Fallback : calculer automatiquement
                        const prices = dishes.map(d => d.price).filter(p => p > 0);
                        const avgPrice = prices.length > 0 
                            ? prices.reduce((a, b) => a + b, 0) / prices.length 
                            : 0;
                        
                        if (avgPrice > 15) priceRange = '€€€';
                        else if (avgPrice > 8) priceRange = '€€';
                        else priceRange = '€';
                    }
            }
        } else {
            // Fallback : calculer basé sur les prix moyens des plats
            const prices = dishes.map(d => d.price).filter(p => p > 0);
            const avgPrice = prices.length > 0 
                ? prices.reduce((a, b) => a + b, 0) / prices.length 
                : 0;
            
            if (avgPrice > 15) priceRange = '€€€';
            else if (avgPrice > 8) priceRange = '€€';
            else priceRange = '€';
        }
        
        // ✅ Analyser les types de plats disponibles
        const hasVegetarian = dishes.some(d => 
            (d.tags && d.tags.includes('VEGETARIAN')) || 
            d.category === 'VEGETARIAN' ||
            d.dishType === 'VEGETARIAN' ||
            (d.name && d.name.toLowerCase().includes('vegetarian'))
        );
        
        const hasVegan = dishes.some(d => 
            (d.tags && d.tags.includes('VEGAN')) || 
            d.category === 'VEGAN' ||
            d.dishType === 'VEGAN' ||
            (d.name && d.name.toLowerCase().includes('vegan'))
        );
        
        const hasGlutenFree = dishes.some(d => 
            (d.tags && d.tags.includes('GLUTEN_FREE')) ||
            (d.name && d.name.toLowerCase().includes('gluten'))
        );
        
        const hasOrganic = dishes.some(d => 
            (d.tags && d.tags.includes('ORGANIC')) ||
            (d.name && d.name.toLowerCase().includes('organic')) ||
            (d.name && d.name.toLowerCase().includes('bio'))
        );
        
        // ✅ Créer les badges de plats spéciaux
        let specialDishBadges = '';
        if (hasVegetarian) specialDishBadges += '<span class="special-badge vegetarian">🌱 Vegetarian</span>';
        if (hasVegan) specialDishBadges += '<span class="special-badge vegan">🥗 Vegan</span>';
        if (hasGlutenFree) specialDishBadges += '<span class="special-badge gluten-free">🌾 Gluten-Free</span>';
        if (hasOrganic) specialDishBadges += '<span class="special-badge organic">🌿 Organic</span>';

        return `
            <div class="restaurant-card" onclick="goToRestaurantMenu(${restaurant.id})">
                <div class="restaurant-header">
                    <div class="header-top">
                        <h3>${escapeHtml(restaurant.name)}</h3>
                        ${statusBadge}
                    </div>
                    <span class="cuisine-badge ${cuisineBadgeClass}">${escapeHtml(cuisineType)}</span>
                </div>
                
                <div class="restaurant-info">
                    <div class="info-row">
                        <span class="info-item">
                            <span class="icon">🍽️</span>
                            <span class="text">${dishes.length} dishes</span>
                        </span>
                        <span class="info-item">
                            <span class="icon">💰</span>
                            <span class="text">${priceRange}</span>
                        </span>
                    </div>
                    
                    ${specialDishBadges ? `
                        <div class="special-dishes">
                            ${specialDishBadges}
                        </div>
                    ` : ''}
                </div>
                
                <div class="restaurant-footer">
                    <button class="view-menu-btn" onclick="event.stopPropagation(); goToRestaurantMenu(${restaurant.id});">
                        View Menu →
                    </button>
                </div>
            </div>
        `;
    }).join('');
}

function displayPagination(page, totalItems, limit) {
    const controls = document.getElementById('pagination-controls');
    const totalPages = Math.ceil(totalItems / limit);

    if (totalPages <= 1) {
        controls.innerHTML = '';
        return;
    }

    let html = '<div class="pagination">';

    if (page > 1) {
        html += `<button onclick="goToPage(${page - 1})" class="page-btn">← Previous</button>`;
    }

    let dotsAdded = false;
    for (let i = 1; i <= totalPages; i++) {
        if (i === page) {
            html += `<button class="page-btn active">${i}</button>`;
            dotsAdded = false;
        } else if (i === 1 || i === totalPages || Math.abs(i - page) <= 2) {
            html += `<button onclick="goToPage(${i})" class="page-btn">${i}</button>`;
            dotsAdded = false;
        } else if (!dotsAdded) {
            html += `<span class="page-dots">...</span>`;
            dotsAdded = true;
        }
    }

    if (page < totalPages) {
        html += `<button onclick="goToPage(${page + 1})" class="page-btn">Next →</button>`;
    }

    html += '</div>';
    controls.innerHTML = html;
}

function goToPage(page) {
    currentPage = page;
    loadRestaurants();
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

window.goToRestaurantMenu = function(restaurantId) {
    if (!restaurantId || restaurantId === 0) {
        console.error('Invalid restaurant ID:', restaurantId);
        return;
    }

    console.log('🍽️ Navigation vers restaurant ID:', restaurantId);
    sessionStorage.setItem('selectedRestaurantId', restaurantId.toString());
    window.location.href = `/order.html`;
}

function loadFiltersFromURL() {
    const params = new URLSearchParams(window.location.search);

    if (params.has('availableNow')) {
        document.getElementById('availability').value = params.get('availableNow');
    }
    if (params.has('category')) {
        document.getElementById('category').value = params.get('category');
    }
    if (params.has('cuisineType')) {
        document.getElementById('cuisine').value = params.get('cuisineType');
    }
    if (params.has('page')) {
        currentPage = parseInt(params.get('page')) || 1;
    }
}

document.addEventListener('DOMContentLoaded', () => {
    console.log('📄 index-restaurants.js chargé - Version améliorée sans listes de plats');
    
    loadFiltersFromURL();
    loadRestaurants();

    document.getElementById('availability').addEventListener('change', () => {
        currentPage = 1;
        loadRestaurants();
    });

    document.getElementById('category').addEventListener('change', () => {
        currentPage = 1;
        loadRestaurants();
    });

    document.getElementById('cuisine').addEventListener('change', () => {
        currentPage = 1;
        loadRestaurants();
    });
});