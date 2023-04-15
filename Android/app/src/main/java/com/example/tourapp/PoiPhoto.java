package com.example.tourapp;

import com.Detail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PoiPhoto {
    public Map detail = new HashMap<Long, Detail>();
    public PoiPhoto(){
        detail.put(23, new Detail("RODEWAY INN",
                "Forget about fancy when all you need is a solid deal on a simple stay in Rodeway Inn hotels are just what you are looking for. When you are traveling on a budget, Rodeway Inn hotels offer the travel basics?with the reliability and security of a national brand you can trust. So you can get the rest you need for wherever travel takes you.",
                new int[]{R.drawable.poi11,R.drawable.poi12,R.drawable.poi13}));
        detail.put(24, new Detail("Iron Works Barbecue",
                "Our famous products are available online for you! It is easy and secure to buy the sauces, spices and meat that make us famous world wide.\n" +
                        "The Iron Works Barbecue has been serving \"Real Texas Barbecue\" Since 1978. Situated in the historic F. Weigl Iron Works building along Waller Creek in downtown Austin, this spot is the perfect lunchtime getaway for barbecue lovers.",
                new int[]{R.drawable.poi21,R.drawable.poi22,R.drawable.poi23}));
        detail.put(18, new Detail("Stanford Dish Trail",
                "The dish satellite is the halfway point in either direction during your run, jog or walk. There are no bicycles, skateboards, scooters or dogs allowed (except trained service animals), strollers of any kind are acceptable. This is a great place to walk/run alone bring your children, or push a stroller up the impressive hills for a difficult work out!",
                new int[]{R.drawable.poi31,R.drawable.poi32,R.drawable.poi33}));
        detail.put(79, new Detail("IRON CACTUS - MEXICAN RESTAURANTS",
                "Iron Cactus Mexican Grill and Margarita Bar is a premier local hot spot for Mexican food and tequila cocktails for visitors and locals in Austin and San Antonio, TX. Our Mexican restaurants serve some of the best contemporary Mexican cuisine?menus?you will ever savor as well as our famous table-side guacamole, fixed fresh to your taste right at your table.",
                new int[]{R.drawable.poi41,R.drawable.poi42,R.drawable.poi43}));
        detail.put(139, new Detail("Summit rooftop lounge",
                "Overlooking the historical warehouse district is one of Austins largest and most prominent rooftop venues. Summit boasts a unique interior design and immense rooftop deck equipped with a state-of-the-art sound system and stunning LED wall. Summits lively atmosphere is complimented by extraordinary views of Austins skyline, creating the perfect backdrop for any formal or casual event. Connect with us today via email, social media or directly by phone; we look forward to seeing you. ",
                new int[]{R.drawable.poi51,R.drawable.poi52,R.drawable.poi53}));
        detail.put(85, new Detail("RODEWAY INN",
                "Forget about fancy when all you need is a solid deal on a simple stay in Rodeway Inn hotels are just what you are looking for. When you are traveling on a budget, Rodeway Inn hotels offer the travel basics?with the reliability and security of a national brand you can trust. So you can get the rest you need for wherever travel takes you.",
                new int[]{R.drawable.poi61,R.drawable.poi62,R.drawable.poi63}));
        detail.put(25, new Detail("Veracruz All Natural",
                "Owners Reyna and Maritza Vazquez are sisters and best friends. They were born and raised in Mexico and now live in Austin, TX. It took a lot of hard work, sacrifice, and perseverance to get to where they are today as business owners.\n" +
                        "From an early age, Reyna and Maritza wanted to own their own restaurant. They grew up helping their mother in the family kitchen in Veracruz, Mexico. Starting at very young ages, Reyna and Maritza’s mother and grandmother taught them the ins and outs of the kitchen, and they have loved to cook ever since. They learned to be creative with their cooking while at the same time respecting the heritage of authentic ingredients and the culture they represent. They also learned the value of using both fresh and organic ingredients in their cooking.",
                new int[]{R.drawable.poi71,R.drawable.poi72,R.drawable.poi73}));
        detail.put(47, new Detail("Ball Peoples",
                "Provider of insurance and employee benefits advisory services based in Austin, Texas.\n" +
                        "Contact Information\n" +
                        "Website\n" +
                        "www.ballpeoples.com\n" +
                        "Ownership Status\n" +
                        "Acquired/Merged\n" +
                        "Financing Status\n" +
                        "Private Equity-Backed\n" +
                        "Primary Industry\n" +
                        "Insurance Brokers\n" +
                        "Primary Office\n" +
                        "\uF0B7600 West 5th Street\n" +
                        "\uF0B7Suite 200\n" +
                        "\uF0B7Austin, TX 78701\n" +
                        "\uF0B7United States",
                new int[]{R.drawable.poi81,R.drawable.poi82,R.drawable.poi83}));
        detail.put(63, new Detail("locale rainey street",
                "Rainey Street features a rooftop terrace and is a 5-minute walk from Austin Convention Center. Tired of playing in the outdoor pool, guests can head to the cafe to find something to eat. This upscale hotel is also just a 5-minute drive from the University of Texas at Austin and Sixth Street. The property's staff and central location have been well received by travelers. Close to public transportation: city center station is just a 9-minute walk.",
                new int[]{R.drawable.poi91,R.drawable.poi92,R.drawable.poi93}));
        detail.put(100, new Detail(" chilis bar",
                "Join us in our bar for an array of wing options. Bone-in or boneless, we have something for every opposing team. The real MVP is the Chili's bar, where you can enjoy?nine?different sauces to dress up your wings, such as Garlic Parmesan and Mango Habanero.\n" +
                        "Bar not your thing? Order online and enjoy Buffalo, Honey-Chipotle or House BBQ flavors!\n" +
                        "When it comes to our menu full of amazing shrimp and fish options, every choice is the right one. From signature sizzlin’ Shrimp Fajitas to our Cajun Shrimp Pasta — a fan-favorite classic with the perfect amount of kick — we’ve got something for every flavor of seafood lover!",
                new int[]{R.drawable.poi101,R.drawable.poi102,R.drawable.poi103}));
    }

}
