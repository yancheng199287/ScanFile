import com.oneinlet.AppConf
import com.oneinlet.ConfigParse
import com.oneinlet.FileScanner
import com.oneinlet.export.PrintFileExport
import com.oneinlet.filter.ExtensionFunnel
import com.oneinlet.filter.Funnel
import com.oneinlet.filter.FunnelChain
import com.oneinlet.filter.NameFunnel
import com.oneinlet.model.*
import org.junit.Test
import java.io.File
import java.time.LocalDateTime

/**
 * Created by WangZiHe on 19-11-27
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */

class Test02 {


    @Test
    fun asddddddddd() {
        var fileValue = FileValue()
        val configParse = ConfigParse(fileValue,AppConf.getStringValue())
        configParse.parse()
        println(fileValue.path)

        val funnelChain = FunnelChain.builder()
                .setCriteria(fileValue.criteria)
                .setFileValueAndAddFunnel(fileValue)
                .build()

        val fileScanner = FileScanner(fileValue.path!!, funnelChain)
        fileScanner.scan()

    }


    @Test
    fun s555656asasas() {
        println("文件扫描...")

        val fileValue = FileValue()
        fileValue.name = FileName("w", FileName.MatchCondition.CONTAIN)
        fileValue.size = FileSize(100, 3000)
        fileValue.datetime = FileDateTime(LocalDateTime.now(), LocalDateTime.now())
        fileValue.extension = FileExtension(FileExtension.Extension.DOCUMENT, "java")
        fileValue.authority = FileAuthority.ALL
        fileValue.fileType = FileType.ALL
        fileValue.showStatus = FileShowStatus.SHOW
        val path = "/home/yancheng/ownCloud"

        val funnelChain = FunnelChain.builder()
                .setCriteria(FunnelChain.Criteria.AND)
                .setExport(PrintFileExport())
                .addFunnel(object : Funnel {
                    override fun funnelProcess(file: File): Boolean {
                        return file.isFile
                    }
                })
                .addFunnel(NameFunnel(fileValue.name!!))
                .addFunnel(ExtensionFunnel(fileValue.extension!!.getSuffixList()))
                .build()
        val fileScanner = FileScanner(path, funnelChain)
        fileScanner.scan()
    }


}