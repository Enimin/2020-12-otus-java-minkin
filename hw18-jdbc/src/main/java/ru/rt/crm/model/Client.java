package ru.rt.crm.model;

@Table(tableName = "client")
public class Client {
    @Id
    private final Long id;
    private final String name;

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Client(String name) {
        this.id = null;
        this.name = name;
    }

    public Client() {
        this.id = null;
        this.name = null;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
