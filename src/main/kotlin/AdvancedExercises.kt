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
    val sql = "select p.prod_id , p.title , i.quan_in_stock from products p inner join inventory i on i.prod_id = p.prod_id where  i.quan_in_stock  <5 "
    Database.getConnection()?.use { conn ->
        conn.createStatement().use {
            val rs = it.executeQuery(sql)
            while (rs.next()) {
                println("${rs.getInt("prod_id")} - ${rs.getString("title")} (${rs.getInt("quan_in_stock")})")
            }
        }
    }
}

fun insertOrderLine(orderId: Int, prodId: Int, quantity: Int) {
    val orderlineid = ultimaOrderLine_(prodId) +1 //obtenemos la ultima orderline del producto y le sumamos 1
    val sql = "INSERT INTO orderlines (orderlineid, orderid, prod_id, quantity, orderdate) VALUES (?, ?, ?, ?, current_date)"
    Database.getConnection()?.use { conn ->
        conn.prepareStatement(sql).use {
            it.setInt(1, orderlineid)       // ID del la order line
            it.setInt(2, orderId)     // id de la orden
            it.setInt(3, prodId)           // id del producto
            it.setInt(4, quantity)         // cantidad
            it.executeUpdate()
            println("Linea de pedido registrada correctamente.")
        }
    }
}

fun listOrderLines(orderId: Int) {
    val sql = "select o.orderlineid , p.title , o.quantity , o.orderdate from orderlines o inner join products p on o.prod_id  = p.prod_id  where o.orderid  = $orderId"
    Database.getConnection()?.use { conn ->
        conn.createStatement().use {
            val rs = it.executeQuery(sql)
            while (rs.next()) {
                println("${rs.getInt("orderlineid")} - ${rs.getString("title")} - (${rs.getInt("quantity")}) - ${rs.getString("orderdate")}")
            }
        }
    }

}

fun deleteCustomerOrders(customerId: Int) {
    borrarOrderLines(customerId) //borrar primero las orderlines

    val sql = "DELETE FROM orders WHERE customerid = ?"
    Database.getConnection()?.use { conn ->
        conn.prepareStatement(sql).use {
            it.setInt(1, customerId)
            it.executeUpdate()
            println("Pedido eliminado.")
        }
    }
}

fun listCustomerPurchaseHistory(customerId: Int) {
    val sql = "select p.title , o.orderdate , ch.orderid from cust_hist ch inner join products p on p.prod_id = ch.prod_id " +
            "inner join orders o on o.orderid = ch.orderid where ch.customerid  = $customerId"
    Database.getConnection()?.use { conn ->
        conn.createStatement().use {
            val rs = it.executeQuery(sql)
            while (rs.next()) {
                println("${rs.getString("title")} - ${rs.getString("orderdate")} - (${rs.getInt("orderid")})")
            }
        }
    }
}

fun insertCategory(categoryName: String) {
    // TODO: Insertar nueva categoría si no existe ya
}

fun deleteCategoryAndProducts(categoryId: Int) {
    // TODO: Eliminar productos de la categoría y luego la categoría si no hay orderlines relacionadas
}

fun ultimaOrderLine_(prod_id : Int):Int{
    var result = 0
    val sql = "select orderlineid from orderlines where prod_id  = $prod_id order by orderlineid desc limit 1"
    Database.getConnection()?.use { conn ->
        conn.createStatement().use {
            val rs = it.executeQuery(sql)
            while (rs.next()) {
                result = rs.getInt("orderlineid")
            }
        }
    }
    return  result
}

fun borrarOrderLines(customerId: Int){
    val sql = "DELETE FROM orderlines WHERE orderid in (select orderid from orders where  customerid  = ?)"

    Database.getConnection()?.use { conn ->
        conn.prepareStatement(sql).use {
            it.setInt(1, customerId)
            it.executeUpdate()
            println("Orderlines eliminadas eliminado.")
        }
    }
}