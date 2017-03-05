package codeplay.thereissecurity;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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
    private static void sendEmail(){
        String email = "ankush.1996@gmail.com";
        String subject = "Dangerous Behaviour Detected";
        String message = "Your device has predicted a dangerous activity taking place in the field of view";

        new SendMail(Detection.context,email,subject,message).execute();
    }



    private static class SendMail extends AsyncTask<Void,Void,Void> {

        //Declaring Variables
        private Context context;
        private Session session;

        //Information to send email
        private String email;
        private String subject;
        private String message;


        //Class Constructor
        public SendMail(Context context, String email, String subject, String message){
            //Initializing variables
            this.context = context;
            this.email = email;
            this.subject = subject;
            this.message = message;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Showing a success message
            Toast.makeText(context,"Message Sent",Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            //Creating properties
            Properties props = new Properties();

            //Configuring properties for gmail
            //If you are not using gmail you may need to change the values
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");

            //Creating a new session
            session = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {
                        //Authenticating the password
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication("spunk.code@gmail.com", "ax50ankush");
                        }
                    });

            try {
                //Creating MimeMessage object
                MimeMessage mm = new MimeMessage(session);

                //Setting sender address
                mm.setFrom(new InternetAddress("spunk.code@gmail.com"));
                //Adding receiver
                mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
                //Adding subject
                mm.setSubject(subject);
                //Adding message
                mm.setText(message);

                //Sending email
                Transport.send(mm);

            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

