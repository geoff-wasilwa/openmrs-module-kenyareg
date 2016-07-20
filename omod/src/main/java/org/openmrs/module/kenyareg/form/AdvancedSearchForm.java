package org.openmrs.module.kenyareg.form;

import ke.go.moh.oec.Person;
import org.openmrs.module.kenyaui.form.ValidatingCommandObject;
import org.springframework.validation.Errors;

import java.util.Date;

/**
 * Created by gitahi on 20/07/16.
 */
public class AdvancedSearchForm extends ValidatingCommandObject implements SearchForm {

    private int server;
    private String surname;
    private String firstName;
    private String middleName;
    private String otherName;
    private Date birthDate;
    private String fathersFirstName;
    private String fathersMiddleName;
    private String fathersLastName;
    private String mothersFirstName;
    private String mothersMiddleName;
    private String mothersLastName;
    private String village;

    public AdvancedSearchForm() {
    }

    @Override
    public int getServer() {
        return server;
    }

    public void setServer(int server) {
        this.server = server;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getFathersFirstName() {
        return fathersFirstName;
    }

    public void setFathersFirstName(String fathersFirstName) {
        this.fathersFirstName = fathersFirstName;
    }

    public String getFathersMiddleName() {
        return fathersMiddleName;
    }

    public void setFathersMiddleName(String fathersMiddleName) {
        this.fathersMiddleName = fathersMiddleName;
    }

    public String getFathersLastName() {
        return fathersLastName;
    }

    public void setFathersLastName(String fathersLastName) {
        this.fathersLastName = fathersLastName;
    }

    public String getMothersFirstName() {
        return mothersFirstName;
    }

    public void setMothersFirstName(String mothersFirstName) {
        this.mothersFirstName = mothersFirstName;
    }

    public String getMothersMiddleName() {
        return mothersMiddleName;
    }

    public void setMothersMiddleName(String mothersMiddleName) {
        this.mothersMiddleName = mothersMiddleName;
    }

    public String getMothersLastName() {
        return mothersLastName;
    }

    public void setMothersLastName(String mothersLastName) {
        this.mothersLastName = mothersLastName;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    @Override
    public void validate(Object o, Errors errors) {
        requireAny(errors, "surname", "firstName", "middleName");
    }

    @Override
    public Person getPerson() {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(surname);
        person.setMiddleName(middleName);
        person.setOtherName(otherName);
        person.setBirthdate(birthDate);
        person.setFathersFirstName(fathersFirstName);
        person.setFathersMiddleName(fathersMiddleName);
        person.setFathersLastName(fathersLastName);
        person.setMothersFirstName(mothersFirstName);
        person.setMothersMiddleName(mothersMiddleName);
        person.setMothersLastName(mothersLastName);
        person.setVillageName(village);
        return person;
    }
}
