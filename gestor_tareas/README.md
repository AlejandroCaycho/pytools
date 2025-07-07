# 🗂️ Gestor de Tareas Plus+

**Gestor de Tareas** es una aplicación de escritorio ligera y moderna para organizar y administrar tus tareas diarias. Cuenta con:

- Interfaz basada en **Tkinter**  
- Calendario integrado (`tkcalendar.DateEntry`)  
- Notificaciones nativas (`plyer`) o emergentes (`messagebox`)  
- Filtros de búsqueda por texto y prioridad  
- Exportación a CSV  
- Temas nocturnos dinámicos (“Night” y “Matrix”)  
- IDs únicos por timestamp  
- Vista de lista con fuente monoespaciada para estilo “dev”

---

## 📌 Características principales

- ➕ Crear / Editar / Eliminar / Completar tareas  
- 📅 Selección de fecha límite con calendario  
- 🎯 Selección de prioridad (Alta, Media, Baja)  
- 🔍 Búsqueda en tiempo real por título o descripción  
- 🎚️ Filtro por prioridad  
- 📤 Exportar todas las tareas a CSV  
- 🌓 Temas dinámicos:  
  - **Night**: tonos grises y azulados  
  - **Matrix**: verde terminal sobre negro  
- 🔔 Notificaciones automáticas cada 5 minutos  
- ✅ No necesitas `alarma.wav`, se usa sonido del sistema

---

## 🚀 Instalación

### 1. Clona el repositorio

```bash
git clone https://github.com/AlejandroCaycho/pytools.git
cd pytools/gestor_tareas
2. (Opcional) Crea y activa un entorno virtual
En Windows:
bash
Copiar
Editar
python -m venv .venv
.venv\Scripts\activate
En Linux/macOS:
bash
Copiar
Editar
python3 -m venv .venv
source .venv/bin/activate
3. Instala las dependencias necesarias
Crea un archivo llamado requirements.txt con este contenido:

txt
Copiar
Editar
tkcalendar>=1.6.1
plyer>=2.0.0
Luego ejecuta:

bash
Copiar
Editar
pip install -r requirements.txt
✅ Nota: No necesitas alarma.wav. Si no tienes sonido personalizado, se usará el sonido por defecto del sistema o un messagebox.

⚙️ Uso
Ejecuta el programa con:

bash
Copiar
Editar
python main.py
🧭 Funcionalidades
➕ Agregar Tarea
Completa los campos requeridos

Selecciona la prioridad (Alta, Media, Baja)

Selecciona una fecha con el calendario

Pulsa Agregar

✏️ Editar Tarea
Selecciona una tarea de la lista

Modifica la información

Pulsa Editar

🗑️ Eliminar / ✅ Completar
Clic derecho sobre una tarea y selecciona Eliminar o Completar

O usa los botones inferiores

🔍 Buscar / 🎯 Filtrar por Prioridad
Escribe texto para buscar por título o descripción

Usa el combo de prioridades para filtrar

📥 Exportar CSV
Pulsa el botón Exportar CSV

Se generará un archivo tareas_exportadas.csv en el mismo directorio

🌓 Cambiar Tema
Alterna entre temas “Night” y “Matrix” en tiempo real

El cambio afecta todos los elementos visuales

🛠️ Estructura del Proyecto
bash
Copiar
Editar
pytools/
└── gestor_tareas/
    ├── main.py             # Código principal de la app
    ├── tareas.json         # Base de datos local generada automáticamente
    ├── requirements.txt    # Dependencias necesarias
    └── README.md           # Este archivo
🙌 Contribuciones
¿Tienes ideas o mejoras? ¡Contribuye!

Haz un fork del repositorio

Crea una nueva rama:

bash
Copiar
Editar
git checkout -b feature/mi-mejora
Realiza tus cambios y haz commit:

bash
Copiar
Editar
git commit -m "feat: agregué una mejora"
Haz push y abre un Pull Request 🚀

📝 Licencia
Este proyecto está bajo la licencia MIT.
Eres libre de usar, modificar y compartir.
Consulta el archivo LICENSE para más detalles.