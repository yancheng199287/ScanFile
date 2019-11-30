package com.oneinlet.filter

import com.oneinlet.model.FileShowStatus
import java.io.File

/**
 * Created by WangZiHe on 19-11-28
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */
class ShowStatusFunnel(private val fileShowStatus: FileShowStatus) : Funnel {

    override fun funnelProcess(file: File): Boolean {
        return when (fileShowStatus) {
            FileShowStatus.SHOW -> {
                !file.isHidden
            }
            FileShowStatus.HIDDEN -> {
                file.isHidden
            }
            else -> {
                true
            }
        }
    }
}