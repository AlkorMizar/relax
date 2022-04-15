package by.bsuir.relax.dao;

import android.graphics.Bitmap;
import android.util.Pair;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import by.bsuir.relax.user.Mood;
import by.bsuir.relax.user.User;
import by.bsuir.relax.user.UsersMood;

public interface DAO {
    void register(User user);
    User getIfRegistered(String email,String password);
    List<Photo> usersPhotos(User user);
    void addUsersPhotos(User user, Bitmap bmt, String date);
    void removeUserPhoto(Photo photo);
    void updateUser(User user);
    List<UsersMood> getUsersMoods(User user);
    void addMood(User user, Mood mood, LocalDateTime date);
}
