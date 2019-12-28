# ScanFile
Find files quickly and instantly the way you like！


```
YC项目暂时处于完善测试阶段，暂未推送到maven仓库，后期推送。所以现在需要手动打包到本地仓库。

打包命令：
mvn install:install-file -DgroupId=com.oneinlet -DartifactId=Scan-File -Dversion=V1.0.0 -Dpackaging=jar -Dfile=scan-file-v1.0.0.jar

在pom文件中引用
   <dependency>
            <groupId>com.oneinlet</groupId>
            <artifactId>Scan-File</artifactId>
            <version>V1.0.0</version>
   </dependency>
```