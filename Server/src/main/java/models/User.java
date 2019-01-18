package models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"userName"})
})
public class User implements Serializable {

    public User() {

    }

    public User(String userName, String userPass) {
        this.userPass = userPass;
        this.userName = userName;
    }

    @Override
    public String toString() {
        return userName + " " + userPass;
    }

    public int getUid() {
        return uid;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public void addUserContact(UserContact userContact) {
        userContact.setUser(this);
        this.userContactList.add(userContact);
    }


    public List<UserContact> getUserContactList() {
        return userContactList;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setUserContactList(List<UserContact> userContactList) {
        this.userContactList = userContactList;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int uid;

    @Column
    private String userName;

    @Column
    private String userPass;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
    private List<UserContact> userContactList = new ArrayList<>();


}
