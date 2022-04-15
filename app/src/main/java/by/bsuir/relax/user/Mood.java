package by.bsuir.relax.user;

public enum Mood {
    Happy(24),
    Relaxed(21),
    Calm(18),
    Concentrated(12),
    Agitated(6),
    Sad(3),
    Angry(0);

    public final int weight;

    Mood(int w){
        weight=w;
    }
}
