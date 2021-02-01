package com.deval.event.fragment.detail.more

import android.R.attr
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.deval.event.Featured.GlideApp
import com.deval.event.R
import com.deval.event.Retrofit.ApiFactory
import com.deval.event.Util.BaseFragment
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_more.*
import org.jetbrains.anko.startActivityForResult
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class MoreFragment : BaseFragment() {

    companion object {
        val NAMA = "NAMA"
        val SLUG = "SLUG"
        val DESCRIPTION = "DESCRIPTION"
        val LOCATION = "LOCATION"
        val BG = "BG"
    }

    var currentPhotoPath: String = ""
    private val restForeground by lazy { ApiFactory.create(false) }
    private var disposable: Disposable? = null
    private lateinit var nama: String
    private lateinit var description: String
    private lateinit var location: String
    private lateinit var bg: String
    private lateinit var slug: String
    val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nama = arguments?.getString(NAMA).toString()
        description = arguments?.getString(DESCRIPTION).toString()
        location = arguments?.getString(LOCATION).toString()
        bg = arguments?.getString(BG).toString()

        iv_more_pict.setOnClickListener {
            dispatchTakePictureIntent()
        }

        btn_more_upload.setOnClickListener {
            showLoading()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === REQUEST_IMAGE_CAPTURE && resultCode === RESULT_OK) {
            val extras: Bundle? = data?.extras
            val imageBitmap = extras?.get("data") as Bitmap?
            iv_more_pict.setImageBitmap(imageBitmap)
            GlideApp.with(requireContext())
                .load(currentPhotoPath)
                .into(iv_more_pict)
            Toast.makeText(context, currentPhotoPath, Toast.LENGTH_SHORT).show()
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            activity?.let { activity ->
                activity.packageManager.let {
                    takePictureIntent.resolveActivity(it)?.also {
                        val photoFile: File? = try {
                            createImageFile()
                        } catch (e: IOException) {
                            null
                        }
                        photoFile?.also {
                            val photoURI: Uri = FileProvider.getUriForFile(
                                activity,
                                "com.deval.event.fileprovider",
                                it
                            )
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                        }
                    }
                }
            }
        }
    }

    protected fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    fun showLoading() {
        dialogAnimation()
        dialogAnimationImage?.visibility = View.GONE
        dialogAnimationTitle?.text = "Upload Score"
        dialogAnimationDesc?.text = "Silahkan menunggu sesaat"
    }

}