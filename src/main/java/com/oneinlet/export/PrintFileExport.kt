package com.oneinlet.export

import java.io.File

/**
 * Created by WangZiHe on 19-11-28
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */
class PrintFileExport : FileExport {
    override fun handleFile(file: File) {
        println("收到一个文件:${file.path}")
    }
}

