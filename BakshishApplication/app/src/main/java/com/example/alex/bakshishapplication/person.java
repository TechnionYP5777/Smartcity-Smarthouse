package com.example.alex.bakshishapplication;

import android.support.annotation.NonNull;

import java.util.Set;

/**
 * Created by Alex on 06/04/2017.
 * This is a basic class that represents our system user
 * we will assume that the name is uniq
 */

public class person implements Comparable<person>{

        private String name;
        private String password;
        private Set<String> mailList;
        private Set<String> phones;

    public person(){

    }
        public person(String name ,String pass){
        this.name = name;
        this.password = pass;
    }
    public void addMail(String newMail){
        mailList.add(newMail);
    }
    public void addPhone(String newPhone){
        phones.add(newPhone);
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Set<String> getMailList() {
        return mailList;
    }

    public Set<String> getPhones() {
        return phones;
    }
    public void removeMail(String mailToRemove){
        mailList.remove(mailToRemove);
    }
    public void removePhone(String phoneToRemove){
        phones.remove(phoneToRemove);
    }

    @Override
    public int compareTo(@NonNull person o) {
        return this.name.compareTo(o.getName());
    }
}
