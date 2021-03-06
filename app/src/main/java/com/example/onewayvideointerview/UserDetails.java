package com.example.onewayvideointerview;

public class UserDetails {

    String username, email, phonenumber, subject, VideoLink, questionsRating, Experience, payment;

    public UserDetails() {
    }


    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getExperience() {
        return Experience;
    }

    public void setExperience(String experience) {
        Experience = experience;
    }

    public UserDetails(String videoLink) {
        VideoLink = videoLink;
    }

    public String getQuestionsRating() {
        return questionsRating;
    }

    public void setQuestionsRating(String questionsRating) {
        this.questionsRating = questionsRating;
    }

    public String getVideoLink() {
        return VideoLink;
    }

    public void setVideoLink(String videoLink) {
        VideoLink = videoLink;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
