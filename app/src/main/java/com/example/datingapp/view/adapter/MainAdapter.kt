package com.example.datingapp.view.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.datingapp.databinding.MainCardItemBinding
import com.example.datingapp.model.data.User
import com.example.datingapp.utils.bind
import com.example.datingapp.utils.reBind
import com.example.datingapp.utils.show
import io.reactivex.rxjava3.subjects.BehaviorSubject
import jp.wasabeef.glide.transformations.BlurTransformation
import java.util.*

class MainAdapter : PagingDataAdapter<User, MainAdapter.MainViewHolder>(PHOTO_COMPARATOR) {

    val cachedUsers : MutableList<User> = mutableListOf()
    lateinit var binding: MainCardItemBinding
    private var onItemClickListener: (View) -> Unit = {}
    private var onIdClickListener: (User) -> Unit = {}

    fun setOnItemClickListener(onItemClickListener: (View) -> Unit) {
        this.onItemClickListener = onItemClickListener
    }

    fun setOnIdClickListener(onIdClickListener: (User) -> Unit) {
        this.onIdClickListener = onIdClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        binding = MainCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        if (cachedUsers.contains(getItem(position))){
            holder.reBind(getItem(position)!!)
        }else{
            holder.bind(getItem(position)!!)
        }
    }

    inner class MainViewHolder(val binding: MainCardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            val url = user.photos[0].url
            val newUrl = url.replace("@", "500")
            bind(user, newUrl, binding, onIdClickListener)
            binding.apply {
                root.setOnClickListener {
                    onItemClickListener(it)
                    cachedUsers.add(user)
                    Glide
                        .with(root)
                        .load(newUrl)
                        .into(imageContainer)
                }
            }
        }
        fun reBind(user: User) {
            val url = user.photos[0].url
            val newUrl = url.replace("@", "500")
            reBind(user, newUrl, binding, onIdClickListener)
        }
    }

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: User, newItem: User) =
                oldItem == newItem
        }
    }
}

