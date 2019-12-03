package com.faculdade.pa_ap2.Activities.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.faculdade.libdownloadmanager.DownloadFacade;
import com.faculdade.pa_ap2.R;

public class Mp3Activity extends AppCompatActivity {

    ListView list;
    String titles[] = {"Coldplay Album - Viva La Vida",
                        "U2 Album - Beautiful Day",
                        "Mumford and Sons Album - Hopeless",
                        "The Fray Album - Somebody that I used to know"};
    String sources[] = {"https://file.io/YRfYne", "https://file.io/sdmURl", "https://file.io/YtQlQX", "https://file.io/C07SoI"};
    //The images are from drawable
    int imgs[] = {R.drawable.vivalavida, R.drawable.u2, R.drawable.mumford, R.drawable.thefray};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp3_activity);

        list = findViewById(R.id.listView_songs);

        //Creating instance of a class MyAdapter
        MyAdapter adapter = new MyAdapter (this, titles, imgs, sources);

        //set Adapter to list
        list.setAdapter(adapter);

        //handle item clicks
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0){
                    DownloadFacade.getInstance().getDownloadController().download(view.getContext(), "https://file.io/YRfYne", "ColdPlay", "mp3");
                }if(position == 1){
                    DownloadFacade.getInstance().getDownloadController().download(view.getContext(), "https://file.io/sdmURl", "Gotye", "mp3");
                }if(position == 2){
                    DownloadFacade.getInstance().getDownloadController().download(view.getContext(), "https://file.io/YtQlQX", "MumfordAndSons", "mp3");
                }if(position == 3){
                    DownloadFacade.getInstance().getDownloadController().download(view.getContext(), "https://file.io/C07SoI", "TheFray", "mp3");
                }if(position == 4){
                    DownloadFacade.getInstance().getDownloadController().download(view.getContext(), "https://file.io/PDMwJJ", "U2", "mp3");
                }

            }
        });



        /*//------- TESTING LIB --------
        DownloadFacade.getInstance().getDownloadController()
                .download(this, "https://www.infoescola.com/wp-content/uploads/2017/07/baleia-azul-565369243.jpg");
                //Obs.: The activity is already a context!*/

    }// closing onCreate


    class MyAdapter extends ArrayAdapter<String>{
        Context context;
        String myTitles[];
        String mySources[];
        int [] imgs;

        MyAdapter(Context c, String[] titles, int[] imgs, String [] sources){
        super(c, R.layout.row_listview, R.id.text_title, titles);
        this.context = c;
        this.imgs = imgs;
        this.myTitles = titles;
        this.mySources = sources;
        }

        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row_listView = layoutInflater.inflate(R.layout.row_listview, parent, false);
            ImageView images = row_listView.findViewById(R.id.photo_album);
            TextView myTitle = row_listView.findViewById(R.id.text_title);
            TextView mySources = row_listView.findViewById(R.id.text_source);
            images.setImageResource(imgs[position]);
            myTitle.setText(titles[position]);
            mySources.setText(sources[position]);

            return row_listView;
        }
    }

}


