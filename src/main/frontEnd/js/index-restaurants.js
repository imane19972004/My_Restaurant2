let currentPage = 1;
let currentLimit = 9;

// FONCTION SÉCURITÉ: Échappe les caractères HTML pour éviter XSS
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

        if (availabilityFilter) {
            params.append('availableNow', availabilityFilter);
        }
        if (categoryFilter) {
            params.append('category', categoryFilter);
        }
        if (cuisineFilter) {
            params.append('cuisineType', cuisineFilter);
        }

        const url = `/api/restaurants?${params.toString()}`;
        console.log('Fetching:', url);

        // Updates browser URL to reflect current filters
        const displayUrl = `/api/restaurants?${params.toString()}`;
        window.history.pushState({ path: displayUrl }, '', displayUrl);

        const response = await fetch(url);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        console.log('Received data:', data);

        displayRestaurants(data.data || []);
        displayPagination(data.page, data.totalItems, currentLimit);

    } catch (error) {
        console.error('Error loading restaurants:', error);
        document.getElementById('restaurants-grid').innerHTML = 
            `<div class="error">Failed to load restaurants. Please try again later.</div>`;
    }
}

function displayRestaurants(restaurants) {
    const grid = document.getElementById('restaurants-grid');

    if (!restaurants || restaurants.length === 0) {
        grid.innerHTML = '<div class="no-results">No restaurants found with these filters.</div>';
        return;
    }

    grid.innerHTML = restaurants.map(restaurant => {
        const dishes = restaurant.dishes || [];
        const dishesHtml = dishes.slice(0, 4).map(dish => `
            <div class="dish-item">
                <span class="dish-name">${escapeHtml(dish.name)}</span>
                <span class="dish-price">${dish.price}€</span>
            </div>
        `).join('');

        const cuisineType = restaurant.cuisineType || 'GENERAL';
        const cuisineBadgeClass = cuisineType.toLowerCase();

        return `
            <div class="restaurant-card" onclick="goToRestaurant(${restaurant.id})">
                
                <div class="restaurant-header">
                    <h3>${escapeHtml(restaurant.name)}</h3>
                    <span class="cuisine-badge ${cuisineBadgeClass}">${escapeHtml(cuisineType)}</span>
                </div>

                <div class="restaurant-info">
                    <span class="dish-count">${dishes.length} dishes</span>
                </div>

                <div class="dishes-preview">
                    ${dishesHtml}
                    ${dishes.length > 4 ? `<div class="more-dishes">+${dishes.length - 4} more</div>` : ''}
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

window.goToRestaurant = function(restaurantId) {
    window.location.href = `/restaurant.html?id=${restaurantId}`;
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
        const pageParam = parseInt(params.get('page'));
        currentPage = Number.isNaN(pageParam) ? 1 : pageParam;
    }
}

document.addEventListener('DOMContentLoaded', () => {
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