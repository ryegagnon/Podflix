package com.example.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

object RssParser {

    suspend fun parse(urlString: String, categoryName: String = "My Feeds"): List<Podcast> = withContext(Dispatchers.IO) {
        val podcasts = mutableListOf<Podcast>()
        try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.readTimeout = 10000
            connection.connectTimeout = 15000
            connection.requestMethod = "GET"
            connection.connect()

            val inputStream: InputStream = connection.inputStream
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val parser = factory.newPullParser()
            parser.setInput(inputStream, null)

            var eventType = parser.eventType
            var currentPodcast: Podcast? = null
            var text = ""

            var title = ""
            var description = ""
            var author = ""
            var imageUrl = ""
            var videoUrl = ""
            var inItem = false

            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = parser.name
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        if (tagName.equals("item", ignoreCase = true)) {
                            inItem = true
                            title = ""
                            description = ""
                            author = ""
                            imageUrl = ""
                            videoUrl = ""
                        } else if (inItem && tagName.equals("enclosure", ignoreCase = true)) {
                            val type = parser.getAttributeValue(null, "type")
                            if (type != null && (type.startsWith("video") || type.startsWith("audio"))) {
                                videoUrl = parser.getAttributeValue(null, "url") ?: ""
                            }
                        } else if (inItem && (tagName.equals("image", ignoreCase = true) || tagName.equals("thumbnail", ignoreCase = true))) {
                           val urlAttr = parser.getAttributeValue(null, "href") ?: parser.getAttributeValue(null, "url")
                           if (urlAttr != null) {
                               imageUrl = urlAttr
                           }
                        }
                    }
                    XmlPullParser.TEXT -> {
                        text = parser.text
                    }
                    XmlPullParser.END_TAG -> {
                        if (inItem) {
                            when (tagName.lowercase()) {
                                "title" -> title = text
                                "description" -> description = text
                                "author" -> author = text
                                "image" -> {
                                    if (imageUrl.isEmpty()) imageUrl = text
                                }
                            }
                        }
                        if (tagName.equals("item", ignoreCase = true)) {
                            if (videoUrl.isNotEmpty()) {
                                podcasts.add(
                                    Podcast(
                                        id = videoUrl.hashCode().toString(),
                                        title = title.ifEmpty { "Unknown Title" },
                                        description = description.ifEmpty { "No Description" },
                                        author = author.ifEmpty { "Unknown Author" },
                                        imageUrl = imageUrl,
                                        videoUrl = videoUrl,
                                        category = categoryName
                                    )
                                )
                            }
                            inItem = false
                        }
                    }
                }
                eventType = parser.next()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext podcasts
    }
}
