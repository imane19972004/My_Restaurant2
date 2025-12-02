// ============================================
// order.js - Gestion complète de la commande avec paiement
// ============================================

let selectedRestaurant = null;
let cart = [];
let currentDish = null;
let currentOrderId = null;

// ========== CHARGEMENT INITIAL ==========
document.addEventListener('DOMContentLoaded', async () => {
    console.log('📄 Page order.js chargée');

    const restaurantId = sessionStorage.getItem('selectedRestaurantId');

    if (!restaurantId) {
        console.error('❌ Aucun restaurant sélectionné');
        showError('Aucun restaurant sélectionné', 'Veuillez d\'abord sélectionner un restaurant');
        return;
    }

    console.log('🔑 Restaurant ID récupéré:', restaurantId);

    await loadRestaurant(restaurantId);
    await loadDeliverySlots(restaurantId);
    setupEventListeners();
    updateCartDisplay();
});

// ========== CHARGEMENT DU RESTAURANT ==========
async function loadRestaurant(restaurantId) {
    console.log('🔄 Chargement du restaurant ID:', restaurantId);

    try {
        const response = await fetch(`http://localhost:8080/api/restaurants/${restaurantId}`);

        if (!response.ok) {
            if (response.status === 404) {
                throw new Error('Restaurant introuvable (ID: ' + restaurantId + ')');
            }
            throw new Error(`Erreur HTTP ${response.status}`);
        }

        selectedRestaurant = await response.json();
        console.log('✅ Restaurant chargé:', selectedRestaurant);
        displayMenu(selectedRestaurant);
    } catch (error) {
        console.error('❌ Erreur chargement restaurant:', error);
        showError('Impossible de charger le menu', error.message);
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

    // Grouper les plats par catégorie
    const dishesByCategory = {};
    restaurant.dishes.forEach(dish => {
        const category = dish.category || 'OTHER';
        if (!dishesByCategory[category]) {
            dishesByCategory[category] = [];
        }
        dishesByCategory[category].push(dish);
    });

    // Ordre d'affichage des catégories
    const categoryOrder = ['STARTER', 'MAIN_COURSE', 'SIDE', 'DESSERT', 'DRINK', 'OTHER'];
    const categoryNames = {
        'STARTER': '🥗 Starters',
        'MAIN_COURSE': '🍽️ Main courses',
        'SIDE': '🍟 Sides',
        'DESSERT': '🍰 Desserts',
        'DRINK': '🥤 Drinks',
        'OTHER': '📦 others'
    };

    menuContainer.innerHTML = `
        <div style="margin-bottom: 2rem;">
            <h2 style="color: var(--primary); margin-bottom: 0.5rem;">${escapeHtml(restaurant.name)}</h2>
            <p style="color: #666; font-size: 0.9rem;">
                ${restaurant.cuisineType ? '🍴 Cuisine ' + escapeHtml(restaurant.cuisineType) : ''}
            </p>
        </div>
        
        ${categoryOrder.map(category => {
        if (!dishesByCategory[category]) return '';

        return `
                <div class="category-section" style="margin-bottom: 2rem;">
                    <h3 style="color: var(--secondary); margin-bottom: 1rem; padding-bottom: 0.5rem; border-bottom: 2px solid #eee;">
                        ${categoryNames[category] || category}
                    </h3>
                    <div style="display: grid; gap: 1rem;">
                        ${dishesByCategory[category].map(dish => `
                            <div class="dish" style="background: white; padding: 1.5rem; border-radius: 12px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
                                <div style="display: flex; justify-content: space-between; align-items: start; margin-bottom: 0.5rem;">
                                    <h4 style="color: var(--secondary); margin: 0;">${escapeHtml(dish.name)}</h4>
                                    <span style="font-size: 1.2rem; font-weight: bold; color: var(--primary);">
                                        ${dish.price.toFixed(2)}€
                                    </span>
                                </div>
                                ${dish.description ? `
                                    <p style="color: #666; margin-bottom: 1rem; font-size: 0.9rem;">
                                        ${escapeHtml(dish.description)}
                                    </p>
                                ` : ''}
                                ${dish.toppings && dish.toppings.length > 0
            ? `<button onclick='selectDishWithToppings(${JSON.stringify(sanitizeDish(dish)).replace(/'/g, "\\'")})'
                                         class="btn" style="width: 100%;">
                                         ➕ Add with toppings
                                       </button>`
            : `<button onclick='addToCart(${JSON.stringify(sanitizeDish(dish)).replace(/'/g, "\\'")})'
                                         class="btn" style="width: 100%;">
                                         ➕ Add to card
                                       </button>`
        }
                            </div>
                        `).join('')}
                    </div>
                </div>
            `;
    }).join('')}
    `;

    console.log('✅ Menu affiché avec', restaurant.dishes.length, 'plats');
}

// ========== ✅ NOUVELLE FONCTION : Nettoyer les données pour JSON ==========
function sanitizeDish(dish) {
    return {
        id: dish.id,
        name: cleanString(dish.name),
        description: cleanString(dish.description),
        price: dish.price,
        category: dish.category,
        dishType: dish.dishType,
        toppings: dish.toppings ? dish.toppings.map(t => ({
            name: cleanString(t.name),
            price: t.price
        })) : []
    };
}

// ========== ✅ FONCTION : Nettoyer les chaînes (supprimer caractères invalides) ==========
function cleanString(str) {
    if (!str) return '';
    // Supprimer les caractères de contrôle (0x00-0x1F sauf tab, newline, carriage return)
    return str.replace(/[\x00-\x08\x0B\x0C\x0E-\x1F\x7F]/g, '')
        .replace(/\n/g, ' ')  // Remplacer newline par espace
        .replace(/\r/g, ' ')  // Remplacer carriage return par espace
        .replace(/\t/g, ' ')  // Remplacer tab par espace
        .trim();
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
        <label style="display: flex; align-items: center; padding: 0.5rem; background: #f9f9f9; border-radius: 5px; margin-bottom: 0.5rem; cursor: pointer;">
            <input type="checkbox" class="topping-checkbox" 
                   data-topping='${JSON.stringify(topping).replace(/'/g, "\\'")}' 
                   style="margin-right: 0.75rem; width: 18px; height: 18px;">
            <span style="flex: 1;">${escapeHtml(topping.name)}</span>
            <span style="font-weight: bold; color: var(--primary);">+${topping.price.toFixed(2)}€</span>
        </label>
    `).join('');

    toppingsSection.classList.remove('hidden');
    toppingsSection.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
    console.log('✅ Toppings affichés');
}

// ========== AJOUT AU PANIER ==========
function addToCart(dish, selectedToppings = []) {
    console.log('🛒 Ajout au panier:', dish.name, 'avec', selectedToppings.length, 'toppings');

    cart.push({
        ...dish,
        selectedToppings: selectedToppings,
        itemId: Date.now() + Math.random()
    });

    updateCartDisplay();

    const toppingsSection = document.getElementById('toppings-section');
    if (toppingsSection) {
        toppingsSection.classList.add('hidden');
    }

    showToast('✅ Ajouté au panier: ' + dish.name);
    console.log('✅ Panier mis à jour. Total:', cart.length, 'articles');
}

// ========== MISE À JOUR DU PANIER ==========
function updateCartDisplay() {
    const cartList = document.getElementById('cart-list');
    const totalElement = document.getElementById('total');
    const confirmBtn = document.getElementById('btn-confirm');

    if (!cartList || !totalElement) {
        console.error('❌ Éléments du panier introuvables');
        return;
    }

    if (cart.length === 0) {
        cartList.innerHTML = '<li style="color: #999; text-align: center; padding: 2rem;">Empty card</li>';
        totalElement.textContent = '0.00';
        if (confirmBtn) confirmBtn.disabled = true;
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
                    <div style="flex: 1;">
                        <strong>${escapeHtml(item.name)}</strong>
                        <span style="color: var(--primary); font-weight: bold; margin-left: 0.5rem;">
                            ${itemTotal.toFixed(2)}€
                        </span>
                        ${item.selectedToppings && item.selectedToppings.length > 0
            ? `<br><small style="color: #666;">+ ${item.selectedToppings.map(t => escapeHtml(t.name)).join(', ')}</small>`
            : ''}
                    </div>
                    <button onclick="removeFromCart(${index})" 
                            style="background: var(--error); color: white; border: none; padding: 0.5rem 1rem; border-radius: 5px; cursor: pointer; font-size: 0.9rem;">
                        ❌
                    </button>
                </div>
            </li>
        `;
    }).join('');

    totalElement.textContent = total.toFixed(2);
    if (confirmBtn) confirmBtn.disabled = false;

    console.log('💰 Total du panier:', total.toFixed(2), '€');
}

function removeFromCart(index) {
    console.log('🗑️ Suppression de l\'article', index);
    const removedItem = cart[index];
    cart.splice(index, 1);
    updateCartDisplay();
    showToast('🗑️ Retiré: ' + removedItem.name);
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
        showToast('⚠️ Impossible de charger les créneaux horaires');
    }
}

function displayDeliverySlots(slots) {
    const select = document.getElementById('delivery-slot');

    if (!select) {
        console.error('❌ Element #delivery-slot introuvable');
        return;
    }

    if (!slots || slots.length === 0) {
        select.innerHTML = '<option value="">No available timeslot</option>';
        select.disabled = true;
        return;
    }

    select.innerHTML = '<option value="">-- Choose a timeslot --</option>' +
        slots.map(slot => `
            <option value="${slot.startTime}-${slot.endTime}" 
                    ${slot.availableCapacity === 0 ? 'disabled' : ''}>
                ${slot.startTime} - ${slot.endTime} 
                ${slot.availableCapacity > 0 ? `(${slot.availableCapacity} places)` : '(Complet)'}
            </option>
        `).join('');

    select.disabled = false;
    console.log('✅ Créneaux affichés');
}

// ========== CONFIGURATION DES ÉVÉNEMENTS ==========
function setupEventListeners() {
    console.log('🔧 Configuration des événements...');

    const addWithToppingsBtn = document.getElementById('add-with-toppings');
    if (addWithToppingsBtn) {
        addWithToppingsBtn.addEventListener('click', () => {
            const checkboxes = document.querySelectorAll('.topping-checkbox:checked');
            const selectedToppings = Array.from(checkboxes).map(cb => JSON.parse(cb.dataset.topping));
            addToCart(currentDish, selectedToppings);
        });
    }

    const confirmBtn = document.getElementById('btn-confirm');
    if (confirmBtn) {
        confirmBtn.addEventListener('click', showPaymentModal);
    }

    console.log('✅ Événements configurés');
}

// ========== MODAL DE PAIEMENT ==========
function showPaymentModal() {
    console.log('💳 Affichage du modal de paiement');

    if (cart.length === 0) {
        showToast('⚠️ Votre panier est vide !');
        return;
    }

    const deliverySlot = document.getElementById('delivery-slot')?.value;
    if (!deliverySlot) {
        showToast('⚠️ Veuillez sélectionner un créneau de livraison');
        document.getElementById('delivery-slot')?.focus();
        return;
    }

    const total = cart.reduce((sum, item) => {
        const toppingsPrice = item.selectedToppings?.reduce((s, t) => s + t.price, 0) || 0;
        return sum + item.price + toppingsPrice;
    }, 0);

    const modal = document.createElement('div');
    modal.id = 'payment-modal';
    modal.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0,0,0,0.5);
        display: flex;
        justify-content: center;
        align-items: center;
        z-index: 1000;
    `;

    modal.innerHTML = `
        <div style="background: white; padding: 2rem; border-radius: 12px; max-width: 500px; width: 90%; max-height: 90vh; overflow-y: auto;">
            <h2 style="margin-bottom: 1rem; color: var(--primary);">💳 Paiement</h2>
            
            <div style="background: #f9f9f9; padding: 1rem; border-radius: 8px; margin-bottom: 1.5rem;">
                <h3 style="margin: 0 0 0.5rem 0; font-size: 1rem;">Récapitulatif</h3>
                <p style="margin: 0; font-size: 1.5rem; font-weight: bold; color: var(--primary);">
                    Total: ${total.toFixed(2)}€
                </p>
            </div>
            
            <div style="margin-bottom: 1.5rem;">
                <label style="display: block; margin-bottom: 0.5rem; font-weight: 600;">
                    Méthode de paiement:
                </label>
                <select id="payment-method" style="width: 100%; padding: 0.75rem; border: 2px solid #ddd; border-radius: 8px; font-size: 1rem;">
                    <option value="">-- Sélectionner --</option>
                    <option value="INTERNAL">💰 Compte étudiant (solde actuel)</option>
                    <option value="EXTERNAL">💳 Carte bancaire</option>
                </select>
            </div>
            
            <div id="card-details" style="display: none; margin-bottom: 1.5rem; padding: 1rem; background: #f0f8ff; border-radius: 8px;">
                <h4 style="margin: 0 0 1rem 0;">Informations de carte</h4>
                
                <label style="display: block; margin-bottom: 0.5rem; font-weight: 600;">
                    Numéro de carte:
                </label>
                <input type="text" id="card-number" placeholder="1234 5634 5666 6" maxlength="19"
                       style="width: 100%; padding: 0.75rem; border: 2px solid #ddd; border-radius: 8px; margin-bottom: 1rem; font-size: 1rem;">
                
                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                    <div>
                        <label style="display: block; margin-bottom: 0.5rem; font-weight: 600;">
                            Expiration (MM/AA):
                        </label>
                        <input type="text" id="card-expiry" placeholder="12/09" maxlength="5"
                               style="width: 100%; padding: 0.75rem; border: 2px solid #ddd; border-radius: 8px; font-size: 1rem;">
                    </div>
                    <div>
                        <label style="display: block; margin-bottom: 0.5rem; font-weight: 600;">
                            CVV:
                        </label>
                        <input type="text" id="card-cvv" placeholder="133" maxlength="3"
                               style="width: 100%; padding: 0.75rem; border: 2px solid #ddd; border-radius: 8px; font-size: 1rem;">
                    </div>
                </div>
            </div>
            
            <div id="payment-error" style="display: none; padding: 1rem; background: #fee; border: 2px solid #f44; border-radius: 8px; margin-bottom: 1rem; color: #c00;">
            </div>
            
            <div style="display: flex; gap: 1rem;">
                <button onclick="closePaymentModal()" 
                        style="flex: 1; padding: 1rem; background: #ccc; color: #333; border: none; border-radius: 8px; font-size: 1rem; cursor: pointer;">
                    Annuler
                </button>
                <button id="process-payment-btn" onclick="processPayment()" 
                        style="flex: 2; padding: 1rem; background: var(--success); color: white; border: none; border-radius: 8px; font-size: 1rem; font-weight: bold; cursor: pointer;">
                    Payer ${total.toFixed(2)}€
                </button>
            </div>
        </div>
    `;

    document.body.appendChild(modal);

    const paymentMethodSelect = document.getElementById('payment-method');
    const cardDetails = document.getElementById('card-details');

    paymentMethodSelect?.addEventListener('change', (e) => {
        if (e.target.value === 'EXTERNAL') {
            cardDetails.style.display = 'block';
        } else {
            cardDetails.style.display = 'none';
        }
    });

    document.getElementById('card-number')?.addEventListener('input', (e) => {
        let value = e.target.value.replace(/\s/g, '');
        let formattedValue = value.match(/.{1,4}/g)?.join(' ') || value;
        e.target.value = formattedValue;
    });

    document.getElementById('card-expiry')?.addEventListener('input', (e) => {
        let value = e.target.value.replace(/\D/g, '');
        if (value.length >= 2) {
            value = value.slice(0, 2) + '/' + value.slice(2, 4);
        }
        e.target.value = value;
    });
}

function closePaymentModal() {
    const modal = document.getElementById('payment-modal');
    if (modal) {
        modal.remove();
    }
}

// ========== TRAITEMENT DU PAIEMENT ==========
async function processPayment() {
    console.log('💳 Traitement du paiement...');

    const paymentMethod = document.getElementById('payment-method')?.value;
    const errorDiv = document.getElementById('payment-error');
    const processBtn = document.getElementById('process-payment-btn');

    if (errorDiv) {
        errorDiv.style.display = 'none';
        errorDiv.textContent = '';
    }

    if (!paymentMethod) {
        showPaymentError('Veuillez sélectionner une méthode de paiement');
        return;
    }

    if (paymentMethod === 'EXTERNAL') {
        const cardNumber = document.getElementById('card-number')?.value.replace(/\s/g, '');
        const cardExpiry = document.getElementById('card-expiry')?.value;
        const cardCVV = document.getElementById('card-cvv')?.value;

        if (!cardNumber || cardNumber.length < 13) {
            showPaymentError('Numéro de carte invalide (minimum 13 chiffres)');
            return;
        }

        if (!cardExpiry || !/^\d{2}\/\d{2}$/.test(cardExpiry)) {
            showPaymentError('Date d\'expiration invalide (format: MM/AA)');
            return;
        }

        if (!cardCVV || cardCVV.length !== 3) {
            showPaymentError('CVV invalide (3 chiffres)');
            return;
        }
    }

    if (processBtn) {
        processBtn.disabled = true;
        processBtn.textContent = '⏳ Traitement en cours...';
    }

    try {
        const deliverySlot = document.getElementById('delivery-slot')?.value;

        // ✅ FIX : Nettoyer les données avant envoi
        const orderData = {
            restaurantId: selectedRestaurant.id,
            dishes: cart.map(item => ({
                name: cleanString(item.name),
                price: item.price + (item.selectedToppings?.reduce((s, t) => s + t.price, 0) || 0)
            })),
            deliveryLocation: "Campus Sophia",
            timeSlot: deliverySlot
        };

        console.log('📤 Création de la commande:', orderData);
        const order = await API.createOrder(orderData);
        currentOrderId = order.id;

        console.log('✅ Commande créée avec ID:', currentOrderId);

        console.log('📤 Traitement du paiement...');
        const paymentData = {
            orderId: currentOrderId,
            paymentMethod: paymentMethod
        };

        const paymentResult = await API.processPayment(paymentData);
        console.log('✅ Paiement traité:', paymentResult);

        if (paymentResult.status === 'VALIDATED') {
            sessionStorage.setItem('lastOrder', JSON.stringify({
                orderId: currentOrderId,
                total: orderData.dishes.reduce((sum, d) => sum + d.price, 0),
                items: cart.length,
                restaurant: selectedRestaurant.name,
                timeSlot: deliverySlot
            }));

            closePaymentModal();
            showToast('✅ Paiement réussi ! Redirection...');

            setTimeout(() => {
                window.location.href = 'confirmation.html';
            }, 1000);
        } else {
            throw new Error('Paiement refusé: ' + paymentResult.message);
        }

    } catch (error) {
        console.error('❌ Erreur paiement:', error);
        showPaymentError('Erreur: ' + error.message);

        if (processBtn) {
            processBtn.disabled = false;
            processBtn.textContent = 'Réessayer';
        }
    }
}

function showPaymentError(message) {
    const errorDiv = document.getElementById('payment-error');
    if (errorDiv) {
        errorDiv.textContent = '❌ ' + message;
        errorDiv.style.display = 'block';
    }
}

// ========== UTILITAIRES ==========
function showError(title, message) {
    const menuContainer = document.getElementById('menu');
    if (menuContainer) {
        menuContainer.innerHTML = `
            <div style="text-align: center; padding: 2rem; color: var(--error);">
                <p style="font-size: 1.2rem; margin-bottom: 1rem;">❌ ${escapeHtml(title)}</p>
                <p style="margin-bottom: 1rem;">${escapeHtml(message)}</p>
                <a href="index.html" class="btn">← Retour à l'accueil</a>
            </div>
        `;
    }
}

function showToast(message) {
    const toast = document.createElement('div');
    toast.textContent = message;
    toast.style.cssText = `
        position: fixed;
        bottom: 20px;
        right: 20px;
        background: #333;
        color: white;
        padding: 1rem 1.5rem;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.3);
        z-index: 10000;
        animation: slideIn 0.3s ease-out;
    `;

    document.body.appendChild(toast);

    setTimeout(() => {
        toast.style.animation = 'slideOut 0.3s ease-in';
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}

function escapeHtml(str) {
    if (!str) return '';
    const div = document.createElement('div');
    div.textContent = str;
    return div.innerHTML;
}

window.selectDishWithToppings = selectDishWithToppings;
window.addToCart = addToCart;
window.removeFromCart = removeFromCart;
window.closePaymentModal = closePaymentModal;
window.processPayment = processPayment;