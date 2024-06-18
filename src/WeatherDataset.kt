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

    var skippedBadFormat: Int = 0
        private set

    var skippedEmptyField: Int = 0
        private set

    constructor(filename: String) : this() {
        File(filename).useLines { lines ->
            lines.drop(1).forEach { createRecord(it) }
        }
    }

    private fun createRecord(line: String) {
        with(line.split(",")) {
            when {
                // Ignore (but count) records with wrong number of fields
                size != NUM_FIELDS -> ++skippedBadFormat

                // Ignore (but count) records where a field of interest is empty
                "" in listOf(
                    get(TIME_FIELD),
                    get(WIND_FIELD),
                    get(TEMP_FIELD),
                    get(SUN_FIELD),
                    get(HUMID_FIELD)
                ) -> ++skippedEmptyField

                else -> {
                    // Attempt to parse fields of interest
                    val time = LocalDateTime.parse(get(TIME_FIELD), WeatherRecord.TIME_FORMAT)
                    val wind = get(WIND_FIELD).toDouble()
                    val temp = get(TEMP_FIELD).toDouble()
                    val sun = get(SUN_FIELD).toDouble()
                    val humid = get(HUMID_FIELD).toDouble()
                    records.add(WeatherRecord(time, wind, temp, sun, humid))
                }
            }
        }
    }

    val size get() = records.size

    operator fun get(index: Int) = records[index]

    operator fun iterator() = records.iterator()

    fun maxWindSpeed(): WeatherRecord? = records.maxByOrNull { it.windSpeed }

    fun maxTemperature(): WeatherRecord? = records.maxByOrNull { it.temperature }

    fun minHumidity(): WeatherRecord? = records.minByOrNull { it.humidity }

    fun insolation(date: LocalDate): Double {
        val dayRecords = records.filter { it.time.toLocalDate() == date }
        return SECONDS_IN_AN_HOUR * dayRecords.sumOf { it.solarIrradiance }
    }
}
