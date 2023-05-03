package example.datlt.nextcloud.framework.presentation.crop.adapter



import android.graphics.Rect
import android.net.Uri
import android.view.View
import example.datlt.nextcloud.database.entities.Photo
import example.datlt.nextcloud.databinding.FragmentItemCropPhotoBinding
import example.datlt.nextcloud.framework.presentation.common.BaseFragment
import example.datlt.nextcloud.framework.presentation.crop.CropFragment.Companion.listStatePhoto
import example.datlt.nextcloud.framework.presentation.crop.StateData
import java.io.File



/**
 * fragment này là item của viewpager dùng để crop ảnh
 */
class ItemCropPhotoFragment(
    val photo : Photo
    ) : BaseFragment<FragmentItemCropPhotoBinding>(FragmentItemCropPhotoBinding::inflate) {

    override fun init(view: View) {
        binding.cropImageView.apply {
            setImageUriAsync(Uri.fromFile(File(photo.path)))
            setOnSetImageUriCompleteListener { view, uri, error ->
                val state = getStateById()
                binding.cropImageView.cropRect = state.rectCrop
                binding.cropImageView.rotateImage(state.rotateDegree)
            }
        }
    }


    override fun subscribeObserver(view: View) {

    }


    fun rotateLeft(){
        binding.cropImageView.rotateImage(-90)
        setState()
    }

    fun rotateRight(){
        binding.cropImageView.rotateImage(90)
        setState()
    }

    override fun onDetach() {
        super.onDetach()
        setState()
    }

    private fun getStateById() : StateData {
        for (state in listStatePhoto){
            if (state.idPhoto == photo.id){
                return state
                break
            }
        }
        return StateData(photo.id , Rect() , 0)
    }

    private fun setState(){
        for (i in 0 until listStatePhoto.size){
            if (listStatePhoto[i].idPhoto == photo.id){
                listStatePhoto[i].rectCrop.set(binding.cropImageView.cropRect)
                listStatePhoto[i].rotateDegree = binding.cropImageView.rotatedDegrees
                break
            }
        }
    }

}