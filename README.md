# 📱 Proyecto Prototipo 2: Intents Android

**Desarrollador:** Ethan Gonzalez  
**Versión Android:** Min API 31 / Target API 36  
**AGP/Gradle:** Versión 9.0.1  

## 🚀 Resumen del Proyecto
Implementación de 8 intents principales en Android con validaciones de entorno, manejo de Threads para procesos asíncronos y acceso seguro al hardware del dispositivo mediante permisos dinámicos.

## 🔗 Intents Implícitos (5)
1. 🗺️ **Mapa:** Ubicación mediante `geo:`.
2. 🌐 **Web:** Sitio web usando `https://`.
3. 📞 **Teléfono:** Marcador mediante `tel:`.
4. ⚙️ **Ajustes:** Configuraciones del sistema de red.
5. 🖼️ **Galería:** Uso de `ACTION_GET_CONTENT`.

## 🔄 Intents Explícitos (3)
1. 📝 **Detalle:** Paso de parámetros por `putExtra()`.
2. ⚙️ **Configuración:** Pantalla con Action Bar y Back Button.
3. ✅ **Formulario:** Se lanza tras espera en un hilo (Thread) secundario y retorna un resultado (`registerForActivityResult`).

## 🔦 Hardware y Servicios Adicionales (2)
1. 💡 **Linterna:** Control del flash (`CameraManager`) con validación de permisos `CAMERA` en tiempo real.
2. 📍 **Ubicación:** Extracción de coordenadas exactas (`LocationManager`) solicitando permisos de GPS y derivando a ajustes si está apagado.
