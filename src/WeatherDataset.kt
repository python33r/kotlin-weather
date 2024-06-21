import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

private const val NUM_FIELDS = 8
private const val TIME_FIELD = 0
private const val WIND_FIELD = 1
private const val TEMP_FIELD = 4
private const val SUN_FIELD = 6
private const val HUMID_FIELD = 7

private const val SECONDS_IN_AN_HOUR = 3600

/**
 * A sequence of weather records.
 *
 * A CSV file must be supplied in order to create a weather dataset.
 * Records from this file are stored and a count is kept of records that
 * couldn't be read successfully due to the wrong number of fields, or
 * a missing or badly formatted date & time field. Missing fields in one
 * line of the file map onto nulls in the corresponding record. You can
 * query a dataset to see how many missing measurements there are.
 *
 * A weather dataset is somewhat list-like: you can query the number
 * of stored records via its `size` property, access an individual record
 * using `[]` with an integer index, and iterate over records using a
 * for loop.
 *
 * A dataset can be queried to find the records with the highest wind
 * speed, highest temperature and lowest humidity. You can also compute
 * total solar insolation for a given date.
 */
class WeatherDataset private constructor() {
    private val records = mutableListOf<WeatherRecord>()

    /**
     * Number of records skipped due to errors
     */
    var skipped: Int = 0
        private set

    /**
     * Creates a dataset from data in the given file.
     *
     * @param[filename] Name of file containing the data
     */
    constructor(filename: String): this() {
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
                    try {
                        val time = LocalDateTime.parse(get(TIME_FIELD), WeatherRecord.TIME_FORMAT)
                        val wind = get(WIND_FIELD).toDoubleOrNull()
                        val temp = get(TEMP_FIELD).toDoubleOrNull()
                        val sun = get(SUN_FIELD).toDoubleOrNull()
                        val humid = get(HUMID_FIELD).toDoubleOrNull()
                        records.add(WeatherRecord(time, wind, temp, sun, humid))
                    }
                    catch (error: DateTimeParseException) {
                        ++skipped
                    }
                }
            }
        }
    }

    /**
     * Number of records in this dataset
     */
    val size get() = records.size

    /**
     * Number of missing wind speed measurements
     */
    val missingWindSpeed get() = records.count { it.windSpeed == null }

    /**
     * Number of missing temperature measurements
     */
    val missingTemperature get() = records.count { it.temperature == null }

    /**
     * Number of missing solar irradiance measurements
     */
    val missingIrradiance get() = records.count { it.solarIrradiance == null }

    /**
     * Number of missing humidity measurements
     */
    val missingHumidity get() = records.count { it.humidity == null }

    /**
     * Retrieves a record from this dataset.
     *
     * @param[index] Position of desired record (zero-based)
     * @return Record at the given position
     * @throws[IndexOutOfBoundsException] if the index is invalid
     */
    operator fun get(index: Int) = records[index]

    /**
     * Provides support for iteration over this dataset's records.
     */
    operator fun iterator() = records.iterator()

    /**
     * Finds the record having the highest wind speed.
     *
     * @return A record, or null if there are no measurements of wind speed
     */
    fun maxWindSpeed(): WeatherRecord? = records.maxWithOrNull(
        compareBy(nullsFirst()) { it.windSpeed })

    /**
     * Finds the record having the highest temperature.
     *
     * @return A record, or null if there are no measurements of temperature
     */
    fun maxTemperature(): WeatherRecord? = records.maxWithOrNull(
        compareBy(nullsFirst()) { it.temperature })

    /**
     * Finds the record having the lowest humidity.
     *
     * @return A record, or null if there are no measurements of humidity
     */
    fun minHumidity(): WeatherRecord? = records.minWithOrNull(
        compareBy(nullsLast()) { it.humidity })

    /**
     * Computes insolation for a given 24-hour period.
     *
     * If the desired date isn't found in this dataset, `null` is returned.
     * If the date is found, a pair is returned containing the insolation
     * computed for that date and the number of hours over which irradiance
     * was integrated.
     *
     * @param[date] Date for which insolation must be computed
     * @return Insolation (Joules per square metre) and number of hours, or `null`
     */
    fun insolation(date: LocalDate): Pair<Double,Int>? {
        with(records.filter { it.time.toLocalDate() == date }) {
            if (isEmpty()) return null
            val hours = count { it.solarIrradiance != null }
            val insolation = SECONDS_IN_AN_HOUR * sumOf { it.solarIrradiance ?: 0.0 }
            return Pair(insolation, hours)
        }
    }
}
