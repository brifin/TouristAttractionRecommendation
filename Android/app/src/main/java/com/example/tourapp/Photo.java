package com.example.tourapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Photo {
    private int[] group1;
    private int[] group2;
    private int[] group3;
    private int[] group4;
    private List<int[]> listGroup = new ArrayList<int[]>();

    private String text1 = "在这个城市，高耸的摩天大楼与古老的建筑交相辉映，像是一幅现代与传统的画卷。熙攘的人群穿梭于城市的大街小巷，其中不乏一些拥有独特气息的小店和咖啡馆，是城市生活中的一道亮丽风景线。"
            + "\n" + "在城市的边缘，河流缓缓流淌，周围绿树环抱，微风拂面，让人心旷神怡。当夕阳西下，夜幕降临，城市灯光熠熠生辉，如同一个巨大的宝石盒，美丽而迷人."
            + "\n" + "而在这座城市的远处，巍峨的山峦如同一条连绵不断的长龙，它们高耸入云，云雾缭绕，仿佛是世界的尽头。站在山顶上，可以俯瞰整个城市，感受到这座城市的魅力与活力。"
            + "\n" + "这里是一个独特而充满生机的城市，它凝聚了各种文化和人才，呈现出丰富多彩的景象，让人沉醉于这里的美丽和魅力。";
    private String text2 = "在这个神奇的地方，城镇和城镇建筑随处可见，仿佛一个个精致的玩具城堡竖立在大地上。城镇的街道宽敞而整洁，让人感到愉悦和舒适。建筑物各具特色，有的是历史悠久的老房子，有的则是现代化的高楼大厦，但它们都有着自己独特的魅力。" +
            "\n" + "这里的城镇，充满了生机和活力，街道上人来人往，车辆穿梭，店铺里充满了各种各样的物品，有着独特的地方特色。每个城镇都有自己的历史和文化，这些历史和文化在城镇的建筑和人们的生活中得到了充分的展现。" + "\n"
            + "在这里，你可以看到古老的教堂、庄严的政府建筑、迷人的小酒馆和咖啡店、以及充满活力的商业中心。建筑外观的设计独具匠心，各种色彩和形式交织在一起，形成了一道道美丽的风景线。" + "\n"
            + "在这里的城镇，你可以看到人们的生活是如此的美好和充实。他们过着轻松自在的生活，享受着各种文化和娱乐活动。城镇里的人们，总是那么热情和友好，他们在这个充满活力和美好的地方，过着充实而美好的生活。";
    private String text3 = "在大地的边缘，连绵着一座座苍茫的山峦，那里的山川，有着无与伦比的壮美和神秘。山脉的轮廓在天空中显得更加清晰，仿佛被刻印在了蔚蓝的天幕上。山脊上，林木葱郁，千姿百态的植被在阳光的照耀下呈现出各种瑰丽的色彩。冬季，厚厚的白雪覆盖在山巅，犹如一层皑皑白绸，随着山风的吹拂而飘摇。" +
            "\n" + "沿着山间的曲径小道，可以看到一些被称作“小镇”的居民点。这些小镇，建筑风格各异，有些是欧式风格，有些则采用了现代化的建筑方式。每个小镇都有自己独特的气质和文化底蕴，这些历史和人文的积淀，让这些小镇变得更加迷人。在小镇的中心广场，可以看到古老的钟楼和雕塑，也可以看到各种各样的商店和餐厅，这些都是小镇独有的风景线。" +
            "\n" + "当你到达山脉的山脚，会看到许多建筑物，这些高楼大厦在阳光下闪闪发光，散发着现代都市的繁华气息。这些高楼大厦，体现着人类的智慧和创造力，是现代文明的象征。虽然在这些高楼大厦的阴影下，山脉的壮美似乎有些黯淡，但山脉仍然在那里，它们如同世界的脊梁，承载着万物的荣光和尊严.";
    private String text4 = "一座城，矗立在大地上，云雾缭绕，摩天大楼耸立，车水马龙。人们来来往往，忙碌着，仿佛永无休止。但城中也有宁静的角落，公园中的小湖，阳光下草地上的行人，以及小巷中藏起来的咖啡店。"
            + "夜幕降临，灯火璀璨，城市也开始了另一种生活。各种文化在这里碰撞，音乐、艺术、美食等等，在这里互相融合，创造出一种独特的氛围。这里的人们充满了热情，他们都在追逐自己的梦想。"
            + "在这座城市中，有一条具有代表性的街道，商店林立，夜晚更是繁华不断。这里的建筑是如此多样化，古老和现代的风格在这里交织，形成了一种独特的韵味。这座城市虽然喧闹，但也不乏宁静之地，如有一所大学校园，绿树环绕，清晨时分，鸟鸣声和呼吸声交织在一起，给人带来一种宁静和祥和之感。这座城市是个独特的存在，充满了生机和活力，也充满了历史和文化的底蕴。";
    private List<String> texts = new ArrayList<String>();

    public Photo() {
        group1 = new int[]{R.drawable.scenery1group1, R.drawable.scenery2group1, R.drawable.scenery3group1, R.drawable.scenery4group1, R.drawable.scenery5group1};
        group2 = new int[]{R.drawable.scenery1group2, R.drawable.scenery2group2, R.drawable.scenery3group2, R.drawable.scenery4group2, R.drawable.scenery5group2};
        group3 = new int[]{R.drawable.scenery1group3, R.drawable.scenery2group3, R.drawable.scenery3group3, R.drawable.scenery4group3, R.drawable.scenery5group3};
        group4 = new int[]{R.drawable.scenery1group4, R.drawable.scenery2group4, R.drawable.scenery3group4, R.drawable.scenery4group4, R.drawable.scenery5group4};
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

}
