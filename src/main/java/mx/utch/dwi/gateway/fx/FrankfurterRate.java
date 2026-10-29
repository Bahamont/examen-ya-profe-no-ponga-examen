package mx.utch.dwi.gateway.fx;

public record FrankfurterRate(boolean ok, Double rate, String date, String source, String error) {

    public static FrankfurterRate success(double rate, String date) {
        return new FrankfurterRate(true, rate, date, "Frankfurter External API", null);
    }

    public static FrankfurterRate failure(String error) {
        return new FrankfurterRate(false, null, null, "Frankfurter External API", error);
    }
}
