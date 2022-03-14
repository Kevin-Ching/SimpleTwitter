package com.codepath.apps.restclienttemplate

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.apps.restclienttemplate.models.Tweet
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "TweetsAdapter"

class TweetsAdapter(val tweets: ArrayList<Tweet>) : RecyclerView.Adapter<TweetsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetsAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        // Inflate our item layout
        val view = inflater.inflate(R.layout.item_tweet, parent, false)

        return ViewHolder(view)
    }

    // Populating data into the item through the viewholder
    override fun onBindViewHolder(holder: TweetsAdapter.ViewHolder, position: Int) {
        // Get the data model based on the position
        val tweet: Tweet = tweets.get(position)

        // Set item views based on data model
        holder.tvUserName.text = tweet.user?.name
        holder.tvUserHandle.text = "@" + tweet.user?.screenName
        holder.tvTweetBody.text = tweet.body
        holder.tvTimeAgo.text = getRelativeTimeAgo(tweet.createdAt)

        Glide.with(holder.itemView).load(tweet.user?.publicImageUrl).into(holder.ivProfileImage)

//        if (tweet.mediaList.size == 1) {
//            val parent = holder.ivEmbed1.parent as ConstraintLayout
//            for (i in 1 until holder.ivEmbedList.size) {
//                parent.removeView(holder.ivEmbedList[i])
//            }
//            val constraintSet = ConstraintSet()
//            constraintSet.clone(parent)
//            constraintSet.connect(holder.ivEmbed1.id, ConstraintSet.RIGHT, holder.tvTweetBody.id, ConstraintSet.RIGHT)
//            constraintSet.applyTo(parent)
//        }
        for (i in 0 until tweet.mediaList.size) {
            Glide.with(holder.itemView).load(tweet.mediaList[i]).into(holder.ivEmbedList[i])
        }
    }

    override fun getItemCount(): Int {
        return tweets.size
    }

    fun clear() {
        tweets.clear()
        notifyDataSetChanged()
    }

    // Add a list of items -- change to type used
    fun addAll(tweetList: List<Tweet>) {
        tweets.addAll(tweetList)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProfileImage = itemView.findViewById<ImageView>(R.id.ivProfileImage)
        val tvUserName = itemView.findViewById<TextView>(R.id.tvUsername)
        val tvTweetBody = itemView.findViewById<TextView>(R.id.tvTweetBody)
        val tvTimeAgo = itemView.findViewById<TextView>(R.id.tvTimeAgo)
        val tvUserHandle = itemView.findViewById<TextView>(R.id.tvUserHandle)
        val ivEmbed1 = itemView.findViewById<ImageView>(R.id.ivEmbed1)
        val ivEmbed2 = itemView.findViewById<ImageView>(R.id.ivEmbed2)
        val ivEmbed3 = itemView.findViewById<ImageView>(R.id.ivEmbed3)
        val ivEmbed4 = itemView.findViewById<ImageView>(R.id.ivEmbed4)
        val ivEmbedList = mutableListOf<ImageView>(ivEmbed1, ivEmbed2, ivEmbed3, ivEmbed4)
    }

    private val SECOND_MILLIS = 1000
    private val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private val DAY_MILLIS = 24 * HOUR_MILLIS

    fun getRelativeTimeAgo(rawJsonDate: String?): String? {
        val twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy"
        val sf = SimpleDateFormat(twitterFormat, Locale.ENGLISH)
        sf.setLenient(true)
        try {
            val time: Long = sf.parse(rawJsonDate).getTime()
            val now = System.currentTimeMillis()
            val diff = now - time
            return if (diff < MINUTE_MILLIS) {
                "just now"
            } else if (diff < 2 * MINUTE_MILLIS) {
                "a minute ago"
            } else if (diff < 50 * MINUTE_MILLIS) {
                (diff / MINUTE_MILLIS).toString() + "m"
            } else if (diff < 90 * MINUTE_MILLIS) {
                "an hour ago"
            } else if (diff < 24 * HOUR_MILLIS) {
                (diff / HOUR_MILLIS).toString() + "h"
            } else if (diff < 48 * HOUR_MILLIS) {
                "yesterday"
            } else {
                (diff / DAY_MILLIS).toString() + "d"
            }
        } catch (e: ParseException) {
            Log.i(TAG, "getRelativeTimeAgo failed")
            e.printStackTrace()
        }
        return ""
    }
}