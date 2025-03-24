# KotlinPostgreSQLJDBC

Este proyecto es una plantilla en Kotlin que se conecta a una base de datos PostgreSQL (esquema `ds2`) usando JDBC. 
Está diseñado para aprender y practicar operaciones CRUD sobre BBDD relacionales.

## Estructura del Proyecto

```
src/main/kotlin/
├── Database.kt            # Clase de conexión a PostgreSQL
├── Main.kt                # Punto de entrada con ejemplos CRUD
├── CustomerService.kt     # CRUD para la tabla 'customers'
├── ProductService.kt      # CRUD para la tabla 'products'
└── OrderService.kt        # CRUD para la tabla 'orders'
```

## Requisitos

- IntelliJ IDEA 
- JDK 17 o superior
- PostgreSQL funcionando localmente
- Base de datos `ds2` cargada (usando el archivo `ds2.sql` en el Classroom de M6)

## Configuración

1. Abre el proyecto en IntelliJ IDEA (`File → Open` y selecciona la carpeta del proyecto).
2. Verifica que el archivo `Database.kt` tiene los datos correctos de conexión:

```kotlin
private const val URL = "jdbc:postgresql://localhost:5432/ds2"
private const val USER = "tu_user"
private const val PASSWORD = "tu_contraseña"
```

3. Sincroniza el proyecto si es necesario (`View → Tool Windows → Gradle → Sync`).

## Ejecución

1. Abre el archivo `Main.kt`.
2. Haz clic en el botón verde ▶️ junto a `fun main()`.
3. Por defecto, solo se listan registros. Puedes activar/descomentar otras operaciones CRUD para probarlas.

## ¿Qué puedes practicar?

- Insertar nuevos clientes, productos y pedidos.
- Leer todos los registros de una tabla.
- Actualizar campos como email, nombre o total de pedido.
- Eliminar registros por ID.
- Combinar operaciones para simular un flujo completo de compra.

## Recomendaciones

- Agrega validaciones a nivel de código.
- Crea clases adicionales para otras tablas como `orderlines`, `inventory`, `categories`.
- Implementa funciones que usen JOINs para obtener datos combinados.
- Usa SQL avanzado para consultas agregadas (AVG, GROUP BY).

---

Ahora ya puedes hacer persistente todos los datos de cualquier aplicación que crees en una BBDD relacional. 
