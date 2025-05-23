package com.moviles.clothingapp.model

import java.io.InputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.OutputStream


data class WeatherResponse(
    val coord: Coord,
    val weather: List<Weather>,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Long,
    val sys: Sys,
    val timezone: Int,
    val id: Int,
    val name: String,
    val cod: Int
)

data class Coord(
    val lon: Double,
    val lat: Double
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int
)

data class Wind(
    val speed: Double,
    val deg: Int
)

data class Clouds(
    val all: Int
)

data class Sys(
    val type: Int,
    val id: Int,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)

interface WeatherDAO {
    fun save(p: Object, outStream: OutputStream)

    fun read(inStream: InputStream) : Object
}

class SerializedWeatherDAO : WeatherDAO {

    override fun save(p: Object, outStream: OutputStream) {
        val o = ObjectOutputStream(outStream)
        outStream.use {
            o.writeObject(p)
        }
    }

    override fun read(inStream: InputStream): Object {
        val i = ObjectInputStream(inStream)
        inStream.use {
            return i.readObject() as Object
        }
    }

}
