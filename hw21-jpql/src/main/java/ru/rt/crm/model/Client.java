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

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private AddressDataSet address;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<PhoneDataSet> phones;

    public Client() {
    }

    public Client(String name) {
        this(null, name, null, new ArrayList<>());
    }

    public Client(Long id, String name, AddressDataSet address) {
        this(id, name, address, new ArrayList<>());
    }

    public Client(Long id, String name, AddressDataSet address, List<PhoneDataSet> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
    }

    @Override
    public Client clone() {
        var cloneClient = new Client(this.id,
                                     this.name,
                                     this.address.clone());
        cloneClient.setPhones(this.phones
                                  .stream()
                                  .map(phone -> phone.clone(cloneClient))
                                  .collect(Collectors.toList()));
        return cloneClient;
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

    public void addPhone(PhoneDataSet phone) {
        phone.setClient(this);
        phones.add(phone);
    }

    public void delPhone(PhoneDataSet phone) {
        phone.setClient(null);
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
