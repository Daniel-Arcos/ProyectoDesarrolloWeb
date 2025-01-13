export class CartService {
    constructor() {
        this.CART_KEY = 'shopping_cart';
        this.cart = this.getCart();
        this.initializeListeners();
    }

    initializeListeners() {
        document.addEventListener('DOMContentLoaded', () => {
            this.updateCartUI();
            
            if (window.location.href.includes('cart.html')) {
                this.initializeCartPageListeners();
            }
        });
    }

    initializeCartPageListeners() {
        const cartContainer = document.getElementById('container-products');
        if (!cartContainer) return;

        cartContainer.addEventListener('click', (e) => {
            const target = e.target;
            
            if (target.classList.contains('btn-decrease')) {
                const idProducto = parseInt(target.dataset.productId);
                const currentItem = this.cart.find(item => item.idProducto === idProducto);
                if (currentItem && currentItem.cantidad > 1) {
                    this.updateQuantity(idProducto, currentItem.cantidad - 1);
                }
            }
            
            if (target.classList.contains('btn-increase')) {
                const idProducto = parseInt(target.dataset.productId);
                const currentItem = this.cart.find(item => item.idProducto === idProducto);
                if (currentItem) {
                    this.updateQuantity(idProducto, currentItem.cantidad + 1);
                }
            }
            
            if (target.classList.contains('btn-remove')) {
                const idProducto = parseInt(target.dataset.productId);
                this.removeFromCart(idProducto);
            }
        });
    }

    getCart() {
        const cart = localStorage.getItem(this.CART_KEY);
        return cart ? JSON.parse(cart) : [];
    }

    saveCart() {
        localStorage.setItem(this.CART_KEY, JSON.stringify(this.cart));
        this.updateCartUI();
    }

    addToCart(producto, cantidad = 1) {
        const existingItem = this.cart.find(item => item.idProducto === producto.id);
        
        if (existingItem) {
            if (existingItem.cantidad + cantidad <= producto.inventario) {
                existingItem.cantidad += cantidad;
            } else {
                throw new Error(`Solo hay ${producto.inventario} unidades disponibles`);
            }
        } else {
            if (cantidad <= producto.inventario) {
                this.cart.push({
                    idProducto: producto.id,
                    idTienda: producto.tiendaId,
                    cantidad: cantidad,
                    precioUnitario: producto.precioVenta,
                    nombre: producto.nombre,
                    imagen: producto.imageData,
                    descripcion: producto.descripcion,
                    inventarioDisponible: producto.inventario
                });
            } else {
                throw new Error(`Solo hay ${producto.inventario} unidades disponibles`);
            }
        }

        this.saveCart();
    }

    updateQuantity(idProducto, nuevaCantidad) {
        const item = this.cart.find(item => item.idProducto === idProducto);
        if (item) {
            if (nuevaCantidad <= item.inventarioDisponible && nuevaCantidad > 0) {
                item.cantidad = nuevaCantidad;
                this.saveCart();
            } else {
                alert(`Solo hay ${item.inventarioDisponible} disponibles`);
            }
        }
    }

    removeFromCart(idProducto) {
        this.cart = this.cart.filter(item => item.idProducto !== idProducto);
        this.saveCart();
    }

    getTotal() {
        return this.cart.reduce((total, item) => 
            total + (item.precioUnitario * item.cantidad), 0);
    }

    toDTO() {
        return this.cart.map(item => ({
            idProducto: item.idProducto,
            idTienda: item.idTienda,
            cantidad: item.cantidad,
            precioUnitario: item.precioUnitario
        }));
    }

    updateCartUI() {
        const cartTotalElement = document.querySelector('.cart-total');
        if (cartTotalElement) {
            cartTotalElement.textContent = `$${this.getTotal().toFixed(2)}`;
        }

        if (window.location.href.includes('cart.html')) {
            this.renderCartPage();
        }
    }

    renderCartPage() {
        const cartContainer = document.getElementById('container-products');
        if (!cartContainer) return;

        const totalProductsElement = document.getElementById('cantidadProductos');
        const totalElement = document.getElementById('total')
        const productsCountElement = document.getElementById('contadorProductos')
        cartContainer.innerHTML = '';
        
        if (this.cart.length === 0) {
            cartContainer.innerHTML = `
                <div class="text-center p-5">
                    <h4>Tu carrito está vacío</h4>
                    <p>¡Agrega algunos productos para comenzar!</p>
                </div>
            `;
        } else {
            this.cart.forEach(item => {
                cartContainer.innerHTML += this.renderCartItem(item);
            });
        }

        totalProductsElement.textContent = `${this.cart.length} artículos totales`;
        totalElement.textContent = `$${this.getTotal().toFixed(2)}`;
        productsCountElement.textContent = `Productos (${this.cart.length})`;
    }

    clearCart() {
        this.cart = [];
        this.saveCart(); 
    }

    renderCartItem(item) {
        const imageSrc = item.imagen ? 
            `data:image/jpeg;base64,${item.imagen}` : 
            '../../assets/images/defecto.jpg';

        return `
            <div class="product-item row border-top border-secondary overflow-hidden mb-5">
                <div class="row py-5">
                    <div class="col-4 h-100">
                        <div class="product-image h-100 w-100 overflow-hidden">
                            <img src="${imageSrc}" class="img-fluid object-fit-contain" alt="${item.nombre}">
                        </div>
                    </div>
                    <div class="col-6 h-100">
                        <div class="fs-6 text-uppercase fw-bold">${item.nombre}</div>
                        <div class="product-description mt-2 mb-4 fs-6 fw-light text-body-secondary">
                            ${item.descripcion}
                        </div>
                        <div class="d-flex gap-2">
                            <div class="d-flex align-items-center border border-secondary rounded rounded-pill p-1 gap-3">
                                <button type="button" class="btn btn-light rounded-circle fs-6 fw-bold px-3 btn-decrease"
                                    data-product-id="${item.idProducto}">-</button>
                                <div class="fs-6">${item.cantidad}</div>
                                <button type="button" class="btn btn-light rounded-circle fs-6 fw-bold px-3 btn-increase"
                                    data-product-id="${item.idProducto}">+</button>
                            </div>
                            <button type="button" class="btn btn-light rounded rounded-pill fs-6 fw-medium btn-remove"
                                data-product-id="${item.idProducto}">Eliminar</button>
                        </div>
                    </div>
                    <div class="col-2 fs-6 f-bold h-100 d-flex justify-content-end">
                        $${(item.precioUnitario * item.cantidad).toFixed(2)}
                    </div>
                </div>
            </div>
        `;
    }
}

export const cartService = new CartService();