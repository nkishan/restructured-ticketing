public class Passenger {
    private String name;
    private String gender;//can use enums
    private int age;
    private String preference;

    public Passenger(String pass_name, String pass_gender, int pass_age) {
        name = pass_name;
        age = pass_age;
        gender = pass_gender;
    }

    public Passenger(String passName, String passGender, int passAge, String passPreference) {
        name = passName;
        age = passAge;
        gender = passGender;
        preference = passPreference;
    }

    public String getPreference() {
        return preference;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

    public void printDetails() {
        System.out.println("Name: " + name);
//        System.out.println("Gender: " + gender);
//        System.out.println("Age: " + age);
    }

}
