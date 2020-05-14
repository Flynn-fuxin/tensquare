package com.tensquare.spit.mongoDB;
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Date;

public class MongoFirstTest {
    public static void testInsert(){
        /*//创建一个连接，自带连接池效果
        MongoClient mongoClient = MongoClients.create("mongodb://localhost");
        //操作一个数据库，如果数据库不存在，当您为该数据库首次存储数据时，会自动创建数据库。
        MongoDatabase spitdb = mongoClient.getDatabase("flynn");
        //得到一个集合对象，如果集合不存在，当您为该数据库首次存储数据时，会自动创建集合。
        MongoCollection<Document> spit = spitdb.getCollection("spit");
        //创建一个文档，参数可以接收键值对，也可以直接接收一个Map对象
        //文档对象的本质是BSON类型，该类型对应java.util.Map;BSON数组对应的是java.util.List
        Document document = new Document("content", "原生驱动操作API")
                .append("userid", "8888")
                .append("visits", 1234)
                .append("publishtime", new Date());
        //插入一个文档，如果文档没有指定_id，则自动生成_id和值
        // 如果批量插入，使用insertMany(list)
        spit.insertOne(document);
        //释放资源
        mongoClient.close();*/
    }

    public static void main(String[] args) {
        testInsert();
    }
}
