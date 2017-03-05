package codeplay.thereissecurity;

/**
 * Created by Ankush on 06-03-2017.
 */
public final class Detection {
    static String[] targetString = new String[] {"action","action energy","aggression","aggressive","ammunition","angry","army","athlete","balaclava","bandit","battle","blaze","blood","bodybuilding","boxer","brawny","broken","bullet","burn","burnt","calamity","caliber","camouflage","commando","combat","competition","cry","danger","defence","demolished","demolition","despair","dirty","disguise","earthquake","eruption","exercise","fall","fighter","fist","flame","flammable","focus","force","fraud","grief","gun","hit","horror","hurricane","illegal","inferno","intensiveness","knife","knife blade","knockout","machine gun","mafia","magnum","martial","martial arts","mask","match","military","military uniform","offence","offense","pain","pistol","police","protection","punch","revolver","rifle","rock","rope","ruin","sadness","safety","scared","security","sharp","shotgun","skirmish","smoke","soldier","spy","steal","stone","storm","strength","strike","strong","surveillance","sword","tank","theft","thief","tornado","trigger","vicious","war","weapon","wildfire","wrestling","zombie"};

    public static int check(String attributeString){
        int count=0;
        for (String s:targetString) {
            if (attributeString.toLowerCase().equals(s))
                count++;
        }
        return count;
    }

    public static void notify_detected(){

    }

}

