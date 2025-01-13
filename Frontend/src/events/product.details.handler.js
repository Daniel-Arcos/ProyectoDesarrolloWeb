import { cartService } from '../services/CartService.js';

document.addEventListener('DOMContentLoaded', () => {
    const selectedProduct = JSON.parse(sessionStorage.getItem('selectedProduct'));
    
    if (!selectedProduct) {
        window.location.href = 'index.html';
        return;
    }

    document.querySelector('.breadcrumb-item.active').textContent = selectedProduct.nombre;
    document.querySelector('h3').textContent = selectedProduct.nombre;
    document.querySelector('h2').textContent = `$${selectedProduct.precioVenta}`;
    document.querySelector('#productDescription').textContent = selectedProduct.descripcion;
    
    const productImage = document.querySelector('#product-image');
    if (selectedProduct.imageData) {
        productImage.src = `data:image/jpeg;base64,${selectedProduct.imageData}`;
    } else {
        productImage.src = '../../assets/images/defecto.jpg';
    }

    const addToCartBtn = document.querySelector('.btn-dark');
    addToCartBtn.addEventListener('click', () => {
        try {
            cartService.addToCart(selectedProduct);
            alert('Producto agregado al carrito');
        } catch (error) {
            alert(error.message);
        }
    });
});