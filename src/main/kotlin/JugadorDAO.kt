package org.example

data class Jugador(
    val id_jugador: Int? = null,
    val nombre: String,
    val fecha_nacimiento: String,
    val posicion: String,
    val id_equipo: Int
)

object JugadorDAO {
    fun listarJugadores(): List<Jugador> {
        val lista = mutableListOf<Jugador>()
        conectarBD()?.use { conn ->
            conn.createStatement().use { stmt ->
                stmt.executeQuery("SELECT * FROM jugador").use { rs ->
                    while (rs.next()) {
                        lista.add(
                            Jugador(
                                id_jugador = rs.getInt("id_jugador"),
                                nombre = rs.getString("nombre"),
                                fecha_nacimiento = rs.getString("fecha_nacimiento"),
                                posicion = rs.getString("posicion"),
                                id_equipo = rs.getInt("id_equipo")
                            )
                        )
                    }
                }
            }
        } ?: println("\nNo se pudo establecer la conexión.\n")
        return lista
    }

    fun consultarJugadorPorId(id: Int): Jugador? {
        var jugador: Jugador? = null
        conectarBD()?.use { conn ->
            conn.prepareStatement("SELECT * FROM jugador WHERE id_jugador = ?").use { pstmt ->
                pstmt.setInt(1, id)
                pstmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        jugador = Jugador(
                            id_jugador = rs.getInt("id_jugador"),
                            nombre = rs.getString("nombre"),
                            fecha_nacimiento = rs.getString("fecha_nacimiento"),
                            posicion = rs.getString("posicion"),
                            id_equipo = rs.getInt("id_equipo")
                        )
                    }
                }
            }
        } ?: println("\nNo se pudo establecer la conexión.\n")
        return jugador
    }

    fun insertarJugador(jugador: Jugador) {

        conectarBD()?.use { conn ->
            conn.prepareStatement(
                "INSERT INTO jugador(nombre, fecha_nacimiento, posicion, id_equipo) VALUES (?, ?, ?, ?)"
            ).use { pstmt ->
                pstmt.setString(1, jugador.nombre)
                pstmt.setString(2, jugador.fecha_nacimiento)
                pstmt.setString(3, jugador.posicion)
                pstmt.setInt(4, jugador.id_equipo)

                pstmt.executeUpdate()
                println("\nJugador '${jugador.nombre}' insertado con éxito.\n")
            }
        } ?: println("\nNo se pudo establecer la conexión.\n")
    }

    fun actualizarJugador(jugador: Jugador) {
        if (jugador.id_jugador == null) {
            println("\nNo se puede actualizar un equipo sin id.\n")
            return
        }
        conectarBD()?.use { conn ->
            conn.prepareStatement(
                "UPDATE jugador SET nombre = ?, fecha_nacimiento = ?, posicion = ?, id_equipo = ? WHERE id_jugador = ?"
            ).use { pstmt ->
                pstmt.setString(1, jugador.nombre)
                pstmt.setString(2, jugador.fecha_nacimiento)
                pstmt.setString(3, jugador.posicion)
                pstmt.setInt(4, jugador.id_equipo)
                pstmt.setInt(5, jugador.id_jugador)

                val filas = pstmt.executeUpdate()
                if (filas > 0) {
                    println("\nJugador con id=${jugador.id_jugador} actualizado con éxito.\n")
                } else {
                    println("\nNo se encontró ningun jugador con id=${jugador.id_jugador}.")
                }
            }
        } ?: println("\nNo se pudo establecer la conexión.\n")
    }

    fun eliminarJugador(id: Int) {
        conectarBD()?.use { conn ->
            conn.prepareStatement("DELETE FROM jugador WHERE id_jugador = ?").use { pstmt ->
                pstmt.setInt(1, id)
                val filas = pstmt.executeUpdate()
                if (filas > 0) {
                    println("\nJugador con id=$id eliminado correctamente.\n")
                } else {
                    println("\nNo se encontró ningun jugador con id=$id.\n")
                }
            }
        } ?: println("\nNo se pudo establecer la conexión.\n")
    }
}