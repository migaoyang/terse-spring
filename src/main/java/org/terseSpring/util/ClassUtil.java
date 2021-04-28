package org.terseSpring.util;

import lombok.extern.slf4j.Slf4j;
import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * @author migaoyang
 * @date 2021/4/28 10:18
 * @description 对class操作的工具类
 */
@Slf4j
public class ClassUtil {
    /**
     * @author migaoyang
     * @date 2021/4/28 12:28
     * @param packageName
     * @return java.util.Set<java.lang.Class>
     * @description 获取packageName下的所有class对象
     */
    public static Set<Class> extractClass(String packageName) {
        // 1、获取实际扫描的URL
        URL url = getURL(packageName);
        // 2、获取url下的所有class对象，若未实例化则实例化为class对象
        if (url == null) {
            log.error("not fond package packageName:" + packageName);
            return null;
        }
        Set<Class> classSet = new HashSet<Class>();
        File file = new File(url.getPath());
        searchFile(classSet, packageName, file);
        return classSet;
    }
    /**
     * @author migaoyang
     * @date 2021/4/28 12:15
     * @param classSet
     * @param packageName
     * @param parrentFile
     * @description 搜索class文件，并实例化为class对象，存储在classSet中
     */
    private static void searchFile(Set<Class> classSet, String packageName, File parrentFile) {
        if (parrentFile == null) {
            return;
        }
        //获得目录下所有文件和目录的绝对路径
        File[] files = parrentFile.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            String absolutePath = file.getAbsolutePath();
            // 判断是否是class文件
            if (!file.isDirectory() && absolutePath.endsWith(".class")) {
                String qualifiedName = getQualifiedName(absolutePath, packageName);
                Class clazz = loadClass(qualifiedName);
                classSet.add(clazz);
            } else {
                //对文件夹继续搜索
                searchFile(classSet, packageName, file);
            }
        }
    }

    /**
     * @author migaoyang
     * @date 2021/4/28 12:15
     * @param absolutePath
     * @param packageName
     * @return java.lang.String
     * @description 把绝对路径转为类的全限定名
     */
    private static String getQualifiedName(String absolutePath, String packageName) {
        // 获取class的全限定名
        // absolutePath 为文件的绝对路径+文件名+文件类型
        // 例如 D:\\code\\terse-spring\\src\\main\\java\\org\\terseSpring\\util\\ClassUtil.java
        // 需要转换为org.terseSpring.util.ClassUtil
        absolutePath = absolutePath.replace(File.separator, ".");
        String qualifiedName = absolutePath.substring(absolutePath.indexOf(packageName));
        return qualifiedName.substring(0, qualifiedName.lastIndexOf("."));
    }

    /**
     * @author migaoyang
     * @date 2021/4/28 12:17
     * @param qualifiedName
     * @return java.lang.Class
     * @description 把class文件实例化class对象
     */
    private static Class loadClass(String qualifiedName) {
        try {
            return Class.forName(qualifiedName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            log.error("cannot load class qualifiedName:" + qualifiedName);
        }
        return null;
    }

    /**
     * @param packageName
     * @return java.net.URL
     * @author migaoyang
     * @date 2021/4/28 10:33
     * @description 获取目标URL 根路径+相对路径的包名
     */
    private static URL getURL(String packageName) {
        // 1、获取类加载器
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        // 2、通过类加载器获取URL
        return classLoader.getResource(packageName.replace(".", "/"));
    }

    public static void main(String[] args) {
        String packageName = "demo";
        Set<Class> classSet = extractClass(packageName);
    }
}
