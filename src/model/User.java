package model;
public class User {
    private int idUser;
    private String namaUser, email, nomorHp, password, role;
    public User(){}
    public User(int id,String nama,String email,String hp,String pass,String role){
        this.idUser=id;this.namaUser=nama;this.email=email;this.nomorHp=hp;this.password=pass;this.role=role;
    }
    public int getIdUser(){return idUser;}
    public String getNamaUser(){return namaUser;}
    public String getEmail(){return email;}
    public String getNomorHp(){return nomorHp;}
    public String getPassword(){return password;}
    public String getRole(){return role;}
    public void setIdUser(int v){idUser=v;}
    public void setNamaUser(String v){namaUser=v;}
    public void setEmail(String v){email=v;}
    public void setNomorHp(String v){nomorHp=v;}
    public void setPassword(String v){password=v;}
    public void setRole(String v){role=v;}
}
