package org.openmrs.module.kenyareg.form;

import ke.go.moh.oec.Person;
import org.openmrs.module.kenyaui.form.ValidatingCommandObject;
import org.springframework.validation.Errors;

import java.util.Date;

/**
 * Created by gitahi on 20/07/16.
 */
public class BasicSearchForm extends ValidatingCommandObject implements SearchForm {

    private int server;
    private String surname;
    private String firstName;
    private String middleName;
    private String otherName;
    private Date birthDate;

    public BasicSearchForm() {
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
        return person;
    }
}
