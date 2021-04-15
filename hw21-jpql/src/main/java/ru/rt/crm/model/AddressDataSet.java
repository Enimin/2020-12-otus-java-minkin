package ru.rt.crm.model;

import javax.persistence.*;

@Entity
@Table(name = "address")
public class AddressDataSet {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
    @Column(name = "id")
    private Long id;

    @Column(name = "street")
    private String street;

    public AddressDataSet(){

    }

    public AddressDataSet(String street){
        this.id = null;
        this.street = street;
    }

    public AddressDataSet(Long id, String street){
        this.id = id;
        this.street = street;
    }

    @Override
    public AddressDataSet clone() {
        return new AddressDataSet(this.id, this.street);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        return "AddressDataSet{" +
                "id=" + id +
                ", street='" + street + '\'' +
                '}';
    }
}
