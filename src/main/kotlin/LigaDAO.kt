package org.example

data class Liga(
    val id_liga: Int? = null,
    val nombre: String,
    val pais: String,
    val division: String
)

object LigaDAO {
    fun listarLigas(): List<Liga> {
        val lista = mutableListOf<Liga>()
        conectarBD()?.use { conn ->
            conn.createStatement().use { stmt ->
                stmt.executeQuery("SELECT * FROM liga").use { rs ->
                    while (rs.next()) {
                        lista.add(
                            Liga(
                                id_liga = rs.getInt("id_liga"),
                                nombre = rs.getString("nombre"),
                                pais = rs.getString("pais"),
                                division = rs.getString("division")
                            )
                        )
                    }
                }
            }
        } ?: println("\nNo se pudo establecer la conexión.\n")
        return lista
    }

    fun consultarLigaPorId(id: Int): Liga? {
        var liga: Liga? = null
        conectarBD()?.use { conn ->
            conn.prepareStatement("SELECT * FROM liga WHERE id_liga = ?").use { pstmt ->
                pstmt.setInt(1, id)
                pstmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        liga = Liga(
                            id_liga = rs.getInt("id_liga"),
                            nombre = rs.getString("nombre"),
                            pais = rs.getString("pais"),
                            division = rs.getString("division")
                        )
                    }
                }
            }
        } ?: println("\nNo se pudo establecer la conexión.\n")
        return liga
    }

    fun insertarLiga(liga: Liga) {

        conectarBD()?.use { conn ->
            conn.prepareStatement(
                "INSERT INTO liga(nombre, pais, division) VALUES (?, ?, ?)"
            ).use { pstmt ->
                pstmt.setString(1, liga.nombre)
                pstmt.setString(2, liga.pais)
                pstmt.setString(3, liga.division)
                pstmt.executeUpdate()
                println("\nLiga '${liga.nombre}' insertada con éxito.\n")
            }
        } ?: println("\nNo se pudo establecer la conexión.\n")
    }

    fun actualizarLiga(liga: Liga) {
        if (liga.id_liga == null) {
            println("\nNo se puede actualizar un equipo sin id.\n")
            return
        }
        conectarBD()?.use { conn ->
            conn.prepareStatement(
                "UPDATE liga SET nombre = ?, pais = ?, division = ? WHERE id_liga = ?"
            ).use { pstmt ->
                pstmt.setString(1, liga.nombre)
                pstmt.setString(2, liga.pais)
                pstmt.setString(3, liga.division)
                pstmt.setInt(4, liga.id_liga)
                val filas = pstmt.executeUpdate()
                if (filas > 0) {
                    println("\nLiga con id=${liga.id_liga} actualizada con éxito.\n")
                } else {
                    println("\nNo se encontró ninguna liga con id=${liga.id_liga}.\n")
                }
            }
        } ?: println("\nNo se pudo establecer la conexión.\n")
    }

    fun eliminarLiga(id: Int) {
        conectarBD()?.use { conn ->
            conn.prepareStatement("DELETE FROM liga WHERE id_liga = ?").use { pstmt ->
                pstmt.setInt(1, id)
                val filas = pstmt.executeUpdate()
                if (filas > 0) {
                    println("\nLiga con id=$id eliminada correctamente.\n")
                } else {
                    println("\nNo se encontró ninguna liga con id=$id.\n")
                }
            }
        } ?: println("\nNo se pudo establecer la conexión.\n")
    }
}