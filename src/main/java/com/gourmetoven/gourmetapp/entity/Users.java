package com.gourmetoven.gourmetapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@SequenceGenerator(name = "USER_SEQ",sequenceName = "user_seq",initialValue = 150)
public class Users {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "USER_SEQ")
    private int id;

    @Column(name = "name_of_user")
    private String name;

    @Column(name = "user_name")
    private String username;


    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Recipe> recipe;

    public Users() {
    }

    public Users(int id, String username, String name) {
        this.id = id;
        this.username = username;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Recipe> getRecipe() {
        return recipe;
    }

    public void setRecipe(List<Recipe> recipe) {
        this.recipe = recipe;
    }


    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", recipe=" + recipe +
                '}';
    }
}
