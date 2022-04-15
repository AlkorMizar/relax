package by.bsuir.relax.dao;

import static android.content.Context.MODE_PRIVATE;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import by.bsuir.relax.RelaxApplication;
import by.bsuir.relax.user.Mood;
import by.bsuir.relax.user.User;
import by.bsuir.relax.user.UsersMood;

public class DB implements DAO{

    private SQLiteDatabase db;

    public DB(){
        try {
            db = RelaxApplication.getAppContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);

            db.execSQL("CREATE TABLE IF NOT EXISTS users ("+"" +
                    "email TEXT NOT NULL UNIQUE,"+
                    "id INTEGER PRIMARY KEY AUTOINCREMENT ,"+
                    "password TEXT NOT NULL," +
                    "name TEXT NOT NULL," +
                    "avatar BLOB NOT NULL," +
                    "birthday TEXT NOT NULL," +
                    "age INTEGER NOT NULL,"+
                    "number TEXT,"+
                    "height REAL NOT NULL,"+
                    "weight REAL NOT NULL,"+
                    "pressure TEXT NOT NULL)");

            db.execSQL("CREATE TABLE IF NOT EXISTS MOODS ("+"" +
                    "user_id TEXT NOT NULL,"+
                    "mood TEXT NOT NULL,"+
                    "date TEXT NOT NULL,"   +
                    "PRIMARY KEY (user_id, date),"+
                    "FOREIGN KEY (user_id)" +
                    "REFERENCES users (id) " +
                    "ON DELETE NO ACTION " +
                    "ON UPDATE NO ACTION)");


            db.execSQL("CREATE TABLE IF NOT EXISTS photos ("+"" +
                    "user_id TEXT NOT NULL,"+
                    "photo BLOB NOT NULL,"+
                    "date TEXT NOT NULL," +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT,"   +
                    "FOREIGN KEY (user_id)" +
                    "REFERENCES users (id) " +
                    "ON DELETE NO ACTION " +
                    "ON UPDATE NO ACTION)");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        db.close();
        super.finalize();
    }
    @Override
    public void register(User user) {
        db.execSQL(
                "INSERT INTO users (email,password,name,avatar,birthday,age,number,weight,height,pressure) VALUES (?,?,?,?,?,?,?,?,?,?)",
                new Object[]{
                            user.getEmail(),
                            user.getPassword(),
                            user.getName(),
                            user.getAvatarBytes(),
                            user.getBirthday().toString(),
                            user.getAge(),
                            user.getNumber(),
                            user.getWeight(),
                            user.getHeight(),
                            user.getPressure().first+"/"+user.getPressure().second}
        );
    }

    @Override
    public User getIfRegistered(String email, String password) {
        Cursor query = db.rawQuery("SELECT id,email,password,name,avatar,birthday,number,weight,pressure,height " +
                        "FROM users WHERE email = ? AND password = ?",
                new String[] {email, password});
        if (query.moveToNext()){
            try {
                User user = new User();
                user.setId(query.getInt(0));
                user.setEmail(email);
                user.setPassword(password);
                user.setName(query.getString(3));
                Bitmap bmp = BitmapFactory.decodeByteArray(query.getBlob(4), 0, query.getBlob(4).length);
                user.setAvatar(bmp);
                user.setBirthday(query.getString(5));
                user.setNumber(query.getString(6));
                user.setWeight(query.getFloat(7)+"");
                user.setPressure(query.getString(8));

                user.setHeight(query.getString(9));

                Cursor queryMood = db.rawQuery("SELECT MOODS.mood,MOODS.date FROM MOODS " +
                        "WHERE MOODS.user_id=?;",
                        new String[]{user.getId()+""});
                ArrayList<UsersMood> moods=new ArrayList<>();
                while(queryMood.moveToNext()){
                    moods.add(new UsersMood(
                            LocalDateTime.parse(queryMood.getString(1)),
                            Mood.valueOf(queryMood.getString(0))
                    ));
                    System.out.println(queryMood.getString(1));
                }
                user.setMoods(moods);
                return  user;
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    @Override
    public List<Photo> usersPhotos(User user) {
        Cursor query = db.rawQuery("SELECT photos.photo,photos.date,photos.id FROM photos " +
                "INNER JOIN users ON users.id=photos.user_id ;", null);
        ArrayList<Photo> photos=new ArrayList<>(query.getCount());
        while(query.moveToNext()){
            Bitmap bmp = BitmapFactory.decodeByteArray(query.getBlob(0), 0, query.getBlob(0).length);
            System.out.println(bmp +"  rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
            photos.add(new Photo(bmp,query.getInt(2),query.getString(1)));
        }
        return photos;
    }

    @Override
    public void addUsersPhotos(User user, Bitmap bmt, String date) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmt.compress(Bitmap.CompressFormat.PNG, 100, stream);
        System.out.println(stream.toByteArray() +"  yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
        db.execSQL(
                "INSERT INTO photos (photo,date,user_id) VALUES (?,?,?)",
                new Object[]{
                        stream.toByteArray(),
                        date,
                        user.getId()}
        );
    }

    @Override
    public void removeUserPhoto(Photo photo) {
        db.execSQL(
                "DELETE FROM photos WHERE id = ?;",
                new Object[]{photo.getId()}
        );
    }

    @Override
    public void updateUser(User user) {
        db.execSQL(
                "UPDATE users SET  name=?,avatar=?,birthday=?,age=?,number=?,weight=?,pressure=? WHERE id = ?",
                new Object[]{
                        user.getName(),
                        user.getAvatarBytes(),
                        user.getBirthday().toString(),
                        user.getAge(),
                        user.getNumber(),
                        user.getWeight(),
                        user.getPressure().first+"/"+user.getPressure().second,
                        user.getId()}
        );
    }

    @Override
    public List<UsersMood> getUsersMoods(User user) {
        Cursor queryMood = db.rawQuery("SELECT MOODS.mood,MOODS.date FROM MOODS " +
                        "WHERE MOODS.user_id=?;",
                new String[]{user.getId()+""});
        ArrayList<UsersMood> moods=new ArrayList<>();
        while(queryMood.moveToNext()){
            moods.add(new UsersMood(
                    LocalDateTime.parse(queryMood.getString(1)),
                    Mood.valueOf(queryMood.getString(0))
            ));
            System.out.println(queryMood.getString(1));
        }
        user.setMoods(moods);
        return  moods;
    }

    @Override
    public void addMood(User user, Mood mood, LocalDateTime date) {
        db.execSQL(
                "INSERT INTO MOODS (user_id,mood,date) VALUES (?,?,?)",
                new Object[]{
                        user.getId(),
                        mood.toString(),
                        date.toString()}
        );
    }
}
