package org.example

data class Equipo(
    val id_equipo: Int? = null,
    val nombre: String,
    val año_fundacion: Int,
    val titulos: Int,
    val facturacion: Double,
    val id_liga: Int
)

object EquipoDAO {
    fun listarEquipos(): List<Equipo> {
        val lista = mutableListOf<Equipo>()
        conectarBD()?.use { conn ->
            conn.createStatement().use { stmt ->
                stmt.executeQuery("SELECT * FROM equipo").use { rs ->
                    while (rs.next()) {
                        lista.add(
                            Equipo(
                                id_equipo = rs.getInt("id_equipo"),
                                nombre = rs.getString("nombre"),
                                año_fundacion = rs.getInt("fundacion"),
                                titulos = rs.getInt("titulos"),
                                facturacion = rs.getDouble("facturacion"),
                                id_liga = rs.getInt("id_liga")
                            )
                        )
                    }
                }
            }
        } ?: println("\nNo se pudo establecer la conexión.\n")
        return lista
    }

    fun consultarEquipoPorId(id: Int): Equipo? {
        var equipo: Equipo? = null
        conectarBD()?.use { conn ->
            conn.prepareStatement("SELECT * FROM equipo WHERE id_equipo = ?").use { pstmt ->
                pstmt.setInt(1, id)
                pstmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        equipo = Equipo(
                            id_equipo = rs.getInt("id_equipo"),
                            nombre = rs.getString("nombre"),
                            año_fundacion = rs.getInt("fundacion"),
                            titulos = rs.getInt("titulos"),
                            facturacion = rs.getDouble("facturacion"),
                            id_liga = rs.getInt("id_liga")
                        )
                    }
                }
            }
        } ?: println("\nNo se pudo establecer la conexión.\n")
        return equipo
    }

    fun insertarEquipo(equipo: Equipo) {

        conectarBD()?.use { conn ->
            conn.prepareStatement(
                "INSERT INTO equipo(nombre, fundacion, titulos, facturacion, id_liga) VALUES (?, ?, ?, ?, ?)"
            ).use { pstmt ->
                pstmt.setString(1, equipo.nombre)
                pstmt.setInt(2, equipo.año_fundacion)
                pstmt.setInt(3, equipo.titulos)
                pstmt.setDouble(4, equipo.facturacion)
                pstmt.setInt(5, equipo.id_liga)
                pstmt.executeUpdate()
                println("\nEquipo '${equipo.nombre}' insertado con éxito.\n")
            }
        } ?: println("\nNo se pudo establecer la conexión.\n")
    }

    fun actualizarEquipo(equipo: Equipo) {
        if (equipo.id_equipo == null) {
            println("\nNo se puede actualizar un equipo sin id.\n")
            return
        }
        conectarBD()?.use { conn ->
            conn.prepareStatement(
                "UPDATE equipo SET nombre = ?, fundacion = ?, titulos = ?, facturacion = ?, id_liga = ? WHERE id_equipo = ?"
            ).use { pstmt ->
                pstmt.setString(1, equipo.nombre)
                pstmt.setInt(2, equipo.año_fundacion)
                pstmt.setInt(3, equipo.titulos)
                pstmt.setDouble(4, equipo.facturacion)
                pstmt.setInt(5, equipo.id_liga)
                pstmt.setInt(6, equipo.id_equipo)
                val filas = pstmt.executeUpdate()
                if (filas > 0) {
                    println("\nEquipo con id=${equipo.id_equipo} actualizado con éxito.\n")
                } else {
                    println("\nNo se encontró ningun equipo con id=${equipo.id_equipo}.\n")
                }
            }
        } ?: println("\nNo se pudo establecer la conexión.\n")
    }

    fun eliminarEquipo(id: Int) {
        conectarBD()?.use { conn ->
            conn.prepareStatement("DELETE FROM equipo WHERE id_equipo = ?").use { pstmt ->
                pstmt.setInt(1, id)
                val filas = pstmt.executeUpdate()
                if (filas > 0) {
                    println("\nEquipo con id=$id eliminado correctamente.\n")
                } else {
                    println("\nNo se encontró ningun equipo con id=$id.\n")
                }
            }
        } ?: println("\nNo se pudo establecer la conexión.\n")
    }

}