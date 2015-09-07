package com.pawcare.source.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by anmumukh on 9/6/15.
 */
public class Persistence {
    public Context context;
    private Persistence(){

    }
    public Persistence(Context context){
        this.context = context;
    }
    public void persistList(List<String> list, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("KEY", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  sharedPreferences.edit();
        String x = "";
        for (String item:list){
            x = x + item+",";
        }
        x = x.substring(0,x.length() - 1);
        editor.putString(key,x);
        editor.commit();
    }
    public List retrieveList(String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("KEY", Context.MODE_PRIVATE);
        String x = sharedPreferences.getString(key, "1,2");
        Log.d("PAWEDX", "CALLED WITH KEY"+key+" and string"+x);
        String arr[] = x.split(",");
        int len = arr.length;
        Log.d("PAWEDX","Split "+x.split(".").length);
        return Arrays.asList(arr);
    }

    public void seedAnimals()
    {
        String animals[] = {"rock agama"," purple rumped sunbird"," purple moorhen"," palm swift"," red vented bulbul"," purple heron"," banded racer"," banded kukri"," buff striped keelback"," indian chameleon"," checkered keelback"," common bronzeback tree snake"," common cat snake"," spectacled cobra"," common garden lizard"," common indian monitor lizard"," common krait"," common sand boa"," common wolf snake"," dumeril's black headed snake"," elliot's sheild tail"," flap shelled turtle"," indian pond terrapin"," indian rat snake"," indian rock python"," marsh crocodile"," olive keelback"," phipson's sheild tail"," red eared slider"," red sand boa"," russels kukri snake"," russel's viper"," saw scaled viper"," spiny tailed lizard"," star tortoise"," reed snake"," indian tent turtle"," common trinket "," green vine snake"," ashy prinia"," brahminy worm snake"," green keelback"," beaked worm snake"," bridal snake"," snake"," chameleon"," cobra"," lizard"," turtle"," terrapin"," python"," crocodile"," viper"," tortoise"," alexandrine parakeet"," asian koel"," lineated barbet"," barn owl"," black headed munia"," black crowned night heron"," brahminy kite"," brown wood owl"," cattle egret"," collared scops owl"," common babbler"," common hawk cuckoo"," common myna"," common quail"," crow pheasant"," egyptian vulture"," eurasian eagle owl"," eurasian golden oriole"," great indian horned owl"," greater cormorant"," grey francolin"," grey heron"," hanging parrot"," hill mynah"," honeybees colony"," house crow"," house sparrow"," house swift"," indian cuckoo"," indian hoopoe"," indian pea fowl"," indian pitta"," indian pond heron"," median egret"," jungle mynah"," jungle babler"," little cormorant"," little egret"," mottled wood owl"," night jar"," oriental magpie robin"," painted stork"," black kite"," red whiskered bul bul"," red headed falcon"," rock bush quail"," blue rock pigeon"," rose ringed parakeet"," rosy starling"," shikra"," small bee eater"," small button quail"," eurasian sparrow hawk"," scaly brested munia"," spot billed duck"," spotted dove"," spotted owlet"," little sun bird"," tailor bird"," tawny eagle"," greenish warbler"," baya weaver "," white breasted waterhen"," white cheeked barbet"," white rose ringed parakeet"," small blue kingfisher"," white throated munia"," yellow legged green pigeon"," crested serpent eagle"," yellow legged button quail"," black winged kite"," jerdon's baza"," large pied wagtail"," little grebe"," plum headed parakeet"," pied bush chat"," booted warbler"," yellow wattled lapwing"," black eared kite"," common kestrel"," jungle crow"," jungle owlet"," red wattle lapwing"," short eared owl"," brown hawk owl"," tickell's flower pecker"," white breasted king fisher"," indian robin"," common moorhen"," black eagle"," house swift"," indian roller"," malabar parakeet"," white headed babbler"," white headed munia"," white throated thrush"," black shouldered woodpecker"," indian darter"," oriental white eye"," blue headed rock thrush"," oriental turtle dove"," black buck"," indian treepie"," slatty legged crake"," black naped hare"," bonnet macaque"," asian palm civet"," small indian civet "," common grey mongoose"," common yellow bat"," fox"," hanuman langur"," indian flying fox"," indian jackal"," indian pangolin"," indian pipistrelle"," indian porcupine"," indian wild boar"," jungle cat"," rhesus macaque"," short nosed fruit bat"," slender loris"," spotted deer"," three striped palm squirrel"," assamme macaque"," malabar gaint squirrel"," parakeet"," koel"," owl"," heron"," egret"," kite"," cuckoo"," myna"," quail"," vulture"," parrot"," sparrow"," swift"," fowl"," robin"," bulbul"," squirrel"," falcon"," hawk"," duck"," dove"," owlet"," eagle"," kingfisher"," woodpecker"," hare"," mongoose"," bat"," jackal"," porcupine"," deer"};
        persistList(Arrays.asList(animals), "animals");
    }
    public void seedCity(){
        String city[] = {"Bangalore","Bengaluru","bangalore","bengaluru"};
        persistList(Arrays.asList(city),"city");
    }
    public boolean isSeeded(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("KEY", Context.MODE_PRIVATE);
        if (sharedPreferences.getString("animals","empty").equalsIgnoreCase("empty"))
           return false;
        return true;
    }
}
