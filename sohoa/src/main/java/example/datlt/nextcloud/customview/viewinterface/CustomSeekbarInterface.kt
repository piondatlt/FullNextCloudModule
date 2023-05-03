package pion.tech.magnifier2.framework.customview.viewinterface

interface CustomSeekbarInterface {
    fun onStartToTouch()
    fun onProgressChange(progress : Int)
    fun onStopToTouch()
}