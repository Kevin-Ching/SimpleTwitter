package com.codepath.apps.restclienttemplate.models

import org.json.JSONArray
import org.json.JSONObject

class Tweet {

    var body: String = ""
    var createdAt: String = ""
    var user: User? = null
    var id: Long = 0

    companion object {
        fun fromJson(jsonObject: JSONObject) : Tweet {
            val tweet = Tweet()
            tweet.body = jsonObject.getString("text")
            tweet.createdAt = jsonObject.getString("created_at")
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"))
            tweet.id = jsonObject.getLong("id")
            return tweet
        }

        fun fromJsonArray(jsonArray: JSONArray): Pair<List<Tweet>, Long> {
            val tweets = ArrayList<Tweet>()
            var tweet: Tweet? = null
            var minId: Long = (1.0F/0.0F).toLong()
            for (i in 0 until jsonArray.length()) {
                tweet = fromJson(jsonArray.getJSONObject(i))
                if (tweet.id < minId) {
                    minId = tweet.id
                }
                tweets.add(tweet)
            }
            return Pair(tweets, minId)
        }
    }
}