package com.oneinlet.filter

import com.oneinlet.model.FileType
import java.io.File
import java.nio.file.Files

/**
 * Created by WangZiHe on 19-11-28
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */
class FileTypeFunnel(private val fileType: FileType) : Funnel {

    override fun funnelProcess(file: File): Boolean {

        return when (fileType) {
            FileType.ALL -> {
                file.isFile || file.isDirectory
            }
            FileType.FILE -> {
                file.isFile && !file.isDirectory && !Files.isSymbolicLink(file.toPath())
            }
            FileType.DIRECTORY -> {
                file.isDirectory
            }
            FileType.LINK -> {
                Files.isSymbolicLink(file.toPath())
            }
        }
    }

}