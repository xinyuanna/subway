import java.util.HashSet;
import java.util.Set;

public class Station {
    private String name;
    private Set<String> lines;

    public Station(String name) {
        this.name = name;
        this.lines = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public Set<String> getLines() {
        return lines;
    }

    public void addLine(String line) {
        lines.add(line);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return name.equals(station.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "<" + name + ", <" + String.join("、", lines) + ">>";
    }
}
