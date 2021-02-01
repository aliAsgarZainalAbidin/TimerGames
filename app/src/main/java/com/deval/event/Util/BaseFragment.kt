package com.deval.event.Util

import android.app.Dialog
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.deval.event.R

open class BaseFragment : Fragment() {

    var dialogAnimation: Dialog? = null
    var dialogAnimationTitle : TextView? = null
    var dialogAnimationDesc : TextView? = null
    var dialogAnimationImage : ImageView? = null
    var dialogAnimationProgress : LottieAnimationView? = null
    var dialogAnimationButon : Button? = null

    fun dialogAnimation() {
        activity ?.let { dialogAnimation =  Dialog(it, R.style.ThemeDialogCustom) }
        dialogAnimation?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogAnimation?.setContentView(R.layout.fragment_success_dialog)
        dialogAnimation?.setCancelable(true)
        dialogAnimation?.setCanceledOnTouchOutside(true)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogAnimation?.window?.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER

        dialogAnimationTitle= dialogAnimation?.findViewById(R.id.tv_dialog_title)
        dialogAnimationDesc= dialogAnimation?.findViewById(R.id.tv_dialog_msg)
        dialogAnimationImage= dialogAnimation?.findViewById(R.id.iv_dialog_msg)
        dialogAnimationProgress = dialogAnimation?.findViewById(R.id.animationView)
        dialogAnimationButon = dialogAnimation?.findViewById(R.id.btn_dialog)
        val dialogAnimationClose = dialogAnimation?.findViewById<ImageButton>(R.id.ib_dialog_close)

        dialogAnimationButon?.visibility = View.GONE
        dialogAnimationClose?.setOnClickListener {
            dialogAnimation?.dismiss()
        }

        dialogAnimation?.window?.attributes = lp
        dialogAnimation?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialogAnimation?.show()
    }
}