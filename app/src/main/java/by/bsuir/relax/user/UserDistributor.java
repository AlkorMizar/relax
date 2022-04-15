package by.bsuir.relax.user;

public class UserDistributor {
    static User user;

    public static User getUser() {
        return user;
    }

    public static void login(User user) {
       if (UserDistributor.user!=null) {
           for (MoodListener ml : UserDistributor.user.getMListeners()) {
               user.addListener(ml);
           }
           for (HeightListner ml : UserDistributor.user.getHListeners()) {
               user.addListener(ml);
           }
       }
        UserDistributor.user = user;
    }

    public static void logout(){
        user=null;
    }
}
