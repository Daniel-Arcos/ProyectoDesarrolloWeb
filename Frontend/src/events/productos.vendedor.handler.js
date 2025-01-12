import { obtenerTiendaVendedor } from '../services/VendedorService.js'; // Importamos la función de peticiones
import { obtenerProductosTienda } from '../services/TiendaService.js'
import { eliminarProducto } from '../services/ProductoService.js'
import {cerrarSesion} from '../services/AuthenticationService.js'
const salirButton = document.getElementById('logout');
salirButton.onclick = () => cerrarSesion();
const agregarProductoButton = document.getElementById("agregarProducto")

document.addEventListener('DOMContentLoaded', async function() {
    try {
        const userData = JSON.parse(Cookies.get('userData'));
        if (userData && userData.nombre) {
            document.getElementById('user-name').textContent = userData.nombre;
        }
        const vendedorId = userData.id;
        const tienda = await obtenerTiendaVendedor(vendedorId);
        const productos = await obtenerProductosTienda(tienda.id);
        mostrarProductos(productos);
    } catch (error) {
        console.error('Error al obtener productos:', error);
    }
});

agregarProductoButton.onclick = () => {
    window.location.href = '../vendedor/register-product.html';
};

function mostrarProductos(productos) {
    const productosContainer = document.querySelector('.row');
    productosContainer.innerHTML = '';

    productos.forEach(producto => {
        const imageSrc = producto.imageData ? 
            `data:image/jpeg;base64,${producto.imageData}` : 
            '../../assets/images/defecto.jpg';

        const productoHTML = `
            <div class="col-12 col-md-6 col-lg-4">
                <div class="card product-card">
                    <div class="card-body">
                        <img src="${imageSrc}" 
                             alt="${producto.nombre}" 
                             class="product-image">
                        <h5 class="card-title">${producto.nombre}</h5>
                        <div class="mb-2">
                            <span class="badge bg-secondary">${producto.categoria || 'Sin categoría'}</span>
                        </div>
                        <p class="card-text">
                            <small class="text-muted">${producto.descripcion}</small>
                        </p>
                        <div class="d-flex justify-content-between align-items-center mb-2">
                            <span class="fw-bold">Precio: $<span class="precio">${producto.precioVenta}</span></span>
                            <span class="text-muted">Inventario: <span class="inventario">${producto.inventario}</span></span>
                        </div>
                        <div class="product-actions">
                            <div class="d-flex justify-content-end">
                                <button class="btn btn-outline-dark fw-semibold mx-2 edit-button" data-producto='${JSON.stringify(producto)}'>
                                    <i class="bi bi-pencil m-2"></i>
                                    Editar
                                </button>
                                <button class="btn btn-outline-dark fw-semibold delete-button" data-id="${producto.id}">
                                    <i class="bi bi-trash"></i>
                                    Eliminar
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        `;
        productosContainer.innerHTML += productoHTML;
    });

    document.querySelectorAll('.edit-button').forEach(button => {
        button.addEventListener('click', function() {
            const producto = JSON.parse(this.dataset.producto);
            editarProducto(producto);
        });
    });

    document.querySelectorAll('.delete-button').forEach(button => {
        button.addEventListener('click', function() {
            const id = this.dataset.id;
            eliminarProductoFuncion(id);
        });
    });
}

function editarProducto(producto) {
    sessionStorage.setItem('productoEditar', JSON.stringify(producto));
    window.location.href = '../vendedor/modify-product.html';
}

async function eliminarProductoFuncion (id) {
    if (confirm('¿Estás seguro de que deseas eliminar este producto?')) {
        try {
            await eliminarProducto(id);
            const userData = JSON.parse(Cookies.get('userData'));
            const vendedorId = userData.id;
            const tienda = await obtenerTiendaVendedor(vendedorId);
            const productos = await obtenerProductosTienda(tienda.id);
            mostrarProductos(productos);
        } catch (error) {
            console.error('Error al eliminar el producto:', error);
            alert('Hubo un error al eliminar el producto');
        }
    }
}