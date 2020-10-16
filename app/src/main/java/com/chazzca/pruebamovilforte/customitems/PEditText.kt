package com.chazzca.pruebamovilforte.customitems

import android.R
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View.OnFocusChangeListener
import android.view.View.OnTouchListener
import android.widget.EditText
import androidx.core.content.res.ResourcesCompat


class PEditText : EditText {

    private val btn_clear: Drawable =
        ResourcesCompat.getDrawable(resources, R.drawable.ic_menu_close_clear_cancel, null)!!

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    fun init() {
        btn_clear!!.setBounds(0, 0, btn_clear.intrinsicWidth, btn_clear.intrinsicHeight)
        val editText = this@PEditText
        setOnTouchListener(OnTouchListener { v, event ->
            if (editText.compoundDrawables[2] == null) return@OnTouchListener false
            if (event.action != MotionEvent.ACTION_UP) return@OnTouchListener false
            if (event.x > editText.width - editText.paddingRight - btn_clear.intrinsicWidth) {
                editText.setText("")
                editText.setCompoundDrawables(
                    editText.compoundDrawables[0],
                    editText.compoundDrawables[1],
                    null,
                    editText.compoundDrawables[3]
                )
            }
            false
        })
        this.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                if ("" != editText.text.toString()) {
                    editText.setCompoundDrawables(
                        editText.compoundDrawables[0],
                        editText.compoundDrawables[1],
                        btn_clear,
                        editText.compoundDrawables[3]
                    )
                }
            } else {
                editText.setCompoundDrawables(
                    editText.compoundDrawables[0],
                    editText.compoundDrawables[1],
                    null,
                    editText.compoundDrawables[3]
                )
            }
        }
        addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (editText.text.toString() == "") {
                    editText.setCompoundDrawables(
                        editText.compoundDrawables[0],
                        editText.compoundDrawables[1],
                        null,
                        editText.compoundDrawables[3]
                    )
                } else {
                    editText.setCompoundDrawables(
                        editText.compoundDrawables[0],
                        editText.compoundDrawables[1],
                        btn_clear,
                        editText.compoundDrawables[3]
                    )
                }
            }

            override fun afterTextChanged(arg0: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }
        })
    }
}