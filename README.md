# CIM UC - App de Vinculación e Innovación en Madera

Aplicación Android nativa desarrollada en **Kotlin** y **Jetpack Compose** para el **Centro UC de Innovación en Madera (CIM UC)**. 
La plataforma permite explorar proyectos y capacidades I+D destacadas, gestionar suscripciones a novedades mediante persistencia local y 
canalizar solicitudes de contacto/colaboración institucional.

Este proyecto forma parte de las evaluaciones **Parcial 2 y Parcial 3** de la asignatura, cumpliendo estrictamente con los lineamientos
de arquitectura limpia y mejores prácticas de desarrollo móvil.

---

## Identidad Visual e Interfaz
El diseño de la aplicación respeta la paleta cromática e identidad institucional de la plataforma web del **CIM UC**:
* **Azul Institucional (`#0A192F`)**: Utilizado en contenedores y estructuras principales.
* **Azul Primario (`#0066CC`)**: Aplicado en botones de acción, íconos y elementos interactivos.
* **Amarillo Acento (`#FFC107`)**: Destacado para etiquetas de áreas temáticas y elementos decorativos.
* **Gris de Fondo (`#F8F9FA`)**: Proporciona un entorno visual limpio e industrial.

---

## Arquitectura y Patrones (MVVM)
El proyecto implementa el patrón **Model-View-ViewModel (MVVM)** asegurando un desacoplamiento efectivo entre la lógica de negocio y la interfaz de usuario:

* **`data/`**: Repositorios de datos sintéticos y gestión de la fuente de información (`ProyectoRepository`).
* **`model/`**: Modelos de datos inmutables y estados de UI (`ProyectoCim`, `FormularioUiState`, `FormularioErrores`).
* **`viewmodel/`**: Lógica de presentación, gestión de flujo de estado con `StateFlow` y validación centralizada (`UsuarioViewModel`, `ProyectoViewModel`, `EstadoViewModel`).
* **`ui/`**:
    * **`components/`**: Componentes reutilizables de Jetpack Compose (`CampoText`, `AceptarTerminos`, `TarjetaProyecto`, `ImagenInteligente`).
    * **`screens/`**: Pantallas principales del flujo de usuario (`PantallaPrincipal`, `RegistroScreen`, `ResumenScreen`, `PerfilScreen`, `AppNavigation`).
    * **`theme/`**: Configuración global de Material Design 3 (`Color.kt`, `Theme.kt`, `Type.kt`).

---

## Funcionalidades Principales

1. **Catálogo de Proyectos I+D (`PantallaPrincipal`)**:
    * Despliegue dinámico y eficiente de iniciativas mediante `LazyColumn`.
    * Renderizado optimizado con la tarjeta personalizada `TarjetaProyecto`.

2. **Persistencia Local y Animaciones UI**:
    * Almacenamiento de preferencias de suscripción utilizando **Jetpack DataStore**.
    * Feedback visual reactivo mediante `animateColorAsState` para transiciones de color y `AnimatedVisibility` para mensajes de confirmación.

3. **Formulario de Vinculación y Contacto (`RegistroScreen` + `ResumenScreen`)**:
    * Captura de campos específicos para el entorno CIM UC (Nombre, Correo Institucional, Empresa/Organización y Área de Interés I+D).
    * Validación desacoplada en tiempo real ejecutada en `UsuarioViewModel`.
    * Pantalla de confirmación reactiva compartiendo la misma instancia del ViewModel.

4. **Perfil de Usuario/Investigador (`PerfilScreen`)**:
    * Captura y previsualización de fotografía mediante integración nativa con **Cámara** y **Galería**.
    * Componente `ImagenInteligente` para el procesamiento y encuadre visual.

---

## Tecnologías y Librerías

* **Lenguaje**: Kotlin
* **UI**: Jetpack Compose (Material Design 3)
* **Navegación**: Navigation Compose (`NavHost`, `NavController`)
* **Persistencia**: Preference DataStore
* **Concurrencia**: Corrutinas de Kotlin y `StateFlow` / `collectAsState`
* **Control de Versiones**: Git & GitHub (Commits atómicos organizados por capas)

---

## Requisitos e Instalación

1. Clonar el repositorio:
   ```bash
   git clone [https://github.com/TU_USUARIO/ProyectoPuc_v10.git](https://github.com/TU_USUARIO/ProyectoPuc_v10.git)