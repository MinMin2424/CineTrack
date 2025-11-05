/*
 * Created by minmin_tranova on 05.11.2025
 */

package cz.cvut.fel.cinetrack.dto.user.response;

import java.time.LocalDate;

public class UserProfileHeaderResponse {

    private String username;
    private String firstname;
    private String lastname;
    private LocalDate creationDate;
    private String avatar;

    public UserProfileHeaderResponse(String username,
                                     String firstname,
                                     String lastname,
                                     LocalDate creationDate,
                                     String avatar) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.creationDate = creationDate;
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
