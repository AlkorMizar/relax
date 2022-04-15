package by.bsuir.relax.user;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import by.bsuir.relax.user.Mood;

public class UsersMood {
    private LocalDateTime date;
    private Mood mood;

    public UsersMood(LocalDateTime date, Mood mood) {
        this.date = date;
        this.mood = mood;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Mood getMood() {
        return mood;
    }
}
