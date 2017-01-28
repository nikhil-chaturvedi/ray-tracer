import org.json.JSONObject;

/**
 * Created by Nikhil on 28/01/17.
 */
class Material {
    private double ka;
    private double kd;
    private double ks;
    private int roughness;

    Material(JSONObject material) {
        this.ka = material.getDouble("mat_ka");
        this.kd = material.getDouble("mat_kd");
        this.ks = material.getDouble("mat_ks");
        this.roughness = material.getInt("rough");
    }

    double getAmbientCoeff() {
        return this.ka;
    }

    double getDiffuseCoeff() {
        return this.kd;
    }

    double getSpecularCoeff() {
        return this.ks;
    }

    public int getRoughness() {
        return roughness;
    }
}
