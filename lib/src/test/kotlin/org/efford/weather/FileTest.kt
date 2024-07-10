package org.efford.weather

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import java.io.FileNotFoundException
import java.io.IOException

@Suppress("unused")
class FileTest: StringSpec({
    "Exception if path is invalid" {
        shouldThrow<FileNotFoundException> {
            WeatherFile("this-path-does-not-exist.csv")
        }
    }

    "Exception if file is empty" {
        shouldThrowExactly<IOException> {
            WeatherFile("../testdata/empty.csv").lines().toList()
        }
    }

    "Exception if first line is not a header" {
        shouldThrowExactly<IOException> {
            WeatherFile("../testdata/no-header.csv").lines().toList()
        }
    }

    "Lines of data read correctly" {
        val file = WeatherFile("../testdata/valid.csv")
        file.lines().toList() shouldContainExactly listOf(
            "01/01/2019 01:00,7.48,271.8,9.58,9.63999,9.89999,,81.4",
            "01/01/2019 02:00,4.851,257,16.54,9.445,9.77,,83",
            "01/01/2019 03:00,4.541,295.2,17.48,9.722,10.02,,83.8"
        )
    }
})
