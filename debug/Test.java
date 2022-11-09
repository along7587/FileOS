//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package debug;

import filemanage.basic.FAT;

import java.io.IOException;

public class Test {
    public Test() {
    }

    public static void main(String[] args) {
        FAT fat = new FAT();
        fat.create_file("C:");
        fat.create_file("C:");
        fat.create_folder("C:");
        fat.create_folder("C:\\文夹1");
        fat.create_folder("C:\\文夹1");
        fat.create_file("C:\\文夹1");
        fat.create_folder("C:\\文夹1\\文夹2");
        fat.delete(fat.getDiskBlocks()[6]);
        fat.create_folder("C:\\文夹1");
        fat.create_file("C:\\文夹1\\文夹1");
        fat.create_folder("C:\\文夹1");
        fat.create_folder("C:\\文夹1\\文夹1");
        fat.create_folder("C:\\文夹1\\文夹1");
        fat.create_file("C:\\文夹1\\文夹1\\文夹2");

        try {
            fat.saveData(fat);
        } catch (ClassNotFoundException var3) {
            var3.printStackTrace();
        } catch (IOException var4) {
            var4.printStackTrace();
        }

    }
}
