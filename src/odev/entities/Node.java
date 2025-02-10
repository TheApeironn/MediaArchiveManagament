package odev.entities;

import java.util.List;

public class Node {
    private String platform;
    private List<String> countries;

    public Node(String platform, List<String> countries) {
        this.platform = platform;
        this.countries = countries;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }
}
