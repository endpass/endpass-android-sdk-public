package endpass.android.endpass_sdk.presentation.utils

import android.content.Context
import endpass.android.endpass_sdk.gateway.EnumCollections
import endpass.android.endpass_sdk.gateway.entity.documents.Document
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import java.io.IOException
import java.io.InputStream


/**
 * Класс предоставляет локальные данные,
 * таким оюразом иммитируем rest-запросы
 * !!! В релиз версиях строго  заменять на реальные api запросы
 * Перед релизом проверить вызовы методов
 */

object FakeEnvironment {


    fun getFakeDocuments(context: Context): ArrayList<Document> {
        val gson = Gson()
        val listType = object : TypeToken<ArrayList<Document>>() {}.type
        return gson.fromJson(getStringFromAssets(context, "documents_list.json"), listType)
    }

    private fun getStringFromAssets(context: Context, fileName: String): String? {
        var str_data: String? = null
        var `is`: InputStream? = null
        try {
            `is` = context.assets.open(fileName)
            val size = `is`!!.available()
            val buffer = ByteArray(size) //declare the size of the byte array with size of the file
            `is`.read(buffer) //read file
            `is`.close() //close file

            // Store text file data in the string variable
            str_data = String(buffer)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return str_data

    }


    fun verifyDocument(
        documents: ArrayList<Document>,
        filters: List<EnumCollections.DocumentType>
    ): ArrayList<Document> {

        val filteredList = documents.filter { it.documentType in filters } as ArrayList<Document>

        filteredList.forEach {
            it.status = EnumCollections.DocumentStatusType.Verified
        }


        return filteredList
    }

}
