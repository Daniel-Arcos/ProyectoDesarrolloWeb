import {obtenerDirecciones, obtenerTarjetas} from "../services/ClienteService.js"
import {cartService} from "../services/CartService.js"
import {guardarPedido} from "../services/PedidosService.js"

class CheckoutPage {
    constructor() {
        this.selectedDireccion = null;
        this.selectedTarjeta = null;
        this.direcciones = [];  
        this.tarjetas = [];     
        this.initializeListeners();
        this.loadData();
    }

    async loadData() {
        try {
            this.direcciones = await this.getDirecciones();
            this.renderDirecciones();

            this.tarjetas = await this.getTarjetas();
            this.renderTarjetas();

            this.renderResumen();
        } catch (error) {
            console.error('Error cargando datos:', error);
            alert('Error cargando los datos. Por favor, intenta de nuevo.');
        }
    }

    renderDirecciones() {
        if (!Array.isArray(this.direcciones)) {
            console.error('direcciones no es un array:', this.direcciones);
            return;
        }

        const container = document.querySelector('.direcciones-container');
        container.innerHTML = this.direcciones.map(dir => `
            <div class="card mb-3 ${this.selectedDireccion === dir.id ? 'border-primary' : ''}">
                <div class="card-body">
                    <div class="form-check">
                        <input class="form-check-input" type="radio" name="direccion" 
                            value="${dir.id}" id="dir-${dir.id}" 
                            ${this.selectedDireccion === dir.id ? 'checked' : ''}>
                        <label class="form-check-label w-100" for="dir-${dir.id}">
                            <div class="ms-2">
                                <p class="mb-0">${dir.calle} ${dir.numeroExterior}
                                    ${dir.numeroInterior ? `, Int. ${dir.numeroInterior}` : ''}</p>
                                <p class="mb-0">Col. ${dir.colonia}, CP. ${dir.codigoPostal}</p>
                                <p class="mb-0">${dir.ciudad}</p>
                            </div>
                        </label>
                    </div>
                </div>
            </div>
        `).join('');
    }

    renderTarjetas() {
        if (!Array.isArray(this.tarjetas)) {
            console.error('tarjetas no es un array:', this.tarjetas);
            return;
        }

        const container = document.querySelector('.tarjetas-container');
        container.innerHTML = this.tarjetas.map(tarjeta => `
            <div class="card mb-3 ${this.selectedTarjeta === tarjeta.id ? 'border-primary' : ''}">
                <div class="card-body">
                    <div class="form-check">
                        <input class="form-check-input" type="radio" name="tarjeta" 
                            value="${tarjeta.id}" id="tarjeta-${tarjeta.id}"
                            ${this.selectedTarjeta === tarjeta.id ? 'checked' : ''}>
                        <label class="form-check-label w-100" for="tarjeta-${tarjeta.id}">
                            <div class="ms-2">
                                <p class="mb-0">${tarjeta.emisorTarjeta} terminada en ${tarjeta.numeroTarjeta.slice(-4)}</p>
                                <p class="mb-0">${tarjeta.nombreTitular}</p>
                                <p class="mb-0">Vence: ${tarjeta.mesVencimiento}/${tarjeta.anoVencimiento}</p>
                            </div>
                        </label>
                    </div>
                </div>
            </div>
        `).join('');
    }

    renderResumen() {
        const cart = cartService.getCart();
        const container = document.querySelector('.productos-resumen');
        
        
        container.innerHTML = `
            <div class="mb-4">                
                <div class="productos-lista">
                    ${cart.map(item => `
                        <div class="producto-resumen mb-4 pb-4 border-bottom">
                            <div class="d-flex gap-3">
                                <div class="flex-shrink-0" style="width: 80px;">
                                    <img 
                                        src="${item.imagen ? `data:image/jpeg;base64,${item.imagen}` : '../../assets/images/defecto.jpg'}" 
                                        class="img-fluid rounded"
                                        alt="${item.nombre}"
                                        style="width: 100%; height: 80px; object-fit: cover;"
                                    >
                                </div>
                                <div class="flex-grow-1">
                                    <div class="d-flex justify-content-between align-items-start mb-1">
                                        <h6 class="mb-0 text-uppercase fw-bold">${item.nombre}</h6>
                                        <span class="ms-2 fw-bold">$${(item.precioUnitario * item.cantidad).toFixed(2)}</span>
                                    </div>
                                    <p class="text-muted small mb-2">${item.descripcion}</p>
                                    <div class="text-muted small">
                                        Cantidad: ${item.cantidad}
                                    </div>
                                </div>
                            </div>
                        </div>
                    `).join('')}
                </div>
            </div>
        `;
    
        document.querySelector('.subtotal').textContent = `$${cartService.getTotal().toFixed(2)}`;
        document.querySelector('.total').textContent = `$${cartService.getTotal().toFixed(2)}`;
    }


    initializeListeners() {
        document.querySelector('.direcciones-container').addEventListener('change', (e) => {
            if (e.target.name === 'direccion') {
                this.selectedDireccion = parseInt(e.target.value);
                this.renderDirecciones();
            }
        });

        document.querySelector('.tarjetas-container').addEventListener('change', (e) => {
            if (e.target.name === 'tarjeta') {
                this.selectedTarjeta = parseInt(e.target.value);
                this.renderTarjetas();
            }
        });

        document.getElementById('btn-pagar').addEventListener('click', async () => {
            if (!this.selectedDireccion || !this.selectedTarjeta) {
                alert('Por favor, selecciona una dirección y un método de pago.');
                return;
            }

            const userData = JSON.parse(Cookies.get('userData'));
            const clienteId = userData.id;
            const pedido = {
                idCliente: clienteId,
                idDireccion: this.selectedDireccion,
                idTarjeta: this.selectedTarjeta,
                productos: cartService.toDTO()
            };

            console.log('Enviando pedido:', pedido);
            try {
                await guardarPedido(pedido);
                cartService.clearCart();
            } catch (error) {
                console.log(error)
                alert('Error al enviar el pedido. Por favor, intenta de nuevo.');
            }
        });
    }

    async getDirecciones() {
        try {
            const data = await obtenerDirecciones();
            return data;
        } catch (error) {
            console.error('Error obteniendo direcciones:', error);
            return [];
        }
    }

    async getTarjetas() {
        try {
            const data = await obtenerTarjetas();
            return data;
        } catch (error) {
            console.error('Error obteniendo tarjetas:', error);
            return [];
        }
    }
}

const checkout = new CheckoutPage();

export default CheckoutPage;