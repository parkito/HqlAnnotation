package ru.siksmfp.spring.hibernatehsql.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Artem Karnov @date 4/17/2018.
 * @email artem.karnov@t-systems.com
 */
@Entity
public class Data {

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "DATA_STRING")
    private String dataString;

    @Column(name = "DATA_DOUBLE")
    private Double dataDouble;

    @Column(name = "DATA_BOOLEAN")
    private boolean dataBoolean;

    public Data() {
    }

    public Data(String dataString, Double dataDouble, boolean dataBoolean) {
        this.dataString = dataString;
        this.dataDouble = dataDouble;
        this.dataBoolean = dataBoolean;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDataString() {
        return dataString;
    }

    public void setDataString(String dataString) {
        this.dataString = dataString;
    }

    public Double getDataDouble() {
        return dataDouble;
    }

    public void setDataDouble(Double dataDouble) {
        this.dataDouble = dataDouble;
    }

    public boolean isDataBoolean() {
        return dataBoolean;
    }

    public void setDataBoolean(boolean dataBoolean) {
        this.dataBoolean = dataBoolean;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Data data = (Data) o;

        if (dataBoolean != data.dataBoolean) return false;
        if (id != null ? !id.equals(data.id) : data.id != null) return false;
        if (dataString != null ? !dataString.equals(data.dataString) : data.dataString != null) return false;
        return dataDouble != null ? dataDouble.equals(data.dataDouble) : data.dataDouble == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (dataString != null ? dataString.hashCode() : 0);
        result = 31 * result + (dataDouble != null ? dataDouble.hashCode() : 0);
        result = 31 * result + (dataBoolean ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Data{" +
                "id=" + id +
                ", dataString='" + dataString + '\'' +
                ", dataDouble=" + dataDouble +
                ", dataBoolean=" + dataBoolean +
                '}';
    }
}
