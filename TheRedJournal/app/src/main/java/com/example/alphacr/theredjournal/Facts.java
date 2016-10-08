package com.example.alphacr.theredjournal;

/**
 * Created by AlphaCR on 08/10/2016.
 */

public class Facts {
    private final String facts[] = {
            "Blood drawn from live animals has been a staple food of nomadic tribes for thousands of years",
            "Rooster blood was the original thickener for the traditional French dish Coq Au Vin",
            "It would take about 1,200,000 mosquito bites to completely drain the average human of blood",
            "Chocolate syrup was used for the blood in the famous shower scene in the 1960 Alfred Hitchcock movie 'Psycho'",
            "The juice of purple grapes seems to have similar effects in inhibiting blood clots and therefore heart attacks",
            "Everyone has a blood type. O is the most common of the four blood types.  AB is the rarest",
            "There are subgroups of the 4 major blood types that are rarer still.  There is a blood type A-H.  Only three people are known to have this type",
            "Cats also have 4 different blood groups.  Cows have over 800",
            "Mr. Spock on Star Trek was said to have T-negative blood",
            "Most people who are at least 17 years old, weigh at least 110 pounds and are in good health can donate blood",
            "The average adult has 10 - 12 pints of blood in their body",
            "What percent of a person's body weight is blood? 7",
            "Red blood cells develop in bone marrow and circulate in the body for around 120 days",
            "As well as delivering important substances to our cells, blood also helps take away unwanted waste products."};
    private int index;

    public Facts() {
        index = (int) (Math.random()*facts.length);
    }

    public String nextFact()
    {
        index = (int) (Math.random()*facts.length);
        return facts[index];
    }


}
