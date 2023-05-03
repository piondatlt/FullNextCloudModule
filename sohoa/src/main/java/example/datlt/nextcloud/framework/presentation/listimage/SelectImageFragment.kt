package example.datlt.nextcloud.framework.presentation.listimage

import android.view.View
import example.datlt.nextcloud.database.entities.Photo
import example.datlt.nextcloud.databinding.FragmentSelectImageBinding
import example.datlt.nextcloud.framework.presentation.common.BaseFragment
import example.datlt.nextcloud.framework.presentation.listimage.adapter.PhotoAdapter
import example.datlt.nextcloud.util.ListFileUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SelectImageFragment :
    BaseFragment<FragmentSelectImageBinding>(FragmentSelectImageBinding::inflate) {

    val listAllPhoto = mutableListOf<Photo>() //list nay la tat ca anh

    val listFolderPhoto = mutableListOf<String>() //list nay la list folder

    val listPhotoDisplay = mutableListOf<Photo>() //list nay la sau khi sap xep

    val listSelectedPhoto = mutableListOf<Photo>() //list photo duoc chon

    val adapter = PhotoAdapter(onClickItem = {
        //click item
        clickItem(it)
    })

    override fun init(view: View) {

        if (listSelectedPhoto.size > 0) {
            binding.btnNext.text = "Next (${listSelectedPhoto.size})"
            binding.btnNext.visibility = View.VISIBLE
        } else {
            binding.btnNext.visibility = View.GONE
        }

        backEvent()
        //init adapter
        initRecyclerView()
        nextEvent()
    }

    override fun subscribeObserver(view: View) {
        //get list photo
        if (listAllPhoto.isEmpty()){
            context?.let {
                CoroutineScope(Dispatchers.IO).launch {
                    ListFileUtils(it).getAllPhoto {
                        listAllPhoto.clear()
                        listPhotoDisplay.clear()
                        listAllPhoto.addAll(it)
                        listPhotoDisplay.addAll(listAllPhoto)
                        setListToAdapter()
                        getListFolder()
                    }
                }
            }
        }
    }

}