package com.chazzca.pruebamovilforte.utils

import android.graphics.Bitmap
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.chazzca.pruebamovilforte.R
import com.google.android.material.snackbar.Snackbar

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}


/*View*/
fun ImageView.loadBitmap(bitmap: Bitmap) {
    Glide.with(this.context)
        .asBitmap()
        .load(bitmap)
        .placeholder(R.drawable.profile)
        .error(R.drawable.profile)
        .skipMemoryCache(true) //2
        .diskCacheStrategy(DiskCacheStrategy.NONE) //3
        .transform(CircleCrop()) //4
        .into(this)
}
fun View.changeBackgroundDrawable(color:Int){
    val drawable = this.background as GradientDrawable
    drawable.setColor(ContextCompat.getColor(this.context, color))
}


fun View.toggleVisibility() {
    if (visibility == View.VISIBLE) {
        visibility = View.GONE
    } else {
        visibility = View.VISIBLE
    }
}

fun View.snackExito(texto: String) {
    val snackbar = Snackbar.make(this, texto, Snackbar.LENGTH_LONG)
    val snackBarView = snackbar.getView()
    snackBarView.setBackgroundColor(this.context.resources.getColor(R.color.verde))
    snackbar.show()
}

fun View.snackAlerta(texto: String) {
    val snackbar = Snackbar.make(this, texto, Snackbar.LENGTH_LONG)
    val snackBarView = snackbar.getView()
    snackBarView.setBackgroundColor(this.context.resources.getColor(R.color.anaranjado))
    snackbar.show()
}


fun View.snackError(texto: String) {
    val snackbar = Snackbar.make(this, texto, Snackbar.LENGTH_LONG)
    val snackBarView = snackbar.getView()
    snackBarView.setBackgroundColor(this.context.resources.getColor(R.color.rojo))
    snackbar.show()
}