import { obtenerTiendaVendedor } from '../services/VendedorService.js'; // Importamos la función de peticiones

const agregarProductoButton = document.getElementById("agregarProducto")

document.addEventListener('DOMContentLoaded', async function() {
    try {
        const userData = JSON.parse(Cookies.get('userData'));
        const vendedorId = userData.id;
        const tienda = await obtenerTiendaVendedor(vendedorId);
        console.log('TIenda:', tienda);
        mostrarProductos(tienda);
    } catch (error) {
        console.error('Error al obtener productos:', error);
    }
});

agregarProductoButton.onclick = () => {
    window.location.href = '../vendedor/register-product.html';
};

function mostrarProductos(tienda) {
    const productosContainer = document.querySelector('.row');
    const productos = tienda.productos;
    
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
                                <button class="btn btn-outline-dark fw-semibold mx-2" onclick="editarProducto(${JSON.stringify(producto).replace(/"/g, '&quot;')})">
                                    <i class="bi bi-pencil m-2"></i>
                                    Editar
                                </button>
                                <button class="btn btn-outline-dark fw-semibold" onclick="eliminarProducto(${producto.id})">
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
}

function editarProducto(producto) {
    sessionStorage.setItem('productoEditar', JSON.stringify(producto));
    //window.location.href = '/modify-product';
}