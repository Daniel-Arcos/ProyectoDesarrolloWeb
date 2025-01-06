import { guardarProducto } from '../services/ProductoService.js'; 

document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('productForm');
    
    form.addEventListener('submit', async function (event) {
        event.preventDefault();
        
        if (!form.checkValidity()) {
            event.stopPropagation();
            form.classList.add('was-validated');
            return;
        }

        const tiendaData = JSON.parse(Cookies.get('tiendaData'));
        const tiendaId = tiendaData.id;
        const formData = {
            categoria: document.getElementsByClassName("form-select")[0].value,
            nombre: document.getElementById('productName').value,
            descripcion: document.getElementById('description').value,
            precioVenta: parseFloat(document.getElementById('price').value),
            inventario: parseFloat(document.getElementById('inventory').value),
            tiendaId: tiendaId,
        };

        // Convertir la imagen a Base64
        const imageFile = document.getElementById('productImage').files[0];
        if (imageFile) {
            const base64Image = await convertImageToBase64(imageFile);
            formData.imageData = base64Image;
        }

        console.log('Datos del producto:', formData);
        
        try {
            const data = await guardarProducto(formData);
            alert('Producto registrado exitosamente');
            form.reset();
            document.getElementById('imagePreview').classList.add('d-none');
        } catch (error) {
            console.log("ERROR DE REGISTRO DE PRODUCTO", error)
            alert('Error al registrar el producto')
        }
    });

    function convertImageToBase64(file) {
        return new Promise((resolve, reject) => {
            const reader = new FileReader();
            reader.onload = () => resolve(reader.result.split(',')[1]);
            reader.onerror = error => reject(error);
            reader.readAsDataURL(file);
        });
    }

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
        }
    });
});