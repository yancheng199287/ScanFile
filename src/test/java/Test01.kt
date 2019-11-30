import com.oneinlet.file.FileScan
import org.junit.Test
import java.io.File

/**
 * Created by WangZiHe on 19-11-21
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */

class Test01 {


    val path = "/home/yancheng/Desktop/pandora"
    val path1 = "/home/yancheng" //较大
    val path2 = "/home/yancheng/Desktop"
    val path3 = "/media/yancheng/文档/open-source/elasticsearch-6.6.1"
    val path4 = "/home/yancheng/ownCloud"
    val file = File(path4)

    var fileList = ArrayList<File>()

    @Test
    fun test001() {
        FileScan.traverseFileTreeByRecursive(file.listFiles(), fileList)
        printlnFilePath(fileList)
    }


    @Test
    fun test002() {
        fileList = FileScan.traverseFileTreeByQueue(path4)
        printlnFilePath(fileList)
    }

    @Test
    fun test003() {
        fileList = FileScan.traverseFileTreeByQueue1("/home/yancheng/ownCloud")
        printlnFilePath(fileList)
    }


    @Test
    fun test004() {
        fileList = FileScan.traverseFileTreeByForkJoin(path4)
        printlnFilePath(fileList)
    }

    @Test
    fun test005() {
        fileList = FileScan.traverseFileTreeByCompletableFuture(path4)
        printlnFilePath(fileList)
    }

    @Test
    fun test006() {
        fileList = FileScan.traverseFileTreeByFastQueue(File(path4), fileList)
        printlnFilePath(fileList)
    }


    fun printlnFilePath(fileList: List<File>) {
        //fileList.forEach { println(it.path) }
        println("总文件数量:${fileList.size}")
    }

}