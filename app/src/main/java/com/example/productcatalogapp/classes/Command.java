package com.example.productcatalogapp.classes;

import java.util.ArrayList;

public class Command {

    private Integer id;
    private Client client;
    private Float total;
    public ArrayList<CommandLine> commandLines;

    public Command() {
        commandLines = new ArrayList<CommandLine>();
    }

    public Command(Integer id, Client client, Float total, boolean isSync) {
        this.setId(id);
        this.setClient(client);
        this.setTotal(total);
        this.setSync(isSync);
        commandLines = new ArrayList<CommandLine>();
    }

    public Command(Client client, Float total, boolean isSync) {
        this.setClient(client);
        this.setTotal(total);
        this.setSync(isSync);
        commandLines = new ArrayList<CommandLine>();
    }

    private boolean isSync;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public boolean isSync() {
        return isSync;
    }

    public void setSync(boolean sync) {
        isSync = sync;
    }

    @Override
    public String toString() {
        return "Command{" +
                "id=" + id +
                ", client=" + client +
                ", total=" + total +
                ", commandLines=" + commandLines.size() +
                ", isSync=" + isSync +
                '}';
    }
}
