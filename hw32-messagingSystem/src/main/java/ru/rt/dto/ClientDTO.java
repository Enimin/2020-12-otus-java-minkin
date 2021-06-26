package ru.rt.dto;

import java.util.List;

public class ClientDTO {
//    private Long id;

    private String name;

    private String address;

    private String phones;

    public ClientDTO() {
    }

    public ClientDTO(String name, String address, String phones) {
//    public ClientDTO(Long id, String name, String address, String phones) {
//        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
    }

//    public Long getId() {
//        return id;
//    }

//    public void setId(Long id) {
//        this.id = id;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhones() {
        return phones;
    }

    public void setPhones(String phones) {
        this.phones = phones;
    }
}
