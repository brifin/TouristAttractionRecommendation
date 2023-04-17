package com.fuchuang.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fuchuang.common.Sender;
import com.fuchuang.common.Result;
import com.fuchuang.common.Sender;
import com.fuchuang.entity.User;
import com.fuchuang.mapper.UserMapper;
import com.fuchuang.service.UserService;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    //返回景点个数
    private int maxAttractions=20;
    //景点距离
    private double distanceLimited=100;
    //返回实例个数
    private int numOfUsage=8;
    int sign=1;
    //登录
    @PostMapping("/login")
    public Result login(@RequestBody User user, HttpServletResponse response){
        System.out.println("login");
        List list = userService.lambdaQuery()
                .eq(User::getAccount,user.getAccount())
                .eq(User::getPassword,user.getPassword()).list();
        if(list.size()<=0){
            return Result.fail();
        }
        Cookie cookie=new Cookie("userId",list.get(0).toString().split("id=")[1].split(",")[0]);
        response.addCookie(cookie);
        cookie.setMaxAge(7*24*3600);
        return list.size()>0?Result.suc(list.get(0)):Result.fail();
    }
    //新增保存
    @PostMapping("/save")
    public Result save(@RequestBody User user){
        List list=userService.lambdaQuery()
                .eq(User::getAccount,user.getAccount()).list();
        if(list.size()!=0){
            System.out.println("重复账号注册！");
            return Result.fail();
        }
        return userService.save(user)?Result.suc():Result.fail();
    }
    //更新(by id
    @PostMapping("/update")
    public Result update(@CookieValue("userId") String id,@RequestBody User user){
        if(userService.getById(Integer.parseInt(id)).getAccount().equals(user.getAccount())){
            user.setId(Integer.parseInt(id));
            return userService.updateById(user)?Result.suc():Result.fail();
        }else{
            System.out.println("无权修改该用户密码！");
            return Result.fail();
        }
    }
    //更新头像
    @PostMapping("/updatePng")
    public Result updateImg(@CookieValue("userId") String id,@RequestParam(value = "applyFiles") MultipartFile applyFile) throws IOException {
        User user=userService.getById(id);
        System.out.println(user.getAccount());
        String originalFileName = applyFile.getOriginalFilename();
        String type = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        String fileName =user.getAccount() + "." +type;
        String path="/root/tvanil/src/main/resources/UserImg/";
        File file=new File("/root/tvanil/src/main/resources/UserImg/");
        if(!file .isDirectory()){
            file.mkdir();
        }
        File targetFile = new File("/root/tvanil/src/main/resources/UserImg/",fileName);
        if (targetFile.exists()){
            targetFile.delete();
        }
        applyFile.transferTo(targetFile);
        return targetFile.exists()?Result.suc():Result.fail();
    }
    //上传头像
    @GetMapping("/getPng")
    public Result getImg(@CookieValue("userId") String id) throws IOException {
        User user=userService.getById(id);
        File targetFile = new File("/root/tvanil/src/main/resources/UserImg/",user.getAccount() + ".png");
        if(targetFile.exists()) {
            InputStream in = new FileInputStream(targetFile);
            byte[] data = new byte[in.available()];
            in.read(data);
            in.close();
            BASE64Encoder encoder = new BASE64Encoder();
            String base = "data:image/png;base64," + encoder.encode(data);
            base = base.replaceAll("\r|\n", "");
            return Result.suc(base);
        }else{
            targetFile = new File("/root/tvanil/src/main/resources/UserImg/",user.getAccount() + ".jpg");
            if(targetFile.exists()) {
                InputStream in = new FileInputStream(targetFile);
                byte[] data = new byte[in.available()];
                in.read(data);
                in.close();
                BASE64Encoder encoder = new BASE64Encoder();
                String base = "data:image/png;base64," + encoder.encode(data);
                base = base.replaceAll("\r|\n", "");
                return Result.suc(base);
            }else{
                targetFile = new File("/root/tvanil/src/main/resources/UserImg/",user.getAccount() + ".jpeg");
                if(targetFile.exists()) {
                    InputStream in = new FileInputStream(targetFile);
                    byte[] data = new byte[in.available()];
                    in.read(data);
                    in.close();
                    BASE64Encoder encoder = new BASE64Encoder();
                    String base = "data:image/png;base64," + encoder.encode(data);
                    base = base.replaceAll("\r|\n", "");
                    return Result.suc(base);
                }else{
                    System.out.println("不支持的图片格式!");
                    return Result.fail();
                }
            }
        }
    }
    //计算用户附近景点
    @PostMapping("/nearbyAttractions")
    public Result nearbyAttractions(@CookieValue("userId") String id,@RequestParam(value = "latitude")String lat, @RequestParam(value = "longitude")String lon) throws IOException {
        User user=userService.getById(id);
        double latA=Double.parseDouble(lat);
        double lonA=Double.parseDouble(lon);
        BufferedReader in=new BufferedReader(new FileReader("/root/tvanil/src/main/resources/datas/AllPois.txt"));
//        BufferedReader in=new BufferedReader(new FileReader("D:\\java\\demo1\\src\\main\\resources\\datas\\AllPois.txt"));
        Sender.send(sign++,user.getLatitude(),user.getLongitude(),lat,lon);
        user.setLatitude(latA);
        user.setLongitude(lonA);
        userService.updateById(user);
        String str=in.readLine();
        String[] strSplit;
        String[] nearbyAttraction=new String[maxAttractions];
        int i=0;
        while(true){
            str=in.readLine();
            strSplit=str.split("\t");
            double latB=Double.parseDouble(strSplit[0]);
            double lonB=Double.parseDouble(strSplit[1]);
            int flag=Integer.parseInt(strSplit[2]);
            double C = Math.sin(latA)*Math.sin(latB) + Math.cos(latA)*Math.cos(latB)*Math.cos(lonA-lonB);
            double distance =6371.004*Math.acos(C)*3.1415926/180;
            if(distance<distanceLimited){
                nearbyAttraction[i++]=strSplit[0]+"   "+strSplit[1]+" "+strSplit[2];
            }
            if(i>=19||flag==3359){
                break;
            }
        }
        return Result.suc(nearbyAttraction);
    }
    //随机返回随机数量positive和negative的Usage
    @GetMapping("/tourGroup")
    public Result tourGroup() throws IOException {
        BufferedReader positiveIn=new BufferedReader(new FileReader("/root/tvanil/src/main/resources/datas/positive_example_usage"));
        BufferedReader negativeIn=new BufferedReader(new FileReader("/root/tvanil/src/main/resources/datas/negetive_example_usage"));
        String str;

        int posUsage=4;
        int negUsage=numOfUsage-posUsage;

        List<String> listOfPositive=new ArrayList<String>();
        List<String> listOfNegative=new ArrayList<String>();
        while((str=positiveIn.readLine())!=null){
            listOfPositive.add(str);
        }
        while((str=negativeIn.readLine())!=null){
            listOfNegative.add(str);
        }
        int n= listOfPositive.size();
        int[] numbers=new int[n];
        for(int i=0;i<numbers.length;i++){
            numbers[i]=i;
        }
        int[] res=new int[posUsage];
        for(int i=0;i<res.length;i++){
            int r=i;
            res[i]=numbers[r];
            numbers[r]=numbers[n-1];
            n--;
        }
        String[] result=new String[numOfUsage];
        int q=0;
        for(int i:res){
            result[q]=listOfPositive.get(i);
            result[q] = result[q].replaceAll("\t", "    ");
            q++;
        }
        n= listOfNegative.size();
        int[] numbers2=new int[n];
        for(int i=0;i<numbers2.length;i++){
            numbers2[i]=i;
        }
        int[] res2=new int[negUsage];
        for(int i=0;i<res2.length;i++){
            int r=i;
            res2[i]=numbers2[r];
            numbers2[r]=numbers2[n-1];
            n--;
        }
        for(int i:res2){
            result[q]=listOfNegative.get(i);
            result[q] = result[q].replaceAll("\t", "    ");
            q++;
        }
        return Result.suc(result);
    }


//    //点赞功能
//    @PostMapping("/clickStars")
//    public Result clickStars(@CookieValue("userId") String id,@RequestParam(value = "poi")String poi){
//        User user=userService.getById(id);
//        if(user.getStars()==null){
//            user.setStars(poi);
//        }else{
//            String str= user.getStars();
//            user.setStars(str+","+ poi);
//        }
//        return userService.updateById(user)?Result.suc():Result.fail();
//    }
//    //点赞数据返回
//    @GetMapping("getStars")
//    public Result getStars(@CookieValue("userId") String id){
//        String stars=userService.getById(id).getStars();
//        String[] result=stars.split(",");
//        return Result.suc(result);
//    }
//    @PostMapping("/deleteStars")
//    public Result deleteStars(@CookieValue("userId") String id,@RequestParam(value = "poi")String poi){
//        User user=userService.getById(id);
//        String[] str=user.getStars().split(",");
//        user.setStars(null);
//        for(String s:str){
//            if(!s.equals(poi)){
//                if(user.getStars()==null){
//                    user.setStars(s);
//                }else{
//                    String stars= user.getStars();
//                    user.setStars(stars+","+ s);
//                }
//            }
//        }
//        return userService.updateById(user)?Result.suc():Result.fail();
//    }
//    //去过数据更新
//    @PostMapping("/gone")
//    public Result gone(@CookieValue("userId") String id,@RequestParam(value = "poi")String poi){
//        User user=userService.getById(id);
//        if(user.getGone()==null){
//            user.setGone(poi);
//        }else{
//            String str= user.getGone();
//            user.setGone(str+","+ poi);
//        }
//        return userService.updateById(user)?Result.suc():Result.fail();
//    }
//    //获取去过
//    @GetMapping("getGone")
//    public Result getGone(@CookieValue("userId") String id){
//        String gone=userService.getById(id).getGone();
//        String[] result=gone.split(",");
//        return Result.suc(result);
//    }


}
