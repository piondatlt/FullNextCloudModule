package example.datlt.nextcloud.framework.presentation.crop.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView

class SnapHelperOneByOne : LinearSnapHelper() {
    override fun findTargetSnapPosition(
        layoutManager: RecyclerView.LayoutManager,
        velocityX: Int,
        velocityY: Int
    ): Int {
        if (layoutManager !is RecyclerView.SmoothScroller.ScrollVectorProvider) {
            return RecyclerView.NO_POSITION
        }
        val currentView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
        val myLayoutManager: LinearLayoutManager = layoutManager as LinearLayoutManager
        val position1: Int = myLayoutManager.findFirstVisibleItemPosition()
        val position2: Int = myLayoutManager.findLastVisibleItemPosition()
        var currentPosition: Int = layoutManager.getPosition(currentView)
        if (velocityX > 400) {
            currentPosition = position2
        } else if (velocityX < 400) {
            currentPosition = position1
        }
        return if (currentPosition == RecyclerView.NO_POSITION) {
            RecyclerView.NO_POSITION
        } else currentPosition
    }
}