package projet;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;


class MapsWindowInter implements InfoWindowAdapter{



    LayoutInflater inflater = null;


    MapsWindowInter(LayoutInflater inflater){
        this.inflater = inflater;

    }

    @Override
    public View getInfoContents(Marker marker) {
        View myContentsView = inflater.inflate(R.layout.info_window_inter, null);

        LatLng latLng = marker.getPosition();

        TextView tvParticipants = ((TextView)myContentsView.findViewById(R.id.participants_number));
        tvParticipants.setText("Participants à récupérer via script");
        TextView tvEquipments = ((TextView)myContentsView.findViewById(R.id.equipments));
        tvEquipments.setText("Équipements à déterminer via script");
        TextView tvGps = ((TextView)myContentsView.findViewById(R.id.gps_coordonate));
        tvGps.setText("Coordonnées GPS: "+latLng.latitude+","+latLng.longitude);
        TextView tvAddress = ((TextView)myContentsView.findViewById(R.id.address));
        tvAddress.setText("Addresse à récupérer via script ");


        return myContentsView;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        // TODO Auto-generated method stub
        return null;
    }

}
