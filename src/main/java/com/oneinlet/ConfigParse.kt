package com.oneinlet

import com.oneinlet.filter.FunnelChain
import com.oneinlet.model.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Created by WangZiHe on 19-11-29
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */
data class ConfigParse(
        private val fileValue: FileValue,
        private val map: HashMap<String, String>
) {


    fun parse(): FileValue {
        val criteriaStr = map["criteria"]
        val path = map["path"]
        requireNotNull(path)
        fileValue.criteria = FunnelChain.Criteria.valueOf(criteriaStr!!)
        fileValue.path = path
        getFileName(map)
        getFileSize(map)
        getDatetime(map)
        getExtension(map)
        getAuthority(map)
        getFileType(map)
        getShowStatus(map)
        return fileValue
    }


    fun getFileName(map: HashMap<String, String>) {
        val name = map["name"]
        if (name != null) {
            val namePriority = map["name.priority"]
            requireNotNull("name.priority", namePriority!!)

            val nameMatchStr = map["name.match"]
            Objects.requireNonNull(namePriority, "name.priority 不能为空")
            val nameMatch = FileName.MatchCondition.valueOf(nameMatchStr!!)
            val fileName = FileName(name!!, nameMatch)
            fileValue.setNamePriority(fileName, namePriority!!.toInt())
        }


    }


    fun getFileSize(map: HashMap<String, String>) {
        val size = map["size"]
        val sizePriority = map["size.priority"]
        val sizeArray = size!!.split(",")
        val filesize = FileSize(sizeArray[0].toInt(), sizeArray[1].toInt())
        fileValue.setSizePriority(filesize, sizePriority!!.toInt())
    }


    fun getDatetime(map: HashMap<String, String>) {
        val datetime = map["datetime"]
        val datetimePriority = map["datetime.priority"]
        val sizeArray = datetime!!.split(",")

        val start = LocalDateTime.parse(sizeArray[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

        val end = LocalDateTime.parse(sizeArray[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val fileDateTime = FileDateTime(start, end)

        fileValue.setDatetimePriority(fileDateTime, datetimePriority!!.toInt())
    }

    fun getExtension(map: HashMap<String, String>) {
        val extensionStr = map["extension"]
        val suffix = map["extension.suffix"]
        val extensionPriority = map["extension.priority"]

        val extension = FileExtension.Extension.valueOf(extensionStr!!)
        val fileExtension = FileExtension(extension, suffix)
        fileValue.setExtensionPriority(fileExtension, extensionPriority!!.toInt())
    }


    fun getAuthority(map: HashMap<String, String>) {
        val authorityStr = map["authority"]
        val authorityPriority = map["authority.priority"]
        val fileAuthority = FileAuthority.valueOf(authorityStr!!)
        fileValue.setAuthorityPriority(fileAuthority, authorityPriority!!.toInt())
    }


    fun getFileType(map: HashMap<String, String>) {
        val fileTypeStr = map["fileType"]
        val authorityPriority = map["fileType.priority"]
        val fileType = FileType.valueOf(fileTypeStr!!)
        fileValue.setFileTypePriority(fileType, authorityPriority!!.toInt())
    }


    fun getShowStatus(map: HashMap<String, String>) {
        val showStatusStr = map["showStatus"]
        val showStatusPriority = map["showStatus.priority"]
        val fileShowStatus = FileShowStatus.valueOf(showStatusStr!!)
        fileValue.setShowStatusPriority(fileShowStatus, showStatusPriority!!.toInt())
    }


    fun requireNotNull(keyName: String, value: String) {
        Objects.requireNonNull(value, String.format("%s parameter can not null !", keyName))
    }


}