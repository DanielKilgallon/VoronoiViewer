package homework3;

/**
 *
 * @author Daniel Kilgallon
 */
public enum DistanceEquation {

    Euclidean, Manhattan, Modulus, Kilgallon;

    /**
     * Gets the equation corresponding to a given string of that equation name.
     *
     * @return the enum equation corresponding to the given index (or null if
     * index is invalid)
     */
    public static DistanceEquation get(String index) {
        switch (index) {
            case "Euclidean":
                return Euclidean;
            case "Manhattan":
                return Manhattan;
            case "Modulus":
                return Modulus;
            case "Kilgallon":
                return Kilgallon;
            default:
                return null;
        }
    }

    /**
     * @return the index of this direction
     */
    public String getDistanceNum() {
        switch (this) {
            case Euclidean:
                return "Euclidean";
            case Manhattan:
                return "Manhattan";
            case Modulus:
                return "Modulus";
            case Kilgallon:
                return "Kilgallon";
            default:
                return null; // unreachable
        }
    }
}
