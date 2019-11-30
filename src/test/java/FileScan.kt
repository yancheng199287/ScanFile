package com.oneinlet.file

import java.io.File
import java.util.*
import java.util.concurrent.*
import java.util.function.Supplier
import kotlin.collections.ArrayList
import kotlin.streams.toList

object FileScan {


    /***
     *  使用递归形式遍历文件，即方法反复调用自己，直至中止
     *  优点：代码少，方便简单
     *  缺点：速度一般，如果层级太深，会出现堆栈溢出异常
     *  场景：适合少量层级目录
     *
     *  @param rootListFile 根目录的文件夹的listFiles
     *  @param fileList 装载所有文件的容器，该方法会将将目录下的所有文件和子目录的所有文件装载到该集合中
     *  @return 返回 fileList，装满所有的文件
     * */
    fun traverseFileTreeByRecursive(rootListFile: Array<File>, fileList: ArrayList<File>) {
        for (file in rootListFile) {
            // 因为file的文件目录对象，有可能是空，而又没有其他方法判断是否为空，为了避免多次调用listFiles，我们在这里缓存结果
            // 所以本方法要求传的是一个文件数组，主要是提高性能，实际测试多次调用listFiles非常耗时
            if (file.isDirectory) {
                val files = file.listFiles()
                if (files != null) {
                    this.traverseFileTreeByRecursive(files, fileList)
                }
            } else {
                fileList.add(file)
            }
        }
    }

    /***
     *  使用队列形式遍历文件，即每次将循环得到的文件目录push到队列尾部中去，然后再从队列首部取出，遍历目录的所有文件夹
     *  优点：不会出现堆栈溢出异常，一种非递归形式的遍历
     *  缺点：速度一般，无限循环，占用CPU高
     *  场景：适合少量层级目录
     *
     *  @param path 根目录的文件夹路径
     *  @return 返回 fileList，装满所有的文件
     * */
    fun traverseFileTreeByQueue(path: String): ArrayList<File> {
        val fileList = ArrayList<File>()
        val rootFileDirectory = File(path)
        if (!rootFileDirectory.isDirectory) {
            return fileList
        }
        val rootFiles = rootFileDirectory.listFiles() ?: return fileList
        val filesInDirectory = LinkedList<Array<File>>()
        for (file in rootFiles) {
            if (file.isDirectory) {
                val files = file.listFiles()
                if (files != null) {
                    filesInDirectory.add(files)
                }
            } else {
                fileList.add(file)
            }
        }
        while (!filesInDirectory.isEmpty()) {
            val childListFile = filesInDirectory.pop()
            for (file in childListFile) {
                if (file.isDirectory) {
                    val files = file.listFiles()
                    if (files != null) {
                        filesInDirectory.push(files)
                    }
                } else {
                    fileList.add(file)
                }
            }
        }
        return fileList
    }


    /***
     *  使用队列形式遍历文件，即每次将循环得到的文件目录push到队列尾部中去，然后再从队列首部取出，遍历目录的所有文件夹
     *  两个while循环，可能会导致CPU利用率短时非常高
     *  优点：不会出现堆栈溢出异常，一种非递归形式的遍历,速度非常快
     *  缺点：速度比较快，无限循环，占用CPU高
     *  场景：适合少量层级目录
     *
     *  @param path 根目录的文件夹路径
     *  @return 返回 fileList，装满所有的文件
     * */
    fun traverseFileTreeByQueue1(path: String): ArrayList<File> {
        val fileList = ArrayList<File>()
        val rootFileDirectory = File(path)
        if (!rootFileDirectory.isDirectory) {
            return fileList
        }
        //获取根目录的所有文件
        var rootFiles = rootFileDirectory.listFiles() ?: return fileList
        val filesInDirectory = LinkedList<Array<File>>()
        while (true) {
            var i = 0
            val len = rootFiles.size
            while (i < len) {
                val file = rootFiles[i]
                if (file.isDirectory) {
                    val files = file.listFiles()
                    if (files != null) {
                        filesInDirectory.push(files)
                    }
                } else {
                    fileList.add(file)
                }
                i++
            }
            if (filesInDirectory.size > 0) {
                rootFiles = filesInDirectory.pop()
            } else {
                return fileList
            }
        }
    }


    /***
     *  使用ForkJoinPool，将大任务不断的拆分子任务，最后合并所有结果，每个任务运行在独立线程中
     *  其实这里也是利用了递归的原理，不断的new任务对象，每次遇到一个目录就开启一个任务，直至任务结束提取所有结果
     *  优点：不会出现堆栈溢出异常，一种非递归形式的遍历,速度非常快，多线程并发
     *  缺点：比较耗费内存，少量文件目录不占优势
     *  场景：适合多量层级目录，非常复杂深层目录，如扫描C盘
     *
     *  @param path 根目录的文件夹路径
     *  @return 返回 fileList，装满所有的文件
     * */
    fun traverseFileTreeByForkJoin(path: String): ArrayList<File> {
        val fileList = ArrayList<File>()
        val rootFileDirectory = File(path)
        if (!rootFileDirectory.isDirectory) {
            return fileList
        }
        val rootFiles = rootFileDirectory.listFiles() ?: return fileList
        val task = FileScanTask(rootFiles, fileList)
        val pool = ForkJoinPool.commonPool()
        val future = pool.submit(task)

        // 超时 异常  任务完成  哪个先到 哪个先完成， 这里设置超时是指最大超时时间，如果任务5S结束，这里也会结束阻塞的
        pool.awaitTermination(1, TimeUnit.HOURS)
        return future.get()
    }


    private class FileScanTask(private val files: Array<File>, private val list: ArrayList<File>) : RecursiveTask<ArrayList<File>>() {
        override fun compute(): ArrayList<File> {
            for (file in files) {
                if (file.isDirectory) {
                    val filesChild = file.listFiles()
                    if (filesChild != null) {
                        //这里真正实现了每个目录执行一个子任务，一个子任务就是一个线程处理
                        val fileScanTask = FileScanTask(filesChild, list)
                        fileScanTask.fork()  //把任务放在队列中，并异步执行任务。
                        // fileScanTask.join()  //阻塞当前线程等待获取结果。 如果需要保证每个子任务是按顺序获取结果的，就用join
                    }
                } else {
                    //获取子任务结果
                    list.add(file)
                }
            }
            return list
        }
    }

    /***
     *  使用CompletableFuture，并行遍历，多线程结果合并
     *  优点：不会出现堆栈溢出异常，一种非递归形式的遍历,速度非常快，多线程并发
     *  场景：适合多量层级目录
     *
     *  @param path 根目录的文件夹路径
     *  @return 返回 fileList，装满所有的文件
     * */
    fun traverseFileTreeByCompletableFuture(path: String): ArrayList<File> {
        val filesInDirectory = ArrayList<File>()
        val fileList = ArrayList<File>()
        val rootFileDirectory = File(path)
        if (!rootFileDirectory.isDirectory) {
            return fileList
        }
        val rootFiles = rootFileDirectory.listFiles() ?: return fileList

        for (file in rootFiles) {
            if (file.isDirectory) {
                filesInDirectory.add(file)
            } else {
                fileList.add(file)
            }
        }
        val executorService = Executors.newFixedThreadPool(filesInDirectory.size)
        val cfs = filesInDirectory.stream().map {
            CompletableFuture
                    .supplyAsync(Supplier { traverseFileTreeByFastQueue(it, ArrayList()) }, executorService)
                    .thenAccept {
                        fileList.addAll(it)
                    }
        }.toList()
        CompletableFuture.allOf(*cfs.toTypedArray()).join()
        executorService.shutdown()
        return fileList
    }


    fun traverseFileTreeByFastQueue(rootDirFile: File, fileList: ArrayList<File>): ArrayList<File> {
        var rootFiles = rootDirFile.listFiles() ?: return fileList
        val filesInDirectory = LinkedList<Array<File>>()
        while (true) {
            var i = 0
            val len = rootFiles.size
            while (i < len) {
                val file = rootFiles[i]
                if (file.isDirectory) {
                    val files = file.listFiles()
                    if (files != null) {
                        filesInDirectory.push(files)
                    }
                } else {
                    fileList.add(file)
                }
                i++
            }
            if (filesInDirectory.size > 0) {
                rootFiles = filesInDirectory.pop()
            } else {
                return fileList
            }
        }
    }

}

