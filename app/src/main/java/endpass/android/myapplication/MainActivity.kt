package endpass.android.myapplication
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        switchFragment(0)
    }


    private fun switchFragment(position: Int) {
        supportFragmentManager.replaceOnce(R.id.frame_container, position.toString(), {
            TestFragment()
        }).commit()
    }


}
