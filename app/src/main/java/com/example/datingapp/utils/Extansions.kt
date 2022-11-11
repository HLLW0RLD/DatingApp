package com.example.datingapp.utils

import android.graphics.drawable.Drawable
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.addRepeatingJob
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.datingapp.databinding.MainCardItemBinding
import com.example.datingapp.model.data.User
import com.example.datingapp.view.adapter.MainAdapter
import com.example.datingapp.viewmodel.MainViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.coroutines.flow.collectLatest
import java.util.concurrent.TimeUnit

fun View.show(): View {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
    return this
}

fun View.hide(): View {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
    return this
}

fun bind(user: User, url : String, binding: MainCardItemBinding, onIdClick: (User) -> Unit){
    Observable
        .just(
            binding.apply {
                Glide
                    .with(root)
                    .load(url)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean
                ): Boolean {
                    if (user.gender == "F") {
                        placeholderF.visibility = View.VISIBLE
                    } else if (user.gender == "M") {
                        placeholderM.visibility = View.VISIBLE
                    }
                    return false
                }
                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean
                ): Boolean {
                    cardLoadingLayout.visibility = View.GONE
                    return false
                }
            })
            .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 3)))
            .into(imageContainer)

        if (user.photos.size > 1) {
            binding.hasMorePhotos.show()
        }
        showId.show()
        showId.setOnClickListener {
            onIdClick(user)
        }
    })
        .subscribeOn(Schedulers.io())
        .subscribe()
}

fun reBind(user: User, url : String, binding: MainCardItemBinding, onIdClick: (User) -> Unit){
    Observable
        .just(
            binding.apply {
                Glide
                    .with(root)
                    .load(url)
                    .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean
                ): Boolean {
                    if (user.gender == "F") {
                        placeholderF.show()
                    } else if (user.gender == "M") {
                        placeholderM.show()
                    }
                    return false
                }
                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean
                ): Boolean {
                    cardLoadingLayout.hide()
                    return false
                }
            })
            .into(imageContainer)

        if (user.photos.size > 1) {
            binding.hasMorePhotos.show()
        }
        showId.show()
        showId.setOnClickListener {
            onIdClick(user)
        }
    }
        )
        .subscribeOn(Schedulers.io())
        .subscribe()
}
