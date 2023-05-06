package example.datlt.nextcloud.framework

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import example.datlt.nextcloud.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_main) //TODO: change name navhost

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerMain) as NavHostFragment
        val navController = navHostFragment.navController

        if (intent.getStringExtra("comeFrom") == "camera") {
            isSelectPhotoThread = false
            navController.navigate(R.id.cameraFragment)
        } else {
            isSelectPhotoThread = true
            navController.navigate(R.id.selectImageFragment)
        }
    }

    companion object{
        var isSelectPhotoThread = true
        var isRemoveAllAction = true
    }



}