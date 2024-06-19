# kotlin-weather

Kotlin classes and a Kotlin application for analysing weather station data
read from a CSV file - see `data/README.md` for details of the format.

Note: `LICENSE` applies to the Kotlin code. The CSV files in the `data`
directory are covered by the [Open Government License][ogl].

## Application

You can build the application from the command line with:

```shell
kotlinc -d weather.jar -include-runtime src/*.kt
```

You can subsequently run it with

```shell
kotlin weather.jar data/leeds_2019.csv
```

The application expects a CSV file as the sole command line argument.

## Class Details

A `WeatherRecord` object captures a subset of the data in one record
from the CSV file, specifically: date & time, wind speed (metres per second),
temperature (Celsius), solar irradiance (Watts per square metre), and
relative humidity (%). All of the meteorological measurements are nullable,
nulls being used to indicate a particular measurement is unavailable for
the date & time in question.

A `WeatherDataset` object encapsulates a sequence of `WeatherRecord` objects
created from data in a CSV file. The filename is supplied when constructing
the dataset:

```kotlin
val dataset = WeatherDataset("weather.csv")
```

Records with the wrong number of fields, or a missing date & time field,
or a badly formatted date & time field, will be skipped.

Once you have created a dataset, you can access the `size` and `skipped`
properties to see how many records from the CSV file were stored and how
many were ignored. You can also see how many of the meteorological
measurements are missing by accessing the properties `missingWindSpeed`,
`missingTemperature`, `missingIrradiance` and `missingHumidity`.

You can access records individually by their position, or iterate over the
entire dataset:

```kotlin
val firstRecord = dataset[0]

for (record in dataset) {
    println(record.temperature)
}
```

The following analysis methods are provided:

* `maxWindSpeed()`, to find the record having the highest wind speed
* `maxTemperature()`, to find the record having the highest temperature
* `minHumidity()`, to find the record having the lowest humidity

Each of these can return `null` in the case of an empty dataset or a dataset
in which measurements are available for the meteorological parameter of
interest.

In addition, the [insolation][ins] over a 24-hour period at the measurement
site can be computed:

```kotlin
val date = LocalDate.of(2019, 3, 8)
dataset.insolation(date)?.let {
    println("Insolation on $date = ${it.first} J/m2")
    println("Computed over ${it.second} hours of measurements")
}
```

Note that the `insolation` method returns `null` if no records are available
for the given date, otherwise a `Pair` containing the computed insolation
and the number of hours over which solar irradiance was integrated.
`Pair(0.0, 0)` will be returned if the date is valid but no measurements
of irradiance were acquired on that date for some reason.

[ogl]: https://www.nationalarchives.gov.uk/doc/open-government-licence/version/3/
[ins]: https://en.wikipedia.org/wiki/Solar_irradiance
