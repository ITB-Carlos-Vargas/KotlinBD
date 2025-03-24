// AdvancedExercises.kt
// Plantilla de ejercicios CRUD avanzados para la base de datos ds2

fun insertInventory(prodId: Int, stock: Int, sales: Int) {
    val existe = comprobarProducto(prodId)
    if (existe){
        val sql = "INSERT INTO inventory (prod_id, quan_in_stock, sales) VALUES ( ?, ?, ?)"
        Database.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use {
                it.setInt(1, prodId)     // ID del producto
                it.setInt(2, stock)     // Cantidad del producto
                it.setInt(3, sales)      // salas del producto
                it.executeUpdate()
                println("Inventario registrado correctamente.")
            }
        }
    }
    else{
        println("No se puede insertar productos inexistentes")
    }
}

fun updateInventoryStock(prodId: Int, change: Int) {
    val existe = comprobarProducto(prodId)
    if (existe && change >=0){
        val sql = "UPDATE inventory SET quan_in_stock = ? WHERE prod_id = ?"
        Database.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use {
                it.setInt(1, change)
                it.setInt(2, prodId)
                it.executeUpdate()
                println("Stock del inventario actualizado.")
            }
        }
    }
    if (change<0){
        println("No se pude ingresar stock negativo")
    }
}

fun listLowStockProducts() {
    // TODO: Mostrar productos con quan_in_stock < 5 junto con su título
}

fun insertOrderLine(orderId: Int, prodId: Int, quantity: Int) {
    // TODO: Insertar una nueva línea en orderlines con la fecha actual
}

fun listOrderLines(orderId: Int) {
    // TODO: Mostrar líneas del pedido, incluyendo título del producto y cantidad
}

fun deleteCustomerOrders(customerId: Int) {
    // TODO: Eliminar todos los pedidos y líneas de pedido asociados al cliente
}

fun listCustomerPurchaseHistory(customerId: Int) {
    // TODO: Mostrar historial de compras del cliente con JOIN entre cust_hist, products y orders
}

fun insertCategory(categoryName: String) {
    // TODO: Insertar nueva categoría si no existe ya
}

fun deleteCategoryAndProducts(categoryId: Int) {
    // TODO: Eliminar productos de la categoría y luego la categoría si no hay orderlines relacionadas
}