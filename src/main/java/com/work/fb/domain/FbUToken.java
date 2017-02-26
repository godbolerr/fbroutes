package com.work.fb.domain;


import java.io.Serializable;
import java.util.Objects;

/**
 * A FbUToken.
 */

public class FbUToken implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long id;


    private String userid;


    private String uToken;

    
    private String createdOn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public FbUToken userid(String userid) {
        this.userid = userid;
        return this;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getuToken() {
        return uToken;
    }

    public FbUToken uToken(String uToken) {
        this.uToken = uToken;
        return this;
    }

    public void setuToken(String uToken) {
        this.uToken = uToken;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public FbUToken createdOn(String createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FbUToken fbUToken = (FbUToken) o;
        if (fbUToken.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, fbUToken.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FbUToken{" +
            "id=" + id +
            ", userid='" + userid + "'" +
            ", uToken='" + uToken + "'" +
            ", createdOn='" + createdOn + "'" +
            '}';
    }
}
