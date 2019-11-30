package com.oneinlet

import com.oneinlet.filter.FunnelChain
import java.io.File
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.RecursiveAction
import java.util.concurrent.RecursiveTask
import java.util.concurrent.TimeUnit

/**
 * Created by WangZiHe on 19-11-27
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */
class FileDetector(private val funnelChain: FunnelChain) {
    /***
     *  使用ForkJoinPool，将大任务不断的拆分子任务，最后合并所有结果，每个任务运行在独立线程中
     *  其实这里也是利用了递归的原理，不断的new任务对象，每次遇到一个目录就开启一个任务，直至任务结束提取所有结果
     *  优点：不会出现堆栈溢出异常，一种非递归形式的遍历,速度非常快，多线程并发
     *  缺点：比较耗费内存，少量文件目录不占优势
     *  场景：适合多量层级目录，非常复杂深层目录，如扫描C盘
     *
     *  @param path 根目录的文件夹路径
     *  @param timeout 超时机制，默认一个小时，单位分钟，超时 异常  任务完成  哪个先到 哪个先完成
     *  @return 返回 fileList，装满所有的文件
     * */
    fun traverseFileTreeByForkJoin(path: String, timeout: Long = 60) {
        val rootFileDirectory = File(path)
        if (!rootFileDirectory.isDirectory) {
            throw RuntimeException("$path ，该路径不是一个目录")
        }
        val rootFiles = rootFileDirectory.listFiles() ?: throw RuntimeException("$path ，该目录没有找到任何文件，请检查权限或者是否存在或者是否已损坏")
        val task = FileScanTask(rootFiles, funnelChain)
        val pool = ForkJoinPool.commonPool()
        val future = pool.submit(task)

        // 超时 异常  任务完成  哪个先到 哪个先完成， 这里设置超时是指最大超时时间，如果任务5S结束，这里也会结束阻塞的
        pool.awaitTermination(timeout, TimeUnit.MINUTES)
        future.get()
    }


    private class FileScanTask(private val files: Array<File>, private val funnelChain: FunnelChain) : RecursiveAction() {
        override fun compute() {
            for (file in files) {
                //获取子任务结果
                funnelChain.process(file)
                if (file.isDirectory) {
                    val filesChild = file.listFiles()
                    if (filesChild != null) {
                        //这里真正实现了每个目录执行一个子任务，一个子任务就是一个线程处理
                        val fileScanTask = FileScanTask(filesChild, funnelChain)
                        fileScanTask.fork()
                        //fileScanTask.join()
                    }
                }
            }
        }
    }
}