package org.example

data class Patrocinador(
    val id_patrocinador: Int? = null,
    val nombre: String,
    val sector: String
)

object PatrocinadorDAO {
    fun listarPatrocinadores(): List<Patrocinador> {
        val lista = mutableListOf<Patrocinador>()
        conectarBD()?.use { conn ->
            conn.createStatement().use { stmt ->
                stmt.executeQuery("SELECT * FROM patrocinador").use { rs ->
                    while (rs.next()) {
                        lista.add(
                            Patrocinador(
                                id_patrocinador = rs.getInt("id"),
                                nombre = rs.getString("nombre"),
                                sector = rs.getString("sector"),
                            )
                        )
                    }
                }
            }
        } ?: println("\nNo se pudo establecer la conexión.\n")
        return lista
    }

    fun consultarPatrocinadorPorId(id: Int): Patrocinador? {
        var patrocinador: Patrocinador? = null
        conectarBD()?.use { conn ->
            conn.prepareStatement("SELECT * FROM patrocinador WHERE id = ?").use { pstmt ->
                pstmt.setInt(1, id)
                pstmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        patrocinador = Patrocinador(
                            id_patrocinador = rs.getInt("id"),
                            nombre = rs.getString("nombre"),
                            sector = rs.getString("sector"),
                        )
                    }
                }
            }
        } ?: println("\nNo se pudo establecer la conexión.\n")
        return patrocinador
    }

    fun insertarPatrocinador(patrocinador: Patrocinador) {

        conectarBD()?.use { conn ->
            conn.prepareStatement(
                "INSERT INTO patrocinador(nombre, sector) VALUES (?, ?)"
            ).use { pstmt ->
                pstmt.setString(1, patrocinador.nombre)
                pstmt.setString(2, patrocinador.sector)

                pstmt.executeUpdate()
                println("\nPatrocinador '${patrocinador.nombre}' insertado con éxito.\n")
            }
        } ?: println("\nNo se pudo establecer la conexión.\n")
    }

    fun actualizarPatrocinador(patrocinador: Patrocinador) {
        if (patrocinador.id_patrocinador == null) {
            println("\nNo se puede actualizar un patrocinador sin id.\n")
            return
        }
        conectarBD()?.use { conn ->
            conn.prepareStatement(
                "UPDATE patrocinador SET nombre = ?, sector = ? WHERE id = ?"
            ).use { pstmt ->
                pstmt.setString(1, patrocinador.nombre)
                pstmt.setString(2, patrocinador.sector)
                pstmt.setInt(5, patrocinador.id_patrocinador)

                val filas = pstmt.executeUpdate()
                if (filas > 0) {
                    println("\npatrocinador con id=${patrocinador.id_patrocinador} actualizado con éxito.\n")
                } else {
                    println("\nNo se encontró ningun patrocinador con id=${patrocinador.id_patrocinador}.")
                }
            }
        } ?: println("\nNo se pudo establecer la conexión.\n")
    }
}