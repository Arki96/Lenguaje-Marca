(() => {
  // Colores principales desde CSS
  const primaryColor = getComputedStyle(document.documentElement).getPropertyValue('--primary-color').trim();
  const dangerColor = getComputedStyle(document.documentElement).getPropertyValue('--danger-color').trim();

  // Clave para localStorage
  const STORAGE_KEY = 'datosReservasBar';

  // Estado de reservas
  let datosReservas = {};

  // Elementos del DOM
  const calendarEl = document.querySelector('.calendar');
  const monthYearLabel = document.getElementById('monthYearLabel');
  const prevMonthBtn = document.getElementById('prevMonthBtn');
  const nextMonthBtn = document.getElementById('nextMonthBtn');
  const dayModal = document.getElementById('dayModal');
  const modalTitle = document.getElementById('modalTitle');
  const modalDescription = document.getElementById('modalDescription');
  const closeModalBtn = document.getElementById('closeModalBtn');
  const toggleOpenCloseBtn = document.getElementById('toggleOpenCloseBtn');
  const reservationsList = document.getElementById('reservationsList');
  const addReservationForm = document.getElementById('addReservationForm');
  const modoEncargadoBtn = document.getElementById('modoEncargadoBtn');
  const confirmModal = document.getElementById('confirmModal');
  const confirmText = document.getElementById('confirmText');
  const confirmYesBtn = document.getElementById('confirmYesBtn');
  const confirmNoBtn = document.getElementById('confirmNoBtn');

  // Estado de la app
  let modoEncargado = false;
  let fechaSeleccionada = null;
  let añoVista, mesVista; // Año y mes actuales

  // --- Utilidades ---
  function formatearFecha(date) {
    const y = date.getFullYear();
    const m = (date.getMonth() + 1).toString().padStart(2, '0');
    const d = date.getDate().toString().padStart(2, '0');
    return `${y}-${m}-${d}`;
  }

  function mostrarFecha(fechaStr) {
    const options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
    const date = new Date(fechaStr + 'T00:00:00');
    return date.toLocaleDateString('es-ES', options);
  }

  function cargarDatos() {
    const guardado = localStorage.getItem(STORAGE_KEY);
    if (guardado) {
      try {
        datosReservas = JSON.parse(guardado);
      } catch {
        datosReservas = {};
      }
    }
  }

  function guardarDatos() {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(datosReservas));
  }

  function generarId() {
    return '_' + Math.random().toString(36).substr(2, 9);
  }

  // --- Renderizado del calendario ---
  function renderizarCalendario() {
    // Limpiar días previos salvo los días de la semana
    while (calendarEl.children.length > 7) {
      calendarEl.removeChild(calendarEl.lastChild);
    }
    const primerDia = new Date(añoVista, mesVista, 1);
    const ultimoDia = new Date(añoVista, mesVista + 1, 0);
    const primerDiaSemana = primerDia.getDay();
    const totalDias = ultimoDia.getDate();

    monthYearLabel.textContent = primerDia.toLocaleDateString('es-ES', { year: 'numeric', month: 'long' });

    // Espacios vacíos antes del primer día
    for (let i = 0; i < primerDiaSemana; i++) {
      const placeholder = document.createElement('div');
      placeholder.classList.add('day');
      placeholder.style.background = 'transparent';
      placeholder.style.cursor = 'default';
      placeholder.tabIndex = -1;
      calendarEl.appendChild(placeholder);
    }

    const hoyStr = formatearFecha(new Date());

    for (let nroDia = 1; nroDia <= totalDias; nroDia++) {
      const date = new Date(añoVista, mesVista, nroDia);
      const dateStr = formatearFecha(date);
      const dayEl = document.createElement('button');
      dayEl.classList.add('day');
      dayEl.type = 'button';
      dayEl.setAttribute('role', 'gridcell');
      dayEl.setAttribute('aria-label', date.toDateString());
      dayEl.tabIndex = 0;

      // Número de día
      const numeroDiaEl = document.createElement('div');
      numeroDiaEl.classList.add('date-number');
      numeroDiaEl.textContent = nroDia;
      dayEl.appendChild(numeroDiaEl);

      // Comprobar si está cerrado
      const infoDia = datosReservas[dateStr];
      if (infoDia?.cerrado) {
        dayEl.classList.add('closed');
        dayEl.setAttribute('aria-disabled', 'true');
        const etiquetaCerrado = document.createElement('div');
        etiquetaCerrado.className = 'open-indicator';
        etiquetaCerrado.textContent = 'Cerrado';
        dayEl.appendChild(etiquetaCerrado);
      } else {
        // Mostrar indicador abierto
        const etiquetaAbierto = document.createElement('div');
        etiquetaAbierto.className = 'open-indicator';
        etiquetaAbierto.textContent = 'Abierto';
        dayEl.appendChild(etiquetaAbierto);

        // Mostrar número de reservas
        if (infoDia?.reservas?.length > 0) {
          const cuentaRes = document.createElement('div');
          cuentaRes.className = 'reservation-count';
          cuentaRes.textContent = `${infoDia.reservas.length} Reserva${infoDia.reservas.length > 1 ? 's' : ''}`;
          dayEl.appendChild(cuentaRes);
        }
      }

      // Resaltar hoy
      if (dateStr === hoyStr) {
        dayEl.classList.add('today');
      }

      // Al hacer clic, abrir modal (si está cerrado y no eres encargado, solo alerta)
      dayEl.addEventListener('click', () => {
        abrirModalDia(dateStr);
      });

      calendarEl.appendChild(dayEl);
    }
  }

  // --- Modal de día ---
  function abrirModalDia(fechaStr) {
    const infoDia = datosReservas[fechaStr];
    // Si el día está cerrado y no eres encargado, solo muestra alerta y no abre modal
    if (infoDia && infoDia.cerrado && !modoEncargado) {
      alert('Este día está cerrado.');
      return;
    }
    fechaSeleccionada = fechaStr;
    modalTitle.textContent = mostrarFecha(fechaStr);
    actualizarDescripcionModal();
    actualizarBotonAbrirCerrar();
    renderizarReservas();
    dayModal.classList.add('show');
    addReservationForm.reset();
    setTimeout(() => {
      document.getElementById('resNameInput').focus();
    }, 150);

    // Mostrar botón solo si modo encargado
    toggleOpenCloseBtn.style.display = modoEncargado ? 'block' : 'none';
  }

  function cerrarModalDia() {
    dayModal.classList.remove('show');
    fechaSeleccionada = null;
  }

  function actualizarBotonAbrirCerrar() {
    const infoDia = datosReservas[fechaSeleccionada] || {};
    if (infoDia.cerrado) {
      toggleOpenCloseBtn.textContent = 'Marcar como Abierto';
      toggleOpenCloseBtn.style.backgroundColor = primaryColor;
      toggleOpenCloseBtn.style.color = 'white';
    } else {
      toggleOpenCloseBtn.textContent = 'Marcar como Cerrado';
      toggleOpenCloseBtn.style.backgroundColor = dangerColor;
      toggleOpenCloseBtn.style.color = 'white';
    }
  }

  function actualizarDescripcionModal() {
    const infoDia = datosReservas[fechaSeleccionada] || {};
    modalDescription.textContent = infoDia.cerrado
      ? 'Este día está cerrado. No se pueden agregar reservas.'
      : 'Día abierto: Puedes agregar reservas.';
  }

  // --- Cambiar estado abierto/cerrado del día ---
  function alternarEstadoDia() {
    const infoDia = datosReservas[fechaSeleccionada] || { cerrado: false, reservas: [] };
    infoDia.cerrado = !infoDia.cerrado;
    datosReservas[fechaSeleccionada] = infoDia;
    guardarDatos();
    actualizarDescripcionModal();
    actualizarBotonAbrirCerrar();
    renderizarCalendario();
    renderizarReservas();
  }

  // --- Renderizar reservas ---
  function renderizarReservas() {
    reservationsList.innerHTML = '';
    const infoDia = datosReservas[fechaSeleccionada];
    if (!infoDia || !infoDia.reservas?.length) {
      const vacio = document.createElement('li');
      vacio.className = 'empty-message';
      vacio.textContent = 'No hay reservas aún.';
      reservationsList.appendChild(vacio);
      return;
    }
    infoDia.reservas.sort((a, b) => a.hora.localeCompare(b.hora)).forEach(res => {
      const li = document.createElement('li');
      li.textContent = `${res.hora} - ${res.nombre} (${res.invitados} invitado${res.invitados > 1 ? 's' : ''})`;

      const acciones = document.createElement('span');
      acciones.className = 'reservation-actions';

      const cancelarBtn = document.createElement('button');
      cancelarBtn.title = 'Cancelar Reserva';
      cancelarBtn.setAttribute('aria-label', `Cancelar reserva de ${res.nombre} a las ${res.hora}`);
      cancelarBtn.innerHTML = '&times;';
      cancelarBtn.addEventListener('click', () => {
        cancelarReserva(res.id);
      });
      acciones.appendChild(cancelarBtn);

      li.appendChild(acciones);
      reservationsList.appendChild(li);
    });
  }

  // --- Cancelar reserva ---
  function cancelarReserva(id) {
    let infoDia = datosReservas[fechaSeleccionada];
    if (!infoDia) return;
    const idx = infoDia.reservas.findIndex(r => r.id === id);
    if (idx === -1) return;

    // Pide la clave solo si no es modo encargado
    if (!modoEncargado) {
      const pin = prompt('Introduce la clave de tu reserva para eliminarla:');
      if (pin !== infoDia.reservas[idx].pin) {
        alert('Clave incorrecta. Solo puedes borrar tu propia reserva.');
        return;
      }
    }

    infoDia.reservas.splice(idx, 1);
    datosReservas[fechaSeleccionada] = infoDia;
    guardarDatos();
    renderizarReservas();
    renderizarCalendario();
  }

  // --- Confirmación de reserva ---
  let reservaPendiente = null;
  addReservationForm.addEventListener('submit', e => {
    e.preventDefault();
    if (!fechaSeleccionada) return;
    const infoDia = datosReservas[fechaSeleccionada] || { cerrado: false, reservas: [] };
    if (infoDia.cerrado) {
      alert('Este día está cerrado. No se pueden agregar reservas.');
      return;
    }
    const nombre = addReservationForm.resName.value.trim();
    const hora = addReservationForm.resTime.value;
    const invitados = parseInt(addReservationForm.resGuests.value);
    const pin = addReservationForm.resPin.value.trim();

    // Solo permitir horas de almuerzo (12:00-16:00) y cena (20:00-23:00)
    if (
      !/^((1[2-5]):[0-5][0-9]|20:[0-5][0-9]|21:[0-5][0-9]|22:[0-5][0-9]|23:00)$/.test(hora)
    ) {
      alert('Solo se permiten reservas para almuerzos (12:00-16:00) y cenas (20:00-23:00).');
      return;
    }

    if (!nombre || !hora || !invitados || invitados < 1) {
      alert('Por favor, completa todos los campos de la reserva con valores válidos.');
      return;
    }

    // Mostrar modal de confirmación
    reservaPendiente = { nombre, hora, invitados, pin };
    confirmText.textContent = `¿Confirmar reserva para ${nombre} a las ${hora} (${invitados} invitado${invitados > 1 ? 's' : ''})?`;
    confirmModal.style.display = 'block';
  });

  confirmYesBtn.addEventListener('click', () => {
    if (!reservaPendiente) return;
    const infoDia = datosReservas[fechaSeleccionada] || { cerrado: false, reservas: [] };
    infoDia.reservas.push({ id: generarId(), ...reservaPendiente });
    datosReservas[fechaSeleccionada] = infoDia;
    guardarDatos();
    renderizarReservas();
    renderizarCalendario();
    addReservationForm.reset();
    confirmModal.style.display = 'none';
    reservaPendiente = null;
    alert('Reserva confirmada.');
  });

  confirmNoBtn.addEventListener('click', () => {
    confirmModal.style.display = 'none';
    reservaPendiente = null;
  });

  // --- Botón salir modo encargado ---
  let salirEncargadoBtn = document.getElementById('salirEncargadoBtn');
  if (!salirEncargadoBtn) {
    salirEncargadoBtn = document.createElement('button');
    salirEncargadoBtn.id = 'salirEncargadoBtn';
    salirEncargadoBtn.textContent = 'Salir Modo Encargado';
    salirEncargadoBtn.style.display = 'none';
    salirEncargadoBtn.className = 'modo-encargado-btn salir';
    document.body.appendChild(salirEncargadoBtn);
  }

  // Oculta el botón al iniciar
  function actualizarBotonSalirEncargado() {
    salirEncargadoBtn.style.display = modoEncargado ? 'block' : 'none';
  }
  actualizarBotonSalirEncargado(); // <-- Añade esta línea aquí

  // --- Modo encargado ---
  modoEncargadoBtn.style.display = 'block'; // SIEMPRE visible y accesible

  modoEncargadoBtn.addEventListener('click', () => {
    const clave = prompt('Introduce la clave de encargado:');
    if (clave === 'bar2024') {
      modoEncargado = true;
      alert('Modo encargado activado.');
      actualizarBotonSalirEncargado();
      if (fechaSeleccionada) toggleOpenCloseBtn.style.display = 'block';
    } else {
      alert('Clave incorrecta.');
    }
  });

  salirEncargadoBtn.addEventListener('click', () => {
    modoEncargado = false;
    alert('Modo encargado desactivado.');
    actualizarBotonSalirEncargado();
    toggleOpenCloseBtn.style.display = 'none';
  });

  // --- Botón abrir/cerrar día ---
  toggleOpenCloseBtn.addEventListener('click', () => {
    alternarEstadoDia();
  });

  // --- Cerrar modal día ---
  closeModalBtn.addEventListener('click', cerrarModalDia);
  dayModal.addEventListener('click', (e) => {
    if (e.target === dayModal) cerrarModalDia();
  });
  window.addEventListener('keydown', (e) => {
    if (e.key === 'Escape' && dayModal.classList.contains('show')) {
      cerrarModalDia();
    }
  });

  // --- Navegación de meses ---
  prevMonthBtn.addEventListener('click', () => {
    mesVista--;
    if (mesVista < 0) {
      mesVista = 11;
      añoVista--;
    }
    renderizarCalendario();
  });
  nextMonthBtn.addEventListener('click', () => {
    mesVista++;
    if (mesVista > 11) {
      mesVista = 0;
      añoVista++;
    }
    renderizarCalendario();
  });

  // --- Inicialización ---
  function init() {
    cargarDatos();
    const ahora = new Date();
    añoVista = ahora.getFullYear();
    mesVista = ahora.getMonth();
    renderizarCalendario();
  }

  init();
})();

