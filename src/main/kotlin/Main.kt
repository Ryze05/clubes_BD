package org.example

import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

/*fun main() {
    val dbPath = "datos/clubes.sqlite"
    val dbFile = File(dbPath)
    println("Ruta de la BD: ${dbFile.absolutePath}")
    val url = "jdbc:sqlite:${dbFile.absolutePath}"

    DriverManager.getConnection(url).use { conn ->
        println("Conexión establecida correctamente con SQLite.")
    }
}*/

val dbPath = "src/main/resources/clubes.sqlite"
val dbFile = File(dbPath)
val url = "jdbc:sqlite:${dbFile.absolutePath}"

fun conectarBD(): Connection? {
    return try {
        DriverManager.getConnection(url)
    } catch (e: SQLException) {
        e.printStackTrace()
        null
    }
}

fun main() {
    /*val conn = conectarBD()
    if (conn != null) {
        println("Conectado a la BD correctamente.")
        Conn.closeConnection(conn)
    }*/

    conectarBD()?.use { conn ->
        println("Conectado a la BD")

        conn.createStatement().use { stmt ->
            stmt.executeQuery("SELECT * FROM equipo").use { rs ->
                while (rs.next()) {
                    println(rs.getString("nombre"))
                }
            }
        }
    } ?: println("No se pudo conectar")
}