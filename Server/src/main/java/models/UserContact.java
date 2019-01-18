package models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"uid", "cid"})
)
public class UserContact implements Serializable {

    public UserContact() {

    }

    public UserContact(User user, Contact contact) {
        this.user = user;
        this.contact = contact;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ucid;

    public int getUcid() {
        return ucid;
    }

    public User getUser() {
        return user;
    }

    public Contact getContact() {
        return contact;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public void setUcid(int ucid) {
        this.ucid = ucid;
    }

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "uid")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "cid")
    private Contact contact;


}
