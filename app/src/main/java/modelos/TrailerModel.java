package modelos;

import java.io.Serializable;

/**
 * Created by Carlos Chica.
 */

public class TrailerModel implements Serializable {

    private String key;
    private String type;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
