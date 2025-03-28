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
    val existencia = comprobarCategoryName(categoryName)
    if (!existencia){
        val categoria = ultimaCategoria() +1 //obtenemos la ultima categoria  y le sumamos 1
        val sql = "INSERT INTO categories (category, categoryname) VALUES (?, ?)"
        Database.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use {
                it.setInt(1, categoria)       // ID del la categoria
                it.setString(2, categoryName)     // nombre de la categoria
                it.executeUpdate()
                println("Categoria registrada correctamente.")
            }
        }
    }

}

fun deleteCategoryAndProducts(categoryId: Int) {
    val sql = "select prod_id from products p where category = $categoryId"
    //primero comprobar si existen orderlines con productos con la categoria asignada
    val existencia = comprobarProductoOrderline(categoryId)
    //borrar productos
    if (!existencia){
        Database.getConnection()?.use { conn ->
            conn.createStatement().use {
                val rs = it.executeQuery(sql)
                while (rs.next()) {
                    val producto = rs.getInt("prod_id")
                    deleteProduct(producto)
                }
            }
        }
        //borrar la categoria
        deleteCategory(categoryId)
    }
    else{
        println("Existen orderLines con productos con la categoria $categoryId")
    }
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

fun ultimaCategoria():Int{
    var categoria = 0
    val sql = "select category from categories c order by category desc limit 1"
    Database.getConnection()?.use { conn ->
        conn.createStatement().use {
            val rs = it.executeQuery(sql)
            while (rs.next()) {
                categoria = rs.getInt("category")
            }
        }
    }

    return categoria
}

fun comprobarCategoryName(name:String):Boolean{
    var result = false
    var nombre = ""
    val sql = "select categoryname from categories c where categoryname  = '$name'"
    Database.getConnection()?.use { conn ->
        conn.createStatement().use {
            val rs = it.executeQuery(sql)
            while (rs.next()) {
                nombre = "${rs.getInt("categoryname")}"
            }
        }
    }
    if (nombre != ""){
        result = true
    }
    else{
        println("CategoryName no  existente")
    }
    return result
}

fun comprobarProductoOrderline(categoryId: Int):Boolean{
    var result = false
    var orderLine = ""
    val sql = "select o.orderlineid from products p inner join orderlines o on o.prod_id  = p.prod_id " +
            "where p.category = $categoryId and o.orderlineid is not null limit 1"
    Database.getConnection()?.use { conn ->
        conn.createStatement().use {
            val rs = it.executeQuery(sql)
            while (rs.next()) {
                orderLine = "${rs.getInt("orderlineid")}"
            }
        }
    }
    if (orderLine != ""){
        result = true
    }
    return result
}


fun deleteCategory(categoryId: Int){
    val sql = "DELETE FROM categories WHERE category = ?"
    Database.getConnection()?.use { conn ->
        conn.prepareStatement(sql).use {
            it.setInt(1, categoryId)
            it.executeUpdate()
            println("Categoria $categoryId eliminado.")
        }
    }
}


