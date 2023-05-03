package example.datlt.nextcloud.framework.presentation.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bumptech.glide.RequestManager
import example.datlt.nextcloud.framework.presentation.splash.SplashFragment
import example.datlt.nextcloud.util.PrefUtil
import javax.inject.Inject

class MainFragmentFactory
@Inject
constructor(
    private val prefUtil: PrefUtil,
    private val glide: RequestManager
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {

        return when (className) {
            SplashFragment::class.java.name -> {
                SplashFragment(glide)
            }

            else -> super.instantiate(classLoader, className)
        }

    }
}