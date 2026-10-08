# 4. Historias de Usuario y Casos de Uso

## 4.1 Historias de Usuario (HU)

### **HU01 - Filtrado de Capacidades e Iniciativas**
* **Como:** Representante de una empresa de la construcción,
* **Quiero:** Filtrar el catálogo por la categoría "Capacidades I+D",
* **Para:** Identificar laboratorios y servicios tecnológicos disponibles en el CIM UC.
* **Criterios de Aceptación:**
    1. Existe una barra de filtro por categorías en la parte superior del catálogo.
    2. Al seleccionar "Capacidades I+D", la lista se actualiza mostrando únicamente dichos contenidos.
    3. Si no hay resultados, se despliega un mensaje claro de "Sin resultados".

### **HU02 - Navegación a Perfil de Integrante Asociado**
* **Como:** Investigador o usuario interesado,
* **Quiero:** Presionar el nombre del líder de un proyecto en la vista de detalle,
* **Para:** Ver la ficha del integrante y conocer su área de especialidad.
* **Criterios de Aceptación:**
    1. La vista de detalle del proyecto muestra una tarjeta/chip interactivo con el nombre del integrante.
    2. Al presionar el chip, la app navega a la pantalla `MemberDetailScreen`.
    3. Muestra la foto, cargo, correo simulado y proyectos asociados al integrante.

### **HU03 - Solicitud de Colaboración**
* **Como:** Usuario de la aplicación,
* **Quiero:** Enviar un mensaje mediante el formulario de contacto,
* **Para:** Solicitar mayor información o proponer un proyecto conjunto.
* **Criterios de Aceptación:**
    1. El formulario solicita Nombre, Correo y Mensaje.
    2. Valida que los campos no estén vacíos y que el correo tenga formato válido (`user@domain.com`).
    3. Al presionar "Enviar", muestra un mensaje visual de éxito (Snackback/Dialog) y limpia los datos.

---

## 4.2 Casos de Uso (CU)

### **CU01: Explorar y Filtrar Catálogo**
* **Actor Principal:** Usuario (Público General / Industria).
* **Precondición:** La aplicación está iniciada en la pantalla principal.
* **Flujo Principal:**
    1. El usuario navega al catálogo unificado.
    2. El sistema despliega la lista completa de elementos MOCK.
    3. El usuario selecciona un filtro por categoría (ej. "Proyectos").
    4. El sistema actualiza la lista mostrando solo los elementos coincidentes.

### **CU02: Consultar Detalle y Navegar a Integrante**
* **Actor Principal:** Usuario.
* **Precondición:** El usuario visualiza una lista de contenidos.
* **Flujo Principal:**
    1. El usuario selecciona una tarjeta del catálogo.
    2. El sistema despliega la pantalla de detalle del contenido.
    3. El usuario presiona la opción "Ver Integrante Responsable".
    4. El sistema redirige a la vista de perfil del integrante con sus datos y proyectos asociados.

### **CU03: Enviar Solicitud de Contacto Simulado**
* **Actor Principal:** Usuario.
* **Precondición:** El usuario está en la vista de detalle o en la pantalla de contacto.
* **Flujo Principal:**
    1. El usuario selecciona la opción "Solicitar Colaboración".
    2. El sistema presenta el formulario de contacto.
    3. El usuario ingresa Nombre, Correo y Mensaje.
    4. El usuario presiona "Enviar".
    5. El sistema valida las entradas, simula el procesamiento y muestra una confirmación de éxito.