package com.moviles.clothingapp.model

import com.squareup.moshi.Json
import java.io.InputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.OutputStream

data class UserData(
    @Json(name="id") val id: Int? = null,
    @Json(name="name") val name: String,
    @Json(name="email") val email: String,
    @Json(name="postedProducts") val postedProducts: String,
    @Json(name="boughtProducts") val boughtProducts: String
)

interface UserDataDAO {
    fun save(p: UserData, outStream: OutputStream)
    fun read(inStream: InputStream): UserData
}

class SerializedUserDataDAO : UserDataDAO {
    override fun save(p: UserData, outStream: OutputStream) {
        val o = ObjectOutputStream(outStream)
        outStream.use {
            o.writeObject(p)
        }
    }

    override fun read(inStream: InputStream): UserData {
        val i = ObjectInputStream(inStream)
        inStream.use {
            return i.readObject() as UserData
        }
    }
}
