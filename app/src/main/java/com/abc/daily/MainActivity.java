package com.abc.daily;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;

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

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements
        View.OnClickListener, DialogInterface, NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    FloatingActionButton fab;
    NotesAdapter adapter;
    List<NoteObjects> list = new ArrayList<>();
    DatabaseConnector dbm = new DatabaseConnector(this);
    DrawerLayout drawerlayout;
    AppCompatImageView weatherImage, search_ic, tempDgree, illustration;
    AppCompatTextView locationName, temp, date, weekDay;
    NavigationView navigationView;
    MaterialButton sort, drawerAddNote_btn, info_btn, green_btn,  teal_btn, blue_btn, orange_btn, red_btn, purple_btn;
    AppCompatEditText searchInput;
    MyDailyDialog dialog;
    ConstraintLayout weatherParent;
    Boolean updateList = false, backPressed = false, sprefBol;
    SpinKitView spinKitView;
    String cityNameString, orderState, orderType, color;

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
        restoreTheme(false);
        setContentView(R.layout.layout_notes_fragment);
        init();

    }

    private void init() {
        drawerlayout = findViewById(R.id.themes_drawerLayout_notesFragment);
        navigationView = findViewById(R.id.themes_navigationView_notesFragment);
        recyclerView = findViewById(R.id.recyclerView_notesList_notesFragment);
        weatherImage = findViewById(R.id.imageView_weatherImage_notesFragment);
        tempDgree = findViewById(R.id.imageView_temp_notesFragment);
        locationName = findViewById(R.id.textView_location_notesFragment);
        search_ic = findViewById(R.id.imageView_searchIcon_notesFragment);
        searchInput = findViewById(R.id.editText_searchNote_notesFragment);
        weatherParent = findViewById(R.id.constraintLayout_weatherParent_notesFragment);
        illustration = findViewById(R.id.imageView_illustration_notesFragment);
        temp = findViewById(R.id.textView_temp_notesFragment);
        date = findViewById(R.id.textView_date_notesFragment);
        weekDay = findViewById(R.id.textView_weekDay_notesFragment);
        fab = findViewById(R.id.fab_addNote_notesFragment);
        spinKitView = findViewById(R.id.spinKit_weatherLoading_notesFragment);
        sort = findViewById(R.id.button_sortItems_notesFragment);

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

        illustration.setVisibility(list.size() >= 1 ? View.GONE : View.VISIBLE);

        restoreDara();
        getWeather();
        getDateTime();

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
    }

    private void restoreDara() {
        restoreTheme(true);
        cityNameString = spref.get(spref.WEATHER).getString(spref.Weather.CITY_NAME, spref.Weather.DEFAULT_CITY_NAME);
        orderState = spref.get(spref.SORT).getString(spref.SORT, spref.SortState.SORT_DEFAULT);
        orderType  = spref.get(spref.SORT).getString(spref.SORT_TYPE, spref.SortType.DEFAULT_TYPE);
        String sortBtnTxt = spref.get(spref.SORT).getString(spref.SORT, spref.SortState.SORT_DEFAULT);
        if (sortBtnTxt.equals(spref.SortState.SORT_BY_DATE)) {
            sort.setText(spref.SortButtonText.SORT_BTN_TXT_DATE);
        } else sort.setText(spref.SortButtonText.SORT_BTN_TXT_NAME);
        sort.setIcon(ContextCompat.getDrawable(this, spref.get(spref.SORT).getInt(spref.SORT_ICON_ID, R.drawable.ic_dateascldpi)));
    }

    public void restoreTheme(Boolean setBackgroundImage) {

        color = spref.get(spref.THEME).getString(spref.Theme.THEME_COLOR, spref.Theme.DEFAULT_THEME_COLOR);
        switch (color) {
            case  spref.Theme.PURPLE_COLOR : {
                if (setBackgroundImage) {
                    weatherParent.setBackground(ContextCompat.getDrawable(this, R.drawable.bc_wea_purple));
                }
                setTheme(R.style.Theme_DailyNoActionbarMainPurple);
                break;
            }
            case spref.Theme.RED_COLOR : {
                if (setBackgroundImage) {
                    weatherParent.setBackground(ContextCompat.getDrawable(this, R.drawable.bc_wea_red));
                }
                setTheme(R.style.Theme_DailyNoActionbarMainRed);
                break;
            }
            case spref.Theme.ORANGE_COLOR : {
                if (setBackgroundImage) {
                    weatherParent.setBackground(ContextCompat.getDrawable(this, R.drawable.bc_wea_orange));
                }
                setTheme(R.style.Theme_DailyNoActionbarMainOrange);
                break;
            }
            case spref.Theme.BLUE_COLOR : {
                if (setBackgroundImage) {
                    weatherParent.setBackground(ContextCompat.getDrawable(this, R.drawable.bc_wea_blue));
                }
                setTheme(R.style.Theme_DailyNoActionbarMainBlue);
                break;
            }
            case spref.Theme.GREEN_COLOR : {
                if (setBackgroundImage) {
                    weatherParent.setBackground(ContextCompat.getDrawable(this, R.drawable.bc_wea_green));
                }
                setTheme(R.style.Theme_DailyNoActionbarMainGreen);
                break;
            }
            case spref.Theme.TEAL_COLOR : {
                if (setBackgroundImage) {
                    weatherParent.setBackground(ContextCompat.getDrawable(this, R.drawable.bc_wea_teal));
                }
                setTheme(R.style.Theme_DailyNoActionbarMainTeal);

                break;
            }
            default : defaultState(setBackgroundImage);
        }
    }


    private void defaultState (Boolean setBackgroundImage) {
        if (setBackgroundImage) {
            weatherParent.setBackground(ContextCompat.getDrawable(this, R.drawable.bc_wea_teal));
        }
        setTheme(R.style.Theme_DailyNoActionbarMainTeal);
    }

    private void getDateTime() {
        Calendar cal = Calendar.getInstance();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        String dayOfWeekStr = "";
        switch (dayOfWeek) {
            case 1 : dayOfWeekStr = getString(R.string.Sunday);
                break;
            case 2 : dayOfWeekStr = getString(R.string.Monday);
                break;
            case 3 : dayOfWeekStr = getString(R.string.Tuesday);
                break;
            case 4 : dayOfWeekStr = getString(R.string.Wednesday);
                break;
            case 5 : dayOfWeekStr = getString(R.string.Thursday);
                break;
            case 6 : dayOfWeekStr = getString(R.string.Friday);
                break;
            case 7 : dayOfWeekStr = getString(R.string.Saturday);
                break;
        }
        String calendar = ShamsiCalendar.getCurrentShamsidate();
        date.setText(calendar);
        weekDay.setText(dayOfWeekStr);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView_location_notesFragment: {
                getCityName();
                break;
            }
            case R.id.imageView_searchIcon_notesFragment: {
                readdata(searchInput.getText().toString());
                break;
            }
            case R.id.button_sortItems_notesFragment: {
                sortData();
                break;
            }
            case R.id.fab_addNote_save: {
                startActivity(new Intent(MainActivity.this, AddReadNote.class));
                break;
            }
            case R.id.drawerAddNote_btn : {
                startActivity(new Intent(MainActivity.this, AddReadNote.class));
                break;
            }
            case R.id.green_btn : {
                saveColorVal(spref.Theme.GREEN_COLOR);
                break;
            }
            case R.id.teal_btn : {
                saveColorVal(spref.Theme.TEAL_COLOR);
                break;
            }
            case R.id.blue_btn : {
                saveColorVal(spref.Theme.BLUE_COLOR);
                break;
            }
            case R.id.orange_btn : {
                saveColorVal(spref.Theme.ORANGE_COLOR);
                break;
            }
            case R.id.red_btn : {
                saveColorVal(spref.Theme.RED_COLOR);
                break;
            }
            case R.id.purple_btn : {
                saveColorVal(spref.Theme.PURPLE_COLOR);
                break;
            }
            case R.id.info_btn : {
                showInfo();
                break;
            }
        }
    }

    private void showInfo() {

        MyDailyDialog dialog = new MyDailyDialog(
                this,
                getString(R.string.mail_feedback),
                getString(R.string.call),
                getString(R.string.about_daily),
                "v1.0" + "\n" + "Hosein sadon asl" + "\n" +
                        getString(R.string.connect_dev) + "\n" +
                        "E-mail: hoseinsadonasl@gmail.com" + "\n" +
                        "Tel: +989168398153",
                1,
                0,
                new DialogInterface() {
                    @Override
                    public void onPositiveClick() {
                        sendFeedbackMail();
                    }

                    @Override
                    public void onNegativeClick() {
                        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+989168398153")));
                    }
                },
                1);
        dialog.setLogoImg(R.drawable.roozane_pic);
        dialog.show();

    }

    private void sendFeedbackMail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:hoseinsadonasl@gmail.com"));
        startActivity(Intent.createChooser(intent, getString(R.string.send_mail)));
    }

    private void saveColorVal(String colorName) {
        spref.get(spref.THEME).edit().putString(spref.Theme.THEME_COLOR, colorName).apply();
        recreate();
    }

    @Override
    protected void onResume() {
            list.clear();
        if (searchInput.getText().toString() != null) {
            list.addAll(readdata(searchInput.getText().toString()));
        } else {
            list.addAll(readdata(""));
        }adapter.notifyDataSetChanged();
        illustration.setVisibility(list.size() >= 1 ? View.GONE : View.VISIBLE);
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (!backPressed){
            backPressed = true;
            searchInput.clearFocus();
            app.t(getString(R.string.toast_exit_app));
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
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(db.Note.NOTE_ID));
            @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(db.Note.NOTE_TITLE));
            @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex(db.Note.NOTE_CONTENT));
            @SuppressLint("Range") String reminderDate = cursor.getString(cursor.getColumnIndex(db.Note.REMINDER_DATE));
            @SuppressLint("Range") String reminderTime = cursor.getString(cursor.getColumnIndex(db.Note.REMINDER_TIME));
            @SuppressLint("Range") String date  = cursor.getString(cursor.getColumnIndex(db.Note.NOTE_DATE));
            @SuppressLint("Range") String lastModify = cursor.getString(cursor.getColumnIndex(db.Note.NOTE_LAST_MODIFY));

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
            sort.setText(spref.SortButtonText.SORT_BTN_TXT_DATE);
            sort.setIconResource(R.drawable.ic_datedescldpi);
            iconId=getResources().getIdentifier(getResources().getResourceEntryName(R.drawable.ic_datedescldpi),"drawable","com.abc.daily");
        } else if (orderState.equals(spref.SortState.SORT_BY_DATE) && orderType.equals(spref.SortType.DESC)) {
            orderState = spref.SortState.SORT_BY_NAME;
            orderType = spref.SortType.ASC;
            sort.setText(spref.SortButtonText.SORT_BTN_TXT_NAME);
            sort.setIconResource(R.drawable.ic_titleascldpi);
            iconId=getResources().getIdentifier(getResources().getResourceEntryName(R.drawable.ic_titleascldpi),"drawable","com.abc.daily");
        } else if (orderState.equals(spref.SortState.SORT_BY_NAME) && orderType.equals(spref.SortType.ASC)) {
            orderState = spref.SortState.SORT_BY_NAME;
            orderType = spref.SortType.DESC;
            sort.setText(spref.SortButtonText.SORT_BTN_TXT_NAME);
            sort.setIconResource(R.drawable.ic_titledescldpi);
            iconId=getResources().getIdentifier(getResources().getResourceEntryName(R.drawable.ic_titledescldpi),"drawable","com.abc.daily");
        } else if (orderState.equals(spref.SortState.SORT_BY_NAME) && orderType.equals(spref.SortType.DESC)) {
            orderState = spref.SortState.SORT_BY_DATE;
            orderType = spref.SortType.ASC;
            sort.setText(spref.SortButtonText.SORT_BTN_TXT_DATE);
            sort.setIconResource(R.drawable.ic_dateascldpi);
            iconId=getResources().getIdentifier(getResources().getResourceEntryName(R.drawable.ic_dateascldpi),"drawable","com.abc.daily");
        }
        spref.get(spref.SORT).edit().putString(spref.SORT, orderState).apply();
        spref.get(spref.SORT).edit().putString(spref.SORT_TYPE, orderType).apply();
        spref.get(spref.SORT).edit().putInt(spref.SORT_ICON_ID, iconId).apply();
        list.clear();
        list.addAll(readdata(""));
        adapter.notifyDataSetChanged();
    }

    private void getWeather() {
        if (cityNameString.isEmpty() || locationName.getText().toString().equals(getString(R.string.invalid_location)))  {
            getCityName();
        }
        Map<String ,String> data = new HashMap();
            data.put("appid","6c7b0789e344c8bdd8f0935ff4568e72");
            data.put("q", cityNameString);
            data.put("lang", getString(R.string.weaLang));

        Application.getApi().getCurrentWeather(data).enqueue(new Callback<WeatherModels>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<WeatherModels> call, Response<WeatherModels> response) {
                if (response.code() == 200) {
                    tempDgree.setVisibility(View.VISIBLE);
                    temp.setVisibility(View.VISIBLE);
                    weatherImage.setVisibility(View.VISIBLE);
                    spinKitView.setVisibility(View.GONE);
                    temp.setText(String.valueOf((int) (response.body().getMain().getTemp() - 273.15)));
                    locationName.setText(response.body().getName() + " - " +  response.body().getWeather().get(0).getMain());
                    String imgUrl = String.valueOf(response.body().getWeather().get(0).getIcon());
                    Glide.with(weatherImage)
                            .load("https://openweathermap.org/img/wn/"+ imgUrl +"@2x.png")
                            .into(weatherImage);
                } else{
                    locationName.setText(getString(R.string.invalid_location));
                    spinKitView.setVisibility(View.VISIBLE);
                    tempDgree.setVisibility(View.GONE);
                    temp.setVisibility(View.GONE);
                    weatherImage.setVisibility(View.GONE);
                    app.t(getString(R.string.invalid_name_toast));
                }
            }

            @Override
            public void onFailure(Call<WeatherModels> call, Throwable t) {
            }
        });
    }


    private void getCityName() {
        dialog = new MyDailyDialog(
                this,
                getString(R.string.dialog_ok),
                getString(R.string.cancel),
                getString(R.string.city_name),
                getString(R.string.enter_city_name),
                1,
                1,
                this,
                0);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onPositiveClick() {
        if (dialog.getInputText().isEmpty()) {
            app.t(getString(R.string.please_enter_city_name));
            return;
        }
        locationName.setText(dialog.getInputText());
        spref.get(spref.WEATHER).edit().putString(spref.Weather.CITY_NAME, locationName.getText().toString()).apply();
        cityNameString = spref.get(spref.WEATHER).getString(spref.Weather.CITY_NAME, spref.Weather.DEFAULT_CITY_NAME);
        getWeather();
        dialog.dismiss();
    }

    @Override
    public void onNegativeClick() {
        dialog.dismiss();
    }

    @SuppressLint("NonConstantResourceId")
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