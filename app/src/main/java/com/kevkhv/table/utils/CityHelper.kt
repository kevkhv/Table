package com.kevkhv.table.utils


import android.content.Context
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

object CityHelper {
    fun getAllCountries(context: Context): ArrayList<String> {
        var tempArray = ArrayList<String>()
        try {
            val inputStream: InputStream = context.assets.open("countriesToCities.json")

            val size: Int = inputStream.available()     //узнаем размер файла в байтах
            val bytesArray = ByteArray(size)            //создаем массива размером cize
            inputStream.read(bytesArray)
            val jsonFile = String(bytesArray)          //преобразуем файл в String
            val jsonObject = JSONObject(jsonFile)      //преобразуем в обьект
            val countriesName = jsonObject.names()     //получение массива из названий всех стран
            if (countriesName != null) {
                for (n in 0 until countriesName.length()) {
                    tempArray.add(countriesName.getString(n))
                }
            }
        } catch (e: IOException) {

        }
        return tempArray
    }
}