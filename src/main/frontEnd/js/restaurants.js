// ============================================
// restaurants.js - Gestion de la page restaurants
// ============================================

let allRestaurants = [];

let currentPage = 1;
const itemsPerPage = 6;
let totalPages = 1;

// ========== CHARGEMENT INITIAL ==========
document.addEventListener('DOMContentLoaded', () => {
    console.log('📄 Page restaurants.js chargée');
    loadRestaurants();
    setupFilters();
});

// ========== PLACEHOLDERS DE CHARGEMENT (Skeleton Loaders) ==========
function showLoadingPlaceholders() {
    const container = document.getElementById('restaurants-list');
    if (!container) {
        console.error('❌ Element #restaurants-list introuvable');
        return;
    }
    
    container.innerHTML = `
        <div class="skeleton skeleton-card"></div>
        <div class="skeleton skeleton-card"></div>
        <div class="skeleton skeleton-card"></div>
    `;
    console.log('⏳ Skeleton loaders affichés');
}

function showError(message) {
    const container = document.getElementById('restaurants-list');
    if (!container) {
        console.error('❌ Element #restaurants-list introuvable');
        return;
    }
    
    container.innerHTML = `
        <div style="text-align: center; padding: 2rem; color: var(--error);">
            <p style="font-size: 1.2rem; margin-bottom: 1rem;">❌ ${message}</p>
            <button onclick="loadRestaurants()" class="btn" style="margin-top: 1rem;">
                🔄 Réessayer
            </button>
        </div>
    `;
    console.error('❌', message);
}

// ========== CHARGEMENT DES RESTAURANTS ==========
    async function loadRestaurants(filters = {}, page = 1) {
        console.log('🔄 Chargement des restaurants - page:', page, 'filtres:', filters);
        showLoadingPlaceholders();
        
        try {
            //  NOUVEAU : Ajouter les paramètres de pagination
            const params = new URLSearchParams({
                ...filters,
                page: page,
                limit: itemsPerPage
            });
            
            // Utilise fetchWithCache
            const response = await fetch(`${API_GATEWAY_URL}/api/restaurants?${params}`);
            const data = await response.json();
            
            console.log('✅ Réponse paginée reçue:', data);
            
            //  NOUVEAU : Extraire les données et métadonnées
            const restaurants = data.data || data; // Compatible avec ancien format
            currentPage = data.page || 1;
            totalPages = data.totalPages || 1;
            
            console.log(`📄 Page ${currentPage}/${totalPages} - ${restaurants.length} restaurants`);
            
            allRestaurants = restaurants;
            displayRestaurants(restaurants);
            updatePaginationControls(); //  NOUVEAU
            
        } catch (error) {
            console.error('❌ Erreur chargement restaurants:', error);
            showError('Impossible de charger les restaurants. Vérifiez que le serveur est démarré sur le port 8080.');
        }
    }
// ========== AFFICHAGE DES RESTAURANTS ==========
function displayRestaurants(restaurants) {
    const container = document.getElementById('restaurants-list');
    
    if (!container) {
        console.error('❌ Element #restaurants-list introuvable');
        return;
    }
    
    //  Cas  : Aucun restaurant trouvé
    if (!restaurants || restaurants.length === 0) {
        container.innerHTML = `
            <div style="text-align: center; padding: 3rem; color: #999;">
                <p style="font-size: 1.2rem; margin-bottom: 1rem;">🔍 Aucun restaurant trouvé</p>
                <p style="font-size: 0.9rem;">Essayez de modifier les filtres ou 
                   <button onclick="loadRestaurants()" class="btn" style="display: inline-block; padding: 0.5rem 1rem; margin-top: 1rem;">
                       Réinitialiser
                   </button>
                </p>
            </div>
        `;
        return;
    }
    
    //  Afficher les restaurants
    container.innerHTML = restaurants.map(restaurant => `
        <div class="restaurant" style="background: white; padding: 1.5rem; border-radius: 12px; margin-bottom: 1rem; box-shadow: 0 2px 8px rgba(0,0,0,0.1); transition: transform 0.2s;">
            <h3 style="color: var(--primary); margin-bottom: 0.5rem;">${escapeHtml(restaurant.name)}</h3>
            <p style="color: #666; margin-bottom: 0.5rem;">
                <strong>🍽️ Cuisine:</strong> ${escapeHtml(restaurant.cuisineType || 'N/A')}
            </p>
            <p style="color: #666; margin-bottom: 1rem;">
                <strong>📋 Plats:</strong> ${restaurant.dishes ? restaurant.dishes.length : 0}
            </p>
            <button onclick="viewRestaurant(${restaurant.id})" class="btn" style="width: 100%;">
                👀 Voir le menu
            </button>
        </div>
    `).join('');
    
    console.log(' Affichage de', restaurants.length, 'restaurants');
}

// ========== CONFIGURATION DES FILTRES ==========
function setupFilters() {
    const filterElements = [
        'availability',
        'diet',
        'price',
        'cuisine',
        'type'
    ];
    
    console.log('🔧 Configuration des filtres avec debounce...');
    
    // Appliquer debounce sur tous les filtres
    filterElements.forEach(filterId => {
        const element = document.getElementById(filterId);
        if (element) {
            // ✅ debounce défini dans api.js
            element.addEventListener('change', debounce(applyFilters, 300));
            console.log('✅ Filtre', filterId, 'configuré avec debounce');
        } else {
            console.warn('⚠️ Filtre', filterId, 'introuvable dans le HTML');
        }
    });
}

// ========== APPLICATION DES FILTRES ==========
function applyFilters() {
    console.log('🔍 Application des filtres...');
    
    const filters = {
        availability: document.getElementById('availability')?.value,
        diet: document.getElementById('diet')?.value,
        price: document.getElementById('price')?.value,
        cuisineType: document.getElementById('cuisine')?.value,
        type: document.getElementById('type')?.value
    };
    
    // Supprimer les valeurs vides
    Object.keys(filters).forEach(key => {
        if (!filters[key] || filters[key] === '') {
            delete filters[key];
        }
    });
    
    console.log('📊 Filtres actifs:', filters);
    
    //  NOUVEAU : Réinitialiser à la page 1 lors du filtrage
    currentPage = 1;
    loadRestaurants(filters, 1);
}
// ========== VOIR UN RESTAURANT ==========
function viewRestaurant(restaurantId) {
    if (!restaurantId) {
        console.error('❌ ID de restaurant invalide');
        alert('Erreur: ID de restaurant invalide');
        return;
    }
    
    console.log('👀 Navigation vers restaurant ID:', restaurantId);
    
    //  Sauvegarder l'ID du restaurant dans sessionStorage
    sessionStorage.setItem('selectedRestaurantId', restaurantId);
    console.log('💾 ID sauvegardé dans sessionStorage');
    
    // Redirection vers order.html
    window.location.href = 'order.html';
}

// ========== UTILITAIRE : Échapper HTML ==========
function escapeHtml(str) {
    if (!str) return '';
    const div = document.createElement('div');
    div.textContent = str;
    return div.innerHTML;
}


// ========== CONTRÔLES DE PAGINATION ==========
function updatePaginationControls() {
    let paginationContainer = document.getElementById('pagination-controls');
    
    // Créer le conteneur s'il n'existe pas
    if (!paginationContainer) {
        const listContainer = document.getElementById('restaurants-list');
        if (!listContainer) return;
        
        paginationContainer = document.createElement('div');
        paginationContainer.id = 'pagination-controls';
        paginationContainer.style.cssText = 'display: flex; justify-content: center; align-items: center; gap: 1rem; margin-top: 2rem; padding: 1rem;';
        listContainer.parentNode.insertBefore(paginationContainer, listContainer.nextSibling);
    }
    
    // Masquer si une seule page
    if (totalPages <= 1) {
        paginationContainer.style.display = 'none';
        return;
    }
    
    paginationContainer.style.display = 'flex';
    
    paginationContainer.innerHTML = `
        <button 
            onclick="goToPage(${currentPage - 1})" 
            class="btn" 
            style="padding: 0.5rem 1rem;"
            ${currentPage <= 1 ? 'disabled' : ''}>
            ← Précédent
        </button>
        
        <span style="font-weight: bold; font-size: 1.1rem;">
            Page ${currentPage} / ${totalPages}
        </span>
        
        <button 
            onclick="goToPage(${currentPage + 1})" 
            class="btn" 
            style="padding: 0.5rem 1rem;"
            ${currentPage >= totalPages ? 'disabled' : ''}>
            Suivant →
        </button>
    `;
}

    function goToPage(page) {
        if (page < 1 || page > totalPages) return;
        
        console.log('📄 Navigation vers page', page);
        
        // Récupérer les filtres actuels
        const filters = {
            availability: document.getElementById('availability')?.value,
            diet: document.getElementById('diet')?.value,
            price: document.getElementById('price')?.value,
            cuisineType: document.getElementById('cuisine')?.value,
            type: document.getElementById('type')?.value
        };
        
        // Supprimer les valeurs vides
        Object.keys(filters).forEach(key => {
            if (!filters[key] || filters[key] === '') {
                delete filters[key];
            }
        });
        
        loadRestaurants(filters, page);
        
        // Scroll vers le haut
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }

    // ========== CONTRÔLES DE PAGINATION ==========
function updatePaginationControls() {
    let paginationContainer = document.getElementById('pagination-controls');
    
    // Créer le conteneur s'il n'existe pas
    if (!paginationContainer) {
        const listContainer = document.getElementById('restaurants-list');
        if (!listContainer) return;
        
        paginationContainer = document.createElement('div');
        paginationContainer.id = 'pagination-controls';
        paginationContainer.style.cssText = 'display: flex; justify-content: center; align-items: center; gap: 1rem; margin-top: 2rem; padding: 1rem;';
        listContainer.parentNode.insertBefore(paginationContainer, listContainer.nextSibling);
    }
    
    // Masquer si une seule page
    if (totalPages <= 1) {
        paginationContainer.style.display = 'none';
        return;
    }
    
    paginationContainer.style.display = 'flex';
    
    paginationContainer.innerHTML = `
        <button 
            onclick="goToPage(${currentPage - 1})" 
            class="btn" 
            style="padding: 0.5rem 1rem;"
            ${currentPage <= 1 ? 'disabled' : ''}>
            ← Précédent
        </button>
        
        <span style="font-weight: bold; font-size: 1.1rem;">
            Page ${currentPage} / ${totalPages}
        </span>
        
        <button 
            onclick="goToPage(${currentPage + 1})" 
            class="btn" 
            style="padding: 0.5rem 1rem;"
            ${currentPage >= totalPages ? 'disabled' : ''}>
            Suivant →
        </button>
    `;
}

function goToPage(page) {
    if (page < 1 || page > totalPages) return;
    
    console.log('📄 Navigation vers page', page);
    
    // Récupérer les filtres actuels
    const filters = {
        availability: document.getElementById('availability')?.value,
        diet: document.getElementById('diet')?.value,
        price: document.getElementById('price')?.value,
        cuisineType: document.getElementById('cuisine')?.value,
        type: document.getElementById('type')?.value
    };
    
    // Supprimer les valeurs vides
    Object.keys(filters).forEach(key => {
        if (!filters[key] || filters[key] === '') {
            delete filters[key];
        }
    });
    
    loadRestaurants(filters, page);
    
    // Scroll vers le haut
    window.scrollTo({ top: 0, behavior: 'smooth' });
}