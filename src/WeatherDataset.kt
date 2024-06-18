import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime

private const val NUM_FIELDS = 8
private const val TIME_FIELD = 0
private const val WIND_FIELD = 2
private const val TEMP_FIELD = 4
private const val SUN_FIELD = 6
private const val HUMID_FIELD = 7

private const val SECONDS_IN_AN_HOUR = 3600

/**
 * A sequence of weather records.
 */
class WeatherDataset private constructor() {
    private val records = mutableListOf<WeatherRecord>()

    var skipped: Int = 0
        private set

    constructor(filename: String) : this() {
        File(filename).useLines { lines ->
            lines.drop(1).forEach { createRecord(it) }
        }
    }

    private fun createRecord(line: String) {
        with(line.split(",")) {
            when {
                size != NUM_FIELDS -> ++skipped
                get(TIME_FIELD).isBlank() -> ++skipped
                else -> {
                    val time = LocalDateTime.parse(get(TIME_FIELD), WeatherRecord.TIME_FORMAT)
                    val wind = get(WIND_FIELD).toDoubleOrNull()
                    val temp = get(TEMP_FIELD).toDoubleOrNull()
                    val sun = get(SUN_FIELD).toDoubleOrNull()
                    val humid = get(HUMID_FIELD).toDoubleOrNull()
                    records.add(WeatherRecord(time, wind, temp, sun, humid))
                }
            }
        }
    }

    val size get() = records.size

    val missingWindSpeed get() = records.count { it.windSpeed == null }

    val missingTemperature get() = records.count { it.temperature == null }

    val missingIrradiance get() = records.count { it.solarIrradiance == null }

    val missingHumidity get() = records.count { it.humidity == null }

    operator fun get(index: Int) = records[index]

    operator fun iterator() = records.iterator()

    fun maxWindSpeed(): WeatherRecord? = records.maxWithOrNull(
        compareBy(nullsFirst()) { it.windSpeed })

    fun maxTemperature(): WeatherRecord? = records.maxWithOrNull(
        compareBy(nullsFirst()) { it.temperature })

    fun minHumidity(): WeatherRecord? = records.minWithOrNull(
        compareBy(nullsLast()) { it.humidity })

    fun insolation(date: LocalDate): Pair<Double,Int> {
        with (records.filter { it.time.toLocalDate() == date }) {
            val hours = count { it.solarIrradiance != null }
            val insolation = SECONDS_IN_AN_HOUR * sumOf { it.solarIrradiance ?: 0.0 }
            return Pair(insolation, hours)
        }
    }
}
