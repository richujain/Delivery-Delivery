package Model;

public class User {
    private String email, password, name, phone,vehicletype,licensenumber;


    public User()
    {

    }

    public User(String email, String password, String name, String phone,String vehicletype, String licensenumber)
    {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.vehicletype = vehicletype;
        this.licensenumber = licensenumber;
    }

    public String getVehicletype() {
        return vehicletype;
    }

    public void setVehicletype(String vehicletype) {
        this.vehicletype = vehicletype;
    }

    public String getLicensenumber() {
        return licensenumber;
    }

    public void setLicensenumber(String licensenumber) {
        this.licensenumber = licensenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
