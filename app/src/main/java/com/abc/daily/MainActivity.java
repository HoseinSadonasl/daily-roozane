package com.abc.daily;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextClock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abc.daily.Adapters.NotesAdapter;
import com.abc.daily.Objects.NoteObjects;
import com.abc.daily.Objects.WeatherModels;
import com.abc.daily.app.Application;
import com.abc.daily.app.DatabaseConnector;
import com.abc.daily.app.ShamsiCalendar;
import com.abc.daily.app.*;
import com.abc.daily.app.db;
import com.abc.daily.interfaces.DialogInterface;
import com.abc.daily.ui.MyDailyDialog;
import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, DialogInterface {

    RecyclerView recyclerView;
    FloatingActionButton fab;
    NotesAdapter adapter;
    List<NoteObjects> list = new ArrayList<>();
    DatabaseConnector dbm = new DatabaseConnector(this);
    NavigationView navigationView;
    DrawerLayout drawerlayout;
    AppCompatImageView weatherImage, search_ic, tempDgree;
    AppCompatTextView locationName, temp, status, date, weekDay;
    AppCompatEditText searchInput;
    MyDailyDialog dialog;
    Boolean updateList = false;
    Boolean backPressed = false;
    SpinKitView spinKitView;
    String cityNameString;


    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (searchInput == null) return;
            list.clear();
            list.addAll(readdata(searchInput.getText().toString()));
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        drawerlayout = findViewById(R.id.drawerlayout);
//        navigationView = findViewById(R.id.navigationView);
        recyclerView = findViewById(R.id.recyclerView);
        weatherImage = findViewById(R.id.weatherImage);
        tempDgree = findViewById(R.id.tempDegree);
        locationName = findViewById(R.id.locationName);
        search_ic = findViewById(R.id.search_ic);
        searchInput = findViewById(R.id.searchInput);
        temp = findViewById(R.id.temp);
        status = findViewById(R.id.status);
        date = findViewById(R.id.date);
        weekDay = findViewById(R.id.weekDay);
        fab = findViewById(R.id.fab);
        spinKitView = findViewById(R.id.spin_kit);

        fab.setOnClickListener(this);
        locationName.setOnClickListener(this);
        search_ic.setOnClickListener(this);

        updateList = true;

        list = dbm.getAllNotes();
        adapter = new NotesAdapter(MainActivity.this, list);
        adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);




        //navigationView.setNavigationItemSelectedListener(this);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 1000);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        getWeather();
        getDateTime();
    }

    private void getDateTime() {
        Calendar cal = Calendar.getInstance();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        String dayOfWeekStr = "";
        switch (dayOfWeek) {
            case 1 : dayOfWeekStr = "Sunday";
                break;
            case 2 : dayOfWeekStr = "Monday";
                break;
            case 3 : dayOfWeekStr = "Tuesday";
                break;
            case 4 : dayOfWeekStr = "Wednesday";
                break;
            case 5 : dayOfWeekStr = "Thursday";
                break;
            case 6 : dayOfWeekStr = "Friday";
                break;
            case 7 : dayOfWeekStr = "Saturday";
                break;
        }
        String calendar = ShamsiCalendar.getCurrentShamsidate();
        date.setText(calendar);
        weekDay.setText(dayOfWeekStr);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab : {
                startActivity(new Intent(MainActivity.this, AddReadNote.class));
                break;
            }
            case R.id.locationName : {
                getCityName();
                break;
            }
            case R.id.search_ic : {
                readdata(searchInput.getText().toString());
                break;
            }
        }
    }


   @Override
    protected void onResume() {
            list.clear();
        if (searchInput.getText().toString() != null) {
            list.addAll(readdata(searchInput.getText().toString()));
        } else {
            list.addAll(readdata(""));
        }
            adapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onBackPressed() {

        if (!backPressed){
            backPressed = true;
            searchInput.clearFocus();
            app.t("Please press back button again to exit app");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backPressed = false;
                }
            },1500);
        } else {
            super.onBackPressed();
        }
    }

    private List<NoteObjects> readdata(String inputText) {

        List<NoteObjects> objList = new ArrayList<>();

        Cursor cursor = null;
        if (inputText.equals("")) {
            cursor = dbm.getDb().rawQuery("SELECT * FROM " + db.Tables.DAILY_NOTE_TABLE , null);

        } else {
            cursor = dbm.getDb().rawQuery("SELECT * FROM " + db.Tables.DAILY_NOTE_TABLE +
                    " WHERE " + db.Note.NOTE_TITLE + " LIKE '%" + inputText + "%'" +
                    " OR " + db.Note.NOTE_CONTENT  + " LIKE '%" + inputText + "%'", null);
        }

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(db.Note.NOTE_ID));
            String title = cursor.getString(cursor.getColumnIndex(db.Note.NOTE_TITLE));
            String content = cursor.getString(cursor.getColumnIndex(db.Note.NOTE_CONTENT));
            String reminderDate = cursor.getString(cursor.getColumnIndex(db.Note.REMINDER_DATE));
            String reminderTime = cursor.getString(cursor.getColumnIndex(db.Note.REMINDER_TIME));
            String date  = cursor.getString(cursor.getColumnIndex(db.Note.NOTE_DATE));
            String lastModify = cursor.getString(cursor.getColumnIndex(db.Note.NOTE_LAST_MODIFY));

            NoteObjects objects = new NoteObjects();
            objects.setId(id);
            objects.setNoteTitle(title);
            objects.setNoteContent(content);
            objects.setReminderDate(reminderDate);
            objects.setReminderTime(reminderTime);
            objects.setNoteDate(date);
            objects.setNoteModifyDate(lastModify);
            objList.add(objects);
        }
        cursor.close();
        return objList;
    }

    private void getWeather() {
        cityNameString = spref.get(spref.tags.WEATHER).getString(spref.Weather.cityName, spref.Weather.defaultCityName);
        if (cityNameString.isEmpty() || locationName.getText().toString().equals("Invalid Location"))  {
            getCityName();
        }
        Map<String ,String> data=new HashMap();
            data.put("appid","6c7b0789e344c8bdd8f0935ff4568e72");
            data.put("q", cityNameString);


        Application.getApi().getCurrentWeather(data).enqueue(new Callback<WeatherModels>() {
            @Override
            public void onResponse(Call<WeatherModels> call, Response<WeatherModels> response) {

                if (response.code() == 200) {
                    tempDgree.setVisibility(View.VISIBLE);
                    temp.setVisibility(View.VISIBLE);
                    weatherImage.setVisibility(View.VISIBLE);
                    spinKitView.setVisibility(View.GONE);
                    locationName.setText(response.body().getName());
                    temp.setText(String.valueOf((int) (response.body().getMain().getTemp() - 273.15)));
                    status.setText(response.body().getWeather().get(0).getMain());
                    String imgUrl = String.valueOf(response.body().getWeather().get(0).getIcon());
                    Glide.with(weatherImage)
                            .load("https://openweathermap.org/img/wn/"+ imgUrl +"@2x.png")
                            .into(weatherImage);
                } else{
                    locationName.setText("Invalid Location");
                    spinKitView.setVisibility(View.VISIBLE);
                    tempDgree.setVisibility(View.GONE);
                    temp.setVisibility(View.GONE);
                    weatherImage.setVisibility(View.GONE);
                    app.t("Ops! Invalid city name, please enter a valid city name");
                }


            }

            @Override
            public void onFailure(Call<WeatherModels> call, Throwable t) {
            }
        });
    }


    private void getCityName() {
        dialog = new MyDailyDialog(this,
                "Ok", "Cancel",
                "City name",
                "Please enter city name", 1,
                1,this, 0, 0);
        dialog.show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int mId = item.getItemId();

        switch (mId) {
            case R.id.createNote : {
                startActivity(new Intent(MainActivity.this, AddReadNote.class));
                break;
            }
            case R.id.settings : {
                app.t("Setting pressed");
                break;
            }
            case R.id.about : {
                app.t("About pressed");
                break;
            }
        }
        drawerlayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPositiveClick() {
        locationName.setText(dialog.getInputText());
        spref.get(spref.tags.WEATHER).edit().putString(spref.Weather.cityName, locationName.getText().toString()).apply();
        getWeather();
        dialog.dismiss();
    }

    @Override
    public void onNegativeClick() {
        dialog.dismiss();
    }

}