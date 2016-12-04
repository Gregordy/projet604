package com.example.mo.projetIFT604;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
/**
 * PAS ENCORE UTILISÉ
 */
public class MapsWindow implements InfoWindowAdapter {
    LayoutInflater inflater = null;
    private TextView textViewTitle;

    public MapsWindow(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View v = inflater.inflate(R.layout.info_window, null);
        if (marker.getTitle() != null) {
            textViewTitle = (TextView) v.findViewById(R.id.textViewTitle);
            textViewTitle.setText(marker.getTitle());
        }
        return (v);
    }

    @Override
    public View getInfoContents(Marker marker) {
        return (null);
    }
}
