package com.bounouamustapha.localisationwifistage

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import android.preference.PreferenceManager
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.api.IMapController
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import org.osmdroid.views.overlay.ScaleBarOverlay
import android.location.LocationManager
import org.osmdroid.util.LocationUtils.getLastKnownLocation
import org.osmdroid.views.overlay.OverlayItem
import android.text.method.TextKeyListener.clear
import org.jetbrains.anko.toast
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay












class MainActivity : AppCompatActivity() {



    lateinit var overlayItemArray: MutableList<OverlayItem>
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val ctx = applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        //setting this before the layout is inflated is a good idea
        //it 'should' ensure that the map has a writable location for the map cache, even without permissions
        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
        //see also StorageUtils
        //note, the load method also sets the HTTP User Agent to your application's package name, abusing osm's tile servers will get you banned based on this string
        overlayItemArray = ArrayList()
        //inflate and create the map
        setContentView(R.layout.activity_main)
        map.setTileSource(TileSourceFactory.MAPNIK)

        map.setBuiltInZoomControls(true)
        map.setMultiTouchControls(true)

        var mapController = map.controller
        mapController.setZoom(4)




        var locationProvider=GpsMyLocationProvider(ctx)
        locationProvider.clearLocationSources()
        locationProvider.locationSources.add(LocationManager.NETWORK_PROVIDER)
        toast("Les sources de  localisation sont :"+locationProvider.locationSources)
        // locationProvider.startLocationProvider { location, source ->  mapController.setCenter(GeoPoint(location.latitude, location.longitude)) }

        var mLocationOverlay = MyLocationNewOverlay(locationProvider, map)
        mLocationOverlay.enableMyLocation()
        map.getOverlays().add(mLocationOverlay)

//        mapController.setCenter(GeoPoint(latitude, longitude))

        var mRotationGestureOverlay = RotationGestureOverlay(ctx, map)
        mRotationGestureOverlay.setEnabled(true)
        map.setMultiTouchControls(true)
        map.getOverlays().add(mRotationGestureOverlay)

    }

    public override fun onResume() {
        super.onResume()
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume() //needed for compass, my location overlays, v6.0.0 and up
    }

    public override fun onPause() {
        super.onPause()
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        super.onResume();

    }




}
