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


    private fun getFileName(map: HashMap<String, String>) {
        val name = map["name"]
        if (name != null) {
            val namePriority = map["name.priority"]
            requireNotNull("name.priority", namePriority)
            val nameMatchStr = map["name.match"]
            requireNotNull("name.match", nameMatchStr)
            val nameMatch = FileName.MatchCondition.valueOf(nameMatchStr!!)
            val fileName = FileName(name, nameMatch)
            fileValue.setNamePriority(fileName, namePriority!!.toInt())
        }


    }


    private fun getFileSize(map: HashMap<String, String>) {
        val size = map["size"]
        if (size != null) {
            val sizePriority = map["size.priority"]
            requireNotNull("size.priority", sizePriority)
            val sizeArray = size.split(",")
            val fileSize = FileSize(sizeArray[0].toInt(), sizeArray[1].toInt())
            fileValue.setSizePriority(fileSize, sizePriority!!.toInt())
        }
    }


    private fun getDatetime(map: HashMap<String, String>) {
        val datetime = map["datetime"]
        if (datetime != null) {
            val datetimePriority = map["datetime.priority"]
            requireNotNull("datetime.priority", datetimePriority)
            val sizeArray = datetime.split(",")
            val start = LocalDateTime.parse(sizeArray[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            val end = LocalDateTime.parse(sizeArray[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            val fileDateTime = FileDateTime(start, end)
            fileValue.setDatetimePriority(fileDateTime, datetimePriority!!.toInt())
        }
    }

    private fun getExtension(map: HashMap<String, String>) {
        val extensionStr = map["extension"]
        if (extensionStr != null) {
            val suffix = map["extension.suffix"]
            if (FileExtension.Extension.DIY.name == extensionStr) {
                requireNotNull("extension.suffix", suffix)
            }
            val extensionPriority = map["extension.priority"]
            requireNotNull("extension.priority", extensionPriority)
            val extension = FileExtension.Extension.valueOf(extensionStr)
            val fileExtension = FileExtension(extension, suffix)
            fileValue.setExtensionPriority(fileExtension, extensionPriority!!.toInt())
        }

    }


    private fun getAuthority(map: HashMap<String, String>) {
        val authorityStr = map["authority"]
        if (authorityStr != null) {
            val authorityPriority = map["authority.priority"]
            requireNotNull("authority.priority", authorityPriority)
            val fileAuthority = FileAuthority.valueOf(authorityStr)
            fileValue.setAuthorityPriority(fileAuthority, authorityPriority!!.toInt())
        }
    }


    private fun getFileType(map: HashMap<String, String>) {
        val fileTypeStr = map["fileType"]
        if (fileTypeStr != null) {
            val fileTypePriority = map["fileType.priority"]
            requireNotNull("fileType.priority", fileTypePriority)
            val fileType = FileType.valueOf(fileTypeStr)
            fileValue.setFileTypePriority(fileType, fileTypePriority!!.toInt())
        }


    }


    private fun getShowStatus(map: HashMap<String, String>) {
        val showStatusStr = map["showStatus"]
        if (showStatusStr != null) {
            val showStatusPriority = map["showStatus.priority"]
            requireNotNull("showStatus.priority", showStatusPriority)
            val fileShowStatus = FileShowStatus.valueOf(showStatusStr)
            fileValue.setShowStatusPriority(fileShowStatus, showStatusPriority!!.toInt())
        }
    }


    private fun requireNotNull(keyName: String, value: String?) {
        Objects.requireNonNull(value, String.format("%s parameter can not null !", keyName))
    }


}