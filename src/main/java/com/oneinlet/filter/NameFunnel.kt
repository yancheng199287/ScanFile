package com.oneinlet.filter

import com.oneinlet.model.FileName
import java.io.File

/**
 * Created by WangZiHe on 19-11-27
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */
class NameFunnel(private val fileName: FileName) : Funnel {


    override fun funnelProcess(file: File): Boolean {

        val name = file.name
        return when (fileName.matchCondition) {
            FileName.MatchCondition.EQUALS -> {
                name == fileName.value
            }
            FileName.MatchCondition.CONTAIN -> {
                name.contains(fileName.value)
            }
            FileName.MatchCondition.STARTING_WITH -> {
                name.startsWith(fileName.value)
            }
            FileName.MatchCondition.ENDING_WITH -> {
                name.endsWith(fileName.value)
            }
        }
    }


}