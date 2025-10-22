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
    do {
        mostarMenuMain()

        val opcion = readln()!!

        when(opcion) {
            "1" -> {
                EquipoDAO.listarEquipos().stream()
                    .forEach {
                        println("${it.id_equipo} - ${it.nombre} (${it.año_fundacion}) - Títulos: ${it.titulos} - Facturación: ${it.facturacion}")
                    }
            }
            "2" -> {
                val id = leerEntero("Introduce el id:")
                val equipo = EquipoDAO.consultarEquipoPorId(id)
                if (equipo != null) {
                    println("${equipo.id_equipo} - ${equipo.nombre} (${equipo.año_fundacion}) - Títulos: ${equipo.titulos} - Facturación: ${equipo.facturacion}")
                } else {
                    println("Equipo no encontrado2")
                }
            }
            "3" -> {
                val nombre = leerCadena("Introduce el nombre:")
                val fundacion = leerEntero("Introduce el año de fundación:")
                val titulos = leerEntero("Introduce la cantidad de títulos:")
                val facturacion = leerDouble("Introduce la facturación;")
                val equipo = Equipo(null,nombre,fundacion,titulos, facturacion)
                EquipoDAO.insertarEquipo(equipo)
            }
            "4" -> {
                val id = leerEntero("Introduce el id del quipo a modificar:")
                val equipoExistente = EquipoDAO.consultarEquipoPorId(id)
                if (equipoExistente != null) {
                    do {
                        mostrarMenuUpdate()
                        val opcionUpdate = readln()
                        when(opcionUpdate) {
                            "1" -> {
                                val nombre = leerCadena("Introduce un nuevo nombre")
                                val equipoCopia = equipoExistente.copy(nombre = nombre)
                                EquipoDAO.actualizarEquipo(equipoCopia)
                            }
                            "2" -> {
                                val fundacion = leerEntero("Introduce el nuevo año de fundación:")
                                val equipoCopia = equipoExistente.copy(año_fundacion = fundacion)
                                EquipoDAO.actualizarEquipo(equipoCopia)
                            }
                            "3" -> {
                                val tiutlos = leerEntero("Introduce la nueva cantidad de títulos:")
                                val equipoCopia = equipoExistente.copy(titulos = tiutlos)
                                EquipoDAO.actualizarEquipo(equipoCopia)
                            }
                            "4" -> {
                                val facturacion = leerDouble("Introduce la nueva facturación")
                                val equipoCopia = equipoExistente.copy(facturacion = facturacion)
                                EquipoDAO.actualizarEquipo(equipoCopia)
                            }
                            "5" -> {
                                println("")
                            }
                            else -> println("Opción no válida")
                        }

                    } while (opcionUpdate != "5")
                } else {
                    println("No se ha encontrado un equipo con ese id")
                }
            }
            "5" -> {
                val id = leerEntero("Introduce el id:")
                EquipoDAO.eliminarEquipo(id)
            }
            "6" -> {
                println("Saliendo del programa....")
            }
            else -> {
                println("Opción no válida")
            }
        }


    } while (opcion != "6")
}

fun mostarMenuMain() {
    println("1. Mostrar equipos\n2. Consultar equipo por ID\n3. Insertar equipo\n4. Actualizar equipo\n5. Eliminar equipo\n6. Salir")
}

fun mostrarMenuUpdate() {
    println("1. Nombre\n2. Año fundación\n3. Títulos\n4. Facturación\n5. Salir")
}

fun leerEntero(mensaje: String): Int {

    while(true) {

        println(mensaje)

        try {
            return readLine()!!.toInt()
        } catch (e: NumberFormatException) {
            println("Formato no válido")
        }
    }

}

fun leerCadena(mensaje: String): String {
    var valido = false
    var cadena = ""

    while (!valido) {

        println(mensaje)

        cadena = readLine()!!

        if (cadena.isNotEmpty()) {
            valido = true
        } else {
            println("La cadena no puede estar vacía")
        }

    }

    return cadena
}

fun leerDouble(mensaje: String): Double {

    while(true) {

        println(mensaje)

        try {
            return readLine()!!.toDouble()
        } catch (e: NumberFormatException) {
            println("Formato no válido")
        }
    }

}