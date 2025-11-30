// ============================================
// order.js - Gestion de la page de commande
// ============================================

let selectedRestaurant = null;
let cart = [];
let currentDish = null;

// ========== CHARGEMENT INITIAL ==========
document.addEventListener('DOMContentLoaded', async () => {
    console.log('📄 Page order.js chargée');
    
    const restaurantId = sessionStorage.getItem('selectedRestaurantId');
    
    // ✅ Gérer le cas où aucun restaurant n'est sélectionné
    if (!restaurantId) {
        console.error('❌ Aucun restaurant sélectionné');
        const menuContainer = document.getElementById('menu');
        if (menuContainer) {
            menuContainer.innerHTML = `
                <div style="text-align: center; padding: 2rem; color: var(--error);">
                    <p style="font-size: 1.2rem; margin-bottom: 1rem;">⚠️ Aucun restaurant sélectionné</p>
                    <a href="restaurant.html" class="btn">← Retour aux restaurants</a>
                </div>
            `;
        }
        return;
    }
    
    console.log('🔑 Restaurant ID récupéré:', restaurantId);
    
    await loadRestaurant(restaurantId);
    await loadDeliverySlots(restaurantId);
    setupEventListeners();
});

// ========== CHARGEMENT DU RESTAURANT ==========
async function loadRestaurant(restaurantId) {
    console.log('🔄 Chargement du restaurant ID:', restaurantId);
    
    try {
        // ✅ Utilise API.getRestaurantById défini dans api.js
        selectedRestaurant = await API.getRestaurantById(restaurantId);
        console.log('✅ Restaurant chargé:', selectedRestaurant);
        displayMenu(selectedRestaurant);
    } catch (error) {
        console.error('❌ Erreur chargement restaurant:', error);
        const menuContainer = document.getElementById('menu');
        if (menuContainer) {
            menuContainer.innerHTML = `
                <div style="text-align: center; padding: 2rem; color: var(--error);">
                    <p style="font-size: 1.2rem; margin-bottom: 1rem;">❌ Impossible de charger le menu</p>
                    <p style="margin-bottom: 1rem;">${error.message}</p>
                    <a href="restaurant.html" class="btn">← Retour aux restaurants</a>
                </div>
            `;
        }
    }
}

// ========== AFFICHAGE DU MENU ==========
function displayMenu(restaurant) {
    const menuContainer = document.getElementById('menu');
    
    if (!menuContainer) {
        console.error('❌ Element #menu introuvable');
        return;
    }
    
    if (!restaurant.dishes || restaurant.dishes.length === 0) {
        menuContainer.innerHTML = '<p>Aucun plat disponible.</p>';
        return;
    }
    
    menuContainer.innerHTML = `
        <h2 style="color: var(--primary); margin-bottom: 1.5rem;">${escapeHtml(restaurant.name)}</h2>
        <div id="dishes-list" style="display: grid; gap: 1rem;">
            ${restaurant.dishes.map(dish => `
                <div class="dish" style="background: white; padding: 1.5rem; border-radius: 12px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
                    <h3 style="color: var(--secondary); margin-bottom: 0.5rem;">${escapeHtml(dish.name)}</h3>
                    <p style="color: #666; margin-bottom: 0.5rem;">${escapeHtml(dish.description || '')}</p>
                    <p style="font-size: 1.2rem; font-weight: bold; color: var(--primary); margin-bottom: 1rem;">
                        ${dish.price}€
                    </p>
                    ${dish.toppings && dish.toppings.length > 0 
                        ? `<button onclick='selectDishWithToppings(${JSON.stringify(dish).replace(/'/g, "\\'")})'
                             class="btn" style="width: 100%;">
                             ➕ Ajouter avec options
                           </button>`
                        : `<button onclick='addToCart(${JSON.stringify(dish).replace(/'/g, "\\'")})'
                             class="btn" style="width: 100%;">
                             ➕ Ajouter au panier
                           </button>`
                    }
                </div>
            `).join('')}
        </div>
    `;
    
    console.log('✅ Menu affiché avec', restaurant.dishes.length, 'plats');
}

// ========== GESTION DES TOPPINGS ==========
function selectDishWithToppings(dish) {
    console.log('🍕 Sélection du plat avec toppings:', dish.name);
    currentDish = dish;
    
    const toppingsSection = document.getElementById('toppings-section');
    const toppingsList = document.getElementById('toppings-list');
    
    if (!toppingsSection || !toppingsList) {
        console.error('❌ Sections toppings introuvables');
        return;
    }
    
    toppingsList.innerHTML = dish.toppings.map((topping) => `
        <label style="display: block; margin-bottom: 0.5rem; cursor: pointer;">
            <input type="checkbox" class="topping-checkbox" 
                   data-topping='${JSON.stringify(topping).replace(/'/g, "\\'")}' 
                   style="margin-right: 0.5rem;">
            ${escapeHtml(topping.name)} (+${topping.price}€)
        </label>
    `).join('');
    
    toppingsSection.classList.remove('hidden');
    console.log('✅ Toppings affichés');
}

// ========== AJOUT AU PANIER ==========
function addToCart(dish, selectedToppings = []) {
    console.log('🛒 Ajout au panier:', dish.name, 'avec', selectedToppings.length, 'toppings');
    
    cart.push({
        ...dish,
        selectedToppings: selectedToppings
    });
    
    updateCartDisplay();
    
    // Masquer la section toppings
    const toppingsSection = document.getElementById('toppings-section');
    if (toppingsSection) {
        toppingsSection.classList.add('hidden');
    }
    
    console.log('✅ Panier mis à jour. Total:', cart.length, 'articles');
}

// ========== MISE À JOUR DU PANIER ==========
function updateCartDisplay() {
    const cartList = document.getElementById('cart-list');
    const totalElement = document.getElementById('total');
    
    if (!cartList || !totalElement) {
        console.error('❌ Éléments du panier introuvables');
        return;
    }
    
    if (cart.length === 0) {
        cartList.innerHTML = '<li style="color: #999;">Panier vide</li>';
        totalElement.textContent = '0';
        return;
    }
    
    let total = 0;
    cartList.innerHTML = cart.map((item, index) => {
        const toppingsPrice = item.selectedToppings 
            ? item.selectedToppings.reduce((sum, t) => sum + t.price, 0) 
            : 0;
        const itemTotal = item.price + toppingsPrice;
        total += itemTotal;
        
        return `
            <li style="margin-bottom: 1rem; padding: 1rem; background: #f9f9f9; border-radius: 8px;">
                <div style="display: flex; justify-content: space-between; align-items: center;">
                    <div>
                        <strong>${escapeHtml(item.name)}</strong> - ${itemTotal.toFixed(2)}€
                        ${item.selectedToppings && item.selectedToppings.length > 0 
                            ? `<br><small style="color: #666;">+ ${item.selectedToppings.map(t => escapeHtml(t.name)).join(', ')}</small>` 
                            : ''}
                    </div>
                    <button onclick="removeFromCart(${index})" 
                            style="background: var(--error); color: white; border: none; padding: 0.5rem 1rem; border-radius: 5px; cursor: pointer;">
                        ❌ Retirer
                    </button>
                </div>
            </li>
        `;
    }).join('');
    
    totalElement.textContent = total.toFixed(2);
    console.log('💰 Total du panier:', total.toFixed(2), '€');
}

function removeFromCart(index) {
    console.log('🗑️ Suppression de l\'article', index);
    cart.splice(index, 1);
    updateCartDisplay();
}

// ========== CHARGEMENT DES CRÉNEAUX ==========
async function loadDeliverySlots(restaurantId) {
    console.log('🔄 Chargement des créneaux pour restaurant ID:', restaurantId);
    
    try {
        const slots = await API.getTimeSlots(restaurantId);
        console.log('✅ Créneaux reçus:', slots.length);
        displayDeliverySlots(slots);
    } catch (error) {
        console.error('❌ Erreur chargement créneaux:', error);
    }
}

function displayDeliverySlots(slots) {
    const select = document.getElementById('delivery-slot');
    
    if (!select) {
        console.error('❌ Element #delivery-slot introuvable');
        return;
    }
    
    if (!slots || slots.length === 0) {
        select.innerHTML = '<option>Aucun créneau disponible</option>';
        return;
    }
    
    select.innerHTML = slots.map(slot => `
        <option value="${slot.startTime}-${slot.endTime}">
            ${slot.startTime} - ${slot.endTime} (${slot.availableCapacity} places)
        </option>
    `).join('');
    
    console.log('✅ Créneaux affichés');
}

// ========== CONFIGURATION DES ÉVÉNEMENTS ==========
function setupEventListeners() {
    console.log('🔧 Configuration des événements...');
    
    // Ajouter avec toppings
    const addWithToppingsBtn = document.getElementById('add-with-toppings');
    if (addWithToppingsBtn) {
        addWithToppingsBtn.addEventListener('click', () => {
            const checkboxes = document.querySelectorAll('.topping-checkbox:checked');
            const selectedToppings = Array.from(checkboxes).map(cb => JSON.parse(cb.dataset.topping));
            addToCart(currentDish, selectedToppings);
        });
    }
    
    // Confirmer la commande
    const confirmBtn = document.getElementById('btn-confirm');
    if (confirmBtn) {
        confirmBtn.addEventListener('click', confirmOrder);
    }
    
    console.log('✅ Événements configurés');
}

// ========== CONFIRMATION DE COMMANDE ==========
async function confirmOrder() {
    console.log('✅ Tentative de confirmation de commande...');
    
    // ✅ Validation : panier non vide
    if (cart.length === 0) {
        alert('⚠️ Votre panier est vide !');
        console.warn('⚠️ Panier vide');
        return;
    }
    
    const orderData = {
        restaurantId: selectedRestaurant.id,
        dishes: cart.map(item => ({
            name: item.name,
            price: item.price
        })),
        deliveryLocation: "Campus Sophia" // À adapter
    };
    
    console.log('📤 Envoi de la commande:', orderData);
    
    try {
        const order = await API.createOrder(orderData);
        console.log('✅ Commande créée:', order);
        alert(`✅ Commande créée ! ID: ${order.id}`);
        window.location.href = 'confirmation.html';
    } catch (error) {
        console.error('❌ Erreur création commande:', error);
        alert('❌ Erreur lors de la création de la commande');
    }
}

// ========== UTILITAIRE : Échapper HTML ==========
function escapeHtml(str) {
    if (!str) return '';
    const div = document.createElement('div');
    div.textContent = str;
    return div.innerHTML;
}