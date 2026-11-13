# CIM UC Explora · App Android

![Kotlin](https://img.shields.io/badge/Kotlin-2.2-7F52FF?logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4?logo=jetpackcompose&logoColor=white)
![Android](https://img.shields.io/badge/Android-minSdk%2026%20·%20targetSdk%2037-3DDC84?logo=android&logoColor=white)
![Arquitectura](https://img.shields.io/badge/arquitectura-MVVM-blue)
![Estado](https://img.shields.io/badge/estado-MVP-orange)

Aplicación móvil para explorar los proyectos, capacidades de I+D y publicaciones del **Centro UC de Innovación en Madera (CIM UC)**, y conectar a empresas, academia y sector público con el equipo del centro.

> [!NOTE]
> Es un **MVP funcional sin backend**: todos los contenidos son ficticios y viven en memoria. Los formularios, el escáner QR y los envíos son simulados; la app no requiere internet ni permisos especiales.

---

## Tabla de contenidos

- [Funcionalidades](#funcionalidades)
- [Navegación](#navegación)
- [Arquitectura](#arquitectura)
- [Modelo de datos](#modelo-de-datos)
- [Stack tecnológico](#stack-tecnológico)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Cómo ejecutar](#cómo-ejecutar)
- [Próximos pasos](#próximos-pasos)
- [Autor](#autor)

---

## Funcionalidades

| Funcionalidad | Descripción |
|---|---|
| **Onboarding "Matchmaking"** | El usuario elige su perfil (Empresa, Academia o Sector Público) y la app personaliza las recomendaciones de inicio. |
| **Inicio** | Recomendados para el perfil elegido, categorías por tipo de contenido, novedades recientes y buscador. |
| **Explorar** | Catálogo con búsqueda de texto (sin distinguir tildes ni mayúsculas) y filtros por tipo de contenido y área temática: Ingeniería Estructural, Construcción Industrializada, Sustentabilidad y Normalización. |
| **Detalle** | Descripción, año, estado (En curso, Finalizado, Publicado), integrantes asociados y botón para contactar al equipo. |
| **Relaciones** | Al tocar un integrante, un *bottom sheet* muestra sus otras iniciativas (relación muchos a muchos Proyecto ↔ Integrante). |
| **Favoritos** | Guardar contenidos con ♡ y verlos en "Mis guardados". |
| **Modo Feria** | Simula el escaneo del QR de un stand y guarda un contenido en favoritos, pensado para ferias y eventos. |
| **Novedades** | Noticias del centro con resumen expandible sin cambiar de pantalla. |
| **Contacto** | Datos del centro y formulario de solicitud con validación de campos obligatorios y diálogo de confirmación. |

## Navegación

```
Onboarding (perfil)
      │
      ▼
┌───────────── Barra inferior ─────────────┐
│  Inicio   │  Explorar  │ Novedades │ Contacto │
└─────┬─────┴─────┬──────┴───────────┴──────────┘
      │           │
      └─────┬─────┘
            ▼
         Detalle ──► Relaciones del integrante (bottom sheet)
```

La profundidad máxima es **Inicio → Explorar → Detalle**, para que el usuario nunca se pierda. Explorar recibe el tipo de contenido y el texto de búsqueda como argumentos de ruta (`explorar?tipo=…&q=…`), así que desde Inicio se puede abrir ya filtrado.

## Arquitectura

**MVVM** con estado unidireccional:

- **UI (Compose)**: pantallas que observan el estado de su ViewModel y le envían los eventos del usuario; solo guardan estado local de interfaz (diálogos, expansiones).
- **ViewModels**: uno por pantalla (`InicioViewModel`, `ExplorarViewModel`, `DetalleViewModel`, `NovedadesViewModel`, `ContactoViewModel`) que exponen `StateFlow`.
- **`AppViewModel`**: estado de la sesión compartido a nivel de Activity (perfil elegido y favoritos).
- **`ProyectoRepository`**: repositorio en memoria con los contenidos, integrantes y novedades; es el único punto que habría que reemplazar para conectar un backend real.

## Modelo de datos

```
Proyecto ───< ProyectoIntegrante >─── Integrante
   │
   ├── tipoContenido: PROYECTO | CAPACIDAD_ID | PUBLICACION
   ├── areaTematica, anoEjecucion, estadoProyecto
   └── perfilesAfines: EMPRESA | ACADEMIA | SECTOR_PUBLICO

Novedad (titulo, resumen, fecha, imagen)
```

El catálogo de ejemplo incluye 26 contenidos, 12 integrantes y 4 novedades.

## Stack tecnológico

| Área | Herramientas |
|---|---|
| Lenguaje | Kotlin 2.2 |
| UI | Jetpack Compose (BOM 2026.02), Material 3, Material Icons Extended |
| Navegación | Navigation Compose |
| Estado | Lifecycle ViewModel, `StateFlow`, Kotlin Coroutines |
| Build | Gradle (Kotlin DSL), Android Gradle Plugin 9.4, version catalog (`libs.versions.toml`) |
| Diseño | Tipografías DM Serif Display e Inter, paleta fija de marca (verdes, café y crema) |
| Pruebas | JUnit 4, Espresso, Compose UI Test |

## Estructura del proyecto

```
app/src/main/java/cl/uc/cimuce/
├── MainActivity.kt
├── data/
│   ├── model/            # Proyecto, Integrante, ProyectoIntegrante, Novedad, PerfilUsuario, TipoContenido, catálogos
│   └── repository/       # ProyectoRepository (datos en memoria, búsqueda y filtros)
└── ui/
    ├── CimUcApp.kt        # Scaffold raíz
    ├── AppViewModel.kt    # Perfil y favoritos de la sesión
    ├── navigation/        # Rutas, NavHost, pestañas y barra inferior
    ├── screens/           # onboarding, inicio (+ Modo Feria), explorar, detalle, novedades, contacto
    ├── components/        # Tarjetas, chips, avatares, diálogos y encabezados reutilizables
    └── theme/             # Colores, tipografía y tema
```

## Cómo ejecutar

**Requisitos:** Android Studio reciente (compatible con AGP 9.4 y `compileSdk 37`), JDK 17 o superior y un emulador o dispositivo con Android 8.0 (API 26) o superior.

1. Clona el repositorio:

   ```bash
   git clone https://github.com/abrahampuente/App-CIM-UC.git
   ```

2. Abre la carpeta en Android Studio y espera la sincronización de Gradle.
3. Selecciona un emulador o dispositivo y ejecuta la configuración **app** (▶).

Desde la terminal:

```bash
./gradlew assembleDebug         # genera el APK de debug (Windows: gradlew.bat assembleDebug)
./gradlew installDebug          # lo instala en el dispositivo conectado
./gradlew test                  # pruebas unitarias
```

## Próximos pasos

- Conectar `ProyectoRepository` a una API REST del centro.
- Persistir favoritos y perfil con DataStore o Room.
- Escaneo QR real con CameraX y ML Kit.
- Envío real del formulario de contacto.
- Pruebas de UI de los flujos principales.

## Autor

**Abraham Puente** — Ingeniería en Informática, Duoc UC
GitHub: [@abrahampuente](https://github.com/abrahampuente)

> Prototipo académico. Los contenidos, nombres y datos de contacto que muestra la app son ficticios y no representan información oficial del CIM UC.
