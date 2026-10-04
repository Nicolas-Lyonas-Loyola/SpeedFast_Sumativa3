# SpeedFast - Sumativa 3

Proyecto desarrollado para la asignatura **PRY2203 - Desarrollo Orientado a Objetos II** de DUOC UC.

La aplicación permite gestionar de forma persistente repartidores, pedidos y entregas utilizando Java Swing, JDBC y MySQL.

## Tecnologías utilizadas

- Java JDK 21
- IntelliJ IDEA Community Edition
- MySQL Server 8.4
- MySQL Workbench
- MySQL Connector/J 26.7.0
- JDBC
- Java Swing
- Git

## Funcionalidades

### Gestión de Repartidores

- Registrar repartidores.
- Listar repartidores almacenados en MySQL.
- Actualizar repartidores.
- Eliminar repartidores.
- Validar nombre obligatorio.
- Validar un máximo de 100 caracteres.

### Gestión de Pedidos

- Registrar pedidos.
- Editar pedidos.
- Eliminar pedidos.
- Listar pedidos.
- Seleccionar tipo:
    - COMIDA
    - ENCOMIENDA
    - EXPRESS
- Seleccionar estado:
    - PENDIENTE
    - EN_REPARTO
    - ENTREGADO
- Validar dirección obligatoria.
- Validar un máximo de 100 caracteres.

### Gestión de Entregas

- Registrar entregas asociando un Pedido y un Repartidor.
- Registrar fecha y hora.
- Listar entregas.
- Actualizar entregas.
- Eliminar entregas.
- Filtrar entregas por Pedido.
- Filtrar entregas por Repartidor.
- Mostrar todas las entregas.
- Validar formato de fecha.
- Validar formato de hora.
- Cargar Pedido y Repartidor desde la base de datos mediante JComboBox.

## Arquitectura del proyecto

El proyecto está organizado mediante separación por responsabilidades:

```text
cl.duoc.speedfast
├── dao
│   ├── EntregaDAO
│   ├── PedidoDAO
│   ├── RepartidorDAO
│   └── impl
│       ├── EntregaDAOImpl
│       ├── PedidoDAOImpl
│       └── RepartidorDAOImpl
├── main
│   └── Main
├── model
│   ├── Entrega
│   ├── EstadoPedido
│   ├── Pedido
│   ├── Repartidor
│   └── TipoPedido
├── util
│   └── ConexionDB
└── view
    ├── PanelEntregas
    ├── PanelPedidos
    ├── PanelRepartidores
    └── VentanaPrincipal
```

Además, el proyecto contiene:

```text
database/
└── speedfast_db.sql
```

El archivo `speedfast_db.sql` permite crear la base de datos y las tablas necesarias para ejecutar la aplicación.

## Base de datos

La aplicación utiliza la base de datos:

```text
speedfast_db
```

con las siguientes tablas:

- `repartidores`
- `pedidos`
- `entregas`

Para crear la base de datos desde cero, ejecutar:

```text
database/speedfast_db.sql
```

en MySQL Workbench o mediante consola MySQL.

## Configuración de MySQL

La aplicación utiliza los siguientes parámetros:

```text
Host: localhost
Puerto: 3306
Base de datos: speedfast_db
Usuario: root
```

La contraseña **no se encuentra escrita en el código fuente ni en el repositorio**.

Se obtiene mediante la variable de entorno:

```text
MYSQL_PASSWORD
```

### Configuración en IntelliJ IDEA

Ir a:

```text
Run
→ Edit Configurations
→ Environment variables
```

Agregar:

```text
MYSQL_PASSWORD = contraseña_del_usuario_mysql
```

No escribir la contraseña directamente en `ConexionDB.java`.

## MySQL Connector/J

El proyecto requiere el driver:

```text
mysql-connector-j-26.7.0.jar
```

En IntelliJ IDEA debe agregarse mediante:

```text
File
→ Project Structure
→ Libraries
→ +
→ Java
```

y seleccionar el archivo `.jar` correspondiente.

## Ejecución

1. Iniciar MySQL Server.
2. Crear la base de datos utilizando `database/speedfast_db.sql`.
3. Configurar la variable de entorno `MYSQL_PASSWORD`.
4. Agregar MySQL Connector/J al proyecto.
5. Abrir el proyecto en IntelliJ IDEA.
6. Ejecutar la clase:

```text
cl.duoc.speedfast.main.Main
```

## Persistencia JDBC

Cada entidad dispone de operaciones CRUD:

```text
create()
readAll()
update()
delete()
```

Las operaciones utilizan:

- `Connection`
- `PreparedStatement`
- `ResultSet`
- `try-with-resources`

para gestionar el acceso y cierre de recursos JDBC.

## Interfaz gráfica

La aplicación fue desarrollada utilizando Java Swing e incluye:

- `JFrame`
- `JPanel`
- `JTabbedPane`
- `JTable`
- `JTextField`
- `JComboBox`
- `JButton`
- `JOptionPane`

La interfaz se encuentra conectada con las clases DAO y actualiza los datos mostrados después de las operaciones CRUD.

Los formularios están organizados en tres pestañas:

- Repartidores
- Pedidos
- Entregas

## Validaciones

La aplicación valida los datos antes de enviarlos a la base de datos.

Entre las validaciones implementadas se encuentran:

- Nombre de repartidor obligatorio.
- Nombre de repartidor con máximo de 100 caracteres.
- Dirección de pedido obligatoria.
- Dirección de pedido con máximo de 100 caracteres.
- Selección de Pedido para registrar una Entrega.
- Selección de Repartidor para registrar una Entrega.
- Fecha obligatoria y con formato válido.
- Hora obligatoria y con formato válido.

## Manejo de errores

La aplicación maneja errores relacionados con las operaciones sobre la base de datos.

También considera las restricciones de claves foráneas.

Por ejemplo, MySQL impide eliminar un Pedido o Repartidor que esté asociado a una Entrega existente.

En esos casos, la aplicación informa el problema al usuario mediante mensajes claros utilizando `JOptionPane`.

## Relaciones entre entidades

La tabla `entregas` relaciona:

- un Pedido existente;
- un Repartidor existente.

Los `JComboBox` de la pestaña Entregas se cargan utilizando los registros almacenados en MySQL.

El usuario visualiza información legible como:

```text
4 - Av. Apoquindo 3000
1 - Camila
```

mientras que internamente la aplicación conserva los identificadores correspondientes.

Los combos se refrescan cuando el usuario vuelve a la pestaña Entregas para reflejar cambios realizados en Pedidos o Repartidores.

## Filtros de Entregas

La pestaña Entregas permite:

- Filtrar por Pedido.
- Filtrar por Repartidor.
- Mostrar todas las entregas.

Los filtros utilizan los datos obtenidos desde la base de datos y actualizan el contenido del `JTable`.

## Control de versiones

El proyecto utiliza Git para mantener un historial ordenado del desarrollo.

Los principales hitos del proyecto incluyen:

- Implementación del modelo y persistencia JDBC.
- Implementación de los DAO con CRUD completo.
- Integración de la interfaz Swing con la base de datos.
- Incorporación de validaciones.
- Implementación de filtros y manejo de relaciones.

## Autor

**Nicolás Loyola**

DUOC UC  
Analista Programador Computacional  
PRY2203 - Desarrollo Orientado a Objetos II