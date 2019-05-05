package persistance.model;

import Bank.OperationException;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class BankClient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(unique = true)
    private String PESEL;

    @Column(length = 512)
    private String password;

    private boolean premiumClient;

    @Transient
    private Float state = null;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<Operation> operations = new HashSet<>();

    public BankClient(String PESEL, boolean premiumClient) {
        this.PESEL = PESEL;
        this.premiumClient = premiumClient;
    }

    public BankClient() { }

    public String getPESEL() {
        return PESEL;
    }

    public void setPESEL(String PESEL) {
        this.PESEL = PESEL;
    }

    public boolean isPremiumClient() {
        return premiumClient;
    }

    public void setPremiumClient(boolean premiumClient) {
        this.premiumClient = premiumClient;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Set<Operation> getOperations() {
        return new HashSet<>(this.operations);
    }

    public void setOperations(Set<Operation> operations) {
        this.operations = operations;
    }

    public int getId() {
        return id;
    }

    public float getState(){
        if(this.state == null){
            calculateState();
        }
        return this.state;
    }

    public void putMoney(Operation operation) throws OperationException {
        if(operation.getValue() <= 0){
            throw new OperationException("Putted value must be positive.");
        }
        if(this.state == null){
            calculateState();
        }
        this.state += operation.getValue();
        this.operations.add(operation);
    }

    public void withdrawMoney(Operation operation) throws OperationException {
        if(operation.getValue() > 0){
            throw new OperationException("Putted value must be positive.");
        }
        if(this.state == null){
            calculateState();
        }
        if(Math.abs(operation.getValue()) > this.state){
            throw new OperationException("Tried to withdraw more money than current state");
        }
        this.state += operation.getValue();
        this.operations.add(operation);
    }

    private void calculateState(){
        this.state = (float)0;
        for(Operation operation : operations){
            this.state += operation.getValue();
        }
    }
}
