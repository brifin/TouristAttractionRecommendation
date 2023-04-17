package com.fuchuang.common;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class SinkToMysql extends RichSinkFunction<List<Message>> {
    PreparedStatement ps;
    BasicDataSource dataSource;
    private Connection connection;

    @Override
    public  void open(Configuration parameters) throws Exception{
        super.open(parameters);
        dataSource=new BasicDataSource();
        connection=getConnection(dataSource);
        String sql="Insert into message(id,message) values(?,?);";
        ps=this.connection.prepareStatement(sql);
    }

    @Override
    public void close() throws Exception{
        super.close();
        if(connection!=null){
            connection.close();
        }
        if(ps!=null){
            ps.close();
        }
    }

    @Override
    public void invoke(List<Message> value,Context conText)throws Exception{
        for(Message message:value){
            ps.setInt(1,message.getId());
            ps.setString(2,message.getMessage());
            ps.addBatch();
        }
        int[] count=ps.executeBatch();
        System.out.println("插入"+count.length+"条数据");
    }

    private static Connection getConnection(BasicDataSource dataSource){
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/wms?characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8&rewriteBatchedStatements=true&allowPublicKeyRetrieval=true");
        dataSource.setUsername("root");
        dataSource.setPassword("abc123");
        dataSource.setInitialSize(10);
        dataSource.setMaxTotal(50);
        dataSource.setMinIdle(2);

        Connection con=null;
        try{
            con=dataSource.getConnection();
            System.out.println("连接到"+con);
        }catch (Exception e){
            System.out.println("fail to connect:"+e.getMessage());
        }
        return con;
    }
}
