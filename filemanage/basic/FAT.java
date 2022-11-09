//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package filemanage.basic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import filemanage.util.FATUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//FXCollections 实用程序类映射到java.util.Collections
//ObservableList 允许跟踪更改的列表
//ArrayList 类是一个可以动态修改的数组,没有固定大小的限制，可以添加或删除元素
//Iterator（迭代器） 是一个接口，它的作用就是遍历容器的所有元素，也是 Java 集合框架的成员，但它与 Collection 和 Map 系列的集合不一样，Collection 和 Map 系列集合主要用于盛装其他对象，而 Iterator 则主要用于遍历（即迭代访问）Collection 集合中的元素。
//List接口是一个有序的集合，允许按顺序存储和访问元素
public class FAT implements Serializable {
    public static FAT fat;
    private static final long serialVersionUID = 1L;
    private DiskBlock[] diskBlocks;
    private transient ObservableList<File> openedFiles;
    private Folder root = new Folder("C:", "root", 0, (Folder)null);
    private Path rootPath = new Path("C:", (Path)null);
    private List<Path> paths;

    public FAT() {
        Folder fat = new Folder("FAT", "FAT", 0, (Folder)null);
        this.diskBlocks = new DiskBlock[128];
        this.diskBlocks[0] = new DiskBlock(0, -1, "FAT", fat);
        this.diskBlocks[0].setBegin(true);
        this.diskBlocks[1] = new DiskBlock(1, -1, "FAT", fat);
        this.diskBlocks[2] = new DiskBlock(2, -1, "DISK", this.root);

        for(int i = 3; i < 128; ++i) {
            this.diskBlocks[i] = new DiskBlock(i, 0, "NULL", (Object)null);
        }

        this.openedFiles = FXCollections.observableArrayList(new ArrayList());
        this.paths = new ArrayList();
        this.paths.add(this.rootPath);
        this.root.setPath(this.rootPath);
    }

    public static FAT getNewFAT() {
        if (fat == null) {
            fat = new FAT();
        }
        return fat;
    }

    public void addFilesOpened(DiskBlock block) {
        File thisFile = (File)block.getObject();
        this.openedFiles.add(thisFile);
        thisFile.setOpened(true);
    }

    public void removeFilesOpened(DiskBlock block) {
        File thisFile = (File)block.getObject();

        for(int i = 0; i < this.openedFiles.size(); ++i) {
            if (this.openedFiles.get(i) == thisFile) {
                this.openedFiles.remove(i);
                thisFile.setOpened(false);
                break;
            }
        }

    }

    public int create_folder(String path) {
        String folderName = null;
        boolean canName = true;
        int index = 1;

        int index2;
        Folder parent;
        do {
            folderName = "文夹";
            canName = true;
            folderName = folderName + index;

            for(index2 = 2; index2 < this.diskBlocks.length; ++index2) {
                if (!this.diskBlocks[index2].isFree() && this.diskBlocks[index2].getType().equals("FOLDER")) {
                    parent = (Folder)this.diskBlocks[index2].getObject();
                    if (path.equals(parent.getLocation()) && folderName.equals(parent.getFolderName())) {
                        canName = false;
                    }
                }
            }

            ++index;
        } while(!canName);

        index2 = this.gethEmptyBlock();
        if (index2 == 255) {
            return 255;
        } else {
            parent = this.getFolder(path);
            Folder folder = new Folder(folderName, path, index2, parent);
            if (parent instanceof Folder) {
                parent.addChildren(folder);
            }

            this.diskBlocks[index2].updateBlock(-1, "FOLDER", folder, true);
            Path parentP = this.getPath(path);
            Path thisPath = new Path(path + "\\" + folderName, parentP);
            if (parentP != null) {
                parentP.addChildren(thisPath);
            }

            this.paths.add(thisPath);
            folder.setPath(thisPath);
            return index2;
        }
    }

    public int create_file(String path) {
        String fileName = null;
        boolean canName = true;
        int index = 1;

        int index2;
        do {
            fileName = "文件";
            canName = true;
            fileName = fileName + index;

            for(index2 = 2; index2 < this.diskBlocks.length; ++index2) {
                if (!this.diskBlocks[index2].isFree() && this.diskBlocks[index2].getType().equals("FILE")) {
                    File file = (File)this.diskBlocks[index2].getObject();
                    if (path.equals(file.getLocation()) && fileName.equals(file.getFileName())) {
                        canName = false;
                    }
                }
            }

            ++index;
        } while(!canName);

        index2 = this.gethEmptyBlock();
        if (index2 == 255) {
            return 255;
        } else {
            Folder parent = this.getFolder(path);
            File file = new File(fileName, path, index2, parent);
            file.setFlag(1);
            if (parent instanceof Folder) {
                parent.addChildren(file);
            }

            this.diskBlocks[index2].updateBlock(-1, "FILE", file, true);
            return index2;
        }
    }

    public int delete(DiskBlock block) {
        int i;
        if (block.getObject() instanceof File) {
            if (this.isOpenedFile(block)) {
                return 3;
            } else {
                File thisFile = (File)block.getObject();
                Folder parent = thisFile.getParent();
                if (parent instanceof Folder) {
                    parent.removeChildren(thisFile);
                    parent.setSize(FATUtil.getFolderSize(parent));

                    while(parent.hasParent()) {
                        parent = parent.getParent();
                        parent.setSize(FATUtil.getFolderSize(parent));
                    }
                }

                for(i = 2; i < this.diskBlocks.length; ++i) {
                    if (!this.diskBlocks[i].isFree() && this.diskBlocks[i].getObject() instanceof File && ((File)this.diskBlocks[i].getObject()).equals(thisFile)) {
                        this.diskBlocks[i].clearBlock();
                    }
                }

                return 1;
            }
        } else {
            String folderPath = ((Folder)block.getObject()).getLocation() + "\\" + ((Folder)block.getObject()).getFolderName();
            int index = 0;

            for(i = 2; i < this.diskBlocks.length; ++i) {
                if (!this.diskBlocks[i].isFree()) {
                    Object obj = this.diskBlocks[i].getObject();
                    if (this.diskBlocks[i].getType().equals("FOLDER") && ((Folder)obj).getLocation().equals(folderPath)) {
                        return 2;
                    }

                    if (this.diskBlocks[i].getType().equals("FILE") && ((File)obj).getLocation().equals(folderPath)) {
                        return 2;
                    }

                    if (this.diskBlocks[i].getType().equals("FOLDER") && ((Folder)this.diskBlocks[i].getObject()).equals(block.getObject())) {
                        index = i;
                    }
                }
            }

            Folder thisFolder = (Folder)block.getObject();
            Folder parent = thisFolder.getParent();
            if (parent instanceof Folder) {
                parent.removeChildren(thisFolder);
                parent.setSize(FATUtil.getFolderSize(parent));
            }

            this.paths.remove(this.getPath(folderPath));
            this.diskBlocks[index].clearBlock();
            return 0;
        }
    }

    public int gethEmptyBlock() {
        for(int i = 2; i < this.diskBlocks.length; ++i) {
            if (this.diskBlocks[i].isFree()) {
                return i;
            }
        }

        return 255;
    }

    public int usedBlocksCount() {
        int n = 0;

        for(int i = 2; i < this.diskBlocks.length; ++i) {
            if (!this.diskBlocks[i].isFree()) {
                ++n;
            }
        }

        return n;
    }

    public void saveData(FAT fat) throws IOException, ClassNotFoundException {
        ObjectOutputStream save = new ObjectOutputStream(new FileOutputStream("Data"));
        save.writeObject(fat);
        System.out.println("保存成功！");
    }

    public int freeBlocksCount() {
        int n = 0;

        for(int i = 2; i < this.diskBlocks.length; ++i) {
            if (this.diskBlocks[i].isFree()) {
                ++n;
            }
        }

        return n;
    }

    public boolean reallocBlocks(int num, DiskBlock block) {
        File thisFile = (File)block.getObject();
        int begin = thisFile.getDiskNum();
        int index = this.diskBlocks[begin].getIndex();

        int oldNum;
        for(oldNum = 1; index != -1; index = this.diskBlocks[index].getIndex()) {
            ++oldNum;
            if (this.diskBlocks[index].getIndex() == -1) {
                begin = index;
            }
        }

        int end;
        int next;
        int i;
        if (num > oldNum) {
            end = num - oldNum;
            if (this.freeBlocksCount() < end) {
                return false;
            }

            next = this.gethEmptyBlock();
            this.diskBlocks[begin].setIndex(next);

            for(i = 1; i <= end; ++i) {
                next = this.gethEmptyBlock();
                if (i == end) {
                    this.diskBlocks[next].updateBlock(-1, "FILE", thisFile, false);
                } else {
                    this.diskBlocks[next].updateBlock(-1, "FILE", thisFile, false);
                    int space2 = this.gethEmptyBlock();
                    this.diskBlocks[next].setIndex(space2);
                }
            }
        } else if (num < oldNum) {
            for(end = thisFile.getDiskNum(); num > 1; --num) {
                end = this.diskBlocks[end].getIndex();
            }


            for(i = this.diskBlocks[end].getIndex(); i != -1; i = next) {
                next = this.diskBlocks[i].getIndex();
                this.diskBlocks[i].clearBlock();
            }

            this.diskBlocks[end].setIndex(-1);
        }

        thisFile.setLength(num);
        return true;
    }

    public List<Folder> getFolders(String path) {
        List<Folder> list = new ArrayList();

        for(int i = 2; i < this.diskBlocks.length; ++i) {
            if (!this.diskBlocks[i].isFree() && this.diskBlocks[i].getObject() instanceof Folder && ((Folder)this.diskBlocks[i].getObject()).getLocation().equals(path)) {
                list.add((Folder)this.diskBlocks[i].getObject());
            }
        }

        return list;
    }

    public List<DiskBlock> getBlockBegin(String path) {
        List<DiskBlock> bList = new ArrayList();

        int i;
        for(i = 2; i < this.diskBlocks.length; ++i) {
            if (!this.diskBlocks[i].isFree() && this.diskBlocks[i].getObject() instanceof Folder && ((Folder)this.diskBlocks[i].getObject()).getLocation().equals(path) && this.diskBlocks[i].isBegin()) {
                bList.add(this.diskBlocks[i]);
            }
        }

        for(i = 2; i < this.diskBlocks.length; ++i) {
            if (!this.diskBlocks[i].isFree() && this.diskBlocks[i].getObject() instanceof File && ((File)this.diskBlocks[i].getObject()).getLocation().equals(path) && this.diskBlocks[i].isBegin()) {
                bList.add(this.diskBlocks[i]);
            }
        }

        return bList;
    }

    public Folder getFolder(String path) {
        if (path.equals("C:")) {
            return this.root;
        } else {
            int split = path.lastIndexOf(92);
            String location = path.substring(0, split);
            String folderName = path.substring(split + 1);
            List<Folder> folders = this.getFolders(location);
            Iterator var7 = folders.iterator();

            while(var7.hasNext()) {
                Folder folder = (Folder)var7.next();
                if (folder.getFolderName().equals(folderName)) {
                    return folder;
                }
            }

            return null;
        }
    }

    public Path getPath(String path) {
        Iterator var3 = this.paths.iterator();

        while(var3.hasNext()) {
            Path p = (Path)var3.next();
            if (p.getPathName().equals(path)) {
                return p;
            }
        }

        return null;
    }

    public DiskBlock[] getDiskBlocks() {
        return this.diskBlocks;
    }

    public void setDiskBlocks(DiskBlock[] diskBlocks) {
        this.diskBlocks = diskBlocks;
    }

    public DiskBlock getBlock(int index) {
        return this.diskBlocks[index];
    }

    public ObservableList<File> getOpenedFiles() {
        return this.openedFiles;
    }

    public void setOpenedFiles(ObservableList<File> openFiles) {
        this.openedFiles = openFiles;
    }

    public List<Path> getPaths() {
        return this.paths;
    }

    public void setPaths(List<Path> paths) {
        this.paths = paths;
    }

    public void addPath(Path path) {
        this.paths.add(path);
    }

    public void removePath(Path path) {
        this.paths.remove(path);
        if (path.hasParent()) {
            path.getParent().removeChildren(path);
        }

    }

    public void replacePath(Path oldPath, String newName) {
        oldPath.setPathName(newName);
    }

    public boolean hasPath(Path path) {
        Iterator var3 = this.paths.iterator();

        while(var3.hasNext()) {
            Path p = (Path)var3.next();
            if (p.equals(path)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasSame(String path, String name) {
        Folder thisFolder = this.getFolder(path);
        Iterator var5 = thisFolder.getChildren().iterator();

        while(var5.hasNext()) {
            Object child = var5.next();
            if (child.toString().equals(name)) {
                return true;
            }
        }

        return false;
    }

    public boolean isOpenedFile(DiskBlock block) {
        return block.getObject() instanceof Folder ? false : ((File)block.getObject()).isOpened();
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.openedFiles = FXCollections.observableArrayList(new ArrayList());
    }
}
