package com.zhuang.test;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

public class App_02 {

    //定义搜索办法
    private void search(Query query) throws IOException {
        // 查询语法
        System.out.println("查询语法：" + query);
        //创建索引库存目录
        Directory directory =FSDirectory.open(new File("D:\\zuoye\\Lucene"));
        // 创建IndexReader读取索引库对象
        IndexReader indexReader = DirectoryReader.open(directory);
        // 创建IndexSearcher，执行搜索索引库
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        /**
         * search方法：执行搜索
         * 参数一：查询对象
         * 参数二：指定搜索结果排序后的前n个（前10个）
         */
    TopDocs topDocs = indexSearcher.search(query,10);
    //处理结果
        System.out.println("总命中的记录数：" + topDocs.totalHits);
        // 获取搜索到得文档数组
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        // ScoreDoc对象：只有文档id和分值信息
        for (ScoreDoc scoreDoc : scoreDocs) {
            System.out.println("-------华丽分割线----------");
            System.out.println("文档id：" + scoreDoc.doc + "文档分值：" + scoreDoc.score);
            // 根据文档id获取指定的文档
            Document doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println("图书Id:" + doc.get("id"));
            System.out.println("图书的名称:" + doc.get("bookName"));
            System.out.println("图书价格:" + doc.get("bookPrice"));
            System.out.println("图书图片:" + doc.get("bookPic"));
            System.out.println("图书描述:" + doc.get("bookDesc"));
        }
          indexReader.close();
    }

    @Test
    public void testTermQuery() throws IOException {
        TermQuery q =new TermQuery(new Term("bookName","java"));
        //执行搜索
        search(q);
    }

    @Test
    public void testNumericRangeQuery() throws IOException {
        Query q = NumericRangeQuery.newDoubleRange("bookPrice",80d,100d,false,false);
        search(q);
    }

    @Test
    public void testBooleanQuery() throws IOException {
        TermQuery q1 =new TermQuery(new Term("bookName","java"));
        // 创建查询对象二
        Query q2 = NumericRangeQuery.newDoubleRange("bookPric",80d,100d,true,true);

        // 创建组合查询条件对象
        BooleanQuery q =new BooleanQuery();
        q.add(q1,BooleanClause.Occur.MUST);
        q.add(q2,BooleanClause.Occur.MUST);

        search(q);
    }
    @Test
    public void testQueryParser() throws ParseException, IOException {
        // 创建分析器，用于分词
        Analyzer analyzer =new IKAnalyzer();
        // 创建QueryParser解析对象
        QueryParser queryParser = new QueryParser("bookName",analyzer);
        Query q = queryParser.parse("bookName:java AND bookName:lucene");

        //执行搜索
        search(q);
    }

}
