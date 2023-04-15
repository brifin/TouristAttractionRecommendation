package com.example.tourapp.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.HeterogeneousExpandableList;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tourapp.R;
import com.example.tourapp.adapter.TourDetailAdapter;
import com.example.tourapp.fragment.TourFragment;
import com.example.tourapp.viewAndItem.RecordItem;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class TourDetailActivity extends AppCompatActivity {

    private List<RecordItem> recordItemList = new ArrayList<>();
    private TourDetailAdapter adapter;
    private ImageView iv_back;
    //private ImageView iv_ScatteredGroups;
    private TextView tv_introduce;
    private String[] introduce = {
            "遇见最美希腊——路线名称：希腊圣托里尼+梅黛奥拉+雅典团。\n" +
                    "上团城市： 雅典(Athens)；\n" +
                    "下团城市： 雅典(Athens)；\n" +
                    "行程概要：雅典-圣托里尼-雅典-德尔菲-卡兰巴卡-梅黛奥拉-雅典。\n" +
                    "【雅典卫城】——凝聚着希腊人民信仰与荣耀的圣域！\n" +
                    "【圣托里尼】——浪漫的发源地，蓝顶教堂，迷人日落，编织爱琴海的梦幻之旅！\n" +
                    "【梅黛奥拉】——希腊的天空之城，漂浮在空中的修道院，非身临其境，无以知其时之绝望，无以知其信仰之坚韧！\n" +
                    "【德尔菲】——地球的中心，世界的肚脐，阿波罗神在此昭晓神谕！",

            "冰与火之歌，冰岛欢乐深度游——行程概要：\n" +
                    "DAY1：雷克雅未克-蓝湖温泉；\n" +
                    "DAY2：瓦特纳冰川国家公园-蓝冰洞-杰古沙龙湖；\n" +
                    "DAY3：塞尔福斯-黑沙滩-斯科加瀑布-塞里雅兰瀑布；\n" +
                    "DAY4：塞尔福斯-辛格维利尔国家公园-凯瑞斯火山口湖-盖锡尔间歇喷泉；\n" +
                    "DAY5：雷克雅未克。\n" +
                    "【蓝湖】——冰岛的象征和代表，大自然赐给冰岛神奇的礼物，是现象级的疗养胜地。\n"+
            "【瓦特纳冰川】——冰上探索好莱坞大片星际穿越，无垠的冰蓝色彩与水晶般的冰壁搭成了一座座隐秘的梦幻仙境。",

            "美国东部经典豪华游——路线名称：纽约+费城+华盛顿+尼亚加拉瀑布+波士顿。\n"+
            "【纽约】——世界的金融中心,全球时装美妆大师的聚集地，各类顶级品牌发布会的集中地；\n"+
            "【费城】——美国的古都，被誉为美国革命的摇篮，保留了很多独立时期的重要遗迹和建筑；\n"+
            "【华盛顿】——聚集了美国政府机构、各类博物馆与纪念馆等重要建筑；\n"+
            "【波士顿】——坐落着哈佛、耶鲁、普林斯顿3所常春藤大学及麻省理工学院的人才培养基地。",

            "畅游美国西海岸——路线名称：拉斯维加斯+大峡谷+羚羊彩穴+马蹄湾+胡佛水坝+巧克力工厂。\n"+
            "【拉斯维加斯】——世界上最有名的赌场及娱乐中心；\n"+
            "【羚羊彩穴】【马蹄湾】【大峡谷】——举世闻名的自然奇观；\n"+
            "【胡佛水坝】——美国综合开发科罗拉多河水资源的关键性工程；\n"+
            "【巧克力工厂】——一览世界上最精良的巧克力制作工艺。",

            "芬兰游：路线名称——图尔库城堡+赫尔辛基+波尔沃。\n"+
            "【图尔库城堡】——芬兰中世纪遗留下来的最著名古堡；\n"+
            "【赫尔辛基】——北欧式的简约主义风格配合各种活泼独到的创意之地；\n"+
            "【波尔沃】——一座风景如画的中世纪古城，重要的贸易中心。",

            "纵贯拉普兰极光破冰之旅:旅游路线——瑞典+芬兰跟团游。\n"+
            "【拉普兰】——广袤的森林、冰冻的湖泊和港湾，纯净的旷野，北极光悬挂天幕，闪着炫目而神秘的光芒，童话故事般的旅游景点，美丽而安详。",

            "德国旅游团：旅游路线——新天鹅堡+柏林+科隆大教堂。\n"+
            "【新天鹅堡】——一座迪士尼灰姑娘城堡原型的浪漫行宫，拥有富丽堂皇的建筑和独特的历史故事；\n"+
            "【柏林】——动人心魄的历史遗迹、创意无限的艺术场所、音乐和文化的殿堂、独特的购物体验和休闲娱乐之地；\n"+
            "【科隆大教堂】——欧洲北部最大的教堂之一，德国科隆的天主教主教座堂，哥特式建筑风格。",

            "美国南部:行程概况——迈阿密-罗德岱堡。\n"+
                    "【迈阿密】——美国佛罗里达州第二大城市，有着迷人的海滩和热带气候、文化多样性和充满活力的夜生活；\n"+
                    "【罗德岱堡】——美国佛罗里达州一处拥有37公里沙滩的海滨小镇，八个不同的海滨小镇各具特色，罗德岱堡和好莱坞更是举办时尚聚会和节日的热门场所。"
    };

    double[] lat = {
            38.52,
            64.09,
            39.56,
            37.10,
            60.27,
            69.02,
            52.30,
            25.47
    };//纬度

    double[] lon = {
            24.52,
            -21.56,
            -75.10,
            -115.08,
            23.14,
            24.08,
            13.25,
            -80.03
    };//精度

    private int[] imageId = {
            R.drawable.view1,
            R.drawable.view2,
            R.drawable.view5,
            R.drawable.view7,
            R.drawable.view6,
            R.drawable.view3,
            R.drawable.view4,
            R.drawable.view8
    };
    private String schedule;
    //private boolean isScatteredGroups;
    private ImageView iv_introduce;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_detail);
        iv_back = findViewById(R.id.iv_back);
        tv_introduce = findViewById(R.id.tv_introduce);
        iv_introduce = findViewById(R.id.iv_introduce);
        //iv_ScatteredGroups = findViewById(R.id.iv_ScatteredGroups);
        schedule = getIntent().getStringExtra("schedule");
        //isScatteredGroups = getIntent().getBooleanExtra("isScatteredGroups", false);
        id = getIntent().getIntExtra("id", 0);
        initTourDetailItems();
        ListView listView = findViewById(R.id.list_item_detail);
        adapter = new TourDetailAdapter(this, R.layout.record_item, recordItemList);
        listView.setAdapter(adapter);
        iv_back.setOnClickListener(v -> {
            finish();
        });
        if (id < 8) {
            tv_introduce.setText(introduce[id]);
            iv_introduce.setImageResource(imageId[id]);
        }

        hideStable();
    }

    private void initTourDetailItems() {
        //System.out.println(schedule);
        String[] str = schedule.split("\\s+");
        for (int i = 1; i < str.length; i++) {

            String[] simple = str[i].split(",");
            double latStart = lat[id];
            double lonStart = lon[id];
            Date nowDate = new Date();
            long startTime = nowDate.getTime() - 2000 * 60 * 1000;
            RecordItem recordItem;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String timeStr = simple[2];
            double timeOffset = Double.parseDouble(timeStr) * 60 * 1000;
            long time = (long) (startTime + timeOffset);
            Date date = new Date(time);
            String formatTime = format.format(date);
            double lat = latStart + Double.parseDouble(simple[0]);
            double lon = lonStart + Double.parseDouble(simple[1]);
            String latStr;
            String lonStr;
            if (lat >= 0) {
                latStr = String.format("%.2f° N", lat);
            } else {
                latStr = String.format("%.2f° S", -lat);
            }

            if (lon >= 0) {
                lonStr = String.format("%.2f° E", lon);
            } else {
                lonStr = String.format("%.2f° W", -lon);
            }


            String place = "(" + latStr + "," + lonStr + ")";
            recordItem = new RecordItem(i, place, formatTime, Integer.parseInt(simple[3]));
            recordItemList.add(recordItem);

        }
    }


    //隐藏状态栏
    public void hideStable() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        ImmersionBar.with(this)
                .transparentBar()
                .statusBarDarkFont(true)
                .statusBarAlpha(0.0f)
                .hideBar(BarHide.FLAG_HIDE_BAR)
                .init();
    }

    /*@Override
    public void sendSchedule(String str) {
        schedule = str;
    }*/

    /*@Override
    public void sendIsScatteredGroups(Boolean iScatteredGroups) {
        isScatteredGroups = iScatteredGroups;
    }*/
}