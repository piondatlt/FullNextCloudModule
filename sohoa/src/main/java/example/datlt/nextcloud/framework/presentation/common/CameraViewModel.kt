package example.datlt.nextcloud.framework.presentation.common

import android.graphics.Bitmap
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CameraViewModel
@Inject
constructor(
) : ViewModel() {

    var currentBitmap : Bitmap? = null

}
