package com.oneinlet.filter

import com.oneinlet.model.FileAuthority
import java.io.File

/**
 * Created by WangZiHe on 19-11-28
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */

class AuthorityFunnel(private val fileAuthority: FileAuthority) : Funnel {

    override fun funnelProcess(file: File): Boolean {
        val r = file.canRead()
        val w = file.canWrite()
        return when (fileAuthority) {
            FileAuthority.ALL -> {
                r && w
            }
            FileAuthority.READ -> {
                r && !w
            }
            FileAuthority.WRITE -> {
                !r && w
            }
        }
    }

}