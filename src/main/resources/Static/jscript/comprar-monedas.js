
document.addEventListener('DOMContentLoaded', () => {
    const coinCards = document.querySelectorAll('.coin-card');
    const paymentMethodSelection = document.getElementById('paymentMethodSelection');
    const paymentFormsContainer = document.getElementById('paymentFormsContainer');
    const paymentMethodCards = document.querySelectorAll('.payment-method-card');
    const buyCoinsButton = document.getElementById('buyCoinsButton');
    const purchaseAmountSpan = document.getElementById('purchaseAmount');
    const paypalCoinsValueSpan = document.getElementById('paypalCoinsValue');
    const transferAmountSpan = document.getElementById('transferAmount');

    let selectedCoins = 0;
    let selectedPrice = 0;
    let selectedCurrency = '';
    let selectedMethod = '';

    // Función para resetear la selección de tarjetas de canje
    const resetCoinCardSelection = () => {
        coinCards.forEach(card => card.classList.remove('selected'));
        paymentMethodSelection.classList.add('hidden'); // Ocultar métodos de pago
        paymentFormsContainer.classList.add('hidden'); // Ocultar formularios
        buyCoinsButton.classList.add('hidden'); // Ocultar botón de compra
        selectedCoins = 0;
        selectedPrice = 0;
        selectedCurrency = '';
        resetPaymentMethodSelection(); // Resetear selección de método de pago
    };

    // Función para resetear la selección de métodos de pago y formularios
    const resetPaymentMethodSelection = () => {
        paymentMethodCards.forEach(card => card.classList.remove('selected'));
        document.querySelectorAll('.payment-form').forEach(form => form.classList.add('hidden'));
        buyCoinsButton.classList.add('hidden');
        selectedMethod = '';
    };

    // Manejar clic en tarjetas de canje
    coinCards.forEach(card => {
        card.addEventListener('click', () => {
            // Si la tarjeta ya está seleccionada, la deseleccionamos y reseteamos
            if (card.classList.contains('selected')) {
                resetCoinCardSelection();
            } else {
                resetCoinCardSelection(); // Primero, deseleccionar todas
                card.classList.add('selected'); // Luego seleccionar esta

                selectedCoins = parseInt(card.dataset.coins);
                selectedPrice = parseFloat(card.dataset.price);
                selectedCurrency = card.dataset.currency;

                // Actualizar texto del botón de compra y spans específicos
                purchaseAmountSpan.textContent = `${selectedCoins} ${selectedCurrency === 'USD' ? '$' + selectedPrice.toFixed(2) : selectedPrice.toFixed(2) + ' ' + selectedCurrency}`;
                paypalCoinsValueSpan.textContent = `${selectedCoins} (${selectedCurrency === 'USD' ? '$' + selectedPrice.toFixed(2) : selectedPrice.toFixed(2) + ' ' + selectedCurrency})`;
                transferAmountSpan.textContent = `${selectedCurrency === 'USD' ? '$' + selectedPrice.toFixed(2) : selectedPrice.toFixed(2) + ' ' + selectedCurrency}`;


                paymentMethodSelection.classList.remove('hidden'); // Mostrar sección de métodos de pago
                // Ocultar formularios y botón de compra si había alguno visible de una selección previa de otra tarjeta
                paymentFormsContainer.classList.add('hidden');
                buyCoinsButton.classList.add('hidden');
                resetPaymentMethodSelection(); // Asegurarse de que no haya un método de pago seleccionado previamente
            }
        });
    });

    // Manejar clic en tarjetas de método de pago
    paymentMethodCards.forEach(card => {
        card.addEventListener('click', () => {
            resetPaymentMethodSelection(); // Primero, deseleccionar todos los métodos y ocultar formularios
            card.classList.add('selected'); // Luego seleccionar este

            selectedMethod = card.dataset.method;

            // Mostrar el formulario correspondiente
            const targetForm = document.getElementById(`form${selectedMethod.charAt(0).toUpperCase() + selectedMethod.slice(1)}`);
            if (targetForm) {
                targetForm.classList.remove('hidden');
            }

            paymentFormsContainer.classList.remove('hidden'); // Mostrar el contenedor de formularios
            buyCoinsButton.classList.remove('hidden'); // Mostrar el botón de compra
        });
    });

    // Manejar clic en el botón de compra (aquí iría la lógica real de procesamiento de pago)
    buyCoinsButton.addEventListener('click', () => {
        if (selectedCoins > 0 && selectedMethod !== '') {
            alert(`Simulando compra: Intentando comprar ${selectedCoins} monedas por ${selectedPrice.toFixed(2)} ${selectedCurrency} usando ${selectedMethod}.`);
            // Aquí integrarías la lógica real para enviar datos al servidor, procesar el pago, etc.
            // Por ejemplo:
            // if (selectedMethod === 'tarjeta') {
            //     const cardNumber = document.getElementById('cardNumber').value;
            //     // Lógica para enviar datos de tarjeta
            // } else if (selectedMethod === 'paypal') {
            //     // Redirigir a PayPal o usar API
            // } else if (selectedMethod === 'transferencia') {
            //     // Lógica para manejar el comprobante
            // }
            alert('¡Compra simulada exitosa! Redirigiendo o mostrando confirmación.');
            // Opcional: limpiar selección después de una compra simulada exitosa
            resetCoinCardSelection();
        } else {
            alert('Por favor, selecciona una cantidad de monedas y un método de pago.');
        }
    });
});