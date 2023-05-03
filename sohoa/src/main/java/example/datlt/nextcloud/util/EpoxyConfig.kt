package example.datlt.nextcloud.util

import com.airbnb.epoxy.EpoxyDataBindingLayouts
import example.datlt.nextcloud.R


@EpoxyDataBindingLayouts(
    value = [
        R.layout.epoxy_item_photo,
        R.layout.epoxy_item_folder_picker
    ]
)
interface EpoxyConfig