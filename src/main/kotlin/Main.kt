package org.example

import org.example.PatrocinadorDAO.transaccionPatroCLub
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

val dbPath = "src/main/resources/clubes.sqlite"
val dbFile = File(dbPath)
val url = "jdbc:sqlite:${dbFile.absolutePath}"

fun conectarBD(): Connection? {
    return try {
        val conn = DriverManager.getConnection(url)
        conn.createStatement().use { stmt ->
            stmt.execute("PRAGMA foreign_keys = ON;")
        }
        conn
    } catch (e: SQLException) {
        e.printStackTrace()
        null
    }
}

fun main() {
    do {
        menuMostar(listOf("Ligas", "Clubes", "Jugadores", "Patrocinadores", "Añadir patrocinador a clubes (transaccion)","Salir"))
        println("\nSelecciona una opción:")
        val opcion1 = readln()!!
        println("")

        when(opcion1) {
            "1" -> crudLigas()
            "2" -> crudClubes()
            "3" -> crudJugadores()
            "4" -> crudPatrocinadores()
            "5" -> {
                val idEquipo = leerEntero("Introduce el id del equipo")
                val idPatro = leerEntero("Introducce el id del patrocinador")
                transaccionPatroCLub(idEquipo, idPatro)
            }
            "6" -> println("Saliendo del programa...")
            else -> println("Opción no válida\n")
        }

    } while (opcion1 != "6")
}

fun menuMostar(options: List<String>) {
    options.forEachIndexed { index, option ->
        println("${index+1}. $option")
    }
}

fun crudClubes() {
    do {
        menuMostar(listOf("Mostrar equipos", "Consultar equipo por ID", "Insertar equipo", "Actualizar equipo", "Eliminar equipo", "Salir"))
        println("\nSelecciona una opción:")
        val opcion = readln()!!
        println("")

        when(opcion) {
            "1" -> {
                EquipoDAO.listarEquipos().stream()
                    .forEach {
                        println("${it.id_equipo} - ${it.nombre} (${it.año_fundacion}) - Títulos: ${it.titulos} - Facturación: ${it.facturacion}€ - ID Liga: ${it.id_liga}")
                    }
                println("")
            }
            "2" -> {
                val id = leerEntero("Introduce el id:")
                val equipo = EquipoDAO.consultarEquipoPorId(id)
                if (equipo != null) {
                    println("\n${equipo.id_equipo} - ${equipo.nombre} (${equipo.año_fundacion}) - Títulos: ${equipo.titulos} - Facturación: ${equipo.facturacion}€ - ID Liga: ${equipo.id_liga}\n")
                } else {
                    println("Equipo no encontrado\n")
                }
            }
            "3" -> {
                val nombre = leerCadena("Introduce el nombre:")
                val fundacion = leerEntero("Introduce el año de fundación:")
                val titulos = leerEntero("Introduce la cantidad de títulos:")
                val facturacion = leerDouble("Introduce la facturación:")
                val idLiga = leerEntero("Introduce el ID de la liga:")
                val equipo = Equipo(null,nombre,fundacion,titulos, facturacion, idLiga)
                EquipoDAO.insertarEquipo(equipo)
            }
            "4" -> {
                val id = leerEntero("Introduce el id del quipo a modificar:")
                val equipoExistente = EquipoDAO.consultarEquipoPorId(id)
                if (equipoExistente != null) {
                    do {
                        menuMostar(listOf("Nombre", "Año fundación", "Títulos", "Facturación", "ID liga","Salir"))
                        println("\nSelecciona una opción:")
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
                                val idLIga = leerEntero("Introduce el nuevo ID de liga")
                                val equipoCopia = equipoExistente.copy(id_liga = idLIga)
                                EquipoDAO.actualizarEquipo(equipoCopia)
                            }
                            "6" -> {
                                println("Saliendo...")
                            }
                            else -> println("Opción no válida")
                        }

                    } while (opcionUpdate != "6")

                } else {
                    println("\nNo se ha encontrado un equipo con ese id\n")
                }
            }
            "5" -> {
                val id = leerEntero("Introduce el id:")
                EquipoDAO.eliminarEquipo(id)
            }
            "6" -> {
                println("Saliendo...\n")
            }
            else -> {
                println("Opción no válida")
            }
        }
    } while (opcion != "6")
}

fun crudLigas() {
    do {
        menuMostar(listOf("Mostrar ligas", "Consultar liga por ID", "Insertar liga", "Actualizar liga", "Eliminar liga", "Salir"))
        println("\nSelecciona una opción:")
        val opcion = readln()!!
        println("")

        when(opcion) {
            "1" -> {
                LigaDAO.listarLigas().stream()
                    .forEach {
                        println("${it.id_liga} - ${it.nombre} - País: ${it.pais} - División: ${it.division}")
                    }
                println("")
            }
            "2" -> {
                val id = leerEntero("Introduce el id:")
                val liga = LigaDAO.consultarLigaPorId(id)
                if (liga != null) {
                    println("\n${liga.id_liga} - ${liga.nombre} - País: ${liga.pais} - División: ${liga.division}\n")
                } else {
                    println("\nEquipo no encontrado\n")
                }
            }
            "3" -> {
                val nombre = leerCadena("Introduce el nombre:")
                val pais = leerCadena("Introduce el país:")
                val division = leerCadena("Introduce la división:")

                val liga = Liga(null,nombre,pais,division)
                LigaDAO.insertarLiga(liga)
            }
            "4" -> {
                val id = leerEntero("Introduce el id de la liga a modificar:")
                val ligaExistente = LigaDAO.consultarLigaPorId(id)
                if (ligaExistente != null) {
                    do {
                        println("")
                        menuMostar(listOf("Nombre", "País", "División", "Salir"))
                        println("\nSeleccion a una opción:")
                        val opcionUpdate = readln()
                        when(opcionUpdate) {
                            "1" -> {
                                val nombre = leerCadena("Introduce un nuevo nombre")
                                val ligaCopia = ligaExistente.copy(nombre = nombre)
                                LigaDAO.actualizarLiga(ligaCopia)
                            }
                            "2" -> {
                                val pais = leerCadena("Introduce un nuevo país")
                                val ligaCopia = ligaExistente.copy(pais = pais)
                                LigaDAO.actualizarLiga(ligaCopia)
                            }
                            "3" -> {
                                val div = leerCadena("Introduce una nueva división")
                                val ligaCopia = ligaExistente.copy(division = div)
                                LigaDAO.actualizarLiga(ligaCopia)
                            }
                            "4" -> {
                                println("Saliendo...\n")
                            }
                            else -> println("Opción no válida\n")
                        }

                    } while (opcionUpdate != "4")

                } else {
                    println("\nNo se ha encontrado una liga con ese id\n")
                }
            }
            "5" -> {
                val id = leerEntero("Introduce el id:")
                LigaDAO.eliminarLiga(id)
            }
            "6" -> {
                println("Saliendo...\n")
            }
            else -> {
                println("Opción no válida")
            }
        }
    } while (opcion != "6")
}

fun crudJugadores() {
    do {
        menuMostar(listOf("Mostrar jugadores", "Consultar jugador por ID", "Insertar jugador", "Actualizar jugador", "Eliminar jugador", "Salir"))
        println("\nSelecciona una opción:")
        val opcion = readln()!!
        println("")

        when(opcion) {
            "1" -> {
                JugadorDAO.listarJugadores().stream()
                    .forEach {
                        println("${it.id_jugador} - ${it.nombre} - Fecha de nacimiento: ${it.fecha_nacimiento} - Posicion: ${it.posicion} - ID Equipo: ${it.id_equipo}")
                    }
                println("")
            }
            "2" -> {
                val id = leerEntero("Introduce el id:")
                val jugador = JugadorDAO.consultarJugadorPorId(id)
                if (jugador != null) {
                    println("\n${jugador.id_jugador} - ${jugador.nombre} - Fecha de nacimiento: ${jugador.fecha_nacimiento} - Posicion: ${jugador.posicion} - ID Equipo: ${jugador.id_equipo}\n")
                } else {
                    println("\nJugador no encontrado\n")
                }
            }
            "3" -> {
                val nombre = leerCadena("Introduce el nombre:")
                val nacimiento = leerCadena("Introduce la fecha de nacimiento (YYYY-MM-DD):")
                val posicion = leerCadena("Introduce la posición del jugador:")
                val idEquipo = leerEntero("Introduce el ID del equipo del jugador:")

                val jugador = Jugador(null,nombre,nacimiento, posicion, idEquipo)
                JugadorDAO.insertarJugador(jugador)
            }
            "4" -> {
                val id = leerEntero("Introduce el id del jugador a modificar:")
                val jugadorExistente = JugadorDAO.consultarJugadorPorId(id)
                if (jugadorExistente != null) {
                    do {
                        println("")
                        menuMostar(listOf("Nombre", "Fecha de nacimiento", "Posición", "ID Equipo","Salir"))
                        println("\nSeleccion a una opción:")
                        val opcionUpdate = readln()
                        when(opcionUpdate) {
                            "1" -> {
                                val nombre = leerCadena("Introduce un nuevo nombre:")
                                val jugadorCopia = jugadorExistente.copy(nombre = nombre)
                                JugadorDAO.actualizarJugador(jugadorCopia)
                            }
                            "2" -> {
                                val nacimiento = leerCadena("Introduce una nueva fecha de nacimiento (YYYY-MM-DD):")
                                val jugadorCopia = jugadorExistente.copy(fecha_nacimiento = nacimiento)
                                JugadorDAO.actualizarJugador(jugadorCopia)
                            }
                            "3" -> {
                                val posicion = leerCadena("Introduce una nueva posición:")
                                val jugadorCopia = jugadorExistente.copy(posicion = posicion)
                                JugadorDAO.actualizarJugador(jugadorCopia)
                            }
                            "4" -> {
                                val idEquipo = leerEntero("Introduce un nuevo ID de equipo para el jugador:")
                                val jugadorCopia = jugadorExistente.copy(id_equipo = idEquipo)
                                JugadorDAO.actualizarJugador(jugadorCopia)
                            }
                            "5" -> println("\nSaliendo...\n")
                            else -> println("Opción no válida\n")
                        }

                    } while (opcionUpdate != "5")

                } else {
                    println("\nNo se ha encontrado una liga con ese id\n")
                }
            }
            "5" -> {
                val id = leerEntero("Introduce el id:")
                JugadorDAO.eliminarJugador(id)
            }
            "6" -> {
                println("Saliendo...\n")
            }
            else -> {
                println("Opción no válida")
            }
        }
    } while (opcion != "6")
}

fun crudPatrocinadores() {
    do {
        menuMostar(listOf("Mostrar patrocinadores", "Consultar partrocinador por ID", "Insertar patrocinador", "Actualizar patrocinador", "Eliminar patrocinador", "Salir"))
        println("\nSelecciona una opción:")
        val opcion = readln()!!
        println("")

        when(opcion) {
            "1" -> {
                PatrocinadorDAO.listarPatrocinadores().stream()
                    .forEach {
                        println("${it.id_patrocinador} - ${it.nombre} - Sector: ${it.sector}")
                    }
                println("")
            }
            "2" -> {
                val id = leerEntero("Introduce el id:")
                val patrocinador = PatrocinadorDAO.consultarPatrocinadorPorId(id)
                if (patrocinador != null) {
                    println("\n${patrocinador.id_patrocinador} - ${patrocinador.nombre} - Sector: ${patrocinador.sector}\n")
                } else {
                    println("\nPatrocinador no encontrado\n")
                }
            }
            "3" -> {
                val nombre = leerCadena("Introduce el nombre:")
                val sector = leerCadena("Introduce el sector:")

                val patro = Patrocinador(null,nombre,sector)
                PatrocinadorDAO.insertarPatrocinador(patro)
            }
            "4" -> {
                val id = leerEntero("Introduce el id del patrocinador a modificar:")
                val patroExistente = PatrocinadorDAO.consultarPatrocinadorPorId(id)
                if (patroExistente != null) {
                    do {
                        println("")
                        menuMostar(listOf("Nombre", "Sector","Salir"))
                        println("\nSeleccion a una opción:")
                        val opcionUpdate = readln()
                        when(opcionUpdate) {
                            "1" -> {
                                val nombre = leerCadena("Introduce un nuevo nombre:")
                                val patroCopia = patroExistente.copy(nombre = nombre)
                                PatrocinadorDAO.actualizarPatrocinador(patroCopia)
                            }
                            "2" -> {
                                val sector = leerCadena("Introduce una nuevo sector:")
                                val patroCopia = patroExistente.copy(sector = sector)
                                PatrocinadorDAO.actualizarPatrocinador(patroCopia)
                            }
                            "3" -> println("\nSaliendo...\n")
                            else -> println("Opción no válida\n")
                        }

                    } while (opcionUpdate != "3")

                } else {
                    println("\nNo se ha encontrado un patrocinador con ese id\n")
                }
            }
            "5" -> {
                val id = leerEntero("Introduce el id:")
                PatrocinadorDAO.eliminarPatrocinador(id)
            }
            "6" -> {
                println("Saliendo...\n")
            }
            else -> {
                println("Opción no válida")
            }
        }
    } while (opcion != "6")
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