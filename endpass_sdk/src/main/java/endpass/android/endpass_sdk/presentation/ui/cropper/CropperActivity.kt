package endpass.android.endpass_sdk.presentation.ui.cropper

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.theartofdev.edmodo.cropper.CropImageView
import endpass.android.endpass_sdk.R
import endpass.android.endpass_sdk.presentation.base.Constant
import endpass.android.endpass_sdk.presentation.base.Constant.PERMISSION_GALLERY_REQUEST_CODE
import endpass.android.endpass_sdk.presentation.ext.toast
import endpass.android.endpass_sdk.presentation.router.MainRouter
import endpass.android.endpass_sdk.presentation.ui.HomeActivity.Companion.HOME
import endpass.android.endpass_sdk.presentation.ui.document.CameraActivity
import endpass.android.endpass_sdk.presentation.utils.DataParser
import kotlinx.android.synthetic.main.activity_cropper.*
import org.koin.android.ext.android.inject
import endpass.android.endpass_sdk.presentation.ext.setDarkStatusBar


class CropperActivity : AppCompatActivity(), CropImageView.OnSetImageUriCompleteListener,
    CropImageView.OnCropImageCompleteListener {

    private val router by inject<MainRouter>()


    companion object {

        fun getStartIntent(context: Context, isFrontSide: Boolean): Intent {
            val intent = Intent(context, CropperActivity::class.java)
            intent.putExtra(CameraActivity.EXTRA_PHOTO_IS_FRONT_SIDE, isFrontSide)
            return intent
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cropper)

        checkPermission()

        submitTxt.setOnClickListener {
            cropImageView.getCroppedImageAsync();
        }

        closeTxt.setOnClickListener {
            router.openHome(this, HOME, null)
        }
        cropImageView.setOnCropImageCompleteListener(this)

        setDarkStatusBar()


    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        checkPermission()


    }

    override fun onSetImageUriComplete(
        view: CropImageView?,
        uri: Uri?,
        error: java.lang.Exception?
    ) {

        toast("Ok")
    }

    override fun onCropImageComplete(view: CropImageView?, result: CropImageView.CropResult?) {

        handleCropResult(result!!)

    }

    private fun handleCropResult(result: CropImageView.CropResult) {
        if (result.error == null) {
            returnImageToParent(DataParser.createFile(result.bitmap, this))
        }
    }

    private fun checkPermission() {
        try {
            if (isGalleryPermitted()) openGallery() else requestGalleryPermission()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun isGalleryPermitted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestGalleryPermission() {
        requestPermissions(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            PERMISSION_GALLERY_REQUEST_CODE
        )
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, Constant.PICK_FROM_GALLERY_REQUEST_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.PICK_FROM_GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            if (uri != null) {
                cropImageView.setImageUriAsync(uri)
                cropImageView.setImageBitmap(DataParser.getBitmapFromPath(getFilePath(uri)))
            }
        }
    }


    private fun getFilePath(uri: Uri): String {
        var res = ""
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor.use {
            if (it!!.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                res = it.getString(columnIndex)
            }
        }

        return res
    }


    private fun returnImageToParent(imagePath: String) {

        val result = Intent()
        result.putExtra(CameraActivity.EXTRA_PHOTO, imagePath)
        result.putExtra(
            CameraActivity.EXTRA_PHOTO_IS_FRONT_SIDE,
            intent.getBooleanExtra(CameraActivity.EXTRA_PHOTO_IS_FRONT_SIDE, true)
        )
        setResult(Activity.RESULT_OK, result)
        finish()
    }
}
