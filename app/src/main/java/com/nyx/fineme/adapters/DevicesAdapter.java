package com.nyx.fineme.adapters;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.nyx.fineme.MainActivity;
import com.nyx.fineme.MapsActivity;
import com.nyx.fineme.R;
import com.nyx.fineme.helper.APIUrl;
import com.nyx.fineme.helper.BackgroundServices;
import com.nyx.fineme.helper.PostAction;
import com.nyx.fineme.helper.SharedPrefManager;
import com.nyx.fineme.models.DeviceRow;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;


public abstract  class DevicesAdapter
        extends RecyclerView.Adapter {

    private List data;
    private Activity C;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name ,date  ,info ,family;
        public View body ,dist ,edit_name ,edit_info ,edit_family ;
        ImageView pic;


        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            info = (TextView) view.findViewById(R.id.info);
            family = (TextView) view.findViewById(R.id.family);
            body =  view.findViewById(R.id.body);
            dist =  view.findViewById(R.id.distance);
            edit_name =  view.findViewById(R.id.edit_name);
            edit_info =  view.findViewById(R.id.edit_info);
            edit_family =  view.findViewById(R.id.edit_family);
            pic =  view.findViewById(R.id.pic);
        }
    }


    public DevicesAdapter(List moviesList, Activity c) {
        this.data = moviesList;
        this.C = c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_device_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder h, final int position) {
        final DeviceRow r = (DeviceRow) data.get(position);
        final MyViewHolder holder = (MyViewHolder) h;
        holder.name.setText(r.name);
        holder.info.setText(r.info);
        if(!r.pic.equals(""))
        Glide.with(C)
                .load(r.pic)
                .into(holder.pic);
        holder.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPic(position);
            }
        });
        holder.body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(C , MapsActivity.class);
                i.putExtra("id" ,r.id);
                C.startActivity(i);
            }
        });

        holder.dist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(C);
                builder.setTitle("قم بتعيين المسافة الصغرى  " +
                        "\n" +
                        "(الحالية : "+r.distance+ ")");

// Set up the input
                final EditText input = new EditText(C);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("حفظ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String   m_Text = input.getText().toString();
                        int d = -1;
                        try{
                            d = Integer.parseInt(m_Text);

                            if(d<0)throw  new Exception();
                        }catch (Exception rj){
                            Toast.makeText(C, "أدخل مسافة حقيقية بين 0 و 5000", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        holder.dist.setEnabled(false);
                        final int y = d;
                        BackgroundServices.getInstance(C)
                                .setBaseUrl(APIUrl.SERVER)
                                .addPostParam("service" ,"update_dist")
                                .addPostParam("id" ,r.id)
                                .addPostParam("dist" ,d+"")
                                .addPostParam("token" ,
                                        SharedPrefManager.getInstance(C).getUserKey(
                                                SharedPrefManager.KEY_TOKEN
                                        ))
                                .CallPost(new PostAction() {
                                    @Override
                                    public void whenFinished(String status, String response) throws JSONException {
                                        holder.dist.setEnabled(true);
                                        r.distance = y;
                                        notifyDataSetChanged();
                                        Toast.makeText(C, new JSONObject(response).getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                });





                    }
                });
                builder.setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        holder.edit_family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(C);
                builder.setTitle("تعديل قائمة العائلة");

// Set up the input
                final EditText input = new EditText(C);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("حفظ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String   m_Text = input.getText().toString();
                        if(m_Text.trim().equals(""))return;

                        holder.edit_family.setEnabled(false);

                        BackgroundServices.getInstance(C)
                                .setBaseUrl(APIUrl.SERVER)
                                .addPostParam("service" ,"edit_data")
                                .addPostParam("id" ,r.id)
                                .addPostParam("key" ,"family")
                                .addPostParam("val" ,m_Text)
                                .addPostParam("token" ,
                                        SharedPrefManager.getInstance(C).getUserKey(
                                                SharedPrefManager.KEY_TOKEN
                                        ))
                                .CallPost(new PostAction() {
                                    @Override
                                    public void whenFinished(String status, String response) throws JSONException {
                                        holder.edit_family.setEnabled(true);
                                        r.family = m_Text;
                                        notifyDataSetChanged();
                                        Toast.makeText(C, new JSONObject(response).getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                });





                    }
                });
                builder.setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        //---------
        holder.edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(C);
                builder.setTitle("اعادة تسمية الجهاز  " +
                        "\n" +
                        "(الحالي : "+r.name+ ")");

// Set up the input
                final EditText input = new EditText(C);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("حفظ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String   m_Text = input.getText().toString();
                        if(m_Text.trim().equals(""))return;

                        holder.edit_name.setEnabled(false);

                        BackgroundServices.getInstance(C)
                                .setBaseUrl(APIUrl.SERVER)
                                .addPostParam("service" ,"edit_data")
                                .addPostParam("id" ,r.id)
                                .addPostParam("key" ,"address")
                                .addPostParam("val" ,m_Text)
                                .addPostParam("token" ,
                                        SharedPrefManager.getInstance(C).getUserKey(
                                                SharedPrefManager.KEY_TOKEN
                                        ))
                                .CallPost(new PostAction() {
                                    @Override
                                    public void whenFinished(String status, String response) throws JSONException {
                                        holder.edit_name.setEnabled(true);
                                        r.name = m_Text;
                                        notifyDataSetChanged();
                                        Toast.makeText(C, new JSONObject(response).getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                });





                    }
                });
                builder.setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
        //---------
        holder.edit_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(C);
                builder.setTitle("تعديل وصف الجهاز  " +
                        "\n" +
                        "(الحالي : "+r.info+ ")");

// Set up the input
                final EditText input = new EditText(C);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("حفظ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     final String   m_Text = input.getText().toString();
if(m_Text.trim().equals(""))return;

                        holder.edit_info.setEnabled(false);

                        BackgroundServices.getInstance(C)
                                .setBaseUrl(APIUrl.SERVER)
                                .addPostParam("service" ,"edit_data")
                                .addPostParam("id" ,r.id)
                                .addPostParam("key" ,"info")
                                .addPostParam("val" ,m_Text)
                                .addPostParam("token" ,
                                        SharedPrefManager.getInstance(C).getUserKey(
                                                SharedPrefManager.KEY_TOKEN
                                        ))
                                .CallPost(new PostAction() {
                                    @Override
                                    public void whenFinished(String status, String response) throws JSONException {
                                        holder.edit_info.setEnabled(true);
                                        r.info = m_Text;
                                        notifyDataSetChanged();
                                        Toast.makeText(C, new JSONObject(response).getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                });





                    }
                });
                builder.setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

public abstract  void editPic(int position);
}