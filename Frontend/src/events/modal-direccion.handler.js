import { agregarDireccion, obtenerDirecciones, eliminarDireccion, actualizarDireccion } from "../services/ClienteService.js";

let editMap, editMarker;

function initEditMap() {
    editMap = new google.maps.Map(document.getElementById("edit-map"), {
        center: { lat: 19.5438, lng: -96.9108 },
        zoom: 14,
    });

    editMarker = new google.maps.Marker({
        position: editMap.getCenter(),
        map: editMap,
        draggable: true,
    });

    editMarker.addListener('dragend', function () {
        const position = editMarker.getPosition();
        document.getElementById('edit-latitude').value = position.lat();
        document.getElementById('edit-longitude').value = position.lng();
    });
}

document.addEventListener("DOMContentLoaded", async () => {
    try {
      const direcciones = await obtenerDirecciones();
      direcciones.forEach(direccion => {
        crearTarjetaDireccion(direccion)
      })
    } catch (error) {
      console.error("Error al cargar direcciones: ", error);
    }
});

function limpiarFormulario(formId) {
    const form = document.getElementById(formId);
    form.reset()
    if (formId === 'addAddressForm') {
        document.getElementById('latitude').value = '';
        document.getElementById('longitude').value = '';
        if (typeof map !== 'undefined' && typeof marker !== 'undefined') {
            const defaultPosition = { lat: 19.5438, lng: -96.9108 };
            map.setCenter(defaultPosition);
            marker.setPosition(defaultPosition);
        }
    } else if (formId === 'editAddressForm') {
        document.getElementById('edit-latitude').value = '';
        document.getElementById('edit-longitude').value = '';
        if (typeof editMap !== 'undefined' && typeof editMarker !== 'undefined') {
            const defaultPosition = { lat: 19.5438, lng: -96.9108 };
            editMap.setCenter(defaultPosition);
            editMarker.setPosition(defaultPosition);
        }
    }
}

function crearTarjetaDireccion(direccion) {
    const addressCard = document.createElement('div');
    addressCard.className = 'address-card mt-4';
    addressCard.dataset.id = direccion.id;

    addressCard.innerHTML = `
        <div class="address-actions">
            <button class="address-action-btn edit-address">
                <i class="fas fa-pen"></i>
            </button>
            <button class="address-action-btn delete-address">
                <i class="fas fa-times"></i>
            </button>
        </div>
        <h3 class="h5 mb-3">${direccion.nombreCompleto}</h3>
        <p class="mb-1">${direccion.calle} ${direccion.numeroExterior}${direccion.numeroInterior ? ` Int. ${direccion.numeroInterior}` : ''}</p>
        <p class="mb-1">${direccion.colonia}</p>
        <p class="mb-1">${direccion.codigoPostal}</p>
        <p class="mb-1">${direccion.ciudad}, ${direccion.entidadFederativa}</p>
        <p class="mb-3">${direccion.telefonoCelular}</p>
    `;

    const editBtn = addressCard.querySelector('.edit-address');
    const deleteBtn = addressCard.querySelector('.delete-address');

    editBtn.addEventListener('click', () => handleEditAddress(direccion.id));
    deleteBtn.addEventListener('click', () => handleDeleteAddress(direccion.id));

    const addressContainer = document.querySelector('section');
    addressContainer.appendChild(addressCard);
}

function actualizarTarjetaDireccion(direccion) {
    const addressCard = document.querySelector(`[data-id="${direccion.id}"]`);
    if (addressCard) {
        addressCard.innerHTML = `
            <div class="address-actions">
                <button class="address-action-btn edit-address">
                    <i class="fas fa-pen"></i>
                </button>
                <button class="address-action-btn delete-address">
                    <i class="fas fa-times"></i>
                </button>
            </div>
            <h3 class="h5 mb-3">${direccion.nombreCompleto}</h3>
            <p class="mb-1">${direccion.calle} ${direccion.numeroExterior}${direccion.numeroInterior ? ` Int. ${direccion.numeroInterior}` : ''}</p>
            <p class="mb-1">${direccion.colonia}</p>
            <p class="mb-1">${direccion.codigoPostal}</p>
            <p class="mb-1">${direccion.ciudad}, ${direccion.entidadFederativa}</p>
            <p class="mb-3">${direccion.telefonoCelular}</p>
        `;

        const editBtn = addressCard.querySelector('.edit-address');
        const deleteBtn = addressCard.querySelector('.delete-address');

        editBtn.addEventListener('click', () => handleEditAddress(direccion.id));
        deleteBtn.addEventListener('click', () => handleDeleteAddress(direccion.id));
    }
}

async function handleEditAddress(id) {
    try {
        const direcciones = await obtenerDirecciones();
        const direccion = direcciones.find(d => d.id === id);
        
        if (direccion) {
            document.getElementById('edit-address-id').value = direccion.id;
            document.getElementById('edit-nombre').value = direccion.nombreCompleto;
            document.getElementById('edit-telefonoCelular').value = direccion.telefonoCelular;
            document.getElementById('edit-calle').value = direccion.calle;
            document.getElementById('edit-numeroExterior').value = direccion.numeroExterior;
            document.getElementById('edit-numeroInterior').value = direccion.numeroInterior || '';
            document.getElementById('edit-colonia').value = direccion.colonia;
            document.getElementById('edit-codigoPostal').value = direccion.codigoPostal;
            document.getElementById('edit-ciudad').value = direccion.ciudad;
            document.getElementById('edit-entidadFederativa').value = direccion.entidadFederativa;
            document.getElementById('edit-municipio').value = direccion.municipio;

            if (direccion.coordenadasDireccion) {
                document.getElementById('edit-latitude').value = direccion.coordenadasDireccion.latitud;
                document.getElementById('edit-longitude').value = direccion.coordenadasDireccion.longitud;
                
                if (typeof editMap !== 'undefined' && typeof editMarker !== 'undefined') {
                    const position = {
                        lat: direccion.coordenadasDireccion.latitud,
                        lng: direccion.coordenadasDireccion.longitud
                    };
                    editMap.setCenter(position);
                    editMarker.setPosition(position);
                } else {
                    setTimeout(initEditMap, 100);
                }
            }
            const editModal = new bootstrap.Modal(document.getElementById('editAddressModal'));
            editModal.show();
        }
    } catch (error) {
        console.error('Error al cargar los datos de la dirección:', error);
        alert('Hubo un error al cargar los datos de la dirección');
    }
}

document.getElementById('updateAddressBtn').addEventListener('click', async function() {
    try {
        if (!document.getElementById('editAddressForm').checkValidity()) {
            alert('Por favor, completa todos los campos requeridos');
            return;
        }

        const id = document.getElementById('edit-address-id').value;
        const direccionDTO = {
            id: id,
            calle: document.getElementById('edit-calle').value,
            ciudad: document.getElementById('edit-ciudad').value,
            codigoPostal: parseInt(document.getElementById('edit-codigoPostal').value),
            colonia: document.getElementById('edit-colonia').value,
            entidadFederativa: document.getElementById('edit-entidadFederativa').value,
            municipio: document.getElementById('edit-municipio').value,
            numeroExterior: document.getElementById('edit-numeroExterior').value,
            numeroInterior: document.getElementById('edit-numeroInterior').value || null,
            nombreCompleto: document.getElementById('edit-nombre').value,
            telefonoCelular: document.getElementById('edit-telefonoCelular').value,
            coordenadasDireccion: {
                latitud: parseFloat(document.getElementById('edit-latitude').value) || null,
                longitud: parseFloat(document.getElementById('edit-longitude').value) || null
            }
        };

        if (!direccionDTO.numeroInterior) delete direccionDTO.numeroInterior;
        if (!direccionDTO.coordenadasDireccion.latitud || !direccionDTO.coordenadasDireccion.longitud) {
            delete direccionDTO.coordenadasDireccion;
        }

        const data = await actualizarDireccion(direccionDTO);
        actualizarTarjetaDireccion(data);
        
        const modal = bootstrap.Modal.getInstance(document.getElementById('editAddressModal'));
        modal.hide();
        limpiarFormulario('editAddressForm');
        
    } catch (error) {
        console.error('Error al actualizar la dirección:', error);
        alert('Hubo un error al actualizar la dirección');
    }
});

async function handleDeleteAddress(id) {
    if (confirm('¿Estás seguro de que deseas eliminar esta dirección?')) {
        try {
            await eliminarDireccion(id);
            const addressCard = document.querySelector(`[data-id="${id}"]`);
            if (addressCard) {
                addressCard.remove();
            }
        } catch (error) {
            console.error('Error al eliminar la dirección:', error);
            alert('Hubo un error al eliminar la dirección');
        }
    }
}

document.getElementById('saveAddressBtn').addEventListener('click', async function() {
    try {
        if (!document.getElementById('addAddressForm').checkValidity()) {
            alert('Por favor, completa todos los campos requeridos');
            return;
        }

        const direccionDTO = {
            calle: document.getElementById('calle').value,
            ciudad: document.getElementById('ciudad').value,
            codigoPostal: parseInt(document.getElementById('codigoPostal').value),
            colonia: document.getElementById('colonia').value,
            entidadFederativa: document.getElementById('entidadFederativa').value,
            municipio: document.getElementById('municipio').value,
            numeroExterior: document.getElementById('numeroExterior').value,
            numeroInterior: document.getElementById('numeroInterior').value || null,
            coordenadasDireccion: {
                latitud: parseFloat(document.getElementById('latitude').value) || null,
                longitud: parseFloat(document.getElementById('longitude').value) || null
            },
            nombreCompleto: document.getElementById('nombre').value,
            telefonoCelular: document.getElementById('telefonoCelular').value,
        };

        if (!direccionDTO.numeroInterior) delete direccionDTO.numeroInterior;
        if (!direccionDTO.coordenadasDireccion.latitud || !direccionDTO.coordenadasDireccion.longitud) {
            delete direccionDTO.coordenadasDireccion;
        }

        const data = await agregarDireccion(direccionDTO);
        
        crearTarjetaDireccion({...direccionDTO, id: data.id});
        
        const modal = bootstrap.Modal.getInstance(document.getElementById('addAddressModal'));
        modal.hide();
        limpiarFormulario("addAddressForm");
        
    } catch (error) {
        console.error('Error al guardar la dirección:', error);
        alert('Hubo un error al guardar la dirección');
    }
});

document.getElementById('addAddressModal').addEventListener('hidden.bs.modal', function () {
    limpiarFormulario('addAddressForm');
});

document.getElementById('editAddressModal').addEventListener('hidden.bs.modal', function () {
    limpiarFormulario('editAddressForm');
});