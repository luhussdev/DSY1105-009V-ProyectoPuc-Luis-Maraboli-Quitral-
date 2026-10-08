# 3. Requerimientos Funcionales y No Funcionales

## 3.1 Requerimientos Funcionales (RF)

* **RF01 - Catálogo Unificado:** La aplicación debe presentar un listado principal con tarjetas descriptivas de los contenidos (Proyectos, Capacidades I+D, Publicaciones, Actividades).
* **RF02 - Filtrado por Categorías y Áreas:** El usuario podrá filtrar el catálogo por tipo de contenido (ej. Proyecto vs Capacidad) o área temática (ej. Construcción Sustentable).
* **RF03 - Vista de Detalle:** Al seleccionar un elemento del catálogo, la aplicación debe mostrar su ficha técnica completa (título, descripción, año, estado, integrante a cargo e imagen).
* **RF04 - Navegación entre Entidades Relacionadas:** Desde la pantalla de detalle de un proyecto, el usuario debe poder presionar sobre el integrante responsable y navegar a la pantalla de su perfil.
* **RF05 - Formulario de Contacto Simulado:** La aplicación debe disponer de un formulario con campos para Nombre, Correo y Mensaje, permitiendo simular el envío de una solicitud de colaboración con confirmación visual.
* **RF06 - Gestión de Favoritos:** El usuario podrá marcar/desmarcar elementos como favoritos y consultarlos en una sección dedicada.

## 3.2 Requerimientos No Funcionales (RNF)

* **RNF01 - Interfaz Nativa (Jetpack Compose / Material Design 3):** La interfaz debe construirse con Jetpack Compose aplicando la guía de estilos y componentes de Material Design 3.
* **RNF02 - Arquitectura Mantenible (MVVM):** El proyecto debe implementarse bajo el patrón Model-View-ViewModel con separación clara entre las capas de interfaz (`ui`), lógica de negocio (`viewmodel`), repositorio (`repository`) y datos (`model`).
* **RNF03 - Desempeño y Fluidez:** Las pantallas principales y transiciones deben renderizar de forma fluida (a 60 fps) sin bloqueos en el hilo principal de la UI.
* **RNF04 - Privacidad y Datos MOCK:** La aplicación no utilizará credenciales reales ni bases de datos productivas. Operará exclusivamente con datos sintéticos, públicos y anónimos.
* **RNF05 - Compatibilidad Móvil:** La aplicación estará optimizada para dispositivos Android (API 24 / Android 7.0 o superior).