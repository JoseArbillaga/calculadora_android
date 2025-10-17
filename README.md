# Calculadora de Propinas para Empleados

Una aplicación Android moderna para distribuir propinas de manera justa entre empleados, considerando días trabajados, semana laboral completa y adelantos.

## 🚀 Características

- **Distribución proporcional**: Calcula propinas basada en días trabajados
- **Semana laboral completa**: Empleados con semana completa reciben distribución del sobrante
- **Manejo de adelantos**: Descuenta automáticamente adelantos del total final
- **Interfaz intuitiva**: 7 pantallas paso a paso con Material Design 3
- **Validación completa**: Verificación de datos en tiempo real

## 📱 Compatibilidad

- **Android mínimo**: 5.0 (API 21)
- **Android objetivo**: 14 (API 34)
- **Cobertura**: 99%+ de dispositivos Android

## 🧮 Lógica de Cálculo

1. **Total semanal**: Suma de propinas de todos los días
2. **Total diario**: Total semanal ÷ días de la semana
3. **Total diario por persona**: Total diario ÷ número de empleados
4. **Propina base**: Total diario por persona × días trabajados
5. **Sobrante**: Se distribuye entre empleados con semana completa (≥ días definidos)
6. **Total final**: Propina base + bono (si aplica) - adelantos

### Ejemplo:
- **Semana**: 7 días, **Semana completa**: 6 días
- **Total semanal**: $700
- **10 empleados**: $70 diario, $7 por persona/día
- **Empleado con 6 días**: $42 base + bono del sobrante
- **Empleado con 4 días**: $28 base (sin bono)

## 📋 Flujo de la Aplicación

1. **Empleados**: Ingresa número total de empleados
2. **Nombres**: Registra nombre de cada empleado
3. **Días trabajados**: Especifica días trabajados por empleado
4. **Configuración**: Define días totales de semana y semana completa
5. **Adelantos**: Registra adelantos solicitados (si aplica)
6. **Montos diarios**: Ingresa propinas recibidas cada día
7. **Resultados**: Visualiza distribución detallada por empleado

## 🛠️ Tecnologías

- **Lenguaje**: Java
- **UI Framework**: Material Design 3
- **IDE**: Android Studio
- **Build System**: Gradle
- **Compatibilidad**: AndroidX

## 📥 Instalación

1. Descarga el APK desde [Releases](../../releases)
2. Habilita "Fuentes desconocidas" en tu dispositivo Android
3. Instala el APK
4. ¡Listo para usar!

## 🔧 Desarrollo

### Prerrequisitos
- Android Studio Arctic Fox o superior
- JDK 11+
- Android SDK API 21+

### Configuración
```bash
git clone https://github.com/tuusuario/calculadora-propinas.git
cd calculadora-propinas
# Abrir en Android Studio
```

### Compilación
```bash
./gradlew assembleDebug
```

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para detalles.

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Para cambios importantes:

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📞 Contacto

**Autor**: JoseArbillaga
**Email**: josearbillaga@gmail.com
**Proyecto**: [https://github.com/tuusuario/calculadora-propinas](https://github.com/tuusuario/calculadora-propinas)

---
⭐ Si este proyecto te ayudó, ¡dale una estrella en GitHub!
