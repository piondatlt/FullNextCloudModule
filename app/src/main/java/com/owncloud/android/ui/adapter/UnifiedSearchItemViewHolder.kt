/*
 *
 * Nextcloud Android client application
 *
 * @author Tobias Kaminsky
 * Copyright (C) 2020 Tobias Kaminsky
 * Copyright (C) 2020 Nextcloud GmbH
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
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.afollestad.sectionedrecyclerview.SectionedViewHolder
import com.bumptech.glide.Glide
import com.nextcloud.client.account.User
import com.nextcloud.client.network.ClientFactory
import com.owncloud.android.R
import com.owncloud.android.databinding.UnifiedSearchItemBinding
import com.owncloud.android.datamodel.FileDataStorageManager
import com.owncloud.android.lib.common.SearchResultEntry
import com.owncloud.android.ui.interfaces.UnifiedSearchListInterface
import com.owncloud.android.utils.MimeTypeUtil
import com.owncloud.android.utils.theme.ViewThemeUtils

@Suppress("LongParameterList")
class UnifiedSearchItemViewHolder(
    val binding: UnifiedSearchItemBinding,
    val user: User,
    val clientFactory: ClientFactory,
    private val storageManager: FileDataStorageManager,
    private val listInterface: UnifiedSearchListInterface,
    val context: Context,
    private val viewThemeUtils: ViewThemeUtils
) :
    SectionedViewHolder(binding.root) {

    fun bind(entry: SearchResultEntry) {
        binding.title.text = entry.title
        binding.subline.text = entry.subline

        if (entry.isFile && storageManager.getFileByDecryptedRemotePath(entry.remotePath()) != null) {
            binding.localFileIndicator.visibility = View.VISIBLE
        } else {
            binding.localFileIndicator.visibility = View.GONE
        }

        val mimetype = MimeTypeUtil.getBestMimeTypeByFilename(entry.title)

        val placeholder = getPlaceholder(entry, mimetype)

        Glide.with(context)
            .asBitmap()
            .load(entry.thumbnailUrl)
            .placeholder(placeholder)
            .error(placeholder)

            .into(binding.thumbnail)

        binding.unifiedSearchItemLayout.setOnClickListener { listInterface.onSearchResultClicked(entry) }
    }

    private fun getPlaceholder(
        entry: SearchResultEntry,
        mimetype: String?
    ): Drawable {
        val drawable = with(entry.icon) {
            when {
                equals("icon-folder") ->
                    ResourcesCompat.getDrawable(context.resources, R.drawable.folder, null)
                startsWith("icon-note") ->
                    ResourcesCompat.getDrawable(context.resources, R.drawable.ic_edit, null)
                startsWith("icon-contacts") ->
                    ResourcesCompat.getDrawable(context.resources, R.drawable.file_vcard, null)
                startsWith("icon-calendar") ->
                    ResourcesCompat.getDrawable(context.resources, R.drawable.file_calendar, null)
                startsWith("icon-deck") ->
                    ResourcesCompat.getDrawable(context.resources, R.drawable.ic_deck, null)
                else ->
                    MimeTypeUtil.getFileTypeIcon(mimetype, entry.title, context, viewThemeUtils)
            }
        }
        return viewThemeUtils.platform.tintPrimaryDrawable(context, drawable)!!
    }
}
