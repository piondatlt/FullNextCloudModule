/*
 *
 * Nextcloud Android client application
 *
 * @author Tobias Kaminsky
 * Copyright (C) 2022 Tobias Kaminsky
 * Copyright (C) 2022 Nextcloud GmbH
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.owncloud.android.ui.adapter

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.target.SimpleTarget
import com.nextcloud.android.lib.resources.dashboard.DashboardWidget
import com.nextcloud.client.account.UserAccountManager
import com.nextcloud.client.network.ClientFactory
import com.nextcloud.client.widget.DashboardWidgetConfigurationInterface
import com.owncloud.android.R
import com.owncloud.android.databinding.WidgetListItemBinding
import com.owncloud.android.utils.DisplayUtils

class WidgetListItemViewHolder(
    val binding: WidgetListItemBinding,
    val accountManager: UserAccountManager,
    val clientFactory: ClientFactory,
    val context: Context
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        dashboardWidget: DashboardWidget,
        dashboardWidgetConfigurationInterface: DashboardWidgetConfigurationInterface
    ) {
        binding.layout.setOnClickListener { dashboardWidgetConfigurationInterface.onItemClicked(dashboardWidget) }




        binding.name.text = dashboardWidget.title
    }
}
