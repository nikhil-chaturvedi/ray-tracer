import org.json.JSONObject;

/**
 * Created by Nikhil on 28/01/17.
 */
class Material {
    private double ka;
    private double kd;
    private double ks;

    private double krg;
    private double ktg;

    private int roughness;
    private double refIndex;

    Material(JSONObject material) {
        this.ka = material.getDouble("mat_ka");
        this.kd = material.getDouble("mat_kd");
        this.ks = material.getDouble("mat_ks");
        this.krg = material.getDouble("mat_krg");
        this.ktg = material.getDouble("mat_ktg");
        this.roughness = material.getInt("mat_rough");
        this.refIndex = material.getDouble("mat_index");
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

    double getReflectiveCoeff() { return this.krg; }

    double getRefractiveCoeff() { return this.ktg; }

    int getRoughness() {
        return this.roughness;
    }

    double getRefractiveIndex() { return this.refIndex; }
}
