import org.json.JSONObject;

/**
 * Created by Nikhil on 27/01/17.
 */
class Vector {
    private double x;
    private double y;
    private double z;

    public Vector() {
        this.x=0;
        this.y=0;
        this.z=0;
    }

    Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    Vector(Vector v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    Vector(JSONObject obj, String key) {
        this.x = obj.getDouble(key + "_x");
        this.y = obj.getDouble(key + "_y");
        this.z = obj.getDouble(key + "_z");
    }

    static double norm(Vector v) {
        return Math.sqrt((v.x * v.x) + (v.y * v.y) + (v.z * v.z));
    }

    static Vector scale(double scalar, Vector v) {
        return new Vector(scalar * v.x, scalar * v.y, scalar * v.z);
    }

    static Vector unit(Vector v) {
        return scale(1.0/norm(v), v);
    }

    static Vector add(Vector v1, Vector v2) {
        return new Vector(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    }

    static Vector subtract(Vector v1, Vector v2) {
        return new Vector(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }

    static double dot(Vector v1, Vector v2) {
        return ((v1.x * v2.x) + (v1.y * v2.y) + (v1.z * v2.z));
    }

    static Vector cross(Vector v1, Vector v2) {
        double x = (v1.y * v2.z) - (v1.z * v2.y);
        double y = (v1.z * v2.x) - (v1.x * v2.z);
        double z = (v1.x * v2.y) - (v1.y * v2.x);
        return new Vector(x, y, z);
    }

    static Vector transform(Vector v, double[][] transformation){
        double[] matrix1 = new double[4]; //pre-transformation in homogenous coordinates
        double[] matrix2 = new double[4]; //post-transformation in homogenous coordinates
        matrix1[0] = v.getX();
        matrix1[1] = v.getY();
        matrix1[2] = v.getZ();
        matrix1[3] = 1.0;


        for(int i=0; i<4; i++) {
            double term=0.0;
            for(int j=0;j<4;j++){
                term+= matrix1[j]*transformation[j][i];
            }
            matrix2[i]=term;
        }

        if(matrix2[4] != 1){ //check if last term 1 or not
            for(int i=0; i<3; i++){
                matrix2[i] *= 1.0/matrix2[4];
            }
        }

        return new Vector(matrix2[0],matrix2[1],matrix2[2]);
}

    void print() {
        System.out.println(x + " " + y + " " + z);
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    double getX() {
        return x;
    }

    double getY() {
        return y;
    }

    double getZ() {
        return z;
    }

    public static double[][] invert(double a[][])
    {
        int n = a.length;
        double x[][] = new double[n][n];
        double b[][] = new double[n][n];
        int index[] = new int[n];
        for (int i=0; i<n; ++i)
            b[i][i] = 1;

        // Transform the matrix into an upper triangle
        gaussian(a, index);

        // Update the matrix b[i][j] with the ratios stored
        for (int i=0; i<n-1; ++i)
            for (int j=i+1; j<n; ++j)
                for (int k=0; k<n; ++k)
                    b[index[j]][k]
                            -= a[index[j]][i]*b[index[i]][k];

        // Perform backward substitutions
        for (int i=0; i<n; ++i)
        {
            x[n-1][i] = b[index[n-1]][i]/a[index[n-1]][n-1];
            for (int j=n-2; j>=0; --j)
            {
                x[j][i] = b[index[j]][i];
                for (int k=j+1; k<n; ++k)
                {
                    x[j][i] -= a[index[j]][k]*x[k][i];
                }
                x[j][i] /= a[index[j]][j];
            }
        }
        return x;
    }

// Method to carry out the partial-pivoting Gaussian
// elimination.  Here index[] stores pivoting order.

    private static void gaussian(double a[][], int index[])
    {
        int n = index.length;
        double c[] = new double[n];

        // Initialize the index
        for (int i=0; i<n; ++i)
            index[i] = i;

        // Find the rescaling factors, one from each row
        for (int i=0; i<n; ++i)
        {
            double c1 = 0;
            for (int j=0; j<n; ++j)
            {
                double c0 = Math.abs(a[i][j]);
                if (c0 > c1) c1 = c0;
            }
            c[i] = c1;
        }

        // Search the pivoting element from each column
        int k = 0;
        for (int j=0; j<n-1; ++j)
        {
            double pi1 = 0;
            for (int i=j; i<n; ++i)
            {
                double pi0 = Math.abs(a[index[i]][j]);
                pi0 /= c[index[i]];
                if (pi0 > pi1)
                {
                    pi1 = pi0;
                    k = i;
                }
            }

            // Interchange rows according to the pivoting order
            int itmp = index[j];
            index[j] = index[k];
            index[k] = itmp;
            for (int i=j+1; i<n; ++i)
            {
                double pj = a[index[i]][j]/a[index[j]][j];

                // Record pivoting ratios below the diagonal
                a[index[i]][j] = pj;

                // Modify other elements accordingly
                for (int l=j+1; l<n; ++l)
                    a[index[i]][l] -= pj*a[index[j]][l];
            }
        }
    }

    public static double[][] transpose(double matrix[][]) {
        int a = matrix.length;
        double[][] transpose = new double[a][a];
        for(int i = 0; i<a; i++) {
            for(int j=0; j<a; j++) {
                transpose[i][j] = matrix[j][i];
            }
        }
        return transpose;
    }
}


