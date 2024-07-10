package org.efford.weather

import java.time.LocalDateTime
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

@Suppress("unused")
class RecordTest: StringSpec({
    val time = LocalDateTime.of(2024, 7, 1, 11, 30)

    "Exception if wind speed is negative" {
        shouldThrow<IllegalArgumentException> {
            WeatherRecord(time, -0.1, 5.0, 50.0, 25.0)
        }
    }

    "Exception if irradiance is negative" {
        shouldThrow<IllegalArgumentException> {
            WeatherRecord(time, 1.0, 5.0, -0.1, 25.0)
        }
    }

    "Exception if humidity is negative" {
        shouldThrow<IllegalArgumentException> {
            WeatherRecord(time, 1.0, 5.0, 50.0, -0.1)
        }
    }

    "Correct string rep with all fields present" {
        WeatherRecord(time, 1.0, 5.0, 50.0, 25.0)
            .toString() shouldBe "01/07/2024 11:30,1.0,5.0,50.0,25.0"
    }

    "Correct string rep with no wind speed" {
        WeatherRecord(time, null, 5.0, 50.0, 25.0)
            .toString() shouldBe "01/07/2024 11:30,,5.0,50.0,25.0"
    }

    "Correct string rep with no temperature" {
        WeatherRecord(time, 1.0, null, 50.0, 25.0)
            .toString() shouldBe "01/07/2024 11:30,1.0,,50.0,25.0"
    }

    "Correct string rep with no irradiance" {
        WeatherRecord(time, 1.0, 5.0, null, 25.0)
            .toString() shouldBe "01/07/2024 11:30,1.0,5.0,,25.0"
    }

    "Correct string rep with no humidity" {
        WeatherRecord(time, 1.0, 5.0, 50.0, null)
            .toString() shouldBe "01/07/2024 11:30,1.0,5.0,50.0,"
    }
})
