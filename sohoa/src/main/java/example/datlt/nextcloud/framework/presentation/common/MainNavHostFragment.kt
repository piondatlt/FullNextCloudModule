package example.datlt.nextcloud.framework.presentation.common

import android.content.Context
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


class MainNavHostFragment : NavHostFragment() {


    override fun onAttach(context: Context) {
        super.onAttach(context)
        childFragmentManager.fragmentFactory = MainFragmentFactory()
    }


}