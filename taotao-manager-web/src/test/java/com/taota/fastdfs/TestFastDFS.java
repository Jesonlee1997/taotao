package com.taota.fastdfs;

import com.taotao.utils.FastDFSClient;
import org.csource.fastdfs.*;
import org.junit.Test;

/**
 * Created by JesonLee
 * on 2017/4/15.
 */
public class TestFastDFS {
    @Test
    public void testUploadFile() throws Exception {
        //1.向工程中添加jar包
        //2.创建配置文件。配置tracker服务器的地址
        //3.加载配置文件
        ClientGlobal.init("J:\\Java\\projects\\taotao\\taotao-manager-web\\src\\main\\resources\\resource\\client.conf");
        //4.创建一个TrackerClient对象
        TrackerClient trackerClient = new TrackerClient();
        //5.使用TrackerClient获得TrackerServer对象
        TrackerServer  trackerServer = trackerClient.getConnection();
        //6.创建一个StorageServer的引用
        StorageServer storageServer = null;
        //7.创建一个StorageClient
        StorageClient storageClient = new StorageClient(trackerServer, storageServer);
        String[] strings = storageClient.upload_file("J:\\Java\\projects\\taotao\\taotao-manager-web\\src\\main\\java\\20.jpg",
                "jpg", null);
        for (String string : strings) {
            System.out.println(string);
        }
    }

    @Test
    public void testUploadFile2() throws Exception {
        System.out.println(new FastDFSClient("classpath:resource/client.conf").uploadFileByClasspath("21.jpg"));

    }


}
