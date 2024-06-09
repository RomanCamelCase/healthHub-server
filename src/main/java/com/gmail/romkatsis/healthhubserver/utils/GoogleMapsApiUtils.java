package com.gmail.romkatsis.healthhubserver.utils;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.TextSearchRequest;
import com.google.maps.model.PlacesSearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GoogleMapsApiUtils {

    private final GeoApiContext apiContext;

    @Autowired
    public GoogleMapsApiUtils(@Value("${google.maps.api-key}") String apiKey) {
        this.apiContext = new GeoApiContext.Builder().apiKey(apiKey).build();
    }

    public String getPlaceIdByAddress(String countryCode, String city, String address) {
        PlacesSearchResponse placesSearchResponse = getPlaceByAddress(countryCode, city, address);

        return placesSearchResponse != null ? placesSearchResponse.results[0].placeId : null;
    }

    private PlacesSearchResponse getPlaceByAddress(String countryCode, String city, String address) {
        TextSearchRequest textSearchRequest = PlacesApi
                .textSearchQuery(
                    apiContext,
                    "%s, %s".formatted(city, address)
                )
                .language("uk")
                .region(countryCode);
        try {
            return textSearchRequest.await();
        } catch (Exception e) {
            return null;
        }
    }
}
