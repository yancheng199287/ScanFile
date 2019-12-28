package com.oneinlet.filter

import com.oneinlet.export.FileExport
import com.oneinlet.export.PrintFileExport
import com.oneinlet.model.FileValue
import java.io.File
import java.lang.RuntimeException
import java.util.*

/**
 * Created by WangZiHe on 19-11-27
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */

open class FunnelChain {

    private val map: MutableMap<Int, Funnel> = TreeMap(naturalOrder())
    private var size: Int = 0
    private var criteria: Criteria = Criteria.AND
    private var fileExport: FileExport = PrintFileExport()

    companion object {
        fun builder(): FunnelChain {
            return FunnelChain()
        }
    }

    fun setCriteria(criteria: Criteria): FunnelChain {
        this.criteria = criteria
        return this
    }

    fun setExport(fileExport: FileExport): FunnelChain {
        this.fileExport = fileExport
        return this
    }


    fun addFunnel(funnel: Funnel, priority: Int = 0): FunnelChain {
        val priority1 = if (priority == 0) {
            map.size + 1
        } else {
            priority
        }
        map[priority1] = funnel
        return this
    }


    fun setFileValueAndAddFunnel(fileValue: FileValue): FunnelChain {

        if (fileValue.name != null) {
            addFunnel(NameFunnel(fileValue.name!!), fileValue.namePriority)
        }

        if (fileValue.extension != null) {
            addFunnel(ExtensionFunnel(fileValue.extension!!.getSuffixList()), fileValue.extensionPriority)
        }

        if (fileValue.size != null) {
            addFunnel(SizeFunnel(fileValue.size!!), fileValue.sizePriority)
        }

        if (fileValue.authority != null) {
            addFunnel(AuthorityFunnel(fileValue.authority!!), fileValue.authorityPriority)
        }

        if (fileValue.fileType != null) {
            addFunnel(FileTypeFunnel(fileValue.fileType!!), fileValue.fileTypePriority)
        }

        if (fileValue.showStatus != null) {
            addFunnel(ShowStatusFunnel(fileValue.showStatus!!), fileValue.showStatusPriority)
        }

        if (fileValue.datetime != null) {
            addFunnel(DatetimeFunnel(fileValue.datetime!!), fileValue.datetimePriority)
        }

        return this
    }


    fun build(): FunnelChain {
        size = map.size
        return this
    }


    fun process(file: File) {
        var index = 1
        for (en in map.entries) {
            //  println("index: $index == $size 优先级：${en.key}-> 漏斗器：${en.value} ,file:${file.name}")
            if (criteria == Criteria.AND) {
                val pass = en.value.funnelProcess(file)

                if (!pass) {
                    return
                }
                if (index == size && pass) {
                    fileExport.handleFile(file)
                }
            } else if (criteria == Criteria.OR) {
                if (en.value.funnelProcess(file)) {
                    fileExport.handleFile(file)
                }
            } else if (criteria == Criteria.NOT) {
                val pass = en.value.funnelProcess(file)
                if (pass) {
                    return
                }
                if (index == size && !pass) {
                    fileExport.handleFile(file)
                }
            }
            index++
        }
    }


    enum class Criteria {
        AND,
        OR,
        NOT
    }

}