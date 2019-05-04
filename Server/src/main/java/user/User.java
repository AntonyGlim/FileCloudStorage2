package user;

public class User {
    private int name;
    private int password;
    private long registration_date;
    private long time_last_visit;

    public User(int name, int password, long registration_date, long time_last_visit) {
        this.name = name;
        this.password = password;
        this.registration_date = registration_date;
        this.time_last_visit = time_last_visit;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public long getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(long registration_date) {
        this.registration_date = registration_date;
    }

    public long getTime_last_visit() {
        return time_last_visit;
    }

    public void setTime_last_visit(long time_last_visit) {
        this.time_last_visit = time_last_visit;
    }

    @Override
    public String toString() {
        return name + " " + registration_date + " " + time_last_visit;
    }
}
