package com.example.specialdaydatabase;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
/*
purpose: facilitate the operation of school. Find Period name, Time, date from a schedule in form of Text.
upload these information into database for my another program: mySchedule.
Tian Yu  Version: 1.0
 */
public class ScrollingActivity extends AppCompatActivity {
    //GUI Put every element shown in the screen into an ArrayList
    ArrayList<String> Text = new ArrayList<>();
    ArrayList<TextView> name=new ArrayList<>();
    ArrayList<TextView> StartT=new ArrayList<>();
    ArrayList<TextView> EndT=new ArrayList<>();

    ArrayList<String> startTime= new ArrayList<>();
    ArrayList<String> endTime=new ArrayList<>();
    ArrayList<String> period= new ArrayList<>();
    ArrayList<Block> blocks=new ArrayList<>();
    //Store the Key for database.
    String Id="";
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    //read Data from the database
    public void readData(View view) {
        TextView info = findViewById(R.id.info);
        TextView monthDisplay = findViewById(R.id.Month);
        TextView DayofWeek = findViewById(R.id.DoW);
        TextView dayDisplay = findViewById(R.id.day);
        //The Program might run more than once. The data of first run will be upload again and again without following clear.
        startTime.clear();
        endTime.clear();
        period.clear();
        blocks.clear();
        String temp = info.getText().toString();
        //avoid crash when there is no information entered.
        if (!temp.equals("")) {
            LoadData(view);
            removeLine();
            clear(view);
            int DoW = findDayOWeek();
            DayofWeek.setText(DoW+"");
            int month = findMonth();
            monthDisplay.setText(month+"");
            int day = findDay();
            dayDisplay.setText(day+"");
            for(int i=1;i<Text.size();i++) {
                startTime.add(findstartTime(i));
                endTime.add(findEndTime(i));
                period.add(findPeriod(i));
            }
            display(view);
        }
    }
    //Load data into Text ArrayList. Each String in the ArrayList represent a line.
    public void LoadData(View view) {
        TextView info = findViewById(R.id.info);
        Scanner scanner;
        Text.clear();
        scanner = new Scanner(info.getText().toString());
        //Reading each line of file using Scanner class
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            line = line.replace('\t', ' ');
            Text.add(line);
            Log.i("info1", Text.size() + "");
        }
    }


    /*remove any empty line in the "info" TextView. When a line is removed from the array list, every remaining object will move forward
    one index. If the loop continue, it will skip to two index after the removed line. The object one index after the removed line is never
    examined. To avoid this error, a variable shift is introduced.
    */
    public void removeLine() {
        int shift = 0;
        for (int i = 0; i < Text.size() + shift; i++) {
            if (Text.get(i - shift).length() < 1/* || Text.get(i).equals("\n")*/) {
                Text.remove(i - shift);
                shift++;
            }
        }
    }

    //display in each TextView. Try and catch avoid crash when there is less then 7 lines information entered initially.
    public void display(View view) {
        TextView info = findViewById(R.id.info);
        try {
            for (int i = 0; i < 7; i++) {
                info.append(Text.get(i) + "\n");
                StartT.get(i).setText(startTime.get(i));
                EndT.get(i).setText(endTime.get(i));
                name.get(i).setText(period.get(i));
            }
        }
        catch (Exception e){
            }
    }
    //Clear everything presented in the screen.
    public void clear(View view) {
        TextView info = findViewById(R.id.info);
        info.setText("");
        TextView monthDisplay = findViewById(R.id.Month);
        TextView DayofWeek = findViewById(R.id.DoW);
        TextView dayDisplay = findViewById(R.id.day);
        monthDisplay.setText("");
        DayofWeek.setText("");
        dayDisplay.setText("");
        for(int i=0;i<7;i++){
            name.get(i).setText("");
            StartT.get(i).setText("");
            EndT.get(i).setText("");
        }
    }
    //uses String class method "contains" to find the word that indicates day of week from the first line.
    //this method return an integer. 1 represents monday, 2 represents Tuesday and so on.
    //
    public int findDayOWeek() {

        int dayOWeek = -1;
        String sub = Text.get(0);
        sub = sub.toLowerCase();
        if (sub.contains("mon")) {
            dayOWeek = 1;
        } else if (sub.contains("tues")) {
            dayOWeek = 2;
        } else if (sub.contains("wed")) {
            dayOWeek = 3;
        } else if (sub.contains("thu")) {
            dayOWeek = 4;
        } else if (sub.contains("fri")) {
            dayOWeek = 5;
        } else if (sub.contains("sat")) {
            dayOWeek = 6;
        } else if (sub.contains("sun")) {
            dayOWeek = 7;
        }
        return dayOWeek;
    }

    /*
    uses String class method "contains" to find the word that indicates month from the first line
    this method return an integer. 1 represents January, 2 represents  February and so on.
    The first line is converted to lower case by default, so it is not case sensitive.
     */
    public int findMonth() {
        int month = -1;
        String sub = Text.get(0);
        sub = sub.toLowerCase();
        if (sub.contains("january"))
            month = 1;
        else if (sub.contains("february"))
            month = 2;
        else if (sub.contains("march"))
            month = 3;
        else if (sub.contains("april"))
            month = 4;
        else if (sub.contains("may"))
            month = 5;
        else if (sub.contains("june"))
            month = 6;
        else if (sub.contains("july"))
            month = 7;
        else if (sub.contains("august"))
            month = 8;
        else if (sub.contains("september"))
            month = 9;
        else if (sub.contains("october"))
            month = 10;
        else if (sub.contains("november"))
            month = 11;
        else if (sub.contains("december"))
            month = 12;
        return month;
    }
    //return the first number in the line.
    public int findDay() {
        String temp = Text.get(0);
        char[] a = temp.toCharArray();
        boolean isNumber = false;
        ArrayList<Integer> numbers = new ArrayList<>();
        int i = 0;
        for (char t : a) {
            try {
                if (!isNumber) {
                    i = Integer.parseInt(t + "");
                    isNumber = true;
                } else {
                    i = Integer.parseInt("" + i + t);
                }
            } catch (Exception e) {
                if (i != 0) {
                    numbers.add(i);
                    i = 0;
                }
                isNumber = false;
            }
        }
        if (i != 0) numbers.add(i);
        try {
            return numbers.get(0);
        }
        catch(Exception e){
            return -1;
        }

    }
    /*
    return a String. Find index of "-" cut the String from 0 to index of "-"
     */
    public String findstartTime(int i) {
        try {
            String text = Text.get(i);
            int temp = text.indexOf("-");
            return text.substring(0, temp);
        } catch (Exception e) {
        }
        return "";
    }
    /*
     Find index of "-" find the index of Tab. return a String from index of "-" to index of Tab.
     */
    public String findEndTime(int i) {
        try {
            String text = Text.get(i);
            int temp = text.indexOf("-");
            int tab = text.indexOf(" ");
            String endTime = text.substring(temp + 1, tab);
            return endTime;
        } catch (Exception e) {
        }
        return "";
    }
//
    public String findPeriod(int i) {
        String temp = Text.get(i);
        String[] tempList = temp.split(" ");
        String result = "";
        for(int t = 1; t < tempList.length; t++){
            if(!tempList[t].equals("")) result += tempList[t] + " ";
        }
        return result;
    }
    /*
    collect information from the Textview and use these information to create object "Block". Each Block is added into blocks TextView.
     */
    public void storeChange(View view) {
        try {
            TextView monthDisplay = findViewById(R.id.Month);
            TextView DayofWeek = findViewById(R.id.DoW);
            TextView dayDisplay = findViewById(R.id.day);
            String day=dayDisplay.getText().toString();
            String DoW=DayofWeek.getText().toString();
            String month=monthDisplay.getText().toString();
            Id=generateID(month,DoW,day);
            for (int i = 0; i < Text.size() - 1; i++) {
                blocks.add(new Block(StartT.get(i).getText().toString(),
                        EndT.get(i).getText().toString(),
                        name.get(i).getText().toString()));

            }

        }
        catch (Exception e) {

        }
    }
    /*
    Generate an key for the database. convert numbers into corresponding English word.
    For example, 1 in the month TextView will be converted to January. 1 in Day of the week TextVew will be converted to Monday.
    the users might enter non-number into Day, Day of the week, and month Textview. Try and catch were set up to avoid crash.
     */
    public String generateID(String month, String DoW, String day){
        Calendar now = Calendar.getInstance();
        int monthI=-1;
        int DoWI=-1;
        try{
            monthI=Integer.parseInt(month);
            DoWI=Integer.parseInt(DoW);
        }
        catch(Exception e){
        }
        switch(monthI){
            case 1:
                month="January";
                break;
            case 2:
                month="February";
                break;
            case 3:
                month="March";
                break;
            case 4:
                month="April";
                break;
            case 5:
                month="May";
                break;
            case 6:
                month="June";
                break;
            case 7:
                month="July";
                break;
            case 8:
                month="August";
                break;
            case 9:
                month="September";
                break;
            case 10:
                month="October";
                break;
            case 11:
                month="November";
                break;
            case 12:
                month="December";
                break;
        }
        switch(DoWI){
            case 1:
                DoW="Monday";
                break;
            case 2:
                DoW="Tuesday";
                break;
            case 3:
                DoW="Wednesday";
                break;
            case 4:
                DoW="Thursday";
                break;
            case 5:
                DoW="Friday";
                break;
            case 6:
                DoW="Saturday";
                break;
            case 7:
                DoW="Sunday";
                break;
        }
        int year = now.get(Calendar.YEAR);
        return year+", "+month+" "+day+", "+DoW;
    }

    public void commitDatabase(View view) {
        storeChange(view);
        removeIllegalSym();
        for (int i = 0; i < blocks.size(); i++) {
            mDatabase.child("SpecialDays").child(Id).child(blocks.get(i).periodName).setValue(blocks.get(i));
        }
    }
    //firebase doesn't allow following symbol as a key. Remove them to prevent error. Each illegal symbol will be replaced by a space.
    public void removeIllegalSym(){
        for(int i=0; i<blocks.size();i++){
            String pro=blocks.get(i).periodName;
            pro=pro.replace("."," ");
            pro=pro.replace("/"," ");
            pro=pro.replace("#"," ");
            pro=pro.replace("["," ");
            pro=pro.replace("]"," ");
            pro=pro.replace("$"," ");
            blocks.get(i).periodName=pro;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        //Add each TextView into an Array List. Edit them through for loop.
        name.add((TextView) findViewById(R.id.p1));
        name.add((TextView) findViewById(R.id.p2));
        name.add((TextView) findViewById(R.id.p3));
        name.add((TextView) findViewById(R.id.p4));
        name.add((TextView) findViewById(R.id.p5));
        name.add((TextView) findViewById(R.id.p6));
        name.add((TextView) findViewById(R.id.p7));
        StartT.add((TextView) findViewById(R.id.s1));
        StartT.add((TextView) findViewById(R.id.s2));
        StartT.add((TextView) findViewById(R.id.s3));
        StartT.add((TextView) findViewById(R.id.s4));
        StartT.add((TextView) findViewById(R.id.s5));
        StartT.add((TextView) findViewById(R.id.s6));
        StartT.add((TextView) findViewById(R.id.s7));
        EndT.add((TextView) findViewById(R.id.e1));
        EndT.add((TextView) findViewById(R.id.e2));
        EndT.add((TextView) findViewById(R.id.e3));
        EndT.add((TextView) findViewById(R.id.e4));
        EndT.add((TextView) findViewById(R.id.e5));
        EndT.add((TextView) findViewById(R.id.e6));
        EndT.add((TextView) findViewById(R.id.e7));
    }
}