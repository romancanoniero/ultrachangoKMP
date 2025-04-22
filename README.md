# UltraChango2

## Descripción
UltraChango2 es una aplicación multiplataforma desarrollada con Kotlin Multiplatform (KMP) y Compose Multiplatform, diseñada para ayudar a los usuarios a gestionar sus compras familiares y encontrar los mejores precios.

## Características Principales
- Gestión de listas de compras familiares
- Comparación de precios entre diferentes supermercados
- Escaneo de códigos de barras para búsqueda rápida de productos
- Compartir listas de compras con familiares
- Seguimiento de precios y ofertas
- Integración con PreciosClaros para datos de productos

## Arquitectura
El proyecto sigue una arquitectura MVVM con las siguientes capas:

### Capa de Presentación
- **ViewModels**: Manejan la lógica de negocio y el estado de la UI
- **Composables**: Componentes de UI reutilizables
- **Screens**: Pantallas principales de la aplicación

### Capa de Dominio
- **Use Cases**: Lógica de negocio específica
- **Models**: Modelos de datos del dominio

### Capa de Datos
- **Repositories**: Acceso a datos locales y remotos
- **APIs**: Integración con servicios externos
- **Database**: Persistencia local con Room

## Tecnologías Utilizadas
- Kotlin Multiplatform (KMP)
- Compose Multiplatform
- Material 3
- Koin para inyección de dependencias
- Room para persistencia de datos
- Coroutines para operaciones asíncronas
- Flow para manejo de estado reactivo

## Estructura del Proyecto
```
composeApp/
├── src/
│   ├── commonMain/
│   │   ├── kotlin/
│   │   │   └── com/iyr/ultrachango/
│   │   │       ├── ui/           # Componentes de UI
│   │   │       ├── data/         # Repositorios y modelos
│   │   │       ├── utils/        # Utilidades
│   │   │       ├── APIs/         # Integración con APIs
│   │   │       ├── viewmodels/   # ViewModels
│   │   │       ├── di/           # Inyección de dependencias
│   │   │       └── auth/         # Autenticación
│   └── androidMain/              # Código específico de Android
│   └── iosMain/                  # Código específico de iOS
```

## Configuración del Entorno
1. Requisitos:
   - Android Studio Hedgehog | 2023.1.1 o superior
   - Xcode 15.0 o superior (para iOS)
   - Kotlin 1.9.0 o superior
   - Gradle 8.0 o superior

2. Configuración:
   ```bash
   # Clonar el repositorio
   git clone [url-del-repositorio]
   
   # Instalar dependencias
   ./gradlew build
   ```

## Guía de Contribución
1. Fork del repositorio
2. Crear una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit de tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request

## Convenciones de Código
- Nombres de clases en PascalCase
- Nombres de variables y funciones en camelCase
- Constantes en UPPER_SNAKE_CASE
- Paquetes en minúsculas
- Documentación con KDoc para clases y métodos públicos

## Licencia
Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

This is a Kotlin Multiplatform project targeting Android, iOS.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that's common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple's CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you're sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…