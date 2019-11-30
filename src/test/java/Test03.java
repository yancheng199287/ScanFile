import com.oneinlet.FileScanner;
import com.oneinlet.export.PrintFileExport;
import com.oneinlet.filter.*;
import com.oneinlet.model.*;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.File;
import java.time.LocalDateTime;

/**
 * Created by WangZiHe on 19-11-29
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */
public class Test03 {


    @Test
    public void simple01() {
        FileValue fileValue = new FileValue();
        fileValue.setNamePriority(new FileName("a", FileName.MatchCondition.CONTAIN), 1);
        fileValue.setExtensionPriority(new FileExtension(FileExtension.Extension.DIY, "java"), 2);
        fileValue.setSizePriority(new FileSize(100, 1024 * 5), 3);
        fileValue.setAuthorityPriority(FileAuthority.READ, 4);
        fileValue.setFileTypePriority(FileType.FILE, 5);
        fileValue.setShowStatusPriority(FileShowStatus.HIDDEN, 6);

        LocalDateTime start = LocalDateTime.now().withHour(12);
        LocalDateTime end = LocalDateTime.now().withHour(15);
        fileValue.setDatetimePriority(new FileDateTime(start, end), 7);

        FunnelChain funnelChain = FunnelChain.Companion.builder()
                .setCriteria(FunnelChain.Criteria.AND)
                .setExport(new PrintFileExport())
                .addFunnel(file -> file.isFile(), 0)
                .setFileValueAndAddFunnel(fileValue)
                .build();
        String path = "/home/yancheng/ownCloud";
        String path1 = "D:\\workspace\\kotlin";

        FileScanner fileScanner = new FileScanner(path1, funnelChain);
        fileScanner.scan();
    }


    @Test
    public void simple() {
        FileValue fileValue = new FileValue();
        fileValue.setName(new FileName("a", FileName.MatchCondition.CONTAIN));
        fileValue.setExtension(new FileExtension(FileExtension.Extension.DIY, "java"));
        fileValue.setSize(new FileSize(100, 1024 * 5));
        fileValue.setAuthority(FileAuthority.READ);
        fileValue.setFileType(FileType.FILE);
        fileValue.setShowStatus(FileShowStatus.HIDDEN);
        LocalDateTime start = LocalDateTime.now().withHour(12);
        LocalDateTime end = LocalDateTime.now().withHour(15);
        fileValue.setDatetime(new FileDateTime(start, end));
        FunnelChain funnelChain = FunnelChain.Companion.builder()
                .setCriteria(FunnelChain.Criteria.AND)
                .setExport(new PrintFileExport())
                .addFunnel(file -> file.isFile(), 0)
                .addFunnel(new NameFunnel(fileValue.getName()), 1)
                .addFunnel(new ExtensionFunnel(fileValue.getExtension().getSuffixList()), 2)
                .addFunnel(new SizeFunnel(fileValue.getSize()), 3)
                .addFunnel(new AuthorityFunnel(fileValue.getAuthority()), 4)
                .addFunnel(new FileTypeFunnel(fileValue.getFileType()), 5)
                .addFunnel(new ShowStatusFunnel(fileValue.getShowStatus()), 6)
                .addFunnel(new DatetimeFunnel(fileValue.getDatetime()), 7)
                .build();
        String path = "/home/yancheng/ownCloud";
        FileScanner fileScanner = new FileScanner(path, funnelChain);
        fileScanner.scan();
    }
}
