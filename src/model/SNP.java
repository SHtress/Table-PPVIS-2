package model;

public class SNP {
    private String surname;
    private String name;
    private String patronym;

    public SNP(String alignForm) {
        String[] splitForm = alignForm.split("\\s+");
        if (splitForm[0] != null) {
            surname = splitForm[0];
        } else {
            surname = "";
        }
        if (splitForm[1] != null) {
            name = splitForm[1];
        } else {
            name = "";
        }
        if (splitForm[2] != null) {
            patronym = splitForm[2];
        } else {
            patronym = "";
        }
    }

    public SNP(String surname, String name, String patronym) {
        this.surname = surname;
        this.name = name;
        this.patronym = patronym;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getPatronym() {
        return patronym;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPatronym(String patronym) {
        this.patronym = patronym;
    }
}
