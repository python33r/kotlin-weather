## Dataset Details

The [data source][src] is Leeds City Council - specifically, the CSV files
of hourly meteorological measurements that LCC has provided. Readings taken
between 1 Jan 2008 and 21 March 2011 come from a weather station located
at Pottery Fields: [longitude -1.543237, latitude 53.787769][loc].
Readings taken between 22 March 2011 and 1 July 2018 come from a different
weather station at Knowsthorpe Gate. The source of readings taken outside
of these periods is not documented.

The original hourly data consisted of two CSV files: one spanning years
2000-2018, the other covering only 2019. In the former there is a problem
with the 2018 data, where two of the columns have been transposed relative
to all of the other data across both files. This transposition is actually a
more logical arrangement of columns, so the rest of the data have been
altered to use it. The larger of the two files has also been split into two
smaller files, covering years 2000-2010 and 2011-2018.

The final datasets are

* `leeds_2000-2010.csv`
* `leeds_2011-2018.csv`
* `leeds_2019.csv`

Columns in theses files are as follows:

1. Date & time (`dd/mm/yyyy HH:MM` format)
2. Wind speed (metres per second)
3. Mean wind direction (degrees)
4. Standard deviation in wind direction (degrees)
5. Temperature at 2 metres elevation (Celsius)
6. Temperature at 8 metres elevation (Celsius)
7. Relative humidity (% of moisture)

### Data Quality Notes

Quoting from the [details given on data.gov.uk][src]:

* "There are periods where the bearings in the wind vane have not been 
  moving freely and may have affected accuracy of the direction in low wind
  speeds etc."

* "Blank sections are due to servicing of, or faults, with the equipment."

In the case of irradiance, however, missing values are mostly just the
the natural consequence of the day-night cycle.

### Data Licensing

Modification of the datasets and their inclusion in this repository are
permitted under the terms of the [Open Government License][ogl].

[src]: https://www.data.gov.uk/dataset/4afd0747-4fba-49c4-b0aa-b6f093a7db2c/leeds-meteorological-data
[loc]: https://maps.app.goo.gl/Gvx65nNL4t1keH8s8
[ogl]: https://www.nationalarchives.gov.uk/doc/open-government-licence/version/3/
