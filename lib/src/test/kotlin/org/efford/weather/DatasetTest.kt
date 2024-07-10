package org.efford.weather

import java.time.LocalDateTime

import io.kotest.assertions.withClue
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe

import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

const val TOLERANCE = 0.000001

@Suppress("unused")
class DatasetTest: StringSpec({
    isolationMode = IsolationMode.InstancePerTest

    val validFile = mock<WeatherFile> {
        on { lines() } doReturn sequenceOf(
            "01/07/2019 09:00,7.38,267.7,13.78,15.378,15.66,174.9,75.8",
            "01/07/2019 10:00,6.83,265.1,14.38,15.476,15.78,149.7,76.6",
            "01/07/2019 11:00,5.525,272.4,17.87,15.7,15.96,107.6,75.5",
            "01/07/2019 12:00,7.95,283.2,14.51,18.59,19.03,564.8,62.77",
            "01/07/2019 13:00,7.83,285.7,16.51,18.334,18.71,676.8,57.59"
        )
    }

    val dataset = WeatherDataset(validFile)

    "Valid file can be read fully" {
        withClue("Number of records") { dataset.size shouldBe 5 }
        withClue("Skipped records") { dataset.skipped shouldBe 0 }
    }

    "Records can be retrieved from a dataset" {
        val rec = dataset[0]
        withClue("Time") {
            rec.time shouldBe LocalDateTime.of(2019, 7, 1, 9, 0)
        }
        withClue("Wind speed") {
            rec.windSpeed?.shouldBe(7.38)?.plusOrMinus(TOLERANCE)
        }
        withClue("Temperature") {
            rec.temperature?.shouldBe(15.378)?.plusOrMinus(TOLERANCE)
        }
        withClue("Irradiance") {
            rec.irradiance?.shouldBe(174.9)?.plusOrMinus(TOLERANCE)
        }
        withClue("Humidity") {
            rec.humidity?.shouldBe(75.8)?.plusOrMinus(TOLERANCE)
        }
    }

    "Max wind speed found successfully" {
        val rec = dataset.maxWindSpeed()
        rec?.windSpeed?.shouldBe(7.95)?.plusOrMinus(TOLERANCE)
    }

    "Min temperature found successfully" {
        val rec = dataset.minTemperature()
        rec?.temperature?.shouldBe(15.378)?.plusOrMinus(TOLERANCE)
    }

    "Max temperature found successfully" {
        val rec = dataset.maxTemperature()
        rec?.temperature?.shouldBe(18.59)?.plusOrMinus(TOLERANCE)
    }

    "Min humidity found successfully" {
        val rec = dataset.minHumidity()
        rec?.humidity?.shouldBe(57.59)?.plusOrMinus(TOLERANCE)
    }

    "Max humidity found successfully" {
        val rec = dataset.maxHumidity()
        rec?.humidity?.shouldBe(76.6)?.plusOrMinus(TOLERANCE)
    }
})
