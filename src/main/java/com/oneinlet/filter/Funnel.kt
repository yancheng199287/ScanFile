package com.oneinlet.filter

import java.io.File

/**
 * Created by WangZiHe on 19-11-27
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */
/**
 *   文件类型过滤器，负责加载，分配，生成，过滤判断逻辑
 * **/
interface Funnel {

    //加载漏斗，支持多个，支持顺序指定，支持动态修改
   // fun loadFunnel()

    // 过滤逻辑，按照什么标准，什么参数进行过滤
    fun funnelProcess(file:File):Boolean

    //执行过滤操作，根据当前漏斗，执行任务
  //  fun filter()

}