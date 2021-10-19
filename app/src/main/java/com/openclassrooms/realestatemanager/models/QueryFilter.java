package com.openclassrooms.realestatemanager.models;

import java.util.List;

public class QueryFilter {

    String type;
    double minPrice, maxPrice;
    boolean isSold;
    double minSurface, maxSurface;
    int minRooms, maxRooms, minBathrooms, maxBathrooms, minBedrooms, maxBedrooms;
    List<String> pointsOfInterest;
    String city;

    public static class Builder {
        String type;
        double minPrice, maxPrice;
        boolean isSold;
        double minSurface, maxSurface;
        int minRooms, maxRooms, minBathrooms, maxBathrooms, minBedrooms, maxBedrooms;
        List<String> pointsOfInterest;
        String city;

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder minPrice(double minPrice) {
            this.minPrice = minPrice;
            return this;
        }

        public Builder maxPrice(double maxPrice) {
            this.maxPrice = maxPrice;
            return this;
        }

        public Builder minSurface(double minSurface) {
            this.minSurface = minSurface;
            return this;
        }


        public Builder maxSurface(double maxSurface) {
            this.maxSurface = maxSurface;
            return this;
        }

        public Builder isSold(Boolean isSold) {
            this.isSold = isSold;
            return this;
        }


        public Builder minRooms(int minRooms) {
            this.minRooms = minRooms;
            return this;
        }

        public Builder maxRooms(int maxRooms) {
            this.maxRooms = maxRooms;
            return this;
        }

        public Builder minBathrooms(int minBathrooms) {
            this.minBathrooms = minBathrooms;
            return this;
        }

        public Builder maxBathrooms(int maxBathrooms) {
            this.maxBathrooms = maxBathrooms;
            return this;
        }

        public Builder minBedrooms(int minBedrooms) {
            this.minBedrooms = minBedrooms;
            return this;
        }

        public Builder maxBedrooms(int maxBedrooms) {
            this.maxBedrooms = maxBedrooms;
            return this;
        }

        public Builder pointsOfInterest(List<String> pointsOfInterest) {
            this.pointsOfInterest = pointsOfInterest;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }


        public QueryFilter build() {
            QueryFilter queryFilter = new QueryFilter();
            queryFilter.type = this.type;
            queryFilter.minPrice = this.minPrice;
            queryFilter.maxPrice = this.maxPrice;
            queryFilter.isSold = this.isSold;
            queryFilter.minSurface = this.minSurface;
            queryFilter.maxSurface = this.maxSurface;
            queryFilter.minRooms = this.minRooms;
            queryFilter.maxRooms = this.maxRooms;
            queryFilter.minBathrooms = this.minBathrooms;
            queryFilter.maxBathrooms = this.maxBathrooms;
            queryFilter.minBedrooms = this.minBedrooms;
            queryFilter.maxBedrooms = this.maxBedrooms;
            queryFilter.pointsOfInterest = this.pointsOfInterest;
            queryFilter.city = this.city;

            return queryFilter;
        }
    }
}