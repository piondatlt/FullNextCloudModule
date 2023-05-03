package example.datlt.nextcloud.framework.presentation.common

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NavViewModel
@Inject
constructor(
) : ViewModel() {

    private val _nav = MutableLiveData<NavView>()
    val nav: LiveData<NavView>
        get() = _nav

    fun navView(navView: NavView) {
        _nav.value = navView
    }
}
