document.addEventListener("DOMContentLoaded", () => {
    // Manejo del botón de menú desplegable
    const menuToggle = document.querySelector(".menu-toggle");
    if (menuToggle) {
        menuToggle.addEventListener("click", () => {
            const menu = document.querySelector(".menu-lista");
            if (menu) {
                menu.classList.toggle("active"); // Activa/desactiva la clase 'active'
            }
        });
    }

    const pantalla = document.querySelector("#resultado");
    let operacionActual = "";
    let parenAbiertos = 0; // Contador de paréntesis abiertos

    // Función para actualizar la pantalla
    function actualizarPantalla() {
        pantalla.textContent = operacionActual || "0";
    }

    // Función para manejar los números
    function agregarNumero(numero) {
        operacionActual += numero;
        actualizarPantalla();
    }

    // Función para manejar los operadores
    function agregarOperador(operador) {
        if (operacionActual === "") return;
        operacionActual += ` ${operador} `;
        actualizarPantalla();
    }

    // Función para manejar los paréntesis
    function agregarParentesis() {
        if (parenAbiertos === 0 || operacionActual.endsWith("(")) {
            // Agregar paréntesis de apertura
            operacionActual += "(";
            parenAbiertos++;
        } else if (parenAbiertos > 0) {
            // Agregar paréntesis de cierre
            operacionActual += ")";
            parenAbiertos--;
        }
        actualizarPantalla();
    }

    // Función para limpiar la pantalla
    function limpiar() {
        operacionActual = "";
        parenAbiertos = 0;
        actualizarPantalla();
    }

    // Función para calcular el resultado
    function calcular() {
        try {
            // Evalúa la operación actual
            const resultado = eval(operacionActual.replace(/x/g, "*"));
            operacionActual = resultado.toString();
        } catch (error) {
            operacionActual = "Error";
        }
        actualizarPantalla();
    }

    // Manejo de los botones
    document.querySelector(".botones").addEventListener("click", (e) => {
        if (!e.target.matches("button")) return;

        const boton = e.target;
        const textoBoton = boton.textContent;

        if (boton.classList.contains("boton-parentesis")) {
            agregarParentesis();
        } else if (boton.classList.contains("operador")) {
            agregarOperador(textoBoton);
        } else if (textoBoton === "AC") {
            limpiar();
        } else if (textoBoton === "=") {
            calcular();
        } else {
            agregarNumero(textoBoton);
        }
    });

    // Inicializa la pantalla
    actualizarPantalla();
});

document.addEventListener("DOMContentLoaded", () => {
    // Inicializa DataTables en la tabla con ID "tabla-formulas"
    $('#tabla-formulas').DataTable({
        paging: true, // Activa la paginación
        searching: false, // Desactiva la barra de búsqueda (opcional)
        info: false, // Oculta la información de "Mostrando X de Y" (opcional)
        lengthChange: false, // Desactiva la opción de cambiar el número de filas visibles
        pageLength: 3, // Número de filas por página
        language: {
            paginate: {
                previous: "Anterior",
                next: "Siguiente"
            }
        }
    });
});