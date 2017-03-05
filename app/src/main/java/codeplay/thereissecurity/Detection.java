package codeplay.thereissecurity;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ankush on 06-03-2017.
 */
public final class Detection {
    private static Context context;

    static String[] targetString = new String[] {"action","action energy","aggression","aggressive","ammunition","angry","army","athlete","balaclava","bandit","battle","blaze","blood","bodybuilding","boxer","brawny","broken","bullet","burn","burnt","calamity","caliber","camouflage","commando","combat","competition","cry","danger","defence","demolished","demolition","despair","dirty","disguise","earthquake","eruption","exercise","fall","fighter","fist","flame","flammable","focus","force","fraud","grief","gun","hit","horror","hurricane","illegal","inferno","intensiveness","knife","knife blade","knockout","machine gun","mafia","magnum","martial","martial arts","mask","match","military","military uniform","offence","offense","pain","pistol","police","protection","punch","revolver","rifle","rock","rope","ruin","sadness","safety","scared","security","sharp","shotgun","skirmish","smoke","soldier","spy","steal","stone","storm","strength","strike","strong","surveillance","sword","tank","theft","thief","tornado","trigger","vicious","war","weapon","wildfire","wrestling","zombie"};

    public static int check(String attributeString){
        int count=0;
        for (String s:targetString) {
            if (attributeString.toLowerCase().equals(s))
                count++;
        }
        return count;
    }

    public static void notify_detected(Context context){
        Detection.context=context;
        sendEmail();
    }
/*    private static void sendEmail(){


        new AsyncTask<Void,Void,Boolean>(){

            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    GMailSender sender = new GMailSender("spunk.code@gmail.com", "ax50ankush");
                    sender.sendMail("Dangerous Behaviour Detected!",
                            "Your device has predicted a dangerous activity taking place in the field of view!",
                            "spunk.code@gmail.com",
                            "ankush.1996@gmail.com");
                    return true;
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                    return false;
                    //Toast.makeText(context,"Error sending email", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            protected void onPostExecute(Boolean result){
                if (result)
                    Toast.makeText(Detection.context, "Mail Sent",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Detection.context,"Error sending email", Toast.LENGTH_LONG).show();
            }
        }.execute();
    }*/
    /*private static void sendEmail(){
        String email = "ankush.1996@gmail.com";
        String subject = "Dangerous Behaviour Detected";
        String message = "Your device has predicted a dangerous activity taking place in the field of view";

        new SendMail(Detection.context,email,subject,message).execute();
    }*/

    private static void sendEmail(){
        String username="spunk.code@gmail.com";
        String password="ax50ankush";
        //String[] toEmails = {"ankush.1996@gmail.com","shubham.note3@gmail.com","swapnillohani96@gmail.com"};
        List<String> toMails=new ArrayList<>();
        toMails.add("ankush.1996@gmail.com");
        toMails.add("shubham.note3@gmail.com");
        toMails.add("swapnillohani96@gmail.com");
        String subject = "Dangerous Behaviour Detected";
        String message = "Your device has predicted a dangerous activity taking place in the field of view";
        new SendMailTask().execute(username,
                password, toMails, subject, message);
    }

    public static class SendMailTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object... args) {
            try {
                Log.i("SendMailTask", "About to instantiate GMail...");
                publishProgress("Processing input....");
                GMail androidEmail = new GMail(args[0].toString(),
                        args[1].toString(), (List) args[2], args[3].toString(),
                        args[4].toString());
                publishProgress("Preparing mail message....");
                androidEmail.createEmailMessage();
                publishProgress("Sending email....");
                androidEmail.sendEmail();
                publishProgress("Email Sent.");
                Log.i("SendMailTask", "Mail Sent.");
            } catch (Exception e) {
                publishProgress(e.getMessage());
                Log.e("SendMailTask", e.getMessage(), e);
            }
            return null;
        }

        @Override
        public void onPostExecute(Object result) {
            Toast.makeText(Detection.context,"Mail Sent",Toast.LENGTH_SHORT).show();
        }

    }
}

