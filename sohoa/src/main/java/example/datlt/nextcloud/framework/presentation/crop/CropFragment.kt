package example.datlt.nextcloud.framework.presentation.crop

import android.graphics.Rect
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import example.datlt.nextcloud.database.entities.Photo
import example.datlt.nextcloud.databinding.FragmentCropBinding
import example.datlt.nextcloud.framework.presentation.common.BaseFragment
import example.datlt.nextcloud.framework.presentation.crop.adapter.ItemCropAdapter


class CropFragment : BaseFragment<FragmentCropBinding>(FragmentCropBinding::inflate) {

    var currentItemId = 0

    val args by navArgs<CropFragmentArgs>()
    val listSelectedPhoto = mutableListOf<Photo>()

    val adapter = ItemCropAdapter()

    lateinit var smoothScroller: RecyclerView.SmoothScroller

    override fun init(view: View) {

        smoothScroller = object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }

            override fun getHorizontalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }

        backEvent()
        getSelectedPhoto()
        previousCropEvent()
        nextCropEvent()
        rotateLeftEvent()
        rotateRightEvent()
        nextEvent()
        deleteEvent()
    }

    override fun subscribeObserver(view: View) {

    }


    companion object {
        val listStatePhoto = mutableListOf<StateData>()
    }
}

class StateData(
    val idPhoto: Long,
    val rectCrop: Rect = Rect(),
    var rotateDegree: Int = 0
)