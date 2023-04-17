package com.fuchuang.controller;
import com.fuchuang.common.*;
import com.fuchuang.entity.Attractions;
import com.fuchuang.service.AttractionsService;
import com.fuchuang.service.MessageService;
import com.google.common.collect.Lists;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
@CrossOrigin
@RestController
@RequestMapping("/attractions")
public class AttractionsController {
    int sign=3;
    @Autowired
    private AttractionsService attractionsService;
    @Autowired
    private MessageService messageService;

    @PostMapping("/generate")
    public Result generate() throws IOException {
        BufferedReader in=new BufferedReader(new FileReader("/root/tvanil/src/main/resources/datas/AllPois.txt"));
        String str=in.readLine();
        String[] strSplit;
        while(true) {
            str = in.readLine();
            strSplit = str.split("\t");
            Attractions attractions=new Attractions();
            attractions.setLatitude(Double.parseDouble(strSplit[0]));
            attractions.setLongitude(Double.parseDouble(strSplit[1]));
            attractions.setPoi(Integer.parseInt(strSplit[2]));
            attractionsService.save(attractions);
            if(Integer.parseInt(strSplit[2])==3359){
                break;
            }
        }
        return Result.suc();
    }

//    @PostMapping("/clickStars")
//    public Result clickStars(@RequestParam(value = "poi")String poi){
//        Attractions attractions=attractionsService.getById(Integer.parseInt(poi));
//        attractions.setStars(attractions.getStars()+1);
//        return attractionsService.updateById(attractions)?Result.suc():Result.fail();
//    }

    @PostMapping("/getDetails")
    public Result getStars(@RequestParam(value = "poi")String poi){
        return Result.suc(attractionsService.getById(Integer.parseInt(poi)));
    }

//    @PostMapping("/routeRecommend")
//    public Result routeRecommend(@RequestBody Routes routes){
//        int numOfRoute1=getRe(routes.getRoute1());
//        int numOfRoute2=getRe(routes.getRoute2());
//        int numOfRoute3=getRe(routes.getRoute3());
//        if(numOfRoute1>=numOfRoute2&&numOfRoute1>=numOfRoute3){
//            return Result.suc(routes.getRoute1());
//        } else if (numOfRoute2 >= numOfRoute3) {
//            return Result.suc(routes.getRoute2());
//        }else {
//            return Result.suc(routes.getRoute3());
//        }
//    }

    @PostMapping("/routeRecommend")
    public Result routeRecommend(@RequestBody Routes route){
        List<List<Location>> routes=new ArrayList<List<Location>>();
        int num = route.getRoute().size();
        for(int i=0;i<num;i++){
            routes.add(getRe(route.getRoute().get(i)));
        }
        return Result.suc(route);
    }


    @GetMapping("/updateNumberOfPerson")
    public Result updateNumberOfPerson(){
        return Result.suc(getMessageById(1));
    }

    public @PostConstruct void receive() throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        Properties props = new Properties();
        props.put("bootstrap.servers", "47.107.38.208:9092");
        props.put("group.id", "test");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("auto.offset.reset", "latest");
        FlinkKafkaConsumer<String> consumer=new FlinkKafkaConsumer<String>("locations" ,new SimpleStringSchema(),props);
        consumer.setStartFromEarliest();
        SingleOutputStreamOperator<Message> messageoso=env.addSource(consumer).setParallelism(1).map(string -> GsonUtil.fromJson(string,Message.class));
        messageoso.countWindowAll(1).apply(new AllWindowFunction<Message,List<Message>, GlobalWindow>() {
            @Override
            public void apply(GlobalWindow window, Iterable<Message> values, Collector<List<Message>> out) throws Exception {
                ArrayList<Message> messages=Lists.newArrayList(values);
                out.collect(messages);
            }
        }).addSink(new SinkToMysql());

        new Thread(() -> {
            try {
                env.execute();
            } catch (Exception e) {
            }
        }).start();

        new Thread(()->{
            int i=1;
            while(true){
                for(;!getMessageById(i).equals("error");i++){
                    try {
                        dec(getMessageById(i));
                        System.out.println("定位更新完成");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
                ).start();
    }

    public void dec(String value) throws IOException {
//        BufferedReader in=new BufferedReader(new FileReader("D:\\java\\demo1\\src\\main\\resources\\datas\\AllPois.txt"));
        BufferedReader in=new BufferedReader(new FileReader("/root/tvanil/src/main/resources/datas/AllPois.txt"));
        String str=in.readLine();
                String[] strSplit;
                String[] user=value.split(",");
                double userPreLat=Double.parseDouble(user[0]);
                double userPreLon=Double.parseDouble(user[1]);
                double userNowLat=Double.parseDouble(user[2]);
                double userNowLon=Double.parseDouble(user[3]);
                while(true){
                    str=in.readLine();
                    strSplit=str.split("\t");
                    double latB=Double.parseDouble(strSplit[0]);
                    double lonB=Double.parseDouble((strSplit[1]));
                    int poi=Integer.parseInt(strSplit[2]);
                    double C=Math.sin(userPreLat)*Math.sin(latB)+Math.cos(userPreLat)*Math.cos(latB)*Math.cos(userPreLon-lonB);
                    double preDistance=6371.004*Math.acos(C)*3.1415926/180;
                    C=Math.sin(userNowLat)*Math.sin(latB)+Math.cos(userNowLat)*Math.cos(latB)*Math.cos(userNowLon-lonB);
                    double nowDistance=6371.004*Math.acos(C)*3.1415926/180;
                    if(nowDistance<1){
                        Attractions attractions=attractionsService.getById(poi);
                        attractions.setStars(attractions.getStars()+1);
                        attractionsService.updateById(attractions);
                    }
                    if(preDistance<1){

                        Attractions attractions=attractionsService.getById(poi);
                        if(attractions.getStars()>0) {
                            attractions.setStars(attractions.getStars() - 1);
                        }else{
                            System.out.println("人数为0，请正确编写测试数据");
                        }
                        attractionsService.updateById(attractions);
                    }
                    if(poi==3359){
                        break;
                    }
                }
    }

//    public int getRe(Route route){
//        System.out.println(route.getAttraction1().getLatitude());
//        System.out.println(route.getAttraction1().getLongitude());
//        List list1=attractionsService.lambdaQuery()
//                .eq(Attractions::getLatitude,route.getAttraction1().getLatitude())
//                .eq(Attractions::getLongitude,route.getAttraction1().getLongitude())
//                .list();
//        int poi1= Integer.parseInt(list1.get(0).toString().split("id=")[1].split(",")[0]);
//        List list2=attractionsService.lambdaQuery()
//                .eq(Attractions::getLatitude,route.getAttraction2().getLatitude())
//                .eq(Attractions::getLongitude,route.getAttraction2().getLongitude())
//                .list();
//        int poi2= Integer.parseInt(list2.get(0).toString().split("id=")[1].split(",")[0]);
//        List list3=attractionsService.lambdaQuery()
//                .eq(Attractions::getLatitude,route.getAttraction3().getLatitude())
//                .eq(Attractions::getLongitude,route.getAttraction3().getLongitude())
//                .list();
//        int poi3= Integer.parseInt(list3.get(0).toString().split("id=")[1].split(",")[0]);
//        List list4=attractionsService.lambdaQuery()
//                .eq(Attractions::getLatitude,route.getAttraction4().getLatitude())
//                .eq(Attractions::getLongitude,route.getAttraction4().getLongitude())
//                .list();
//        int poi4= Integer.parseInt(list4.get(0).toString().split("id=")[1].split(",")[0]);
//        return attractionsService.getById(poi1).getStars()
//                +attractionsService.getById(poi2).getStars()
//                +attractionsService.getById(poi3).getStars()
//                +attractionsService.getById(poi4).getStars();
//    }

    public List<Location> getRe(List<Location> route){
        int num=route.size();
        int number=0;
        for(int i=0;i<num;i++){
            List list1=attractionsService.lambdaQuery()
                    .eq(Attractions::getPoi,route.get(i).getPoi())
                    .list();
            if(list1.size()==0){
                System.out.println("提供了错误的Poi号");
            }else{
                route.get(i).setStars(Integer.parseInt(list1.get(0).toString().split("stars=")[1].split("\\)")[0]));
            }
        }
        return route;
//        List list1=attractionsService.lambdaQuery()
//                .eq(Attractions::getLatitude,route.getAttraction1().getLatitude())
//                .eq(Attractions::getLongitude,route.getAttraction1().getLongitude())
//                .list();
//        int poi1= Integer.parseInt(list1.get(0).toString().split("id=")[1].split(",")[0]);
//        List list2=attractionsService.lambdaQuery()
//                .eq(Attractions::getLatitude,route.getAttraction2().getLatitude())
//                .eq(Attractions::getLongitude,route.getAttraction2().getLongitude())
//                .list();
//        int poi2= Integer.parseInt(list2.get(0).toString().split("id=")[1].split(",")[0]);
//        List list3=attractionsService.lambdaQuery()
//                .eq(Attractions::getLatitude,route.getAttraction3().getLatitude())
//                .eq(Attractions::getLongitude,route.getAttraction3().getLongitude())
//                .list();
//        int poi3= Integer.parseInt(list3.get(0).toString().split("id=")[1].split(",")[0]);
//        List list4=attractionsService.lambdaQuery()
//                .eq(Attractions::getLatitude,route.getAttraction4().getLatitude())
//                .eq(Attractions::getLongitude,route.getAttraction4().getLongitude())
//                .list();
//        int poi4= Integer.parseInt(list4.get(0).toString().split("id=")[1].split(",")[0]);
//        return attractionsService.getById(poi1).getStars()
//                +attractionsService.getById(poi2).getStars()
//                +attractionsService.getById(poi3).getStars()
//                +attractionsService.getById(poi4).getStars();
    }

    public String getMessageById(int id){
        List list=messageService.lambdaQuery()
                .eq(com.fuchuang.entity.Message::getId,id).list();
        if(list.size()>0) {
            int i = Integer.parseInt(list.get(0).toString().split("id=")[1].split(",")[0]);
            com.fuchuang.entity.Message message = messageService.getById(i);
            return message.getMessage();
        }else{
            return "error";
        }
    }

}
