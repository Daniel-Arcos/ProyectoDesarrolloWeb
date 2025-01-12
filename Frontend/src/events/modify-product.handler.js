import { actualizarProducto } from '../services/ProductoService.js'; 
import {cerrarSesion} from '../services/AuthenticationService.js'
const salirButton = document.getElementById('logout');
salirButton.onclick = () => cerrarSesion();

document.addEventListener('DOMContentLoaded', function () {
    const producto = JSON.parse(sessionStorage.getItem('productoEditar'));
    if (producto) {
        llenarFormulario(producto);
        sessionStorage.removeItem('productoEditar');
    }

    const descriptionField = document.getElementById('description');
    const charCount = document.getElementById('charCount');
    
    function updateCharCount() {
        charCount.textContent = descriptionField.value.length;
    }
    
    descriptionField.addEventListener('input', updateCharCount);

    const imageInput = document.getElementById('productImage');
    const imagePreview = document.getElementById('imagePreview');
    
    imageInput.addEventListener('change', function() {
        const file = this.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function(e) {
                imagePreview.src = e.target.result;
                imagePreview.classList.remove('d-none');
            }
            reader.readAsDataURL(file);
        } else {
            imagePreview.classList.add('d-none');
        }
    });

    const form = document.getElementById('editProductForm');
    const submitButton = form.querySelector('button[type="submit"]');
    const originalText = submitButton.innerHTML;
    form.addEventListener('submit', async function (event) {
        event.preventDefault();
        
        if (!form.checkValidity()) {
            event.stopPropagation();
            form.classList.add('was-validated');
            return;
        }

        
        try {
            submitButton.disabled = true;
            submitButton.innerHTML = `
                <span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                Guardando...
            `;

            const tiendaData = JSON.parse(Cookies.get('tiendaData'));
            const tiendaId = tiendaData.id;
            const formData = {
                id: document.getElementById('productId').value,
                categoria: document.getElementsByClassName("form-select")[0].value,
                nombre: document.getElementById('productName').value,
                descripcion: document.getElementById('description').value,
                precioVenta: parseFloat(document.getElementById('price').value),
                inventario: parseFloat(document.getElementById('inventory').value),
                tiendaId: tiendaId,
            };

            const imageFile = document.getElementById('productImage').files[0];
            if (imageFile) {
                const base64Image = await convertImageToBase64(imageFile);
                formData.imageData = base64Image;
            }

           
            const data = await actualizarProducto(formData);
            alert('Producto actualizado exitosamente');
            
            window.location.href = '../vendedor/dashboard.html';

        } catch (error) {
            console.error('Error updating product:', error);
            alert('Error al actualizar el producto');
        } finally {
            submitButton.disabled = false;
            submitButton.innerHTML = originalText;
        }

    });
});

function llenarFormulario(producto) {
    console.log(producto);
    document.getElementById('productId').value = producto.id;
    document.getElementById('productName').value = producto.nombre;
    document.getElementsByClassName("form-select")[0].value = producto.categoria || '';
    document.getElementById('description').value = producto.descripcion;
    document.getElementById('price').value = producto.precioVenta;
    document.getElementById('inventory').value = producto.inventario;

    if (producto.imageData) {
        document.getElementById('currentImage').src = `data:image/jpeg;base64,${producto.imageData}`;
    } else {
        document.getElementById('currentImage').src = '../../assets/images/defecto.jpg';
    }

    const charCount = document.getElementById('charCount');
    if (charCount) {
        charCount.textContent = producto.descripcion.length;
    }
}