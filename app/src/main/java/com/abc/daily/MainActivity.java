package com.abc.daily;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
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
import com.google.android.material.button.MaterialButton;
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
        View.OnClickListener, DialogInterface, NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    FloatingActionButton fab;
    NotesAdapter adapter;
    List<NoteObjects> list = new ArrayList<>();
    DatabaseConnector dbm = new DatabaseConnector(this);
    DrawerLayout drawerlayout;
    AppCompatImageView weatherImage, search_ic, tempDgree, more;
    AppCompatTextView locationName, temp, status, date, weekDay;
    NavigationView navigationView;
    MaterialButton sort, drawerAddNote_btn, info_btn, green_btn,  teal_btn, blue_btn, orange_btn, red_btn, purple_btn;
    AppCompatEditText searchInput;
    MyDailyDialog dialog;
    ConstraintLayout weatherParent;
    Boolean updateList = false;
    Boolean backPressed = false;
    SpinKitView spinKitView;
    String cityNameString;
    String orderState;
    String orderType;

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
        restoreTheme();
        setContentView(R.layout.activity_main);
        init();
        //weatherParent.setBackground(ContextCompat.getDrawable(this, R.drawable.bc_wea_purple));
    }

    private void init() {
        drawerlayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.navigationView);
        recyclerView = findViewById(R.id.recyclerView);
        weatherImage = findViewById(R.id.weatherImage);
        tempDgree = findViewById(R.id.tempDegree);
        locationName = findViewById(R.id.locationName);
        search_ic = findViewById(R.id.search_ic);
        searchInput = findViewById(R.id.searchInput);
        weatherParent = findViewById(R.id.weatherParent);
        temp = findViewById(R.id.temp);
        status = findViewById(R.id.status);
        date = findViewById(R.id.date);
        weekDay = findViewById(R.id.weekDay);
        fab = findViewById(R.id.fab);
        spinKitView = findViewById(R.id.spin_kit);
        sort = findViewById(R.id.sort);

        View header = navigationView.getHeaderView(0);
        drawerAddNote_btn = header.findViewById(R.id.drawerAddNote_btn);
        info_btn = header.findViewById(R.id.info_btn);
        green_btn = header.findViewById(R.id.green_btn);
        teal_btn = header.findViewById(R.id.teal_btn);
        blue_btn = header.findViewById(R.id.blue_btn);
        orange_btn = header.findViewById(R.id.orange_btn);
        red_btn = header.findViewById(R.id.red_btn);
        purple_btn = header.findViewById(R.id.purple_btn);


        fab.setOnClickListener(this);
        locationName.setOnClickListener(this);
        search_ic.setOnClickListener(this);
        sort.setOnClickListener(this);
        drawerAddNote_btn.setOnClickListener(this);
        info_btn.setOnClickListener(this);
        green_btn.setOnClickListener(this);
        teal_btn.setOnClickListener(this);
        blue_btn.setOnClickListener(this);
        orange_btn.setOnClickListener(this);
        red_btn.setOnClickListener(this);
        purple_btn.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);

        updateList = true;

        list = dbm.getAllNotes();
        adapter = new NotesAdapter(MainActivity.this, list);
        adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


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

        restoreDara();
//        if (Application.isNetworkEnabled()) {
//        } else {
//            app.t("Network is unavailable");
//        }

        getWeather();
        getDateTime();
    }
    private void restoreTheme() {
        String color = spref.get(spref.tags.THEME).getString(spref.Theme.THEME_COLOR, spref.Theme.DEFAULT_THEME_COLOR);
        switch (color) {
            case "purple" : {
                setTheme(R.style.Theme_DailyNoActionbarMainPurple);
                break;
            }
            case "red" : {
                setTheme(R.style.Theme_DailyNoActionbarMainRed);
                break;
            }
            case "orange" : {
                setTheme(R.style.Theme_DailyNoActionbarMainOrange);
                break;
            }
            case "blue" : {
                setTheme(R.style.Theme_DailyNoActionbarMainBlue);
                break;
            }
            case "green" : {
                setTheme(R.style.Theme_DailyNoActionbarMainGreen);
                break;
            }
            case "teal" : {
                setTheme(R.style.Theme_DailyNoActionbarMainTeal);
                break;
            }
            default:setTheme(R.style.Theme_DailyNoActionbarMainTeal);
        }
    }

    private void restoreDara() {
        cityNameString = spref.get(spref.tags.WEATHER).getString(spref.Weather.cityName, spref.Weather.defaultCityName);
        orderState = spref.get(spref.tags.SORT).getString(spref.tags.SORT, spref.SortState.SORT_DEFAULT);
        orderType  = spref.get(spref.tags.SORT).getString(spref.tags.SORT_TYPE, spref.SortType.DEFAULT_TYPE);
        sort.setText(spref.get(spref.tags.SORT_BTN_TXT).getString(spref.tags.SORT_BTN_TXT, spref.tags.SORT_BTN_DEFAULT_TXT));
        sort.setIcon(ContextCompat.getDrawable(this,spref.get(spref.tags.SORT).getInt(spref.tags.SORT_ICON_ID,R.drawable.ic_dateascldpi)));
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
            case R.id.locationName : {
                getCityName();
                break;
            }
            case R.id.search_ic : {
                readdata(searchInput.getText().toString());
                break;
            }
            case R.id.sort : {
                sortData();
                break;
            }
            case R.id.fab : {
                startActivity(new Intent(MainActivity.this, AddReadNote.class));
                break;
            }
            case R.id.drawerAddNote_btn : {
                startActivity(new Intent(MainActivity.this, AddReadNote.class));
                break;
            }
            case R.id.green_btn : {
                saveColorVal("green");
                break;
            }
            case R.id.teal_btn : {
                saveColorVal("teal");
                break;
            }
            case R.id.blue_btn : {
                saveColorVal("blue");
                break;
            }
            case R.id.orange_btn : {
                saveColorVal("orange");
                break;
            }
            case R.id.red_btn : {
                saveColorVal("red");
                break;
            }
            case R.id.purple_btn : {
                saveColorVal("purple");
                break;
            }
            case R.id.info_btn : {

                break;
            }
        }
    }

    private void saveColorVal(String colorName) {
        spref.get(spref.tags.THEME).edit().putString(spref.Theme.THEME_COLOR, colorName).apply();
        recreate();
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
            },3000);
        } else {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }

    private List<NoteObjects> readdata(String inputText) {

        app.l(orderState + "-" +  orderType);

        List<NoteObjects> objList = new ArrayList<>();
        Cursor cursor = null;
        if (inputText.equals("")) {
            cursor = dbm.getDb().rawQuery("SELECT * FROM " + db.Tables.DAILY_NOTE_TABLE +
                    " ORDER BY " + orderState + " " + orderType , null);

        } else {
            cursor = dbm.getDb().rawQuery("SELECT * FROM " + db.Tables.DAILY_NOTE_TABLE +
                    " WHERE " + db.Note.NOTE_TITLE + " LIKE '%" + inputText + "%'" +
                    " OR " + db.Note.NOTE_CONTENT  + " LIKE '%" + inputText + "%'"+
                    " ORDER BY " + orderState + " " + orderType , null);
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

    private void sortData() {
        int iconId = 0;
        if (orderState.equals(spref.SortState.SORT_BY_DATE) && orderType.equals(spref.SortType.ASC)) {
            orderState = spref.SortState.SORT_BY_DATE;
            orderType = spref.SortType.DESC;
            sort.setText("Date");
            sort.setIconResource(R.drawable.ic_datedescldpi);
            iconId=getResources().getIdentifier(getResources().getResourceEntryName(R.drawable.ic_datedescldpi),"drawable","com.abc.daily");
        } else if (orderState.equals(spref.SortState.SORT_BY_DATE) && orderType.equals(spref.SortType.DESC)) {
            orderState = spref.SortState.SORT_BY_NAME;
            orderType = spref.SortType.ASC;
            sort.setText("Name");
            sort.setIconResource(R.drawable.ic_titleascldpi);
            iconId=getResources().getIdentifier(getResources().getResourceEntryName(R.drawable.ic_titleascldpi),"drawable","com.abc.daily");
        } else if (orderState.equals(spref.SortState.SORT_BY_NAME) && orderType.equals(spref.SortType.ASC)) {
            orderState = spref.SortState.SORT_BY_NAME;
            orderType = spref.SortType.DESC;
            sort.setText("Name");
            sort.setIconResource(R.drawable.ic_titledescldpi);
            iconId=getResources().getIdentifier(getResources().getResourceEntryName(R.drawable.ic_titledescldpi),"drawable","com.abc.daily");
        } else if (orderState.equals(spref.SortState.SORT_BY_NAME) && orderType.equals(spref.SortType.DESC)) {
            orderState = spref.SortState.SORT_BY_DATE;
            orderType = spref.SortType.ASC;
            sort.setText("Date");
            sort.setIconResource(R.drawable.ic_dateascldpi);
            iconId=getResources().getIdentifier(getResources().getResourceEntryName(R.drawable.ic_dateascldpi),"drawable","com.abc.daily");
        }
        spref.get(spref.tags.SORT).edit().putString(spref.tags.SORT, orderState).apply();
        spref.get(spref.tags.SORT).edit().putString(spref.tags.SORT_TYPE, orderType).apply();
        spref.get(spref.tags.SORT_BTN_TXT).edit().putString(spref.tags.SORT_BTN_TXT,sort.getText().toString()).apply();
        spref.get(spref.tags.SORT).edit().putInt(spref.tags.SORT_ICON_ID, iconId).apply();
        list.clear();
        list.addAll(readdata(""));
        adapter.notifyDataSetChanged();
    }

    private static final String TAG = "MainActivity";
    private void getWeather() {
        if (cityNameString.isEmpty() || locationName.getText().toString().equals("Invalid Location"))  {
            getCityName();
        }
        Map<String ,String> data=new HashMap();
            data.put("appid","6c7b0789e344c8bdd8f0935ff4568e72");
            data.put("q", cityNameString);
        Log.d(TAG, "getWeather: getting data");

        Application.getApi().getCurrentWeather(data).enqueue(new Callback<WeatherModels>() {
            @Override
            public void onResponse(Call<WeatherModels> call, Response<WeatherModels> response) {
                Log.d(TAG, "onResponse: "+response.body());
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
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }


    private void getCityName() {
        dialog = new MyDailyDialog(
                this,
                "Ok",
                "Cancel",
                "City name",
                "Please enter city name",
                1,
                1,
                this,
                0,
                0);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onPositiveClick() {
        locationName.setText(dialog.getInputText());
        spref.get(spref.tags.WEATHER).edit().putString(spref.Weather.cityName, locationName.getText().toString()).apply();
        cityNameString = spref.get(spref.tags.WEATHER).getString(spref.Weather.cityName, spref.Weather.defaultCityName);

        getWeather();
        dialog.dismiss();
    }

    @Override
    public void onNegativeClick() {
        dialog.dismiss();
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
}