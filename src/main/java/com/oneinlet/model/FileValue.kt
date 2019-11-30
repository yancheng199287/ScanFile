package com.oneinlet.model

import com.oneinlet.AppConf
import com.oneinlet.ConfigParse
import com.oneinlet.filter.FunnelChain
import java.time.LocalDateTime
import java.util.HashMap

/**
 * Created by WangZiHe on 19-11-27
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */

/***
 *  扫描条件
 * */
data class FileValue(
        var name: FileName? = null,
        var size: FileSize? = null,
        var datetime: FileDateTime? = null,
        var extension: FileExtension? = null,
        var authority: FileAuthority? = null,
        var fileType: FileType? = null,
        var showStatus: FileShowStatus? = null
) {

    var path: String? = null
    var criteria: FunnelChain.Criteria = FunnelChain.Criteria.AND

    var namePriority: Int = 0
    var sizePriority: Int = 0
    var datetimePriority: Int = 0
    var extensionPriority: Int = 0
    var authorityPriority: Int = 0
    var fileTypePriority: Int = 0
    var showStatusPriority: Int = 0


    fun setNamePriority(name: FileName, namePriority: Int): FileValue {
        this.name = name
        this.namePriority = namePriority

        return this
    }

    fun setSizePriority(fileSize: FileSize, sizePriority: Int): FileValue {
        this.size = fileSize
        this.sizePriority = sizePriority
        return this
    }

    fun setDatetimePriority(fileDateTime: FileDateTime, datetimePriority: Int): FileValue {
        this.datetime = fileDateTime
        this.datetimePriority = datetimePriority
        return this
    }

    fun setExtensionPriority(fileExtension: FileExtension, extensionPriority: Int): FileValue {
        this.extension = fileExtension
        this.extensionPriority = extensionPriority
        return this
    }

    fun setAuthorityPriority(fileAuthority: FileAuthority, authorityPriority: Int): FileValue {
        this.authorityPriority = authorityPriority
        this.authority = fileAuthority
        return this
    }

    fun setFileTypePriority(fileType: FileType, fileTypePriority: Int): FileValue {
        this.fileTypePriority = fileTypePriority
        this.fileType = fileType
        return this
    }


    fun setShowStatusPriority(fileShowStatus: FileShowStatus, showStatusPriority: Int): FileValue {
        this.showStatusPriority = showStatusPriority
        this.showStatus = fileShowStatus
        return this
    }

    fun parse(): FileValue {
        ConfigParse(this, AppConf.getStringValue()).parse()
        return this
    }

}

data class FileName(
        val value: String,
        val matchCondition: MatchCondition

) {

    enum class MatchCondition {
        EQUALS,
        CONTAIN,
        STARTING_WITH,
        ENDING_WITH,

    }

}


data class FileSize(
        val startSize: Int,
        val endSize: Int
)


data class FileDateTime(
        val startDateTime: LocalDateTime,
        val endDateTime: LocalDateTime
)


enum class FileAuthority {
    ALL,
    READ,
    WRITE
}


enum class FileType {
    ALL,
    FILE,
    DIRECTORY,
    LINK     //boolean isSymbolicLink =Files.isSymbolicLink（file）;  Files.readSymbolicLink（link）
}

enum class FileShowStatus {
    ALL,
    SHOW,
    HIDDEN
}

data class FileExtension(private val extension: Extension, private val otherSuffix: String? = null) {

    enum class Extension(val suffix: String) {
        COMPRESSION("zip,rar,7z,tar,gz"),
        DOCUMENT("txt,md,rtf,doc,log,pdf"),
        IMAGE("jpg,jpeg,png,bmp,gif,psd,ico,tga,svg,fpx,raw"),
        VOICE("mp3,aac,wav,wma,mid,flac,ape"),
        VIDEO("mp4,avi,mkv,rmvb,flv,mpg"),
        DIY(""),

    }

    fun getSuffixList(): List<String> {
        val strSuffix = if (extension == Extension.DIY) {
            otherSuffix!!
        } else {
            extension.suffix.plus(",").plus(otherSuffix!!)
        }
        return strSuffix.split(",")
    }

}