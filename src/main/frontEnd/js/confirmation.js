// ============================================
// confirmation.js - Page de confirmation améliorée
// ============================================

document.addEventListener('DOMContentLoaded', () => {
    console.log('📄 Page confirmation.js chargée');

    const summary = document.getElementById('summary');

    if (!summary) {
        console.error('❌ Element #summary introuvable');
        return;
    }

    // Récupérer les informations de la commande
    const orderDataStr = sessionStorage.getItem('lastOrder');

    if (!orderDataStr) {
        console.warn('⚠️ Aucune commande trouvée');
        summary.innerHTML = `
            <div class="message-box" style="text-align: center; padding: 3rem; background: white; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.1);">
                <h2 style="color: #f44; font-size: 2rem; margin-bottom: 1rem;">⚠️ Aucune commande trouvée</h2>
                <p style="font-size: 1.1rem; color: #666; margin-bottom: 2rem;">
                    Il semble qu'aucune commande n'ait été enregistrée.
                </p>
                <a href="index.html" class="btn" style="display: inline-block; padding: 1rem 2rem; background: var(--primary); color: white; text-decoration: none; border-radius: 8px; font-weight: bold;">
                    🏠 Retour à l'accueil
                </a>
            </div>
        `;
        return;
    }

    const orderData = JSON.parse(orderDataStr);

    // Afficher la confirmation
    summary.innerHTML = `
        <div class="success-message" style="background: white; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); overflow: hidden;">
            
            <!-- Header de succès -->
            <div style="background: linear-gradient(135deg, #4caf50, #45a049); color: white; padding: 2rem; text-align: center;">
                <div style="font-size: 4rem; margin-bottom: 0.5rem;">✅</div>
                <h2 style="margin: 0; font-size: 2rem;">Commande Confirmée !</h2>
                <p style="margin: 0.5rem 0 0 0; opacity: 0.9;">Numéro de commande: #${orderData.orderId}</p>
            </div>
            
            <!-- Détails de la commande -->
            <div style="padding: 2rem;">
                
                <!-- Informations principales -->
                <div style="background: #f9f9f9; padding: 1.5rem; border-radius: 8px; margin-bottom: 1.5rem;">
                    <h3 style="margin: 0 0 1rem 0; color: var(--secondary); font-size: 1.2rem;">
                        📋 Détails de votre commande
                    </h3>
                    
                    <div style="display: grid; gap: 0.75rem;">
                        <div style="display: flex; justify-content: space-between;">
                            <span style="color: #666;">Restaurant:</span>
                            <strong>${escapeHtml(orderData.restaurant)}</strong>
                        </div>
                        
                        <div style="display: flex; justify-content: space-between;">
                            <span style="color: #666;">Nombre d'articles:</span>
                            <strong>${orderData.items} article${orderData.items > 1 ? 's' : ''}</strong>
                        </div>
                        
                        ${orderData.timeSlot ? `
                        <div style="display: flex; justify-content: space-between;">
                            <span style="color: #666;">Créneau de livraison:</span>
                            <strong>🕐 ${orderData.timeSlot}</strong>
                        </div>
                        ` : ''}
                        
                        <div style="display: flex; justify-content: space-between; padding-top: 0.75rem; border-top: 2px solid #ddd; margin-top: 0.5rem;">
                            <span style="color: #666; font-size: 1.1rem;">Total payé:</span>
                            <strong style="color: var(--success); font-size: 1.3rem;">
                                ${orderData.total.toFixed(2)}€
                            </strong>
                        </div>
                    </div>
                </div>
                
                <!-- Prochaines étapes -->
                <div style="background: #e3f2fd; padding: 1.5rem; border-radius: 8px; border-left: 4px solid #2196f3; margin-bottom: 1.5rem;">
                    <h3 style="margin: 0 0 1rem 0; color: #1976d2; font-size: 1.1rem;">
                        📢 Que se passe-t-il maintenant ?
                    </h3>
                    
                    <ol style="margin: 0; padding-left: 1.5rem; color: #333;">
                        <li style="margin-bottom: 0.5rem;">Votre commande est transmise au restaurant</li>
                        <li style="margin-bottom: 0.5rem;">Le restaurant prépare vos plats</li>
                        <li style="margin-bottom: 0.5rem;">Vous recevrez une notification lorsqu'elle sera prête</li>
                        <li style="margin-bottom: 0;">Récupérez votre commande au créneau sélectionné</li>
                    </ol>
                </div>
                
                <!-- Statut de la commande -->
                <div style="text-align: center; padding: 1.5rem; background: #fff3e0; border-radius: 8px; margin-bottom: 1.5rem;">
                    <div style="font-size: 2rem; margin-bottom: 0.5rem;">⏳</div>
                    <p style="margin: 0; color: #f57c00; font-weight: bold; font-size: 1.1rem;">
                        Statut: En préparation
                    </p>
                    <p style="margin: 0.5rem 0 0 0; color: #666; font-size: 0.9rem;">
                        Vous serez notifié(e) lorsque votre commande sera prête
                    </p>
                </div>
                
                <!-- Boutons d'action -->
                <div style="display: grid; gap: 1rem; margin-top: 2rem;">
                    <a href="index.html" class="btn" style="display: block; text-align: center; padding: 1rem; background: var(--primary); color: white; text-decoration: none; border-radius: 8px; font-weight: bold; font-size: 1rem;">
                        🏠 Retour à l'accueil
                    </a>
                    <a href="restaurant.html" class="btn" style="display: block; text-align: center; padding: 1rem; background: var(--success); color: white; text-decoration: none; border-radius: 8px; font-weight: bold; font-size: 1rem;">
                        🍽️ Commander à nouveau
                    </a>
                </div>
                
                <!-- Note de bas de page -->
                <div style="margin-top: 2rem; padding: 1rem; background: #f5f5f5; border-radius: 8px; text-align: center;">
                    <p style="margin: 0; color: #666; font-size: 0.9rem;">
                        💡 <strong>Conseil:</strong> Conservez ce numéro de commande (#${orderData.orderId}) pour toute question
                    </p>
                </div>
            </div>
        </div>
    `;

    // Nettoyer le sessionStorage après affichage
    // sessionStorage.removeItem('lastOrder'); // Décommentez si vous voulez nettoyer

    console.log('✅ Page de confirmation affichée pour la commande #' + orderData.orderId);
});

function escapeHtml(str) {
    if (!str) return '';
    const div = document.createElement('div');
    div.textContent = str;
    return div.innerHTML;
}