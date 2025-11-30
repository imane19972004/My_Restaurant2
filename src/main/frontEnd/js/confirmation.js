// ============================================
// confirmation.js - Page de confirmation
// ============================================

document.addEventListener('DOMContentLoaded', () => {
    console.log('📄 Page confirmation.js chargée');
    
    const summary = document.getElementById('summary');
    
    if (!summary) {
        console.error('❌ Element #summary introuvable');
        return;
    }
    
    summary.innerHTML = `
        <div class="success-message" style="text-align: center; padding: 3rem; background: white; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.1);">
            <h2 style="color: var(--success); font-size: 2rem; margin-bottom: 1rem;">✅ Commande Confirmée !</h2>
            <p style="font-size: 1.1rem; color: #666; margin-bottom: 0.5rem;">Votre commande a été enregistrée avec succès.</p>
            <p style="font-size: 1rem; color: #999; margin-bottom: 2rem;">Vous recevrez une notification lorsqu'elle sera prête.</p>
            <a href="restaurant.html" class="btn" style="margin-right: 1rem;">🍽️ Commander à nouveau</a>
            <a href="index.html" class="btn">🏠 Retour à l'accueil</a>
        </div>
    `;
    
    console.log('✅ Page de confirmation affichée');
});