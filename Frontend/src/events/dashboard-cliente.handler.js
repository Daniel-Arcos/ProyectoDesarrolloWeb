import { obtenerProductos } from "../services/ProductoService.js";
import { cartService } from "../services/CartService.js";

let productos = [];
let productosFiltrados = [];
let categoriaSeleccionada = "Categorías";
let busquedaActual = "";

document.addEventListener("DOMContentLoaded", () => {
  const userData = JSON.parse(Cookies.get("userData"));
  if (userData && userData.nombre) {
    document.getElementById("user-name").textContent = userData.nombre;
  }

  initializeFilters();
});

document.addEventListener("DOMContentLoaded", async function () {
  try {
    const productosObtenidos = await obtenerProductos();
    productos = productosObtenidos;
    productosFiltrados = productos;
    console.log("Productos obtenidos:", productos);
    mostrarProductos(productosFiltrados);
  } catch (error) {
    console.error("Error al obtener productos:", error);
  }
});

function initializeFilters() {
  const categorySelect = document.querySelector(".form-select");
  categorySelect.addEventListener("change", function () {
    categoriaSeleccionada = this.value;
    aplicarFiltros();
  });

  const searchInput = document.querySelector(".form-control");
  const searchForm = document.getElementById("search-form");

  searchForm.addEventListener("submit", function (e) {
    e.preventDefault();
  });

  searchInput.addEventListener("input", function () {
    busquedaActual = this.value.trim().toLowerCase();
    aplicarFiltros();
  });
}

function aplicarFiltros() {
  let resultados = productos;

  if (categoriaSeleccionada !== "Categorías") {
    resultados = resultados.filter(
      (producto) =>
        producto.categoria &&
        producto.categoria.toLowerCase() === categoriaSeleccionada.toLowerCase()
    );
  }

  if (busquedaActual) {
    resultados = resultados.filter(
      (producto) =>
        producto.nombre.toLowerCase().includes(busquedaActual) ||
        producto.descripcion.toLowerCase().includes(busquedaActual)
    );
  }

  productosFiltrados = resultados;
  mostrarProductos(productosFiltrados);

  const contenedor = document.querySelector(".contenedor");
  if (productosFiltrados.length === 0) {
    contenedor.innerHTML = `
            <div class="col-12 text-center">
                <h3 class="text-muted">No se encontraron productos</h3>
                <p class="text-muted">Intenta con otros términos de búsqueda o categoría</p>
            </div>
        `;
  }
}

function mostrarProductos(productos) {
  const contenedor = document.querySelector(".contenedor");
  contenedor.innerHTML = "";
  const fragment = document.createDocumentFragment();

  productos.forEach((producto) => {
    const productoDiv = document.createElement("div");
    productoDiv.className = "col";

    const imageSrc = producto.imageData
      ? `data:image/jpeg;base64,${producto.imageData}`
      : "../../assets/images/defecto.jpg";

    productoDiv.innerHTML = `
            <div class="card border-0 h-100">
                <div class="product-card-img-container" style="cursor: pointer;">
                    <img src="${imageSrc}" class="card-img-top" alt="${
      producto.nombre
    }">
                </div>
                <div class="card-body px-0">
                    <div class="d-flex justify-content-between align-items-start mb-2">
                        <h5 class="card-title mb-0">${producto.nombre}</h5>
                    </div>
                    <p class="card-text text-muted mb-2">${
                      producto.descripcion
                    }</p>
                    <div class="mt-auto">
                        <div class="d-flex justify-content-between align-items-center">
                            <span class="fs-5 fw-bold">$${
                              producto.precioVenta
                            }</span>
                            <button class="btn btn-primary rounded-circle add-to-cart-btn" 
                                data-product='${JSON.stringify(producto)}'>
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" class="bi bi-cart-plus" viewBox="0 0 16 16">
                                    <path d="M9 5.5a.5.5 0 0 0-1 0V7H6.5a.5.5 0 0 0 0 1H8v1.5a.5.5 0 0 0 1 0V8h1.5a.5.5 0 0 0 0-1H9V5.5z"/>
                                    <path d="M.5 1a.5.5 0 0 0 0 1h1.11l.401 1.607 1.498 7.985A.5.5 0 0 0 4 12h1a2 2 0 1 0 0 4 2 2 0 0 0 0-4h7a2 2 0 1 0 0 4 2 2 0 0 0 0-4h1a.5.5 0 0 0 .491-.408l1.5-8A.5.5 0 0 0 14.5 3H2.89l-.405-1.621A.5.5 0 0 0 2 1H.5zm3.915 10L3.102 4h10.796l-1.313 7h-8.17zM6 14a1 1 0 1 1-2 0 1 1 0 0 1 2 0zm7 0a1 1 0 1 1-2 0 1 1 0 0 1 2 0z"/>
                                </svg>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        `;

    const productImage = productoDiv.querySelector(
      ".product-card-img-container"
    );
    productImage.addEventListener("click", () => {
      sessionStorage.setItem("selectedProduct", JSON.stringify(producto));
      window.location.href = "product-details.html";
    });

    const addToCartBtn = productoDiv.querySelector(".add-to-cart-btn");
    addToCartBtn.addEventListener("click", function (e) {
      e.stopPropagation();
      try {
        const producto = JSON.parse(this.dataset.product);
        cartService.addToCart(producto);
        alert("Producto agregado al carrito");
      } catch (error) {
        alert(error.message);
      }
    });

    fragment.appendChild(productoDiv);
  });

  contenedor.appendChild(fragment);
}
