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

package com.nextcloud.client.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.RemoteViews
import com.nextcloud.android.lib.resources.dashboard.DashboardButton
import com.nextcloud.client.account.CurrentAccountProvider
import com.nextcloud.client.network.ClientFactory
import com.owncloud.android.R
import javax.inject.Inject

class DashboardWidgetUpdater @Inject constructor(
    private val context: Context,
    private val clientFactory: ClientFactory,
    private val accountProvider: CurrentAccountProvider
) {

    fun updateAppWidget(
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        title: String,
        iconUrl: String,
        addButton: DashboardButton?
    ) {
        val intent = Intent(context, DashboardWidgetService::class.java).apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
        }

        val views = RemoteViews(context.packageName, R.layout.dashboard_widget).apply {
            setRemoteAdapter(R.id.list, intent)
            setEmptyView(R.id.list, R.id.empty_view)
            setTextViewText(R.id.title, title)

            setAddButton(addButton, appWidgetId, this)
            setPendingReload(this, appWidgetId)
            setPendingClick(this)

        }

        appWidgetManager.updateAppWidget(appWidgetId, views)
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list)
    }

    private fun setPendingReload(remoteViews: RemoteViews, appWidgetId: Int) {
        val intentUpdate = Intent(context, DashboardWidgetProvider::class.java)
        intentUpdate.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE

        val idArray = intArrayOf(appWidgetId)
        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray)

        remoteViews.setOnClickPendingIntent(
            R.id.reload,
            PendingIntent.getBroadcast(
                context,
                appWidgetId,
                intentUpdate,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        )
    }

    private fun setPendingClick(remoteViews: RemoteViews) {
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE

        val clickIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(),
            flags
        )

        remoteViews.setPendingIntentTemplate(R.id.list, clickIntent)
    }

    private fun setAddButton(addButton: DashboardButton?, appWidgetId: Int, remoteViews: RemoteViews) {
        // create add button
        if (addButton == null) {
            remoteViews.setViewVisibility(R.id.create, View.GONE)
        } else {
            remoteViews.setViewVisibility(R.id.create, View.VISIBLE)
            remoteViews.setContentDescription(R.id.create, addButton.text)

            val clickIntent = Intent(context, DashboardWidgetProvider::class.java)
            clickIntent.action = DashboardWidgetProvider.OPEN_INTENT
            clickIntent.data = Uri.parse(addButton.link)

            remoteViews.setOnClickPendingIntent(
                R.id.create,
                PendingIntent.getBroadcast(
                    context,
                    appWidgetId,
                    clickIntent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
        }
    }

}
