package org.efford.weather

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter as Formatter

/**
 * A set of weather station measurements made at a given point in time.
 *
 * Time of measurement is always required but the measurement values
 * can be `null` in order to represent missing data. Data might be
 * missing for a variety of reasons (a fault, unavailability due
 * to maintenance of instruments, or simply an inability to make a
 * valid measurement).
 *
 * @property[time] Combined date & time of the measurements
 * @property[windSpeed] Wind speed in metres per second
 * @property[temperature] Air temperature in Celsius
 * @property[irradiance] Solar irradiance (Watts per square metre)
 * @property[humidity] Relative humidity (%)
 */
data class WeatherRecord(
    val time: LocalDateTime,
    val windSpeed: Double?,
    val temperature: Double?,
    val irradiance: Double?,
    val humidity: Double?
) {
    companion object {
        val TIME_FORMAT: Formatter = Formatter.ofPattern("dd/MM/yyyy HH:mm")
    }

    /**
     * Generates a CSV-formatted string representation of this record.
     *
     * @return This record as a string
     */
    override fun toString() = buildString {
        append(time.format(TIME_FORMAT))
        append(",")
        append(windSpeed ?: "")
        append(",")
        append(temperature ?: "")
        append(",")
        append(irradiance ?: "")
        append(",")
        append(humidity ?: "")
    }
}
