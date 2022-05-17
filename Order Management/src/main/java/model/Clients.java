package model;

public class Clients {
    private int id;
    private String name;
    private String address;
    private String email;

    public Clients(){

    }

    public Clients(int id, String name, String address, String email){
        super();
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
    }

    public Clients(String name,String address, String email){
        super();
        this.name = name;
        this.address = address;
        this.email = email;
    }



    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Client id=" + id + ", name=" + name + ", address=" + address + ", email=" + email ;
    }


}
