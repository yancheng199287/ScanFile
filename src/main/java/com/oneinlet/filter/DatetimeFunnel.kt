package com.oneinlet.filter

import com.oneinlet.model.FileDateTime
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * Created by WangZiHe on 19-11-28
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */
class DatetimeFunnel(private val fileDateTime: FileDateTime) : Funnel {

    override fun funnelProcess(file: File): Boolean {
        val dateTime = file.lastModified()
        val startDateTime = fileDateTime.startDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli()
        val endDateTime = fileDateTime.endDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli()
        return dateTime in startDateTime..endDateTime
    }

}