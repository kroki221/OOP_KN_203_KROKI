package teleg.service;

public interface IGeoService {
    double getLatitude(String city);
    double getLongitude(String city);
}
