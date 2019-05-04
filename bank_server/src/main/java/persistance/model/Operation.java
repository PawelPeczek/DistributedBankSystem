package persistance.model;

import javax.persistence.*;

@Entity
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Float value;

    @ManyToOne
    private BankClient client;

    public Operation(Float value, BankClient client) {
        this.value = value;
        this.client = client;
    }

    public Operation() {
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public BankClient getClient() {
        return client;
    }

    public void setClient(BankClient client) {
        this.client = client;
    }

    public int getId() {
        return id;
    }
}
