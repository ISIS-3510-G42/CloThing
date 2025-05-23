package com.moviles.clothingapp.model

import com.squareup.moshi.Json
import java.io.InputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.OutputStream


/* Decorators help moshi assign values of the JSON: */
data class PostData(
    @Json(name="id") val id: String?=null, // Autoasigned in postgreSQL
    @Json(name="name") val name: String,
    @Json(name="price") val price: String,
    @Json(name="brand") val brand: String,
    @Json(name="category") val category: String,
    @Json(name="image") val image: String,
    @Json(name="color") val color: String,
    @Json(name="size") val size: String,
    @Json(name="group") val group: String

)

interface PostDataDAO {
    fun save(p: PostData, outStream: OutputStream)

    fun read(inStream: InputStream) : PostData
}


// TODO: Document this and how its used
class SerializedPostDataDAO : PostDataDAO {

    override fun save(p: PostData, outStream: OutputStream) {
        val o = ObjectOutputStream(outStream)
        outStream.use {
            o.writeObject(p)
        }
    }

    override fun read(inStream: InputStream): PostData {
        val i = ObjectInputStream(inStream)
        inStream.use {
            return i.readObject() as PostData
        }
    }

}
