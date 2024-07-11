# kotlin-weather

A Kotlin library and associated applications for analysing weather
station data read from a CSV file - see `data/README.md` for details
of the format.

Note: `LICENSE` applies to the Kotlin code. The CSV files in the `data`
directory are covered by the [Open Government License][ogl].

## Library

This consists of three classes: `WeatherFile`, `WeatherRecord` and
`WeatherDataset`. You can run the tests for these classes with
```shell
./gradlew :lib:test
```

`WeatherFile` represents a CSV file containing weather station data.
It does some basic checking that the file exists and that it has a valid
header when you attempt to read from it. Calling the `lines()` method will
return a sequence of lines from the file but this class does no further
processing of the data, leaving that task to `WeatherDataset`.

A `WeatherRecord` object captures a subset of the data in one record
from the CSV file, specifically: date & time, wind speed (metres per second),
temperature (Celsius), solar irradiance (Watts per square metre), and
relative humidity (%). All of the meteorological measurements are nullable,
nulls being used to indicate a particular measurement is unavailable for
the date & time in question.

A `WeatherDataset` object encapsulates a sequence of `WeatherRecord` objects
created from data in a CSV file. The file is specified as a `WeatherFile`
object. This object's `lines()` method will be invoked by `WeatherDataset`
in order to read the data.

```kotlin
val file = WeatherFile("weather.csv")
val dataset = WeatherDataset(file)
```

When processing the data read from the file, records with the wrong number
of fields, or a missing date & time field, or a badly formatted date & time
field, will be skipped.

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
    println(record.temperature ?: "?")
}
```

The following analysis methods are provided:

* `maxWindSpeed()`, to find the record having the highest wind speed
* `minTemperature()`, to find the record having the lowest temperature
* `maxTemperature()`, to find the record having the highest temperature
* `minHumidity()`, to find the record having the lowest humidity
* `maxHumidity()`, to find the record having thr highest humidity

Each of these can return `null` in the case of an empty dataset or a dataset
in which measurements are not available for the meteorological parameter of
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
for the given date, otherwise it returns a `Pair` containing the computed
insolation and the number of hours over which solar irradiance was integrated.
`Pair(0.0, 0)` will be returned if the date is valid but no measurements
of irradiance were acquired on that date for some reason.

## Applications

### stats

This application takes the path to a CSV file of weather data as its sole
command line argument.

It reports on

* Numbers of processed and skipped records
* How many missing measurements there were in the dataset
* Maximum wind speed, and the time on which this was measured
* Minimum & maximum temperature, and the times on which these were measured
* Minimum & maximum humidity, and the times on which these were measured

You can try out the application with Gradle using
```shell
./gradlew :stats:run
```
Use `--args` to override the hard-coded input filename, e.g.,
```shell
./gradlew :stats:run --args="../data/leeds_2011-2018.csv"
```

You can package the application for distribution like so:
```shell
./gradlew :stats:distZip
```

### insolation

This application takes the path to a CSV file of weather data and a date
as command line arguments. The date should be formatted according to
ISO 8601, i.e., YYYY-MM-DD.

The application computes insolation on the specified date by integrating
solar irradiance measurements over that date. It displays both the computed
value and the number of hours over which it was computed.

You can try out the application with Gradle using
```shell
./gradlew :insolation:run
```
You can specify your own command line arguments using the `--args` option:
```shell
./gradlew :insolation:run --args="../data/leeds_2019.csv 2019-04-19"
```

You can package the application for distribution like so:
```shell
./gradlew :insolation:distZip
```

[ogl]: https://www.nationalarchives.gov.uk/doc/open-government-licence/version/3/
[ins]: https://en.wikipedia.org/wiki/Solar_irradiance
