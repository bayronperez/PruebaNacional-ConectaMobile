# ConectaMobile

*ConectaMobile* es una aplicación de conectividad intuitiva que permite a los usuarios gestionar conexiones en tiempo real utilizando tecnologías modernas como **Firebase** y **MQTT**. Diseñada para ser ligera y eficiente, *ConectaMobile* es ideal para aplicaciones de comunicación, monitoreo y sincronización entre dispositivos.

## Descripción General

*ConectaMobile* permite a los usuarios gestionar conexiones de manera eficiente mediante **Firebase** para almacenamiento y autenticación, y **MQTT** para comunicaciones en tiempo real. La aplicación combina un diseño limpio con funcionalidades robustas para brindar una experiencia confiable y fluida.

## Funcionalidades

- *Inicio de Sesión y Registro*: Permite a los usuarios autenticarse de manera segura utilizando **Firebase Authentication**.
- *Conexión en Tiempo Real*: Soporte para **MQTT**, permitiendo comunicación rápida entre dispositivos conectados.
- *Gestión de Sesiones*: Integración con **Firebase** para almacenamiento seguro y persistencia de datos.
- *Interfaz de Usuario Intuitiva*: Diseñada para facilitar la navegación y operación de los usuarios.
- *Compatibilidad Extensa*: Compatible con dispositivos Android API 32 o superior.

## Base de Datos y Comunicación

*ConectaMobile* utiliza una infraestructura moderna y confiable para sus funcionalidades principales:

- *Firebase*:
  - Autenticación de usuarios.
  - Almacenamiento de datos en la nube.
- *MQTT*:
  - Protocolo ligero para comunicación en tiempo real entre dispositivos.

## Tecnologías y Herramientas

- *Lenguaje*: Java
- *IDE*: Android Studio
- *Protocolo*: MQTT para comunicación en tiempo real.
- *Framework de Backend*: Firebase para autenticación y sincronización.
- *Gradle*: Configuración con Kotlin DSL (`build.gradle.kts`).
- *Compatibilidad*: Android API 32 o superior.

## Estructura del Proyecto

- **app**: Contiene el código fuente de la aplicación, organizado de la siguiente manera:
  - **src/main/java**: Código fuente principal.
  - **res/layout**: Diseños XML de la interfaz de usuario.
  - **res/values**: Archivos XML para colores, estilos y cadenas.
  - **AndroidManifest.xml**: Configuración global de la aplicación.
  - **google-services.json**: Archivo necesario para la integración con Firebase.
- **Configuración del Proyecto**:
  - **build.gradle.kts**: Configuración específica del módulo.
  - **settings.gradle.kts**: Configuración global de módulos.

## Instalación y Ejecución

1. Clona el repositorio:
   ```bash
   git clone https://github.com/bayronperez/PruebaNacional-ConectaMobile.git

