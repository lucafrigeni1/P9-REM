package com.openclassrooms.realestatemanager.models;

import java.util.Date;

public class RealEstate {

    //list?
    String type;

    //list?
    String pointOfInterest;

    double price;
    double surface;
    int rooms;
    String descriptions;
    String adress;
    boolean isSold;
    Date recordDate;
    Date saleDate;
    String estateAgent;

    //Au moins une photo, avec une description associée.
    //Vous devez gérer le cas où plusieurs photos sont présentes pour un bien !
    //La photo peut être récupérée depuis la galerie photos du téléphone, ou prise directement avec l'équipement ;
}
