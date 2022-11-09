//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package filemanage.util;

import filemanage.basic.File;
import filemanage.basic.Folder;

import java.util.Iterator;
import java.util.List;

public class FATUtil {
    public static final int END = -1;
    public static final int ERROR = 255;
    public static final int FREE = 0;
    public static final String DISK = "DISK";
    public static final String FOLDER = "FOLDER";
    public static final String FILE = "FILE";
    public static final String EMPTY = "NULL";
    public static final int FLAGREAD = 0;
    public static final int FLAGWRITE = 1;
    public static final String ICO = "resourses/images/ico.png";
    public static final String FOLDER_IMG = "resourses/images/folder1.png";
    public static final String FILE_IMG = "resourses/images/file1.png";
    public static final String DISK_IMG = "resourses/images/disk.png";
    public static final String TREE_NODE_IMG = "resourses/images/node.png";
    public static final String FORWARD_IMG = "resourses/images/forward.png";
    public static final String BACK_IMG = "resourses/images/back.png";
    public static final String SAVE_IMG = "resourses/images/save.png";
    public static final String CLOSE_IMG = "resourses/images/close.png";

    public FATUtil() {
    }

    public static double getFolderSize(Folder folder) {
        List<Object> children = folder.getChildren();
        double size = 0.0D;
        Iterator var5 = children.iterator();

        while(var5.hasNext()) {
            Object child = var5.next();
            if (child instanceof File) {
                size += ((File)child).getSize();
            } else {
                size += getFolderSize((Folder)child);
            }
        }

        return Double.parseDouble(String.format("%.2f", size));
    }

    public static double getSize(int length) {
        return Double.parseDouble(String.format("%.2f", (double)length / 1024.0D));
    }

    public static int blocksCount(int length) {
        if (length <= 64) {
            return 1;
        } else {
            int n;
            if (length % 64 == 0) {
                n = length / 64;
            } else {
                n = length / 64;
                ++n;
            }
            return n;
        }
    }
}
