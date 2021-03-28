package ru.rt.crm.model;

@Table(tableName = "manager")
public class Manager {
    @Id
    private final Long id;
    private final String name;

    public Manager(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Manager(String name) {
        this.id = null;
        this.name = name;
    }

    public Manager() {
        this.id = null;
        this.name = null;
    }

    public Long getNo() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Manager{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
