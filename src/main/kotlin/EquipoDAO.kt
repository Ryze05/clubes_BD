package org.example

data class Equipo(
    val id_equipo: Int? = null,
    val nombre: String,
    val año_fundacion: Int,
    val titulos: Int,
    val facturacion: Double
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
                                id_equipo = rs.getInt("id"),
                                nombre = rs.getString("nombre"),
                                año_fundacion = rs.getInt("fundacion"),
                                titulos = rs.getInt("titulos"),
                                facturacion = rs.getDouble("facturacion")
                            )
                        )
                    }
                }
            }
        } ?: println("No se pudo establecer la conexión.")
        return lista
    }

    fun consultarEquipoPorId(id: Int): Equipo? {
        var equipo: Equipo? = null
        conectarBD()?.use { conn ->
            conn.prepareStatement("SELECT * FROM equipo WHERE id = ?").use { pstmt ->
                pstmt.setInt(1, id)
                pstmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        equipo = Equipo(
                            id_equipo = rs.getInt("id"),
                            nombre = rs.getString("nombre"),
                            año_fundacion = rs.getInt("fundacion"),
                            titulos = rs.getInt("titulos"),
                            facturacion = rs.getDouble("facturacion")
                        )
                    }
                }
            }
        } ?: println("No se pudo establecer la conexión.")
        return equipo
    }

    fun insertarEquipo(equipo: Equipo) {

        conectarBD()?.use { conn ->
            conn.prepareStatement(
                "INSERT INTO equipo(nombre, fundacion, titulos, facturacion) VALUES (?, ?, ?, ?)"
            ).use { pstmt ->
                pstmt.setString(1, equipo.nombre)
                pstmt.setInt(2, equipo.año_fundacion)
                pstmt.setInt(3, equipo.titulos)
                pstmt.setDouble(4, equipo.facturacion)
                pstmt.executeUpdate()
                println("Equipo '${equipo.nombre}' insertado con éxito.")
            }
        } ?: println("No se pudo establecer la conexión.")
    }

    fun actualizarEquipo(equipo: Equipo) {
        if (equipo.id_equipo == null) {
            println("No se puede actualizar un equipo sin id.")
            return
        }
        conectarBD()?.use { conn ->
            conn.prepareStatement(
                "UPDATE equipo SET nombre = ?, fundacion = ?, titulos = ?, facturacion = ? WHERE id = ?"
            ).use { pstmt ->
                pstmt.setString(1, equipo.nombre)
                pstmt.setInt(2, equipo.año_fundacion)
                pstmt.setInt(3, equipo.titulos)
                pstmt.setDouble(4, equipo.facturacion)
                pstmt.setInt(5, equipo.id_equipo)
                val filas = pstmt.executeUpdate()
                if (filas > 0) {
                    println("Equipo con id=${equipo.id_equipo} actualizado con éxito.")
                } else {
                    println("No se encontró ningun equipo con id=${equipo.id_equipo}.")
                }
            }
        } ?: println("No se pudo establecer la conexión.")
    }

    fun eliminarEquipo(id: Int) {
        conectarBD()?.use { conn ->
            conn.prepareStatement("DELETE FROM equipo WHERE id = ?").use { pstmt ->
                pstmt.setInt(1, id)
                val filas = pstmt.executeUpdate()
                if (filas > 0) {
                    println("Equipo con id=$id eliminado correctamente.")
                } else {
                    println("No se encontró ningun equipo con id=$id.")
                }
            }
        } ?: println("No se pudo establecer la conexión.")
    }

}