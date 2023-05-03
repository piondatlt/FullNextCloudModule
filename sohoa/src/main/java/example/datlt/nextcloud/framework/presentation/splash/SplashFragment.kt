package example.datlt.nextcloud.framework.presentation.splash


import android.view.View
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import example.datlt.nextcloud.R
import example.datlt.nextcloud.databinding.FragmentSplashBinding
import example.datlt.nextcloud.framework.presentation.common.BaseFragment

@AndroidEntryPoint
class SplashFragment(
    private val glide: RequestManager
) : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    override fun init(view: View) {

        binding.loadingView.startAnim(3000) {
            safeNav(R.id.splashFragment , R.id.action_splashFragment_to_homeFragment)
        }
    }

    override fun subscribeObserver(view: View) {

    }
}
