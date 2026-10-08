---

### ARCHIVO 6: `6_Mockups_y_Flujo_de_Navegacion.md`

```markdown
# 6. Mockups de la Aplicación y Flujo de Navegación

## 6.1 Flujo Principal de Navegación Demostrable
El flujo priorizado para la demostración del MVP recorre el siguiente camino:

[Inicio / HomeScreen]
↓
[Catálogo / CatalogScreen] ──(Aplicar Filtro "Proyectos")──> [Lista Filtrada]
↓
[Confirmación Formulario] <──(Enviar)── [Formulario Contacto] <── [Detalle Proyecto]
↓
[Perfil del Integrante]

---

## 6.2 Representación de Pantallas Principales (Mockups Layout)

### Pantalla 1: Home / Catálogo Unificado (`CatalogScreen`)

+---------------------------------------------------+
|  [≡] CIM UC - Exploración                  [🔍]   |
+---------------------------------------------------+
| Categorías: [Todas] [Proyectos] [Capacidades I+D] |
+---------------------------------------------------+
| +-----------------------------------------------+ |
| | [ Imagen Proyecto ]                           | |
| | Torre Edificación Sustentable Madera          | |
| | Tipo: PROYECTO | Área: Estructuras            | |
| | Líder: Dr. Juan Pérez                         | |
| +-----------------------------------------------+ |
| +-----------------------------------------------+ |
| | [ Imagen Capacidad ]                          | |
| | Ensayo de Resistencia Mecánica                | |
| | Tipo: CAPACIDAD I+D | Área: Materiales        | |
| +-----------------------------------------------+ |
+---------------------------------------------------+
|  [🏠 Inicio]      [⭐ Favoritos]    [📧 Contacto] |
+---------------------------------------------------+

### Pantalla 2: Detalle del Contenido (`DetailScreen`)

+---------------------------------------------------+
|  [← Volver]    Detalle de Proyecto         [⭐]   |
+---------------------------------------------------+
| +-----------------------------------------------+ |
| |                                               | |
| |            IMAGEN DEL PROYECTO                | |
| |                                               | |
| +-----------------------------------------------+ |
|                                                   |
| Torres de Madera en Altura                        |
| Estado: En Ejecución | Año: 2025                  |
|                                                   |
| Descripción:                                      |
| Proyecto orientado al desarrollo de sistemas      |
| estructurales en madera laminada cruzada (CLT)... |
|                                                   |
| Responsable:                                      |
| [ 👤 Dr. Juan Pérez (Ver Perfil) > ]              |
|                                                   |
| +-----------------------------------------------+ |
| |     [ BUTTON: Solicitar Colaboración ]        | |
| +-----------------------------------------------+ |
+---------------------------------------------------+

### Pantalla 3: Perfil del Integrante (`MemberDetailScreen`)

+---------------------------------------------------+
|  [← Volver]    Perfil de Integrante               |
+---------------------------------------------------+
|                  [ 👤 Foto ]                      |
|                Dr. Juan Pérez                     |
|     Investigador Senior - Área Estructuras        |
|          contacto.simulado@cimuc.cl               |
+---------------------------------------------------+
| Proyectos e Iniciativas Asociadas:                |
|                                                   |
| * Torres de Madera en Altura (2025)               |
| * Caracterización Mecánica de Pino Radiata (2024) |
|                                                   |
+---------------------------------------------------+

### Pantalla 4: Formulario de Contacto Simulado (`ContactScreen`)


+---------------------------------------------------+
|  [← Volver]    Solicitar Colaboración             |
+---------------------------------------------------+
| Asunto: Consulta sobre "Torres de Madera"         |
|                                                   |
| Nombre Completo:                                  |
| [ Juan Silva                                    ] |
|                                                   |
| Correo Electrónico:                               |
| [ jsilva@empresa.cl                             ] |
|                                                   |
| Mensaje o Requerimiento:                          |
| [ Quisiera solicitar información sobre...       ] |
| [                                               ] |
|                                                   |
| +-----------------------------------------------+ |
| |          [ BUTTON: Enviar Solicitud ]         | |
| +-----------------------------------------------+ |
+---------------------------------------------------+

