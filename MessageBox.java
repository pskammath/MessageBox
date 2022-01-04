package com.psk.lockemergency.messagebox;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.psk.lockemergency.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class MessageBox {

    protected static final String MODE ="MODE" ;
    protected Dialog mDialog;
    protected boolean visible;
    protected static  String TITLE="TITLE";
    protected static String MESSAGE="MSG";
    protected static String INPUTHINT="HINT";
    protected static String DONE="DONE";
    protected static String EXIT="EXIT";
    protected static String ALERT_PASSWORD="PASSWORD";
    protected static String ALERT_ONLY="ALERT";
    protected static String IMAGE_URL="URL";
    public static  String NOBANNER="hide";



    Context c;

    public MessageBox(){}

    public MessageBox(Context c)
    {
        this.c=c;
    }

    public static Map<String, String> getConfig(String title, String message, String inputHintIfVisible, String OkButtonCaption, String exitButtonCaption, boolean InputMode, String BannerUrl) {
        return     new HashMap<String, String>() {
            {

//                put(MessageBox.TITLE, "Device Registration");
//                put(MessageBox.MESSAGE, "Device registration allows a user to flag the computer, PDA, mobile phone, or other devices he is logging in with as a safe device.  The device is added to the user's profile as a registered device.");
//                put(MessageBox.INPUTHINT, "Enter Password");
//                put(MessageBox.DONE, "Register");
//                put(MessageBox.EXIT, "Cancel");
//                put(MessageBox.MODE, MessageBox.ALERT_ONLY);
//                put(MessageBox.IMAGE_URL, "https://i.pinimg.com/originals/f2/d9/c3/f2d9c3dbdc12351f8d32585b8cf5152b.gif");

                put(MessageBox.TITLE, title);
                put(MessageBox.MESSAGE,message);
                put(MessageBox.INPUTHINT, inputHintIfVisible);
                put(MessageBox.DONE, OkButtonCaption);
                put(MessageBox.EXIT, exitButtonCaption);
                put(MessageBox.MODE, InputMode? MessageBox.ALERT_PASSWORD : MessageBox.ALERT_ONLY);
                put(MessageBox.IMAGE_URL, BannerUrl );

            }
        };
    }

    public void show()
    {

        visible=true;
        mDialog.show();
    }






    public void show(Map<String,String> map, responseLstnr clstnr){
        setDefaults(c,clstnr , map);
        visible=true;
        mDialog.show();
    }


    public void dismiss() {
        if (mDialog != null) {
            visible=false;
            mDialog.dismiss();
            mDialog = null;
        }
    }

    public boolean isShowing() {
        return visible;
    }



    public void setDefaults(Context c, responseLstnr lstnr, Map<String, String> values) {
        mDialog = new Dialog(c);
        // no tile for the dialog
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        mDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressdialogbg);


        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);

        mDialog.setContentView(R.layout.custom_dialog_layout);


        TextView okt= mDialog.findViewById(R.id.cus_dlg_ok);
        TextView cancelt= mDialog.findViewById(R.id.cus_dlg_cancel);

        ((TextView)mDialog.findViewById(R.id.cus_dlg_title)).setText(values.get(TITLE));
        ((TextView)mDialog.findViewById(R.id.cus_dlg_content)).setText(values.get(MESSAGE));
       if(values.get(DONE).length()>0)
           ((TextView)mDialog.findViewById(R.id.cus_dlg_ok)).setText(values.get(DONE));
       else
           ((TextView)mDialog.findViewById(R.id.cus_dlg_ok)).setVisibility(View.GONE);

        if(values.get(EXIT).length()>0)
            ((TextView)mDialog.findViewById(R.id.cus_dlg_cancel)).setText(values.get(EXIT));
        else
            ((TextView)mDialog.findViewById(R.id.cus_dlg_cancel)).setVisibility(View.GONE);

        ((EditText)mDialog.findViewById(R.id.editTextTextPassword)).setHint(values.get(INPUTHINT));
        String mode=Objects.requireNonNull(values.get(MODE));
        if(mode.contains(ALERT_PASSWORD))
        {
            mDialog.findViewById(R.id.editTextTextPassword).setVisibility(View.VISIBLE);
        }
        else if(mode.contains(ALERT_ONLY))
        {
            mDialog.findViewById(R.id.editTextTextPassword).setVisibility(View.GONE);
        }
       if(!values.get(IMAGE_URL).equals(NOBANNER))
       {
           Glide.with(mDialog.getContext())
                   .load(values.get(IMAGE_URL))
                   .centerCrop()
                   .into(((ImageView) mDialog.findViewById(R.id.icon)));
       }
       else
       {
           ((ImageView) mDialog.findViewById(R.id.icon)).setVisibility(View.GONE);
       }






        okt.setOnClickListener(v -> {
            if(mDialog!=null)
                mDialog.dismiss();

            String et=((EditText) mDialog.findViewById(R.id.editTextTextPassword)).getText().toString();
           if(MODE==ALERT_PASSWORD)
               lstnr.getInput(et);
           else
               lstnr.onOk();
        });
        cancelt.setOnClickListener(v -> {
            if(mDialog!=null)
                mDialog.dismiss();
            lstnr.onCancel();
        });

    }
    public interface responseLstnr{
        void onOk();
        void onCancel();
        void getInput(String input);
    }
}
