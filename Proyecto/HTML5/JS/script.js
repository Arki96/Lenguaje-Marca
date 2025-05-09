function enviarFormularioEntradas(event) {
    event.preventDefault(); // Evita el comportamiento predeterminado del formulario

    // Recopila los datos del formulario de compra
    const nombre = document.getElementById('nombre').value;
    const fecha = document.getElementById('fecha').value;
    const lugar = document.getElementById('lugar').value;
    const cantidad = document.getElementById('cantidad').value;
    const categoria = document.getElementById('categoria').value;
    const metodoPago = document.querySelector('input[name="metodo_pago"]:checked').value;
    const terminos = document.querySelector('input[name="terminos"]:checked').value;

    // Guarda los datos en sessionStorage
    sessionStorage.setItem('nombre', nombre);
    sessionStorage.setItem('fecha', fecha);
    sessionStorage.setItem('lugar', lugar);
    sessionStorage.setItem('cantidad', cantidad);
    sessionStorage.setItem('categoria', categoria);
    sessionStorage.setItem('metodoPago', metodoPago);
    sessionStorage.setItem('terminos', terminos);

    // Redirige a la página de confirmación
    window.location.href = 'confirmacion.html';
}
// Código para mostrar los datos en Confirmacion.html
if (window.location.pathname.endsWith('confirmacion.html')) {
    const nombre = sessionStorage.getItem('nombre');
    const fecha = sessionStorage.getItem('fecha');
    const lugar = sessionStorage.getItem('lugar');
    const cantidad = sessionStorage.getItem('cantidad');
    const categoria = sessionStorage.getItem('categoria');
    const metodoPago = sessionStorage.getItem('metodoPago');
    const terminos = sessionStorage.getItem('terminos');

    // Muestra los datos en la página
    document.getElementById('nombreConfirmacion').textContent = nombre;
    document.getElementById('fechaConfirmacion').textContent = fecha;
    document.getElementById('lugarConfirmacion').textContent = lugar;
    document.getElementById('cantidadConfirmacion').textContent = cantidad;
    document.getElementById('categoriaConfirmacion').textContent = categoria;
    document.getElementById('metodoPagoConfirmacion').textContent = metodoPago;
    document.getElementById('terminosConfirmacion').textContent = terminos;
}

// Función para imprimir el contenido de la página
document.addEventListener('DOMContentLoaded', function () {
    const botonImprimir = document.getElementById('imprimir');
    if (botonImprimir) {
        botonImprimir.addEventListener('click', function () {
            window.print();
        });
    }
});
