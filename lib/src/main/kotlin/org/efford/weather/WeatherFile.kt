package org.efford.weather

import java.io.FileNotFoundException
import java.io.IOException
import kotlin.io.path.Path
import kotlin.io.path.exists

const val HEADER = "Time,W Spd m/s,W Dir,STD W Dir,Temp 2m,Temp 8m,Glob Rad W/m2,Rel Hum %"

/**
 * A CSV file of weather station data.
 *
 * @property[path] Path to the CSV file
 */
class WeatherFile(location: String) {
    private val path = Path(location).also {
        if (!it.exists()) throw FileNotFoundException("$it does not exist")
    }

    /**
     * Obtains lines of data from this file.
     *
     * The file is checked for the existence of a valid header line,
     * but the header itself is not returned.
     *
     * @return Sequence of lines
     * @throws IOException if the file is empty, or there is no valid header,
     *   or reading a line of data failed
     */
    fun lines() = sequence {
        val reader = path.toFile().bufferedReader()
        reader.use {
            val first = reader.readLine() ?: throw IOException("No header found")
            if (first != HEADER) throw IOException("Invalid header")
            while (true) {
                val line = reader.readLine() ?: break
                yield(line)
            }
        }
    }
}
