package com.oneinlet

import com.oneinlet.export.PrintFileExport
import com.oneinlet.filter.Funnel
import com.oneinlet.filter.FunnelChain
import com.oneinlet.filter.NameFunnel
import com.oneinlet.model.FileValue
import java.io.File

/**
 * Created by WangZiHe on 19-11-27
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */

class FileScanner(
        private val path: String,
        private val funnelChain: FunnelChain
) {


    fun scan() {
        /* funnelChain.addFunnel(NameFunnel(fileValue.name!!), 0)
                 .addFunnel(NameFunnel(fileValue.name!!), 1)*/
        FileDetector(funnelChain).traverseFileTreeByForkJoin(path)
    }

}