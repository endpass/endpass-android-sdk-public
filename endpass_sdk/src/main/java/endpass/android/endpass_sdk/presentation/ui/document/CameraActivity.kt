package endpass.android.endpass_sdk.presentation.ui.document

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import endpass.android.endpass_sdk.R
import endpass.android.endpass_sdk.presentation.base.Constant.EXTERNAL_FILES_DIRECTORY_NAME
import endpass.android.endpass_sdk.presentation.base.Constant.PERMISSION_CAMERA_REQUEST_CODE
import endpass.android.endpass_sdk.presentation.ext.*
import endpass.android.endpass_sdk.presentation.router.MainRouter
import endpass.android.endpass_sdk.presentation.ui.HomeActivity.Companion.HOME
import io.fotoapparat.Fotoapparat
import io.fotoapparat.log.fileLogger
import io.fotoapparat.log.logcat
import io.fotoapparat.log.loggers
import io.fotoapparat.parameter.ScaleType
import kotlinx.android.synthetic.main.activity_camera.*
import org.koin.android.ext.android.inject
import java.io.File
import java.util.*

/**
 * Make photo activity.
 */
class CameraActivity : AppCompatActivity() {


    companion object {

        const val EXTRA_PHOTO = "extra_photo_data"
        const val EXTRA_PHOTO_IS_FRONT_SIDE = "extra_photo_is_front_side"

        fun getStartIntent(context: Context, isFrontSide: Boolean): Intent {
            val intent = Intent(context, CameraActivity::class.java)
            intent.putExtra(EXTRA_PHOTO_IS_FRONT_SIDE, isFrontSide)
            return intent
        }
    }

    private lateinit var fotoapparat: Fotoapparat
    private var imagePath: String? = null
    private val router by inject<MainRouter>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        setUp()
        initCamera()
        initListeners()
    }

    private fun setUp() {
        setDarkStatusBar()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {

        if (requestCode == PERMISSION_CAMERA_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                renderCamera()
            }
        }

    }


    /**
     * Initialize camera function.
     */

    private fun initCamera() {
        if (checkPermission) {
            renderCamera()
        } else {
            requestPermission()
        }
    }


    /**
     * Start image capture function.
     */
    private fun renderCamera() {
        fotoapparat = Fotoapparat(
            context = this,
            view = camera_view,
            scaleType = ScaleType.CenterCrop,
            logger = loggers(
                logcat(),
                fileLogger(this)
            ),
            cameraErrorCallback = {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        )
        fotoapparat.start()
    }


    /**
     * Return image result to parent/host activity.
     */

    private fun returnImageToParent() {

        val result = Intent()
        result.putExtra(EXTRA_PHOTO, imagePath)
        result.putExtra(
            EXTRA_PHOTO_IS_FRONT_SIDE,
            intent.getBooleanExtra(EXTRA_PHOTO_IS_FRONT_SIDE, true)
        )
        setResult(Activity.RESULT_OK, result)
        finish()
    }


    private fun takePhoto() {

        val photoResult = fotoapparat.takePicture()
        val file = File(
            getExternalFilesDir(EXTERNAL_FILES_DIRECTORY_NAME),
            "photo" + (Random().nextInt() % 10000) + ".png"
        )
        photoResult.saveToFile(file).whenAvailable {
            fotoapparat.stop()
            imagePath = file.absolutePath
            previewIv.loadImageFromStorage(imagePath!!)
            showPreView()


        }
    }


    /**
     * Initialize listeners function.
     */
    private fun initListeners() {

        closeTxt.setOnClickListener {
            if (imagePath == null) {
                router.openHome(this,HOME,null)
            } else {
                makePhotoState()
            }
        }

        takePhotoIv.setOnClickListener {
            takePhoto()
        }

        submitTxt.setOnClickListener {
            returnImageToParent()
        }

    }

    private fun makePhotoState() {
        fotoapparat.start()
        closeTxt.text = getString(R.string.close_camera_text)
        maskView.setImageResource(R.drawable.mask_photo_focus)
        takePhotoIv.show()
        submitTxt.hide()
        previewIv.hide()
        imagePath = null

    }

    private fun showPreView() {
        previewIv.show()
        maskView.setImageResource(R.drawable.mask_photo_preview)
        closeTxt.text = getString(R.string.retake_text)
        submitTxt.show()
        takePhotoIv.hide()

    }

}
