package com.oneinlet.filter

import com.oneinlet.model.FileExtension
import java.io.File
import java.io.PrintStream

/**
 * Created by WangZiHe on 19-11-28
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */
class ExtensionFunnel(private val extensionList: List<String>
) : Funnel {

    override fun funnelProcess(file: File): Boolean {
        return extensionList.contains(file.extension)
    }

}