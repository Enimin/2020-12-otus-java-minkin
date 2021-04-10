package ru.rt.crm.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "client")
public class Client implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(targetEntity = AddressDataSet.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id", updatable = false)
    private AddressDataSet address;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="client_id", updatable = false)
    private List<PhoneDataSet> phones = new ArrayList<>();

    public Client() {
    }

    public Client(String name) {
        this.id = null;
        this.name = name;
    }

    public Client(Long id, String name, AddressDataSet address, List<PhoneDataSet> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
    }

    @Override
    public Client clone() {
        return new Client(this.id,
                          this.name,
                          this.address.clone(),
                          new ArrayList<>(this.phones
                                              .stream()
                                              .map(PhoneDataSet::clone)
                                              .collect(Collectors.toList())));
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
        return new AddressDataSet(address.getId(), address.getStreet());
    }

    public void setAddress(AddressDataSet address) {
        this.address = address;
    }

    public List<PhoneDataSet> getPhones() {
        return new ArrayList<>(this.phones);
    }

    public void setPhones(List<PhoneDataSet> phones) {
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
