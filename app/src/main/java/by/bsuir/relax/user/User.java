package by.bsuir.relax.user;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.util.Pair;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User implements Cloneable{
    String email;
    String password;

    int id;
    String name;
    List<UsersMood> moods;
    float weight;
    float height;
    LocalDate birthday;
    int age;
    Pair<Integer,Integer> pressure;
    Bitmap avatar;
    String number;
    Mood currentMood;


    public User() {
        moods=new ArrayList<>();
        email="";
        name="";
        currentMood=Mood.Calm;
        number="";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws Exception {

        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if(!matcher.matches()) {
            throw new Exception("Error in email");
        }
        this.email = email;
        
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws Exception {
        if(password.length()<6){
            throw new Exception("Error in password less then 6");
        }
        this.password = password;
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws Exception{
        if (name.trim().length()==0) {
            throw new Exception("Error in name");
        }
        this.name = name;

    }

    public List<UsersMood> getMoods() {
        return moods;
    }

    public void setMoods(List<UsersMood> moods) {
        this.moods = moods;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(String weightStr) throws Exception {
        try{
            float weight=Float.parseFloat(weightStr);
            if(weight<0){
                throw new Exception("Error in weight");
            }
            this.weight = weight;

        }catch (NumberFormatException e){
            throw new Exception("Error in weight");
        }
        
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setBirthday(String birthdayStr) throws Exception {
        LocalDate b;

        try {
            b = LocalDate.parse(birthdayStr); // use for age-calculation: LocalDate.now()
        }catch (Exception e){
            throw new Exception("Error in date");
        }

        int years = (int)ChronoUnit.YEARS.between(b,LocalDate.now());
        if(years<0){
            throw new Exception("Error in date");
        }
        
        birthday=b;
        age=years;

    }

    public int getAge() {
        return age;
    }


    public Pair<Integer, Integer> getPressure() {
        return pressure;
    }

    public void setPressure(String pressureStr) throws Exception{

        if (!pressureStr.matches("\\d+/\\d+")){
            throw new Exception("Error in blood pressure");
        }

        String[] pressurs=pressureStr.split("/");

        try {
            Pair<Integer,Integer> pressure=new Pair<>(Integer.parseInt(pressurs[0]),Integer.parseInt(pressurs[1]));

            if(pressure.first<0 || pressure.second<0){
                throw new Exception("Error in blood pressure");
            }
            this.pressure = pressure;
        }catch (NumberFormatException e){
            throw new Exception("Error in blood pressure");
        }

    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public byte[] getAvatarBytes(){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        avatar.compress(Bitmap.CompressFormat.PNG, 100, stream);
        System.out.println(Arrays.toString(stream.toByteArray()));
        return stream.toByteArray();
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;

        for (ImgListner ml : iListners)
            ml.onChange();
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) throws Exception{
        if(number==null || number.trim().length()==0){

            return;
        }
        String patterns
                = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$";
        Pattern pattern = Pattern.compile(patterns);
        Matcher matcher = pattern.matcher(number);
        if(!matcher.matches()){
            throw new Exception("Error in number");
        }
        this.number = number;
    }

    public Mood getCurrentMood() {
        return currentMood;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setCurrentMood(Mood currentMood) {
        this.currentMood = currentMood;
        moods.add(new UsersMood( LocalDateTime.now(),currentMood));

        // Notify everybody that may be interested.
        for (MoodListener ml : listeners)
            ml.moodChanged(currentMood);
    }

    private List<MoodListener> listeners = new ArrayList<MoodListener>();
    private List<HeightListner> listners=new ArrayList<>();
    private List<ImgListner> iListners=new ArrayList<>();

    public List<MoodListener> getMListeners() {
        return listeners;
    }

    public List<HeightListner> getHListeners() {
        return listners;
    }

    public void addListener(MoodListener toAdd) {
        listeners.add(toAdd);
    }
    public void addListener(HeightListner toAdd) {
        listners.add(toAdd);
    }
    public void addListener(ImgListner toAdd) {
        iListners.add(toAdd);
    }


    public float getHeight() {
        return height;
    }

    public void setHeight(String height) throws Exception {
        try {
            float h=Float.parseFloat(height);
            if(h<0){
                throw new Exception("Error in height");
            }

            this.height = h;
            System.out.println("heighttttttttttttt");
            // Notify everybody that may be interested.
            for (HeightListner ml : listners)
                ml.heightChanged();

        }catch (NumberFormatException e){
            throw new Exception("Error in height");
        }
    }

    @Override
    public User clone() {
        try {
            User clone = (User) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public String getBMI() {
        float bmi=weight/(height*height);
        return bmi+"";
    }
}


