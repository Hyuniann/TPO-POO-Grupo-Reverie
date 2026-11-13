# Universidad Argentina de la Empresa (UADE)
## Trabajo Práctico Obligatorio de Programación Orientada a Objetos
### Propuesta Elegida: C - Gestión y analítica de torneos de fútbol
### Integrantes:
- Eric Nahuel Song (Legajo: 1218923)
- Ian Hyun Chae (Legajo: 1216749)

---

## 1. Cambios respecto del diseño original
Durante la codificación y puesta a punto de la Etapa 2, nos apartamos mínimamente del diagrama de clases estático inicial para resolver cuestiones prácticas de interacción por consola:
- **Controlador y Asistente en Main:** Se consolidaron las operaciones del menú en `Main.java` utilizando un asistente guiado interactivo para evitar errores operativos en la demo, delegando el manejo global en `ControladorSistemaFutbol`.
- **Persistencia Global TXT:** En lugar de guardar archivos separados por cada evento aislado, se estructuró la salida en un único archivo maestro (`torneo.txt`) que consolida las 3 tablas solicitadas (Posiciones, Partidos con incidencias/estadios, y Estadísticas de Jugadores).
- **Validaciones de Sanciones:** Se ajustó el método `registrarEvento` en la clase `Partido` para validar en tiempo real la acumulación de tarjetas (dos amarillas o roja directa) e impedir la participación de jugadores expulsados.

## 2. Dificultades encontradas
- **Manejo de estados y flujo de partidos:** Al principio costó sincronizar los estados del partido (`PROGRAMADO`, `EN_JUEGO`, `FINALIZADO`) con el menú de la consola para que el usuario no pudiera registrar eventos con el encuentro cerrado. Lo resolvimos encapsulando las restricciones dentro de las reglas de negocio en el modelo.
- **Control de versiones (Git):** Tuvimos algunos cruces menores al sincronizar ramas de trabajo en paralelo (por ejemplo, con los paquetes al mover clases), los cuales resolvimos mediante *pull requests* y revisiones de código previas al *merge* con la rama principal.

## 3. Distribución de tareas
En base al historial de commits del repositorio, el trabajo se dividió de la siguiente manera:
- **Eric Song:** 
  - *Etapa 1:* Diagramas de clases (inicial y final aplicando SOLID/GRASP) y diagrama de secuencia (programar partido).
  - *Etapa 2:* Estructura de carpetas MVC, creación de las enumeraciones y la jerarquía polimórfica de `Persona`. Desarrollo de entidades base (Club, Plantel, Estadio, TernaArbitral). Implementación de la lectura y escritura del archivo `torneo.txt`. Ajustes de reglas de negocio (expulsiones/tarjetas) y retoques finales en el menú de `Main`.
- **Ian Hyun:** 
  - *Etapa 1:* Redacción de requisitos y supuestos, documento de diseño, diagrama de casos de uso y diagrama de secuencia (registrar evento).
  - *Etapa 2:* Creación de la jerarquía de `Evento` y entidades secundarias (Contrato, Patrocinio, Sponsor, Suscriptor). Desarrollo de `Torneo`, `GeneradorReportes` (lógica de analítica y proyecciones) y `GestorNotificaciones`. Armado de las clases de prueba (`/test`), carga de datos iniciales e instanciación de objetos en el `Main`.

## 4. Reflexión del grupo
El desarrollo de este trabajo práctico nos permitió comprender la importancia de llevar un diseño conceptual sólido (UML, principios SOLID y patrones GRASP) directamente a un entorno de código real en Java. Comprobamos que una buena abstracción inicial facilita enormemente la escritura posterior de las clases, el testeo unitario y la resolución de problemas imprevistos durante la etapa de codificación.