package Data;


import java.io.Serializable;

public class tag implements Serializable {
    private String name;
    private String value;

    private static final long serialVersionUID = 1L;
    /**
     * constructor for tag
     *
     * @param myName String
     * @param myVal  String
     */
    public tag(String myName, String myVal) {
        name = myName;
        value = myVal;
    }

    /**
     * gets name value
     *
     * @return String name
     */

    public String getName() {
        return name;
    }

    /**
     * gets Value value
     *
     * @return String Value
     */
    public String getValue() {
        return value;

    }

    /**
     * gets toString
     *
     * @return String
     */
    public String toString() {
        return name + ":" + value;
    }

    /**
     * checks tag equality
     *
     * @param myTag tag
     * @return Boolean
     */
    public boolean equals(tag myTag) {
        return name.equalsIgnoreCase(myTag.getName()) && value.equalsIgnoreCase(myTag.getValue());
    }
}
