package com.oneinlet.model

/**
 * Created by WangZiHe on 19-11-27
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */
enum class FileProperty {
    //名称
    NAME,
    //大小  byte  kb mb gb
    SIZE,
    //修改时间
    DATETIME,
    //文件扩展名  参考 FileExtension
    EXTENSION,
    //权限  read write execute
    AUTHORITY,
    //文件类型，文件或者目录,或者链接文件
    TYPE,
    //显示状态，显示还是隐藏
    SHOW_STATUS
    ;
}