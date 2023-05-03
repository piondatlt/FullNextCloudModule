package example.datlt.nextcloud.framework.presentation.common

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.afollestad.materialdialogs.MaterialDialog

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<Binding : ViewBinding>(
    private val inflate: Inflate<Binding>
) : Fragment() {

    protected val commonViewModel: CommonViewModel by activityViewModels()

    lateinit var stateChangeListener: DataStateChangeListener
    lateinit var navController: NavController

    private var _binding: Binding? = null
    val binding get() = _binding!!

    var dialogLoading: MaterialDialog? = null

    private var isInit = false
    var saveView = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            stateChangeListener = context as DataStateChangeListener
        } catch (e: ClassCastException) {
            Log.e(TAG, "$context must implement DataStateChangeListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (saveView) {
            if (_binding == null) {
                isInit = true
                _binding = inflate.invoke(inflater, container, false)
            } else {
                isInit = false
            }
        } else {
            _binding = inflate.invoke(inflater, container, false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        init(view)
        subscribeObserver(view)
    }

    abstract fun init(view: View)

    abstract fun subscribeObserver(view: View)

    fun showDialogLoading() {
//        if (dialogLoading == null) {
//            dialogLoading = MaterialDialog(requireContext()).apply {
//                cancelable(false)
//                customView(R.layout.dialog_loading)
//            }
//        }
//        dialogLoading?.show {
//            cornerRadius(16f)
//        }
    }

    fun hideDialogLoading() {
        dialogLoading?.dismiss()
        dialogLoading = null
    }

    fun safeNav(currentDestination: Int, action: Int) {
        if (navController.currentDestination?.id == currentDestination) {
            lifecycle.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (event == Lifecycle.Event.ON_RESUME) {
                        lifecycle.removeObserver(this)
                        try {
                            navController.navigate(action)
                        } catch (e: IllegalArgumentException) {
                            Log.e(TAG, "safeNav: ${e.message}")
                        }
                    }
                }
            })
        }
    }

    fun safeNav(currentDestination: Int, action: Int , bundle: Bundle) {
        if (navController.currentDestination?.id == currentDestination) {
            lifecycle.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (event == Lifecycle.Event.ON_RESUME) {
                        lifecycle.removeObserver(this)
                        try {
                            navController.navigate(action , bundle)
                        } catch (e: IllegalArgumentException) {
                            Log.e(TAG, "safeNav: ${e.message}")
                        }
                    }
                }
            })
        }
    }

    fun safeNav(currentDestination: Int, navDirections: NavDirections) {
        if (navController.currentDestination?.id == currentDestination) {
            lifecycle.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (event == Lifecycle.Event.ON_RESUME) {
                        lifecycle.removeObserver(this)
                        try {
                            navController.navigate(navDirections)
                        } catch (e: IllegalArgumentException) {
                            Log.e(TAG, "safeNav: ${e.message}")
                        }
                    }
                }
            })
        }
    }

    companion object {
        private const val TAG = "BaseFragment"
    }
}
