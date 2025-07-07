import json
import csv
import logging
from dataclasses import dataclass, asdict, field
from datetime import datetime, date
from pathlib import Path

import tkinter as tk
from tkinter import messagebox, ttk
from tkcalendar import DateEntry

try:
    from plyer import notification
    HAS_PLYER = True
except ImportError:
    HAS_PLYER = False

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

@dataclass
class Tarea:
    titulo: str
    descripcion: str
    prioridad: str
    fecha_limite: str
    completado: bool = False
    id: int = field(default_factory=lambda: int(datetime.now().timestamp()*1000))

RUTA_DATOS = Path("tareas.json")
class Persistencia:
    @staticmethod
    def cargar() -> list[Tarea]:
        if not RUTA_DATOS.exists():
            return []
        try:
            raw = json.loads(RUTA_DATOS.read_text(encoding='utf-8'))
            return [Tarea(**item) for item in raw]
        except Exception as e:
            logging.error(f"Error al leer JSON: {e}")
            return []
    @staticmethod
    def guardar(tareas: list[Tarea]) -> None:
        try:
            RUTA_DATOS.write_text(
                json.dumps([asdict(t) for t in tareas], indent=2, ensure_ascii=False),
                encoding='utf-8'
            )
        except Exception as e:
            logging.error(f"Error al guardar JSON: {e}")

class Exportador:
    @staticmethod
    def a_csv(tareas: list[Tarea], archivo: str = "tareas_exportadas.csv"):
        try:
            with open(archivo, mode='w', newline='', encoding='utf-8') as f:
                writer = csv.writer(f)
                writer.writerow(["ID","Título","Descripción","Prioridad","Fecha límite","Completado"])
                for t in tareas:
                    writer.writerow([
                        t.id, t.titulo, t.descripcion,
                        t.prioridad, t.fecha_limite,
                        'Sí' if t.completado else 'No'
                    ])
        except Exception as e:
            logging.error(f"Error al exportar CSV: {e}")

class GestorTareasApp(tk.Tk):
    def __init__(self):
        super().__init__()
        self.title("🗂️ Gestor de Tareas Plus+")
        self._setup_window()

        self.themes = {
            'Night': {
                'bg':'#1e1e2e','fg':'#c5c8c6','accent':'#81a2be','accent2':'#b294bb','alt':'#282a2e'
            },
            'Matrix': {
                'bg':'#0b0f0b','fg':'#0f0','accent':'#070','accent2':'#3f0','alt':'#060'
            }
        }
        self.current_theme = 'Night'
        self._setup_styles()

        self.tareas = Persistencia.cargar()
        self.filtered = list(self.tareas)
        self._editing_id = None

        self._create_widgets()
        self._actualizar_treeview()
        self._programar_notificaciones()
        self.protocol("WM_DELETE_WINDOW", self._on_close)

    def _setup_window(self):
        w,h=720,780; x=(self.winfo_screenwidth()-w)//2; y=(self.winfo_screenheight()-h)//2
        self.geometry(f"{w}x{h}+{x}+{y}"); self.minsize(600,650)

    def _setup_styles(self):
        th = self.themes[self.current_theme]
        self.style = ttk.Style(self)
        self.style.theme_use('clam')
        self.configure(bg=th['bg'])

        self.style.configure('Treeview',
                             background=th['bg'], foreground=th['fg'],
                             fieldbackground=th['bg'], rowheight=26,
                             font=('Consolas',10))
        self.style.map('Treeview',
                       background=[('selected', th['accent2'])],
                       foreground=[('selected', 'white')])
        self.style.configure('Treeview.Heading',
                             background=th['accent'], foreground='white',
                             font=('Segoe UI Semibold',11))
        self.row_tags = {'even': {'background': th['bg']}, 'odd': {'background': th['alt']}}

        self.style.configure('Accent.TButton',
                             background=th['accent'], foreground='white',
                             font=('Segoe UI',11,'bold'), padding=6)
        self.style.map('Accent.TButton',
                       background=[('active', th['accent2'])])

    def _switch_theme(self, *_):
        keys = list(self.themes.keys())
        idx = (keys.index(self.current_theme)+1) % len(keys)
        self.current_theme = keys[idx]
        self._setup_styles()
        self._actualizar_treeview()
        for child in self.winfo_children():
            child.configure(bg=self.themes[self.current_theme]['bg'])

    def _create_widgets(self):
        top = ttk.Frame(self, padding=6)
        top.pack(fill='x', padx=12, pady=8)

        ttk.Label(top, text='🔍 Buscar:').pack(side='left')
        self.busq = tk.StringVar(self)
        ttk.Entry(top, textvariable=self.busq, width=24).pack(side='left', padx=6)
        self.busq.trace_add('write', lambda *a: self._filtrar())

        ttk.Label(top, text='🎯 Prioridad:').pack(side='left', padx=(20,0))
        self.fprio = tk.StringVar(self, value='Todas')
        ttk.Combobox(top, textvariable=self.fprio,
                     values=['Todas','Alta','Media','Baja'],
                     width=12, state='readonly').pack(side='left', padx=6)
        self.fprio.trace_add('write', lambda *a: self._filtrar())

        ttk.Button(top, text='🌓 Theme', style='Accent.TButton',
                   command=self._switch_theme).pack(side='right', padx=(0,6))
        ttk.Button(top, text='📥 CSV', style='Accent.TButton',
                   command=lambda: Exportador.a_csv(self.tareas)).pack(side='right', padx=6)

        cols=('ID','Título','Prio','Fecha','Estado')
        self.tree=ttk.Treeview(self, columns=cols, show='headings', selectmode='browse')
        for c in cols:
            self.tree.heading(c, text=c)
            self.tree.column(c, width=80 if c!='Título' else 240, anchor='center')
        self.tree.pack(fill='both',expand=True,padx=12,pady=6)
        self.tree.bind('<<TreeviewSelect>>', self._on_select)

        det=ttk.Labelframe(self, text='📝 Detalle de Tarea', padding=10)
        det.pack(fill='x',padx=12,pady=6)
        self._crear_detalle(det)

        actions=ttk.Frame(self,padding=8)
        actions.pack(fill='x',padx=12,pady=(0,12))
        for txt,cmd in [('➕ Agregar',self._guardar),
                        ('✏️ Editar',self._guardar),
                        ('🗑️ Eliminar',self._eliminar)]:
            ttk.Button(actions, text=txt, style='Accent.TButton', command=cmd).pack(side='left', padx=4)
        ttk.Button(actions, text='✅ Completar', style='Accent.TButton',
                   command=self._completar).pack(side='right')

    def _crear_detalle(self,parent):
        campos=['Título','Descripción','Prioridad','Fecha límite']
        self.entradas={}
        for i,txt in enumerate(campos):
            ttk.Label(parent, text=txt+':').grid(row=i,column=0,sticky='e',padx=4,pady=4)
            if txt=='Fecha límite':
                e=DateEntry(parent, date_pattern='yyyy-MM-dd', state='readonly')
            elif txt=='Prioridad':
                v=tk.StringVar(self, value='Media')
                e=ttk.Combobox(parent, textvariable=v,
                               values=['Alta','Media','Baja'],
                               state='readonly')
            else:
                e=ttk.Entry(parent)
            e.grid(row=i,column=1,sticky='we',padx=4,pady=4)
            self.entradas[txt.lower()]=e
        parent.columnconfigure(1, weight=1)

    def _filtrar(self):
        term=self.busq.get().lower(); prio=self.fprio.get()
        self.filtered=[
            t for t in self.tareas
            if (term in t.titulo.lower() or term in t.descripcion.lower())
            and (prio=='Todas' or t.prioridad==prio)
        ]
        self._actualizar_treeview()

    def _actualizar_treeview(self):
        for iid in self.tree.get_children(): self.tree.delete(iid)
        for idx,t in enumerate(self.filtered):
            est='✅' if t.completado else '❌'
            tag='even' if idx%2==0 else 'odd'
            self.tree.insert('', 'end', iid=t.id,
                             values=(t.id,t.titulo,t.prioridad,t.fecha_limite,est),
                             tags=(tag,))
        for tag,cfg in self.row_tags.items():
            self.tree.tag_configure(tag, **self.row_tags[tag])

    def _on_select(self,event=None):
        sel=self.tree.selection()
        if not sel: return
        t=next((x for x in self.tareas if str(x.id)==sel[0]), None)
        if not t: return
        self._editing_id=t.id
        self.entradas['título'].delete(0,'end');    self.entradas['título'].insert(0,t.titulo)
        self.entradas['descripción'].delete(0,'end');self.entradas['descripción'].insert(0,t.descripcion)
        self.entradas['prioridad'].set(t.prioridad)
        self.entradas['fecha límite'].set_date(datetime.strptime(t.fecha_limite,'%Y-%m-%d'))

    def _get_inputs(self)->Tarea:
        e=self.entradas
        titulo=e['título'].get().strip()
        if not titulo: raise ValueError('Título obligatorio')
        return Tarea(
            titulo,
            e['descripción'].get().strip(),
            e['prioridad'].get(),
            e['fecha límite'].get_date().strftime('%Y-%m-%d')
        )

    def _guardar(self):
        try:
            nt=self._get_inputs()
            if self._editing_id:
                orig=next(x for x in self.tareas if x.id==self._editing_id)
                orig.titulo,orig.descripcion,orig.prioridad,orig.fecha_limite = \
                    nt.titulo,nt.descripcion,nt.prioridad,nt.fecha_limite
                msg='Tarea editada'
            else:
                if any(x.titulo.lower()==nt.titulo.lower() for x in self.tareas):
                    raise ValueError('Tarea duplicada')
                self.tareas.append(nt); msg='Tarea agregada'
            Persistencia.guardar(self.tareas)
            self._reset_inputs(); self._filtrar()
            messagebox.showinfo('✅ Éxito', msg)
            self._editing_id=None
        except Exception as e:
            messagebox.showerror('❌ Error', str(e))

    def _eliminar(self):
        sel=self.tree.selection()
        if not sel: return
        if messagebox.askyesno('⚠️ Confirmar','Eliminar tarea?'):
            self.tareas=[t for t in self.tareas if str(t.id)!=sel[0]]
            Persistencia.guardar(self.tareas)
            self._reset_inputs(); self._filtrar(); self._editing_id=None

    def _completar(self):
        sel=self.tree.selection()
        if not sel: return
        t=next(x for x in self.tareas if str(x.id)==sel[0])
        t.completado=True
        Persistencia.guardar(self.tareas)
        self._reset_inputs(); self._filtrar(); self._editing_id=None

    def _reset_inputs(self):
        for w in self.entradas.values():
            if isinstance(w,ttk.Entry):       w.delete(0,'end')
            elif isinstance(w,DateEntry):     w.set_date(date.today())
            elif isinstance(w,ttk.Combobox):  w.set('Media')

    def _programar_notificaciones(self):
        def chk():
            hoy=date.today()
            for t in self.tareas:
                if not t.completado and datetime.strptime(t.fecha_limite,'%Y-%m-%d').date()<=hoy:
                    self._notificar(t)
            self.after(5*60*1000, chk)
        chk()

    def _notificar(self,t):
        msg=f"⏰ {t.titulo} ({t.fecha_limite})\n{t.descripcion}"
        if HAS_PLYER: notification.notify(title="📌 Tarea Pendiente",message=msg,timeout=5)
        else: messagebox.showwarning('📌 Recordatorio', msg)

    def _on_close(self):
        if messagebox.askyesno('❓ Salir','¿Desea salir?'):
            self.destroy()

if __name__=='__main__':
    GestorTareasApp().mainloop()
