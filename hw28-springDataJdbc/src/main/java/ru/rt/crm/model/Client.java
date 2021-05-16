package ru.rt.crm.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.HashSet;
import java.util.Set;

@Table("client")
public class Client implements Cloneable {

    @Id
    private Long id;

    private String name;

    @MappedCollection(idColumn = "client_id")
    private AddressDataSet address;

    @MappedCollection(idColumn = "client_id")
    private Set<PhoneDataSet> phones;

    public Client() {
    }

    public Client(String name, AddressDataSet address, Set<PhoneDataSet> phones) {
        this.name = name;
        this.address = address;
        this.phones = phones;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressDataSet getAddress() {
        return address;
    }

    public void setAddress(AddressDataSet address) {
        this.address = address;
    }

    public Set<PhoneDataSet> getPhones() {
        return phones;
    }

    public void setPhones(Set<PhoneDataSet> phones) {
        this.phones = phones;
    }

    public void setPhone(PhoneDataSet phone) {
        phones.add(phone);
    }

    public void delPhone(PhoneDataSet phone) {
        phones.remove(phone);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address=" + address +
                ", phones=" + phones +
                '}';
    }
}
