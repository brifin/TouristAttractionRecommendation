package com.example.tourapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Photo {
    private int[] group1 ;
    private int[] group2 ;
    private int[] group3 ;
    private int[] group4 ;
    private List<int[]> listGroup = new ArrayList<int[]>();

    private String text1 = "这是一座富有活力和文化底蕴的城市。这里有着独特的城市景观，让人们惊叹不已。河流穿过城市，建筑物高耸入云，巨型的钢索桥横跨在它们之间，构成了一个壮观的景象。在城市的中心，你可以看到一些引人注目的建筑物，包括哥特式建筑、现代化摩天大楼和各种艺术博物馆。这里的美食文化也非常丰富，你可以品尝到当地著名的烤肉、披萨和海鲜等美食。总之，这是一个充满活力、文化和美食的城市，值得一游。";
    private String text2 = "在里，散布着许多美丽的城镇，这些城镇有着各自独特的魅力和建筑风格。有些城镇是欧式建筑风格，保留着传统的石砌建筑和古老的教堂，让人们仿佛回到了欧洲的中世纪。而有些城镇则是典型的美式建筑，拥有着醒目的霓虹灯和流线型的摩天大楼，展现出现代都市的繁华和活力。这些城镇中，你可以发现许多有趣的景点，如历史博物馆、文化中心、艺术馆、咖啡馆等等，这些景点让城镇充满了浓郁的文化气息和人文历史。除了建筑和景点，这些城镇还有着各种美食和当地特色的文化活动，如音乐会、嘉年华、食品节、文化节等等，吸引着许多人前来探索和享受。总之，这些城镇是一个个充满生机和活力的社区，每一个城镇都有着独特的魅力和文化，等待着人们去探索和发现。";
    private String text3 = "在那遥远的山脉之间，有着巍峨挺拔的高峰，峰顶常年覆盖着洁白的积雪。山脉的轮廓线清晰而又粗犷，沿着它们的山峦连绵起伏，峰峦之间是广袤的草原和森林。在清晨和黄昏时分，太阳的余辉将这片山脉映照得金碧辉煌，绚烂多姿。在这里，大自然展现出了它最为宏伟壮丽的景象，让人们不由得感叹生命的伟大和世界的奇妙。";
    private String text4 = "这里，充满了无数的霓虹灯和无尽的繁华。在这里，你可以看到世界上最高的建筑物，可以在咖啡店里闲逛，也可以在街角的小巷里感受到浓浓的文艺气息。这里有大量的艺术家和文化人士，他们为这座城市注入了无限的生命力，使得这里不仅仅是一座城市，更像是一个充满活力的文化中心。在曼哈顿，你可以感受到城市的脉搏，可以看到人们的热情和活力，这里永远都不会让你感到无聊和失落。";
    private List<String> texts = new ArrayList<String>();
    public Photo() {
        group1 =new int[] {R.drawable.scenery1group1, R.drawable.scenery2group1, R.drawable.scenery3group1,R.drawable.scenery4group1,};
        group2 =new int[] {R.drawable.scenery1group2, R.drawable.scenery2group2, R.drawable.scenery3group2,R.drawable.scenery4group2,};
        group3 =new int[] {R.drawable.scenery1group3, R.drawable.scenery2group3, R.drawable.scenery3group3,R.drawable.scenery4group3,};
        group4 =new int[] {R.drawable.scenery1group4, R.drawable.scenery2group4, R.drawable.scenery3group4,R.drawable.scenery4group4,};
        listGroup.add(group1);
        listGroup.add(group2);
        listGroup.add(group3);
        listGroup.add(group4);
        texts.add(text1);
        texts.add(text2);
        texts.add(text3);
        texts.add(text4);
    }

    public List<int[]> getListGroup() {
        return listGroup;
    }

    public void setListGroup(List<int[]> listGroup) {
        this.listGroup = listGroup;
    }

    public List<String> getTexts() {
        return texts;
    }

    public void setTexts(List<String> texts) {
        this.texts = texts;
    }

    //    public int[] getPhotoGroup() {
//        Random random = new Random();
//        int number = random.nextInt(100);
//        return listGroup.get(number%4);
//    }
//    public List<String> getTextDetail(){
//        return texts;
//    }
}
