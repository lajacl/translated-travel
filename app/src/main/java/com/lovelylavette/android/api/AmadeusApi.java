package com.lovelylavette.android.api;

import android.os.AsyncTask;
import android.util.Log;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Airline;
import com.amadeus.resources.FlightOffer;
import com.amadeus.resources.HotelOffer;
import com.amadeus.resources.Location;
import com.amadeus.resources.PointOfInterest;
import com.google.android.gms.maps.model.LatLng;
import com.lovelylavette.android.BuildConfig;
import com.lovelylavette.android.model.Trip;
import com.lovelylavette.android.util.ResponseListener;

import java.text.SimpleDateFormat;
import java.util.Locale;

public final class AmadeusApi {
    private static final String TAG = "AmadeusApi";
    private static final Amadeus amadeus = Amadeus.builder(BuildConfig.AmadeusApiKey,
            BuildConfig.AmadeusApiSecret).build();


    public static final class findNearestRelevantAirports extends AsyncTask<LatLng, Void, Location[]> {
        ResponseListener.Locations listener;

        public void setOnResponseListener(ResponseListener.Locations listener) {
            this.listener = listener;
        }

        @Override
        protected Location[] doInBackground(LatLng... latLngs) {
            Location[] airports = null;

            try {
                airports = amadeus.referenceData.locations.airports.get(Params
                        .with("latitude", latLngs[0].latitude)
                        .and("longitude", latLngs[0].longitude)
                        .and("radius", 100)
                        .and("page[limit]", 5));

            } catch (ResponseException e) {
                e.printStackTrace();
            }
            return airports;
        }

        @Override
        protected void onPostExecute(Location[] airports) {
            super.onPostExecute(airports);
            listener.onResponseReceive(airports);
        }
    }

    public static final class findLowFareFlights extends AsyncTask<Trip, Void, FlightOffer[]> {
        ResponseListener.FlightOffers listener;

        public void setOnResponseListener(ResponseListener.FlightOffers listener) {
            this.listener = listener;
        }

        @Override
        protected FlightOffer[] doInBackground(Trip... trips) {
            FlightOffer[] flightOffers = null;
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd", Locale.getDefault());

            try {
                Trip trip = trips[0];

                Params params = Params.with("origin", trip.getOriginAirport())
                        .and("destination", trip.getDestinationAirport())
                        .and("departureDate", sdf.format(trip.getDepartureDate().getTime()))
                        .and("currency", "USD");

                if(trip.isRoundTrip()) {
                    params.and("returnDate", sdf.format(trip.getReturnDate().getTime()));
                }

//                TODO Add option for nonstop flights only
                /*if(trip.flightNonstop) {
                    params.and("nonStop", true);
                }*/

                flightOffers = amadeus.shopping.flightOffers.get(params);

                //TODO Add pagination
                 /*FlightOffer[] flightOffersNext = (FlightOffer[])amadeus.next(flightOffers[0]);*/

            } catch (ResponseException e) {
                e.printStackTrace();
            }
            return flightOffers;
        }

        @Override
        protected void onPostExecute(FlightOffer[] flightOffers) {
            super.onPostExecute(flightOffers);
            listener.onResponseReceive(flightOffers);
        }
    }

    public static final class findAirlines extends AsyncTask<String, Void, Airline[]> {
        ResponseListener.Airlines listener;

        public void setOnResponseListener(ResponseListener.Airlines listener) {
            this.listener = listener;
        }

        @Override
        protected Airline[] doInBackground(String... strings) {
            Airline[] airlines = null;

            try {
                String airlineString = strings[0];
                airlines = amadeus.referenceData.airlines.get(Params
                        .with("airlineCodes", airlineString));

            } catch (ResponseException e) {
                e.printStackTrace();
            }
            return airlines;
        }

        @Override
        protected void onPostExecute(Airline[] airlines) {
            super.onPostExecute(airlines);
            listener.onResponseReceive(airlines);
        }
    }

    public static final class findHotels extends AsyncTask<LatLng, Void, HotelOffer[]> {

        @Override
        protected HotelOffer[] doInBackground(LatLng... latLngs) {
            HotelOffer[] hotelOffers = null;

            try {
                hotelOffers = amadeus.shopping.hotelOffers.get(Params
                        .with("latitude", latLngs[0].latitude)
                        .and("longitude", latLngs[0].longitude)
                        .and("checkInDate", "2019-08-30")
                        .and("checkOutDate", "2019-09-29"));

            } catch (ResponseException e) {
                e.printStackTrace();
            }
            return hotelOffers;
        }

        @Override
        protected void onPostExecute(HotelOffer[] hotelOffers) {
            super.onPostExecute(hotelOffers);
            if(hotelOffers != null && hotelOffers.length > 0) {
                Log.i(TAG, hotelOffers[0].toString());
            }
        }
    }

    public static final class findPointsOfInterest extends AsyncTask<LatLng, Void, PointOfInterest[]> {

        @Override
        protected PointOfInterest[] doInBackground(LatLng... latLngs) {
            PointOfInterest[] pointsOfInterest = null;

            try {
                pointsOfInterest = amadeus.referenceData.locations.pointsOfInterest.get(Params
                        .with("latitude", latLngs[0].latitude)
                        .and("longitude", latLngs[0].longitude));

            } catch (ResponseException e) {
                e.printStackTrace();
            }
            return pointsOfInterest;
        }

        @Override
        protected void onPostExecute(PointOfInterest[] pointsOfInterest) {
            super.onPostExecute(pointsOfInterest);
            if(pointsOfInterest != null && pointsOfInterest.length > 0) {
                Log.i(TAG, pointsOfInterest[0].toString());
            }
        }
    }
}
